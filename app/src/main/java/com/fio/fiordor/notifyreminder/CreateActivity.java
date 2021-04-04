package com.fio.fiordor.notifyreminder;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.Calendar;
import java.util.Locale;

public class CreateActivity extends AppCompatActivity {

    private TextView tvDate;
    private TextView tvTime;
    private TextView tvRepeatEvery;

    private int dayOfMonth;
    private int month;
    private int year;

    private int hourOfDay;
    private int minute;

    private int repeatMinute;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);

        tvDate = findViewById(R.id.tvDate);
        tvTime = findViewById(R.id.tvTime);
        tvRepeatEvery = findViewById(R.id.tvRepeatEvery);
    }

    public void btSelectDate(View view) {

        DatePickerDialog picker = new DatePickerDialog(this);

        picker.setOnDateSetListener((view1, year, month, dayOfMonth) -> {
            this.dayOfMonth = dayOfMonth;
            this.month = month;
            this.year = year;

            String date = String.format(Locale.getDefault(), "%s: %d, %s: %d, %s: %d",
                    getString(R.string.day), dayOfMonth, getString(R.string.month), month, getString(R.string.year), year);

            tvDate.setText(date);
        });
        picker.show();
    }

    public void btSelectTime(View view) {

        int defaultHour = 0;
        int defaultMinute = 0;

        boolean isSelectTime = view.getId() == R.id.btSelectTime;

        if (isSelectTime) {
            defaultHour = Calendar.HOUR_OF_DAY;
            defaultMinute = Calendar.MINUTE;
        }

        TimePickerDialog picker = new TimePickerDialog(this, (TimePicker view1, int hourOfDay, int minute) -> {

            String time = String.format(Locale.getDefault(), "%d %s %d %s",
                    hourOfDay, getString(R.string.hour), minute, getString(R.string.minute));

            if (isSelectTime) {

                tvTime.setText(time);

                this.hourOfDay = hourOfDay;
                this.minute = minute;
            }
            else {

                tvRepeatEvery.setText(time);

                repeatMinute = (hourOfDay * 60) + minute;
            }

        }, defaultHour, defaultMinute, true);

        picker.show();
    }
}