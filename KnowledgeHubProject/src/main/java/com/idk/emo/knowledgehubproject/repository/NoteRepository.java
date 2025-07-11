package com.idk.emo.knowledgehubproject.repository;

import com.idk.emo.knowledgehubproject.model.Note;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NoteRepository extends JpaRepository<Note,Long> {
    @Query("SELECT n FROM Note n WHERE n.user.username = ?1")
    List<Note> findByUsername(String loggedInUserName);
}
