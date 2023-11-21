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
import android.widget.CheckBox;
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

public class CreateListNote extends AppCompatActivity {

    ImageButton
            cancelButtonList,
            deleteButtonList,
            editButtonList,
            reminderButtonList,
            shareButtonList,
            doneButtonList;
    private EditText inputListTitle;
    private EditText[] inputLists = new EditText[25];
    private CheckBox[] checkBoxes = new CheckBox[25];
    private Note alreadyAvailableNote;
    private ImageButton
            save_reminder_list,
            delete_reminder_list;
    ConstraintLayout
            cl_list,
            blur_reminder_list;
    TextView date_show_list;
    boolean isReminder = false;
    boolean isPast = false;
    AlarmManager alarmManager;
    PendingIntent pendingIntent;
    public static boolean isActivated = false;
    private int alarmID = 0;
    String reminderTime;
    private boolean isEdited;
    private ImageButton cancel_reminder_choose_list;
    private ImageButton reminder_button_list2;
    private TextView time_view_note_list;


    @SuppressLint("BatteryLife")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_list_note);
        cancelButtonList = findViewById(R.id.cancelButtonList);
        deleteButtonList = findViewById(R.id.deleteButtonList);
        editButtonList = findViewById(R.id.editButtonList);
        reminderButtonList = findViewById(R.id.reminderButtonList);
        shareButtonList = findViewById(R.id.shareButtonList);
        doneButtonList = findViewById(R.id.doneButtonList);
        cancel_reminder_choose_list = findViewById(R.id.cancel_reminder_choose_list);
        /////////
        inputListTitle = findViewById(R.id.inputListName);
        time_view_note_list = findViewById(R.id.time_view_note_list);
        reminder_button_list2 = findViewById(R.id.reminderButtonList2);
        //////////
        save_reminder_list = findViewById(R.id.save_reminder_list);
        delete_reminder_list = findViewById(R.id.delete_reminder_list);
        cl_list = findViewById(R.id.tp_container_list);
        date_show_list = findViewById(R.id.date_show_list);
        blur_reminder_list = findViewById(R.id.blur_reminder_list);
        save_reminder_list.setEnabled(false);
        save_reminder_list.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#4000FF00")));
        TimePicker timePicker = findViewById(R.id.time_picker_list);
        timePicker.setIs24HourView(true);
        timePicker.setOnTimeChangedListener((timePicker1, i, i1) -> {
            reminderTime = i + ":" + i1 + ":" + MainActivity.notesDay;
            save_reminder_list.setEnabled(true);
            save_reminder_list.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#00FF00")));

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
        delete_reminder_list.setOnClickListener(view -> {
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
                Toast.makeText(this, "Reminder deleted", Toast.LENGTH_SHORT).show();
                saveNote();
            }
        });
        reminderButtonList.setOnClickListener(view -> setReminder());
        save_reminder_list.setOnClickListener(view -> {
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
            setAlarm(
                    String.valueOf(inputListTitle.getText()),
                    temp[2],
                    temp[0] + ":" + temp[1]
            );

            isReminder = true;

            cl_list.setVisibility(View.GONE);
            blur_reminder_list.setVisibility(View.GONE);
            saveNote();
        });
        blur_reminder_list.setOnClickListener(view -> {
            blur_reminder_list.setVisibility(View.GONE);
            cl_list.setVisibility(View.GONE);
        });
        cancel_reminder_choose_list.setOnClickListener(v -> {
            blur_reminder_list.setVisibility(View.GONE);
            cl_list.setVisibility(View.GONE);
        });
        time_view_note_list.setOnClickListener(v -> setReminder());
        /////////
        editButtonList.setEnabled(false);
        shareButtonList.setEnabled(false);
        doneButtonList.setEnabled(false);
        reminderButtonList.setEnabled(false);

        shareButtonList.setOnClickListener(view -> share());

        inputListTitle.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {
            }

            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                if (!inputListTitle.getText().toString().trim().isEmpty()) {
                    doneButtonList.setEnabled(true);
                    doneButtonList.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#00FF00")));
                    reminderButtonList.setEnabled(true);
                    reminderButtonList.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#57ACF9")));
                } else {
                    doneButtonList.setEnabled(false);
                    doneButtonList.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#4000FF00")));
                    reminderButtonList.setEnabled(false);
                    reminderButtonList.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#7057ACF9")));
                }
            }
        });

        inputLists[0] = findViewById(R.id.inputList1);
        inputLists[1] = findViewById(R.id.inputList2);
        inputLists[2] = findViewById(R.id.inputList3);
        inputLists[3] = findViewById(R.id.inputList4);
        inputLists[4] = findViewById(R.id.inputList5);
        inputLists[5] = findViewById(R.id.inputList6);
        inputLists[6] = findViewById(R.id.inputList7);
        inputLists[7] = findViewById(R.id.inputList8);
        inputLists[8] = findViewById(R.id.inputList9);
        inputLists[9] = findViewById(R.id.inputList10);
        inputLists[10] = findViewById(R.id.inputList11);
        inputLists[11] = findViewById(R.id.inputList12);
        inputLists[12] = findViewById(R.id.inputList13);
        inputLists[13] = findViewById(R.id.inputList14);
        inputLists[14] = findViewById(R.id.inputList15);
        inputLists[15] = findViewById(R.id.inputList16);
        inputLists[16] = findViewById(R.id.inputList17);
        inputLists[17] = findViewById(R.id.inputList18);
        inputLists[18] = findViewById(R.id.inputList19);
        inputLists[19] = findViewById(R.id.inputList20);
        inputLists[20] = findViewById(R.id.inputList21);
        inputLists[21] = findViewById(R.id.inputList22);
        inputLists[22] = findViewById(R.id.inputList23);
        inputLists[23] = findViewById(R.id.inputList24);
        inputLists[24] = findViewById(R.id.inputList25);

        checkBoxes[0] = findViewById(R.id.checkbox1);
        checkBoxes[1] = findViewById(R.id.checkbox2);
        checkBoxes[2] = findViewById(R.id.checkbox3);
        checkBoxes[3] = findViewById(R.id.checkbox4);
        checkBoxes[4] = findViewById(R.id.checkbox5);
        checkBoxes[5] = findViewById(R.id.checkbox6);
        checkBoxes[6] = findViewById(R.id.checkbox7);
        checkBoxes[7] = findViewById(R.id.checkbox8);
        checkBoxes[8] = findViewById(R.id.checkbox9);
        checkBoxes[9] = findViewById(R.id.checkbox10);
        checkBoxes[10] = findViewById(R.id.checkbox11);
        checkBoxes[11] = findViewById(R.id.checkbox12);
        checkBoxes[12] = findViewById(R.id.checkbox13);
        checkBoxes[13] = findViewById(R.id.checkbox14);
        checkBoxes[14] = findViewById(R.id.checkbox15);
        checkBoxes[15] = findViewById(R.id.checkbox16);
        checkBoxes[16] = findViewById(R.id.checkbox17);
        checkBoxes[17] = findViewById(R.id.checkbox18);
        checkBoxes[18] = findViewById(R.id.checkbox19);
        checkBoxes[19] = findViewById(R.id.checkbox20);
        checkBoxes[20] = findViewById(R.id.checkbox21);
        checkBoxes[21] = findViewById(R.id.checkbox22);
        checkBoxes[22] = findViewById(R.id.checkbox23);
        checkBoxes[23] = findViewById(R.id.checkbox24);
        checkBoxes[24] = findViewById(R.id.checkbox25);

        for (int i = 0; i < inputLists.length; i++) {
            int finalI = i;
            inputLists[i].setOnFocusChangeListener((view, b) -> {
                if (b) {
                    inputLists[finalI].setSelection(inputLists[finalI].length());
                }
            });
        }

        for (CheckBox checkBox : checkBoxes) {
            checkBox.setVisibility(View.GONE);
        }

        for (int i = 0; i < checkBoxes.length; i++) {
            int finalI = i;
            checkBoxes[i].setOnClickListener(view -> {
                if (checkBoxes[finalI].isChecked()) {
                    inputLists[finalI].setEnabled(false);
                    inputLists[finalI].setTextColor(Color.parseColor("#707FCEFF"));
                } else {
                    inputLists[finalI].setEnabled(true);
                    inputLists[finalI].setTextColor(Color.parseColor("#7FCEFF"));
                }
                doneButtonList.setEnabled(true);
                doneButtonList.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#00FF00")));
            });
        }

        inputListTitle.requestFocus();
        inputListTitle.setFocusableInTouchMode(true);
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(inputListTitle, InputMethodManager.SHOW_FORCED);
        inputListTitle.post(() -> inputListTitle.setSelection(inputListTitle.getText().length()));

        cancelButtonList.setOnClickListener(view -> {
            onBackPressed();
            imm.hideSoftInputFromWindow(inputListTitle.getWindowToken(), InputMethodManager.RESULT_UNCHANGED_SHOWN);
        });
        doneButtonList.setOnClickListener(view -> {
            saveNote();
            imm.hideSoftInputFromWindow(inputListTitle.getWindowToken(), InputMethodManager.RESULT_UNCHANGED_SHOWN);
        });

        editButtonList.setOnClickListener(view -> {
            inputListTitle.setEnabled(true);
            for (int i = 0; i < inputLists.length; i++) {
                if (!checkBoxes[i].isChecked()) {
                    inputLists[i].setEnabled(true);
                }
            }
            inputListTitle.requestFocus();
            inputListTitle.setFocusableInTouchMode(true);
            imm.showSoftInput(inputListTitle, InputMethodManager.SHOW_FORCED);
            inputListTitle.post(() -> inputListTitle.setSelection(inputListTitle.getText().length()));
            doneButtonList.setEnabled(true);
            doneButtonList.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#00FF00")));
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
            deleteButtonList.setOnClickListener(view -> deleteNote());
        }
    }

    private void setReminder() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(inputListTitle.getWindowToken(), InputMethodManager.RESULT_UNCHANGED_SHOWN);



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
        blur_reminder_list.setVisibility(View.VISIBLE);
        cl_list.setVisibility(View.VISIBLE);
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
            date_show_list.setText(temp);
        } else {
            try {
                date = noteDayF.parse(alreadyAvailableNote.getDate());
            } catch (ParseException ignored) {
            }
            String temp = null;
            if (date != null) {
                temp = forDay.format(date);
            }
            date_show_list.setText(temp);
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

        long temp = date1.getTime();
        String[] ts = time.split(":");
        if (ts[0].equals("12")){
            temp += 43200000;
        }

        alarmManager = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
        AlarmManager.AlarmClockInfo aci = new AlarmManager.AlarmClockInfo(temp, pendingIntent);
        alarmManager.setAlarmClock(aci, pendingIntent);
        Toast.makeText(this, "Reminder set", Toast.LENGTH_SHORT).show();
    }

    private void share() {
        Intent myIntent = new Intent(Intent.ACTION_SEND);
        myIntent.setType("text/plain");
        StringBuilder body = new StringBuilder(String.valueOf(inputListTitle.getText()));
        for (int i = 0; i < inputLists.length; i++) {
            if (!inputLists[i].getText().toString().trim().isEmpty()) {
                int temp = i + 1;
                body.append("\n").append(temp).append(". ").append(inputLists[i].getText().toString());
            }
        }
        myIntent.putExtra(Intent.EXTRA_TEXT, body.toString());
        startActivity(Intent.createChooser(myIntent, "Share List"));
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
        inputListTitle.setEnabled(false);
        for (EditText inputList : inputLists) {
            inputList.setEnabled(false);
        }
        editButtonList.setEnabled(true);
        shareButtonList.setEnabled(true);
        doneButtonList.setEnabled(false);

        if (alreadyAvailableNote.isReminderSet()){
            delete_reminder_list.setVisibility(View.VISIBLE);
            cancel_reminder_choose_list.setVisibility(View.GONE);
        }

        if (alreadyAvailableNote.isReminderSet()){
            delete_reminder_list.setVisibility(View.VISIBLE);
            cancel_reminder_choose_list.setVisibility(View.GONE);

            reminder_button_list2.setVisibility(View.VISIBLE);
            reminderButtonList.setVisibility(View.INVISIBLE);
            time_view_note_list.setVisibility(View.VISIBLE);
            String[] split = alreadyAvailableNote.getReminder_time().split(":");
            if (split[1].length() == 1) {
                split[1] = "0" + split[1];
            }
            if (split[0].length() == 1) {
                split[0] = "0" + split[0];
            }
            time_view_note_list.setText(split[0] + ":" + split[1]);
        }

        Log.e("DateSavedList", alreadyAvailableNote.getDate());

        deleteButtonList.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#57ACF9")));
        editButtonList.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#57ACF9")));
        shareButtonList.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#57ACF9")));
        doneButtonList.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#4000FF00")));

        for (CheckBox checkBox : checkBoxes) {
            checkBox.setVisibility(View.VISIBLE);
        }


        inputListTitle.setText(alreadyAvailableNote.getTextNoteTitle());
        inputLists[0].setText(alreadyAvailableNote.getList_item1());
        inputLists[1].setText(alreadyAvailableNote.getList_item2());
        inputLists[2].setText(alreadyAvailableNote.getList_item3());
        inputLists[3].setText(alreadyAvailableNote.getList_item4());
        inputLists[4].setText(alreadyAvailableNote.getList_item5());
        inputLists[5].setText(alreadyAvailableNote.getList_item6());
        inputLists[6].setText(alreadyAvailableNote.getList_item7());
        inputLists[7].setText(alreadyAvailableNote.getList_item8());
        inputLists[8].setText(alreadyAvailableNote.getList_item9());
        inputLists[9].setText(alreadyAvailableNote.getList_item10());
        inputLists[10].setText(alreadyAvailableNote.getList_item11());
        inputLists[11].setText(alreadyAvailableNote.getList_item12());
        inputLists[12].setText(alreadyAvailableNote.getList_item13());
        inputLists[13].setText(alreadyAvailableNote.getList_item14());
        inputLists[14].setText(alreadyAvailableNote.getList_item15());
        inputLists[15].setText(alreadyAvailableNote.getList_item16());
        inputLists[16].setText(alreadyAvailableNote.getList_item17());
        inputLists[17].setText(alreadyAvailableNote.getList_item18());
        inputLists[18].setText(alreadyAvailableNote.getList_item19());
        inputLists[19].setText(alreadyAvailableNote.getList_item20());
        inputLists[20].setText(alreadyAvailableNote.getList_item21());
        inputLists[21].setText(alreadyAvailableNote.getList_item22());
        inputLists[22].setText(alreadyAvailableNote.getList_item23());
        inputLists[23].setText(alreadyAvailableNote.getList_item24());
        inputLists[24].setText(alreadyAvailableNote.getList_item25());

        if (alreadyAvailableNote.isIs_checked1()) {
            checkBoxes[0].setChecked(true);
            inputLists[0].setEnabled(false);
            inputLists[0].setTextColor(Color.parseColor("#707FCEFF"));
        }
        if (alreadyAvailableNote.isIs_checked2()) {
            checkBoxes[1].setChecked(true);
            inputLists[1].setEnabled(false);
            inputLists[1].setTextColor(Color.parseColor("#707FCEFF"));
        }
        if (alreadyAvailableNote.isIs_checked3()) {
            checkBoxes[2].setChecked(true);
            inputLists[2].setEnabled(false);
            inputLists[2].setTextColor(Color.parseColor("#707FCEFF"));
        }
        if (alreadyAvailableNote.isIs_checked4()) {
            checkBoxes[3].setChecked(true);
            inputLists[3].setEnabled(false);
            inputLists[3].setTextColor(Color.parseColor("#707FCEFF"));
        }
        if (alreadyAvailableNote.isIs_checked5()) {
            checkBoxes[4].setChecked(true);
            inputLists[4].setEnabled(false);
            inputLists[4].setTextColor(Color.parseColor("#707FCEFF"));
        }
        if (alreadyAvailableNote.isIs_checked6()) {
            checkBoxes[5].setChecked(true);
            inputLists[5].setEnabled(false);
            inputLists[5].setTextColor(Color.parseColor("#707FCEFF"));
        }
        if (alreadyAvailableNote.isIs_checked7()) {
            checkBoxes[6].setChecked(true);
            inputLists[6].setEnabled(false);
            inputLists[6].setTextColor(Color.parseColor("#707FCEFF"));
        }
        if (alreadyAvailableNote.isIs_checked8()) {
            checkBoxes[7].setChecked(true);
            inputLists[7].setEnabled(false);
            inputLists[7].setTextColor(Color.parseColor("#707FCEFF"));
        }
        if (alreadyAvailableNote.isIs_checked9()) {
            checkBoxes[8].setChecked(true);
            inputLists[8].setEnabled(false);
            inputLists[8].setTextColor(Color.parseColor("#707FCEFF"));
        }
        if (alreadyAvailableNote.isIs_checked10()) {
            checkBoxes[9].setChecked(true);
            inputLists[9].setEnabled(false);
            inputLists[9].setTextColor(Color.parseColor("#707FCEFF"));
        }
        if (alreadyAvailableNote.isIs_checked11()) {
            checkBoxes[10].setChecked(true);
            inputLists[10].setEnabled(false);
            inputLists[10].setTextColor(Color.parseColor("#707FCEFF"));
        }
        if (alreadyAvailableNote.isIs_checked12()) {
            checkBoxes[11].setChecked(true);
            inputLists[11].setEnabled(false);
            inputLists[11].setTextColor(Color.parseColor("#707FCEFF"));
        }
        if (alreadyAvailableNote.isIs_checked13()) {
            checkBoxes[12].setChecked(true);
            inputLists[12].setEnabled(false);
            inputLists[12].setTextColor(Color.parseColor("#707FCEFF"));
        }
        if (alreadyAvailableNote.isIs_checked14()) {
            checkBoxes[13].setChecked(true);
            inputLists[13].setEnabled(false);
            inputLists[13].setTextColor(Color.parseColor("#707FCEFF"));
        }
        if (alreadyAvailableNote.isIs_checked15()) {
            checkBoxes[14].setChecked(true);
            inputLists[14].setEnabled(false);
            inputLists[14].setTextColor(Color.parseColor("#707FCEFF"));
        }
        if (alreadyAvailableNote.isIs_checked16()) {
            checkBoxes[15].setChecked(true);
            inputLists[15].setEnabled(false);
            inputLists[15].setTextColor(Color.parseColor("#707FCEFF"));
        }
        if (alreadyAvailableNote.isIs_checked17()) {
            checkBoxes[16].setChecked(true);
            inputLists[16].setEnabled(false);
            inputLists[16].setTextColor(Color.parseColor("#707FCEFF"));
        }
        if (alreadyAvailableNote.isIs_checked18()) {
            checkBoxes[17].setChecked(true);
            inputLists[17].setEnabled(false);
            inputLists[17].setTextColor(Color.parseColor("#707FCEFF"));
        }
        if (alreadyAvailableNote.isIs_checked19()) {
            checkBoxes[18].setChecked(true);
            inputLists[18].setEnabled(false);
            inputLists[18].setTextColor(Color.parseColor("#707FCEFF"));
        }
        if (alreadyAvailableNote.isIs_checked20()) {
            checkBoxes[19].setChecked(true);
            inputLists[19].setEnabled(false);
            inputLists[19].setTextColor(Color.parseColor("#707FCEFF"));
        }
        if (alreadyAvailableNote.isIs_checked21()) {
            checkBoxes[20].setChecked(true);
            inputLists[20].setEnabled(false);
            inputLists[20].setTextColor(Color.parseColor("#707FCEFF"));
        }
        if (alreadyAvailableNote.isIs_checked22()) {
            checkBoxes[21].setChecked(true);
            inputLists[21].setEnabled(false);
            inputLists[21].setTextColor(Color.parseColor("#707FCEFF"));
        }
        if (alreadyAvailableNote.isIs_checked23()) {
            checkBoxes[22].setChecked(true);
            inputLists[22].setEnabled(false);
            inputLists[22].setTextColor(Color.parseColor("#707FCEFF"));
        }
        if (alreadyAvailableNote.isIs_checked24()) {
            checkBoxes[23].setChecked(true);
            inputLists[23].setEnabled(false);
            inputLists[23].setTextColor(Color.parseColor("#707FCEFF"));
        }
        if (alreadyAvailableNote.isIs_checked25()) {
            checkBoxes[24].setChecked(true);
            inputLists[24].setEnabled(false);
            inputLists[24].setTextColor(Color.parseColor("#707FCEFF"));
        }

        boolean[] isEnabledS = new boolean[25];

        for (int i = 0; i < inputLists.length; i++) {
            int finalI = i;
            inputLists[i].setOnClickListener(view -> {
                if (isEnabledS[finalI]) {
                    inputLists[finalI].setTextColor(Color.parseColor("#707FCEFF"));
                    isEnabledS[finalI] = false;
                } else {
                    inputLists[finalI].setTextColor(Color.parseColor("#7FCEFF"));
                    isEnabledS[finalI] = true;
                }
            });
        }
    }

    private void saveNote() {
        if (inputListTitle.getText().toString().trim().isEmpty()) {
            return;
        }
        final Note note = new Note();
        note.setTextNoteTitle(inputListTitle.getText().toString());
        note.setList_item1(inputLists[0].getText().toString());
        note.setList_item2(inputLists[1].getText().toString());
        note.setList_item3(inputLists[2].getText().toString());
        note.setList_item4(inputLists[3].getText().toString());
        note.setList_item5(inputLists[4].getText().toString());
        note.setList_item6(inputLists[5].getText().toString());
        note.setList_item7(inputLists[6].getText().toString());
        note.setList_item8(inputLists[7].getText().toString());
        note.setList_item9(inputLists[8].getText().toString());
        note.setList_item10(inputLists[9].getText().toString());
        note.setList_item11(inputLists[10].getText().toString());
        note.setList_item12(inputLists[11].getText().toString());
        note.setList_item13(inputLists[12].getText().toString());
        note.setList_item14(inputLists[13].getText().toString());
        note.setList_item15(inputLists[14].getText().toString());
        note.setList_item16(inputLists[15].getText().toString());
        note.setList_item17(inputLists[16].getText().toString());
        note.setList_item18(inputLists[17].getText().toString());
        note.setList_item19(inputLists[18].getText().toString());
        note.setList_item20(inputLists[19].getText().toString());
        note.setList_item21(inputLists[20].getText().toString());
        note.setList_item22(inputLists[21].getText().toString());
        note.setList_item23(inputLists[22].getText().toString());
        note.setList_item24(inputLists[23].getText().toString());
        note.setList_item25(inputLists[24].getText().toString());
        note.setIs_list(true);

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
                        String.valueOf(inputListTitle.getText()),
                        temp[2],
                        temp[0] + ":" + temp[1]
                );
                isReminder = true;
            }
        }

            if (alreadyAvailableNote != null) {
                note.setIs_checked1(checkBoxes[0].isChecked());
                note.setIs_checked2(checkBoxes[1].isChecked());
                note.setIs_checked3(checkBoxes[2].isChecked());
                note.setIs_checked4(checkBoxes[3].isChecked());
                note.setIs_checked5(checkBoxes[4].isChecked());
                note.setIs_checked6(checkBoxes[5].isChecked());
                note.setIs_checked7(checkBoxes[6].isChecked());
                note.setIs_checked8(checkBoxes[7].isChecked());
                note.setIs_checked9(checkBoxes[8].isChecked());
                note.setIs_checked10(checkBoxes[9].isChecked());
                note.setIs_checked11(checkBoxes[10].isChecked());
                note.setIs_checked12(checkBoxes[11].isChecked());
                note.setIs_checked13(checkBoxes[12].isChecked());
                note.setIs_checked14(checkBoxes[13].isChecked());
                note.setIs_checked15(checkBoxes[14].isChecked());
                note.setIs_checked16(checkBoxes[15].isChecked());
                note.setIs_checked17(checkBoxes[16].isChecked());
                note.setIs_checked18(checkBoxes[17].isChecked());
                note.setIs_checked19(checkBoxes[18].isChecked());
                note.setIs_checked20(checkBoxes[19].isChecked());
                note.setIs_checked21(checkBoxes[20].isChecked());
                note.setIs_checked22(checkBoxes[21].isChecked());
                note.setIs_checked23(checkBoxes[22].isChecked());
                note.setIs_checked24(checkBoxes[23].isChecked());
                note.setIs_checked25(checkBoxes[24].isChecked());
                note.setDate(alreadyAvailableNote.getDate());
                note.setId(alreadyAvailableNote.getId());
                note.setReminderSet(isReminder);
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

            @SuppressWarnings("deprecation")
            @SuppressLint("StaticFieldLeak")
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