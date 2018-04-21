package com.ashiana.zlifno.alder.data.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import com.ashiana.zlifno.alder.data.dao.NoteTypeDao;
import com.ashiana.zlifno.alder.data.entity.NoteType;

// Makes the database using the entity
@Database(entities = {NoteType.class}, version = 1)
public abstract class NoteTypeDatabase extends RoomDatabase {

    public abstract NoteTypeDao noteDao();

    private static NoteTypeDatabase INSTANCE;

    public static NoteTypeDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (NoteTypeDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            NoteTypeDatabase.class, "note_type_database")
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
