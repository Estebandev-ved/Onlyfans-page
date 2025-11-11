package com.proyecto.repository;

import com.proyecto.entity.Payment;
import com.proyecto.entity.User;
import com.proyecto.entity.Payment.PaymentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, UUID> {

    List<Payment> findByUserOrderByCreatedAtDesc(User user);

    List<Payment> findByUserAndStatusOrderByCreatedAtDesc(User user, Payment.PaymentStatus status);

    @Query("SELECT SUM(p.amount) FROM Payment p WHERE p.user = :user AND p.status = 'COMPLETED'")
    BigDecimal getTotalSpentByUser(@Param("user") User user);

    @Query("SELECT SUM(p.amount) FROM Payment p WHERE p.user = :user AND p.status = 'COMPLETED' " +
           "AND p.createdAt BETWEEN :startDate AND :endDate")
    BigDecimal getTotalSpentByUserAndDateRange(@Param("user") User user,
                                                @Param("startDate") LocalDateTime startDate,
                                                @Param("endDate") LocalDateTime endDate);
}