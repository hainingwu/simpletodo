package com.codepath.simpletodo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    ArrayList<TodoItem> items;
    TodoItemAdapter itemsAdapter;
    ListView lvItems;
    int edit_pos;
    long edit_id;

    TodoItemDatabaseHelper databaseHelper;

    private final int EDIT_ITEM_ACTIVITY_REQUEST_NONE = 0;
    private final int EDIT_ITEM_ACTIVITY_REQUEST_ADD = 1;
    private final int EDIT_ITEM_ACTIVITY_REQUEST_UPDATE = 2;

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
        launchEditItemActivity(-1);
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
        // If pos < 0, create an empty TodoItem instance, otherwise get the item to be edited.
        TodoItem item;
        int requestCode = EDIT_ITEM_ACTIVITY_REQUEST_NONE;
        Intent i = new Intent(this, EditItemActivity.class);

        if (pos < 0) {
            item = new TodoItem();
            requestCode = EDIT_ITEM_ACTIVITY_REQUEST_ADD;
        }
        else {
            item = items.get(pos);
            requestCode = EDIT_ITEM_ACTIVITY_REQUEST_UPDATE;
            i.putExtra("item_description", item.description);
            i.putExtra("item_due_date", item.dueDate);
            i.putExtra("item_critical", item.critical);
        }
        startActivityForResult(i, requestCode);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            TodoItem item = new TodoItem();
            item.description = data.getStringExtra("item_description");
            item.dueDate = data.getStringExtra("item_due_date");
            item.critical = data.getIntExtra("item_critical", 0);
            if (requestCode == EDIT_ITEM_ACTIVITY_REQUEST_ADD) {
                item.id = databaseHelper.addTodoItem(item);
                items.add(item);
                itemsAdapter.notifyDataSetChanged();
            }
            else if (requestCode == EDIT_ITEM_ACTIVITY_REQUEST_UPDATE) {
                item.id = edit_id;
                items.set(edit_pos, item);
                itemsAdapter.notifyDataSetChanged();
                databaseHelper.updateTodoItem(item);
            }
        }
    }
}
