package com.hosnydev.azkar.Notification;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.PowerManager;
import android.os.SystemClock;

import static android.content.Context.POWER_SERVICE;

public class AutoStart extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        if (Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())) {

            SharedPreferences preferences = context.getSharedPreferences("time", Context.MODE_PRIVATE);
            int time = preferences.getInt("time", 0);

            if (time != 0) {

                AlarmManager alarmManager = (AlarmManager)
                        context.getSystemService(
                                Context.ALARM_SERVICE);

                Intent alertIntent = new Intent(context, Alarm.class);

                PendingIntent pendingIntent = PendingIntent.getBroadcast(
                        context,
                        1,
                        alertIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT);
                alarmManager.setInexactRepeating(
                        AlarmManager.RTC,
                        SystemClock.elapsedRealtime()
                        , time
                        , pendingIntent);
            }
        }
    }
}