package com.idk.emo.knowledgehubproject.service;

import com.idk.emo.knowledgehubproject.model.FileEmbedding;

import java.util.List;

public interface FileEmbeddingService {
    List<Double> generateEmbedding(String textContent);
    // FileEmbeddingService.java
    public List<FileEmbedding> getAllEmbeddingsForUser(Long userId);


}
