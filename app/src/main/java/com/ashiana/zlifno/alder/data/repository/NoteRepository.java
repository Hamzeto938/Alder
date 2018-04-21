package com.ashiana.zlifno.alder.data.repository;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import com.ashiana.zlifno.alder.data.dao.NoteTextDao;
import com.ashiana.zlifno.alder.data.dao.NoteTypeDao;
import com.ashiana.zlifno.alder.data.database.NoteTextDatabase;
import com.ashiana.zlifno.alder.data.database.NoteTypeDatabase;
import com.ashiana.zlifno.alder.data.entity.NoteText;
import com.ashiana.zlifno.alder.data.entity.NoteType;
import com.ashiana.zlifno.alder.view_model.ListViewModel;

import java.util.List;
import java.util.Objects;

public class NoteRepository {
    private NoteTypeDao noteTypeDao;
    private NoteTextDao noteTextDao;

    private LiveData<List<NoteType>> noteTypeList;

    public NoteRepository(Application application) {

        NoteTypeDatabase noteTypeDatabase = NoteTypeDatabase.getDatabase(application);
        NoteTextDatabase noteTextDatabase = NoteTextDatabase.getDatabase(application);

        noteTypeDao = noteTypeDatabase.noteDao();
        noteTextDao = noteTextDatabase.noteTextDao();

        noteTypeList = noteTypeDao.getAllNotes();
    }

    public LiveData<List<NoteType>> getNoteTypeList() {
        return noteTypeList;
    }

    public void insertNoteText(NoteText noteText) {
        new insertNoteTextAsyncTask(noteTypeDao, noteTextDao,
                Objects.requireNonNull(noteTypeList.getValue()).size() + 1)
                .execute(noteText);
    }

    public void deleteNoteText(NoteText noteText) {
        new deleteNoteTextAsyncTask(noteTypeDao, noteTextDao, Objects.requireNonNull(noteTypeList.getValue()).size()).execute(noteText);
    }

    public void updateNoteText(NoteText noteText) {
        new updateNoteAsyncTask(noteTextDao, noteText).execute();
    }

    public void moveNoteText(NoteType holdingNote, NoteType destNote, ListViewModel model) {
        model.inProgress = true;
        new moveNoteAsyncTask(noteTypeDao, holdingNote, destNote, model).execute();
    }

    public Object getNote(NoteType noteType) {
        if (noteType.noteType == 1) {
            return noteTextDao.getNoteById(noteType.noteTypeId);
        }
        return null;
    }

    private static class insertNoteTextAsyncTask extends AsyncTask<NoteText, Void, Void> {

        private NoteTypeDao noteTypeDao;
        private NoteTextDao noteTextDao;
        private int listPos;

        insertNoteTextAsyncTask(NoteTypeDao noteTypeDao, NoteTextDao noteTextDao, int listPos) {
            this.noteTypeDao = noteTypeDao;
            this.noteTextDao = noteTextDao;
            this.listPos = listPos;
        }

        @Override
        protected Void doInBackground(NoteText... noteTexts) {
            NoteType temp = new NoteType(NoteType.TYPE_TEXT);
            temp.position = listPos;
            noteTypeDao.insertNote(temp);

            NoteText note = noteTexts[0];
            note.noteTextId = temp.noteTypeId;
            noteTextDao.insertNote(note);

            return null;
        }
    }

    private static class deleteNoteTextAsyncTask extends AsyncTask<NoteText, Void, Void> {

        private NoteTypeDao noteTypeDao;
        private NoteTextDao noteTextDao;
        private int listSize;

        deleteNoteTextAsyncTask(NoteTypeDao noteTypeDao, NoteTextDao noteTextDao, int listSize) {
            this.noteTypeDao = noteTypeDao;
            this.noteTextDao = noteTextDao;
            this.listSize = listSize;
        }

        @Override
        protected Void doInBackground(NoteText... noteTexts) {

            NoteText temp = noteTextDao.getNoteById(noteTexts[0].noteTextId);
            NoteType type = noteTypeDao.getNoteById(temp.noteTextId);
            NoteType next = noteTypeDao.getNoteByPos(type.position + 1);

            noteTextDao.deleteNoteById(temp.noteTextId);
            noteTypeDao.deleteNoteById(type.noteTypeId);

            if (next != null) {
                moveNotePosUp(next.position);
            }
            return null;
        }

        private void moveNotePosUp(int firstItemPos) {
            NoteType current;
            for (int i = firstItemPos; i <= listSize; i++) {
                current = noteTypeDao.getNoteByPos(i);
                current.position = i - 1;
                noteTypeDao.updateNote(current);
            }
        }
    }

    private static class updateNoteAsyncTask extends AsyncTask<Void, Void, Void> {
        private NoteTextDao noteTextDao;
        private NoteText noteText;

        updateNoteAsyncTask(NoteTextDao noteTextDao, NoteText noteText) {
            this.noteTextDao = noteTextDao;
            this.noteText = noteText;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            noteTextDao.updateNote(noteText);
            return null;
        }
    }

    private static class moveNoteAsyncTask extends AsyncTask<Void, Void, Void> {
        private NoteTypeDao noteTypeDao;
        private NoteType holdingNote;
        private NoteType destNote;
        private ListViewModel model;

        moveNoteAsyncTask(NoteTypeDao noteTypeDao, NoteType holdingNote, NoteType destNote, ListViewModel model) {
            this.noteTypeDao = noteTypeDao;
            this.holdingNote = holdingNote;
            this.destNote = destNote;
            this.model = model;
        }

        @Override
        protected Void doInBackground(Void... voids) {

            int fromPosition = holdingNote.position;
            int toPosition = destNote.position;

            if (fromPosition < toPosition) {
                for (int i = fromPosition; i < toPosition; i++) {
                    swap(i, i + 1);
                }

                // Moved up
            } else {
                for (int i = fromPosition; i > toPosition; i--) {
                    swap(i, i - 1);
                }
            }
            model.inProgress = false;
            return null;
        }

        // Postion of notes to swap
        private void swap(int firstPos, int secondPos) {
            NoteType firstTextNote = noteTypeDao.getNoteByPos(firstPos);
            NoteType secondTextNote = noteTypeDao.getNoteByPos(secondPos);

            firstTextNote.position = secondPos;
            secondTextNote.position = firstPos;

            noteTypeDao.updateNote(firstTextNote);
            noteTypeDao.updateNote(secondTextNote);
        }
    }
}
