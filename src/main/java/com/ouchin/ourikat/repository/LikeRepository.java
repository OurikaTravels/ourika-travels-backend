package com.ouchin.ourikat.repository;

import com.ouchin.ourikat.entity.Like;
import com.ouchin.ourikat.entity.Post;
import com.ouchin.ourikat.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface LikeRepository extends JpaRepository<Like, Long> {
    Optional<Like> findByUserAndPost(User user, Post post);
    int countByPost(Post post);
    boolean existsByUserAndPost(User user, Post post);
}