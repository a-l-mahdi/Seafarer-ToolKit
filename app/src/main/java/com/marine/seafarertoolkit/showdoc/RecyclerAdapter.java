package com.marine.seafarertoolkit.showdoc;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.marine.seafarertoolkit.DataBaseHelper;
import com.marine.seafarertoolkit.R;
import com.marine.seafarertoolkit.ShowDocumentInfo;
import com.marine.seafarertoolkit.notification.SharedPreferenceForNotification;
import com.white.progressview.HorizontalProgressView;

import org.joda.time.Days;
import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.List;


public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {

//
//    NotificationManager notifManager;
//    String validationNotification;

    Context context;
    DataBaseHelper myDb;
    List<DataInfo> datainfos;

    Integer deleteRow;

    LocalDate sDate;
    LocalDate xDate;
    LocalDate vDate;
    int passDays;
    int validDays;
    int diff_days;
    int fullDays;
    float percentDaysGreen;
    float percentDaysRed;
    private int i;


    public RecyclerAdapter(Context context, List<DataInfo> datainfos) {
        this.context = context;
        this.datainfos = datainfos;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.recycler_item, viewGroup, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int i) {


        final DataInfo dataInfo = datainfos.get(i);

//        DateTime localDate = new DateTime();

        final String startDate = dataInfo.issue;
        final String expiryDate = dataInfo.expiry;
        String validDateRaw = dataInfo.validationDate;

        String validDate = validDateRaw.substring(0, 10);

        DateTimeFormatter formatterStartDay = DateTimeFormat.forPattern("dd/MM/yyyy");
        DateTimeFormatter formatterExpiryDay = DateTimeFormat.forPattern("dd/MM/yyyy");
        DateTimeFormatter formatterValidDate = DateTimeFormat.forPattern("yyyy-MM-dd");

//        sDate = formatter.parseDateTime(startDate);
        sDate = LocalDate.parse(startDate, formatterStartDay);
        xDate = LocalDate.parse(expiryDate, formatterExpiryDay);
//        vDate = formatterValidDate.parseDateTime(validDate);
        vDate = LocalDate.parse(validDate, formatterValidDate);


        passDays = Days.daysBetween(sDate, LocalDate.now()).getDays();
        fullDays = Days.daysBetween(sDate, xDate).getDays();
        validDays = Days.daysBetween(sDate, vDate).getDays();

        diff_days = dataInfo.diffDays;

        percentDaysGreen = (float) passDays / (float) diff_days * 100;
        percentDaysRed = (float) validDays / (float) diff_days * 100;

//        Toast.makeText(context,percentDaysGreen+"", Toast.LENGTH_SHORT).show();
//        Toast.makeText(context,percentDaysRed+"", Toast.LENGTH_SHORT).show();


//        holder.id.setText("ID: "+dataInfo.id);
        holder.title.setText("" + dataInfo.title);
        holder.title.postDelayed(new Runnable() {
            @Override
            public void run() {
                holder.title.setSelected(true);
            }
        }, 2500);

        holder.issue.setText("Date of Issue: " + dataInfo.issue);
        holder.expiry.setText("Date of Expiry: " + dataInfo.expiry);
        holder.validation.setText("Date of Validation: " + vDate.getDayOfMonth() + "/" + vDate.getMonthOfYear() + "/" + vDate.getYear());
//        holder.progress.setProgress(Math.round(percentDaysGreen));
//        holder.horizontalProgressView.setProgress(Math.round(percentDaysGreen));

        holder.horizontalProgressView.setReachBarSize(7);

//        holder.horizontalProgressView.setNormalBarSize(1);
        holder.horizontalProgressView.setMax(fullDays);

//        holder.horizontalProgressView.setProgressInTime(0,Math.round(percentDaysGreen),1000);
        holder.horizontalProgressView.setProgressInTime(fullDays, fullDays - passDays, 1000);
//        holder.horizontalProgressView.setMin(0);

//        holder.horizontalProgressView.setTextPrefix(String.valueOf(fullDays-passDays));
        holder.horizontalProgressView.setTextSuffix(" d");
//
//        holder.horizontalProgressView.setValue((float) (fullDays-passDays));

//        holder.horizontalProgressView.setProgressColor(5,10);

//
//        holder.horizontalProgressView.setColorSupplier(new HorizontalProgressView.ColorSupplier() {
//            @Override
//            public int getColor(float v) {
//
////                int color = 0;
////                color = Color.GREEN;
////                if(v<=validDays){
////                    color = Color.GREEN;
////                }else if(v>validDays){
////                    color = Color.RED;
////                }
//                return Color.GREEN;
//            }
//        });


        if (vDate.isAfter(LocalDate.now())) {
            holder.horizontalProgressView.setReachBarColor(Color.GREEN);
            holder.horizontalProgressView.setTextColor(Color.GREEN);
        } else if (vDate.isBefore(LocalDate.now()) || vDate.isEqual(LocalDate.now())) {
            holder.horizontalProgressView.setReachBarColor(Color.RED);
            holder.horizontalProgressView.setTextColor(Color.RED);
        }

//        if(Math.round(percentDaysGreen)<Math.round(percentDaysRed)){
//            holder.horizontalProgressView.setReachBarColor(Color.GREEN);
//            holder.horizontalProgressView.setTextColor(Color.GREEN);
//        }else if(Math.round(percentDaysGreen)>=Math.round(percentDaysRed)){
//            holder.horizontalProgressView.setReachBarColor(Color.RED);
//            holder.horizontalProgressView.setTextColor(Color.RED);
//        }


        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ShowDocumentInfo.class);
                intent.putExtra("titleKey", dataInfo.title);
                intent.putExtra("issueKey", dataInfo.issue);
                intent.putExtra("expiryKey", dataInfo.expiry);
                intent.putExtra("validationKey", dataInfo.validationDate);
                context.startActivity(intent);
            }
        });


        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDb = new DataBaseHelper(context);

                AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
                alertDialog.setMessage("Do You Want To Delete \n" + dataInfo.title + "?");
                alertDialog.setNegativeButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deleteRow = myDb.deleteData("document", dataInfo.id);
                        if (deleteRow > 0) {
                            Toast.makeText(context, "Doc Deleted", Toast.LENGTH_SHORT).show();
                            datainfos.remove(i);
                            notifyItemRemoved(i);
                            notifyItemRangeChanged(i, datainfos.size());
                            new SharedPreferenceForNotification(context).execute();
                        } else {
                            Toast.makeText(context, "Doc Not Deleted", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                alertDialog.setPositiveButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

                AlertDialog alert = alertDialog.create();
                alert.show();


            }
        });


//        holder.edite.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                Intent intent = new Intent(context, AddDocument.class);
//                intent.putExtra("idKey",  dataInfo.id);
//                intent.putExtra("titleKey",  dataInfo.title);
//                intent.putExtra("issueKey",  dataInfo.issue);
//                intent.putExtra("expiryKey",  dataInfo.expiry);
//
//                context.startActivity(intent);
//            }
//        });

    }

    @Override
    public int getItemCount() {
        return datainfos.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView id, title, issue, expiry, validation;

        ImageButton delete, edite;

//        ProgressBar progress;

//        NumberProgressBar progress;

        HorizontalProgressView horizontalProgressView;


        LinearLayout linearLayout;

        CardView cardView;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);


//            id = (TextView) itemView.findViewById(R.id.id);
            title = (TextView) itemView.findViewById(R.id.doc_title);
            issue = (TextView) itemView.findViewById(R.id.doc_issue);
            validation = (TextView) itemView.findViewById(R.id.doc_validation);
            expiry = (TextView) itemView.findViewById(R.id.doc_expiry);
            horizontalProgressView = (HorizontalProgressView) itemView.findViewById(R.id.progressBar);


//            progress = (NumberProgressBar) itemView.findViewById(R.id.progressBar);
            linearLayout = (LinearLayout) itemView.findViewById(R.id.linearlayout);
            cardView = (CardView) itemView.findViewById(R.id.cardview);


//            progress.setIndeterminateDrawable(new SmoothProgressDrawable.Builder(context).color(0xff0000)
//            .interpolator(new DecelerateInterpolator())
//            .sectionsCount(4)
//            .separatorLength(8)
//            .strokeWidth(8f)
//            .speed(2f)
//            .progressiveStartSpeed(2)
//            .progressiveStopSpeed(3.4f)
//            .reversed(false)
//            .mirrorMode(false)
//            .progressiveStart(true)
//            .build());
//
//            cardView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    context.startActivity(new Intent(context, ShowDocumentInfo.class));
//                }
//            });


//            progress.getProgressDrawable().setColorFilter(Color.GREEN, PorterDuff.Mode.SRC_IN);
//
//            if(progress.getProgress()>10){
//                progress.getProgressDrawable().setColorFilter(Color.RED, PorterDuff.Mode.SRC_IN);
//            }

            delete = (ImageButton) itemView.findViewById(R.id.item_delete);
//            edite=(Button) itemView.findViewById(R.id.item_edit);


        }
    }


//    private void createNotifChannel() {
//
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//
//            String offerChannelName = "Seafarer ToolKit";
//            String offerChannelDescription= "Document Validation Finished";
//            int offerChannelImportance = android.app.NotificationManager.IMPORTANCE_DEFAULT;
//
//            NotificationChannel notifChannel = new NotificationChannel(validationNotification, offerChannelName, offerChannelImportance);
//            notifChannel.setDescription(offerChannelDescription);
//            //notifChannel.enableVibration(true);
//            notifChannel.enableLights(true);
//            notifChannel.setLightColor(Color.GREEN);
//
//            notifManager.createNotificationChannel(notifChannel);
//
//        }
//
//    }


//    public void simpleNotification(String title , String content) {
//
//        NotificationCompat.Builder sNotifBuilder = new NotificationCompat.Builder(context, validationNotification)
//                .setSmallIcon(R.drawable.notif_icon)
//                .setContentTitle(title)
//                .setContentText(content)
//                .setVibrate(new long[]{100,500,500,500,500,500})
//                .setLargeIcon(BitmapFactory.decodeResource(null, R.drawable.android))
//                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
//                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC);
//
//        notifManager.notify(1, sNotifBuilder.build());
//
//    }

}
