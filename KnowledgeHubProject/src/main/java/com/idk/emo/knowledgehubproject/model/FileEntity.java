package com.idk.emo.knowledgehubproject.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name="files")
@Data
public class FileEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long fileId;

    private String fileName;
    private String contentType;
    private Long size;
    private String filePath;
    private LocalDateTime createdAt;
    @Enumerated(EnumType.STRING)
    private FileCategory category;

    @Lob
    @Column(columnDefinition = "TEXT")
    private String textContent;

    @JsonIgnore
    @OneToOne(cascade = CascadeType.ALL,fetch = FetchType.LAZY,mappedBy = "file")
    FileEmbedding fileEmbedding;
    @ManyToOne
    @JoinColumn(name="user_id")
    private User user;
    @PrePersist
    public void onCreate(){
        this.createdAt = LocalDateTime.now();
    }
}
