package com.marine.seafarertoolkit.notification.alarmset;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO Auto-generated method stub
//        Toast.makeText(context, "Alarm reciver", Toast.LENGTH_SHORT).show();
        Intent i = new Intent(context, AlarmActivity.class);
////        i.setClassName(context.getPackageName(), AlarmActivity.class.getName());
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
//        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(i);
    }


}