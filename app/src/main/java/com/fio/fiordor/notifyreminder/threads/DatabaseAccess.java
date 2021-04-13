package com.fio.fiordor.notifyreminder.threads;

import android.util.Log;

import com.fio.fiordor.notifyreminder.ListActivity;
import com.fio.fiordor.notifyreminder.database.NotifyDao;
import com.fio.fiordor.notifyreminder.database.NotifyDatabase;
import com.fio.fiordor.notifyreminder.pojo.Notify;

import java.lang.ref.WeakReference;
import java.util.List;

public class DatabaseAccess extends Thread {

    public final int ADD_NOTIFY = 0;
    public final int DELETE_NOTIFY = 1;
    public final int LOAD_ALL_NOTIFIES = 2;
    public final int GET_NOTIFY = 3;
    public final int DELETE_NOTIFY_BY_ID = 4;

    private int selection;
    private Notify notify;
    private int id;
    private boolean update;

    private WeakReference<ListActivity> weakReference;

    public DatabaseAccess(ListActivity weakReference) {
        this.weakReference = new WeakReference<>(weakReference);
        selection = -1;
        update = false;
    }

    public void addNotify(Notify notify, boolean update) {
        this.notify = notify;
        this.update = update;
        selection = ADD_NOTIFY;
        start();
    }

    public void deleteNotify(Notify notify, boolean update) {
        this.notify = notify;
        this.update = update;
        selection = DELETE_NOTIFY;
        start();
    }

    public void deleteNotifyById(int id, boolean update) {
        selection = DELETE_NOTIFY_BY_ID;
        this.update = update;
        start();
    }

    public List<Notify> loadAllNotifies() {
        selection = LOAD_ALL_NOTIFIES;
        start();
        return null;
    }

    @Override
    public void run() {

        if (weakReference.get() == null) return;
        switch (selection) {
            case ADD_NOTIFY :
                NotifyDatabase.getInstance(weakReference.get()).notifyDao().addNotify(notify);
                break;
            case DELETE_NOTIFY :
                NotifyDatabase.getInstance(weakReference.get()).notifyDao().deleteNotify(notify);
                break;
            case LOAD_ALL_NOTIFIES :
                update = true;
                break;
            case DELETE_NOTIFY_BY_ID :
                Notify n = NotifyDatabase.getInstance(weakReference.get()).notifyDao().getNotify(id);
                if (n != null) {
                    NotifyDatabase.getInstance(weakReference.get()).notifyDao().deleteNotify(n);
                }
                break;
        }

        if (update) {
            if (weakReference.get() == null) return;
            List<Notify> notifies = NotifyDatabase.getInstance(weakReference.get()).notifyDao().loadAllNotifies();
            weakReference.get().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    weakReference.get().updateList(notifies);
                }
            });
        }
    }
}
