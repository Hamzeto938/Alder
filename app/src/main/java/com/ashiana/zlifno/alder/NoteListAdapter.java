package com.ashiana.zlifno.alder;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ashiana.zlifno.alder.Fragment.ListFragment;
import com.ashiana.zlifno.alder.data.entity.NoteText;
import com.ashiana.zlifno.alder.data.entity.NoteType;
import com.ashiana.zlifno.alder.view_model.ListViewModel;
import com.skyfishjy.library.RippleBackground;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NoteListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final LayoutInflater mInflater;
    private ListViewModel listViewModel;
    private List<NoteType> notes; // Cached copy of notes

    public NoteListAdapter(Context context, ListViewModel listViewModel) {
        mInflater = LayoutInflater.from(context);
        this.listViewModel = listViewModel;
    }

    @Override
    public int getItemViewType(int position) {
        int type = notes.get(position).noteType;
        return type;

    }

    public void setNotes(List<NoteType> words) {
        notes = words;
        Log.v("Alder", "Adapter: Item count is " + getItemCount());
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public NoteTextViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        switch (viewType) {
            case NoteType.TYPE_TEXT:
                View itemView = mInflater.inflate(R.layout.text_note_card, parent, false);
                return new NoteTextViewHolder(itemView);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        // Text Note
        if (notes.get(position).noteType == 1) {
            NoteType noteType = notes.get(position);
            NoteText noteText = (NoteText) listViewModel.getNote(noteType);
            NoteTextViewHolder textViewHolder = (NoteTextViewHolder) holder;

            textViewHolder.currentItem = noteText;

            String title = textViewHolder.currentItem.title;

            if (title.length() > 20) {
                textViewHolder.noteTitleView.setSingleLine(false);
            }

            textViewHolder.noteTitleView.setText(textViewHolder.currentItem.title);
            textViewHolder.noteTimeCreatedView.setText(textViewHolder.currentItem.timeCreated);

            textViewHolder.noteTitleView.setTransitionName("transition" + position);
//        holder.noteTimeCreatedView.setTransitionName("transition" + position);

            if (ListFragment.isNewNote instanceof NoteText && noteText.equals(ListFragment.isNewNote)) {
                RippleBackground rippleBackground = textViewHolder.parent.findViewById(R.id.content);
                rippleBackground.startRippleAnimation();
                new CountDownTimer(2000, 1000) {

                    public void onTick(long millisUntilFinished) {

                    }

                    public void onFinish() {
                        rippleBackground.stopRippleAnimation();
                    }

                }.start();
                ListFragment.isNewNote = null;
            }
        }

    }

    // getItemCount() is called many times, and when it is first called,
    // mWords has not been updated (means initially, it's null, and we can't return null).
    @Override
    public int getItemCount() {
        if (notes != null)
            return notes.size();
        else return 0;
    }


    public void deleteNote(int position) {
        notifyItemRemoved(position);
    }

    public Object getNote(int position) {
        return notes.get(position);
    }

    class NoteTextViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.card_note_title)
        public TextView noteTitleView;
        @BindView(R.id.card_note_time_text)
        public TextView noteTimeCreatedView;

        public View parent;

        public NoteText currentItem;

        public NoteTextViewHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(itemView);

            parent = itemView;
            noteTitleView = itemView.findViewById(R.id.card_note_title);
            noteTitleView.setInputType(0);

            noteTimeCreatedView = itemView.findViewById(R.id.card_note_time_text);
            noteTimeCreatedView.setInputType(0);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            NoteText current = (NoteText) listViewModel.getNote(notes.get(getAdapterPosition()));

            ListFragment.updateNote(current, getAdapterPosition(), v);

        }

    }
}