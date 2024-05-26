package com.example.todoapptest.auth;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.todoapptest.todo.ToDoItem;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "todoapp.db3";
    private static final int DATABASE_VERSION = 1;

    // User table
    private static final String TABLE_USERS = "users";
    private static final String COL_USER_ID = "user_id";
    private static final String COL_USERNAME = "username";
    private static final String COL_PASSWORD = "password";

    // Todo table
    private static final String TABLE_TODO = "todo";
    private static final String COL_TODO_ID = "todo_id";
    private static final String COL_TODO_USER_ID = "user_id";
    private static final String COL_TITLE = "title";
    private static final String COL_DESCRIPTION = "description";
    private static final String COL_DUE_DATE = "due_date";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create user table
        String createUserTableQuery = "CREATE TABLE " + TABLE_USERS + " (" +
                COL_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                COL_USERNAME + " TEXT UNIQUE," +
                COL_PASSWORD + " TEXT)";
        db.execSQL(createUserTableQuery);

        // Create todo table
        String createTodoTableQuery = "CREATE TABLE " + TABLE_TODO + " (" +
                COL_TODO_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                COL_TODO_USER_ID + " INTEGER," +
                COL_TITLE + " TEXT," +
                COL_DESCRIPTION + " TEXT," +
                COL_DUE_DATE + " TEXT," +
                "FOREIGN KEY(" + COL_TODO_USER_ID + ") REFERENCES " + TABLE_USERS + "(" + COL_USER_ID + "))";
        db.execSQL(createTodoTableQuery);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TODO);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        onCreate(db);
    }

    // Insert a new user
    public boolean insertUser(String username, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_USERNAME, username);
        values.put(COL_PASSWORD, password);
        long id = db.insert(TABLE_USERS, null, values);
        db.close();
        return id != -1;
    }

    // Insert a new todo item for a specific user
    public long insertTodoItem(int userId, String title, String description, String dueDate) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_TODO_USER_ID, userId);
        values.put(COL_TITLE, title);
        values.put(COL_DESCRIPTION, description);
        values.put(COL_DUE_DATE, dueDate);
        long id = db.insert(TABLE_TODO, null, values);
        db.close();
        return id;
    }

    // Get all todo items for a specific user
    public ArrayList<String> getAllTodoItems(int userId) {
        ArrayList<String> todoItems = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_TODO, new String[]{COL_TITLE, COL_DESCRIPTION, COL_DUE_DATE},
                COL_TODO_USER_ID + "=?", new String[]{String.valueOf(userId)}, null, null, null);

        if (cursor.moveToFirst()) {
            int columnIndexTitle = cursor.getColumnIndex(COL_TITLE);
            int columnIndexDescription = cursor.getColumnIndex(COL_DESCRIPTION);
            int columnIndexDueDate = cursor.getColumnIndex(COL_DUE_DATE);

            // Check if the columns exist in the result set
            if (columnIndexTitle != -1 && columnIndexDescription != -1 && columnIndexDueDate != -1) {
                do {
                    // Retrieve todo item details
                    String title = cursor.getString(columnIndexTitle);
                    String description = cursor.getString(columnIndexDescription);
                    String dueDate = cursor.getString(columnIndexDueDate);
                    todoItems.add(title + " - " + description + " (Due: " + dueDate + ")");
                } while (cursor.moveToNext());
            }

        }


        if (cursor != null) {
            cursor.close();
        }
        db.close();
        return todoItems;
    }

    // Check if a user exists with the given username and password
    public boolean checkUser(String username, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_USERS + " WHERE " + COL_USERNAME + " = ? AND " + COL_PASSWORD + " = ?", new String[]{username, password});
        boolean exists = cursor.getCount() > 0;
        cursor.close();
        db.close();
        return exists;
    }

    public int getUserIdByUsername(String username) {
        SQLiteDatabase db = this.getReadableDatabase();
        int userId = -1; // Default value if user ID is not found
        Cursor cursor = db.query(TABLE_USERS, new String[]{COL_USER_ID}, COL_USERNAME + "=?", new String[]{username}, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            // Check if the column index is valid (not -1)
            int columnIndex = cursor.getColumnIndex(COL_USER_ID);
            if (columnIndex != -1) {
                // Retrieve the user ID
                userId = cursor.getInt(columnIndex);
            }
            cursor.close(); // Close the cursor after retrieving the user ID
       }
        db.close();
        return userId;
    }

    // Get todo item ID by title and user ID
    public int getTodoItemIdByTitle(int userId, String title) {
        int todoItemId = -1;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_TODO, new String[]{COL_TODO_ID}, COL_TODO_USER_ID + "=? AND " + COL_TITLE + "=?", new String[]{String.valueOf(userId), title}, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            int columnIndex = cursor.getColumnIndex(COL_TODO_ID);
            if (columnIndex >= 0) {
                todoItemId = cursor.getInt(columnIndex);
            }
            cursor.close();
        }
        db.close();
        return todoItemId;
    }


    // Delete a todo item
    public void deleteTodoItem(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_TODO, COL_TODO_ID + " = ?",
                new String[]{String.valueOf(id)});
        db.close();
    }


    public ToDoItem getToDoItem(String title) {
        SQLiteDatabase db = this.getReadableDatabase();
        String selection = COL_TITLE + " = ?";
        String[] selectionArgs = {title};

        Cursor cursor = db.query(TABLE_TODO, null, selection, selectionArgs, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            for (String columnName : cursor.getColumnNames()) {
                Log.d("DatabaseHelper", "Column: " + columnName);
            }

            // Get indices of the columns
            int userIdIndex = cursor.getColumnIndex(COL_USER_ID);
            int descriptionIndex = cursor.getColumnIndex(COL_DESCRIPTION);
            int dueDateIndex = cursor.getColumnIndex(COL_DUE_DATE);

            // Check if column indices are valid
            if (userIdIndex >= 0 && descriptionIndex >= 0 && dueDateIndex >= 0) {
                int userId = cursor.getInt(userIdIndex);
                String description = cursor.getString(descriptionIndex);
                String dueDate = cursor.getString(dueDateIndex);

                ToDoItem toDoItem = new ToDoItem(userId, title, description, dueDate);
                cursor.close();
                db.close();
                return toDoItem;
            } else {
                // Log an error if the columns are not found
                Log.e("DatabaseHelper", "Column not found: userIdIndex=" + userIdIndex + ", descriptionIndex=" + descriptionIndex + ", dueDateIndex=" + dueDateIndex);
            }
        }

        if (cursor != null) {
            cursor.close();
        }
        db.close();
        return null;
    }

    public boolean updateTodoItem(int id, String title, String description, String dueDate) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_TITLE, title);
        values.put(COL_DESCRIPTION, description);
        values.put(COL_DUE_DATE, dueDate);

        // Updating row
        int rowsAffected = db.update(TABLE_TODO, values, COL_TODO_ID + " = ?", new String[]{String.valueOf(id)});
        db.close();

        // Check if the update was successful
        return rowsAffected > 0;
    }

    public List<ToDoItem> getTodoItemsByDueDate(int userId, String dueDate) {
        List<ToDoItem> todoItems = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        // Define the columns to retrieve
        String[] columns = {COL_TODO_ID, COL_TITLE, COL_DESCRIPTION, COL_DUE_DATE};

        // Define the selection criteria
        String selection = COL_USER_ID + " = ? AND " + COL_DUE_DATE + " = ?";
        String[] selectionArgs = {String.valueOf(userId), dueDate};

        Cursor cursor = db.query(TABLE_TODO, columns, selection, selectionArgs, null, null, null);

        // Get column indices
        int idIndex = cursor.getColumnIndex(COL_TODO_ID);
        int titleIndex = cursor.getColumnIndex(COL_TITLE);
        int descriptionIndex = cursor.getColumnIndex(COL_DESCRIPTION);
        int dueDateIndex = cursor.getColumnIndex(COL_DUE_DATE);

        // Iterate through the cursor to retrieve ToDoItems
        if (cursor != null && cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(idIndex);
                String title = cursor.getString(titleIndex);
                String description = cursor.getString(descriptionIndex);
                String dueDateStr = cursor.getString(dueDateIndex);

                ToDoItem toDoItem = new ToDoItem(id, title, description, dueDateStr);
                todoItems.add(toDoItem);
            } while (cursor.moveToNext());
        }

        // Close the cursor and database connection
        if (cursor != null) {
            cursor.close();
        }
        db.close();

        return todoItems;
    }

}


