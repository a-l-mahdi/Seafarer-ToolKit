package com.marine.seafarertoolkit;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import com.marine.seafarertoolkit.showdoc.ShowInfo;

import java.util.ArrayList;
import java.util.List;


public class DataBaseHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "seafarer.db";
    public static final String TABLE_NAME = "document";

    public static final String TABLE_DOC_TITLE = "doc_title";

    public static final String COL1 = "ID";
    public static final String COL2 = "TITLE";
    public static final String COL3 = "ISSUE_DATE";
    public static final String COL4 = "EXPIRY_DATE";
    public static final String COL5 = "DIFF_DAYS";
    public static final String COL6 = "VALID_DATE";
    public static final String COL7 = "NOTIFICATION_TIMES";

    public static final String KEY_ID = "ID";
    public static final String KEY_TITLE = "DOC_TITLE";
    public static final String KEY_VALIDATION = "DOC_VALIDATION";
    public static final String KEY_LOCK = "DOC_LOCK";

    Context context;


    public DataBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 2);
        this.context = context;
        SQLiteDatabase db = this.getWritableDatabase();
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_NAME + "(ID INTEGER PRIMARY KEY AUTOINCREMENT,TITLE TEXT,ISSUE_DATE TEXT,EXPIRY_DATE TEXT,DIFF_DAYS INTEGER,VALID_DATE TEXT,NOTIFICATION_TIMES TEXT)");
        db.execSQL("create table " + TABLE_DOC_TITLE + "(ID INTEGER PRIMARY KEY AUTOINCREMENT,DOC_TITLE TEXT,DOC_VALIDATION INTEGER,DOC_LOCK INTEGER)");
//        db.close();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_DOC_TITLE);
        onCreate(db);
//        db.close();
    }


    public boolean insertData(String title, String issue, String expiry, int diff_days, String doc_validation, String notification_times) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(COL2, title);
        contentValues.put(COL3, issue);
        contentValues.put(COL4, expiry);
        contentValues.put(COL5, diff_days);
        contentValues.put(COL6, doc_validation);
        contentValues.put(COL7, notification_times);

        long Result = db.insert(TABLE_NAME, null, contentValues);
//        db.close();

        return Result != -1;
    }


    public boolean insertDocTitleData(String doc_title, int doc_validation, int doc_lock) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();


        contentValues.put(KEY_TITLE, doc_title);
        contentValues.put(KEY_VALIDATION, doc_validation);
        contentValues.put(KEY_LOCK, doc_lock);

        long Result = db.insert(TABLE_DOC_TITLE, null, contentValues);
//        db.close();

        return Result != -1;
    }


    public List<String> getAllDocTitle() {

        List<String> doc_title = new ArrayList<String>();

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_DOC_TITLE, null);
        if (cursor.moveToFirst()) {

            do {
                doc_title.add(cursor.getString(1));
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return doc_title;
    }

    public List<String> getAllDocValidity() {

        List<String> doc_validity = new ArrayList<String>();

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_DOC_TITLE, null);
        if (cursor.moveToFirst()) {

            do {
                doc_validity.add(cursor.getString(2));
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return doc_validity;
    }


    public Cursor getAllData() {

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM " + TABLE_NAME, null);

//        res.close();
//        db.close();

        return res;
    }

    public Integer deleteData(String table, String id) {

        SQLiteDatabase db = this.getWritableDatabase();
        int result = db.delete(table, "ID=?", new String[]{id});
//        db.close();
        return result;

    }


    public Integer deleteDataForDocTitle(String table, String title) {

        SQLiteDatabase db = this.getWritableDatabase();
//        String query = "SELECT rowid, * FROM " + table + " WHERE DOC_TITLE = '" + title + "'";

        int result = db.delete(table, "DOC_TITLE=?", new String[]{title});
//        db.close();

        return result;
    }

    public List validationDate(String title) {

        List<String> lables = new ArrayList<String>();
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor resualt = db.query(TABLE_DOC_TITLE, new String[]{"DOC_VALIDATION"}, "DOC_TITLE=?", new String[]{title}, null, null, null);

        try {
            if (resualt != null && resualt.moveToFirst()) {
                do {
                    lables.add(resualt.getString(0));
                } while (resualt.moveToNext());
            }
        } finally {
            if (resualt != null) {
                resualt.close();
            }
            if (db != null) {
                db.close();
            }
        }


        return lables;


    }

    public List<String> getIdForNotification() {

        List<String> id = new ArrayList<String>();

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME, null);
        if (cursor.moveToFirst()) {

            do {
                id.add(cursor.getString(0));
            } while (cursor.moveToNext());
        }
//
//        cursor.close();
//        db.close();
        return id;
    }

    public List<String> getValidationDateForNotification() {

        List<String> valid_date = new ArrayList<String>();

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME, null);
        if (cursor.moveToFirst()) {

            do {
                valid_date.add(cursor.getString(5));
            } while (cursor.moveToNext());
        }
//
//        cursor.close();
//        db.close();
        return valid_date;
    }

    public List<String> getTitleForValidationDateForNotification() {

        List<String> valid_date = new ArrayList<String>();

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME, null);
        if (cursor.moveToFirst()) {

            do {
                valid_date.add(cursor.getString(1));
            } while (cursor.moveToNext());
        }
//
//        cursor.close();
//        db.close();
        return valid_date;
    }

    public List<String> getNotificationTimes() {

        List<String> notification_times = new ArrayList<String>();

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME, null);
        if (cursor.moveToFirst()) {

            do {
                notification_times.add(cursor.getString(6));
            } while (cursor.moveToNext());
        }
//
//        cursor.close();
//        db.close();
        return notification_times;
    }

    public List getLockRow(String title) {

        List<String> lables = new ArrayList<String>();
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor resualt = db.query(TABLE_DOC_TITLE, new String[]{"DOC_LOCK"}, "DOC_TITLE=?", new String[]{title}, null, null, null);

        try {
            if (resualt != null && resualt.moveToFirst()) {
                do {
                    lables.add(resualt.getString(0));
                } while (resualt.moveToNext());
            }
        } finally {
            if (resualt != null) {
                resualt.close();
            }
            if (db != null) {
                db.close();
            }
        }


        return lables;
    }

    public void setNotificationTimes(String title, String notificationTime) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL7, notificationTime);
        db.update(TABLE_NAME, contentValues, "ID= ?", new String[]{title});
//        db.close();
    }

    public void setDefaultDoc() {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();


        contentValues.put(KEY_TITLE, "CDC");
        contentValues.put(KEY_VALIDATION, 6);
        contentValues.put(KEY_LOCK, 1);
        db.insert(TABLE_DOC_TITLE, null, contentValues);

        contentValues.put(KEY_TITLE, "PASSPORT");
        contentValues.put(KEY_VALIDATION, 10);
        contentValues.put(KEY_LOCK, 1);
        db.insert(TABLE_DOC_TITLE, null, contentValues);

        contentValues.put(KEY_TITLE, "COC");
        contentValues.put(KEY_VALIDATION, 6);
        contentValues.put(KEY_LOCK, 1);
        db.insert(TABLE_DOC_TITLE, null, contentValues);

        contentValues.put(KEY_TITLE, "MEDICAL FITNESS");
        contentValues.put(KEY_VALIDATION, 4);
        contentValues.put(KEY_LOCK, 1);
        db.insert(TABLE_DOC_TITLE, null, contentValues);


        contentValues.put(KEY_TITLE, "SURVIVAL CRAFT & RESCUE BOAT");
        contentValues.put(KEY_VALIDATION, 6);
        contentValues.put(KEY_LOCK, 1);
        db.insert(TABLE_DOC_TITLE, null, contentValues);


        contentValues.put(KEY_TITLE, "SECURITY AWARENESS");
        contentValues.put(KEY_VALIDATION, 6);
        contentValues.put(KEY_LOCK, 1);
        db.insert(TABLE_DOC_TITLE, null, contentValues);


        contentValues.put(KEY_TITLE, "MEDICAL FIRST AID=>500");
        contentValues.put(KEY_VALIDATION, 6);
        contentValues.put(KEY_LOCK, 1);
        db.insert(TABLE_DOC_TITLE, null, contentValues);


        contentValues.put(KEY_TITLE, "BASIC TRAINING FOR OIL & CHEM. TANKER");
        contentValues.put(KEY_VALIDATION, 6);
        contentValues.put(KEY_LOCK, 1);
        db.insert(TABLE_DOC_TITLE, null, contentValues);


        contentValues.put(KEY_TITLE, "BASIC SAFETY TRAINING=>500");
        contentValues.put(KEY_VALIDATION, 6);
        contentValues.put(KEY_LOCK, 1);
        db.insert(TABLE_DOC_TITLE, null, contentValues);


        contentValues.put(KEY_TITLE, "BASIC TRAINING FOR LIQUIFIED GAS TANKER");
        contentValues.put(KEY_VALIDATION, 6);
        contentValues.put(KEY_LOCK, 1);
        db.insert(TABLE_DOC_TITLE, null, contentValues);


        contentValues.put(KEY_TITLE, "ADVANCED FIRE FIGHTING");
        contentValues.put(KEY_VALIDATION, 6);
        contentValues.put(KEY_LOCK, 1);
        db.insert(TABLE_DOC_TITLE, null, contentValues);

//        db.close();

    }

}
