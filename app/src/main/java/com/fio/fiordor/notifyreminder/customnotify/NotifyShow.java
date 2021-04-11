package com.fio.fiordor.notifyreminder.customnotify;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.app.TaskStackBuilder;

import com.fio.fiordor.notifyreminder.ListActivity;
import com.fio.fiordor.notifyreminder.R;
import com.fio.fiordor.notifyreminder.database.NotifyDatabase;
import com.fio.fiordor.notifyreminder.pojo.Notify;
import com.fio.fiordor.notifyreminder.threads.DatabaseAccess;

import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class NotifyShow extends BroadcastReceiver {

    private final String CHANNEL_ID = "channel_id"; //random int

    @Override
    public void onReceive(Context context, Intent intent) {

        Log.d("tags", "entor");

        int id = intent.getIntExtra("id", -1);
        String title = intent.getStringExtra("title");
        String text = intent.getStringExtra("text");

        new Thread(new Runnable() {
            @Override
            public void run() {
                Notify notify = NotifyDatabase.getInstance(context).notifyDao().getNotify(id);
                if (notify != null) {
                    NotifyDatabase.getInstance(context).notifyDao().deleteNotify(notify);
                }
            }
        }).start();

        createNotificationChannel(context);

        PendingIntent resultPendingIntent = createPendingIntent(context);

        Notification notification = createNotification(context, resultPendingIntent, title, text);

        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(context);
        notificationManagerCompat.notify(id, notification);

        //createNotificationFullScreen(context, id, title, text);

    }

    private void createNotificationFullScreen(Context context, int id, String title, String text) {

        Intent fullScreenIntent = new Intent(context, ListActivity.class);
        PendingIntent fullScreenPendingIntent = PendingIntent.getActivity(context, 0,
                fullScreenIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(context, CHANNEL_ID)
                        .setSmallIcon(R.drawable.ic_baseline_check_24)
                        .setContentTitle(title)
                        .setContentText(text)
                        .setPriority(NotificationCompat.PRIORITY_HIGH)
                        .setCategory(NotificationCompat.CATEGORY_CALL)

                        // Use a full-screen intent only for the highest-priority alerts where you
                        // have an associated activity that you would like to launch after the user
                        // interacts with the notification. Also, if your app targets Android 10
                        // or higher, you need to request the USE_FULL_SCREEN_INTENT permission in
                        // order for the platform to invoke this notification.
                        .setFullScreenIntent(fullScreenPendingIntent, true);

        Notification incomingCallNotification = notificationBuilder.build();

        //context.startService()

        // Provide a unique integer for the "notificationId" of each notification.
        //context.startForeground(id, incomingCallNotification);

    }

    private Notification createNotification(Context context, PendingIntent pendingIntent, String title, String text) {

        //crea la notificación y se le asocia un intent
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_baseline_check_24)
                .setContentTitle(title)
                .setContentText(text)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        builder.setContentIntent(pendingIntent);

        return builder.build();
    }

    private PendingIntent createPendingIntent(Context context) {

        //define el intent que se ejecutará cuando se haga click sobre la notificación
        //el intent abrirá el main y se añadirá a la pila de activities para mejor experiencia de navegación
        Intent resultIntent = new Intent(context, ListActivity.class);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addNextIntentWithParentStack(resultIntent);
        return  stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
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
