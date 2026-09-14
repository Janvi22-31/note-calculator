package com.example.notecalculator.controller;

import com.example.notecalculator.dto.CalculationRequest;
import com.example.notecalculator.dto.CalculationResponse;
import com.example.notecalculator.service.CalculationService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller layer -> the entry point for all HTTP requests.
 * It receives requests from the frontend, delegates the real work
 * to the Service layer, and returns responses as JSON.
 *
 * @CrossOrigin allows the frontend (running on a different port,
 * e.g. via Live Server on port 5500) to call this API without
 * being blocked by the browser's CORS policy.
 */
@RestController
@RequestMapping("/api/calculations")
@CrossOrigin(origins = "*")
public class CalculationController {

    private final CalculationService calculationService;

    public CalculationController(CalculationService calculationService) {
        this.calculationService = calculationService;
    }

    // POST /api/calculations -> create a new calculation
    @PostMapping
    public ResponseEntity<CalculationResponse> calculate(@Valid @RequestBody CalculationRequest request) {
        CalculationResponse response = calculationService.calculate(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    // GET /api/calculations -> get full history
    @GetMapping
    public ResponseEntity<List<CalculationResponse>> getAllCalculations() {
        List<CalculationResponse> calculations = calculationService.getAllCalculations();
        return new ResponseEntity<>(calculations, HttpStatus.OK);
    }

    // GET /api/calculations/{id} -> get one calculation
    @GetMapping("/{id}")
    public ResponseEntity<CalculationResponse> getCalculationById(@PathVariable Long id) {
        CalculationResponse response = calculationService.getCalculationById(id);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // DELETE /api/calculations/{id} -> delete one calculation
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCalculationById(@PathVariable Long id) {
        calculationService.deleteCalculationById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    // DELETE /api/calculations -> clear all history
    @DeleteMapping
    public ResponseEntity<Void> deleteAllCalculations() {
        calculationService.deleteAllCalculations();
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
