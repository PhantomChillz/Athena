package com.idk.emo.knowledgehubproject.service.serviceImpl;

import com.idk.emo.knowledgehubproject.model.Note;
import com.idk.emo.knowledgehubproject.model.User;
import com.idk.emo.knowledgehubproject.payload.NoteDTO;
import com.idk.emo.knowledgehubproject.payload.NoteResponseDTO;
import com.idk.emo.knowledgehubproject.repository.NoteRepository;
import com.idk.emo.knowledgehubproject.repository.UserRepository;
import com.idk.emo.knowledgehubproject.service.NoteService;
import com.idk.emo.knowledgehubproject.util.AuthUtil;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.nio.file.AccessDeniedException;
import java.util.List;

@Service
public class NoteServiceImpl implements NoteService {
    @Autowired
    AuthUtil authUtil;
    
    @Autowired
    NoteRepository noteRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public NoteDTO createNote(NoteDTO noteDTO) {
        User user = userRepository.findByUsername(authUtil.getLoggedInUserName())
                .orElseThrow(()->new RuntimeException("User not found with username : "+authUtil.getLoggedInUserName()));
        Note note = new Note();
        note.setTitle(noteDTO.getTitle());
        note.setContent(noteDTO.getContent());
        note.setUser(user);
        noteRepository.save(note);
        return modelMapper.map(note,NoteDTO.class);
    }

    @Override
    public NoteResponseDTO getAllNotesOfUser() {
        User user = userRepository.findByUsername(authUtil.getLoggedInUserName())
                .orElseThrow(()->new RuntimeException("User not found with username : "+authUtil.getLoggedInUserName()));
        List<Note> notes = noteRepository.findByUsername(authUtil.getLoggedInUserName());
        NoteResponseDTO noteResponseDTO = new NoteResponseDTO();
        noteResponseDTO.setUserId(user.getUserId());
        List<NoteDTO> noteDTOS = notes.stream().map(note-> modelMapper.map(note,NoteDTO.class)).toList();
        noteResponseDTO.setNotes(noteDTOS);
        return noteResponseDTO;
    }

    @Override
    public NoteDTO getNoteById(Long noteId) throws AccessDeniedException {
        Long userId = authUtil.getLoggedInUserId();
        Note note = noteRepository.findById(noteId)
                .orElseThrow(()->new RuntimeException("Note not found with id : "+noteId));
        Long NoteUserId = note.getUser().getUserId();
        if(userId.equals(NoteUserId)){
            return modelMapper.map(note,NoteDTO.class);
        }
        else{
            throw  new AccessDeniedException("User not authorized to access this note");
        }
    }

    @Override
    public NoteDTO updateNoteById(Long noteId, NoteDTO noteDTO) throws AccessDeniedException {
        Long userId = authUtil.getLoggedInUserId();
        Note note = noteRepository.findById(noteId)
                .orElseThrow(()->new RuntimeException("Note not found with id : "+noteId));
        Long NoteUserId = note.getUser().getUserId();
        if(userId.equals(NoteUserId)){
            note.setTitle(noteDTO.getTitle());
            note.setContent(noteDTO.getContent());
            noteRepository.save(note);
            return modelMapper.map(note,NoteDTO.class);
        }
        else{
            throw  new AccessDeniedException("User not authorized to access this note");
        }
    }

    @Override
    public String deleteNoteById(Long noteId) throws AccessDeniedException {
        Long userId = authUtil.getLoggedInUserId();
        Note note = noteRepository.findById(noteId)
                .orElseThrow(()->new RuntimeException("Note not found with id : "+noteId));
        Long NoteUserId = note.getUser().getUserId();
        if(userId.equals(NoteUserId)){
            noteRepository.delete(note);
            return "Note deleted successfully";
        }
        else{
            throw  new AccessDeniedException("User not authorized to access this note");
        }
    }
}
