package com.idk.emo.knowledgehubproject.payload;

import com.idk.emo.knowledgehubproject.model.Note;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NoteResponseDTO {
    private Long userId;
    List<NoteDTO> notes = new ArrayList<>();
}
