package com.fio.fiordor.notifyreminder.pojo;

import android.util.Log;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Calendar;
import java.util.Date;

@Entity(tableName = "notify_database")
public class Notify {

    @PrimaryKey
    @ColumnInfo
    private int id;

    @ColumnInfo
    private String title;

    @ColumnInfo
    private int year;
    @ColumnInfo
    private int month;
    @ColumnInfo
    private int dayOfMonth;

    @ColumnInfo
    private int hour;
    @ColumnInfo
    private int minute;

    @ColumnInfo
    private int repeatMinute;

    @ColumnInfo
    private String text;

    public Notify(String title, int year, int month, int dayOfMonth, int hour, int minute, int repeatMinute, String text) {
        this.title = title;
        this.year = year;
        this.month = month;
        this.dayOfMonth = dayOfMonth;
        this.hour = hour;
        this.minute = minute;
        this.repeatMinute = repeatMinute;
        this.text = text;

        Calendar calendarSchema = Calendar.getInstance();
        calendarSchema.set(2020, 0, 0, 0, 0, 0);

        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, dayOfMonth, hour, minute, 0);

        long millis = calendar.getTimeInMillis() - calendarSchema.getTimeInMillis();

        millis = millis / 1000;
        millis = millis / 60;

        id = (int) millis;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getDayOfMonth() {
        return dayOfMonth;
    }

    public void setDayOfMonth(int dayOfMonth) {
        this.dayOfMonth = dayOfMonth;
    }

    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public int getMinute() {
        return minute;
    }

    public void setMinute(int minute) {
        this.minute = minute;
    }

    public int getRepeatMinute() {
        return repeatMinute;
    }

    public void setRepeatMinute(int repeatMinute) {
        this.repeatMinute = repeatMinute;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
