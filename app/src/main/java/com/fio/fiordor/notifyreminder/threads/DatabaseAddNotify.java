package com.fio.fiordor.notifyreminder.threads;

import android.widget.Toast;

import com.fio.fiordor.notifyreminder.CreateActivity;
import com.fio.fiordor.notifyreminder.ListActivity;
import com.fio.fiordor.notifyreminder.R;
import com.fio.fiordor.notifyreminder.database.NotifyDatabase;
import com.fio.fiordor.notifyreminder.pojo.Notify;

import java.lang.ref.WeakReference;
import java.util.List;

public class DatabaseAddNotify extends Thread {

    private WeakReference<CreateActivity> weakReference;
    private Notify notify;

    public DatabaseAddNotify(CreateActivity weakReference, Notify notify) {
        this.weakReference = new WeakReference<>(weakReference);
        this.notify = notify;
    }

    @Override
    public void run() {

        if (weakReference.get() == null) {
            return;
        }

        NotifyDatabase.getInstance(weakReference.get()).notifyDao().addNotify(notify);

        weakReference.get().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(weakReference.get(), R.string.save, Toast.LENGTH_SHORT).show();
                weakReference.get().onBackPressed();
            }
        });
    }
}