package com.hosnydev.azkar;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.os.Build;
import android.os.SystemClock;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.hosnydev.azkar.Notification.Alarm;
import com.judemanutd.autostarter.AutoStartPermissionHelper;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    // view ui
    private Switch mSwitch;
    private Spinner mSpinner;
    private LinearLayout contentTime;
    private AdView mAdView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //    getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_main);

        // find view
        mSpinner = findViewById(R.id.mSpinner);
        mSwitch = findViewById(R.id.mSwitch);
        contentTime = findViewById(R.id.contentTime);

        MobileAds.initialize(this, getString(R.string.admob_app_id));

        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);


        // text view marquee
        TextView mText = findViewById(R.id.mText);
        mText.setSelected(true);

        AutoStartPermissionHelper.getInstance().getAutoStartPermission(this);

        // list of minute name
        final String[] timeName = {
                "1 m",
                "5 m",
                "10 m",
                "15 m",
                "20 m",
                "30 m",
                "60 m"};

        // list of minute time
        final int[] timeMinute = {
                60 * 1000,
                5 * 60 * 1000,
                10 * 60 * 1000,
                15 * 60 * 1000,
                20 * 60 * 1000,
                30 * 60 * 1000,
                60 * 60 * 1000,
        };

        // custom spinner display
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                R.layout.spinner_layout,
                R.id.spinnerItem,
                timeName);
        mSpinner.setAdapter(adapter);

        // check if switch turn on or off
        // true if the switch is in the On position

        mSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked) {

                    SharedPreferences preferencesTime = getSharedPreferences("time", MODE_PRIVATE);
                    int index = preferencesTime.getInt("index", 0);

                    mSpinner.setSelection(index);

                    getSharedPreferences("stateRun", MODE_PRIVATE)
                            .edit()
                            .putInt("state", 1)
                            .apply();
                    contentTime.setVisibility(View.VISIBLE);

                } else {

                    contentTime.setVisibility(View.GONE);
                    getSharedPreferences("stateRun", MODE_PRIVATE)
                            .edit()
                            .putInt("state", 0)
                            .apply();

                }

            }
        });


        // change selected spinner
        mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                int time = timeMinute[i];

                getSharedPreferences("time", MODE_PRIVATE)
                        .edit()
                        .putInt("time", time)
                        .putInt("index", i)
                        .apply();

                // runAlarm();
                runAlarm2();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }


    private void runAlarm2() {

        SharedPreferences preferences = getSharedPreferences("time", MODE_PRIVATE);
        int time = preferences.getInt("time", 0);

        if (time != 0) {

            AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
            Intent alertIntent = new Intent(MainActivity.this, Alarm.class);

            PendingIntent pendingIntent = PendingIntent.getBroadcast(
                    MainActivity.this,
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

    // on start to check if switch is on or off in shared pref
    @Override
    protected void onStart() {
        super.onStart();

        SharedPreferences preferences = getSharedPreferences("stateRun", MODE_PRIVATE);
        int state = preferences.getInt("state", 0);

        if (state == 1) {

            contentTime.setVisibility(View.VISIBLE);
            mSwitch.setChecked(true);


        } else {

            contentTime.setVisibility(View.GONE);
            mSwitch.setChecked(false);

        }

    }

    @Override
    public void onBackPressed() {
        finishAffinity();
    }

}
