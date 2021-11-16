package com.example.faststudy

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.faststudy.model.Quotes

class QuotesPagerAdapter(
    private val quotes: List<Quotes>,
    private val isNameRevealed: Boolean

): RecyclerView.Adapter<QuotesPagerAdapter.QuoteViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        QuoteViewHolder (
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_quote, parent, false)
        )


    override fun onBindViewHolder(holder: QuoteViewHolder, position: Int) {

        val acutualPostion = position % quotes.size
        holder.bind(quotes[acutualPostion], isNameRevealed)
    }

    override fun getItemCount() = Int.MAX_VALUE


    class QuoteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){

        private val quotTextView : TextView = itemView.findViewById(R.id.quoteTextView)
        private val nameTextView : TextView = itemView.findViewById(R.id.nameTextView)
        @SuppressLint("SetTextI18n")
        fun bind(quotes: Quotes, isNameRevealed: Boolean){

            quotTextView.text = "\"${quotes.quote}\""

            if(isNameRevealed){
                nameTextView.text = quotes.name
                nameTextView.visibility = View.VISIBLE
            }else{
                nameTextView.visibility = View.GONE
            }

        }
    }

}