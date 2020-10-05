package com.marine.seafarertoolkit;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class AboutActivity extends AppCompatActivity {
    TextView telegram_link;
    ImageButton telegram;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        telegram_link = (TextView) findViewById(R.id.telegram_link);
        telegram = (ImageButton) findViewById(R.id.telegram);

        telegram_link.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent telegram = new Intent(Intent.ACTION_VIEW, Uri.parse("https://t.me/Android_dev1991"));
                    telegram.setPackage("org.telegram.messenger");
                    startActivity(telegram);
                } catch (Exception e) {
                    Toast.makeText(AboutActivity.this, "Telegram app is not installed", Toast.LENGTH_LONG).show();
                }
            }
        });

        telegram.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent telegram = new Intent(Intent.ACTION_VIEW, Uri.parse("https://t.me/Android_dev1991"));
                    telegram.setPackage("org.telegram.messenger");
                    startActivity(telegram);
                } catch (Exception e) {
                    Toast.makeText(AboutActivity.this, "Telegram app is not installed", Toast.LENGTH_LONG).show();
                }
            }
        });

    }
}
