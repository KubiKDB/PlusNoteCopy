package com.daniel.plusnote.activities;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.IntentService;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;

import androidx.annotation.Nullable;

import com.daniel.plusnote.entities.Note;
import com.daniel.plusnote.database.NotesDatabase;

import java.util.List;

public class NotificationService extends IntentService {
    private static List<Note> noteList;

    public NotificationService() {
        super("name");
    }

    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        return START_STICKY;
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent alarmIntent = new Intent(this, AlarmBroadcast.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, alarmIntent, 0);
        alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), pendingIntent);
//        Toast.makeText(this, "Reset Alarms", Toast.LENGTH_SHORT).show();
//        getNotes(this);
//        for (int i = 0; i < noteList.size(); i++) {
//            String[] str = noteList.get(i).getReminder_time().split(":");
//            String dateandtime = str[2] + " " + str[0] + str[1];
//            @SuppressLint("SimpleDateFormat")
//            DateFormat formatter = new SimpleDateFormat("yyyy_MM_dd hh:mm");
//            Date date1 = null;
//            try {
//                date1 = formatter.parse(dateandtime);
//            } catch (ParseException e) {
//            }
//
//            Intent intent1 = new Intent(this, AlarmBroadcast.class);
//            intent.putExtra("event", noteList.get(i).getTextNoteTitle());
//            intent.putExtra("time", str[0] + ":" + str[1]);
//            intent.putExtra("date", str[2]);
//            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//            int alarmID = noteList.get(i).getReminder_id();
//            PendingIntent pendingIntent = PendingIntent.getBroadcast(
//                    this,
//                    alarmID,
//                    intent1,
//                    PendingIntent.FLAG_IMMUTABLE);
//            long temp = 0;
//            if (date1 != null) {
//                temp = date1.getTime();
//            }
//            AlarmManager.AlarmClockInfo aci = new AlarmManager.AlarmClockInfo(temp, pendingIntent);
//            alarmManager.setAlarmClock(aci, pendingIntent);
//            Log.e("ResetAlarm", String.valueOf(temp));
//        }
    }

    private static void getNotes(Context context) {

        @SuppressLint("StaticFieldLeak")
        class GetNotesTask extends AsyncTask<Void, Void, List<Note>> {

            @Override
            protected List<Note> doInBackground(Void... voids) {
                return NotesDatabase
                        .getDatabase(context)
                        .noteDao()
                        .getAllNotes();
            }

            @SuppressLint("NotifyDataSetChanged")
            @Override
            protected void onPostExecute(List<Note> notes) {
                super.onPostExecute(notes);
                noteList.addAll(notes);
            }
        }
        new GetNotesTask().execute();
    }
}
