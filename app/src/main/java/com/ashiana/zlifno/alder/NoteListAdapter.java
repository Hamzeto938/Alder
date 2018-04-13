package com.ashiana.zlifno.alder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ashiana.zlifno.to_do.R;
import com.ashiana.zlifno.alder.data.Note;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.List;

public class NoteListAdapter extends RecyclerView.Adapter<NoteListAdapter.NoteViewHolder> {

    class NoteViewHolder extends RecyclerView.ViewHolder {
        private final MaterialEditText noteTitleView;
        private final MaterialEditText noteTimeCreatedView;

        private NoteViewHolder(View itemView) {
            super(itemView);
            noteTitleView = itemView.findViewById(R.id.card_note_title);
            noteTitleView.setInputType(0);

            noteTimeCreatedView = itemView.findViewById(R.id.card_time_created);
            noteTimeCreatedView.setInputType(0);
        }
    }

    private final LayoutInflater mInflater;
    private List<Note> mNotes; // Cached copy of notes

    NoteListAdapter(Context context) {
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public NoteViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.recyclerview_item_note, parent, false);
        return new NoteViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(NoteViewHolder holder, int position) {
        Note current = mNotes.get(position);
        holder.noteTitleView.setText(current.getTitle());
        holder.noteTimeCreatedView.setText(current.getTimeCreated());
    }

    void setNotes(List<Note> words) {
        mNotes = words;
        Log.v("APPD", "Adapter: Item count is " + getItemCount());
        notifyDataSetChanged();
    }

    // getItemCount() is called many times, and when it is first called,
    // mWords has not been updated (means initially, it's null, and we can't return null).
    @Override
    public int getItemCount() {
        if (mNotes != null)
            return mNotes.size();
        else return 0;
    }
}

