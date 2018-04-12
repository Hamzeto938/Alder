package com.ashiana.zlifno.to_do.data;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import java.util.List;

// Makes an API off of the Dao
public class NoteRepository {
    private NoteDao noteDao;
    private LiveData<List<Note>> notesList;

    public NoteRepository(Application application) {
        NoteRoomDatabase database = NoteRoomDatabase.getDatabase(application);
        noteDao = database.noteDao();
        notesList = noteDao.getAllNotes();
    }

    public LiveData<List<Note>> getNotesList() {
        return notesList;
    }

    public LiveData<List<Note>> getNotesListByTimeAsc() {
        notesList = noteDao.getAllNotesByTimeAsc();
        return notesList;
    }

    public LiveData<List<Note>> getNotesListByTimeDesc() {
        notesList = noteDao.getAllNotesByTimeDesc();
        return notesList;
    }

    public void insertNote(Note note) {
        new insertAsyncTask(noteDao).execute(note);
    }

    private static class insertAsyncTask extends AsyncTask<Note, Void, Void> {

        private NoteDao asyncTaskNoteDao;

        insertAsyncTask(NoteDao dao) {
            asyncTaskNoteDao = dao;
        }

        @Override
        protected Void doInBackground(final Note... params) {
            asyncTaskNoteDao.insertNote(params[0]);
            return null;
        }
    }

    public void deleteNote(Note note) {
        noteDao.deleteNote(note);
        new deleteAsyncTask(noteDao).execute(note);
    }

    private static class deleteAsyncTask extends AsyncTask<Note, Void, Void> {
        private NoteDao asyncTaskNoteDao;

        deleteAsyncTask(NoteDao dao) {
            asyncTaskNoteDao = dao;
        }

        @Override
        protected Void doInBackground(final Note... params) {
            asyncTaskNoteDao.deleteNote(params[0]);
            return null;
        }
    }

    public void updateNote(Note note) {
        noteDao.updateNote(note);
        new updateAsyncTask(noteDao).execute(note);
    }

    private static class updateAsyncTask extends AsyncTask<Note, Void, Void> {
        private NoteDao asyncTaskNoteDao;

        updateAsyncTask(NoteDao dao) {
            asyncTaskNoteDao = dao;
        }

        @Override
        protected Void doInBackground(final Note... params) {
            asyncTaskNoteDao.updateNote(params[0]);
            return null;
        }
    }

}