package com.example.adbapp;

import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase;
import android.content.Context;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "adb.db"; // название бд
    private static final int SCHEMA = 1; // версия базы данных
    static final String TABLE_NAMES = "name_clusters"; // название таблицы в бд
    static final String TABLE_FIELDS = "field_clusters"; // название таблицы в бд
    static final String TABLE_MAIN = "main_clusters"; // название таблицы в бд
    static final String TABLE_OBJECTS = "list_objects"; // название таблицы в бд
    // названия столбцов
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_TYPE = "type_";
    public static final String COLUMN_TIME = "time_";
    public static final String COLUMN_NAME = "name_";
    public static final String COLUMN_NAME_ID = "name_id";
    public static final String COLUMN_FIELD_ID = "field_id";
    public static final String COLUMN_PARENT_ID = "parent_id";
    public static final String COLUMN_OBJECT_ID = "object_id";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, SCHEMA);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("CREATE TABLE " + TABLE_NAMES +
                " (" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT UNIQUE NOT NULL, "
                + COLUMN_NAME + " TEXT);");

        db.execSQL("CREATE TABLE " + TABLE_FIELDS +
                " (" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT UNIQUE NOT NULL, "
                + COLUMN_TYPE + " INTEGER, "
                + COLUMN_NAME_ID + " INTEGER NOT NULL, FOREIGN KEY ("
                + COLUMN_NAME_ID + ") REFERENCES " + TABLE_NAMES + "(" + COLUMN_ID + "));");

        db.execSQL("CREATE TABLE " + TABLE_OBJECTS +
                " (" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT UNIQUE NOT NULL);");

        db.execSQL("CREATE TABLE " + TABLE_MAIN +
                " (" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT UNIQUE NOT NULL, "
                + COLUMN_OBJECT_ID + " INTEGER NOT NULL, "
                + COLUMN_PARENT_ID + " INTEGER, "
                + COLUMN_FIELD_ID + " INTEGER NOT NULL,"
                + COLUMN_TIME + " INTEGER, FOREIGN KEY ("
                + COLUMN_OBJECT_ID + ") REFERENCES " + TABLE_OBJECTS + "(" + COLUMN_ID + "), FOREIGN KEY ("
                + COLUMN_FIELD_ID + ") REFERENCES " + TABLE_FIELDS + "(" + COLUMN_ID + "));");

        // добавление начальных данных
        db.execSQL("INSERT INTO "+ TABLE_NAMES +" (" + COLUMN_NAME + ")" +
                   "VALUES ('Друг')," +
                          "('Валеев')," +
                          "('Азат');");

        db.execSQL("INSERT INTO "+ TABLE_FIELDS +" (" + COLUMN_TYPE + "," + COLUMN_NAME_ID + ")" +
                "VALUES (0,1)," +
                       "(0,2)," +
                       "(0,3);");

        db.execSQL("INSERT INTO "+ TABLE_OBJECTS +" (" + COLUMN_ID + ")" +
                "VALUES (1);");

        db.execSQL("INSERT INTO "+ TABLE_MAIN +" (" + COLUMN_OBJECT_ID + "," + COLUMN_PARENT_ID + "," + COLUMN_FIELD_ID +")" +
                "VALUES (1,0,1)," +
                       "(1,1,2)," +
                       "(1,1,3);");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion,  int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_NAMES);
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_FIELDS);
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_MAIN);
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_OBJECTS);
        onCreate(db);
    }

}
