package com.fio.fiordor.notifyreminder;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import com.fio.fiordor.notifyreminder.pojo.Notify;
import com.fio.fiordor.notifyreminder.threads.DatabaseAddNotify;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class CreateActivity extends AppCompatActivity {

    private EditText etTitle;
    private TextView tvDate;
    private TextView tvTime;
    private TextView tvRepeatEvery;
    private EditText etText;

    private int dayOfMonth;
    private int month;
    private int year;

    private int hourOfDay;
    private int minute;

    private int repeatMinute;
    
    private boolean firstClick;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);

        etTitle = findViewById(R.id.etTitle);
        tvDate = findViewById(R.id.tvDate);
        tvTime = findViewById(R.id.tvTime);
        tvRepeatEvery = findViewById(R.id.tvRepeatEvery);
        etText = findViewById(R.id.etText);

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeZone(TimeZone.getDefault());

        dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
        month = calendar.get(Calendar.MONTH) + 1;
        year = calendar.get(Calendar.YEAR);

        String date = String.format(Locale.getDefault(), "%s: %d, %s: %d, %s: %d",
                getString(R.string.day), dayOfMonth, getString(R.string.month), month + 1, getString(R.string.year), year);

        tvDate.setText(date);

        String defaultTitle = String.format(Locale.getDefault(), "Notify-%02d:%02d:%02d-%d/%02d/%02d",
                calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), calendar.get(Calendar.SECOND),
                year, month, dayOfMonth);

        etTitle.setText(defaultTitle);
        
        firstClick = false;

        etTitle.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!firstClick) {
                    etTitle.setText("");
                    firstClick = true;
                }
            }
        });
    }

    public void btSelectDate(View view) {

        DatePickerDialog picker = new DatePickerDialog(this);

        picker.setOnDateSetListener((view1, year, month, dayOfMonth) -> {
            this.dayOfMonth = dayOfMonth;
            this.month = month;
            this.year = year;

            String date = String.format(Locale.getDefault(), "%s: %d, %s: %d, %s: %d",
                    getString(R.string.day), dayOfMonth, getString(R.string.month), month + 1, getString(R.string.year), year);

            tvDate.setText(date);
        });
        picker.show();
    }

    public void btSelectTime(View view) {

        int defaultHour = 0;
        int defaultMinute = 0;

        boolean isSelectTime = view.getId() == R.id.btSelectTime;

        if (isSelectTime) {
            defaultHour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
            defaultMinute = Calendar.getInstance().get(Calendar.MINUTE);
        }

        TimePickerDialog picker = new TimePickerDialog(this, (TimePicker view1, int hourOfDay, int minute) -> {

            if (isSelectTime) {

                String time = String.format(Locale.getDefault(), "%s %d %s %d",
                        getString(R.string.hour), hourOfDay, getString(R.string.minute), minute);

                tvTime.setText(time);

                this.hourOfDay = hourOfDay;
                this.minute = minute;
            }
            else {

                String time = String.format(Locale.getDefault(), "%d %s %d %s",
                        hourOfDay, getString(R.string.hour), minute, getString(R.string.minute));

                tvRepeatEvery.setText(time);

                repeatMinute = (hourOfDay * 60) + minute;
            }

        }, defaultHour, defaultMinute, true);

        picker.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.just_save, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == R.id.imJustSave) {

            DatabaseAddNotify add = new DatabaseAddNotify(this, new Notify(
                    etTitle.getText().toString(), year, month, dayOfMonth, hourOfDay, minute, repeatMinute, etText.getText().toString()
            ));
            add.start();

            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}