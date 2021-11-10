package com.example.faststudy.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.faststudy.model.Diary

@Dao
interface DiaryDao{
    @Query("SELECT * FROM diary_data")
    fun getAll():List<Diary>

    @Insert
    fun insertAll(vararg diary: Diary)

    @Delete
    fun delete(diary: Diary)
}