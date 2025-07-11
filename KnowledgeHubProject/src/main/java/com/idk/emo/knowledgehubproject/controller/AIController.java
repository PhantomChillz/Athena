package com.idk.emo.knowledgehubproject.controller;

import com.idk.emo.knowledgehubproject.payload.AIAnswerDTO;
import com.idk.emo.knowledgehubproject.payload.AIRequestDTO;
import com.idk.emo.knowledgehubproject.payload.FileDTO;
import com.idk.emo.knowledgehubproject.service.AIService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/ai")
public class AIController {

    @Autowired
    private AIService aiService;

    @PostMapping("/semantic-search")
    public ResponseEntity<List<FileDTO>> semanticSearch(
            @RequestBody AIRequestDTO request,
            @RequestParam(defaultValue = "5") int topN) {
        try {
            List<FileDTO> topFiles = aiService.searchTopFiles(request.getQuestion(), topN);
            return ResponseEntity.ok(topFiles);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @PostMapping("/ask")
    public ResponseEntity<  AIAnswerDTO> getAnswer(@RequestBody AIRequestDTO requestDTO) {
        try {
            String answer = aiService.getAnswer(requestDTO);
            return ResponseEntity.ok(new AIAnswerDTO(answer));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new AIAnswerDTO("Error generating answer: " + e.getMessage()));
        }
    }
}