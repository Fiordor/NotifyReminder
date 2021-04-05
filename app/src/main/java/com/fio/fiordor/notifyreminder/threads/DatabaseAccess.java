package com.fio.fiordor.notifyreminder.threads;

import android.util.Log;

import com.fio.fiordor.notifyreminder.ListActivity;
import com.fio.fiordor.notifyreminder.database.NotifyDao;
import com.fio.fiordor.notifyreminder.database.NotifyDatabase;
import com.fio.fiordor.notifyreminder.pojo.Notify;

import java.lang.ref.WeakReference;
import java.util.List;

public class DatabaseAccess extends Thread implements NotifyDao {

    private final int ADD_NOTIFY = 0;
    private final int DELETE_NOTIFY = 1;
    private final int LOAD_ALL_NOTIFIES = 2;

    private int selection;
    private Notify notify;
    private int id;

    private WeakReference<ListActivity> weakReference;

    public DatabaseAccess(ListActivity weakReference) {
        this.weakReference = new WeakReference<>(weakReference);
        selection = -1;
    }

    @Override
    public void addNotify(Notify notify) {
        this.notify = notify;
        selection = ADD_NOTIFY;
        start();
    }

    @Override
    public void deleteNotify(Notify notify) {
        this.notify = notify;
        selection = DELETE_NOTIFY;
        start();
    }

    @Override
    public List<Notify> loadAllNotifies() {
        start();
        selection = LOAD_ALL_NOTIFIES;
        return null;
    }

    @Override
    public void run() {
        switch (selection) {
            case ADD_NOTIFY :
                NotifyDatabase.getInstance(weakReference.get()).notifyDao().addNotify(notify);
                break;
            case DELETE_NOTIFY :
                NotifyDatabase.getInstance(weakReference.get()).notifyDao().deleteNotify(notify);
                break;
            case LOAD_ALL_NOTIFIES :
                List<Notify> notifies = NotifyDatabase.getInstance(weakReference.get()).notifyDao().loadAllNotifies();
                weakReference.get().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        weakReference.get().updateList(notifies);
                    }
                });
                break;
        }
    }
}
