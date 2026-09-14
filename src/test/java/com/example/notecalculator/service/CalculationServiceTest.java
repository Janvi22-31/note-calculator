package com.example.notecalculator.service;

import com.example.notecalculator.dto.CalculationRequest;
import com.example.notecalculator.dto.CalculationResponse;
import com.example.notecalculator.entity.Calculation;
import com.example.notecalculator.exception.InvalidDenominationException;
import com.example.notecalculator.repository.CalculationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

/**
 * Unit tests for the core note-calculation business logic.
 * We mock the repository so these tests do NOT need a real MySQL
 * database to run - they test only the calculation logic.
 */
class CalculationServiceTest {

    private CalculationRepository calculationRepository;
    private CalculationService calculationService;

    @BeforeEach
    void setUp() {
        calculationRepository = Mockito.mock(CalculationRepository.class);
        calculationService = new CalculationService(calculationRepository);

        // Whenever save() is called, just return whatever entity was passed in
        // but pretend the database assigned it an ID of 1.
        when(calculationRepository.save(any(Calculation.class))).thenAnswer(invocation -> {
            Calculation c = invocation.getArgument(0);
            c.setId(1L);
            return c;
        });
    }

    @Test
    void testValidCalculation() {
        // 5000 / 500 = 10 notes, remainder 0
        CalculationRequest request = new CalculationRequest(BigDecimal.valueOf(5000), 500);
        CalculationResponse response = calculationService.calculate(request);

        assertEquals(10, response.getNumberOfNotes());
        assertEquals(0, response.getRemainingAmount().compareTo(BigDecimal.ZERO));
    }

    @Test
    void testAmountSmallerThanDenomination() {
        // 50 / 500 = 0 notes, remainder 50
        CalculationRequest request = new CalculationRequest(BigDecimal.valueOf(50), 500);
        CalculationResponse response = calculationService.calculate(request);

        assertEquals(0, response.getNumberOfNotes());
        assertEquals(0, response.getRemainingAmount().compareTo(BigDecimal.valueOf(50)));
    }

    @Test
    void testAmountExactlyDivisible() {
        // 100 / 100 = 1 note, remainder 0
        CalculationRequest request = new CalculationRequest(BigDecimal.valueOf(100), 100);
        CalculationResponse response = calculationService.calculate(request);

        assertEquals(1, response.getNumberOfNotes());
        assertEquals(0, response.getRemainingAmount().compareTo(BigDecimal.ZERO));
    }

    @Test
    void testAmountWithRemainder() {
        // 530 / 500 = 1 note, remainder 30
        CalculationRequest request = new CalculationRequest(BigDecimal.valueOf(530), 500);
        CalculationResponse response = calculationService.calculate(request);

        assertEquals(1, response.getNumberOfNotes());
        assertEquals(0, response.getRemainingAmount().compareTo(BigDecimal.valueOf(30)));
    }

    @Test
    void testZeroAmountIsRejectedByValidationLayer() {
        // Note: @DecimalMin on the DTO blocks this at the HTTP layer via @Valid.
        // Here we confirm the service still behaves sanely if given zero directly,
        // producing zero notes and zero remainder (defense in depth).
        CalculationRequest request = new CalculationRequest(BigDecimal.ZERO, 500);
        CalculationResponse response = calculationService.calculate(request);

        assertEquals(0, response.getNumberOfNotes());
        assertEquals(0, response.getRemainingAmount().compareTo(BigDecimal.ZERO));
    }

    @Test
    void testNegativeAmountProducesNegativeNotes() {
        // Negative amounts are rejected by @DecimalMin(0.01) before reaching the
        // service in real requests. This test documents the service's raw behavior.
        CalculationRequest request = new CalculationRequest(BigDecimal.valueOf(-500), 500);
        CalculationResponse response = calculationService.calculate(request);

        assertTrue(response.getNumberOfNotes() < 0);
    }

    @Test
    void testInvalidDenominationThrowsException() {
        // 300 is not a real currency note denomination
        CalculationRequest request = new CalculationRequest(BigDecimal.valueOf(1000), 300);

        assertThrows(InvalidDenominationException.class, () -> calculationService.calculate(request));
    }
}
