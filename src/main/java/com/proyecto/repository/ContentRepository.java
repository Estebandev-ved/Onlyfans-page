package com.proyecto.repository;

import com.proyecto.entity.Content;
import com.proyecto.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ContentRepository extends JpaRepository<Content, UUID> {

    List<Content> findByUserAndStatus(User user, Content.ContentStatus status);

    List<Content> findByStatusAndAccessType(Content.ContentStatus status, 
                                           Content.AccessType accessType);

    List<Content> findByStatus(Content.ContentStatus status);

    List<Content> findByCategoryAndStatus(Content.ContentCategory category, 
                                         Content.ContentStatus status);

    List<Content> findByContentTypeAndStatus(Content.ContentType contentType, 
                                            Content.ContentStatus status);

    @Query("SELECT c FROM Content c WHERE c.status = 'PUBLISHED' AND c.accessType = 'FREE' " +
           "ORDER BY c.createdAt DESC")
    List<Content> findFreePublicContent();

    @Query("SELECT c FROM Content c WHERE c.status = 'PUBLISHED' " +
           "ORDER BY c.viewCount DESC")
    List<Content> findMostViewedContent();

    @Query("SELECT c FROM Content c WHERE c.status = 'PUBLISHED' " +
           "ORDER BY c.likeCount DESC")
    List<Content> findMostLikedContent();

    @Query("SELECT c FROM Content c WHERE c.status = 'PUBLISHED' " +
           "ORDER BY c.createdAt DESC")
    List<Content> findLatestPublishedContent();

    @Query("SELECT c FROM Content c WHERE c.status = 'PUBLISHED' AND " +
           "(LOWER(c.title) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
           "LOWER(c.description) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
           "LOWER(c.tags) LIKE LOWER(CONCAT('%', :search, '%')))")
    List<Content> searchContent(@Param("search") String search);

    @Query("SELECT c FROM Content c WHERE c.status = 'PUBLISHED' AND c.user = :creator AND " +
           "(LOWER(c.title) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
           "LOWER(c.description) LIKE LOWER(CONCAT('%', :search, '%')))")
    List<Content> searchCreatorContent(@Param("creator") User creator, 
                                      @Param("search") String search);
}