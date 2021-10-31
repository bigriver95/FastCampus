package com.example.faststudy

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.secret_diary.*
import java.text.SimpleDateFormat
import java.util.*

class SecretDiary : AppCompatActivity() {

    val TAG = "ListActivity"
    var db : DiaryDatabase? = null //db 사용선언

    var diaryList = mutableListOf<Diary>()
    val adapter = DiaryAdapter(diaryList)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.secret_diary)
        //초기화
        db = DiaryDatabase.getInstance(this)

        val savedDiary = db!!.diaryDao().getAll()

        if(savedDiary.isNotEmpty()){
            diaryList.addAll(savedDiary)
        }

        //어댑터, 아이템 클릭 : 아이템 삭제

        adapter.setItemClickListener(object : DiaryAdapter.OnItemClickListener{

            override fun onClick(v: View, position: Int) {
                Toast.makeText(this@SecretDiary,"길게 누르면 삭제 됩니다",Toast.LENGTH_SHORT).show()
            }

            override fun LongClick(v: View, position: Int) {
                val diary = diaryList[position]
                Toast.makeText(this@SecretDiary,"삭제되었습니다.", Toast.LENGTH_SHORT).show()
                db?.diaryDao()?.delete(diary=diary)
                diaryList.removeAt(position)
                adapter.notifyDataSetChanged()

                Log.d(TAG, "remove item($position). name:${diary}")
            }
        })

        re_diary.adapter = adapter
        re_diary.layoutManager = LinearLayoutManager(this)

        diary_enter.setOnClickListener { DiaryAdd()}
    }

    private fun DiaryAdd() {
        val add = diary_text.text.toString()
        val time = System.currentTimeMillis()
        val dateFormat = SimpleDateFormat("aa hh:mm")
        val ct = dateFormat.format(Date(time)).toString()
        val diary = Diary(0,"${add}","${ct}")

        db?.diaryDao()?.insertAll(diary)
        diaryList.add(diary)


        diary_text.setText("")
        adapter.notifyDataSetChanged()
    }
}