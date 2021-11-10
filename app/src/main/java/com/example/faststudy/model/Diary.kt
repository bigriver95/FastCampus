package com.example.faststudy.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "diary_data")
data class Diary(
    @PrimaryKey(autoGenerate = true)
    val id:Long,
    var text: String,
    var daily: String

)
