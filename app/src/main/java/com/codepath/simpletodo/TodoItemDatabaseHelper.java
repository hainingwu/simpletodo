package com.codepath.simpletodo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

public class TodoItemDatabaseHelper extends SQLiteOpenHelper {
    private static TodoItemDatabaseHelper sInstance;

    // Database Info
    private static final String DATABASE_NAME = "TodoItemDatabase";
    private static final int DATABASE_VERSION = 1;

    // Table Names
    private static final String TABLE_TODO_ITEMS = "TodoItems";

    // todoItems Table Columns
    private static final String KEY_ITEM_ID = "id";
    private static final String KEY_ITEM_DESCRIPTION = "description";
    private static final String KEY_ITEM_DUE_DATE = "due_date";
    private static final String KEY_ITEM_CRITICAL = "critical";

    public static synchronized TodoItemDatabaseHelper getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new TodoItemDatabaseHelper(context.getApplicationContext());
        }
        return sInstance;
    }

    private TodoItemDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // SQL for creating the tables
        String CREATE_TODO_ITEMS_TABLE = "CREATE TABLE " + TABLE_TODO_ITEMS +
                "(" +
                    KEY_ITEM_ID + " INTEGER PRIMARY KEY," +
                    KEY_ITEM_DESCRIPTION + " TEXT, " +
                    KEY_ITEM_DUE_DATE + " TEXT, " +
                    KEY_ITEM_CRITICAL + " INTEGER" +
                ")";
        db.execSQL(CREATE_TODO_ITEMS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion != newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_TODO_ITEMS);
            onCreate(db);
        }
    }

    public long addTodoItem(TodoItem item) {
        SQLiteDatabase db = getWritableDatabase();
        long id = -1;

        db.beginTransaction();
        try {
            ContentValues values = new ContentValues();
            values.put(KEY_ITEM_DESCRIPTION, item.description);
            values.put(KEY_ITEM_DUE_DATE, item.dueDate);
            values.put(KEY_ITEM_CRITICAL, item.critical);
            id = db.insertOrThrow(TABLE_TODO_ITEMS, null, values);
            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.d("SimpleTodo", "Error while trying to add item to database");
        } finally {
            db.endTransaction();
        }
        return id;
    }

    public void deleteTodoItem(long id) {
        SQLiteDatabase db = getWritableDatabase();

        db.beginTransaction();
        try {
            db.delete(TABLE_TODO_ITEMS, KEY_ITEM_ID + "=" + Long.toString(id), null);
            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.d("SimpleTodo", "Error while trying to delete item to database");
        } finally {
            db.endTransaction();
        }
    }

    public void updateTodoItem(TodoItem item) {
        SQLiteDatabase db = getWritableDatabase();

        db.beginTransaction();
        try {
            ContentValues values = new ContentValues();
            values.put(KEY_ITEM_DESCRIPTION, item.description);
            values.put(KEY_ITEM_DUE_DATE, item.dueDate);
            values.put(KEY_ITEM_CRITICAL, item.critical);
            db.update(TABLE_TODO_ITEMS, values, KEY_ITEM_ID + "=" + Long.toString(item.id), null);
            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.d("SimpleTodo", "Error while trying to update item to database");
        } finally {
            db.endTransaction();
        }
    }

    public ArrayList<TodoItem> getAllTodoItems() {
        ArrayList<TodoItem> items = new ArrayList<>();

        String ITEMS_SELECT_QUERY =
                String.format("SELECT * FROM %s", TABLE_TODO_ITEMS);

        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(ITEMS_SELECT_QUERY, null);
        try {
            if (cursor.moveToFirst()) {
                do {
                    TodoItem newItem = new TodoItem();
                    newItem.id = cursor.getInt(cursor.getColumnIndex(KEY_ITEM_ID));
                    newItem.description = cursor.getString(cursor.getColumnIndex(KEY_ITEM_DESCRIPTION));
                    newItem.dueDate = cursor.getString(cursor.getColumnIndex(KEY_ITEM_DUE_DATE));
                    newItem.critical = cursor.getInt(cursor.getColumnIndex(KEY_ITEM_CRITICAL));
                    items.add(newItem);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.d("SimpleTodo", "Error while trying to read item to database");
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
        return items;
    }
}
