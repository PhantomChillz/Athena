package com.idk.emo.knowledgehubproject.controller;

import com.idk.emo.knowledgehubproject.model.FileEntity;
import com.idk.emo.knowledgehubproject.payload.FileDTO;
import com.idk.emo.knowledgehubproject.payload.FileResponseDTO;
import com.idk.emo.knowledgehubproject.repository.FileRepository;
import com.idk.emo.knowledgehubproject.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api")
public class FileController {
    @Autowired
    FileService fileService;

    @Autowired
    FileRepository fileRepository;
    @PostMapping("/files")
    public ResponseEntity<FileDTO> uploadFile(@RequestParam MultipartFile file) throws Exception{
        FileDTO fileDTO = fileService.uploadFile(file);
        return new ResponseEntity<>(fileDTO, HttpStatus.CREATED);
    }

    @GetMapping("/files")
    public ResponseEntity<FileResponseDTO> getAllFiles(){
        FileResponseDTO fileResponseDTO = fileService.getAllFiles();
        return new ResponseEntity<>(fileResponseDTO, HttpStatus.OK);
    }

    @GetMapping("/files/{fileId}")
    public ResponseEntity<FileDTO> getFileById(@PathVariable("fileId") Long fileId){
        FileDTO fileDTO = fileService.getFileById(fileId);
        return new ResponseEntity<>(fileDTO, HttpStatus.OK);
    }

    @DeleteMapping("files/{fileId}")
    public ResponseEntity<String> deleteFileById(@PathVariable("fileId") Long fileId) throws IOException {
        String res = fileService.deleteFileById(fileId);
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    @GetMapping("/files/download/{fileId}")
    public ResponseEntity<Resource> downloadFile(@PathVariable("fileId") Long fileId){
        Resource resource = fileService.downloadFile(fileId);
        FileEntity fileEntity = fileRepository.findById(fileId)
                .orElseThrow(()-> new RuntimeException("File not found with id: "+fileId));
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(fileEntity.getContentType()))
                .header(HttpHeaders.CONTENT_DISPOSITION,"attachment; filename=\""+resource.getFilename()+"\"")
                .body(resource);
    }

    @GetMapping("/files/preview/{fileId}")
    public ResponseEntity<Resource> previewFile(@PathVariable("fileId") Long fileId){
        Resource resource = fileService.previewFile(fileId);
        FileEntity fileEntity = fileRepository.findById(fileId)
                .orElseThrow(()-> new RuntimeException("File not found with id: "+fileId));
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(fileEntity.getContentType()))
                .header(HttpHeaders.CONTENT_DISPOSITION,"inline; filename=\""+resource.getFilename()+"\"")
                .body(resource);
    }
}
