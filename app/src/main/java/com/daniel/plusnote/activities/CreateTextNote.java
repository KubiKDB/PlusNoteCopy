package com.daniel.plusnote.activities;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.daniel.plusnote.entities.Note;
import com.daniel.plusnote.R;
import com.daniel.plusnote.database.NotesDatabase;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class CreateTextNote extends AppCompatActivity {
    private ImageButton
            cancelButton,
            deleteButton,
            editButton,
            reminderButton,
            shareButton,
            doneButton,
            save_reminder,
            delete_reminder,
            cancel_reminder_choose;
    private EditText inputTextTitle, inputTextNote;
    private Note alreadyAvailableNote;
    ConstraintLayout cl;
    TextView date_show;
    private ConstraintLayout blur_reminder;
    boolean isReminder = false;
    boolean isPast = false;
    AlarmManager alarmManager;
    PendingIntent pendingIntent;
    public static boolean isActivated = false;
    private int alarmID = 0;
    String reminderTime;
    private boolean isEdited = false;
    private ImageButton reminder_button2;
    private TextView time_view_note;

    @SuppressLint("BatteryLife")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_text_note);
        ///////
        cancelButton = findViewById(R.id.cancelButton);
        deleteButton = findViewById(R.id.deleteButton);
        editButton = findViewById(R.id.editButton);
        reminderButton = findViewById(R.id.reminderButton);
        shareButton = findViewById(R.id.shareButton);
        doneButton = findViewById(R.id.doneButton);
        reminder_button2 = findViewById(R.id.reminderButton2);
        time_view_note = findViewById(R.id.time_view_note);
        ///////
        inputTextTitle = findViewById(R.id.inputTextTitle);
        inputTextNote = findViewById(R.id.inputTextNote);
        //////
        save_reminder = findViewById(R.id.save_reminder);
        delete_reminder = findViewById(R.id.delete_reminder);
        cancel_reminder_choose = findViewById(R.id.cancel_reminder_choose);
        cl = findViewById(R.id.tp_container);
        date_show = findViewById(R.id.date_show);
        blur_reminder = findViewById(R.id.blur_reminder);
        save_reminder.setEnabled(false);
        save_reminder.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#4000FF00")));

//        String[] str = MainActivity.notesDay.split("_");
//
//        LocalDate localDate = LocalDate.of(
//                Integer.parseInt(str[0]),
//                Integer.parseInt(str[1]),
//                Integer.parseInt(str[2]));
//
//        if (localDate.isBefore(LocalDate.now())){
//            reminderButton.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#7057ACF9")));
//        }

        TimePicker timePicker = findViewById(R.id.time_picker);
        timePicker.setIs24HourView(true);
        timePicker.setOnTimeChangedListener((timePicker1, i, i1) -> {
            reminderTime = i + ":" + i1 + ":" + MainActivity.notesDay;
            save_reminder.setEnabled(true);
            save_reminder.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#00FF00")));

            final DateTimeFormatter hhmm = DateTimeFormatter.ofPattern("HH:mm");
            final DateTimeFormatter noteDayF = DateTimeFormatter.ofPattern("yyyy_MM_dd");

            LocalDate ld = LocalDate.now();
            LocalTime ldt = LocalTime.from(LocalTime.now());

            String ld1 = noteDayF.format(ld);
            ld = LocalDate.from(noteDayF.parse(ld1));
            String[] split = hhmm.format(ldt).split(":");

            LocalDate test;
            if (alreadyAvailableNote != null) {
                test = LocalDate.from(noteDayF.parse(alreadyAvailableNote.getDate()));
            } else {
                test = LocalDate.from(noteDayF.parse(MainActivity.notesDay));
            }

            if (test.isEqual(ld)) {
                if (Integer.parseInt(split[0]) > i) {
                    isPast = true;
                } else isPast = Integer.parseInt(split[0]) == i && Integer.parseInt(split[1]) > i1;
            } else isPast = test.isBefore(ld);
        });
        cancel_reminder_choose.setOnClickListener(v -> {
            blur_reminder.setVisibility(View.GONE);
            cl.setVisibility(View.GONE);
        });
        delete_reminder.setOnClickListener(view -> {
            if (alreadyAvailableNote.isReminderSet()) {
                AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                Intent intent = new Intent(this, AlarmBroadcast.class);
                alarmID = alreadyAvailableNote.getReminder_id();
                PendingIntent pendingIntent = PendingIntent.getBroadcast(
                        getApplicationContext(),
                        alarmID,
                        intent,
                        PendingIntent.FLAG_IMMUTABLE);
                alarmManager.cancel(pendingIntent);
                isReminder = false;
                alreadyAvailableNote.setReminderSet(false);
                alreadyAvailableNote.setReminder_id(0);
                Toast.makeText(this, "Reminder deleted", Toast.LENGTH_SHORT).show();
                saveNote();
            }
        });
        reminderButton.setOnClickListener(view -> setReminder());
        save_reminder.setOnClickListener(view -> {
            if (alreadyAvailableNote != null) {
                AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                Intent intent = new Intent(this, AlarmBroadcast.class);
                alarmID = alreadyAvailableNote.getReminder_id();

                PendingIntent pendingIntent = PendingIntent.getBroadcast(
                        getApplicationContext(),
                        alarmID,
                        intent,
                        PendingIntent.FLAG_IMMUTABLE);
                alarmManager.cancel(pendingIntent);

                alreadyAvailableNote.setReminder_id(0);
            }

            if (isPast) {
                Toast.makeText(this, "Set future time", Toast.LENGTH_SHORT).show();
                return;
            }

            String[] temp = reminderTime.split(":");
            Log.e("temp[0]", temp[0]);
            setAlarm(
                    String.valueOf(inputTextTitle.getText()),
                    temp[2],
                    temp[0] + ":" + temp[1]
            );

            isReminder = true;

            cl.setVisibility(View.GONE);
            blur_reminder.setVisibility(View.GONE);
            saveNote();
        });
        blur_reminder.setOnClickListener(view -> {
            blur_reminder.setVisibility(View.GONE);
            cl.setVisibility(View.GONE);
        });
        time_view_note.setOnClickListener(v -> setReminder());
        //////

        editButton.setEnabled(false);
        shareButton.setEnabled(false);
        doneButton.setEnabled(false);
        reminderButton.setEnabled(false);

        shareButton.setOnClickListener(view -> share());

        inputTextNote.setOnFocusChangeListener((view, b) -> {
            if (b) {
                inputTextNote.setSelection(inputTextNote.length());
            }
        });

        inputTextTitle.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {
            }

            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                if (!inputTextTitle.getText().toString().trim().isEmpty()) {
                    doneButton.setEnabled(true);
                    doneButton.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#00FF00")));
                    reminderButton.setEnabled(true);
                    reminderButton.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#57ACF9")));
                } else {
                    doneButton.setEnabled(false);
                    doneButton.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#4000FF00")));
                    reminderButton.setEnabled(false);
                    reminderButton.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#7057ACF9")));
                }
            }
        });

        inputTextTitle.requestFocus();
        inputTextTitle.setFocusableInTouchMode(true);
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(inputTextTitle, InputMethodManager.SHOW_FORCED);
        inputTextTitle.post(() -> inputTextTitle.setSelection(inputTextTitle.getText().length()));

        cancelButton.setOnClickListener(view -> {
            onBackPressed();
            imm.hideSoftInputFromWindow(inputTextTitle.getWindowToken(), InputMethodManager.RESULT_UNCHANGED_SHOWN);
        });
        doneButton.setOnClickListener(view -> {
            saveNote();
            imm.hideSoftInputFromWindow(inputTextTitle.getWindowToken(), InputMethodManager.RESULT_UNCHANGED_SHOWN);
        });

        editButton.setOnClickListener(view -> {
            inputTextTitle.setEnabled(true);
            inputTextNote.setEnabled(true);
            inputTextTitle.requestFocus();
            inputTextTitle.setFocusableInTouchMode(true);
            imm.showSoftInput(inputTextTitle, InputMethodManager.SHOW_FORCED);
            inputTextTitle.post(() -> inputTextTitle.setSelection(inputTextTitle.getText().length()));
            doneButton.setEnabled(true);
            doneButton.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#00FF00")));
            isEdited = true;
        });

        if (getIntent().getBooleanExtra("isViewOrUpdate", false)) {
            alreadyAvailableNote = (Note) getIntent().getSerializableExtra("note");
            setViewOrUpdateNote();
            if (getIntent().getBooleanExtra("deleteNote", false)) {
                deleteNote();
            }
            if (getIntent().getBooleanExtra("shareNote", false)) {
                share();
            }
            if (getIntent().getBooleanExtra("setReminder", false)) {
                setReminder();
            }
        }

        if (alreadyAvailableNote != null) {
            deleteButton.setOnClickListener(view -> deleteNote());
        }
    }

    private void setReminder() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(inputTextTitle.getWindowToken(), InputMethodManager.RESULT_UNCHANGED_SHOWN);

        final DateTimeFormatter ndf = DateTimeFormatter.ofPattern("yyyy_MM_dd");
        LocalDate ld = LocalDate.now();
        LocalDate ld1;
        if (alreadyAvailableNote != null) {
            ld1 = LocalDate.from(ndf.parse(alreadyAvailableNote.getDate()));
        } else {
            ld1 = LocalDate.from(ndf.parse(MainActivity.notesDay));
        }
        if (ld.isAfter(ld1)) {
            Toast.makeText(this, "Set future date", Toast.LENGTH_SHORT).show();
            return;
        }
        blur_reminder.setVisibility(View.VISIBLE);
        cl.setVisibility(View.VISIBLE);
        @SuppressLint("SimpleDateFormat") final DateFormat noteDayF = new SimpleDateFormat("yyyy_MM_dd");
        @SuppressLint("SimpleDateFormat") final DateFormat forDay = new SimpleDateFormat("dd MMM yyyy");
        Date date = null;
        if (alreadyAvailableNote == null) {
            try {
                date = noteDayF.parse(MainActivity.notesDay);
            } catch (ParseException ignored) {
            }
            String temp = null;
            if (date != null) {
                temp = forDay.format(date);
            }
            date_show.setText(temp);
        } else {
            try {
                date = noteDayF.parse(alreadyAvailableNote.getDate());
            } catch (ParseException ignored) {
            }
            String temp = null;
            if (date != null) {
                temp = forDay.format(date);
            }
            date_show.setText(temp);
        }
    }

    private void setAlarm(String text, String date, String time) {
        Intent intent = new Intent(this, AlarmBroadcast.class);
        intent.putExtra("event", text);
        intent.putExtra("time", time);
        intent.putExtra("date", date);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        alarmID = (int) System.currentTimeMillis();
        pendingIntent = PendingIntent.getBroadcast(
                this,
                alarmID,
                intent,
                PendingIntent.FLAG_IMMUTABLE);
        String dateandtime = date + " " + time;
        @SuppressLint("SimpleDateFormat")
        DateFormat formatter = new SimpleDateFormat("yyyy_MM_dd hh:mm");
        Date date1 = null;
        try {
            date1 = formatter.parse(dateandtime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        alarmManager = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);

        long temp = date1.getTime();
        String[] ts = time.split(":");
        if (ts[0].equals("12")){
            temp += 43200000;
        }

        Log.e("logTime", dateandtime + " // " + temp);
        AlarmManager.AlarmClockInfo aci = new AlarmManager.AlarmClockInfo(temp, pendingIntent);
        alarmManager.setAlarmClock(aci, pendingIntent);
        Toast.makeText(this, "Reminder set", Toast.LENGTH_SHORT).show();
    }

    private void share() {
        Intent myIntent = new Intent(Intent.ACTION_SEND);
        myIntent.setType("text/plain");
        String body = inputTextTitle.getText().toString() + "\n" + inputTextNote.getText().toString();
        myIntent.putExtra(Intent.EXTRA_TEXT, body);
        startActivity(Intent.createChooser(myIntent, "Share Note"));
    }

    private void deleteNote() {
        if (alreadyAvailableNote.isReminderSet()) {
            AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
            Intent intent = new Intent(this, AlarmBroadcast.class);
            alarmID = alreadyAvailableNote.getReminder_id();
            PendingIntent pendingIntent = PendingIntent.getBroadcast(
                    getApplicationContext(),
                    alarmID,
                    intent,
                    PendingIntent.FLAG_IMMUTABLE);
            alarmManager.cancel(pendingIntent);
            isReminder = false;
            alreadyAvailableNote.setReminder_id(0);
            saveNote();
        }
        @SuppressWarnings("deprecation")
        @SuppressLint("StaticFieldLeak")
        class DeleteNoteTask extends AsyncTask<Void, Void, Void> {
            @Override
            protected Void doInBackground(Void... voids) {
                NotesDatabase.getDatabase(getApplicationContext()).noteDao()
                        .deleteNote(alreadyAvailableNote);
                return null;
            }

            @Override
            protected void onPostExecute(Void unused) {
                super.onPostExecute(unused);
                Intent intent = new Intent();
                intent.putExtra("isNoteDeleted", true);
                setResult(RESULT_OK, intent);
                finish();
            }
        }
        new DeleteNoteTask().execute();
    }

    private void setViewOrUpdateNote() {
        inputTextTitle.setEnabled(false);
        inputTextNote.setEnabled(false);
        editButton.setEnabled(true);
        shareButton.setEnabled(true);
        doneButton.setEnabled(false);

        if (alreadyAvailableNote.isReminderSet()) {
            delete_reminder.setVisibility(View.VISIBLE);
            cancel_reminder_choose.setVisibility(View.GONE);

            reminder_button2.setVisibility(View.VISIBLE);
            reminderButton.setVisibility(View.INVISIBLE);
            time_view_note.setVisibility(View.VISIBLE);
            String[] split = alreadyAvailableNote.getReminder_time().split(":");
            if (split[1].length() == 1) {
                split[1] = "0" + split[1];
            }
            if (split[0].length() == 1) {
                split[0] = "0" + split[0];
            }
            time_view_note.setText(split[0] + ":" + split[1]);
        }

        Log.e("DateSavedText", alreadyAvailableNote.getDate());

        deleteButton.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#57ACF9")));
        editButton.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#57ACF9")));
        shareButton.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#57ACF9")));


        inputTextTitle.setText(alreadyAvailableNote.getTextNoteTitle());
        inputTextNote.setText(alreadyAvailableNote.getTextNoteText());
    }

    private void saveNote() {
        if (inputTextTitle.getText().toString().trim().isEmpty()) {
            return;
        }
        final Note note = new Note();
        note.setTextNoteTitle(inputTextTitle.getText().toString());
        note.setTextNoteText(inputTextNote.getText().toString());
        note.setIs_list(false);
        note.setIs_image(false);
        note.setIs_voice(false);
        if (isEdited) {
            if (alreadyAvailableNote.isReminderSet()) {
                AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                Intent intent = new Intent(this, AlarmBroadcast.class);
                alarmID = alreadyAvailableNote.getReminder_id();

                PendingIntent pendingIntent = PendingIntent.getBroadcast(
                        getApplicationContext(),
                        alarmID,
                        intent,
                        PendingIntent.FLAG_IMMUTABLE);
                alarmManager.cancel(pendingIntent);

                alreadyAvailableNote.setReminder_id(0);
                reminderTime = alreadyAvailableNote.getReminder_time();

                String[] temp = reminderTime.split(":");
                setAlarm(
                        String.valueOf(inputTextTitle.getText()),
                        temp[2],
                        temp[0] + ":" + temp[1]
                );
                isReminder = true;
            }
        }

        if (alreadyAvailableNote != null) {
            note.setDate(alreadyAvailableNote.getDate());
            note.setId(alreadyAvailableNote.getId());
            note.setReminderSet(alreadyAvailableNote.isReminderSet());
            if (reminderTime != null) {
                note.setReminder_time(reminderTime);
                note.setReminderSet(true);
                note.setReminder_id(alarmID);
            } else {
                note.setReminder_time(alreadyAvailableNote.getReminder_time());
                note.setReminder_id(alreadyAvailableNote.getReminder_id());
            }
        } else {
            if (isReminder) {
                note.setReminder_time(reminderTime);
                note.setReminder_id(alarmID);
            }
            note.setReminderSet(isReminder);
            note.setDate(MainActivity.notesDay);
        }

        @SuppressLint("StaticFieldLeak")
        @SuppressWarnings("deprecation")
        class SaveNoteTask extends AsyncTask<Void, Void, Void> {
            @Override
            protected Void doInBackground(Void... voids) {
                NotesDatabase.getDatabase(getApplicationContext()).noteDao().insertNote(note);
                return null;
            }

            @Override
            protected void onPostExecute(Void unused) {
                super.onPostExecute(unused);
                Intent intent = new Intent();
                setResult(RESULT_OK, intent);
                finish();
            }
        }
        new SaveNoteTask().execute();
    }
}