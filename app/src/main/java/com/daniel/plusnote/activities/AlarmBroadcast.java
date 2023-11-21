package com.daniel.plusnote.activities;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.PowerManager;
import android.widget.RemoteViews;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.daniel.plusnote.entities.Note;
import com.daniel.plusnote.R;

import java.util.List;

public class AlarmBroadcast extends BroadcastReceiver {
    private static List<Note> noteList;

    @Override
    public void onReceive(Context context, Intent intent) {
        if (Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())) {
//            getNotes(context);
        } else {
            Bundle bundle = intent.getExtras();
            String text = bundle.getString("event");
            String date = bundle.getString("date") + " " + bundle.getString("time");

            String channelId = "channel_id5";
            CharSequence name = context.getString(R.string.app_main_name);
            String description = context.getString(R.string.app_main_name);
            NotificationChannel channel = new NotificationChannel(
                    channelId,
                    name,
                    NotificationManager.IMPORTANCE_HIGH);
            channel.enableVibration(true);
            channel.setVibrationPattern(new long[] {200, 400, 800, 400, 200});
            channel.setDescription(description);
            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);

            Intent intent1 = new Intent(context, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent1, PendingIntent.FLAG_IMMUTABLE);

            RemoteViews notificationLayout = new RemoteViews(context.getPackageName(), R.layout.notification_small);
            notificationLayout.setTextViewText(R.id.Title, text);
            String[] split = bundle.getString("time").split(":");
            if (split[1].length() == 1) {
                split[1] = "0" + split[1];
            }
            if (split[0].length() == 1) {
                split[0] = "0" + split[0];
            }
            notificationLayout.setTextViewText(R.id.time_show, split[0] + ":" + split[1]);

            NotificationCompat.Builder builder = new NotificationCompat.Builder(context, channelId)
                    .setSmallIcon(R.drawable.ic_notification_icon6)
                    .setWhen(0)
                    .setContent(notificationLayout)
                    .setContentIntent(pendingIntent)
                    .setAutoCancel(true)
                    .setVibrate(new long[] {200, 400, 800, 400, 200})
                    .setDefaults(NotificationCompat.DEFAULT_ALL)
                    .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                    .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                    .setPriority(NotificationCompat.PRIORITY_HIGH);

            PowerManager powerManager = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
            PowerManager.WakeLock  wakeLock = powerManager.newWakeLock(PowerManager.FULL_WAKE_LOCK |
                    PowerManager.ACQUIRE_CAUSES_WAKEUP |
                    PowerManager.ON_AFTER_RELEASE, "PlusNote::WakeLock");
            wakeLock.acquire(60 * 1000L);

            NotificationManagerCompat notificationManager1 = NotificationManagerCompat.from(context);
            notificationManager1.notify((int) System.currentTimeMillis(), builder.build());
        }
    }

//    private static void getNotes(Context context) {
//
//        @SuppressLint("StaticFieldLeak")
//        class GetNotesTask extends AsyncTask<Void, Void, List<Note>> {
//
//            @Override
//            protected List<Note> doInBackground(Void... voids) {
//                return NotesDatabase
//                        .getDatabase(context)
//                        .noteDao()
//                        .getAllNotes();
//            }
//
//            @SuppressLint("NotifyDataSetChanged")
//            @Override
//            protected void onPostExecute(List<Note> notes) {
//                super.onPostExecute(notes);
//                noteList = new ArrayList<>();
//                noteList.addAll(notes);
//                for (int i = 0; i < noteList.size(); i++) {
//                    AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
//                    String[] str = noteList.get(i).getReminder_time().split(":");
//                    String dateandtime = str[2] + " " + str[0] + str[1];
//                    @SuppressLint("SimpleDateFormat")
//                    DateFormat formatter = new SimpleDateFormat("yyyy_MM_dd hh:mm");
//                    Date date1 = null;
//                    try {
//                        date1 = formatter.parse(dateandtime);
//                    } catch (ParseException e) {}
//
//                    Intent intent1 = new Intent(context, AlarmBroadcast.class);
//                    intent1.putExtra("event", noteList.get(i).getTextNoteTitle());
//                    intent1.putExtra("time", str[0] + ":" + str[1]);
//                    intent1.putExtra("date", str[2]);
//                    intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                    int alarmID = noteList.get(i).getReminder_id();
//                    PendingIntent pendingIntent = PendingIntent.getBroadcast(
//                            context,
//                            alarmID,
//                            intent1,
//                            PendingIntent.FLAG_IMMUTABLE);
//                    long temp = 0;
//                    if (date1 != null) {
//                        temp = date1.getTime();
//                    }
//                    am.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, temp, pendingIntent);
//                }
//            }
//        }
//        new GetNotesTask().execute();
//    }
}

