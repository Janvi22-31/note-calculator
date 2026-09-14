package com.example.notecalculator.repository;

import com.example.notecalculator.entity.Calculation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository layer -> talks directly to the MySQL database.
 *
 * By extending JpaRepository, Spring Data JPA automatically gives us
 * methods like save(), findAll(), findById(), deleteById(), deleteAll()
 * without us having to write any SQL ourselves.
 */
@Repository
public interface CalculationRepository extends JpaRepository<Calculation, Long> {
    // No extra methods needed for this beginner project -
    // JpaRepository already provides everything we use.
}
