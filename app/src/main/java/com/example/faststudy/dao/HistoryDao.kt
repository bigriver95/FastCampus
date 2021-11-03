package com.example.faststudy.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.faststudy.model.History

@Dao
interface HistoryDao{
    @Query("SELECT * FROM history")
    fun getAll():List<History>

    @Insert
    fun insertAll(vararg history: History)

    @Query("DELETE FROM history")
    fun deleteAll()

}