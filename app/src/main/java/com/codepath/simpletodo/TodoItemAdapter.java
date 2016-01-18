package com.codepath.simpletodo;

import android.content.Context;
import android.graphics.Color;
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
        TextView tvCritical = (TextView) convertView.findViewById(R.id.tvCritical);
        tvDescription.setText(item.description);
        tvDueDate.setText(item.dueDate);
        if (item.critical != 0) {
            tvCritical.setText("HIGH");
            tvCritical.setTextColor(Color.RED);
        }
        else {
            tvCritical.setText("");
        }
        return convertView;
    }
}
