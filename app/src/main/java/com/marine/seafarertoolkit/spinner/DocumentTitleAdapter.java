package com.marine.seafarertoolkit.spinner;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.marine.seafarertoolkit.R;

import java.util.ArrayList;

public class DocumentTitleAdapter extends ArrayAdapter<DocumentTitleForSpinner> {
    private static final int CLOSE = 525;
    private static final int OPEN = 655;
    boolean isFirst;

    public DocumentTitleAdapter(Context context, ArrayList<DocumentTitleForSpinner> documentTitles, boolean isFirst) {
        super(context, 0, documentTitles);
        this.isFirst = isFirst;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return initView(position, convertView, parent, CLOSE);
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return initView(position, convertView, parent, OPEN);
    }


    private View initView(int position, View convertView, ViewGroup parent, int type) {
        if (convertView == null & type == CLOSE) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.spinner_item_close, parent, false);
        } else if (convertView == null & type == OPEN)
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.spinner_item_open, parent, false);


        final TextView textViewSpinner = convertView.findViewById(R.id.spinner_txt);

        textViewSpinner.postDelayed(new Runnable() {
            @Override
            public void run() {
                textViewSpinner.setSelected(true);
            }
        }, 2500);
        DocumentTitleForSpinner currentItem = getItem(position);

        if (currentItem != null)
            textViewSpinner.setText(currentItem.getmDocumentTitle());
//        if (currentItem!=null && !isFirst)
//            textViewSpinner.setText(currentItem.getmDocumentTitle());

        return convertView;
    }


}

