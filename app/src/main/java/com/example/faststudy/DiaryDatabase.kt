package com.example.faststudy

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Diary::class],version = 1,exportSchema = false)
abstract class DiaryDatabase : RoomDatabase(){
    abstract fun diaryDao():DiaryDao

    companion object {
        private var instance: DiaryDatabase? = null

        @Synchronized
        fun getInstance(context: Context): DiaryDatabase? {
            if (instance == null) {
                instance = Room.databaseBuilder(
                    context.applicationContext,
                    DiaryDatabase::class.java,
                    "database-contacts"
                )
                    .allowMainThreadQueries()
                    .build()
            }
            return instance
        }
    }

}