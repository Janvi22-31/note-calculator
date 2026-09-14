package com.example.notecalculator.service;

import com.example.notecalculator.dto.CalculationRequest;
import com.example.notecalculator.dto.CalculationResponse;
import com.example.notecalculator.entity.Calculation;
import com.example.notecalculator.exception.CalculationNotFoundException;
import com.example.notecalculator.exception.InvalidDenominationException;
import com.example.notecalculator.repository.CalculationRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Service layer -> contains the actual business logic.
 * The Controller does NOT do calculations itself; it simply calls
 * this service and returns whatever the service gives back.
 */
@Service
public class CalculationService {

    // Only these denominations are allowed, matching real Indian currency notes
    private static final Set<Integer> ALLOWED_DENOMINATIONS = Set.of(10, 50, 100, 200, 500, 2000);

    private final CalculationRepository calculationRepository;

    public CalculationService(CalculationRepository calculationRepository) {
        this.calculationRepository = calculationRepository;
    }

    /**
     * Performs the note calculation, saves it to the database,
     * and returns the result.
     */
    public CalculationResponse calculate(CalculationRequest request) {
        Integer denomination = request.getDenomination();
        BigDecimal amount = request.getAmount();

        if (!ALLOWED_DENOMINATIONS.contains(denomination)) {
            throw new InvalidDenominationException(
                    "Denomination must be one of " + ALLOWED_DENOMINATIONS);
        }

        // Core calculation logic (same idea as: amount / denomination and amount % denomination)
        BigDecimal denominationValue = BigDecimal.valueOf(denomination);
        BigDecimal[] divideAndRemainder = amount.divideAndRemainder(denominationValue);

        int numberOfNotes = divideAndRemainder[0].setScale(0, RoundingMode.DOWN).intValue();
        BigDecimal remainingAmount = divideAndRemainder[1];

        Calculation calculation = new Calculation(
                amount,
                denomination,
                numberOfNotes,
                remainingAmount,
                LocalDateTime.now()
        );

        Calculation saved = calculationRepository.save(calculation);

        return toResponse(saved);
    }

    /** Returns all past calculations, most recent first. */
    public List<CalculationResponse> getAllCalculations() {
        return calculationRepository.findAll()
                .stream()
                .sorted((a, b) -> b.getCalculatedAt().compareTo(a.getCalculatedAt()))
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    /** Returns one calculation by its ID, or throws if it doesn't exist. */
    public CalculationResponse getCalculationById(Long id) {
        Calculation calculation = calculationRepository.findById(id)
                .orElseThrow(() -> new CalculationNotFoundException("Calculation not found with id: " + id));
        return toResponse(calculation);
    }

    /** Deletes one calculation by ID. */
    public void deleteCalculationById(Long id) {
        if (!calculationRepository.existsById(id)) {
            throw new CalculationNotFoundException("Calculation not found with id: " + id);
        }
        calculationRepository.deleteById(id);
    }

    /** Deletes all calculation history. */
    public void deleteAllCalculations() {
        calculationRepository.deleteAll();
    }

    // Helper method to convert an Entity into a DTO for the API response
    private CalculationResponse toResponse(Calculation calculation) {
        return new CalculationResponse(
                calculation.getId(),
                calculation.getAmount(),
                calculation.getDenomination(),
                calculation.getNumberOfNotes(),
                calculation.getRemainingAmount(),
                calculation.getCalculatedAt()
        );
    }
}
