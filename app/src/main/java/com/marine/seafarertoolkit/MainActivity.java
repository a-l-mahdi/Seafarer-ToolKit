package com.marine.seafarertoolkit;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.marine.seafarertoolkit.showdoc.ShowInfo;

public class MainActivity extends AppCompatActivity {

    private GridView main_gridView;
    private GridViewAdapter gridViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);

        main_gridView = findViewById(R.id.main_gridView);

        gridViewAdapter = new GridViewAdapter(MainActivity.this);
        main_gridView.setAdapter(gridViewAdapter);

        main_gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(MainActivity.this, position + "", Toast.LENGTH_SHORT).show();

                switch (position) {

                    case 0:
                        startActivity(new Intent(MainActivity.this, ShowInfo.class));
                        break;
                    case 1:
                        startActivity(new Intent(MainActivity.this, SeatimeActivity.class));
                        break;
                    case 2:
                        startActivity(new Intent(MainActivity.this, AddDocumentTitle.class));
                        break;
                    case 3:
                        startActivity(new Intent(MainActivity.this, Weather.class));
                        break;
                }
            }
        });
    }
}
