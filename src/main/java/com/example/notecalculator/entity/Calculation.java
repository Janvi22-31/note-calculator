package com.example.notecalculator.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Entity class -> represents one row in the "calculations" table.
 * Every time a user performs a note calculation, one row like this
 * gets saved to the MySQL database.
 */
@Entity
@Table(name = "calculations")
public class Calculation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "amount", nullable = false)
    private BigDecimal amount;

    @Column(name = "denomination", nullable = false)
    private Integer denomination;

    @Column(name = "number_of_notes", nullable = false)
    private Integer numberOfNotes;

    @Column(name = "remaining_amount", nullable = false)
    private BigDecimal remainingAmount;

    @Column(name = "calculated_at", nullable = false)
    private LocalDateTime calculatedAt;

    // JPA requires a no-argument constructor
    public Calculation() {
    }

    public Calculation(BigDecimal amount, Integer denomination, Integer numberOfNotes,
                        BigDecimal remainingAmount, LocalDateTime calculatedAt) {
        this.amount = amount;
        this.denomination = denomination;
        this.numberOfNotes = numberOfNotes;
        this.remainingAmount = remainingAmount;
        this.calculatedAt = calculatedAt;
    }

    // Getters and setters

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
