package com.idk.emo.knowledgehubproject.controller;


import com.idk.emo.knowledgehubproject.payload.NoteDTO;
import com.idk.emo.knowledgehubproject.payload.NoteResponseDTO;
import com.idk.emo.knowledgehubproject.service.NoteService;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.file.AccessDeniedException;

@RestController
@RequestMapping("/api")
public class NoteController {

    @Autowired
    NoteService noteService;

    @PostMapping("/notes")
    public ResponseEntity<NoteDTO> createNote(@RequestBody NoteDTO noteDTO){
        NoteDTO noteResponseDTO = noteService.createNote(noteDTO);
        return new ResponseEntity<>(noteResponseDTO, HttpStatus.CREATED);
    }

    @GetMapping("/notes")
    public ResponseEntity<NoteResponseDTO> getAllNotesOfUser(){
        NoteResponseDTO noteResponseDTO = noteService.getAllNotesOfUser();
        return new ResponseEntity<>(noteResponseDTO, HttpStatus.OK);
    }

    @GetMapping("/notes/{noteId}")
    public ResponseEntity<NoteDTO> getNoteById(@PathVariable("noteId") Long noteId) throws AccessDeniedException {
        NoteDTO noteDTO = noteService.getNoteById(noteId);
        return new ResponseEntity<>(noteDTO, HttpStatus.OK);
    }

    @PutMapping("/notes/{noteId}")
    public ResponseEntity<NoteDTO> updateNoteById(@PathVariable("noteId") Long noteId,@RequestBody NoteDTO noteDTO) throws AccessDeniedException {
        NoteDTO responseNoteDTO = noteService.updateNoteById(noteId,noteDTO);
        return new ResponseEntity<>(responseNoteDTO, HttpStatus.OK);
    }
    @DeleteMapping("/notes/{noteId}")
    public ResponseEntity<String> deleteNoteById(@PathVariable("noteId") Long noteId) throws AccessDeniedException {
        String response = noteService.deleteNoteById(noteId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
