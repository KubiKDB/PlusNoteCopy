package com.daniel.plusnote.activities;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.media.MediaScannerConnection;
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
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.daniel.plusnote.R;
import com.daniel.plusnote.database.NotesDatabase;
import com.daniel.plusnote.entities.Note;

import java.io.File;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class CreateImageNote extends AppCompatActivity {
    private ImageButton
            cancelButtonImage,
            deleteButtonImage,
            editButtonImage,
            reminderButtonImage,
            shareButtonImage,
            doneButtonImage,
            play_button_video,
            image_icon;
    private EditText inputImageTitle;
    private Note alreadyAvailableNote;
    public static String selectedImagePath;
    ImageView imageView;
    public static File outputFile;
    boolean isPlaying = false;
    ConstraintLayout video_controller;
    VideoView videoView;
    Chronometer playback_timer_video;
    TextView video_length;
    private long timePaused = 0;
    boolean needToRecreate = true;

    private Runnable updateSeekbar;
    private Handler seekbarHandler = new Handler();
    SeekBar seekbar_video;

    private ImageButton
            save_reminder_image,
            delete_reminder_image;
    ConstraintLayout
            cl_image,
            blur_reminder_image;
    TextView date_show_image;
    boolean isReminder = false;
    boolean isPast = false;
    AlarmManager alarmManager;
    PendingIntent pendingIntent;
    public static boolean isActivated = false;
    private int alarmID = 0;
    String reminderTime;
    private boolean isEdited;
    private ImageButton cancel_reminder_choose_image;
    private ImageButton reminder_button_image2;
    private TextView time_view_note_image;
    private int seekbar_video_time;
    private boolean resumeVideo = false;


    @SuppressLint({"SetTextI18n", "BatteryLife"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_image_note);
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        videoView = findViewById(R.id.video_view);
        ///////
        video_controller = findViewById(R.id.video_controller);
        cancelButtonImage = findViewById(R.id.cancelButtonImage);
        deleteButtonImage = findViewById(R.id.deleteButtonImage);
        editButtonImage = findViewById(R.id.editButtonImage);
        reminderButtonImage = findViewById(R.id.reminderButtonImage);
        shareButtonImage = findViewById(R.id.shareButtonImage);
        doneButtonImage = findViewById(R.id.doneButtonImage);
        image_icon = findViewById(R.id.image_icon);
        imageView = findViewById(R.id.image_view);
        playback_timer_video = findViewById(R.id.playback_timer_video);
        video_length = findViewById(R.id.video_length);
        seekbar_video = findViewById(R.id.seekbar_video);
        play_button_video = findViewById(R.id.play_button_video);
        cancel_reminder_choose_image = findViewById(R.id.cancel_reminder_choose_image);
        reminder_button_image2 = findViewById(R.id.reminderButtonImage2);
        time_view_note_image = findViewById(R.id.time_view_note_image);
        ///////
        inputImageTitle = findViewById(R.id.inputImageTitle);
        //////
        save_reminder_image = findViewById(R.id.save_reminder_image);
        delete_reminder_image = findViewById(R.id.delete_reminder_image);
        cl_image = findViewById(R.id.tp_container_image);
        date_show_image = findViewById(R.id.date_show_image);
        blur_reminder_image = findViewById(R.id.blur_reminder_image);
        save_reminder_image.setEnabled(false);
        save_reminder_image.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#4000FF00")));
        TimePicker timePicker = findViewById(R.id.time_picker_image);
        timePicker.setIs24HourView(true);
        timePicker.setOnTimeChangedListener((timePicker1, i, i1) -> {
            reminderTime = i + ":" + i1 + ":" + MainActivity.notesDay;
            save_reminder_image.setEnabled(true);
            save_reminder_image.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#00FF00")));

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
        delete_reminder_image.setOnClickListener(view -> {
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
        reminderButtonImage.setOnClickListener(view -> setReminder());
        save_reminder_image.setOnClickListener(view -> {
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
                    String.valueOf(inputImageTitle.getText()),
                    temp[2],
                    temp[0] + ":" + temp[1]
            );

            isReminder = true;

            cl_image.setVisibility(View.GONE);
            blur_reminder_image.setVisibility(View.GONE);
            saveNote();
        });
        blur_reminder_image.setOnClickListener(view -> {
            blur_reminder_image.setVisibility(View.GONE);
            cl_image.setVisibility(View.GONE);
        });
        cancel_reminder_choose_image.setOnClickListener(v -> {
            blur_reminder_image.setVisibility(View.GONE);
            cl_image.setVisibility(View.GONE);
        });
        time_view_note_image.setOnClickListener(v -> setReminder());
        //////
        editButtonImage.setEnabled(false);
        shareButtonImage.setEnabled(false);
        doneButtonImage.setEnabled(false);
        reminderButtonImage.setEnabled(false);

        shareButtonImage.setOnClickListener(view -> share());

        inputImageTitle.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {
            }

            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                if (!inputImageTitle.getText().toString().trim().isEmpty()) {
                    doneButtonImage.setEnabled(true);
                    doneButtonImage.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#00FF00")));
                    reminderButtonImage.setEnabled(true);
                    reminderButtonImage.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#57ACF9")));
                } else {
                    doneButtonImage.setEnabled(false);
                    doneButtonImage.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#4000FF00")));
                    reminderButtonImage.setEnabled(false);
                    reminderButtonImage.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#7057ACF9")));
                }
            }
        });

        play_button_video.setOnClickListener(view -> {
            if (isPlaying) {
                pauseVideo();
                isPlaying = false;
            } else {
                if (resumeVideo) {
                    resumeVideo();
                } else {
                    resumeVideo(selectedImagePath);
                }
                isPlaying = true;
            }
//            if (isPlaying) {
//                videoView.pause();
//                playback_timer_video.stop();
//                timePaused = SystemClock.elapsedRealtime() - playback_timer_video.getBase();
//                seekbarHandler.removeCallbacks(updateSeekbar);
//                play_button_video.setBackgroundResource(R.drawable.ic_baseline_play_arrow_24);
//                play_button_video.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#8000FF00")));
//            } else {
//                playback_timer_video.setBase(SystemClock.elapsedRealtime() - timePaused);
//                playback_timer_video.start();
//                if (needToRecreate) {
//                    if (alreadyAvailableNote != null) {
//                        outputFile = new File(alreadyAvailableNote.getImage_path());
//                    }
//                    videoView.setVideoPath(selectedImagePath);
//                    needToRecreate = false;
//                }
//                videoView.start();
//                play_button_video.setBackgroundResource(R.drawable.ic_baseline_pause_24);
//                play_button_video.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#FFA500")));
//                if (alreadyAvailableNote != null) {
//                    seekbar_video.setMax(Integer.parseInt(alreadyAvailableNote.getAudio_length().substring(3)) * 1000);
//                } else
//                    seekbar_video.setMax(Integer.parseInt(VideoXActivity.timer_string.substring(3)) * 1000);
//                seekbarHandler = new Handler();
//                updateSeekbar = new Runnable() {
//                    @Override
//                    public void run() {
//                        seekbar_video.setProgress(videoView.getCurrentPosition());
//                        seekbarHandler.postDelayed(this, 100);
//                    }
//                };
//                seekbarHandler.postDelayed(updateSeekbar, 0);
//                videoView.setOnCompletionListener(mediaPlayer -> {
//                    mediaPlayer.seekTo(1);
//                    mediaPlayer.stop();
//                    playback_timer_video.stop();
//                    playback_timer_video.setBase(SystemClock.elapsedRealtime());
//                    play_button_video.setBackgroundResource(R.drawable.ic_baseline_play_arrow_24);
//                    play_button_video.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#8000FF00")));
//                    isPlaying = false;
//                    needToRecreate = true;
//                    seekbar_video.setProgress(0);
//                });
//            }
//
//            isPlaying = !isPlaying;
        });
        seekbar_video.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                if (b) {
                    videoView.seekTo(i);
                    seekbar_video.setMax(videoView.getDuration());

                    seekbar_video_time = i;

                    resumeVideo();
                    resumeVideo = true;
                    isPlaying = true;
                    videoView.setOnCompletionListener(mediaPlayer -> {
                        playback_timer_video.stop();
                        playback_timer_video.setBase(SystemClock.elapsedRealtime());
                        play_button_video.setBackgroundResource(R.drawable.ic_baseline_play_arrow_24);
                        play_button_video.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#8000FF00")));
                        isPlaying = false;
                        resumeVideo = false;
                        seekbar_video.setProgress(0);
                        videoView.seekTo(0);
                        videoView.pause();
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

        videoView.setOnCompletionListener(mediaPlayer -> {
            playback_timer_video.stop();
            play_button_video.setBackgroundResource(R.drawable.ic_baseline_play_arrow_24);
            play_button_video.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#8000FF00")));
            isPlaying = false;
            resumeVideo = false;
            seekbar_video.setProgress(0);
            mediaPlayer.seekTo(0);
            mediaPlayer.pause();
            seekbarHandler.removeCallbacks(updateSeekbar);
        });

        if (VideoXActivity.isVideo) {
            image_icon.setBackgroundResource(R.drawable.ic_video);
            videoView.seekTo(1);
        }


        cancelButtonImage.setOnClickListener(view -> {
            Intent intent = new Intent();
            setResult(RESULT_CANCELED, intent);
            finish();
            imm.hideSoftInputFromWindow(inputImageTitle.getWindowToken(), InputMethodManager.RESULT_UNCHANGED_SHOWN);
        });
        doneButtonImage.setOnClickListener(view -> {
            saveNote();
            imm.hideSoftInputFromWindow(inputImageTitle.getWindowToken(), InputMethodManager.RESULT_UNCHANGED_SHOWN);
        });
        editButtonImage.setOnClickListener(view -> {
            inputImageTitle.setEnabled(true);
            inputImageTitle.requestFocus();
            inputImageTitle.setFocusableInTouchMode(true);
            imm.showSoftInput(inputImageTitle, InputMethodManager.SHOW_FORCED);
            inputImageTitle.post(() -> inputImageTitle.setSelection(inputImageTitle.getText().length()));
            doneButtonImage.setEnabled(true);
            doneButtonImage.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#00FF00")));
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
        } else {
            if (VideoXActivity.isVideo) {
                videoView.setVisibility(View.VISIBLE);
                video_controller.setVisibility(View.VISIBLE);
                videoView.setVideoPath(selectedImagePath);
                videoView.seekTo(1);
                video_length.setText(VideoXActivity.timer_string);
            } else {
                Bitmap temp = BitmapFactory.decodeFile(selectedImagePath);
                if (temp.getWidth() > temp.getHeight()) {
                    if (temp.getWidth() == 1920) {
                        temp = RotateBitmap(temp, 270);
                    } else temp = RotateBitmap(temp, 90);
                }
                if (CameraXActivity.fromGallery) {
                    temp = RotateBitmap(temp, 0);
                } else {
                    image_icon.setBackgroundResource(R.drawable.ic_camera);
                }
                imageView.setImageBitmap(temp);
            }
        }
        if (alreadyAvailableNote != null) {
            deleteButtonImage.setOnClickListener(view -> deleteNote());
        }
    }

    private void setReminder() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(inputImageTitle.getWindowToken(), InputMethodManager.RESULT_UNCHANGED_SHOWN);



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
        blur_reminder_image.setVisibility(View.VISIBLE);
        cl_image.setVisibility(View.VISIBLE);
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
            date_show_image.setText(temp);
        } else {
            try {
                date = noteDayF.parse(alreadyAvailableNote.getDate());
            } catch (ParseException ignored) {
            }
            String temp = null;
            if (date != null) {
                temp = forDay.format(date);
            }
            date_show_image.setText(temp);
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

    @SuppressWarnings("deprecation")
    @SuppressLint("NewApi")
    private void share() {
        if (!alreadyAvailableNote.isIs_video()) {
            MediaScannerConnection.scanFile(this, new String[]{alreadyAvailableNote.getImage_path()},
                    null, (path, uri) -> {
                        Intent shareIntent = new Intent(
                                Intent.ACTION_SEND);
                        shareIntent.setType("image/*");
                        shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
                        shareIntent
                                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
                        startActivity(Intent.createChooser(shareIntent,
                                "Share Image"));
                    });
        } else {
            MediaScannerConnection.scanFile(this, new String[]{alreadyAvailableNote.getImage_path()},
                    null, (path, uri) -> {
                        Intent shareIntent = new Intent(
                                Intent.ACTION_SEND);
                        shareIntent.setType("video/*");
                        shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
                        shareIntent
                                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
                        startActivity(Intent.createChooser(shareIntent,
                                "Share Video"));
                    });
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

    @SuppressLint("SetTextI18n")
    private void setViewOrUpdateNote() {
        inputImageTitle.setEnabled(false);
        editButtonImage.setEnabled(true);
        shareButtonImage.setEnabled(true);
        doneButtonImage.setEnabled(false);

        if (alreadyAvailableNote.isReminderSet()){
            delete_reminder_image.setVisibility(View.VISIBLE);
            cancel_reminder_choose_image.setVisibility(View.GONE);
        }

        if (alreadyAvailableNote.isReminderSet()){
            delete_reminder_image.setVisibility(View.VISIBLE);
            cancel_reminder_choose_image.setVisibility(View.GONE);

            reminder_button_image2.setVisibility(View.VISIBLE);
            reminderButtonImage.setVisibility(View.INVISIBLE);
            time_view_note_image.setVisibility(View.VISIBLE);
            String[] split = alreadyAvailableNote.getReminder_time().split(":");
            if (split[1].length() == 1) {
                split[1] = "0" + split[1];
            }
            if (split[0].length() == 1) {
                split[0] = "0" + split[0];
            }
            time_view_note_image.setText(split[0] + ":" + split[1]);
        }

        Log.e("DateSavedImage", alreadyAvailableNote.getDate());

        deleteButtonImage.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#57ACF9")));
        editButtonImage.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#57ACF9")));
        shareButtonImage.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#57ACF9")));
        doneButtonImage.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#4000FF00")));

        inputImageTitle.setText(alreadyAvailableNote.getTextNoteTitle());
        try {
            if (alreadyAvailableNote.isIs_video()) {
                image_icon.setBackgroundResource(R.drawable.ic_video);
                videoView.setVisibility(View.VISIBLE);
                video_controller.setVisibility(View.VISIBLE);
                video_length.setText(alreadyAvailableNote.getAudio_length());
                videoView.setVideoPath(alreadyAvailableNote.getImage_path());
                videoView.seekTo(1);
            } else {
                Bitmap temp = BitmapFactory.decodeFile(alreadyAvailableNote.getImage_path());
                if (temp.getWidth() > temp.getHeight()) {
                    if (temp.getWidth() == 1920) {
                        temp = RotateBitmap(temp, 270);
                    } else temp = RotateBitmap(temp, 90);
                }
                if (alreadyAvailableNote.isFrom_gallery()) {
                    temp = RotateBitmap(temp, 0);
                } else {
                    image_icon.setBackgroundResource(R.drawable.ic_camera);
                }
                imageView.setImageBitmap(temp);
            }
        } catch (Exception e) {
            if (!getIntent().getBooleanExtra("deleteNote", false)) {
                Toast toast = Toast.makeText(this, "Image has been deleted", Toast.LENGTH_LONG);
                toast.show();
            }
        }
    }

    private void saveNote() {
        if (inputImageTitle.getText().toString().trim().isEmpty()) {
            return;
        }
        final Note note = new Note();
        note.setTextNoteTitle(inputImageTitle.getText().toString());
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
                        String.valueOf(inputImageTitle.getText()),
                        temp[2],
                        temp[0] + ":" + temp[1]
                );
                isReminder = true;
            }
        }
        if (alreadyAvailableNote == null) {
            note.setImage_path(selectedImagePath);
            note.setAudio_length(VideoXActivity.timer_string);
            note.setFrom_gallery(CameraXActivity.fromGallery);
            note.setIs_video(VideoXActivity.isVideo);
            note.setDate(MainActivity.notesDay);
            note.setReminderSet(isReminder);
            if (isReminder) {
                note.setReminder_time(reminderTime);
                note.setReminder_id(alarmID);
            }
            if (reminderTime != null) {
                note.setReminder_time(reminderTime);
                note.setReminderSet(true);
                note.setReminder_id(alarmID);
            }
        } else {
            note.setIs_video(alreadyAvailableNote.isIs_video());
            note.setIs_photo(alreadyAvailableNote.isIs_photo());
            note.setImage_path(alreadyAvailableNote.getImage_path());
            note.setAudio_length(alreadyAvailableNote.getAudio_length());
            note.setFrom_gallery(alreadyAvailableNote.isFrom_gallery());
            note.setDate(alreadyAvailableNote.getDate());
            if (isReminder) {
                note.setReminder_time(reminderTime);
                note.setReminder_id(alarmID);
            }
            note.setReminderSet(isReminder);
            if (reminderTime != null) {
                note.setReminder_time(reminderTime);
                note.setReminderSet(true);
                note.setReminder_id(alarmID);
            } else {
                if(alreadyAvailableNote.isReminderSet()){
                    note.setReminder_time(alreadyAvailableNote.getReminder_time());
                    note.setReminder_id(alreadyAvailableNote.getReminder_id());
                }
            }
        }
        note.setIs_image(true);
        if (alreadyAvailableNote != null) {
            note.setId(alreadyAvailableNote.getId());
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

    public static Bitmap RotateBitmap(Bitmap source, float angle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (isPlaying) {
            videoView.stopPlayback();
        }
    }

    private void pauseVideo() {
        videoView.pause();
        playback_timer_video.stop();
        timePaused = SystemClock.elapsedRealtime() - playback_timer_video.getBase();
        seekbarHandler.removeCallbacks(updateSeekbar);
        play_button_video.setBackgroundResource(R.drawable.ic_baseline_play_arrow_24);
        play_button_video.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#8000FF00")));
    }

    private void resumeVideo() {
        videoView.start();
        if (seekbar_video_time == 0) {
            playback_timer_video.setBase(SystemClock.elapsedRealtime() - timePaused);
            timePaused = 0;
        } else {
            playback_timer_video.setBase(SystemClock.elapsedRealtime() - seekbar_video_time);
            seekbar_video_time = 0;
        }

        playback_timer_video.start();
        play_button_video.setBackgroundResource(R.drawable.ic_baseline_pause_24);
        play_button_video.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#FFA500")));
        updateRunnableVideo();
        seekbarHandler.postDelayed(updateSeekbar, 0);
    }

    private void resumeVideo(String path) {
        videoView.start();
        isPlaying = true;
        resumeVideo = true;
        seekbar_video.setMax(videoView.getDuration());
        playback_timer_video.start();
        play_button_video.setBackgroundResource(R.drawable.ic_baseline_pause_24);
        play_button_video.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#FFA500")));
        playback_timer_video.setBase(SystemClock.elapsedRealtime());
        updateRunnableVideo();
        seekbarHandler.postDelayed(updateSeekbar, 0);
        videoView.setOnCompletionListener(mediaPlayer -> {
            playback_timer_video.stop();
            playback_timer_video.setBase(SystemClock.elapsedRealtime());
            play_button_video.setBackgroundResource(R.drawable.ic_baseline_play_arrow_24);
            play_button_video.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#8000FF00")));
            isPlaying = false;
            resumeVideo = false;
            seekbar_video.setProgress(0);
            videoView.seekTo(0);
            videoView.pause();
            seekbarHandler.removeCallbacks(updateSeekbar);
        });
    }

    private void updateRunnableVideo() {
        seekbarHandler = new Handler();
        updateSeekbar = new Runnable() {
            @Override
            public void run() {
                seekbar_video.setProgress(videoView.getCurrentPosition());
                seekbarHandler.postDelayed(this, 100);
            }
        };
    }

}