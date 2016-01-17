package com.codepath.simpletodo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    ArrayList<TodoItem> items;
    TodoItemAdapter itemsAdapter;
    ListView lvItems;
    int edit_pos;
    long edit_id;

    TodoItemDatabaseHelper databaseHelper;

    private final int REQUEST_CODE = 20;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        databaseHelper = TodoItemDatabaseHelper.getInstance(this);
        items = databaseHelper.getAllTodoItems();

        lvItems = (ListView)findViewById(R.id.lvItems);
        itemsAdapter = new TodoItemAdapter(this, items);
        lvItems.setAdapter(itemsAdapter);
        setupListViewListener();
    }

    public void onAddItem(View v) {
        EditText etNewItem = (EditText) findViewById(R.id.etNewItem);
        TodoItem item = new TodoItem();
        item.description = etNewItem.getText().toString();;
        item.dueDate = "2016-01-01";
        item.critical = 1;
        item.id = databaseHelper.addTodoItem(item);
        itemsAdapter.add(item);
        etNewItem.setText("");
    }

    private void setupListViewListener() {
        lvItems.setOnItemLongClickListener(
                new AdapterView.OnItemLongClickListener() {
                    @Override
                    public boolean onItemLongClick(AdapterView<?> adapter, View item, int pos, long id) {
                        TodoItem deleteItem = items.get(pos);
                        items.remove(pos);
                        itemsAdapter.notifyDataSetChanged();
                        databaseHelper.deleteTodoItem(deleteItem.id);
                        return true;
                    }
                }
        );

        lvItems.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapter, View item, int pos, long id) {
                        edit_pos = pos;
                        edit_id = items.get(pos).id;
                        launchEditItemActivity(pos);
                    }
                }
        );
    }

    private void launchEditItemActivity(int pos) {
        Intent i = new Intent(this, EditItemActivity.class);
        TodoItem item = items.get(pos);
        i.putExtra("item_text", item.description);
        startActivityForResult(i, REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE) {
            TodoItem item = new TodoItem();
            item.id = edit_id;
            item.description = data.getStringExtra("item_text");
            item.dueDate = "2016-01-01";
            item.critical = 1;
            items.set(edit_pos, item);
            itemsAdapter.notifyDataSetChanged();
            databaseHelper.updateTodoItem(item);
        }
    }
}
