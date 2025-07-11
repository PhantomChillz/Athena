package com.idk.emo.knowledgehubproject.service;

import com.idk.emo.knowledgehubproject.payload.NoteDTO;
import com.idk.emo.knowledgehubproject.payload.NoteResponseDTO;

import java.nio.file.AccessDeniedException;

public interface NoteService {
    NoteDTO createNote(NoteDTO noteDTO);

    NoteResponseDTO getAllNotesOfUser();

    NoteDTO getNoteById(Long noteId) throws AccessDeniedException;

    NoteDTO updateNoteById(Long noteId,NoteDTO noteDTO) throws AccessDeniedException ;

    String deleteNoteById(Long noteId) throws AccessDeniedException;
}
