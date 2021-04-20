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

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.app.RemoteInput;
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

    private final String CHANNEL_ID = "notify_channel_id";

    @Override
    public void onReceive(Context context, Intent intent) {

        int id = intent.getIntExtra("id", -1);
        if (id == -1) { return; }
        String title = intent.getStringExtra("title");
        String text = intent.getStringExtra("text");
        int repeatEvery = intent.getIntExtra("repeatEvery", 0);

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

        PendingIntent resultPendingIntent = createPendingIntent(context, id);

        Notification notification = createNotification(context, resultPendingIntent, title, text, repeatEvery);

        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(context);
        notificationManagerCompat.notify(id, notification);
    }

    private Notification createNotification(Context context, PendingIntent pendingIntent, String title, String text, int repeatEvery) {

        String replyLabel = "some string";
        RemoteInput remoteInput = new RemoteInput.Builder("key_text_reply").setLabel(replyLabel).build();

        PendingIntent replyPendingIntent = PendingIntent.getBroadcast(context, 0, new Intent(context, NotifyReply.class), PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Action action = new NotificationCompat.Action.Builder(R.drawable.ic_baseline_reply_24, "label", replyPendingIntent)
                .addRemoteInput(remoteInput)
                .build();

        //crea la notificación y se le asocia un intent
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_baseline_notification_important_24)
                .setContentTitle(title)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        if (text.trim().length() != 0) {
            builder.setContentText(text);
        }

        builder.setContentIntent(pendingIntent)
                .addAction(action);

        Notification notification = builder.build();

        if (repeatEvery == -1) {
            notification.flags |= Notification.FLAG_NO_CLEAR;
        }

        return notification;
    }

    private PendingIntent createPendingIntent(Context context, int id) {

        //define el intent que se ejecutará cuando se haga click sobre la notificación
        //el intent abrirá el main y se añadirá a la pila de activities para mejor experiencia de navegación
        Intent resultIntent = new Intent(context, ListActivity.class);
        resultIntent.putExtra("notify_id", id);
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
