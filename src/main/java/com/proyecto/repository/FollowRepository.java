package com.proyecto.repository;

import com.proyecto.entity.Follow;
import com.proyecto.entity.User;
import com.proyecto.entity.Follow.FollowStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface FollowRepository extends JpaRepository<Follow, UUID> {

    Optional<Follow> findByFollowerAndFollowing(User follower, User following);

    List<Follow> findByFollower(User follower);

    List<Follow> findByFollowing(User following);

    @Query("SELECT f FROM Follow f WHERE f.follower = :user AND f.status = 'ACCEPTED'")
    List<Follow> findAcceptedFollowing(@Param("user") User user);

    @Query("SELECT f FROM Follow f WHERE f.following = :user AND f.status = 'ACCEPTED'")
    List<Follow> findAcceptedFollowers(@Param("user") User user);

    long countByFollowingAndStatus(User following, Follow.FollowStatus status);

    long countByFollowerAndStatus(User follower, Follow.FollowStatus status);
}