package com.ouchin.ourikat.repository;

import com.ouchin.ourikat.entity.Comment;
import com.ouchin.ourikat.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByPostOrderByCreatedAtDesc(Post post);
    int countByPost(Post post);
}