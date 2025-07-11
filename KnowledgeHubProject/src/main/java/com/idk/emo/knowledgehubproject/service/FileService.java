package com.idk.emo.knowledgehubproject.service;

import com.idk.emo.knowledgehubproject.payload.FileDTO;
import com.idk.emo.knowledgehubproject.payload.FileResponseDTO;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface FileService {
    FileDTO uploadFile(MultipartFile file) throws IOException;

    FileResponseDTO getAllFiles();

    Resource downloadFile(Long fileId);

    Resource previewFile(Long fileId);

    FileDTO getFileById(Long fileId);

    String deleteFileById(Long fileId) throws IOException;
}
