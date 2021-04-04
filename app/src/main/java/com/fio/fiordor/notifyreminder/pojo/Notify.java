package com.fio.fiordor.notifyreminder.pojo;

public class Notify {

    private int id;
    private String title;

    private int year;
    private int month;
    private int dayOfMonth;

    private int hour;
    private int minute;

    private int repeatMinute;

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

        id = title.hashCode() + year + month + dayOfMonth;
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
