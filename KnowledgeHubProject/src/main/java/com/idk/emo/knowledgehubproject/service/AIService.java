package com.idk.emo.knowledgehubproject.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.idk.emo.knowledgehubproject.payload.AIRequestDTO;
import com.idk.emo.knowledgehubproject.payload.FileDTO;

import java.util.List;

public interface AIService {
    String getAnswer(AIRequestDTO requestDTO) throws JsonProcessingException;
    List<FileDTO> searchTopFiles(String question, int topN) throws JsonProcessingException;
}
