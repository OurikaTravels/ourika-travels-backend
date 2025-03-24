package com.ouchin.ourikat.repository;

import com.ouchin.ourikat.entity.Guide;
import com.ouchin.ourikat.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findByGuideIdOrderByCreatedAtDesc(Long guideId);
    @Query("SELECT l.post.id FROM Like l WHERE l.user.id = :userId")
    List<Long> findPostIdsByUserId(@Param("userId") Long userId);
}
