package com.example.notecalculator.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

/**
 * DTO (Data Transfer Object) used to receive data from the frontend
 * when a user submits amount + denomination for calculation.
 *
 * We use a separate DTO instead of the Entity directly so that the
 * client only sends the fields we need (amount, denomination) and
 * cannot set fields like "id" or "calculatedAt" themselves.
 */
public class CalculationRequest {

    @NotNull(message = "Amount is required")
    @DecimalMin(value = "0.01", message = "Amount must be greater than zero")
    private BigDecimal amount;

    @NotNull(message = "Denomination is required")
    private Integer denomination;

    public CalculationRequest() {
    }

    public CalculationRequest(BigDecimal amount, Integer denomination) {
        this.amount = amount;
        this.denomination = denomination;
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
}
