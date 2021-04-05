package com.fio.fiordor.notifyreminder.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.fio.fiordor.notifyreminder.pojo.Notify;

import java.util.List;

@Dao
public interface NotifyDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void addNotify(Notify notify);

    @Delete
    void deleteNotify(Notify notify);

    @Query("SELECT * FROM notify_database")
    List<Notify> loadAllNotifies();

}
