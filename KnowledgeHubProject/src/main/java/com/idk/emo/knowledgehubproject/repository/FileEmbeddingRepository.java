package com.idk.emo.knowledgehubproject.repository;

import com.idk.emo.knowledgehubproject.model.FileEmbedding;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface FileEmbeddingRepository extends JpaRepository<FileEmbedding,Long> {
    @Query("SELECT fe FROM FileEmbedding fe WHERE fe.file.user.userId = :userId")
    List<FileEmbedding> findByUserId(@Param("userId") Long userId);

}
