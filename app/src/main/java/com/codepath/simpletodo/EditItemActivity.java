package com.codepath.simpletodo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

public class EditItemActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_item);
        EditText etItem = (EditText) findViewById(R.id.etItem);
        etItem.setText(getIntent().getStringExtra("item_text"));
        etItem.setSelection(etItem.getText().length());
    }

    public void onSave(View v) {
        EditText etItem = (EditText) findViewById(R.id.etItem);
        Intent data = new Intent();
        data.putExtra("item_text", etItem.getText().toString());
        setResult(RESULT_OK, data);
        finish();
    }
}
