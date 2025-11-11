package com.proyecto.repository;

import com.proyecto.entity.User;
import com.proyecto.entity.User.UserRole;
import com.proyecto.entity.User.AccountStatus;
import com.proyecto.entity.SubscriptionTier;
import com.proyecto.entity.Subscription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {

    Optional<User> findByEmail(String email);

    Optional<User> findByUsername(String username);

    boolean existsByEmail(String email);

    boolean existsByUsername(String username);

    @Query("SELECT u FROM User u WHERE u.role = :role AND u.status = :status")
    List<User> findByRoleAndStatus(@Param("role") User.UserRole role, 
                                   @Param("status") User.AccountStatus status);

    @Query("SELECT u FROM User u WHERE u.role = 'CREATOR' AND u.status = 'ACTIVE' ORDER BY u.createdAt DESC")
    List<User> findActiveCreators();

    @Query("SELECT u FROM User u WHERE u.role = 'CREATOR' AND u.status = 'ACTIVE' AND " +
           "(LOWER(u.displayName) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
           "LOWER(u.username) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
           "LOWER(u.bio) LIKE LOWER(CONCAT('%', :search, '%')))")
    List<User> searchCreators(@Param("search") String search);

    @Query("SELECT u FROM User u WHERE u.status = 'ACTIVE' AND u.emailVerified = true")
    List<User> findActiveVerifiedUsers();
}