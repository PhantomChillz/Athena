package com.idk.emo.knowledgehubproject.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NoteDTO {
    private Long noteId;
    private String title;
    private String content;
}
