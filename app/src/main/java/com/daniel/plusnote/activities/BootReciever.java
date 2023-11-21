package com.daniel.plusnote.activities;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

import com.daniel.plusnote.entities.Note;
import com.daniel.plusnote.database.NotesDatabase;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class BootReciever extends BroadcastReceiver {
    private static List<Note> noteList;

    @Override
    public void onReceive(Context context, Intent intent) {
        if (Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())){
            getNotes(context);
        }
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
                noteList = new ArrayList<>();
                noteList.addAll(notes);
                for (int i = 0; i < noteList.size(); i++) {
                    if (noteList.get(i).isReminderSet()){
                        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
                        String[] str = noteList.get(i).getReminder_time().split(":");
                        Log.e("time", Arrays.toString(str));
                        String dateandtime = str[2] + " " + str[0] + ":" + str[1];
                        @SuppressLint("SimpleDateFormat")
                        DateFormat formatter = new SimpleDateFormat("yyyy_MM_dd hh:mm");
                        Date date1 = null;
                        try {
                            date1 = formatter.parse(dateandtime);
                        } catch (ParseException e) {
                            Log.e("date", e.getLocalizedMessage());
                        }

                        Intent intent1 = new Intent(context, AlarmBroadcast.class);
                        intent1.putExtra("event", noteList.get(i).getTextNoteTitle());
                        intent1.putExtra("time", str[0] + ":" + str[1]);
                        intent1.putExtra("date", str[2]);
                        intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        int alarmID = noteList.get(i).getReminder_id();
                        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                                context,
                                alarmID,
                                intent1,
                                PendingIntent.FLAG_IMMUTABLE);
                        long temp = 0;
                        if (date1 != null) {
                            temp = date1.getTime();
                        }

                        if (str[0].equals("12")){
                            temp += 43200000;
                        }

                        if (!date1.before(new Date())){
                            am.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, temp, pendingIntent);
                        }
                    }
                }
            }
        }
        new GetNotesTask().execute();
    }
}
