package com.idk.emo.knowledgehubproject.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FileEmbeddingResponseDTO {
    List<Double> embedding;
}