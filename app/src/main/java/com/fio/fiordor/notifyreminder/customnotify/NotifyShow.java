package com.fio.fiordor.notifyreminder.customnotify;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.fio.fiordor.notifyreminder.ListActivity;
import com.fio.fiordor.notifyreminder.R;
import com.fio.fiordor.notifyreminder.database.NotifyDatabase;
import com.fio.fiordor.notifyreminder.pojo.Notify;
import com.fio.fiordor.notifyreminder.threads.DatabaseAccess;

import java.util.Calendar;
import java.util.Locale;

public class NotifyShow extends BroadcastReceiver {

    private final String CHANNEL_ID = "channel_id"; //random int

    @Override
    public void onReceive(Context context, Intent intent) {

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
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_baseline_check_24)
                .setContentTitle(title)
                .setContentText(text)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(context);

        Notification notification = builder.build();

        notificationManagerCompat.notify(title.hashCode(), notification);

        //Intent open = context.getPackageManager().getLaunchIntentForPackage("NotifyReminder");
        //open.addCategory(Intent.CATEGORY_LAUNCHER);
        //context.startActivity(intent);
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
