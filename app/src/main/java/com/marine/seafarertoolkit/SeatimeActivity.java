package com.marine.seafarertoolkit;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Toast;

import java.io.File;

public class SeatimeActivity extends AppCompatActivity {

    private static final int REQUEST_CODE = 6000;

    SharedPreferences sharedPrefImage;
    String profileImageUri = "";
    public static final String profileImage = "MyProfileImage";
    public static final String profileSetting = "MyProfileSetting";
    public static final String ImageUri = "ImageUri";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seatime);


        sharedPrefImage = this.getSharedPreferences(profileImage, Context.MODE_PRIVATE);


        profileImageUri = sharedPrefImage.getString(ImageUri, "");

        Toast.makeText(this, profileImageUri + "", Toast.LENGTH_SHORT).show();

        Intent intent = new Intent("com.intsig.camscanner.ACTION_SCAN");
// Or content uri picked from gallery
        Uri uri = Uri.fromFile(new File("/sdcard/source.jpg"));
        intent.putExtra(Intent.EXTRA_STREAM, "file:///data/user/0/com.marine.seafarertoolkit/cache/1593540052316.jpg");
        intent.putExtra("scanned_image", "file:///data/user/0/com.marine.seafarertoolkit/cache/150.jpg");
        intent.putExtra("pdf_path", "/sdcard/processed.jpg");
        intent.putExtra("org_image", "/sdcard/org.jpg");
        startActivityForResult(intent, REQUEST_CODE);


    }


    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE) {
//            int responseCode = data.getIntExtra("RESULT_OK", -1);
            if (requestCode == Activity.RESULT_OK) {
                Toast.makeText(this, "sucsses", Toast.LENGTH_SHORT).show();
                // Success
            } else if (resultCode == Activity.RESULT_FIRST_USER) {
                // Fail
            } else if (resultCode == Activity.RESULT_CANCELED) {
                // User canceled
            }
        }
    }
}