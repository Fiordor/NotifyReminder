package com.fio.fiordor.notifyreminder.broadcasts;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

public class NotifyUrl extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        String url = intent.getStringExtra("url");

        if (url != null) {
            Intent openUrl = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            openUrl.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(openUrl);
        }
    }
}
