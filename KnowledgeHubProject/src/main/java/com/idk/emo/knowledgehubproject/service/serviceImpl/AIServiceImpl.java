package com.idk.emo.knowledgehubproject.service.serviceImpl;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.idk.emo.knowledgehubproject.model.FileEmbedding;
import com.idk.emo.knowledgehubproject.model.FileEntity;
import com.idk.emo.knowledgehubproject.payload.*;
import com.idk.emo.knowledgehubproject.service.AIService;
import com.idk.emo.knowledgehubproject.service.FileEmbeddingService;
import com.idk.emo.knowledgehubproject.util.AppConfig;
import com.idk.emo.knowledgehubproject.util.AuthUtil;
import com.idk.emo.knowledgehubproject.util.EmbedUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class AIServiceImpl implements AIService {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private FileEmbeddingService fileEmbeddingService;

    @Autowired
    AuthUtil authUtil;

    @Autowired
    ModelMapper modelMapper;
    // Generates question embedding vector using Python API
    public List<Double> genQEmbeddingVector(FileEmbeddingDTO requestDTO) {
        String pyServerUrl = "http://0.0.0.0:8000/generate-embedding";

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<FileEmbeddingDTO> httpEntity = new HttpEntity<>(requestDTO, httpHeaders);

        ResponseEntity<AIResponseDTO> response = restTemplate.exchange(
                pyServerUrl,
                HttpMethod.POST,
                httpEntity,
                AIResponseDTO.class
        );

        if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
            return response.getBody().getEmbedding();
        } else {
            throw new RuntimeException("Failed to generate embeddings from Python service.");
        }
    }

    // Semantic search logic
    @Override
    public List<FileDTO> searchTopFiles(String question, int topN) throws JsonProcessingException {
        // 1. Generate embedding for the question
        List<Double> questionEmbedding = genQEmbeddingVector(new FileEmbeddingDTO(question));

        // 2. Get current user's embeddings
        Long userId = authUtil.getLoggedInUserId();
        List<FileEmbedding> allEmbeddings = fileEmbeddingService.getAllEmbeddingsForUser(userId);

        // 3. Compute cosine similarity for each
        List<Map.Entry<FileEntity, Double>> scored = new ArrayList<>();
        for (FileEmbedding emb : allEmbeddings) {
            double score = EmbedUtils.cosineSimilarity(questionEmbedding, emb.getEmbeddingVector());
            scored.add(new AbstractMap.SimpleEntry<>(emb.getFile(), score));
        }

        // 4. Sort and map top N to DTOs
        return scored.stream()
                .sorted((a, b) -> Double.compare(b.getValue(), a.getValue()))
                .limit(topN)
                .map(entry -> modelMapper.map(entry.getKey(), FileDTO.class))
                .collect(Collectors.toList());
    }



    @Override
    public String getAnswer(AIRequestDTO requestDTO) throws JsonProcessingException {
        // Step 1: Get top relevant files
        List<FileDTO> topFiles = searchTopFiles(requestDTO.getQuestion(), 3); // top 3 relevant files

        // Step 2: Build prompt context from their textContent
        StringBuilder contextBuilder = new StringBuilder();
        for (FileDTO file : topFiles) {
            contextBuilder.append("File: ").append(file.getFileName()).append("\n");
            contextBuilder.append(file.getTextContent()).append("\n\n");
        }

        String finalPrompt = """
            You are a helpful AI assistant. Based on the following context from the user's documents, answer the question.

            Context:
            %s

            Question:
            %s

            Answer:""".formatted(contextBuilder.toString(), requestDTO.getQuestion());

        // Step 3: Call local LLM (Ollama) to get answer
        String ollamaUrl = "http://localhost:11434/api/generate"; // adjust as needed

        Map<String, Object> requestMap = new HashMap<>();
        requestMap.put("model", "lLaVa"); // or your actual model
        requestMap.put("prompt", finalPrompt);
        requestMap.put("stream", false); // non-streaming response

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestMap, headers);
        ResponseEntity<Map> response = restTemplate.exchange(
                ollamaUrl, HttpMethod.POST, entity, Map.class
        );

        if (response.getStatusCode().is2xxSuccessful()) {
            Object output = response.getBody().get("response");
            return output != null ? output.toString().trim() : "No answer generated.";
        } else {
            throw new RuntimeException("Failed to get response from Ollama");
        }
    }


}
