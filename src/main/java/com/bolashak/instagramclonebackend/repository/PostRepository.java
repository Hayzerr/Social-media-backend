package com.bolashak.instagramclonebackend.repository;

import com.bolashak.instagramclonebackend.model.Post;
import com.bolashak.instagramclonebackend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findByUserOrderByIdDesc(User user);
    List<Post> findAllByOrderByIdDesc();
    Post findPostById(Long id);
    Post deletePostById(Long id);

}
