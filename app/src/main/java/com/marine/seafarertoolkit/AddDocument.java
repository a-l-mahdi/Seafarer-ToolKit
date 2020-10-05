package com.marine.seafarertoolkit;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputLayout;
import com.marine.seafarertoolkit.notification.SharedPreferenceForNotification;
import com.marine.seafarertoolkit.spinner.DocumentTitleAdapter;
import com.marine.seafarertoolkit.spinner.DocumentTitleForSpinner;


import org.joda.time.Days;
import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;

public class AddDocument extends AppCompatActivity /* implements AdapterView.OnItemSelectedListener*/ {


    private static final Pattern DATE_PATTERN = Pattern.compile(
            "^" +
                    "([0-2][0-9]|3[0-1])" +
                    "/" +
                    "(0[0-9]|1[0-2])" +
                    "/" +
                    "([0-9][0-9])?[0-9][0-9]" +
                    "$"
    );

    DataBaseHelper myDb;
    private TextInputLayout edt_expiry;
    private TextInputLayout edt_issue;
    Button insert;

    Bundle bundle;

    int x = 0;

    //    int diff_days;
    String strDiffDays;
    int validMonth;

    LocalDate sDate;
    LocalDate eDate;

    LocalDate issueDate;
    LocalDate expiryDate;

    //   DateTime validation;
    LocalDate endDateForValidation;

    Calendar startDayCalendar;

    List<String> v1_date;
    List<String> title;
    List<String> notificationTimes;

    boolean isFirst;


    Spinner spinner;

    String docTitle;

    DatePicker datePicker;

    SharedPreferences shPref;
    public static final String MyPref = "MyPrefers";
    public static final String Date = "validDate";
    public static final String Title = "title";
    public static final String NotificationTimes = "NTimes";

    SharedPreferenceForNotification sharedPreferenceForNotification;

    private ArrayList<DocumentTitleForSpinner> mDocumentTitle;
    private DocumentTitleAdapter mAdapter;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_document);


        edt_issue = (TextInputLayout) findViewById(R.id.issue_date);
        edt_expiry = (TextInputLayout) findViewById(R.id.expiry_date);
        insert = (Button) findViewById(R.id.insert_doc);

        spinner = findViewById(R.id.spinner_title);
//        maskedEditText = findViewById(R.id.masked_edit_text);

        isFirst = true;

//        datePicker = (DatePicker) findViewById(R.id.date_picker);

        myDb = new DataBaseHelper(this);

        AddData();

        loadSpinnerData();


//        maskedEditText = new MaskedEditText.Builder(this)
//                .build();
//        maskedEditText.setMaskedText("dd/MM/yyyy");                     //set text into widget it will be look like 8 (555) 123 55-67

//        edt_issue.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                x=1;
//                showDatePickerDailog();
//
//            }
//        });
//
//
//        edt_expiry.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                x=2;
//                showDatePickerDailog();
//
//            }
//        });


//        bundle=getIntent().getExtras();
//
//        if(bundle!=null){
//
//        edt_issue.setText(bundle.getString("issueKey"));
//        edt_expiry.setText(bundle.getString("expiryKey"));
//        }


        spinner.setPrompt("Select");

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                DocumentTitleForSpinner documentTitleForSpinner = (DocumentTitleForSpinner) parent.getItemAtPosition(position);
                docTitle = documentTitleForSpinner.getmDocumentTitle();
//                Toast.makeText(AddDocument.this, position+"", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        Objects.requireNonNull(edt_issue.getEditText()).addTextChangedListener(new TextWatcher() {

            private String current = "";
            private String current1 = "";
            private String ddmmyyyy = "DDMMYYYY";
            private String ddmmyyyy1 = "\f\f\f\f\f\f\f\f";
            private Calendar cal = Calendar.getInstance();
            private Calendar cal1 = Calendar.getInstance();

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {


                edt_expiry.setError(null);


                CharSequence s1 = s;


                if (!s.toString().equals(current)) {
                    String clean = s.toString().replaceAll("[^\\d.]|\\.", "");
                    String cleanC = current.replaceAll("[^\\d.]|\\.", "");

                    int cl = clean.length();
                    int sel = cl;
                    for (int i = 2; i <= cl && i < 6; i += 2) {
                        sel++;
                    }
                    //Fix for pressing delete_icon next to a forward slash
                    if (clean.equals(cleanC)) sel--;

                    if (clean.length() < 8) {
                        clean = clean + ddmmyyyy.substring(clean.length());
                    } else {
                        //This part makes sure that when we finish entering numbers
                        //the date is correct, fixing it otherwise
                        int day = Integer.parseInt(clean.substring(0, 2));
                        int mon = Integer.parseInt(clean.substring(2, 4));
                        int year = Integer.parseInt(clean.substring(4, 8));

                        mon = mon < 1 ? 1 : mon > 12 ? 12 : mon;
                        cal.set(Calendar.MONTH, mon - 1);
                        year = (year < 1900) ? 1900 : (year > 2100) ? 2100 : year;
                        cal.set(Calendar.YEAR, year);
                        // ^ first set year for the line below to work correctly
                        //with leap years - otherwise, date e.g. 29/02/2012
                        //would be automatically corrected to 28/02/2012

                        day = (day > cal.getActualMaximum(Calendar.DATE)) ? cal.getActualMaximum(Calendar.DATE) : day;
                        clean = String.format("%02d%02d%02d", day, mon, year);
                    }

                    clean = String.format("%s/%s/%s", clean.substring(0, 2),
                            clean.substring(2, 4),
                            clean.substring(4, 8));

                    sel = sel < 0 ? 0 : sel;
                    current = clean;
                    edt_issue.setHelperText(current);
                }


                if (!s1.toString().equals(current1)) {
                    String clean1 = s1.toString().replaceAll("[^\\d.]|\\.", "");
                    String cleanC1 = current1.replaceAll("[^\\d.]|\\.", "");

                    int cl1 = clean1.length();
                    int sel1 = cl1;
                    for (int i = 2; i <= cl1 && i < 6; i += 2) {
                        sel1++;
                    }
                    //Fix for pressing delete_icon next to a forward slash
                    if (clean1.equals(cleanC1)) sel1--;

                    if (clean1.length() < 8) {
                        clean1 = clean1 + ddmmyyyy1.substring(clean1.length());
                    } else {
                        //This part makes sure that when we finish entering numbers
                        //the date is correct, fixing it otherwise
                        int day1 = Integer.parseInt(clean1.substring(0, 2));
                        int mon1 = Integer.parseInt(clean1.substring(2, 4));
                        int year1 = Integer.parseInt(clean1.substring(4, 8));

                        mon1 = mon1 < 1 ? 1 : mon1 > 12 ? 12 : mon1;
                        cal1.set(Calendar.MONTH, mon1 - 1);
                        year1 = (year1 < 1900) ? 1900 : (year1 > 2100) ? 2100 : year1;
                        cal1.set(Calendar.YEAR, year1);
                        // ^ first set year for the line below to work correctly
                        //with leap years - otherwise, date e.g. 29/02/2012
                        //would be automatically corrected to 28/02/2012

                        day1 = (day1 > cal1.getActualMaximum(Calendar.DATE)) ? cal1.getActualMaximum(Calendar.DATE) : day1;
                        clean1 = String.format("%02d%02d%02d", day1, mon1, year1);
                    }

                    clean1 = String.format("%s/%s/%s", clean1.substring(0, 2),
                            clean1.substring(2, 4),
                            clean1.substring(4, 8));

                    sel1 = sel1 < 0 ? 0 : sel1;
                    current1 = clean1;
                    edt_issue.getEditText().setText(current1);
                    edt_issue.getEditText().setSelection(sel1 < current1.length() ? sel1 : current1.length());
                }


            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        edt_expiry.getEditText().addTextChangedListener(new TextWatcher() {

            private String current = "";
            private String current1 = "";
            private String ddmmyyyy = "DDMMYYYY";
            private String ddmmyyyy1 = "\f\f\f\f\f\f\f\f";
            private Calendar cal = Calendar.getInstance();
            private Calendar cal1 = Calendar.getInstance();

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {


                edt_issue.setError(null);

                CharSequence s1 = s;


                if (!s.toString().equals(current)) {
                    String clean = s.toString().replaceAll("[^\\d.]|\\.", "");
                    String cleanC = current.replaceAll("[^\\d.]|\\.", "");

                    int cl = clean.length();
                    int sel = cl;
                    for (int i = 2; i <= cl && i < 6; i += 2) {
                        sel++;
                    }
                    //Fix for pressing delete_icon next to a forward slash
                    if (clean.equals(cleanC)) sel--;

                    if (clean.length() < 8) {
                        clean = clean + ddmmyyyy.substring(clean.length());
                    } else {
                        //This part makes sure that when we finish entering numbers
                        //the date is correct, fixing it otherwise
                        int day = Integer.parseInt(clean.substring(0, 2));
                        int mon = Integer.parseInt(clean.substring(2, 4));
                        int year = Integer.parseInt(clean.substring(4, 8));

                        mon = mon < 1 ? 1 : mon > 12 ? 12 : mon;
                        cal.set(Calendar.MONTH, mon - 1);
                        year = (year < 1900) ? 1900 : (year > 2100) ? 2100 : year;
                        cal.set(Calendar.YEAR, year);
                        // ^ first set year for the line below to work correctly
                        //with leap years - otherwise, date e.g. 29/02/2012
                        //would be automatically corrected to 28/02/2012

                        day = (day > cal.getActualMaximum(Calendar.DATE)) ? cal.getActualMaximum(Calendar.DATE) : day;
                        clean = String.format("%02d%02d%02d", day, mon, year);
                    }

                    clean = String.format("%s/%s/%s", clean.substring(0, 2),
                            clean.substring(2, 4),
                            clean.substring(4, 8));

                    sel = sel < 0 ? 0 : sel;
                    current = clean;
                    edt_expiry.setHelperText(current);
                }

                if (!s1.toString().equals(current1)) {
                    String clean1 = s1.toString().replaceAll("[^\\d.]|\\.", "");
                    String cleanC1 = current1.replaceAll("[^\\d.]|\\.", "");

                    int cl1 = clean1.length();
                    int sel1 = cl1;
                    for (int i = 2; i <= cl1 && i < 6; i += 2) {
                        sel1++;
                    }
                    //Fix for pressing delete_icon next to a forward slash
                    if (clean1.equals(cleanC1)) sel1--;

                    if (clean1.length() < 8) {
                        clean1 = clean1 + ddmmyyyy1.substring(clean1.length());
                    } else {
                        //This part makes sure that when we finish entering numbers
                        //the date is correct, fixing it otherwise
                        int day1 = Integer.parseInt(clean1.substring(0, 2));
                        int mon1 = Integer.parseInt(clean1.substring(2, 4));
                        int year1 = Integer.parseInt(clean1.substring(4, 8));

                        mon1 = mon1 < 1 ? 1 : mon1 > 12 ? 12 : mon1;
                        cal1.set(Calendar.MONTH, mon1 - 1);
                        year1 = (year1 < 1900) ? 1900 : (year1 > 2100) ? 2100 : year1;
                        cal1.set(Calendar.YEAR, year1);
                        // ^ first set year for the line below to work correctly
                        //with leap years - otherwise, date e.g. 29/02/2012
                        //would be automatically corrected to 28/02/2012

                        day1 = (day1 > cal1.getActualMaximum(Calendar.DATE)) ? cal1.getActualMaximum(Calendar.DATE) : day1;
                        clean1 = String.format("%02d%02d%02d", day1, mon1, year1);
                    }

                    clean1 = String.format("%s/%s/%s", clean1.substring(0, 2),
                            clean1.substring(2, 4),
                            clean1.substring(4, 8));

                    sel1 = sel1 < 0 ? 0 : sel1;
                    current1 = clean1;
                    edt_expiry.getEditText().setText(current1);
                    edt_expiry.getEditText().setSelection(sel1 < current1.length() ? sel1 : current1.length());
                }


            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


    }


    private boolean validateIssueDate() {
        String issueDateInput = edt_issue.getEditText().getText().toString().trim();
        if (issueDateInput.isEmpty()) {
            edt_issue.setError("Date Of Issue can't be empty");
            return false;
        } else if (!DATE_PATTERN.matcher(issueDateInput).matches()) {
            edt_issue.setError("Please input correct Date...!");
            return false;
        } else {
            edt_issue.setError(null);
            return true;
        }
    }

    private boolean validateExpiryDate() {
        String expiryDateInput = edt_expiry.getEditText().getText().toString().trim();
        if (expiryDateInput.isEmpty()) {
            edt_expiry.setError("Date Of Issue can't be empty");
            return false;
        } else if (!DATE_PATTERN.matcher(expiryDateInput).matches()) {
            edt_expiry.setError("Please input correct Date...!");
            return false;
        } else {
            edt_expiry.setError(null);
            return true;
        }
    }


//    private void showDatePickerDailog(){
////
////
////        SimpleDateFormat sdf=new SimpleDateFormat("dd/MM/yyyy");
////        java.util.Date d=new Date();
////        sdf.format(d);
////        static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
////        DateTimeFormatter formatter = DateTimeFormat.forPattern(DATE_FORMAT);
//
//        MaterialDatePicker.Builder builder =
//                MaterialDatePicker.Builder.datePicker();
//        if(x==1) builder.setTitleText("Date Of Issue");
//        else if(x==2) builder.setTitleText("Date Of Expiry");
//        builder.setInputMode(MaterialDatePicker.INPUT_MODE_TEXT);
//        final MaterialDatePicker picker = builder.build();
//        picker.show(getSupportFragmentManager(), "picker.toString()");
//
//        CalendarConstraints.Builder constraintBuilder = new CalendarConstraints.Builder();
//        picker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener() {
//            @Override
//            public void onPositiveButtonClick(Object selection) {
//
////                String date = picker.getdayOfMonth + "/" + (month+1) + "/" + year +"";
//
//
//                if(x==1) {
//                    edt_issue.setText(picker.getHeaderText());
//                    x=0;
//                }else if(x==2){
//                    edt_expiry.setText(picker.getHeaderText());
//                    x=0;
//                }
//
//            }
//        });
//
//
//
//
//
//
////        DatePickerDialog datePickerDialog = new DatePickerDialog(
////                this,
////                this,
////                Calendar.getInstance().get(Calendar.YEAR),
////                Calendar.getInstance().get(Calendar.MONTH),
////                Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
////        );
////        datePickerDialog.show();
//
//    }


    public void AddData() {

        insert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!validateIssueDate() | !validateExpiryDate()) {
                    Toast.makeText(AddDocument.this, "Please input correct Date...!", Toast.LENGTH_SHORT).show();
                } else if (validateIssueDate() & validateExpiryDate()) {

                    DateTimeFormatter formatterStartDay = DateTimeFormat.forPattern("dd/MM/yyyy");
                    DateTimeFormatter formatterExpiryDay = DateTimeFormat.forPattern("dd/MM/yyyy");
                    issueDate = LocalDate.parse(edt_issue.getEditText().getText().toString(), formatterStartDay);
                    expiryDate = LocalDate.parse(edt_expiry.getEditText().getText().toString(), formatterExpiryDay);

                    if (issueDate.isAfter(expiryDate) || issueDate.isEqual(expiryDate)) {
                        Toast.makeText(AddDocument.this, "Date of expiry should after Issue Date....!", Toast.LENGTH_SHORT).show();
                        edt_expiry.setError("Date of expiry should after Issue Date!");
                    } else if (issueDate.isAfter(LocalDate.now())) {
                        edt_issue.setError("Date of issue is wrong!");
                        Toast.makeText(AddDocument.this, "Date of issue is wrong....!", Toast.LENGTH_SHORT).show();
                    } else if (expiryDate.isBefore(LocalDate.now())) {
                        edt_expiry.setError("This document expired!");
                        Toast.makeText(AddDocument.this, "This document already expired....!", Toast.LENGTH_SHORT).show();
                    } else if (expiryDate.isAfter(issueDate)) {

                        boolean isInsert = myDb.insertData(docTitle,
                                edt_issue.getEditText().getText().toString(),
                                edt_expiry.getEditText().getText().toString(), daysDifference(), validationDay(), "0");

                        if (isInsert) {
                            edt_issue.getEditText().setText(null);
                            edt_expiry.getEditText().setText(null);

//                    SetSharedPref();
                            new SharedPreferenceForNotification(getApplicationContext()).execute();
                            Toast.makeText(AddDocument.this, "OK", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(AddDocument.this, "Errrrorr", Toast.LENGTH_SHORT).show();
                        }

                    }

                }

            }
        });
    }


//    @Override
//    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
//        String date = dayOfMonth + "/" + (month+1) + "/" + year +"";
//
//        if(x==1) {
//            edt_issue.setText(date);
//            x=0;
//        }else if(x==2){
//            edt_expiry.setText(date);
//            x=0;
//        }
//
//    }


    public void loadSpinnerData() {
        DataBaseHelper db = new DataBaseHelper(getApplicationContext());

        List<String> title = db.getAllDocTitle();

        mDocumentTitle = new ArrayList<>();


        for (String item : title) {
            mDocumentTitle.add(new DocumentTitleForSpinner(item));

        }

        mAdapter = new DocumentTitleAdapter(this, mDocumentTitle, isFirst);
        spinner.setAdapter(mAdapter);


//        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,title);
//
//        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//
//        spinner.setAdapter(dataAdapter);
//
//        spinner.setOnItemSelectedListener(this);


//        spinner.flo("Document Title");

//        spinner.setOnItemSelectedListener(this);


    }

//    @Override
//    protected void onDestroy() {
//        startNotification();
//        super.onDestroy();
//    }
//
//    @Override
//    protected void onPause() {
//        startNotification();
//        super.onPause();
//    }


//
//    @Override
//    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//
//        String label = parent.getItemAtPosition(position).toString();
//
//        docTitle = label;
//
//    }
//
//    @Override
//    public void onNothingSelected(AdapterView<?> parent) {
//
//    }


    public int daysDifference() {


        String startDate = edt_issue.getEditText().getText().toString();
        String endDate = edt_expiry.getEditText().getText().toString();


        DateTimeFormatter formatterStartDay = DateTimeFormat.forPattern("dd/MM/yyyy");
        DateTimeFormatter formatterExpiryDay = DateTimeFormat.forPattern("dd/MM/yyyy");

        sDate = LocalDate.parse(startDate, formatterStartDay);
        eDate = LocalDate.parse(endDate, formatterExpiryDay);


        int diff_days = Days.daysBetween(sDate, eDate).getDays();

        return diff_days;

    }

    public String validationDay() {

        DataBaseHelper db = new DataBaseHelper(getApplicationContext());

        String endDate = edt_expiry.getEditText().getText().toString();
        DateTimeFormatter formatter = DateTimeFormat.forPattern("dd/MM/yyyy");
        endDateForValidation = LocalDate.parse(endDate, formatter);

        List<String> list = db.validationDate(docTitle);
        validMonth = Integer.parseInt(list.get(0));
//        Toast.makeText(AddDocument.this, validMonth + "", Toast.LENGTH_SHORT).show();
        LocalDate validation = new LocalDate(endDateForValidation.minusDays(validMonth * 30));
//
//        Toast.makeText(AddDocument.this, validation+"", Toast.LENGTH_SHORT).show();
        return validation.toString();

    }


    public String[] GetDateForNotification() {

        myDb = new DataBaseHelper(this);
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

        myDb = new DataBaseHelper(this);

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

        myDb = new DataBaseHelper(this);

        notificationTimes = myDb.getNotificationTimes();
        List<String> notification_times = new ArrayList<>();

        for (String item : notificationTimes) {
            notification_times.add(item);
        }

        String[] notificationTimesBuffer;
        notificationTimesBuffer = notification_times.toArray(new String[notification_times.size()]);
        return notificationTimesBuffer;

    }


    public void SetSharedPref() {

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

        shPref = getSharedPreferences(MyPref, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = shPref.edit();
        editor.clear().apply();

        editor.putString(Date, sb.toString());
        editor.putString(Title, sb1.toString());
        editor.putString(NotificationTimes, sb2.toString());

        editor.apply();

    }


//    public  void startNotification(){
//
//        Intent intent = new Intent(this.getApplicationContext(), MyBroadcastReceiver.class);
//        PendingIntent pendingIntent = PendingIntent.getBroadcast(
//                this.getApplicationContext(),0, intent, 0);
//        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
//        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, startDayCalendar.getTimeInMillis()
//                , AlarmManager.INTERVAL_DAY, pendingIntent);
//
//
//        Toast.makeText(this, "Alarm set in " + 5 + " seconds",Toast.LENGTH_LONG).show();
//    }
}
