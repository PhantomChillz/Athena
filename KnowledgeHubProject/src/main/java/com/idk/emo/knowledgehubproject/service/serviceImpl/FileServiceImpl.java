package com.idk.emo.knowledgehubproject.service.serviceImpl;

import com.idk.emo.knowledgehubproject.model.FileCategory;
import com.idk.emo.knowledgehubproject.model.FileEmbedding;
import com.idk.emo.knowledgehubproject.model.FileEntity;
import com.idk.emo.knowledgehubproject.model.User;
import com.idk.emo.knowledgehubproject.payload.FileDTO;
import com.idk.emo.knowledgehubproject.payload.FileResponseDTO;
import com.idk.emo.knowledgehubproject.repository.FileRepository;
import com.idk.emo.knowledgehubproject.service.FileEmbeddingService;
import com.idk.emo.knowledgehubproject.service.FileService;
import com.idk.emo.knowledgehubproject.util.AuthUtil;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.ui.ModelMap;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;

@Service
public class FileServiceImpl implements FileService {
    @Autowired
    AuthUtil authUtil;
    @Autowired
    FileRepository fileRepository;
    @Autowired
    ModelMapper modelMapper;

    @Autowired
    FileEmbeddingService fileEmbeddingService;

    @Override
    public FileDTO uploadFile(MultipartFile file) throws IOException {
        //1.get logged in user
        User user = authUtil.getLoggedInUser();

        //2. get file meta data
        String fileName = file.getOriginalFilename();
        String contentType = file.getContentType();
        Long fileSize = file.getSize();

        //3. define file storage path
        String storageDir = "kh_uploads/";
        String uniqueFileName = UUID.randomUUID().toString() + "_" + fileName;
        Path fileStoragePath = Paths.get(storageDir + uniqueFileName);

        //4. create directories if they don't exist
        Files.createDirectories(fileStoragePath.getParent());

        //5. Save file to the disk
        Files.copy(file.getInputStream(), fileStoragePath, StandardCopyOption.REPLACE_EXISTING);

        // if content type is not autodetected
        FileCategory category;
        String textContent=null;
        if (contentType == null) {
            contentType = Files.probeContentType(fileStoragePath); // fallback
        }

        if (contentType != null) {
            if (contentType.startsWith("image/")) {
                category = FileCategory.IMAGE;
            } else if (contentType.equals("application/pdf")) {
                category = FileCategory.PDF;
                textContent = extractTextFromPdf(fileStoragePath);
            } else if (contentType.equals("application/vnd.openxmlformats-officedocument.wordprocessingml.document")) {
                category = FileCategory.WORD;
                textContent = extractTextFromDocx(fileStoragePath);
            } else if (contentType.startsWith("text/")) {
                category = FileCategory.TEXT;
                textContent = extractTextFromPlainText(fileStoragePath);
            } else {
                category = FileCategory.UNKNOWN;
            }
        } else {
            category = FileCategory.UNKNOWN;
        }

        // 6. save the meta data to DB
        FileEntity fileEntity = new FileEntity();
        fileEntity.setFileName(fileName);
        fileEntity.setSize(fileSize);
        fileEntity.setUser(user);
        fileEntity.setContentType(contentType);
        fileEntity.setFilePath(fileStoragePath.toString());
        fileEntity.setCategory(category);
        fileEntity.setTextContent(textContent==null?"":textContent.trim());
        if (textContent != null && !textContent.isBlank()) {
            List<Double> vector = fileEmbeddingService.generateEmbedding(textContent);
            FileEmbedding embedding = new FileEmbedding();
            embedding.setEmbeddingVector(vector);
            embedding.setFile(fileEntity);
            fileEntity.setFileEmbedding(embedding);
        }

        fileRepository.save(fileEntity);

        //7. return the fileDTO as response
        FileDTO fileDto = modelMapper.map(fileEntity, FileDTO.class);
        String baseUrl = ServletUriComponentsBuilder.fromCurrentContextPath().build().toUriString();
        fileDto.setDownloadUrl(baseUrl + "/api/files/download/" + fileEntity.getFileId());
        fileDto.setPreviewUrl(baseUrl + "/api/files/preview/" + fileEntity.getFileId());
        return fileDto;

    }

    @Override
    public FileResponseDTO getAllFiles() {
        Long userId = authUtil.getLoggedInUserId();
        List<FileEntity> files = fileRepository.findAllByUserId(userId);
        FileResponseDTO fileResponseDTO = new FileResponseDTO();
        fileResponseDTO.setUserId(userId);
        List<FileDTO> fileDTOS = files.stream().map(file->{
            FileDTO fileDto =modelMapper.map(file,FileDTO.class);
            String baseUrl = ServletUriComponentsBuilder.fromCurrentContextPath().build().toUriString();
            fileDto.setDownloadUrl(baseUrl + "/api/files/download/" + file.getFileId());
            fileDto.setPreviewUrl(baseUrl + "/api/files/preview/" + file.getFileId());
            return fileDto;
        }).toList();
        fileResponseDTO.setFiles(fileDTOS);
        fileResponseDTO.setTotalFiles(files.size());
        return fileResponseDTO;
    }

    @Override
    public Resource downloadFile(Long fileId) {
        FileEntity file = fileRepository.findById(fileId)
                .orElseThrow(()->new RuntimeException("File not found with id : "+fileId));
        Path fileStoragePath = Paths.get(file.getFilePath());
        Resource resource;
        try{
            resource = new UrlResource(fileStoragePath.toUri());
            if(!resource.exists()||!resource.isReadable()){
                throw new RuntimeException("File not readable with id : "+fileId);
            }
        }
        catch (Exception e){
            throw new RuntimeException("File not found with id : "+fileId);
        }
        return resource;
    }

    @Override
    public Resource previewFile(Long fileId) {
        FileEntity file = fileRepository.findById(fileId)
                .orElseThrow(()->new RuntimeException("File not found with id : "+fileId));
        Path fileStoragePath = Paths.get(file.getFilePath());
        Resource resource;
        try{
            resource = new UrlResource(fileStoragePath.toUri());
            if(!resource.exists()||!resource.isReadable()){
                throw new RuntimeException("File not readable with id : "+fileId);
            }
        }catch (Exception e){
            throw new RuntimeException("File not found with id : "+fileId);
        }
        return resource;
    }

    @Override
    public FileDTO getFileById(Long fileId) {
        Long userId = authUtil.getLoggedInUserId();
        FileEntity file = fileRepository.findById(fileId).orElseThrow(()->new RuntimeException("File not found with id : "+fileId));
        if(userId.equals(file.getUser().getUserId())){
            FileDTO fileDto = modelMapper.map(file,FileDTO.class);
            String baseUrl = ServletUriComponentsBuilder.fromCurrentContextPath().build().toUriString();
            fileDto.setDownloadUrl(baseUrl + "/api/files/download/" + file.getFileId());
            fileDto.setPreviewUrl(baseUrl + "/api/files/preview/" + file.getFileId());
            return fileDto;
        }
        else{
            throw new ResourceAccessException("User not authorized to access this file");
        }
    }

    @Override
    public String deleteFileById(Long fileId) throws IOException {
        Long userId = authUtil.getLoggedInUserId();
        FileEntity file = fileRepository.findById(fileId)
                .orElseThrow(()->new RuntimeException("File not found with id : "+fileId));
        if(userId.equals(file.getUser().getUserId())){
            fileRepository.delete(file);
            Path path = Paths.get(file.getFilePath());
            try {
                Files.deleteIfExists(path);
            } catch (IOException e) {
                throw new RuntimeException("Failed to delete file from disk: " + e.getMessage());
            }
            return "File deleted successfully";
        }
        else{
            throw new ResourceAccessException("User not authorized to access this file");
        }
    }

    private String extractTextFromPdf(Path filePath) throws IOException {
        try{
            PDDocument document = PDDocument.load(filePath.toFile());
            PDFTextStripper pdfStripper = new PDFTextStripper();
            return pdfStripper.getText(document);
        }
        catch (Exception e){
            throw new IOException("Failed to extract text from PDF: "+e.getMessage());
        }
    }
    public String extractTextFromPlainText(Path path) {
        try {
            return Files.readString(path);
        } catch (IOException e) {
            throw new RuntimeException("Failed to read plain text file", e);
        }
    }
    public String extractTextFromDocx(Path path) {
        try{
            FileInputStream fis = new FileInputStream(path.toFile());
             XWPFDocument doc = new XWPFDocument(fis);
            XWPFWordExtractor extractor = new XWPFWordExtractor(doc);
            return extractor.getText();
        } catch (IOException e) {
            throw new RuntimeException("Failed to extract text from DOCX file", e);
        }
    }


}
