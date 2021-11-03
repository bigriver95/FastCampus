package com.example.faststudy

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.faststudy.dao.HistoryDao
import com.example.faststudy.model.History

@Database(entities = [History::class],version = 1,exportSchema = false)
abstract class HistoryDatabase : RoomDatabase(){
    abstract fun historyDao():HistoryDao

    companion object {
        private var instance: HistoryDatabase? = null

        @Synchronized
        fun getInstance(context: Context): HistoryDatabase? {
            if (instance == null) {
                instance = Room.databaseBuilder(
                    context.applicationContext,
                    HistoryDatabase::class.java,
                    "database-contacts"
                )
                    .allowMainThreadQueries()
                    .build()
            }
            return instance
        }
    }

}