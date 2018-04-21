package com.ashiana.zlifno.alder.data.entity;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import java.io.Serializable;

@Entity(tableName = "note_type")
public class NoteType implements Serializable {

    public NoteType(int noteType) {
        this.noteType = noteType;
    }

    @PrimaryKey(autoGenerate = true)
    @NonNull
    public int noteTypeId;

    @NonNull
    public int noteType;

    @NonNull
    public int position;

    public final static int TYPE_TEXT = 1;

}
