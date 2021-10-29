package com.example.faststudy

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import kotlinx.android.synthetic.main.lotto.*
import kotlinx.android.synthetic.main.lotto.view.*

/*NumberPicker 을 사용
*Shape Drawable 사용 : 코드로 수정하는 방식으로 xml넣는것보다 용량이 작음 수정이 빠름*/
class Lotto : AppCompatActivity() {

    private var didRun = false
    // 뭐가
    private val pickNumberSet = hashSetOf<Int>()
    //
    private val numberTextViewList: List<TextView> by lazy {
        listOf<TextView>(
                lotto_number1,
                lotto_number2,
                lotto_number3,
                lotto_number4,
                lotto_number5,
                lotto_number6
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.lotto)

        lotto_picker.minValue =1
        lotto_picker.maxValue =45
        // 사실 코드를 정리하기위해서는 처음부터 함수로 정릴하는게 편하지 않을까?



        //하나씩 실행
        lotto_select.setOnClickListener{
            if(didRun){
                //뭐가 false 인거야?? 그냥 넣은건가??
                Toast.makeText(this,"더 이상 추가 할 수 없습니다 \n리셋을 눌러주세요",Toast.LENGTH_SHORT).show()
                return@setOnClickListener}
            if(pickNumberSet.size >= 6){
                Toast.makeText(this,"번호는 5개까지만 선택할 수 있습니다.",Toast.LENGTH_SHORT).show()
            return@setOnClickListener}
            if(pickNumberSet.contains(lotto_picker.value)){ Toast.makeText(this,"이미 선택된 숫자 입니다.",Toast.LENGTH_SHORT).show()
                return@setOnClickListener}

            val textView = numberTextViewList[pickNumberSet.size]
            //size가 현재 위치를 이야기하느겠죠?
            textView.isVisible =true
            textView.text = lotto_picker.value.toString()

            setNumberBackground(lotto_picker.value,textView)
            when(lotto_picker.value){
                in 1..10 -> textView.background = ContextCompat.getDrawable(this, R.drawable.number_ball_y)
                in 11..20 -> textView.background = ContextCompat.getDrawable(this, R.drawable.number_ball_b)
                in 21..30 -> textView.background = ContextCompat.getDrawable(this, R.drawable.number_ball_r)
                in 31..40 -> textView.background = ContextCompat.getDrawable(this, R.drawable.number_ball_g)
                else -> textView.background = ContextCompat.getDrawable(this, R.drawable.number_ball_gr)
            }


            pickNumberSet.add(lotto_picker.value)
        }
        //나머지 값 랜덤 실행
        lotto_random.setOnClickListener{
            val list = randomList()
            Log.d("Lotto",list.toString())

            didRun = true

            list.forEachIndexed{ index, number ->
                //인덱스와 번호가 둘다 가져올 수 있게
                val textView = numberTextViewList[index]
                setNumberBackground(number, textView)

                textView.text = number.toString()
                textView.isVisible = true
            }
        }
        //초기화
        lotto_reset.setOnClickListener{
            pickNumberSet.clear()
            numberTextViewList.forEach{
                //forEach = 순차적으로 하나하나 꺼내주는 값
                it.isVisible = false
                //다시 하나씩 숨긴다
            }
            didRun =false

        }

    }
    //랜덤 백그라운드 중복사용이라
    private fun setNumberBackground(number:Int, textView: TextView){
        when(number){
            in 1..10 -> textView.background = ContextCompat.getDrawable(this, R.drawable.number_ball_y)
            in 11..20 -> textView.background = ContextCompat.getDrawable(this, R.drawable.number_ball_b)
            in 21..30 -> textView.background = ContextCompat.getDrawable(this, R.drawable.number_ball_r)
            in 31..40 -> textView.background = ContextCompat.getDrawable(this, R.drawable.number_ball_g)
            else -> textView.background = ContextCompat.getDrawable(this, R.drawable.number_ball_gr)
        }
    }

    private fun randomList(): List<Int> {
        val numberList = mutableListOf<Int>()
                .apply {
                    for (i in 1..45){
                        if(pickNumberSet.contains(i)){
                            continue
                        }

                        this.add(i)
                    }
                }
            numberList.shuffle()
        val newList = pickNumberSet.toList() + numberList.subList(0,6 - pickNumberSet.size)
        return newList.sorted()
        //sorted() 리스트에 있는 숫자를 정렬하는 개념
    }
}