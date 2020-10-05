package com.marine.seafarertoolkit.notification;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;

import com.marine.seafarertoolkit.DataBaseHelper;

import java.util.ArrayList;
import java.util.List;

public class SharedPreferenceForNotification extends AsyncTask<Void, Void, Void> {

    private final Context mContext;
    DataBaseHelper myDb;
    List<String> v1_date;
    List<String> title;
    List<String> notificationTimes;
    SharedPreferences shPref;
    public static final String MyPref = "MyPrefers";
    public static final String Date = "validDate";
    public static final String Title = "title";
    public static final String ID = "id";
    public static final String NotificationTimes = "NTimes";

    public SharedPreferenceForNotification(final Context context) {
        mContext = context;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected Void doInBackground(Void... voids) {


        myDb = new DataBaseHelper(mContext);

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < GetDateForNotification().length; i++) {
            sb.append(GetDateForNotification()[i]).append(",");
        }

        StringBuilder sb1 = new StringBuilder();
        for (int i = 0; i < GetTitleForNotification().length; i++) {
            sb1.append(GetTitleForNotification()[i]).append(",");
        }

        StringBuilder sb2 = new StringBuilder();
        for (int i = 0; i < GetNotificationTimes().length; i++) {
            sb2.append(GetNotificationTimes()[i]).append(",");
        }

        StringBuilder sb3 = new StringBuilder();
        for (int i = 0; i < GetIdForNotification().length; i++) {
            sb3.append(GetIdForNotification()[i]).append(",");
        }

        shPref = mContext.getSharedPreferences(MyPref, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = shPref.edit();
        editor.clear().apply();

        editor.putString(Date, sb.toString());
        editor.putString(Title, sb1.toString());
        editor.putString(NotificationTimes, sb2.toString());
        editor.putString(ID, sb3.toString());

        editor.apply();


        return null;
    }


    public String[] GetIdForNotification() {

        v1_date = myDb.getIdForNotification();
        List<String> id = new ArrayList<>();

        for (String item : v1_date) {
            id.add(item);
        }

        String[] vDateBuffer;
        vDateBuffer = id.toArray(new String[id.size()]);
        return vDateBuffer;
    }

    public String[] GetDateForNotification() {

        v1_date = myDb.getValidationDateForNotification();
        List<String> valid_date = new ArrayList<>();

        for (String item : v1_date) {
            valid_date.add(item.substring(0, 10));
        }

        String[] vDateBuffer;
        vDateBuffer = valid_date.toArray(new String[valid_date.size()]);
        return vDateBuffer;
    }

    public String[] GetTitleForNotification() {

        title = myDb.getTitleForValidationDateForNotification();
        List<String> titleList = new ArrayList<>();

        for (String item : title) {
            titleList.add(item);
        }

        String[] titleBuffer;
        titleBuffer = titleList.toArray(new String[titleList.size()]);
        return titleBuffer;
    }

    public String[] GetNotificationTimes() {

        notificationTimes = myDb.getNotificationTimes();
        List<String> notification_times = new ArrayList<>();

        for (String item : notificationTimes) {
            notification_times.add(item);
        }

        String[] notificationTimesBuffer;
        notificationTimesBuffer = notification_times.toArray(new String[notification_times.size()]);
        return notificationTimesBuffer;
    }

}
