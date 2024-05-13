package com.example.timetracker;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "YourDatabaseName.db";
    private static final int DATABASE_VERSION = 1;

    // Table name and column names
    private static final String TABLE_NAME = "tasks";
    private static final String COLUMN_ID = "_id";
    private static final String COLUMN_TYPE = "type";
    private static final String COLUMN_NAME = "name";
    private static final String COLUMN_START_TIME = "start_time";
    private static final String COLUMN_END_TIME = "end_time";
    private static final String COLUMN_DIFFICULTY_COLOR = "difficulty_color";

    // Create table SQL query
    private static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    COLUMN_TYPE + " TEXT," +
                    COLUMN_NAME + " TEXT," +
                    COLUMN_START_TIME + " TEXT," +
                    COLUMN_END_TIME + " TEXT," +
                    COLUMN_DIFFICULTY_COLOR + " TEXT)";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public long insertTask(TaskData task) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_TYPE, task.getTypeActivitySelected());
        values.put(COLUMN_NAME, task.getTaskName());
        values.put(COLUMN_START_TIME, task.getSelectedStartTime());
        values.put(COLUMN_END_TIME, task.getSelectedEndTime());
        values.put(COLUMN_DIFFICULTY_COLOR, task.getDifficultyColor());

        long newRowId = db.insert(TABLE_NAME, null, values);
        db.close();
        return newRowId;
    }
}
