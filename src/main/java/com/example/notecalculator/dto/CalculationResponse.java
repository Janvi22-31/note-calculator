package com.example.notecalculator.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * DTO used to send calculation results back to the frontend as JSON.
 * Keeping this separate from the Entity means we control exactly what
 * shape of JSON goes out over the API.
 */
public class CalculationResponse {

    private Long id;
    private BigDecimal amount;
    private Integer denomination;
    private Integer numberOfNotes;
    private BigDecimal remainingAmount;
    private LocalDateTime calculatedAt;

    public CalculationResponse() {
    }

    public CalculationResponse(Long id, BigDecimal amount, Integer denomination,
                                Integer numberOfNotes, BigDecimal remainingAmount,
                                LocalDateTime calculatedAt) {
        this.id = id;
        this.amount = amount;
        this.denomination = denomination;
        this.numberOfNotes = numberOfNotes;
        this.remainingAmount = remainingAmount;
        this.calculatedAt = calculatedAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Integer getDenomination() {
        return denomination;
    }

    public void setDenomination(Integer denomination) {
        this.denomination = denomination;
    }

    public Integer getNumberOfNotes() {
        return numberOfNotes;
    }

    public void setNumberOfNotes(Integer numberOfNotes) {
        this.numberOfNotes = numberOfNotes;
    }

    public BigDecimal getRemainingAmount() {
        return remainingAmount;
    }

    public void setRemainingAmount(BigDecimal remainingAmount) {
        this.remainingAmount = remainingAmount;
    }

    public LocalDateTime getCalculatedAt() {
        return calculatedAt;
    }

    public void setCalculatedAt(LocalDateTime calculatedAt) {
        this.calculatedAt = calculatedAt;
    }
}
