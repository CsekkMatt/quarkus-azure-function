package org.acme.service;

import java.util.List;
import java.util.stream.Collectors;

import org.acme.domain.NoteEntity;
import org.acme.model.Note;
import org.acme.repository.NoteRepository;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class NoteService {

    @Inject
    private NoteRepository noteRepository;

    public List<Note> getAllNotes() {
        List<NoteEntity> noteEntities = noteRepository.listAll();
        return noteEntities.stream()
                .map(this::mapNoteEntityToNote)
                .collect(Collectors.toList());
    }

    public Note findNoteById(Long id) {
        NoteEntity noteEntity = noteRepository.findById(id);
        return mapNoteEntityToNote(noteEntity);
    }

    @Transactional
    public void saveNote(Note note) {
        NoteEntity noteEntity = new NoteEntity();
        noteEntity.setTitle(note.getTitle());
        noteEntity.setContent(note.getContent());
        noteRepository.persist(noteEntity);
    }

    public void updateNote(Note note) {
        NoteEntity noteEntity = noteRepository.findById(note.getId());
        if (noteEntity != null) {
            noteEntity.setTitle(note.getTitle());
            noteEntity.setContent(note.getContent());
            noteRepository.persist(noteEntity);
        } else {
            throw new IllegalArgumentException("Note not found");
        }
    }

    public void deleteNote(Long id) {
        NoteEntity noteEntity = noteRepository.findById(id);
        if (noteEntity != null) {
            noteRepository.delete(noteEntity);
        } else {
            throw new IllegalArgumentException("Note not found");
        }
    }

    private Note mapNoteEntityToNote(NoteEntity noteEntity) {
        Note note = new Note();
        note.setId(noteEntity.id);
        note.setTitle(noteEntity.getTitle());
        note.setContent(noteEntity.getContent());
        return note;
    }

}
