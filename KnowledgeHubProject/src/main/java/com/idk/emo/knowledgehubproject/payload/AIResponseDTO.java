package com.idk.emo.knowledgehubproject.payload;

import lombok.Data;

import java.util.List;

@Data
public class AIResponseDTO {
    private String question;
    private List<Double> embedding;
}
