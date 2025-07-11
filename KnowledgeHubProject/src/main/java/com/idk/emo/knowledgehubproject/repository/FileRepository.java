package com.idk.emo.knowledgehubproject.repository;

import com.idk.emo.knowledgehubproject.model.FileEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FileRepository extends JpaRepository<FileEntity,Long> {
    @Query("SELECT f FROM FileEntity f WHERE f.user.userId = ?1")
    List<FileEntity> findAllByUserId(Long userId);

    @Query("SELECT f FROM FileEntity f JOIN FETCH f.fileEmbedding WHERE f.fileEmbedding.embedding IS NOT NULL AND f.user.userId = :userId")
    List<FileEntity> findAllWithEmbeddingsByUserId(@Param("userId") Long userId);

}
