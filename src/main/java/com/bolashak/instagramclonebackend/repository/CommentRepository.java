package com.bolashak.instagramclonebackend.repository;

import com.bolashak.instagramclonebackend.model.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Integer> {
    Boolean existsById(Long commentId);
    void deleteById(Long commentId);
    Optional<Comment> findById(Long commentId);
}
