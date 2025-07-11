package com.idk.emo.knowledgehubproject.payload;

import com.idk.emo.knowledgehubproject.model.FileCategory;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class FileDTO {
    private Long fileId;
    private String fileName;
    private String contentType;
    private Long size;
    private String downloadUrl;
    private String previewUrl;
    private LocalDateTime createdAt;
    private FileCategory category;

    // ✅ Needed for AI answer generation
    private String textContent;
}
