package com.ashiana.zlifno.alder.data.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import com.ashiana.zlifno.alder.data.dao.NoteTextDao;
import com.ashiana.zlifno.alder.data.dao.NoteTypeDao;
import com.ashiana.zlifno.alder.data.entity.NoteText;
import com.ashiana.zlifno.alder.data.entity.NoteType;

@Database(entities = {NoteText.class}, version = 1)
public abstract class NoteTextDatabase extends RoomDatabase {

    public abstract NoteTextDao noteTextDao();

    private static NoteTextDatabase INSTANCE;

    public static NoteTextDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (NoteTextDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            NoteTextDatabase.class, "note_text_database")
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}