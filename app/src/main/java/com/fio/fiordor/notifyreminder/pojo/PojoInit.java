package com.fio.fiordor.notifyreminder.pojo;

import java.util.Calendar;

public class PojoInit {

    public static Notify notify(Calendar calendar, String title, String text, String replyText, int repeatMinute) {

        Calendar calendarSchema = Calendar.getInstance();
        calendarSchema.set(2020, 0, 0, 0, 0, 0);

        long millis = calendar.getTimeInMillis() - calendarSchema.getTimeInMillis();

        millis = millis / 1000;
        millis = millis / 60;

        millis = millis * 10;

        int id = (int) millis;
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        return new Notify(id, title, year, month, dayOfMonth, hour, minute, repeatMinute, replyText == null ? "" : replyText, text);
    }

    public static Wiki wiki(String info) {

        if (info == null || info.trim().length() == 0) {
            return null;
        }

        String[] split = info.split("\n");

        if (split.length != 3) {
            return null;
        }

        return new Wiki(split[0], split[1], split[2]);
    }
}
