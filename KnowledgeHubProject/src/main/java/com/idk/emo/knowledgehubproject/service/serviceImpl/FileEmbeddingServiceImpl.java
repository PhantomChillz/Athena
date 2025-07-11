package com.idk.emo.knowledgehubproject.service.serviceImpl;

import com.idk.emo.knowledgehubproject.model.FileEmbedding;
import com.idk.emo.knowledgehubproject.payload.FileEmbeddingDTO;
import com.idk.emo.knowledgehubproject.payload.FileEmbeddingResponseDTO;
import com.idk.emo.knowledgehubproject.repository.FileEmbeddingRepository;
import com.idk.emo.knowledgehubproject.service.FileEmbeddingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class FileEmbeddingServiceImpl implements FileEmbeddingService {
    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private FileEmbeddingRepository fileEmbeddingRepository;

    @Override
    public List<Double> generateEmbedding(String textContent) {
        String pyServerUrl = "http://0.0.0.0:8000/generate-embedding";

        FileEmbeddingDTO fileEmbeddingDTO = new FileEmbeddingDTO();
        fileEmbeddingDTO.setText(textContent);
        HttpHeaders httpHeaders = new HttpHeaders();
        HttpEntity<FileEmbeddingDTO> httpEntity = new HttpEntity<>(fileEmbeddingDTO,httpHeaders);
        ResponseEntity<FileEmbeddingResponseDTO> response = restTemplate.exchange(
                pyServerUrl,
                HttpMethod.POST,
                httpEntity,
                FileEmbeddingResponseDTO.class
        );
        if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
            return response.getBody().getEmbedding();
        } else {
            throw new RuntimeException("Failed to generate embeddings from Python service.");
        }
    }

    @Override
    public List<FileEmbedding> getAllEmbeddingsForUser(Long userId) {
        return fileEmbeddingRepository.findByUserId(userId);
    }
}
