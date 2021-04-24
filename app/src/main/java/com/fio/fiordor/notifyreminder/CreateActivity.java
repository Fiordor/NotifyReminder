package com.fio.fiordor.notifyreminder;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.fio.fiordor.notifyreminder.broadcasts.NotifyShow;
import com.fio.fiordor.notifyreminder.pojo.PojoInit;
import com.fio.fiordor.notifyreminder.pojo.Notify;
import com.fio.fiordor.notifyreminder.pojo.Wiki;
import com.fio.fiordor.notifyreminder.threads.DatabaseAddNotify;
import com.fio.fiordor.notifyreminder.threads.GetHTML;
import com.google.android.material.switchmaterial.SwitchMaterial;

import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;

public class CreateActivity extends AppCompatActivity {

    private EditText etTitle;
    private TextView tvDate;
    private TextView tvTime;
    private Button btSelectRepeatEvery;
    private TextView tvRepeatEvery;
    private SwitchMaterial swFixed;
    private SwitchMaterial swReply;
    private EditText etText;

    private int dayOfMonth;
    private int month;
    private int year;

    private int hourOfDay;
    private int minute;

    private int repeatMinute;

    private GetHTML getHTML;
    private boolean firstClick;
    private String replyText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);

        etTitle = findViewById(R.id.etTitle);
        tvDate = findViewById(R.id.tvDate);
        tvTime = findViewById(R.id.tvTime);
        tvRepeatEvery = findViewById(R.id.tvRepeatEvery);
        etText = findViewById(R.id.etText);
        btSelectRepeatEvery = findViewById(R.id.btSelectRepeatEvery);
        swFixed = findViewById(R.id.swFixed);
        swReply = findViewById(R.id.swReply);

        getHTML = null;
        replyText = null;

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeZone(TimeZone.getDefault());

        dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
        month = calendar.get(Calendar.MONTH);
        year = calendar.get(Calendar.YEAR);

        String defaultTitle = String.format(Locale.getDefault(), "Notify-%02d:%02d:%02d-%d/%02d/%02d",
                calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), calendar.get(Calendar.SECOND),
                year, month, dayOfMonth);

        etTitle.setText(defaultTitle);

        String date = String.format(Locale.getDefault(), "%s: %d, %s: %d, %s: %d",
                getString(R.string.day), dayOfMonth, getString(R.string.month), month + 1, getString(R.string.year), year);

        tvDate.setText(date);
        
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

        swFixed.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                btSelectRepeatEvery.setEnabled(!isChecked);
            }
        });

        CreateActivity createActivity = this;
        swReply.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (getHTML != null && getHTML.isAlive()) {
                    getHTML.cancel();
                }

                if (isChecked) {
                    getHTML = new GetHTML(createActivity);
                    getHTML.start();
                } else {
                    getHTML = null;
                }
            }
        });
    }

    public void setReplyText(String replyText) {

        Context context = this;
        Wiki wiki = PojoInit.wiki(replyText);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(context, "Wiki: " + wiki.getTitle(), Toast.LENGTH_SHORT).show();
            }
        });
        this.replyText = replyText;
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

            String title = etTitle.getText().toString();
            String text = etText.getText().toString();
            int repeat = swFixed.isChecked() ? -1 : repeatMinute;

            if (!swReply.isChecked()) {
                replyText = null;
            }

            if (title.trim().length() == 0) return false;

            Calendar calendar = Calendar.getInstance(Locale.getDefault());
            calendar.set(year, month, dayOfMonth, hourOfDay, minute, 0);

            Notify notify = PojoInit.notify(calendar, title, text, replyText, repeat);

            DatabaseAddNotify add = new DatabaseAddNotify(this, notify);
            add.start();

            AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
            Intent intent = new Intent(this, NotifyShow.class);
            intent.putExtra("id", notify.getId());
            intent.putExtra("title", notify.getTitle());
            intent.putExtra("text", notify.getText());
            intent.putExtra("repeatEvery", notify.getRepeatMinute());
            intent.putExtra("replyText", notify.getReplyText());
            PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent, 0);

            alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);

            setResult(Activity.RESULT_OK);
        } else {
            setResult(Activity.RESULT_CANCELED);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        setResult(Activity.RESULT_CANCELED);
    }
}