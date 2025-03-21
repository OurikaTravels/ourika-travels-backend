package com.ouchin.ourikat.repository;

import com.ouchin.ourikat.entity.Guide;
import com.ouchin.ourikat.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findByGuideIdOrderByCreatedAtDesc(Long guideId);
}
