package com.udacity.jwdnd.course1.cloudstorage.service;

import com.udacity.jwdnd.course1.cloudstorage.mapper.NoteMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.Note;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NoteService {
    NoteMapper noteMapper;

    public NoteService(NoteMapper noteMapper) {
        this.noteMapper = noteMapper;
    }

    public int storeNote(int userId, Note note){
        Note anote = new Note(null, note.getNoteTitle(), note.getNoteDescription(), userId);
        return noteMapper.insertNote(anote);
    }

    public List<Note> getNotes(int userId) {
        return noteMapper.getNotes(userId);
    }

    public int deleteNote(int userId, int noteId) {
        return noteMapper.deleteNote(userId, noteId);
    }

    public int updateNote(int userId, Note note) {
        Note anote = new Note(note.getNoteId(), note.getNoteTitle(), note.getNoteDescription(), userId);
        return noteMapper.updateNote(anote);
    }
}
