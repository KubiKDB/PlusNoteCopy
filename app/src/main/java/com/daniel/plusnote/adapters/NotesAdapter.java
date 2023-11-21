package com.daniel.plusnote.adapters;

import static com.daniel.plusnote.activities.MainActivity.REQUEST_CODE_SHOW_NOTES;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.Chronometer;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.daniel.plusnote.entities.Note;
import com.daniel.plusnote.listeners.NotesListener;
import com.daniel.plusnote.R;
import com.daniel.plusnote.activities.AlarmBroadcast;
import com.daniel.plusnote.activities.MainActivity;
import com.daniel.plusnote.database.NotesDatabase;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;

public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.NoteViewHolder>{

    private List<Note> notes;
    private NotesListener notesListener;
    private boolean isEnabled = true;
    private Context context;

    public NotesAdapter(List<Note> notes, NotesListener notesListener, Context context) {
        this.notes = notes;
        this.notesListener = notesListener;
        this.context = context;
    }

    @NonNull
    @Override
    public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new NoteViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(
                        R.layout.recycler_view_item,
                        parent,
                        false
                )
        );
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onBindViewHolder(@NonNull NoteViewHolder holder, int position) {
        holder.setNote(notes.get(position));
        holder.btnContainer.setVisibility(View.GONE);
        holder.rvi_checkbox.setChecked(notes.get(position).isRvi_checked());
        if (notes.get(position).isRvi_checked()) {
            if (notes.get(position).isIs_list()) {
                holder.note_icon_text.setBackgroundResource(R.drawable.ic_listnote_dark);
                holder.viewButton.setBackgroundResource(R.drawable.ic_listnote_dark);
            } else if (notes.get(position).isIs_image()) {
                if (notes.get(position).isIs_video()) {
                    holder.note_icon_text.setBackgroundResource(R.drawable.ic_video_dark);
                    holder.viewButton.setBackgroundResource(R.drawable.ic_video_dark);
                } else if (notes.get(position).isFrom_gallery()){
                    holder.note_icon_text.setBackgroundResource(R.drawable.ic_image_dark);
                    holder.viewButton.setBackgroundResource(R.drawable.ic_image_dark);
                } else {
                    holder.note_icon_text.setBackgroundResource(R.drawable.ic_camera_dark);
                    holder.viewButton.setBackgroundResource(R.drawable.ic_camera_dark);
                }
            } else if (notes.get(position).isIs_voice()) {
                holder.note_icon_text.setBackgroundResource(R.drawable.ic_microphone_dark);
                holder.viewButton.setBackgroundResource(R.drawable.ic_microphone_dark);
            } else {
                holder.note_icon_text.setBackgroundResource(R.drawable.ic_textnote_dark);
                holder.viewButton.setBackgroundResource(R.drawable.ic_textnote_dark);
            }
            holder.titleOut.setTextColor(Color.parseColor("#707FCEFF"));
            holder.reminder_icon_mini.setBackgroundResource(R.drawable.ic_reminder_1_dark);
            holder.note_icon_text.setEnabled(false);
        } else {
            if (notes.get(position).isIs_list()) {
                holder.note_icon_text.setBackgroundResource(R.drawable.ic_listnote);
                holder.viewButton.setBackgroundResource(R.drawable.ic_listnote);
            } else if (notes.get(position).isIs_image()) {
                if (notes.get(position).isIs_video()) {
                    holder.note_icon_text.setBackgroundResource(R.drawable.ic_video);
                    holder.viewButton.setBackgroundResource(R.drawable.ic_video);
                } else if (notes.get(position).isFrom_gallery()) {
                    holder.note_icon_text.setBackgroundResource(R.drawable.ic_image);
                    holder.viewButton.setBackgroundResource(R.drawable.ic_image);
                } else {
                    holder.note_icon_text.setBackgroundResource(R.drawable.ic_camera);
                    holder.viewButton.setBackgroundResource(R.drawable.ic_camera);
                }
            } else if (notes.get(position).isIs_voice()) {
                holder.note_icon_text.setBackgroundResource(R.drawable.ic_microphone);
                holder.viewButton.setBackgroundResource(R.drawable.ic_microphone);
            } else {
                holder.note_icon_text.setBackgroundResource(R.drawable.ic_textnote);
                holder.viewButton.setBackgroundResource(R.drawable.ic_textnote);
            }
            holder.titleOut.setTextColor(Color.parseColor("#7FCEFF"));
            holder.note_icon_text.setEnabled(true);
            holder.reminder_icon_mini.setBackgroundResource(R.drawable.ic_reminder_1);
        }

        holder.rvi_checkbox.setOnClickListener(view -> {
            if (!notes.get(position).isRvi_checked()) {
                if (notes.get(position).isIs_list()) {
                    holder.note_icon_text.setBackgroundResource(R.drawable.ic_listnote_dark);
                    holder.viewButton.setBackgroundResource(R.drawable.ic_listnote_dark);
                } else if (notes.get(position).isIs_image()) {
                    if (notes.get(position).isIs_video()) {
                        holder.note_icon_text.setBackgroundResource(R.drawable.ic_video_dark);
                        holder.viewButton.setBackgroundResource(R.drawable.ic_video_dark);
                    } else if (notes.get(position).isFrom_gallery()){
                        holder.note_icon_text.setBackgroundResource(R.drawable.ic_image_dark);
                        holder.viewButton.setBackgroundResource(R.drawable.ic_image_dark);
                    } else {
                        holder.note_icon_text.setBackgroundResource(R.drawable.ic_camera_dark);
                        holder.viewButton.setBackgroundResource(R.drawable.ic_camera_dark);
                    }
                } else if (notes.get(position).isIs_voice()) {
                    holder.note_icon_text.setBackgroundResource(R.drawable.ic_microphone_dark);
                    holder.viewButton.setBackgroundResource(R.drawable.ic_microphone_dark);
                } else {
                    holder.note_icon_text.setBackgroundResource(R.drawable.ic_textnote_dark);
                    holder.viewButton.setBackgroundResource(R.drawable.ic_textnote_dark);
                }
                holder.titleOut.setTextColor(Color.parseColor("#707FCEFF"));
                holder.reminder_icon_mini.setBackgroundResource(R.drawable.ic_reminder_1_dark);
                holder.note_icon_text.setEnabled(false);
            } else {
                if (notes.get(position).isIs_list()) {
                    holder.note_icon_text.setBackgroundResource(R.drawable.ic_listnote);
                    holder.viewButton.setBackgroundResource(R.drawable.ic_listnote);
                }
                else if (notes.get(position).isIs_image()) {
                    if (notes.get(position).isIs_video()) {
                        holder.note_icon_text.setBackgroundResource(R.drawable.ic_video);
                        holder.viewButton.setBackgroundResource(R.drawable.ic_video);
                    } else if (notes.get(position).isFrom_gallery()){
                        holder.note_icon_text.setBackgroundResource(R.drawable.ic_image);
                        holder.viewButton.setBackgroundResource(R.drawable.ic_image);
                    }else {
                        holder.note_icon_text.setBackgroundResource(R.drawable.ic_camera);
                        holder.viewButton.setBackgroundResource(R.drawable.ic_camera);
                    }
                }
                else if (notes.get(position).isIs_voice()) {
                    holder.note_icon_text.setBackgroundResource(R.drawable.ic_microphone);
                    holder.viewButton.setBackgroundResource(R.drawable.ic_microphone);
                }
                else {
                    holder.note_icon_text.setBackgroundResource(R.drawable.ic_textnote);
                    holder.viewButton.setBackgroundResource(R.drawable.ic_textnote);
                }
                holder.titleOut.setTextColor(Color.parseColor("#7FCEFF"));
                holder.note_icon_text.setEnabled(true);
                holder.reminder_icon_mini.setBackgroundResource(R.drawable.ic_reminder_1);
            }
            setChecked(notes.get(position), !notes.get(position).isRvi_checked());
        });

        holder.sent_to_tomorrow.setOnClickListener(v -> sendToTomorrow(notes.get(position), context));

        if (notes.get(position).isReminderSet()) {
            holder.reminder_icon_mini.setVisibility(View.VISIBLE);
            holder.reminder_button2.setVisibility(View.VISIBLE);
            holder.reminder_button.setVisibility(View.INVISIBLE);
            holder.time_view_note.setVisibility(View.VISIBLE);
            String[] split = notes.get(position).getReminder_time().split(":");
            if (split[1].length() == 1) {
                split[1] = "0" + split[1];
            }
            if (split[0].length() == 1) {
                split[0] = "0" + split[0];
            }
            holder.time_view_note.setText(split[0] + ":" + split[1]);
        } else {
            holder.reminder_icon_mini.setVisibility(View.GONE);
            holder.reminder_button2.setVisibility(View.GONE);
            holder.reminder_button.setVisibility(View.VISIBLE);
            holder.time_view_note.setVisibility(View.GONE);
        }

        holder.note_icon_text.setOnClickListener(view -> holder.btnContainer.setVisibility(View.VISIBLE));
        holder.shareButton.setOnClickListener(view -> notesListener.onNoteClicked(notes.get(position), position, false, true));
        holder.viewButton.setOnClickListener(view -> {
            notesListener.onNoteClicked(notes.get(position), position);
            holder.btnContainer.setVisibility(View.GONE);
        });
        holder.deleteButton.setOnClickListener(view -> notesListener.onNoteClicked(notes.get(position), position, true));
        holder.reminder_button.setOnClickListener(view ->
                notesListener.onNoteClicked(notes.get(position), position, false, false, true)
        );
        holder.time_view_note.setOnClickListener(view ->
                notesListener.onNoteClicked(notes.get(position), position, false, false, true)
        );
        holder.cancelButton.setOnClickListener(view -> holder.btnContainer.setVisibility(View.GONE));
        holder.btnContainer.setOnClickListener(view -> {
        });
    }

    @Override
    public int getItemCount() {
        return notes.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    static class NoteViewHolder extends RecyclerView.ViewHolder {

        TextView titleOut, audio_length, time_view_note;
        SeekBar seekbar;
        Chronometer playback_timer;
        ImageButton
                note_icon_text,
                cancelButton,
                deleteButton,
                play_btn,
                viewButton,
                shareButton,
                reminder_button,
                reminder_button2, sent_to_tomorrow;
        ImageView reminder_icon_mini;
        ConstraintLayout outContainer;
        ConstraintLayout btnContainer;
        CheckBox rvi_checkbox;

        NoteViewHolder(@NonNull View itemView) {
            super(itemView);
            titleOut = itemView.findViewById(R.id.TitleOut);
            time_view_note = itemView.findViewById(R.id.time_view_note);
            note_icon_text = itemView.findViewById(R.id.search_note_icon);
            outContainer = itemView.findViewById(R.id.textOutContainer);
            btnContainer = itemView.findViewById(R.id.btncontainer);
            cancelButton = itemView.findViewById(R.id.cancelButtonSwipe);
            deleteButton = itemView.findViewById(R.id.deleteButtonSwipe);
            viewButton = itemView.findViewById(R.id.open_view);
            play_btn = itemView.findViewById(R.id.play_button);
            audio_length = itemView.findViewById(R.id.audio_length);
            playback_timer = itemView.findViewById(R.id.playback_timer);
            seekbar = itemView.findViewById(R.id.seekbar_audio);
            shareButton = itemView.findViewById(R.id.shareButtonSwipe);
            reminder_icon_mini = itemView.findViewById(R.id.reminder_icon_mini);
            reminder_button = itemView.findViewById(R.id.reminderButtonSwipe);
            reminder_button2 = itemView.findViewById(R.id.reminderButton2);
            rvi_checkbox = itemView.findViewById(R.id.rvi_checkbox);
            sent_to_tomorrow = itemView.findViewById(R.id.sent_to_tomorrow);
        }

        void setNote(Note note) {
            titleOut.setText(note.getTextNoteTitle());
        }
    }

    private void setChecked(Note note, boolean b) {
        final Note note1 = note;

        note1.setRvi_checked(b);

        @SuppressLint("StaticFieldLeak")
        @SuppressWarnings("deprecation")
        class SaveNoteTask extends AsyncTask<Void, Void, Void> {
            @Override
            protected Void doInBackground(Void... voids) {
                NotesDatabase.getDatabase(context).noteDao().insertNote(note1);
                return null;
            }

            @Override
            protected void onPostExecute(Void unused) {
                super.onPostExecute(unused);
            }
        }
        new SaveNoteTask().execute();
    }

    public void sendToTomorrow(Note alreadyAvailableNote, Context context) {
        final Note note = alreadyAvailableNote;
        final DateTimeFormatter noteDayF = DateTimeFormatter.ofPattern("yyyy_MM_dd");
        LocalDate localDate = LocalDate.from(noteDayF.parse(note.getDate()));
        localDate = localDate.plusDays(1);
        String date = noteDayF.format(localDate);
        note.setDate(date);
        if (alreadyAvailableNote.isReminderSet()) {
            AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            Intent intent = new Intent(context, AlarmBroadcast.class);
            int alarmID = alreadyAvailableNote.getReminder_id();

            PendingIntent pendingIntent = PendingIntent.getBroadcast(
                    context,
                    alarmID,
                    intent,
                    PendingIntent.FLAG_IMMUTABLE);
            alarmManager.cancel(pendingIntent);

            alreadyAvailableNote.setReminder_id(0);
            String reminderTime = alreadyAvailableNote.getReminder_time();

            String[] temp = reminderTime.split(":");
            setAlarm(
                    alreadyAvailableNote.getTextNoteTitle(),
                    date,
                    temp[0] + ":" + temp[1],
                    context,
                    note
            );
        }
        @SuppressLint("StaticFieldLeak")
        @SuppressWarnings("deprecation")
        class SaveNoteTask extends AsyncTask<Void, Void, Void> {
            @Override
            protected Void doInBackground(Void... voids) {
                NotesDatabase.getDatabase(context).noteDao().insertNote(note);
                return null;
            }

            @Override
            protected void onPostExecute(Void unused) {
                super.onPostExecute(unused);
                MainActivity.getNotes(REQUEST_CODE_SHOW_NOTES, false, context);
                Toast.makeText(context, "Sent to tomorrow", Toast.LENGTH_SHORT).show();
            }
        }
        new SaveNoteTask().execute();
    }

    private void setAlarm(String text, String date, String time, Context context, Note note) {
        Intent intent = new Intent(context, AlarmBroadcast.class);
        intent.putExtra("event", text);
        intent.putExtra("time", time);
        intent.putExtra("date", date);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        int alarmID = note.getReminder_id();
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                context,
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
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        long temp = 0;
        if (date1 != null) {
            temp = date1.getTime();
        }
        String[] ts = time.split(":");
        if (ts[0].equals("12")){
            temp += 43200000;
        }

        Log.e("logTime", dateandtime + " // " + temp);
        AlarmManager.AlarmClockInfo aci = new AlarmManager.AlarmClockInfo(temp, pendingIntent);
        alarmManager.setAlarmClock(aci, pendingIntent);
    }

}
