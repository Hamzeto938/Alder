package com.ashiana.zlifno.alder.Activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import com.ashiana.zlifno.alder.Fragment.AddTextNoteFragment;
import com.ashiana.zlifno.alder.Fragment.ListFragment;
import com.ashiana.zlifno.alder.R;
import com.ashiana.zlifno.alder.data.entity.NoteText;

public class ListActivity extends AppCompatActivity implements ListFragment.MainIntents, AddTextNoteFragment.ChangeNoteIntent {

    private ListFragment listFragment;
    private FragmentManager fragmentManager;
    private SharedPreferences prefs;
//    private SharedPreferences.Editor editor;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        // For testing with SharedPreferences
        prefs = getSharedPreferences("alder_prefs", Context.MODE_PRIVATE);
//        editor = prefs.edit();
//        editor.clear();
//        editor.apply();

        fragmentManager = getSupportFragmentManager();
        listFragment = ListFragment.newInstance();
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.root_activity, listFragment)
                .commit();
    }

    @Override
    public void onBackPressed() {
        if (prefs.getBoolean(ListFragment.TAG_FINISHED_FINAL_SPOTLIGHT, false)) {
            if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
                String last = getSupportFragmentManager().getBackStackEntryAt(
                        getSupportFragmentManager()
                                .getBackStackEntryCount() - 1)
                        .getName();

                // Coming back from AddTextNote
                if (last.equals("AddTextNote")) {
                    listFragment.closeFAB();
                    super.onBackPressed();
                }
            } else {
                if (!listFragment.closeFAB()) {
                    super.onBackPressed();
                }
            }
        }
    }

    private void changeBarColors(int color) {
        getWindow().setStatusBarColor(getResources().getColor(color));
        getWindow().setNavigationBarColor(getResources().getColor(color));
        findViewById(R.id.my_toolbar).setBackgroundColor(getResources().getColor(color));
    }

    @Override
    public void newNote() {

        changeBarColors(R.color.colorPrimaryDark);
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .add(R.id.root_activity, new AddTextNoteFragment())
                .addToBackStack("AddTextNote")
                .commit();
    }

    // Called on touch
    @Override
    public void updateNote(Object noteType, int position, View v) {
        changeBarColors(R.color.colorPrimaryDark);
        if (noteType instanceof NoteText) {
            AddTextNoteFragment fragment = new AddTextNoteFragment();
            Bundle args = new Bundle();
            args.putString("transitionName", "transition" + position);
            args.putSerializable("current", (NoteText) noteType);
            fragment.setArguments(args);
            getFragmentManager().popBackStack();
            getSupportFragmentManager().beginTransaction()
                    .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                    .replace(R.id.root_activity, fragment)
                    .addToBackStack("AddTextNote")
                    .commit();
        }

    }


    private void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(findViewById(R.id.root_activity).getWindowToken(), 0);
    }

    // New noteText to add
    @Override
    public void addNoteText(NoteText noteText) {
        changeBarColors(R.color.colorPrimary);
        listFragment.closeFAB();
        hideKeyboard();
        getSupportFragmentManager().popBackStack();
        listFragment.addNote(noteText);
    }

    // Update contents of given noteText
    @Override
    public void saveNoteText(NoteText noteText) {
//        changeBarColors(R.color.colorPrimary);
        listFragment.closeFAB();
        hideKeyboard();
        getSupportFragmentManager().popBackStack();
        listFragment.saveNote(noteText);
    }

    @Override
    public void titleEmpty() {
        listFragment.closeFAB();
        hideKeyboard();
        getSupportFragmentManager().popBackStack();
        showSnackBar("Title can't be empty", android.R.color.holo_red_light);
        Vibrator v = (Vibrator) getApplicationContext().getSystemService(Context.VIBRATOR_SERVICE);

// Vibrate for 1 seconds
        assert v != null;
        v.vibrate(1000);
    }

    private void showSnackBar(String test, int color) {

        Snackbar snackbar;
        snackbar = Snackbar.make(this.findViewById(R.id.coordinator_layout), test, Snackbar.LENGTH_LONG);
        View snackBarView = snackbar.getView();
        snackBarView.setBackgroundColor(getResources().getColor(color));
        TextView textView = (TextView) snackBarView.findViewById(android.support.design.R.id.snackbar_text);
        textView.setTextColor(getResources().getColor(android.R.color.white));
        snackbar.show();
    }
}