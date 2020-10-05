package com.marine.seafarertoolkit.notification.alarmset;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.marine.seafarertoolkit.R;

public class AlarmActivity extends AppCompatActivity {

    MediaPlayer mediaPlayer;
    Button stopAlarm;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.alarm_activity);

        stopAlarm = (Button) findViewById(R.id.stop_alarm);

        startService(new Intent(AlarmActivity.this, AlarmService.class));

        stopAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopService(new Intent(AlarmActivity.this, AlarmService.class));
            }
        });
//        MediaPlayer mediaPlayer = new MediaPlayer();
//        mediaPlayer = MediaPlayer.create(this,R.raw.alarm);
//        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
//        mediaPlayer.setLooping(true);
//        mediaPlayer.start();
    }

    @Override
    protected void onStop() {
//        mediaPlayer = MediaPlayer.create(this,R.raw.alarm);
//        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
//        mediaPlayer.setLooping(true);
//        mediaPlayer.start();
////        mediaPlayer.stop();
////        finish();
//        stopService(new Intent(AlarmActivity.this,AlarmService.class));
        super.onStop();
    }
}
