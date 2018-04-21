package com.ashiana.zlifno.alder.data.entity;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import com.ashiana.zlifno.alder.data.dao.NoteTypeDao;

import java.io.Serializable;

@Entity(tableName = "note_text")
public class NoteText implements Serializable {

    public NoteText(String title, String content, String timeCreated) {
        this.title = title;
        this.content = content;
        this.timeCreated = timeCreated;
    }

    @PrimaryKey
    @NonNull
    public int noteTextId;

    @NonNull
    public String title;

    public String content;

    public String timeCreated;

    public final int getType() {
        return 1;
    }
}
