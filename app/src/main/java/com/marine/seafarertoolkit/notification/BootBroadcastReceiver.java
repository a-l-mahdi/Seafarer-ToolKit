package com.marine.seafarertoolkit.notification;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import java.util.Calendar;
import java.util.concurrent.TimeUnit;


public class BootBroadcastReceiver extends BroadcastReceiver {

    Calendar startDayCalendar;

    @Override
    public void onReceive(Context context, Intent intent) {


        startDayCalendar = Calendar.getInstance();
        startDayCalendar.setTimeInMillis(System.currentTimeMillis());
        startDayCalendar.set(Calendar.HOUR_OF_DAY, 21);
        startDayCalendar.set(Calendar.MINUTE, 10);

        WorkManager mWorkManager = WorkManager.getInstance(context);
        mWorkManager.cancelAllWork();
        PeriodicWorkRequest workRequest = new PeriodicWorkRequest.Builder(MyWorkerNotification.class, 15, TimeUnit.MINUTES).build();
        mWorkManager.enqueue(workRequest);
    }
}
