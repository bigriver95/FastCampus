package com.example.faststudy

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class DiaryAdapter (private val itemList: MutableList<Diary>) : RecyclerView.Adapter<DiaryAdapter.DiaryVH>(){

    override fun getItemCount(): Int {
        return  itemList.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DiaryAdapter.DiaryVH {
        val inflated = LayoutInflater.from(parent.context).inflate(R.layout.diary_item,parent,false)
        return DiaryVH(inflated)
    }



    override fun onBindViewHolder(holder: DiaryAdapter.DiaryVH, position: Int){
        val item = itemList[position]


        holder.itemView.setOnClickListener {
            itemClickListener.onClick(it, position)
        }
        holder.itemView.setOnLongClickListener {
            itemClickListener.LongClick(it, position)
            return@setOnLongClickListener true
        }

        holder.apply {
            bind(item)
        }
    }

    interface OnItemClickListener {
        fun onClick(v: View, position: Int)
        fun LongClick(v: View, position: Int)
    }

    private lateinit var itemClickListener : OnItemClickListener
    fun setItemClickListener(itemClickListener: OnItemClickListener) {
        this.itemClickListener = itemClickListener
    }



    inner class DiaryVH(view: View) :RecyclerView.ViewHolder(view){
        private val mText: TextView = itemView.findViewById(R.id.memo_list)
        private val mTime: TextView = itemView.findViewById(R.id.time_list)

        fun bind(item: Diary){
            mText.text = item.text
            mTime.text = item.daily
        }
    }
}