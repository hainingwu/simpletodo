package com.codepath.simpletodo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class TodoItemAdapter extends ArrayAdapter<TodoItem> {
    public TodoItemAdapter(Context context, ArrayList<TodoItem> todoItems) {
        super(context, 0, todoItems);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TodoItem item = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.todo_item, parent, false);
        }

        TextView tvDescription = (TextView) convertView.findViewById(R.id.tvDescription);
        TextView tvDueDate = (TextView) convertView.findViewById(R.id.tvDueDate);
        tvDescription.setText(item.description);
        tvDueDate.setText(item.dueDate);
        return convertView;
    }
}
