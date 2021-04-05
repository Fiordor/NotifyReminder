package com.fio.fiordor.notifyreminder.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.fio.fiordor.notifyreminder.pojo.Notify;

@Database(version = 1, entities = Notify.class)
public abstract class NotifyDatabase extends RoomDatabase {

    private static NotifyDatabase ourInstance;

    public synchronized static NotifyDatabase getInstance(Context context) {
        if (ourInstance == null) {
            ourInstance = Room.databaseBuilder(context, NotifyDatabase.class, "notify_database").build();
        }

        return ourInstance;
    }

    public abstract NotifyDao notifyDao();
}
