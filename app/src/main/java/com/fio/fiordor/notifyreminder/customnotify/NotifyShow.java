package com.fio.fiordor.notifyreminder.customnotify;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.fio.fiordor.notifyreminder.ListActivity;

public class NotifyShow extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Toast.makeText(context, "Alarma", Toast.LENGTH_LONG).show();

        //Intento de lanzar activity pero falla
        //Intent list = new Intent(context, ListActivity.class);
        //context.startActivity(list);
    }
}
