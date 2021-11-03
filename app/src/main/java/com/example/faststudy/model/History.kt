package com.example.faststudy.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import javax.xml.xpath.XPathExpression

@Entity(tableName = "history")
data class History(
    @PrimaryKey(autoGenerate = true)
    val uid:Long?,
    @ColumnInfo(name="expression")
    var expression: String?,
    @ColumnInfo(name = "result")
    var result: String?
    // 각각 나뉘어서 테이블이 생성된다.

)
