package com.fio.fiordor.notifyreminder.pojo;

import androidx.annotation.NonNull;

import java.util.Calendar;

public class PojoInit {

    /**
     * Método lanzadera para declara un Notify.
     * Como se usan objetos pojo los contructores de estos no los podemos escribir a nuestro gusto,
     * por tanto, se usa este método lanzadera el cual es más como de llamar y donde se definen
     * todos los atributos del objeto Notify.
     *
     * @param calendar del lanzamiento de la notificación
     * @param title de la notificación
     * @param text de la notificación que puede ser vacío
     * @param replyText en caso de que se responda
     * @param repeatMinute -1 para dejar fijo la notificación
     * @return el objeto Notify
     */
    public static Notify notify(@NonNull Calendar calendar, @NonNull String title, @NonNull String text, String replyText, int repeatMinute) {

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

    /**
     * Método lanzadera para declara una Wiki.
     * Como se usan objetos pojo los contructores de estos no los podemos escribir a nuestro gusto,
     * por tanto, se usa este método lanzadera el cual es más como de llamar y donde se definen
     * todos los atributos del objeto Wiki.
     *
     * @param info String que debe contener la url el título y el texto separados por saltos de línea
     * @return el objeto Wiki
     */
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
