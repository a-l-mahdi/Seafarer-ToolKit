package com.marine.seafarertoolkit;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.white.progressview.HorizontalProgressView;

import org.joda.time.Days;
import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;


public class ShowDocumentInfo extends AppCompatActivity {

    TextView doc_title_show, doc_issue_show, doc_expiry_show, doc_validation_show, remain_days_to_validity, remain_days_to_expiry;
    HorizontalProgressView progressbar_show;
    String title, issue_date, expiry_date, validation_date;
    LocalDate sDate;
    LocalDate vDate;
    LocalDate xDate;

    int passDays;
    int fullDays;

    Bundle bundle;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_document);

        doc_title_show = (TextView) findViewById(R.id.doc_title_show);
        doc_issue_show = (TextView) findViewById(R.id.doc_issue_show);
        doc_expiry_show = (TextView) findViewById(R.id.doc_expiry_show);
        doc_validation_show = (TextView) findViewById(R.id.doc_validation_show);
        remain_days_to_validity = (TextView) findViewById(R.id.remain_days_to_validity);
        remain_days_to_expiry = (TextView) findViewById(R.id.remain_days_to_expiry);

        progressbar_show = (HorizontalProgressView) findViewById(R.id.progressbar_show);


        bundle = getIntent().getExtras();

        title = bundle.getString("titleKey");
        issue_date = bundle.getString("issueKey");
        expiry_date = bundle.getString("expiryKey");
        validation_date = bundle.getString("validationKey");

        String validDate = validation_date.substring(0, 10);

        DateTimeFormatter formatterStartDay = DateTimeFormat.forPattern("dd/MM/yyyy");
        DateTimeFormatter formatterValidDate = DateTimeFormat.forPattern("yyyy-MM-dd");
        DateTimeFormatter formatterExpiryDay = DateTimeFormat.forPattern("dd/MM/yyyy");

        sDate = LocalDate.parse(issue_date, formatterStartDay);
        vDate = LocalDate.parse(validDate, formatterValidDate);
        xDate = LocalDate.parse(expiry_date, formatterExpiryDay);


        passDays = Days.daysBetween(sDate, LocalDate.now()).getDays();
        fullDays = Days.daysBetween(sDate, xDate).getDays();

        doc_title_show.setText(title);
        doc_issue_show.setText("Date of Issue: " + issue_date);
        doc_expiry_show.setText("Date of Expiry: " + expiry_date);
        doc_validation_show.setText("This Doc. is Valid up to: " + vDate.getDayOfMonth() + "/" + vDate.getMonthOfYear() + "/" + vDate.getYear());
        if (Days.daysBetween(LocalDate.now(), vDate).getDays() <= 0) {
            remain_days_to_validity.setText("Validation date: Passed");
            remain_days_to_validity.setTextColor(Color.RED);
            remain_days_to_validity.setTextSize(18);
        } else {
            remain_days_to_validity.setText("Days remain until valid date: " + Days.daysBetween(LocalDate.now(), vDate).getDays() + " Days");
        }

        if (Days.daysBetween(LocalDate.now(), xDate).getDays() <= 0) {
            remain_days_to_expiry.setText("Expiry Date: Passed");
            remain_days_to_expiry.setTextColor(Color.RED);
            remain_days_to_expiry.setTextSize(18);
        } else {
            remain_days_to_expiry.setText(Days.daysBetween(LocalDate.now(), xDate).getDays() + " Days more will be expired");
        }

        progressbar_show.setReachBarSize(7);
        progressbar_show.setMax(fullDays);
        progressbar_show.setProgressInTime(fullDays, fullDays - passDays, 1000);
        progressbar_show.setTextSuffix(" d");

        if (vDate.isAfter(LocalDate.now())) {
            progressbar_show.setReachBarColor(Color.GREEN);
            progressbar_show.setTextColor(Color.GREEN);
        } else if (vDate.isBefore(LocalDate.now()) || vDate.isEqual(LocalDate.now())) {
            progressbar_show.setReachBarColor(Color.RED);
            progressbar_show.setTextColor(Color.RED);
        }

    }
}
