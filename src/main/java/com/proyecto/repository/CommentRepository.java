package com.proyecto.repository;

import com.proyecto.entity.Comment;
import com.proyecto.entity.Content;
import com.proyecto.entity.Like;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface CommentRepository extends JpaRepository<Comment, UUID> {

    List<Comment> findByContentAndParentIsNullOrderByCreatedAtDesc(Content content);

    List<Comment> findByContentAndParentOrderByCreatedAtDesc(Content content, Comment parent);

    List<Comment> findByContentOrderByCreatedAtDesc(Content content);

    List<Like> findByComment(Comment comment);
}