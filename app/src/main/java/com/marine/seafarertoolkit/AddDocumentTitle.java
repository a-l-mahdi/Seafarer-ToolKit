package com.marine.seafarertoolkit;

import android.app.ListActivity;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

public class AddDocumentTitle extends AppCompatActivity {


    Button btn_add_doc_title;
    EditText edt_add_title;
    EditText doc_validation;
    ListView list_doc_title;

    Integer deleteRow;

    List<String> title = new ArrayList<String>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_document_title);

        btn_add_doc_title = (Button) findViewById(R.id.add_doc_title_to_list);
        edt_add_title = (EditText) findViewById(R.id.title_for_add);
        doc_validation = (EditText) findViewById(R.id.validation);
        list_doc_title = findViewById(R.id.list_title);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        loadListData();

//        edt_add_title.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//
//                if(s.toString().equals("")){
//                    btn_add_doc_title.setEnabled(false);
//                }else {
//                    btn_add_doc_title.setEnabled(false);
//                }
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//
//            }
//        });
//
//
//        doc_validation.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//
//                if(s.toString().equals("")){
//                    btn_add_doc_title.setEnabled(false);
//                }else {
//                    btn_add_doc_title.setEnabled(false);
//                }
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//
//            }
//        });
//


        list_doc_title.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {


                DataBaseHelper myDb = new DataBaseHelper(getApplicationContext());

//                String delTitle = (String) ((TextView)view).getText() + " That Is Valid For ";


                List<String> list = myDb.getLockRow(title.get(position));
                int doc_lock = Integer.parseInt(list.get(0));

//                Toast.makeText(getApplicationContext(), doc_lock +"", Toast.LENGTH_SHORT).show();


                if (doc_lock == 0) {
                    deleteRow = myDb.deleteDataForDocTitle("doc_title", title.get(position));

                    if (deleteRow > 0) {
                        Toast.makeText(getApplicationContext(), "Title Deleted", Toast.LENGTH_SHORT).show();
                    } else if (deleteRow == 0) {
                        Toast.makeText(getApplicationContext(), "Title Not Deleted", Toast.LENGTH_SHORT).show();
                    }

                } else if (doc_lock == 1) {
                    Toast.makeText(getApplicationContext(), "This Title Is Default, You Can't Delete It", Toast.LENGTH_SHORT).show();


                }


                loadListData();

                return false;
            }
        });


        btn_add_doc_title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = edt_add_title.getText().toString().toUpperCase();
                String doc_valid = doc_validation.getText().toString().toUpperCase();


                if (title.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Please Insert YR Document Title",
                            Toast.LENGTH_SHORT).show();

                } else if (doc_valid.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Please Enter Validation Is How Many Month",
                            Toast.LENGTH_SHORT).show();

                } else {
                    DataBaseHelper db = new DataBaseHelper(getApplicationContext());

                    db.insertDocTitleData(title, Integer.parseInt(doc_valid), 0);
                    edt_add_title.setText("");
                    doc_validation.setText("");
                    loadListData();

//                    // Hiding the keyboard
//                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//                    imm.hideSoftInputFromWindow(edt_add_title.getWindowToken(), 0);


                }
            }
        });
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

        list_doc_title.setAdapter(dataAdapter);

    }

}
