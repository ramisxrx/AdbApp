package com.example.adbapp;

import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase;
import android.content.Context;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "adb.db"; // название бд
    private static final int SCHEMA = 1; // версия базы данных
    static final String TABLE_TYPES = "type_clusters"; // название таблицы в бд
    static final String TABLE_NAMES = "name_clusters"; // название таблицы в бд
    static final String TABLE_FIELDS = "field_clusters"; // название таблицы в бд
    static final String TABLE_TIME = "time_clusters"; // название таблицы в бд
    static final String TABLE_MAIN = "main_clusters"; // название таблицы в бд
    static final String TABLE_OBJECTS = "list_objects"; // название таблицы в бд
    // названия столбцов
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_TYPE = "type_";
    public static final String COLUMN_TYPE_ID = "type_id";
    public static final String COLUMN_TIME = "time_";
    public static final String COLUMN_TIME_ID = "time_id";
    public static final String COLUMN_NAME = "name_";
    public static final String COLUMN_NAME_ID = "name_id";
    public static final String COLUMN_FIELD_ID = "field_id";
    public static final String COLUMN_PARENT_ID = "parent_id";
    public static final String COLUMN_OBJECT_ID = "object_id";
}
