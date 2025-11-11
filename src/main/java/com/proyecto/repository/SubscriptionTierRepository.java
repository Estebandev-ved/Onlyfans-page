package com.proyecto.repository;

import com.proyecto.entity.SubscriptionTier;
import com.proyecto.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface SubscriptionTierRepository extends JpaRepository<SubscriptionTier, UUID> {

    List<SubscriptionTier> findByCreatorAndIsActiveTrueOrderBySortOrderAsc(User creator);

    List<SubscriptionTier> findByCreator(User creator);
}