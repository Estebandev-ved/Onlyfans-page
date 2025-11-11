package com.proyecto.repository;

import com.proyecto.entity.Subscription;
import com.proyecto.entity.User;
import com.proyecto.entity.Subscription.SubscriptionStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface SubscriptionRepository extends JpaRepository<Subscription, UUID> {

    Optional<Subscription> findBySubscriberAndCreator(User subscriber, User creator);

    List<Subscription> findBySubscriberAndStatus(User subscriber, Subscription.SubscriptionStatus status);

    List<Subscription> findByCreatorAndStatus(User creator, Subscription.SubscriptionStatus status);

    @Query("SELECT s FROM Subscription s WHERE s.subscriber = :subscriber AND s.status = 'ACTIVE' " +
           "ORDER BY s.createdAt DESC")
    List<Subscription> findActiveSubscriptionsBySubscriber(@Param("subscriber") User subscriber);

    @Query("SELECT s FROM Subscription s WHERE s.creator = :creator AND s.status = 'ACTIVE' " +
           "ORDER BY s.createdAt DESC")
    List<Subscription> findActiveSubscriptionsByCreator(@Param("creator") User creator);

    @Query("SELECT COUNT(s) FROM Subscription s WHERE s.creator = :creator AND s.status = 'ACTIVE'")
    long countActiveSubscribersByCreator(@Param("creator") User creator);

    @Query("SELECT SUM(s.amount) FROM Subscription s WHERE s.creator = :creator AND s.status = 'ACTIVE'")
    java.math.BigDecimal getTotalRevenueByCreator(@Param("creator") User creator);
}