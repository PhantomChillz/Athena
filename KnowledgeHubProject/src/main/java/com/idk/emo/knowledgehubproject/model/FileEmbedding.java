package com.idk.emo.knowledgehubproject.model;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="file_embeddings")
public class FileEmbedding {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long fileEmbeddingId;
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="file_id")
    private FileEntity file;
    @Lob
    @NotBlank
    private String embedding;
    public void setEmbeddingVector(List<Double> vector) throws JsonProcessingException {
        this.embedding = new ObjectMapper().writeValueAsString(vector); // store as JSON
    }

    public List<Double> getEmbeddingVector() throws JsonProcessingException {
        return new ObjectMapper().readValue(embedding, new TypeReference<List<Double>>() {});
    }
}
