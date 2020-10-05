package com.marine.seafarertoolkit.showdoc;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.marine.seafarertoolkit.AboutActivity;
import com.marine.seafarertoolkit.AddDocument;
import com.marine.seafarertoolkit.AddDocumentTitle;
import com.marine.seafarertoolkit.DataBaseHelper;
import com.marine.seafarertoolkit.R;
import com.marine.seafarertoolkit.SeafarerSetting;
import com.marine.seafarertoolkit.profilepic.GlideApp;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class ShowInfo extends AppCompatActivity {


    RecyclerView recyclerView;
    List<DataInfo> dataInfoList = new ArrayList<>();
    DataBaseHelper myDb;
    RecyclerAdapter adapter;
    SwipeRefreshLayout swipeRefreshLayout;
    FloatingActionButton fab;
    Button delete;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    ImageView hamburger;
    CollapsingToolbarLayout myToolbar;
    boolean addOrNot = false;
    SharedPreferences sharedPref;
    SharedPreferences sharedPrefName;
    public static final String profileImage = "MyProfileImage";
    public static final String profileSetting = "MyProfileSetting";
    public static final String ImageUri = "ImageUri";
    public static final String TxtRank = "TxtRank";
    public static final String TxtName = "TxtName";
    public static final String TxtLastName = "TxtLastName";
    String profileImageUri = "";
    ImageView profileImageView;
    TextView navigation_txt_rank, navigation_txt_name;


    @Override
    protected void onPostResume() {
        if (addOrNot) {
            refreshLayuot();
            addOrNot = false;
        }
        profilePicture();
        loadProfileRankName();
        super.onPostResume();
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        supportRequestWindowFeature(Window.FEATURE_ACTION_BAR_OVERLAY);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.navigation_drawer);
        myDb = new DataBaseHelper(this);

        recyclerView = findViewById(R.id.recyclerview);
        fab = findViewById(R.id.fab);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        adapter = new RecyclerAdapter(this, dataInfoList);

        recyclerView.setAdapter(adapter);

//        recyclerView.setNestedScrollingEnabled(true);
        swipeRefreshLayout = findViewById(R.id.swipe_layout);
        drawerLayout = findViewById(R.id.navigation_drawer_drawer);
        navigationView = findViewById(R.id.navigation_view);
        myToolbar = findViewById(R.id.myToolbar);
        hamburger = findViewById(R.id.hamburger);
        View view = navigationView.inflateHeaderView(R.layout.navigation_header);
        profileImageView = view.findViewById(R.id.img_profile_navigation);
        navigation_txt_rank = view.findViewById(R.id.navigation_txt_rank);
        navigation_txt_name = view.findViewById(R.id.navigation_txt_name);


//       getWindow().setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION,WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
//       getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));


        Cursor res = myDb.getAllData();

        if (res.getCount() == 0) {

            Toast.makeText(this, "No Document...!", Toast.LENGTH_SHORT).show();
        }

        while (res.moveToNext()) {
            DataInfo dataInfo = new DataInfo();
            dataInfo.id = res.getString(0);
            dataInfo.title = res.getString(1);
            dataInfo.issue = res.getString(2);
            dataInfo.expiry = res.getString(3);
            dataInfo.diffDays = res.getInt(4);
            dataInfo.validationDate = res.getString(5);

            dataInfoList.add(dataInfo);
        }


        profilePicture();
        loadProfileRankName();


//        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(ShowInfo.this,
//                drawerLayout,
//                (CollapsingToolbarLayout) myToolbar,
//                R.string.open,R.string.close){
//
//            @Override
//            public void onDrawerOpened(View drawerView) {
//                super.onDrawerOpened(drawerView);
//
//                Toast.makeText(ShowInfo.this,"Open",Toast.LENGTH_SHORT).show();
//            }
//
//            @Override
//            public void onDrawerClosed(View drawerView) {
//                super.onDrawerClosed(drawerView);
//                Toast.makeText(ShowInfo.this,"close",Toast.LENGTH_SHORT).show();
//            }
//        };


        hamburger.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(Gravity.LEFT);

            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ShowInfo.this, AddDocument.class));
                addOrNot = true;
            }
        });


        fab.clearColorFilter();

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshLayuot();
                swipeRefreshLayout.setRefreshing(false);
            }
        });

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                int item = menuItem.getItemId();
                if (item == R.id.add_doc_title)
                    startActivity(new Intent(ShowInfo.this, AddDocumentTitle.class));
                if (item == R.id.add_doc)
                    startActivity(new Intent(ShowInfo.this, AddDocument.class));
                if (item == R.id.about)
                    startActivity(new Intent(ShowInfo.this, AboutActivity.class));
                if (item == R.id.home) drawerLayout.closeDrawer(Gravity.LEFT);
                if (item == R.id.setting)
                    startActivity(new Intent(ShowInfo.this, SeafarerSetting.class));
                drawerLayout.closeDrawer(Gravity.LEFT);
                return true;
            }
        });

//        delete_icon.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                refreshLayuot();
//            }
//        });
    }

    public void refreshLayuot() {

        dataInfoList.clear();


        Cursor res = myDb.getAllData();

        if (res.getCount() == 0) {

            Toast.makeText(this, "No Information...!", Toast.LENGTH_SHORT).show();
        }

        while (res.moveToNext()) {
            DataInfo dataInfo = new DataInfo();
            dataInfo.id = res.getString(0);
            dataInfo.title = res.getString(1);
            dataInfo.issue = res.getString(2);
            dataInfo.expiry = res.getString(3);
            dataInfo.diffDays = res.getInt(4);
            dataInfo.validationDate = res.getString(5);

            dataInfoList.add(dataInfo);
        }


        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        adapter = new RecyclerAdapter(this, dataInfoList);

        recyclerView.setAdapter(adapter);

    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(Gravity.LEFT)) {
            drawerLayout.closeDrawer(Gravity.LEFT);
        } else {
            super.onBackPressed();
        }
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.navigation_item,menu);
//        return super.onCreateOptionsMenu(menu);
//    }

    public void profilePicture() {

        loadSharedPreference();
//        loadProfileDefault();
        if (!profileImageUri.equals("")) loadPreviousProfileImage();
        else loadProfileDefault();

        profileImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ShowInfo.this, SeafarerSetting.class));
                drawerLayout.closeDrawer(Gravity.LEFT);
            }
        });

    }


    public void loadSharedPreference() {
        sharedPref = this.getSharedPreferences(profileImage, Context.MODE_PRIVATE);

        profileImageUri = sharedPref.getString(ImageUri, "");
    }


    private void loadPreviousProfileImage() {

        loadSharedPreference();

        Log.d("prev", "Image cache path: " + profileImageUri + "");

        profileImageView.clearColorFilter();

        GlideApp.with(ShowInfo.this)
                .load(profileImageUri)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .into(profileImageView);

    }

    private void loadProfileDefault() {
        Log.d("def", "Image cache path: " + profileImageUri + "");
        GlideApp.with(this).load(R.drawable.baseline_account_circle_black_48)
                .into(profileImageView);
        profileImageView.setColorFilter(ContextCompat.getColor(this, R.color.profile_default_tint));
    }

    private void loadProfileRankName() {
        sharedPrefName = this.getSharedPreferences(profileSetting, Context.MODE_PRIVATE);
        navigation_txt_rank.setText(sharedPrefName.getString(TxtRank, "Rank"));
        navigation_txt_name.setText(sharedPrefName.getString(TxtName, "Your Name").substring(0, 1) + "." + sharedPrefName.getString(TxtLastName, ""));

    }


}


