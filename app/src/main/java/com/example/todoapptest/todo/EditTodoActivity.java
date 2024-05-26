package com.example.todoapptest.todo;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.todoapptest.auth.DatabaseHelper;
import com.example.todoapptest.R;
import com.example.todoapptest.auth.DatabaseHelper;

public class EditTodoActivity extends AppCompatActivity {

    private EditText etTitle, etDescription, etDueDate;
    private Button btnSave;
    private DatabaseHelper dbHelper;
    private int todoItemId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_todo);

        dbHelper = new DatabaseHelper(this);
        etTitle = findViewById(R.id.etTitle);
        etDescription = findViewById(R.id.etDescription);
        etDueDate = findViewById(R.id.etDueDate);
        btnSave = findViewById(R.id.btnSave);



        // Populate EditText fields with existing todo item details
        populateFields(dbHelper.getUserIdByUsername(getIntent().getStringExtra("USERNAME")),getIntent().getStringExtra("TITLE"));

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = etTitle.getText().toString();
                String description = etDescription.getText().toString();
                String dueDate = etDueDate.getText().toString();
                int id = dbHelper.getTodoItemIdByTitle(dbHelper.getUserIdByUsername(getIntent().getStringExtra("USERNAME")),getIntent().getStringExtra("TITLE"));
                // Perform validation if needed

                boolean result = dbHelper.updateTodoItem(id, title, description,dueDate);

                if (result) {
                    Toast.makeText(EditTodoActivity.this, "Todo item updated successfully", Toast.LENGTH_SHORT).show();
                    finish(); // Close the activity and return to the previous one
                } else {
                    Toast.makeText(EditTodoActivity.this, "Failed to update todo item", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void populateFields(int userid, String title) {
        // Get todo item details from the database and populate EditText fields
        ToDoItem todoItemDetails = dbHelper.getToDoItem(title);
        if (todoItemDetails != null) {
            etTitle.setText(todoItemDetails.getTitle());
            etDescription.setText(todoItemDetails.getDescription());
            etDueDate.setText(todoItemDetails.getDueDate());
        }
    }
}

