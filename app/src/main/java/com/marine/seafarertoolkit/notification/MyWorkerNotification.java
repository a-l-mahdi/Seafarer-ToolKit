package com.marine.seafarertoolkit.notification;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.marine.seafarertoolkit.DataBaseHelper;
import com.marine.seafarertoolkit.R;

import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.Calendar;

import static android.content.Context.NOTIFICATION_SERVICE;

public class MyWorkerNotification extends Worker {


    LocalDate vDate;
    String[] vDateBuffer;
    String[] title;
    String[] notificationTimes;
    String[] id;
    Calendar calendar;
    Context context;
    DataBaseHelper myDb;

    public static final String MyPref = "MyPrefers";
    public static final String Date = "validDate";
    public static final String Title = "title";
    public static final String NotificationTimes = "NTimes";
    public static final String ID = "id";


    NotificationManager notifManager;
    String validationNotification = "Seafarer ToolKit";


    public MyWorkerNotification(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        this.context = context;
    }

    @NonNull
    @Override
    public Result doWork() {

        myDb = new DataBaseHelper(context);

        notifManager = (android.app.NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
//        SharedPreferenceForNotification sharedPreferenceForNotification = new SharedPreferenceForNotification(getApplicationContext());

        SharedPreferences prefs = context.getSharedPreferences(MyPref, Context.MODE_PRIVATE);
        String valid = prefs.getString(Date, null);
        String titleBuffer = prefs.getString(Title, null);
        String notificationTimesBuffer = prefs.getString(NotificationTimes, null);
        String idBuffer = prefs.getString(ID, null);


        calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);

//        vDateBuffer = new String[0];


        vDateBuffer = valid.split(",");
        title = titleBuffer.split(",");
        notificationTimes = notificationTimesBuffer.split(",");
        id = idBuffer.split(",");

        Log.d("date buffer", vDateBuffer.length + "");

        createNotifChannel();

        if (!valid.isEmpty()) {
            for (int i = 0; i < vDateBuffer.length; i++) {

                String item = vDateBuffer[i];
                DateTimeFormatter formatterValidDate = DateTimeFormat.forPattern("yyyy-MM-dd");
                vDate = LocalDate.parse(item, formatterValidDate);
//                int ind = Arrays.asList(vDateBuffer).indexOf(item);

                if ((vDate.isEqual(LocalDate.now()) || vDate.isBefore(LocalDate.now())) && (hour == 11 || hour == 12)
                        && (Integer.parseInt(notificationTimes[i]) == 0)) {
                    notificationApply(i);
                    myDb.setNotificationTimes(id[i], "1");
                    new SharedPreferenceForNotification(context).execute();

                } else if ((vDate.isEqual(LocalDate.now()) || vDate.isBefore(LocalDate.now())) && (hour == 21 || hour == 22)
                        && Integer.parseInt(notificationTimes[i]) == 1) {
                    notificationApply(i);
                    myDb.setNotificationTimes(id[i], "2");
                    new SharedPreferenceForNotification(context).execute();

                }

            }
        }
        return null;
    }


    private void notificationApply(int i) {


        Intent notificationIntent = new Intent(context, NotificationActivity.class);
        notificationIntent.putExtra("title", title[i]);
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent contentIntent = PendingIntent.getActivity(context, i + 1, notificationIntent, 0);


        NotificationCompat.Builder sNotifBuilder = new NotificationCompat.Builder(context, validationNotification)
                .setSmallIcon(R.drawable.notif_icon)
                .setContentTitle("Attention..!")
                .setContentText(title[i] + " Validation Date For Join Finish...!")
                .setVibrate(new long[]{100, 500, 500, 500, 500, 500})
                .setLargeIcon(BitmapFactory.decodeResource(null, R.drawable.android))
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setContentIntent(contentIntent)
                .setAutoCancel(true);


        notifManager.notify(i, sNotifBuilder.build());


    }


    private void createNotifChannel() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            String offerChannelName = "SeafarerToolKit";
            String offerChannelDescription = "Document Validation Finished";
            int offerChannelImportance = android.app.NotificationManager.IMPORTANCE_DEFAULT;

            NotificationChannel notifChannel = new NotificationChannel(validationNotification, offerChannelName, offerChannelImportance);
            notifChannel.setDescription(offerChannelDescription);
            notifChannel.enableVibration(true);
            notifChannel.enableLights(true);
            notifChannel.setLightColor(Color.GREEN);

            notifManager.createNotificationChannel(notifChannel);

        }

    }


}
