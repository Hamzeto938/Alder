package com.ashiana.zlifno.alder.view_model;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.util.Log;

import com.ashiana.zlifno.alder.data.entity.NoteText;
import com.ashiana.zlifno.alder.data.entity.NoteType;
import com.ashiana.zlifno.alder.data.repository.NoteRepository;

import java.util.List;

// Passes on data to UI
public class ListViewModel extends AndroidViewModel {

    private NoteRepository repository;
    private LiveData<List<NoteType>> notesList;
    public boolean inProgress;

    public ListViewModel(Application application) {
        super(application);
        repository = new NoteRepository(application);
        notesList = repository.getNoteTypeList();
        inProgress = false;
    }

    public LiveData<List<NoteType>> getNotesList() {
        return notesList;
    }

    public void insertNote(Object note) {

        Log.v("Alder", "ListViewModel : Sending to repository");

        if (note instanceof NoteText) {
            repository.insertNoteText((NoteText) note);
        }
    }

    public void deleteNote(Object note) {
        Log.v("Alder", "ListViewModel : Deleting note");

        if (note instanceof NoteText) {
            repository.deleteNoteText((NoteText) note);
        }
    }

    public void moveNote(Object holdingNoteText, Object destinationNoteText) {

        Log.v("Alder", "ListViewModel : Moving note to new position");

        repository.moveNoteText((NoteType) holdingNoteText, (NoteType) destinationNoteText, this);
    }

    public void updateNote(Object note) {

        Log.v("Alder", "ListViewModel : Updating noteText");

        if (note instanceof NoteText) {
            repository.updateNoteText((NoteText) note);
        }
    }

    public Object getNote(NoteType noteType) {
        return repository.getNote(noteType);
    }

}


