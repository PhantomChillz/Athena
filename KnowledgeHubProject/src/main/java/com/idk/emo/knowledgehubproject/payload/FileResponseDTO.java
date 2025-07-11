package com.idk.emo.knowledgehubproject.payload;

import lombok.Data;

import java.util.List;

    @Data
    public class FileResponseDTO {
        Long userId;
        Integer totalFiles;
        List<FileDTO> files;
    }
