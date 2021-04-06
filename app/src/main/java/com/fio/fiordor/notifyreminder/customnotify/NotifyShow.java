package com.fio.fiordor.notifyreminder.customnotify;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.fio.fiordor.notifyreminder.ListActivity;
import com.fio.fiordor.notifyreminder.R;

public class NotifyShow extends BroadcastReceiver {

    private final String CHANNEL_ID = "channel_id"; //random int

    @Override
    public void onReceive(Context context, Intent intent) {

        Toast.makeText(context, "Alarma", Toast.LENGTH_LONG).show();


        /*
        EEror opening kernel wakelock stats for: wakeup34: Permission denied
        para mi que falta algún tipo de permiso para la aplicación reciva la notificación de la alarma
        y pueda lanzarse sola fuera de la app
        * */

        createNotificationChannel(context);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_baseline_check_24)
                .setContentTitle(context.getString(R.string.app_name))
                .setContentText(context.getText(R.string.save))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(context);

        //random int
        int notificationId = 123123;
        Notification notification = builder.build();



        notificationManagerCompat.notify(notificationId, notification);

        //Intento de lanzar activity pero falla
        //Intent list = new Intent(context, ListActivity.class);
        //context.startActivity(list);
    }

    private void createNotificationChannel(Context context) {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "getString(R.string.channel_name)";
            String description = "getString(R.string.channel_description)";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

}
