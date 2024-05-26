package com.example.todoapptest.todo;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SearchView;

import androidx.appcompat.app.AppCompatActivity;
import com.example.todoapptest.auth.DatabaseHelper;
import com.example.todoapptest.R;
import java.util.ArrayList;
import java.util.List;

public class ViewTodoActivity extends AppCompatActivity implements TodoAdapter.OnDeleteButtonClickListener, TodoAdapter.OnUpdateButtonClickListener {


    private ListView listView;
    private DatabaseHelper dbHelper;
    private SearchView searchView;

    private Button addTodoButton;

    @Override
    public void onDeleteButtonClick(String position) {

        dbHelper.deleteTodoItem(dbHelper.getTodoItemIdByTitle(dbHelper.getUserIdByUsername(getIntent().getStringExtra("USERNAME")), position));
        ArrayList<String> updatedTodoItems = dbHelper.getAllTodoItems(dbHelper.getUserIdByUsername(getIntent().getStringExtra("USERNAME")));
        // Update the adapter with the new dataset
        ((TodoAdapter)listView.getAdapter()).updateData(updatedTodoItems);
    }

    @Override
    public void onUpdateButtonClick(String title) {
        Intent intent = new Intent(ViewTodoActivity.this, EditTodoActivity.class);
        intent.putExtra("USERNAME", getIntent().getStringExtra("USERNAME"));
        intent.putExtra("TITLE", title);
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Get all todo items from the database
        ArrayList<String> todoItems = dbHelper.getAllTodoItems(dbHelper.getUserIdByUsername(getIntent().getStringExtra("USERNAME")));

        // Create an adapter to display the todo items in a ListView
        TodoAdapter adapter = new TodoAdapter(this, todoItems);
        adapter.setOnDeleteButtonClickListener(this);
        adapter.setOnUpdateButtonClickListener(this);
        listView.setAdapter(adapter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_todo);
        addTodoButton = findViewById(R.id.addTodoButton);
        searchView = findViewById(R.id.searchView);
        // OnClickListener for the "Add Todo" button
        addTodoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //  Intent to start the CreateTodoActivity
                Intent intent = new Intent(ViewTodoActivity.this, CreateTodoActivity.class);
                intent.putExtra("USERNAME", getIntent().getStringExtra("USERNAME"));
                startActivity(intent); // Start the activity
            }
        });
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                filterTodoItemsByDueDate(dbHelper.getUserIdByUsername(getIntent().getStringExtra("USERNAME")),query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterTodoItemsByDueDate(dbHelper.getUserIdByUsername(getIntent().getStringExtra("USERNAME")),newText);
                return true;
            }
        });


        dbHelper = new DatabaseHelper(this);
        listView = findViewById(R.id.listView);

        // Get all todo items from the database
        ArrayList<String> todoItems = dbHelper.getAllTodoItems(dbHelper.getUserIdByUsername(getIntent().getStringExtra("USERNAME")));

        // an adapter to display the todo items in a ListView
        TodoAdapter adapter = new TodoAdapter(this, todoItems);
        adapter.setOnDeleteButtonClickListener(this);
        adapter.setOnUpdateButtonClickListener(this);
        listView.setAdapter(adapter);

    }
    private void filterTodoItemsByDueDate(int userID, String dueDate) {
        List<ToDoItem> filteredItems = dbHelper.getTodoItemsByDueDate(userID, dueDate);

        // Extract titles from ToDoItems
        ArrayList<String> filteredTitles = new ArrayList<>();
        for (ToDoItem item : filteredItems) {
            filteredTitles.add(item.getTitle());
        }

        // Create the adapter with filtered titles
        TodoAdapter adapter = new TodoAdapter(this, filteredTitles);

        // Update the ListView with the filtered data
        listView.setAdapter(adapter);
    }


}
