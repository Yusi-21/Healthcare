package com.mirea.healthcare;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import java.util.List;

@Dao
public interface MessageDao {
    @Insert
    void insert(Message message);

    @Query("SELECT * FROM message WHERE userId = :userId ORDER BY timestamp ASC")
    List<Message> getMessagesByUser(int userId);

    @Query("DELETE FROM message WHERE userId = :userId")
    void clearMessagesForUser(int userId);

    @Delete
    void delete(Message message);
}
