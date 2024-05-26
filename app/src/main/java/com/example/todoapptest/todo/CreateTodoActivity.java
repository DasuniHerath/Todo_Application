package com.example.todoapptest.todo;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.todoapptest.auth.DatabaseHelper;
import com.example.todoapptest.R;

public class CreateTodoActivity extends AppCompatActivity {

    private EditText etTitle, etDescription, etDueDate;
    private Button btnSave;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_todo);

        dbHelper = new DatabaseHelper(this);
        etTitle = findViewById(R.id.etTitle);
        etDescription = findViewById(R.id.etDescription);
        etDueDate = findViewById(R.id.etDueDate);
        btnSave = findViewById(R.id.btnSave);

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = etTitle.getText().toString();
                String description = etDescription.getText().toString();
                String dueDate = etDueDate.getText().toString();

                // Get the user ID from the logged-in user's session
                int userId = dbHelper.getUserIdByUsername(getIntent().getStringExtra("USERNAME"));

                long result = dbHelper.insertTodoItem(userId, title, description, dueDate);

                if (result != -1) {
                    Toast.makeText(CreateTodoActivity.this, "Todo item saved successfully", Toast.LENGTH_SHORT).show();
                    finish(); // Close the activity and return to the previous one
                } else {
                    Toast.makeText(CreateTodoActivity.this, "Failed to save todo item", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}

