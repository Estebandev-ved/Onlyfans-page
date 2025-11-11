package com.proyecto.repository;

import com.proyecto.entity.Content;
import com.proyecto.entity.Like;
import com.proyecto.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface LikeRepository extends JpaRepository<Like, UUID> {

    Optional<Like> findByUserAndContent(User user, Content content);

    long countByContent(Content content);

    boolean existsByUserAndContent(User user, Content content);
}