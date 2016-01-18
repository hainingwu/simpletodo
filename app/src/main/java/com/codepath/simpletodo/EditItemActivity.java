package com.codepath.simpletodo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.ToggleButton;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class EditItemActivity extends AppCompatActivity {
    int days[] = {31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
    int defaultYear;
    int defaultMon;
    int defaultDay;

    Spinner monSpinner;
    Spinner daySpinner;
    Spinner yearSpinner;
    ToggleButton criticalToggleButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_item);

        // Description
        EditText etItem = (EditText) findViewById(R.id.etItem);
        etItem.setText(getIntent().getStringExtra("item_description"));
        etItem.setSelection(etItem.getText().length());

        // Due Date
        String dueDate = getIntent().getStringExtra("item_due_date");
        if (dueDate != null) {
            defaultYear = Integer.parseInt(dueDate.substring(0, 4));
            defaultMon = Integer.parseInt(dueDate.substring(5, 7));
            defaultDay = Integer.parseInt(dueDate.substring(8, 10));
        }
        else {
            defaultYear = -1;
            defaultMon = -1;
            defaultDay = -1;
        }
        monSpinner = (Spinner) findViewById(R.id.spinnerMonth);
        daySpinner = (Spinner) findViewById(R.id.spinnerDay);
        yearSpinner = (Spinner) findViewById(R.id.spinnerYear);
        setMonSpinner();
        setDaySpinner();
        setYearSpinner();

        // Critical
        int critical = getIntent().getIntExtra("item_critical", 0);
        criticalToggleButton = (ToggleButton) findViewById(R.id.tbCritical);
        criticalToggleButton.setChecked(critical != 0);
    }

    private void setMonSpinner() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.month_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        monSpinner.setAdapter(adapter);
        if (defaultMon != -1) {
            monSpinner.setSelection(defaultMon-1);
        }
        monSpinner.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view,
                                               int pos, long id) {
                        setDaySpinner();
                    }

                    public void onNothingSelected(AdapterView<?> parent) {
                    }
                }
        );
    }

    private void setDaySpinner() {
        int posMon = monSpinner.getSelectedItemPosition();
        ArrayList<String> dayArray = new ArrayList<String>();
        for (int i=1; i<=days[posMon]; i++) {
            dayArray.add(Integer.toString((i)));
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, dayArray);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        daySpinner.setAdapter(adapter);
        if (defaultDay != -1) {
            daySpinner.setSelection(defaultDay-1);
        }
        daySpinner.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view,
                                               int pos, long id) {
                    }

                    public void onNothingSelected(AdapterView<?> parent) {
                    }
                }
        );
    }

    private void setYearSpinner() {
        Calendar now = Calendar.getInstance();
        final int startYear = now.get(Calendar.YEAR);

        // Only show current year to current year + 2
        ArrayList<String> yearArray = new ArrayList<String>();
        for (int i=0; i<3; i++) {
            yearArray.add(Integer.toString((startYear+i)));
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, yearArray);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        yearSpinner.setAdapter(adapter);
        if (defaultYear != -1) {
            yearSpinner.setSelection(defaultYear-startYear);
        }
        yearSpinner.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view,
                                               int pos, long id) {
                    }

                    public void onNothingSelected(AdapterView<?> parent) {
                    }
                }
        );
    }

    public void onSave(View v) {
        EditText etItem = (EditText) findViewById(R.id.etItem);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        int selectedYear = Integer.parseInt(yearSpinner.getSelectedItem().toString());
        int selectedMon = monSpinner.getSelectedItemPosition() + 1;
        int selectedDay = daySpinner.getSelectedItemPosition() + 1;
        String dueDate = String.format("%4d-%02d-%02d", selectedYear, selectedMon, selectedDay);
        int critical = 0;
        if (criticalToggleButton.isChecked()) {
            critical = 1;
        }

        Intent data = new Intent();
        data.putExtra("item_description", etItem.getText().toString());
        data.putExtra("item_due_date", dueDate.toString());
        data.putExtra("item_critical", critical);
        setResult(RESULT_OK, data);
        finish();
    }
}
