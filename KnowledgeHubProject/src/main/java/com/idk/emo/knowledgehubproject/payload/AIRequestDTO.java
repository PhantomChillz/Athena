package com.idk.emo.knowledgehubproject.payload;

import lombok.Data;

@Data
public class AIRequestDTO {
    private String question;

    public AIRequestDTO(String question) {
        this.question=question;
    }
}
