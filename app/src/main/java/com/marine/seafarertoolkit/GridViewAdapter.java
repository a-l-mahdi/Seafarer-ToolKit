package com.marine.seafarertoolkit;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;

public class GridViewAdapter extends BaseAdapter {

    public Integer[] img_gridView = {
            R.drawable.document_manager,
            R.drawable.seatimes,
            R.drawable.document_title,
            R.drawable.weather
    };

    Context context;

    public GridViewAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return img_gridView.length;
    }

    @Override
    public Object getItem(int position) {
        return img_gridView[position];
    }

    @Override
    public long getItemId(int position) {
        return img_gridView[position];
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ImageView imageView = new ImageView(context);
        imageView.setImageResource(img_gridView[position]);
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        imageView.setLayoutParams(new GridView.LayoutParams(512, 512));
        return imageView;
    }
}
