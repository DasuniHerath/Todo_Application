package com.example.todoapptest.todo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import com.example.todoapptest.R;
import java.util.ArrayList;

public class TodoAdapter extends ArrayAdapter<String> {

    private ArrayList<String> todoItems;

    public TodoAdapter(Context context, ArrayList<String> todoItems) {
        super(context, 0, todoItems);
        this.todoItems = todoItems;
    }


    public interface OnDeleteButtonClickListener {
        void onDeleteButtonClick(String position);
    }
    private OnUpdateButtonClickListener updateButtonClickListener;

    public interface OnUpdateButtonClickListener {
        void onUpdateButtonClick(String title);
    }
    private OnDeleteButtonClickListener deleteButtonClickListener;
    public void setOnUpdateButtonClickListener(OnUpdateButtonClickListener listener) {
        this.updateButtonClickListener = listener;
    }
    public void setOnDeleteButtonClickListener(OnDeleteButtonClickListener listener) {
        this.deleteButtonClickListener = listener;
    }
    public void updateData(ArrayList<String> todoItems) {
        this.todoItems.clear();
        this.todoItems.addAll(todoItems);
        notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        String todoItem = getItem(position);

        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item_todo, parent, false);
        }

        // Lookup view for data population
        CheckBox checkBox = convertView.findViewById(R.id.checkBox);
        TextView textViewTodo = convertView.findViewById(R.id.textViewTodo);
        Button btnDelete = convertView.findViewById(R.id.btnDelete);
        Button btnUpdate = convertView.findViewById(R.id.btnUpdate);

        // Populate the data into the template view using the data object
        textViewTodo.setText(todoItem);
        String[] parts = todoItem.split(" - ");
        String title = parts[0].trim();


        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (deleteButtonClickListener != null) {
                    deleteButtonClickListener.onDeleteButtonClick(title);
                }
            }
        });

        // Set update button click listener
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (updateButtonClickListener != null) {
                    // Extract the title part before the hyphen
                    String[] parts = todoItem.split(" - ");
                    String title = parts[0];
                    updateButtonClickListener.onUpdateButtonClick(title);
                }
            }
        });
        // Return the completed view to render on screen
        return convertView;
    }


}
