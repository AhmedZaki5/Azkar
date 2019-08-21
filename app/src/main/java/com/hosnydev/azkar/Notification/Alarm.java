package com.hosnydev.azkar.Notification;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.os.Build;
import android.os.PowerManager;

import androidx.core.app.NotificationCompat;

import com.hosnydev.azkar.MainActivity;
import com.hosnydev.azkar.R;


import static android.content.Context.MODE_PRIVATE;
import static android.content.Context.POWER_SERVICE;

public class Alarm extends BroadcastReceiver {

    int NOTIFICATION_ID = 234;
    int state;

    @Override
    public void onReceive(Context context, Intent intent) {

        SharedPreferences preferences = context.getSharedPreferences("stateRun", MODE_PRIVATE);
        state = preferences.getInt("state", 0);

        if (state == 1) {
            createNotification(context, context.getString(R.string.titleHome));
            MediaPlayer mediaPlayer = MediaPlayer.create(context, R.raw.mp);
            mediaPlayer.start();
        }

    }

    public void createNotification(Context context, String msg) {

        NotificationManager notificationManager = (NotificationManager)
                context.getSystemService(Context.NOTIFICATION_SERVICE);

        String CHANNEL_ID = "my_channel_01";

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {

            CharSequence name = "my_channel";
            String Description = "This is my channel";
            int importance = NotificationManager.IMPORTANCE_HIGH;

            NotificationChannel mChannel = new NotificationChannel(CHANNEL_ID, name, importance);
            mChannel.setDescription(Description);
            mChannel.enableLights(true);

            mChannel.setLightColor(Color.WHITE);
            mChannel.enableVibration(true);
            mChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500});
            mChannel.setShowBadge(false);
            notificationManager.createNotificationChannel(mChannel);
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID);

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder.setSmallIcon(R.drawable.noty);
            builder.setStyle(new NotificationCompat.BigTextStyle()
                    .bigText(msg));
            builder.setColor(context.getResources().getColor(R.color.colorPrimaryDark));

        } else {
            builder.setSmallIcon(R.drawable.noty);
            builder.setStyle(new NotificationCompat.BigTextStyle()
                    .bigText(msg));
        }

        builder.setContentTitle("Azkar")
                .setContentText(msg)
                .setLights(Color.WHITE, 3000, 300)
                .setVibrate(new long[]{100, 200, 300, 400, 500})
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));

        Intent resultIntent = new Intent(context, MainActivity.class);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addParentStack(MainActivity.class);
        stackBuilder.addNextIntent(resultIntent);

        PendingIntent resultPendingIntent = PendingIntent.getActivity(
                context, 0, new Intent(context, MainActivity.class), 0);

        builder.setContentIntent(resultPendingIntent);
        builder.setAutoCancel(true);

        notificationManager.notify(NOTIFICATION_ID, builder.build());

    }
}