package com.marine.seafarertoolkit;

import androidx.appcompat.app.AppCompatActivity;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.jobdispatcher.Constraint;
import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.GooglePlayDriver;
import com.firebase.jobdispatcher.Job;
import com.firebase.jobdispatcher.Lifetime;
import com.firebase.jobdispatcher.RetryStrategy;
import com.firebase.jobdispatcher.Trigger;
import com.marine.seafarertoolkit.notification.alarmset.AlarmActivity;
import com.marine.seafarertoolkit.notification.alarmset.AlarmService;
import com.marine.seafarertoolkit.notification.MyWorkerNotification;
import com.marine.seafarertoolkit.notification.NotificationJobService;
import com.marine.seafarertoolkit.notification.SharedPreferenceForNotification;
import com.marine.seafarertoolkit.showdoc.ShowInfo;

import org.joda.time.DateTime;

import java.util.Calendar;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class SplashScreen extends AppCompatActivity {

    //    private ScheduleClient scheduleClient;
    DataBaseHelper myDb;

    DateTime validDate;
    //    Date javaValidationDate;
    Calendar calendar;
    Calendar startDayCalendar;
    List<String> v1_date;
    List<String> title;
    List<String> notificationTimes;
    SharedPreferences shPref;
    SharedPreferences sharedPref;
    public static final String MyPref = "MyPrefers";
    public static final String Date = "validDate";
    public static final String Title = "title";
    public static final String NotificationTimes = "NTimes";
    public static final String IsFirstRun = "IsFirstRunApp";
    public static final String profileSetting = "MyProfileSetting";
    public static final String TxtRank = "TxtRank";
    public static final String TxtName = "TxtName";
    public static final String TxtLastName = "TxtLastName";
    TextView seafarer_txt, toolkit_txt;
    LinearLayout layout_splash;


    @Override
    protected void onPostResume() {
        super.onPostResume();
        new SharedPreferenceForNotification(getApplicationContext()).execute();
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash_screen);
//
//        RoundCornerProgressBar roundCornerProgressBar = (RoundCornerProgressBar) findViewById(R.id.progressBar);
//
//        roundCornerProgressBar.setProgress();
//
//        Button doc = (Button) findViewById(R.id.doc);
//        Button show = (Button) findViewById(R.id.show);
//        Button show_add_doc_title = (Button) findViewById(R.id.show_add_doc_title);
        seafarer_txt = (TextView) findViewById(R.id.seafarer_txt);
        toolkit_txt = (TextView) findViewById(R.id.toolkit_txt);
        layout_splash = (LinearLayout) findViewById(R.id.layout_splash);


        myDb = new DataBaseHelper(this);

        shPref = PreferenceManager.getDefaultSharedPreferences(this);
        sharedPref = this.getSharedPreferences(profileSetting, Context.MODE_PRIVATE);
        Boolean firstRun = shPref.getBoolean("IsFirstRunApp", true);

        if (!sharedPref.contains(TxtRank)) {
            SharedPreferences.Editor editor = shPref.edit();
            editor.putBoolean(IsFirstRun, false);
            editor.apply();

            SharedPreferences.Editor editor2 = sharedPref.edit();
            editor2.putString(TxtRank, "Rank");
            editor2.putString(TxtName, "Name");
            editor2.putString(TxtLastName, "LastName");
            editor2.apply();
        }

        if (isAlarmServiceRunning(AlarmService.class))
            startActivity(new Intent(SplashScreen.this, AlarmActivity.class));

//        if(isNotificationServiceRunning(NotificationService.class))startNotification();

        Typeface mFont = Typeface.createFromAsset(getAssets(), "font/PAPYRUS.TTF");
        seafarer_txt.setTypeface(mFont);
        toolkit_txt.setTypeface(mFont);


        if (firstRun) {

            myDb.setDefaultDoc();

            //            Toast.makeText(this, "FFFFFFFFFFFFFFFFFFFF",Toast.LENGTH_LONG).show();

            myDb.close();
        } else {
            myDb.close();
        }

        myDb.close();
//        GetDateForNotification();
//        GetTitleForNotification();

        new SharedPreferenceForNotification(getApplicationContext()).execute();

        workManagerForNotification();
//        GetSharedPref();
//
//        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            workManagerForNotification();
//        }else {
//            workManagerForNotification();
////            Toast.makeText(this, "<<<<m" , Toast.LENGTH_LONG).show();
////            fireBaseDispatcher();
//        }
//
//        startDayCalendar = Calendar.getInstance();
//        startDayCalendar.setTimeInMillis(System.currentTimeMillis());
//        startDayCalendar.set(Calendar.HOUR_OF_DAY, 12);
//        startDayCalendar.set(Calendar.MINUTE, 20);
//
////        v1_date = new ArrayList<>();


        anim_splash();


        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(SplashScreen.this, MainActivity.class));
                finish();
            }
        }, 2300);

//        PowerManager powerManager = (PowerManager) getSystemService(POWER_SERVICE);
//        PowerManager.WakeLock wakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,
//                "app::MyWakelockTag");
//        wakeLock.acquire(86500000);


//
//        doc.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View v) {
//
//                startActivity(new Intent(MainActivity.this, AddDocument.class));
//            }
//        });
//
//        show.setOnClickListener(new View.OnClickListener() {
//
//
//            public void onClick(View v) {
//                startActivity(new Intent(MainActivity.this, ShowInfo.class));
////                startActivity(new Intent(MainActivity.this, AlarmActivity.class));
//
//            }
//        });
//
//        show_add_doc_title.setOnClickListener(new View.OnClickListener() {
//
//
//            public void onClick(View v) {
//                startActivity(new Intent(MainActivity.this, AddDocumentTitle.class));
//            }
//        });

    }

    @Override
    protected void onStop() {
//        Intent intent = new Intent(this.getApplicationContext(), MyBroadcastReceiver.class);
//        PendingIntent pendingIntent = PendingIntent.getBroadcast(this,0,intent,PendingIntent.FLAG_NO_CREATE);


//        new SharedPreferenceForNotification(getApplicationContext()).execute();


//
////        boolean alarmUp  = (PendingIntent.getBroadcast(this, 0, new Intent("com.marine.seafarertoolkit.notification.MyBroadcastReceiver"),
////                PendingIntent.FLAG_NO_CREATE) != null);
//
//        if(!isNotificationServiceRunning(NotificationService.class) && pendingIntent == null && mCursor.getCount() != 0) {
//
//            startNotification();
//        }
        super.onStop();
    }

    //    @Override
//    protected void onDestroy() {
//
//
//        super.onDestroy();
//
//
//    }

//    public String[] GetDateForNotification() {
//
//        myDb = new DataBaseHelper(this);
//        v1_date = myDb.getValidationDateForNotification();
//        List<String> valid_date = new ArrayList<>();
//
//        for (String item : v1_date) {
//            valid_date.add(item.substring(0,10));
//        }
//
//        String[] vDateBuffer;
//        vDateBuffer = valid_date.toArray(new String[valid_date.size()]);
//        return vDateBuffer;
//
//
//    }
//
//    public String[] GetTitleForNotification() {
//
//        myDb = new DataBaseHelper(this);
//        title = myDb.getTitleForValidationDateForNotification();
//        List<String> titleList = new ArrayList<>();
//
//        for (String item : title) {
//            titleList.add(item);
//        }
//
//        String[] titleBuffer;
//        titleBuffer = titleList.toArray(new String[titleList.size()]);
//        return titleBuffer;
//
//    }
//
//    public String[] GetNotificationTimes() {
//
//        myDb = new DataBaseHelper(this);
//
//        notificationTimes = myDb.getNotificationTimes();
//        List<String> notification_times = new ArrayList<>();
//
//        for (String item : notificationTimes) {
//            notification_times.add(item);
//        }
//
//        String[] notificationTimesBuffer;
//        notificationTimesBuffer = notification_times.toArray(new String[notification_times.size()]);
//        return notificationTimesBuffer;
//
//    }


//    public  void startNotification(){
//
//
//        int interval=24*60*60*1000;
//
//        Intent intent1 = new Intent(this, MyWakeFullBroadcastReceiver.class);
//        Intent intent2 = new Intent(this, MyBroadcastReceiver.class);
//        PendingIntent pendingIntent1 = PendingIntent.getBroadcast(this.getApplicationContext(),0, intent1, PendingIntent.FLAG_UPDATE_CURRENT);
//        PendingIntent pendingIntent2 = PendingIntent.getBroadcast(this.getApplicationContext(),0, intent2, PendingIntent.FLAG_UPDATE_CURRENT);
//        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
//
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            Toast.makeText(this, ">>>>m" , Toast.LENGTH_LONG).show();
//            alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, System.currentTimeMillis()+10
//                    ,pendingIntent1);
//        }else {
//            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, startDayCalendar.getTimeInMillis()
//                    ,AlarmManager.INTERVAL_DAY, pendingIntent2);
//        }
//
////
////        Toast.makeText(getApplicationContext(), calendar+"", Toast.LENGTH_SHORT).show();
//
//        Toast.makeText(this, "Alarm set in " + 5 + " seconds",Toast.LENGTH_LONG).show();
//    }


//    public void GetSharedPref (){
//
//        StringBuilder sb = new StringBuilder();
//        for (int i = 0; i < GetDateForNotification().length; i++) {
//            sb.append(GetDateForNotification()[i]).append(",");
//        }
//
//        StringBuilder sb1 = new StringBuilder();
//        for (int i = 0; i < GetTitleForNotification().length; i++) {
//            sb1.append(GetTitleForNotification()[i]).append(",");
//        }
//
//        StringBuilder sb2 = new StringBuilder();
//        for (int i = 0; i < GetNotificationTimes().length; i++) {
//            sb2.append(GetNotificationTimes()[i]).append(",");
//        }
//
//        shPref = getSharedPreferences(MyPref, Context.MODE_PRIVATE);
//        SharedPreferences.Editor editor = shPref.edit();
//        editor.clear().apply();
//
//        editor.putString(Date,sb.toString());
//        editor.putString(Title,sb1.toString());
//        editor.putString(NotificationTimes,sb2.toString());
//
//        editor.apply();
//
//    }

    private boolean isAlarmServiceRunning(Class<?> AlarmService) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (AlarmService.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    private boolean isNotificationServiceRunning(Class<?> NotificationService) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (NotificationService.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    private void fireBaseDispatcher() {
        FirebaseJobDispatcher dispatcher = new FirebaseJobDispatcher(new GooglePlayDriver(this));
        dispatcher.cancelAll();

        Job myJob = dispatcher.newJobBuilder()
                .setService(NotificationJobService.class)
                .setTag("my-notification-tag")
                .setConstraints(Constraint.ON_ANY_NETWORK)
                .setConstraints(Constraint.DEVICE_CHARGING)
                .setConstraints(Constraint.DEVICE_IDLE)
                .setRecurring(true)
                .setLifetime(Lifetime.FOREVER)
                .setReplaceCurrent(false)
                .setTrigger(Trigger.executionWindow(10, 30))
                .setRetryStrategy(RetryStrategy.DEFAULT_EXPONENTIAL)
                .build();

        dispatcher.mustSchedule(myJob);

    }

    private void workManagerForNotification() {
//        Toast.makeText(this, "workmanager" , Toast.LENGTH_LONG).show();
        WorkManager mWorkManager = WorkManager.getInstance(this);
        mWorkManager.cancelAllWork();
        PeriodicWorkRequest workRequest = new PeriodicWorkRequest.Builder(MyWorkerNotification.class, 15, TimeUnit.MINUTES).build();
        mWorkManager.enqueue(workRequest);
    }

    private void anim_splash() {
        Animation animation = AnimationUtils.loadAnimation(SplashScreen.this, R.anim.alphalayout);
        layout_splash.setAnimation(animation);
        animation.reset();

        Animation animation2 = AnimationUtils.loadAnimation(SplashScreen.this, R.anim.translate_seafarer);
        seafarer_txt.setAnimation(animation2);
        animation2.reset();

        Animation animation3 = AnimationUtils.loadAnimation(SplashScreen.this, R.anim.translate_toolkit);
        toolkit_txt.setAnimation(animation3);
        animation3.reset();
    }


}
