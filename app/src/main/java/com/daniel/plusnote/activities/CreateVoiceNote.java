package com.daniel.plusnote.activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.media.MediaScannerConnection;
import android.media.SoundPool;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Chronometer;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;

import com.daniel.plusnote.R;
import com.daniel.plusnote.database.NotesDatabase;
import com.daniel.plusnote.entities.Note;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

@SuppressWarnings("deprecation")
public class CreateVoiceNote extends AppCompatActivity {
    private ImageButton
            cancelButtonVoice,
            deleteButtonVoice,
            editButtonVoice,
            reminderButtonVoice,
            shareButtonVoice,
            doneButtonVoice,
            play_btn,
            record_voice;
    private EditText inputVoiceTitle;
    private Note alreadyAvailableNote;
    private boolean isRecording = false;
    private Chronometer timer, playback_timer;
    private MediaRecorder mediaRecorder;
    private File outputFile = null;
    private MediaPlayer mediaPlayer = null;
    private boolean isPlaying = false;
    private SeekBar seekBar;
    private TextView textView;
    private Handler seekbarHandler = new Handler();
    private Runnable updateSeekbar;
    private boolean resumeAudio = false;
    private long elapsedMillis = 0;
    private SoundPool mSound1, mSound2;
    private final int mMelody = 1;
    private int mPlay;
    private ImageButton
            save_reminder_voice,
            delete_reminder_voice;
    ConstraintLayout
            cl_voice,
            blur_reminder_voice;
    TextView date_show_voice;
    boolean isReminder = false;
    boolean isPast = false;
    AlarmManager alarmManager;
    PendingIntent pendingIntent;
    public static boolean isActivated = false;
    private int alarmID = 0;
    String reminderTime;
    private boolean isEdited;
    private ImageButton cancel_reminder_choose_voice;
    private ImageButton reminder_button_voice2;
    private TextView time_view_note_voice;
    private long seekbar_time;

    @SuppressLint("BatteryLife")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_voice_note);
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        mSound1 = new SoundPool(1, AudioManager.STREAM_MUSIC, 0);
        mSound1.load(this, R.raw.record_sound, 1);
        mSound2 = new SoundPool(1, AudioManager.STREAM_MUSIC, 0);
        mSound2.load(this, R.raw.record_sound, 1);
        ///////
        cancelButtonVoice = findViewById(R.id.cancelButtonVoice);
        deleteButtonVoice = findViewById(R.id.deleteButtonVoice);
        editButtonVoice = findViewById(R.id.editButtonVoice);
        reminderButtonVoice = findViewById(R.id.reminderButtonVoice);
        shareButtonVoice = findViewById(R.id.shareButtonVoice);
        doneButtonVoice = findViewById(R.id.doneButtonVoice);
        record_voice = findViewById(R.id.voice_icon);
        play_btn = findViewById(R.id.play_button);
        inputVoiceTitle = findViewById(R.id.inputVoiceTitle);
        timer = findViewById(R.id.timer);
        seekBar = findViewById(R.id.seekbar_audio);
        playback_timer = findViewById(R.id.playback_timer);
        textView = findViewById(R.id.audio_length);
        cancel_reminder_choose_voice = findViewById(R.id.cancel_reminder_choose_voice);
        reminder_button_voice2 = findViewById(R.id.reminderButtonVoice2);
        time_view_note_voice = findViewById(R.id.time_view_note_voice);
        ////////
        save_reminder_voice = findViewById(R.id.save_reminder_voice);
        delete_reminder_voice = findViewById(R.id.delete_reminder_voice);
        cl_voice = findViewById(R.id.tp_container_voice);
        date_show_voice = findViewById(R.id.date_show_voice);
        blur_reminder_voice = findViewById(R.id.blur_reminder_voice);
        save_reminder_voice.setEnabled(false);
        save_reminder_voice.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#4000FF00")));
        TimePicker timePicker = findViewById(R.id.time_picker_voice);
        timePicker.setIs24HourView(true);
        timePicker.setOnTimeChangedListener((timePicker1, i, i1) -> {
            reminderTime = i + ":" + i1 + ":" + MainActivity.notesDay;
            save_reminder_voice.setEnabled(true);
            save_reminder_voice.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#00FF00")));

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
        delete_reminder_voice.setOnClickListener(view -> {
            if (alreadyAvailableNote.isReminderSet()){
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
        reminderButtonVoice.setOnClickListener(view -> setReminder());
        save_reminder_voice.setOnClickListener(view -> {
            if (alreadyAvailableNote != null){
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
                    String.valueOf(inputVoiceTitle.getText()),
                    temp[2],
                    temp[0] + ":" + temp[1]
            );

            isReminder = true;

            cl_voice.setVisibility(View.GONE);
            blur_reminder_voice.setVisibility(View.GONE);
            saveNote();
        });
        blur_reminder_voice.setOnClickListener(view -> {
            blur_reminder_voice.setVisibility(View.GONE);
            cl_voice.setVisibility(View.GONE);
        });
        cancel_reminder_choose_voice.setOnClickListener(v -> {
            blur_reminder_voice.setVisibility(View.GONE);
            cl_voice.setVisibility(View.GONE);
        });
        time_view_note_voice.setOnClickListener(v -> setReminder());
        ////////

        shareButtonVoice.setOnClickListener(view -> share());

        inputVoiceTitle.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {
            }

            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                if (!inputVoiceTitle.getText().toString().trim().isEmpty()) {
                    doneButtonVoice.setEnabled(true);
                    doneButtonVoice.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#00FF00")));
                    reminderButtonVoice.setEnabled(true);
                    reminderButtonVoice.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#57ACF9")));
                } else {
                    doneButtonVoice.setEnabled(false);
                    doneButtonVoice.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#4000FF00")));
                    reminderButtonVoice.setEnabled(false);
                    reminderButtonVoice.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#7057ACF9")));
                }
            }
        });

        record_voice.setOnClickListener(view -> {
            if (isRecording) {
                AudioManager audioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
                mPlay = mSound1.play(mMelody, (float) 0.3, (float) 0.3, 1, 0, 1);
                stopRecording();
                record_voice.setBackgroundResource(R.drawable.ic_microphone_dark);
                record_voice.setEnabled(false);
                timer.setVisibility(View.GONE);

                seekBar.setVisibility(View.VISIBLE);
                playback_timer.setVisibility(View.VISIBLE);
                textView.setVisibility(View.VISIBLE);
                play_btn.setVisibility(View.VISIBLE);
                textView.setText(timer.getText());

                doneButtonVoice.setEnabled(true);
                doneButtonVoice.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#00FF00")));

                inputVoiceTitle.setVisibility(View.VISIBLE);
                inputVoiceTitle.requestFocus();
                inputVoiceTitle.setFocusableInTouchMode(true);
                imm.showSoftInput(inputVoiceTitle, InputMethodManager.SHOW_FORCED);
                inputVoiceTitle.post(() -> inputVoiceTitle.setSelection(inputVoiceTitle.getText().length()));
            } else {
                if (checkPermissions()) {
                    AudioManager audioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
                    mPlay = mSound2.play(mMelody, (float) 0.5, (float) 0.5, 1, 0, 1);
                    try {
                        Thread.sleep(400);
                    } catch (InterruptedException ignored) {
                    }
                    startRecording();
                    doneButtonVoice.setEnabled(false);
                    doneButtonVoice.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#4000FF00")));
                    record_voice.setBackgroundResource(R.drawable.microphone_recording);
                    timer.setVisibility(View.VISIBLE);
                }
            }
            isRecording = !isRecording;
        });

        cancelButtonVoice.setOnClickListener(view -> {
            Intent intent = new Intent();
            setResult(RESULT_CANCELED, intent);
            finish();
            imm.hideSoftInputFromWindow(inputVoiceTitle.getWindowToken(), InputMethodManager.RESULT_UNCHANGED_SHOWN);
        });
        doneButtonVoice.setOnClickListener(view -> {
            saveNote();
            imm.hideSoftInputFromWindow(inputVoiceTitle.getWindowToken(), InputMethodManager.RESULT_UNCHANGED_SHOWN);
        });
        editButtonVoice.setOnClickListener(view -> {
            inputVoiceTitle.setEnabled(true);
            inputVoiceTitle.requestFocus();
            inputVoiceTitle.setFocusableInTouchMode(true);
            imm.showSoftInput(inputVoiceTitle, InputMethodManager.SHOW_FORCED);
            inputVoiceTitle.post(() -> inputVoiceTitle.setSelection(inputVoiceTitle.getText().length()));
            doneButtonVoice.setEnabled(true);
            doneButtonVoice.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#00FF00")));
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
            if (getIntent().getBooleanExtra("setReminder", false)){
                setReminder();
            }
            play_btn.setOnClickListener(view -> {
                if (isPlaying) {
                    pauseAudio();
                    isPlaying = false;
                } else {
                    if (resumeAudio) {
                        resumeAudio();
                    } else {
                        resumeAudio(outputFile.getAbsolutePath());
                    }
                    isPlaying = true;
                }
//                if (isPlaying) {
//                    playback_timer.stop();
//                    pauseAudio();
//                    play_btn.setBackgroundResource(R.drawable.ic_baseline_play_arrow_24);
//                } else {
//                    if (!resumeAudio) {
//                        playback_timer.setBase(SystemClock.elapsedRealtime());
//                        playback_timer.start();
//                        playAudio(alreadyAvailableNote.getImage_path());
//                        play_btn.setBackgroundResource(R.drawable.ic_baseline_pause_24);
//                        resumeAudio = !resumeAudio;
//                    } else {
//                        playback_timer.setBase(SystemClock.elapsedRealtime() - elapsedMillis);
//                        playback_timer.start();
//                        play_btn.setBackgroundResource(R.drawable.ic_baseline_pause_24);
//                        resumeAudio();
//                    }
//                }
//                isPlaying = !isPlaying;
            });
        } else {
            reminderButtonVoice.setEnabled(false);
            reminderButtonVoice.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#7057ACF9")));
            play_btn.setOnClickListener(view -> {
                if (isPlaying) {
                    pauseAudio();
                    isPlaying = false;
                } else {
                    if (resumeAudio) {
                        resumeAudio();
                    } else {
                        resumeAudio(outputFile.getAbsolutePath());
                    }
                    isPlaying = true;
                }
            });
        }
        if (alreadyAvailableNote != null) {
            deleteButtonVoice.setOnClickListener(view -> deleteNote());
        }

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                if (b) {
                    mediaPlayer.seekTo(i);
                    seekBar.setMax(mediaPlayer.getDuration());

//                    int minutes = i / 60;
//                    int seconds = i % 60;
//
//                    // format the minutes and seconds as a string
//                    String time = String.format("%02d:%02d", minutes, seconds);

                    seekbar_time = i;

                    resumeAudio();
                    resumeAudio = true;
                    isPlaying = true;
                    mediaPlayer.setOnCompletionListener(mediaPlayer -> {
                        playback_timer.stop();
                        playback_timer.setBase(SystemClock.elapsedRealtime());
                        play_btn.setBackgroundResource(R.drawable.ic_baseline_play_arrow_24);
                        play_btn.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#8000FF00")));
                        isPlaying = false;
                        resumeAudio = false;
                        seekBar.setProgress(0);
                        mediaPlayer.seekTo(0);
                        mediaPlayer.pause();
                        seekbarHandler.removeCallbacks(updateSeekbar);
                    });
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                seekbarHandler.removeCallbacks(updateSeekbar);
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                seekbarHandler.postDelayed(updateSeekbar, 1000);
            }
        });
    }
    private void setReminder() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(inputVoiceTitle.getWindowToken(), InputMethodManager.RESULT_UNCHANGED_SHOWN);

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
        blur_reminder_voice.setVisibility(View.VISIBLE);
        cl_voice.setVisibility(View.VISIBLE);
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
            date_show_voice.setText(temp);
        } else {
            try {
                date = noteDayF.parse(alreadyAvailableNote.getDate());
            } catch (ParseException ignored) {
            }
            String temp = null;
            if (date != null) {
                temp = forDay.format(date);
            }
            date_show_voice.setText(temp);
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

        alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        AlarmManager.AlarmClockInfo aci = new AlarmManager.AlarmClockInfo(temp, pendingIntent);
        alarmManager.setAlarmClock(aci, pendingIntent);
        Toast.makeText(this, "Reminder set", Toast.LENGTH_SHORT).show();
    }

    private void share() {
        MediaScannerConnection.scanFile(this, new String[]{alreadyAvailableNote.getImage_path()},
                null, (path, uri) -> {
                    Intent shareIntent = new Intent(
                            Intent.ACTION_SEND);
                    shareIntent.setType("audio/*");
                    shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
                    shareIntent
                            .addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
                    startActivity(Intent.createChooser(shareIntent,
                            "Share Audio"));
                });
    }

    private void stopAudio() {
        mediaPlayer.stop();
        seekbarHandler.removeCallbacks(updateSeekbar);
    }

    private void pauseAudio() {
        mediaPlayer.pause();
        playback_timer.stop();
        elapsedMillis = SystemClock.elapsedRealtime() - playback_timer.getBase();
        seekbarHandler.removeCallbacks(updateSeekbar);
        play_btn.setBackgroundResource(R.drawable.ic_baseline_play_arrow_24);
        play_btn.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#8000FF00")));
    }

    private void resumeAudio() {
        mediaPlayer.start();
        if (seekbar_time == 0) {
            playback_timer.setBase(SystemClock.elapsedRealtime() - elapsedMillis);
            elapsedMillis = 0;
        } else {
            playback_timer.setBase(SystemClock.elapsedRealtime() - seekbar_time);
            seekbar_time = 0;
        }

        playback_timer.start();
        play_btn.setBackgroundResource(R.drawable.ic_baseline_pause_24);
        play_btn.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#FFA500")));
        updateRunnable();
        seekbarHandler.postDelayed(updateSeekbar, 0);
    }

    private void resumeAudio(String path) {
        mediaPlayer.start();
        isPlaying = true;
        resumeAudio = true;
        seekBar.setMax(mediaPlayer.getDuration());
        playback_timer.start();
        play_btn.setBackgroundResource(R.drawable.ic_baseline_pause_24);
        play_btn.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#FFA500")));
        updateRunnable();
        seekbarHandler.postDelayed(updateSeekbar, 0);
        mediaPlayer.setOnCompletionListener(mediaPlayer -> {
            playback_timer.stop();
            playback_timer.setBase(SystemClock.elapsedRealtime());
            play_btn.setBackgroundResource(R.drawable.ic_baseline_play_arrow_24);
            play_btn.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#8000FF00")));
            isPlaying = false;
            resumeAudio = false;
            seekBar.setProgress(0);
            mediaPlayer.seekTo(0);
            mediaPlayer.pause();
            seekbarHandler.removeCallbacks(updateSeekbar);
        });
    }

    private void playAudio(String path) {
        mediaPlayer = new MediaPlayer();

        isPlaying = true;

        try {
            mediaPlayer.setDataSource(path);
            mediaPlayer.prepare();
            mediaPlayer.start();
        } catch (Exception e) {
            if (!getIntent().getBooleanExtra("deleteNote", false)) {
                Toast.makeText(this, "Audio has been deleted", Toast.LENGTH_LONG)
                        /*.setGravity(Gravity.CENTER, 0, 0)*/.show();
            }
        }

        mediaPlayer.setOnCompletionListener(mediaPlayer -> {
            playback_timer.stop();
            playback_timer.setBase(SystemClock.elapsedRealtime());
            play_btn.setBackgroundResource(R.drawable.ic_baseline_play_arrow_24);
            play_btn.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#8000FF00")));
            isPlaying = false;
            resumeAudio = false;
            seekBar.setProgress(0);
            stopAudio();
        });

        seekBar.setMax(mediaPlayer.getDuration());
        seekbarHandler = new Handler();
        updateRunnable();
        seekbarHandler.postDelayed(updateSeekbar, 0);
    }

    private void startRecording() {
        timer.setBase(SystemClock.elapsedRealtime());
        timer.start();
        mediaRecorder = new MediaRecorder();
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        outputFile = CameraXActivity.getOutputMediaFile(CameraXActivity.MEDIA_TYPE_AUDIO);
        mediaRecorder.setOutputFile(outputFile);
        mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(outputFile)));

        try {
            mediaRecorder.prepare();
        } catch (IOException ignored) {
        }

        mediaRecorder.start();
    }

    private void stopRecording() {
        timer.stop();
        mediaRecorder.stop();
        mediaRecorder.release();
        mediaRecorder = null;
        mediaPlayer = MediaPlayer.create(this, Uri.parse(outputFile.getAbsolutePath()));
    }

    private void updateRunnable() {
        updateSeekbar = new Runnable() {
            @Override
            public void run() {
                seekBar.setProgress(mediaPlayer.getCurrentPosition());
                seekbarHandler.postDelayed(this, 100);
            }
        };
    }

    @Override
    protected void onStop() {
        super.onStop();
        try {
            stopAudio();
            stopRecording();
        } catch (Exception ignored){}
    }

    private boolean checkPermissions() {
        if (ActivityCompat.checkSelfPermission(
                getApplicationContext(),
                Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            ActivityCompat.requestPermissions(
                    this,
                    new String[]{Manifest.permission.RECORD_AUDIO},
                    1);
            return false;
        }
    }

    private void deleteNote() {
        if (alreadyAvailableNote.isReminderSet()){
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
        inputVoiceTitle.setEnabled(false);
        inputVoiceTitle.setVisibility(View.VISIBLE);
        editButtonVoice.setEnabled(true);
        shareButtonVoice.setEnabled(true);
        doneButtonVoice.setEnabled(false);
        record_voice.setEnabled(false);
        seekBar.setVisibility(View.VISIBLE);
        playback_timer.setVisibility(View.VISIBLE);
        textView.setVisibility(View.VISIBLE);
        play_btn.setVisibility(View.VISIBLE);
        textView.setText(alreadyAvailableNote.getAudio_length());

        mediaPlayer = MediaPlayer.create(this, Uri.parse(alreadyAvailableNote.getImage_path()));

        if (alreadyAvailableNote.isReminderSet()){
            delete_reminder_voice.setVisibility(View.VISIBLE);
            cancel_reminder_choose_voice.setVisibility(View.GONE);
        }

        if (alreadyAvailableNote.isReminderSet()){
            delete_reminder_voice.setVisibility(View.VISIBLE);
            cancel_reminder_choose_voice.setVisibility(View.GONE);

            reminder_button_voice2.setVisibility(View.VISIBLE);
            reminderButtonVoice.setVisibility(View.INVISIBLE);
            time_view_note_voice.setVisibility(View.VISIBLE);
            String[] split = alreadyAvailableNote.getReminder_time().split(":");
            if (split[1].length() == 1) {
                split[1] = "0" + split[1];
            }
            if (split[0].length() == 1) {
                split[0] = "0" + split[0];
            }
            time_view_note_voice.setText(split[0] + ":" + split[1]);
        }

        Log.e("DateSavedAudio", alreadyAvailableNote.getDate());

        deleteButtonVoice.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#57ACF9")));
        editButtonVoice.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#57ACF9")));
        shareButtonVoice.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#57ACF9")));
        doneButtonVoice.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#4000FF00")));

        inputVoiceTitle.setText(alreadyAvailableNote.getTextNoteTitle());
    }

    private void saveNote() {
        if (inputVoiceTitle.getText().toString().trim().isEmpty()) {
            return;
        }
        final Note note = new Note();
        note.setTextNoteTitle(inputVoiceTitle.getText().toString());
        note.setIs_voice(true);
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
                        String.valueOf(inputVoiceTitle.getText()),
                        temp[2],
                        temp[0] + ":" + temp[1]
                );
                isReminder = true;
            }
        }
        if (alreadyAvailableNote != null) {
            note.setImage_path(alreadyAvailableNote.getImage_path());
            note.setAudio_length(alreadyAvailableNote.getAudio_length());
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
            note.setImage_path(outputFile.getAbsolutePath());
            note.setAudio_length((String) timer.getText());
            note.setDate(MainActivity.notesDay);
            if (isReminder) {
                note.setReminder_time(reminderTime);
                note.setReminder_id(alarmID);
            }
            note.setReminderSet(isReminder);
        }

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