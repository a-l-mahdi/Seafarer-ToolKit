package com.marine.seafarertoolkit;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
//import android.support.annotation.Nullable;
//import android.support.v4.content.ContextCompat;
//import android.support.v7.app.AlertDialog;
//import android.support.v7.app.AppCompatActivity;
//import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleOwner;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.material.textfield.TextInputLayout;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.marine.seafarertoolkit.notification.SharedPreferenceForNotification;
import com.marine.seafarertoolkit.profilepic.GlideApp;
import com.marine.seafarertoolkit.profilepic.ImagePickerActivity;
import com.marine.seafarertoolkit.showdoc.ShowInfo;
import com.skydoves.balloon.ArrowOrientation;
import com.skydoves.balloon.Balloon;
import com.skydoves.balloon.BalloonAnimation;
import com.skydoves.balloon.OnBalloonClickListener;
import com.skydoves.balloon.OnBalloonOutsideTouchListener;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SeafarerSetting extends AppCompatActivity {
    private static final String TAG = SplashScreen.class.getSimpleName();
    public static final int REQUEST_IMAGE = 100;

    TextView txt_rank, txt_name;
    ImageButton img_rank, img_name, img_btn_document_in_rank;

    String selectedRank;

    //    private final Context mContext;
    SharedPreferences sharedPrefImage;
    SharedPreferences sharedPrefName;

    @BindView(R.id.img_profile)
    ImageView imgProfile;
    String profileImageUri = "";
    public static final String profileImage = "MyProfileImage";
    public static final String profileSetting = "MyProfileSetting";
    public static final String ImageUri = "ImageUri";
    public static final String TxtRank = "TxtRank";
    public static final String TxtName = "TxtName";
    public static final String TxtLastName = "TxtLastName";


    private TextInputLayout edt_name;
    private Spinner spinner_rank;
    private TextInputLayout edt_last_name;


    ListView setting_list_doc_title;
    List<String> title = new ArrayList<String>();
//    private Object LifecycleOwner;

//    public SeafarerSetting(Context mContext) {
//        this.mContext = mContext;
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        ButterKnife.bind(this);
//        Toolbar toolbar = findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setTitle(null);

        txt_name = findViewById(R.id.txt_name);
        txt_rank = findViewById(R.id.txt_rank);

        img_name = findViewById(R.id.img_name);
        img_rank = findViewById(R.id.img_rank);
        img_btn_document_in_rank = findViewById(R.id.img_btn_document_in_rank);

        setting_list_doc_title = findViewById(R.id.setting_list_title);


        sharedPrefImage = this.getSharedPreferences(profileImage, Context.MODE_PRIVATE);
        sharedPrefName = this.getSharedPreferences(profileSetting, Context.MODE_PRIVATE);


        profileImageUri = sharedPrefImage.getString(ImageUri, "");

//        loadProfileDefault();
        if (!profileImageUri.equals("")) loadPreviousProfileImage();
        else loadProfileDefault();

//
//        File imagePath = new File(getApplicationContext().getCacheDir(), "camera");
//        File newFile = new File(imagePath, "20200410_165832.jpg");
//        Uri contentUri = FileProvider.getUriForFile(getApplicationContext(), "com.marine.seafarertoolkit", newFile);
//        loadProfile(contentUri.toString());

        if (Objects.equals(sharedPrefName.getString(TxtRank, ""), "Rank")) {
            txt_rank.setText("Rank");
        } else {
            txt_rank.setText(sharedPrefName.getString(TxtRank, ""));
        }

        if (Objects.equals(sharedPrefName.getString(TxtName, ""), "Name")) {
            txt_name.setText("Name & Last Name");
        } else {
            txt_name.setText(sharedPrefName.getString(TxtName, "") + " " + sharedPrefName.getString(TxtLastName, ""));
        }


        loadListData();


        LifecycleOwner lifecycleOwner = new LifecycleOwner() {
            @NonNull
            @Override
            public Lifecycle getLifecycle() {
                return null;
            }
        };


        img_rank.setOnClickListener(v -> {


            ArrayList<String> rank_title = new ArrayList<>();
            ArrayList<String> rank_abr = new ArrayList<>();

            rank_title.add("Master");
            rank_title.add("Chief Engineer");
            rank_title.add("Chief Officer");
            rank_title.add("2nd Engineer");
            rank_title.add("2nd Officer");
            rank_title.add("ETO");
            rank_title.add("Catering Officer");
            rank_title.add("3rd Engineer");
            rank_title.add("3rd Officer");
            rank_title.add("4th Engineer");
            rank_title.add("Jr Engineer");
            rank_title.add("Gas Engineer");
            rank_title.add("Bosun");
            rank_title.add("Fitter");
            rank_title.add("Pump Man");
            rank_title.add("Able Seaman");
            rank_title.add("Oiler");
            rank_title.add("Ordinary Seaman");
            rank_title.add("Wiper");
            rank_title.add("Chief Cook");
            rank_title.add("1st Cook");
            rank_title.add("2nd Cook");
            rank_title.add("Mess Man");
            rank_title.add("Deck Cadet");
            rank_title.add("Engine Cadet");


            rank_abr.add("Master");
            rank_abr.add("Ch/ENG");
            rank_abr.add("Ch/OFF");
            rank_abr.add("2nd/ENG");
            rank_abr.add("2nd/OFF");
            rank_abr.add("ETO");
            rank_abr.add("Cat/OFF");
            rank_abr.add("3rd/ENG");
            rank_abr.add("3rd/OFF");
            rank_abr.add("4th/ENG");
            rank_abr.add("Jr/ENG");
            rank_abr.add("Gas/ENG");
            rank_abr.add("BSN");
            rank_abr.add("FTR");
            rank_abr.add("P/M");
            rank_abr.add("A/B");
            rank_abr.add("OLR");
            rank_abr.add("O/S");
            rank_abr.add("WPR");
            rank_abr.add("Ch/Ck");
            rank_abr.add("1st/Ck");
            rank_abr.add("2nd/Ck");
            rank_abr.add("M/M");
            rank_abr.add("Dk/C");
            rank_abr.add("Eng/C");


            LayoutInflater inflater = LayoutInflater.from(this);
            View view = inflater.inflate(R.layout.dialog_rank, null);

            spinner_rank = (Spinner) view.findViewById(R.id.spinner_rank);


            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, rank_title);

            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            spinner_rank.setAdapter(dataAdapter);

            spinner_rank.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    selectedRank = parent.getItemAtPosition(position).toString();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }

            });

            AlertDialog.Builder alertDialog = new AlertDialog.Builder(SeafarerSetting.this);

            alertDialog.setView(view);

            alertDialog.setNegativeButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    txt_rank.setText(rank_abr.get(rank_title.indexOf(selectedRank)));
                    SharedPreferences.Editor editor = sharedPrefName.edit();
                    editor.putString(TxtRank, rank_abr.get(rank_title.indexOf(selectedRank)));
                    editor.apply();

                }
            });


            AlertDialog alert = alertDialog.create();
            alert.show();


        });


        img_name.setOnClickListener(v -> {


            LayoutInflater inflater = LayoutInflater.from(this);
            View view = inflater.inflate(R.layout.dialog_name, null);

            edt_name = (TextInputLayout) view.findViewById(R.id.edt_name);
            edt_last_name = (TextInputLayout) view.findViewById(R.id.edt_last_name);

            AlertDialog alertDialog = new AlertDialog.Builder(SeafarerSetting.this)
                    .setView(view)
                    .setCancelable(false)
                    .setPositiveButton("OK", null)
                    .setNegativeButton("Cancel", null)
                    .show();

            Button positiveBtn = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE);
            Button negativeBtn = alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE);

            edt_name.getEditText().addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    edt_name.setError(null);
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });

            edt_last_name.getEditText().addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    edt_last_name.setError(null);
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });


            negativeBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    alertDialog.dismiss();
                }
            });


            positiveBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (edt_name.getEditText().getText().toString().equals("")) {
                        edt_name.setError("Insert your Name...!");
                    } else if (edt_last_name.getEditText().getText().toString().equals("")) {
                        edt_last_name.setError("Insert your Last Name...!");
                    } else if (!edt_name.getEditText().getText().toString().equals("") && !edt_last_name.getEditText().getText().toString().equals("")) {
                        txt_name.setText(edt_name.getEditText().getText().toString() + " " + edt_last_name.getEditText().getText().toString());
                        SharedPreferences.Editor editor = sharedPrefName.edit();
                        editor.putString(TxtName, edt_name.getEditText().getText().toString());
                        editor.putString(TxtLastName, edt_last_name.getEditText().getText().toString());
                        editor.apply();
                        alertDialog.dismiss();
                    }
                }
            });

//            AlertDialog alert = alertDialog.create();
//            alert.show();


        });


        Balloon balloon = new Balloon.Builder(this)
                .setArrowSize(10)
                .setArrowOrientation(ArrowOrientation.BOTTOM)
                .setArrowVisible(true)
                .setWidthRatio(1.0f)
                .setHeight(65)
                .setTextSize(15f)
                .setArrowPosition(0.62f)
                .setCornerRadius(4f)
                .setAlpha(0.9f)
                .setText("Click Here to add/remove documents titles")
                .setTextColor(ContextCompat.getColor(this, R.color.white))
                .setIconDrawable(ContextCompat.getDrawable(this, R.drawable.ic_baseline_info_24))
                .setBackgroundColor(ContextCompat.getColor(this, R.color.colorPrimaryDark))
                .setBalloonAnimation(BalloonAnimation.OVERSHOOT)
                .setAutoDismissDuration(3500L)
                .setLifecycleOwner(lifecycleOwner)
                .build();


//        balloon.setOnBalloonOutsideTouchListener(new OnBalloonOutsideTouchListener() {
//            @Override
//            public void onBalloonOutsideTouch(@NotNull View view, @NotNull MotionEvent motionEvent) {
//                balloon.dismiss();
//            }
//        });

        balloon.setOnBalloonClickListener(new OnBalloonClickListener() {
            @Override
            public void onBalloonClick(View view) {
                startActivity(new Intent(SeafarerSetting.this, AddDocumentTitle.class));
                balloon.dismiss();
            }

        });

        img_btn_document_in_rank.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                balloon.showAlignTop(img_btn_document_in_rank, 20, 10);
            }
        });


        // Clearing older images from cache directory
        // don't call this line if you want to choose multiple images in the same activity
        // call this once the bitmap(s) usage is over
//        ImagePickerActivity.clearCache(this);
    }

    private void loadProfile(String url) {
        Log.d(TAG, "Image cache path: " + url);
        profileImageUri = url;
        sharedPrefImage = this.getSharedPreferences(profileImage, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPrefImage.edit();
        editor.putString(ImageUri, profileImageUri);
        editor.apply();
        GlideApp.with(this).load(url)
                .into(imgProfile);
        imgProfile.setColorFilter(ContextCompat.getColor(this, android.R.color.transparent));
    }

    private void loadProfileDefault() {
        GlideApp.with(this).load(R.drawable.baseline_account_circle_black_48)
                .into(imgProfile);
        imgProfile.setColorFilter(ContextCompat.getColor(this, R.color.profile_default_tint));
    }


    // Load profile image from a file
    private void loadPreviousProfileImage() {


        GlideApp.with(this)
                .load(profileImageUri)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .into(imgProfile);

    }

//    private Uri getCacheImagePath(String fileName) {
//        File path = new File(getExternalCacheDir(), "camera");
//        if (!path.exists()) path.mkdirs();
//        File image = new File(path, fileName);
//        return getUriForFile(SeafarerSetting.this, getPackageName() + ".provider", image);
//    }

    @OnClick({R.id.img_profile})
    void onProfileImageClick() {
        Dexter.withActivity(this)
                .withPermissions(
//                        Manifest.permission.CAMERA,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        if (report.areAllPermissionsGranted()) {
                            showImagePickerOptions();
                        }

                        if (report.isAnyPermissionPermanentlyDenied()) {
                            showSettingsDialog();
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).check();
    }

    private void showImagePickerOptions() {
        ImagePickerActivity.showImagePickerOptions(this, new ImagePickerActivity.PickerOptionListener() {
            @Override
            public void onTakeCameraSelected() {
                launchCameraIntent();
            }

            @Override
            public void onChooseGallerySelected() {
                launchGalleryIntent();
            }
        });
    }

    private void launchCameraIntent() {
        Intent intent = new Intent(SeafarerSetting.this, ImagePickerActivity.class);
        intent.putExtra(ImagePickerActivity.INTENT_IMAGE_PICKER_OPTION, ImagePickerActivity.REQUEST_IMAGE_CAPTURE);

        // setting aspect ratio
        intent.putExtra(ImagePickerActivity.INTENT_LOCK_ASPECT_RATIO, true);
        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_X, 1); // 16x9, 1x1, 3:4, 3:2
        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_Y, 1);

        // setting maximum bitmap width and height
        intent.putExtra(ImagePickerActivity.INTENT_SET_BITMAP_MAX_WIDTH_HEIGHT, true);
        intent.putExtra(ImagePickerActivity.INTENT_BITMAP_MAX_WIDTH, 1000);
        intent.putExtra(ImagePickerActivity.INTENT_BITMAP_MAX_HEIGHT, 1000);

        startActivityForResult(intent, REQUEST_IMAGE);
    }

    private void launchGalleryIntent() {
        Intent intent = new Intent(SeafarerSetting.this, ImagePickerActivity.class);
        intent.putExtra(ImagePickerActivity.INTENT_IMAGE_PICKER_OPTION, ImagePickerActivity.REQUEST_GALLERY_IMAGE);

        // setting aspect ratio
        intent.putExtra(ImagePickerActivity.INTENT_LOCK_ASPECT_RATIO, true);
        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_X, 1); // 16x9, 1x1, 3:4, 3:2
        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_Y, 1);
        startActivityForResult(intent, REQUEST_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE) {
            if (resultCode == Activity.RESULT_OK) {
                Uri uri = data.getParcelableExtra("path");
                try {
                    // You can update this bitmap to your server
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);

                    // loading profile image from local cache
                    loadProfile(uri.toString());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Showing Alert Dialog with Settings option
     * Navigates user to app settings
     * NOTE: Keep proper title and message depending on your app
     */
    private void showSettingsDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(SeafarerSetting.this);
        builder.setTitle(getString(R.string.dialog_permission_title));
        builder.setMessage(getString(R.string.dialog_permission_message));
        builder.setPositiveButton(getString(R.string.go_to_settings), (dialog, which) -> {
            dialog.cancel();
            openSettings();
        });
        builder.setNegativeButton(getString(android.R.string.cancel), (dialog, which) -> dialog.cancel());
        builder.show();

    }

    // navigating user to app settings
    private void openSettings() {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", getPackageName(), null);
        intent.setData(uri);
        startActivityForResult(intent, 101);
    }

    public void loadListData() {

        DataBaseHelper db = new DataBaseHelper(getApplicationContext());

        title = db.getAllDocTitle();
        String m = "";
        List<String> validity = db.getAllDocValidity();
        List<String> merg = new ArrayList<>();
        for (int i = 0; i < title.size(); i++) {

            if (Integer.parseInt(validity.get(i)) <= 1) {
                m = title.get(i) + " (" + validity.get(i) + ") Month";
            } else if (Integer.parseInt(validity.get(i)) > 1) {
                m = title.get(i) + " (" + validity.get(i) + ") Months";
            }
            merg.add(m);

        }

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, merg);


        setting_list_doc_title.setAdapter(dataAdapter);

    }
}
