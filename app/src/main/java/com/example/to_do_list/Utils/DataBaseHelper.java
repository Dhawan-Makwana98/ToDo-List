package com.example.to_do_list.Utils;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.example.to_do_list.Model.ToDoModel;

import java.util.ArrayList;
import java.util.List;

public class DataBaseHelper extends SQLiteOpenHelper {

    // Database and table details
    public static final String DATABASE_NAME = "TODO_DATABASE";
    public static final String TABLE_NAME = "TODO_TABLE";
    public static final String COL_1 = "ID";
    public static final String COL_2 = "TASK";
    public static final String COL_3 = "STATUS";

    public DataBaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, 2); // Updated to version 2
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create table SQL query
        String createTableSQL = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " (" +
                COL_1 + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_2 + " TEXT, " +
                COL_3 + " INTEGER)";
        db.execSQL(createTableSQL); // Execute SQL query
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Handle database upgrade logic here
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Backup existing data before dropping the table
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME, null);
        List<ToDoModel> backupList = new ArrayList<>();

        if (cursor.moveToFirst()) {
            do {
                ToDoModel task = new ToDoModel();
                task.setId(cursor.getInt(cursor.getColumnIndexOrThrow(COL_1)));
                task.setTask(cursor.getString(cursor.getColumnIndexOrThrow(COL_2)));
                task.setStatus(cursor.getInt(cursor.getColumnIndexOrThrow(COL_3)));
                backupList.add(task);
            } while (cursor.moveToNext());
        }
        cursor.close();

        // Drop the table and recreate it
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);

        // Restore the backed-up data
        for (ToDoModel task : backupList) {
            ContentValues values = new ContentValues();
            values.put(COL_1, task.getId());
            values.put(COL_2, task.getTask());
            values.put(COL_3, task.getStatus());
            db.insert(TABLE_NAME, null, values);
        }
    }

    public void insertTask(ToDoModel model) {
        try (SQLiteDatabase db = this.getWritableDatabase()) {
            ContentValues values = new ContentValues();
            values.put(COL_2, model.getTask());
            values.put(COL_3, 0); // Default status for a new task
            db.insert(TABLE_NAME, null, values);
        }
    }

    public void updateTask(int id, String task) {
        try (SQLiteDatabase db = this.getWritableDatabase()) {
            ContentValues values = new ContentValues();
            values.put(COL_2, task);
            db.update(TABLE_NAME, values, COL_1 + "=?", new String[]{String.valueOf(id)});
        }
    }

    public void updateStatus(int id, int status) {
        try (SQLiteDatabase db = this.getWritableDatabase()) {
            ContentValues values = new ContentValues();
            values.put(COL_3, status);
            db.update(TABLE_NAME, values, COL_1 + "=?", new String[]{String.valueOf(id)});
        }
    }

    public void deleteTask(int id) {
        try (SQLiteDatabase db = this.getWritableDatabase()) {
            db.delete(TABLE_NAME, COL_1 + "=?", new String[]{String.valueOf(id)});
        }
    }

    @SuppressLint("Range")
    public List<ToDoModel> getAllTask() {
        List<ToDoModel> modelList = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_NAME;
        try (SQLiteDatabase db = this.getReadableDatabase();
             Cursor cursor = db.rawQuery(selectQuery, null)) {
            if (cursor.moveToFirst()) {
                do {
                    ToDoModel task = new ToDoModel();
                    task.setId(cursor.getInt(cursor.getColumnIndexOrThrow(COL_1)));
                    task.setTask(cursor.getString(cursor.getColumnIndexOrThrow(COL_2)));
                    task.setStatus(cursor.getInt(cursor.getColumnIndexOrThrow(COL_3)));
                    modelList.add(task);
                } while (cursor.moveToNext());
            }
        }
        return modelList;
    }
}
