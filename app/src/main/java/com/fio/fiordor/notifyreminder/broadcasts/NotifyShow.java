package com.fio.fiordor.notifyreminder.broadcasts;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.app.RemoteInput;
import androidx.core.app.TaskStackBuilder;

import com.fio.fiordor.notifyreminder.ListActivity;
import com.fio.fiordor.notifyreminder.R;
import com.fio.fiordor.notifyreminder.database.NotifyDatabase;
import com.fio.fiordor.notifyreminder.pojo.Notify;
import com.fio.fiordor.notifyreminder.pojo.PojoInit;
import com.fio.fiordor.notifyreminder.pojo.Wiki;

public class NotifyShow extends BroadcastReceiver {

    private final String CHANNEL_ID = "notify_channel_id";

    @Override
    public void onReceive(Context context, Intent intent) {

        int id = intent.getIntExtra("id", -1);
        if (id == -1) { return; }
        String title = intent.getStringExtra("title");
        String text = intent.getStringExtra("text");
        int repeatEvery = intent.getIntExtra("repeatEvery", 0);
        Wiki wiki = PojoInit.wiki(intent.getStringExtra("replyText"));

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

        NotificationCompat.Builder builder = initNotificationBuilder(context, title, text, wiki);

        NotificationCompat.Action replyAction = null;

        Intent resultIntent;
        if (wiki != null) {

            replyAction = createReplyPendingIntent(context, id, title, wiki.getTitle());

            //resultIntent = new Intent(context, NotifyUrl.class).putExtra("url", wiki.getHref());
            resultIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(wiki.getHref()));
        } else {
            resultIntent = new Intent(context, ListActivity.class).putExtra("notify_id", id);
        }
        PendingIntent resultPendingIntent = createResultPendingIntent(context, resultIntent);

        Notification notification = createNotification(context, builder, resultPendingIntent, replyAction, wiki, repeatEvery);

        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(context);
        notificationManagerCompat.notify(id, notification);
    }

    private Notification createNotification(Context context, NotificationCompat.Builder builder, PendingIntent pendingIntent, NotificationCompat.Action replyAction, Wiki wiki, int repeatEvery) {

        builder.setContentIntent(pendingIntent);

        if (replyAction != null) {
            builder.addAction(replyAction);
            Intent openUrlAction = new Intent(context, NotifyUrl.class).putExtra("url", wiki.getHref());
            PendingIntent openUrlActionPendingIntent = PendingIntent.getBroadcast(context, 0 , openUrlAction, 0);
            builder.addAction(R.drawable.ic_baseline_reply_24, context.getString(R.string.open_wiki), openUrlActionPendingIntent);
        }

        Notification notification = builder.build();

        if (repeatEvery == -1) {
            notification.flags |= Notification.FLAG_NO_CLEAR;
        }

        return notification;
    }

    private NotificationCompat.Builder initNotificationBuilder(Context context, String title, String text, Wiki wiki) {
        //crea la notificación y se le asocia un intent
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_baseline_notification_important_24)
                .setContentTitle(title)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        if (text.trim().length() != 0) {
            builder.setContentText(text);
        }

        if (wiki != null) {
            String largeText = wiki.getTitle() + "\n" + wiki.getText();
            builder.setStyle(new NotificationCompat.BigTextStyle().bigText(largeText));
        }

        return builder;
    }

    private NotificationCompat.Action createReplyPendingIntent(Context context, int id, String title, String replyText) {

        String keyReply = title + id;
        RemoteInput remoteInput = new RemoteInput.Builder(keyReply).setLabel(replyText).build();

        Intent intent = new Intent(context, NotifyReply.class);
        intent.putExtra("id", id);
        intent.putExtra("title", title);
        intent.putExtra("replyText", replyText);

        PendingIntent replyPendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        return new NotificationCompat.Action.Builder(R.drawable.ic_baseline_reply_24, context.getString(R.string.reply), replyPendingIntent)
                .addRemoteInput(remoteInput)
                .build();
    }

    private PendingIntent createResultPendingIntent(Context context, Intent intent) {

        //define el intent que se ejecutará cuando se haga click sobre la notificación
        //el intent abrirá el main y se añadirá a la pila de activities para mejor experiencia de navegación
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addNextIntentWithParentStack(intent);
        return  stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    private void createNotificationChannel(Context context) {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = context.getString(R.string.notify_for_reminder);
            String description = context.getString(R.string.notify_for_reminder_summary);
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
