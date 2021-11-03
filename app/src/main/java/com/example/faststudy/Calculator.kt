package com.example.faststudy

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.core.text.buildSpannedString
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.room.Room
import com.example.faststudy.model.History
import kotlinx.android.synthetic.main.alculator.*
import kotlinx.android.synthetic.main.alculator.view.*
import java.lang.NumberFormatException

class Calculator : AppCompatActivity() {

    private var ww = false
    // ww = isOperator
    // 숫자를 클릭할때 연산자를 치다가 들어오는 경우
    // 띄어쓰기를 한번더 해줘야하기 때문에
    private var hasOperator = false
    // 오퍼레이터를 하나만 써여하기때문에

    lateinit var db: HistoryDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.alculator)

        db = Room.databaseBuilder(
            applicationContext,
            HistoryDatabase::class.java,
            "historyDB"
        ).build()
    }

    fun buttonClick(v: View){ //여러개의 숫자와 연산자
        when(v.id){
            R.id.bt_0 -> numberBtClick("0")
            R.id.bt_1 -> numberBtClick("1")
            R.id.bt_2 -> numberBtClick("2")
            R.id.bt_3 -> numberBtClick("3")
            R.id.bt_4 -> numberBtClick("4")
            R.id.bt_5 -> numberBtClick("5")
            R.id.bt_6 -> numberBtClick("6")
            R.id.bt_7 -> numberBtClick("7")
            R.id.bt_8 -> numberBtClick("8")
            R.id.bt_9 -> numberBtClick("9")
            R.id.bt_plus -> operatorBtClick("+")
            R.id.bt_minus -> operatorBtClick("-")
            R.id.bt_multiply -> operatorBtClick("*")
            R.id.bt_division -> operatorBtClick("/")
            R.id.bt_modulo -> operatorBtClick("%")


        }
    }

    private fun numberBtClick(number:String){

        if (ww){
            //버튼을 눌렀는데 number가 아닐때 (즉 : 연산자 일때)
            expression_tv.append(" ")
        }
        ww = false //다시 false로 만들어준다.


      val expressionText =  expression_tv.text.split(" ")
        //split 구분자를 통해서 각각 띄어서 배열로 저장할 수 있게 도와주는 함수
        // 중간 중간 띄어놓으면 숫자 연산자 숫자 중간에 스플릿을 넣어서

        if(expressionText.isNotEmpty()&&expressionText.last().length>= 15){
            //공백이 아닌데 글자수가 15이상이면 Toast출력
            //isEmpty 텍스트가 ""공백일때 true " " false로 반환시켜준다
            Toast.makeText(this,"15자리까지만 사용 할 수 있엉",Toast.LENGTH_SHORT).show()
            return // 15를 넘으려고 하면 못넘게 리턴 시킨다

        }else if (expressionText.last().isEmpty()&& number == "0"){
            //마지막이 비워있는데 숫자가 0인경우
            Toast.makeText(this,"0은 제일 앞에 올 수 없엉",Toast.LENGTH_SHORT).show()
            return
        }
        expression_tv.append(number)
        // TODO resultTextView 실시간으로 계산 결과를 넣어야 하는 기능
        result_tv.text = calculateExpression()
    }

    private fun operatorBtClick(operator:String){
        if(expression_tv.text.isEmpty()){
            return
            //공백일때 이버튼을 누르면 아무 작동이 되지않게 만든다
        }

        when{ //연사자를 썼는데 또 쓰는 경우
            ww->{ //TODO OK TRUE라고 해 위에꺼랑 밑에꺼 차이가 뭔데
                val text = expression_tv.text.toString()
                expression_tv.text = text.dropLast(1) + operator
                //맨끝에 한자리를 지워준다 + 그리고 다시 연산자를 넣어줌
            }
            hasOperator -> { //TODO 왜 true가 되는건댜???
                Toast.makeText(this, "연산자는 한번만 사용 가능.",Toast.LENGTH_SHORT).show()
                return
            }

            else-> {
                expression_tv.append(" $operator")
            //둘다 false일 경우는 숫자만 입력하고 연산자를 한번도 입력 안한 경우임
            }
        }

        val ssb = SpannableStringBuilder(expression_tv.text)
        //연산자일경우 초록색으로 변경하는 방법

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            ssb.setSpan(
                ForegroundColorSpan(getColor(R.color.green)),
                expression_tv.text.length -1, //연산자 하나만 오는 경우라서 마지막에 하나만
                expression_tv.text.length, //
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        }

        expression_tv.text = ssb
        ww = true
        hasOperator = true


    }
    fun resultBt(v:View){
        val expressionTexts = expression_tv.text.split(" ")
        if(expression_tv.text.isEmpty() || expressionTexts.size == 1){
            return
        }
        if(expressionTexts.size != 3 && hasOperator){
            Toast.makeText(this, "아직 완성 되지 않은 수식이야",Toast.LENGTH_SHORT).show()
            return
            //숫자와 연산자만 입력한 경우
        }
        if (expressionTexts[0].isNumber().not() ||expressionTexts[2].isNumber().not()){
           Toast.makeText(this, "오류가 발생했엉",Toast.LENGTH_SHORT).show()
            return
        }

        val exceptionText = expression_tv.text.toString()
        val resultText = calculateExpression()

        //todo 디비에 넣어줘야하는 부분
        Thread(Runnable { db.historyDao().insertAll(History(null,exceptionText,resultText)) }).start()


        result_tv.text = ""
        expression_tv.text = resultText

        ww = false
        hasOperator = false
        //연산이 끝났기 때문에 false를 준다

    }

    fun clearBt(v:View){
        result_tv.text = ""
        expression_tv.text = ""
        hasOperator = false
        ww = false
    }

    fun historyBt(v:View){
        history_text.isVisible = true
        history_layout.removeAllViews()

        Thread(Runnable { db.historyDao().getAll().reversed().forEach{

            runOnUiThread{
                val historyView = LayoutInflater.from(this).inflate(R.layout.history_row, null, false)
                historyView.findViewById<TextView>(R.id.expression_tv).text = it.expression
                historyView.findViewById<TextView>(R.id.result_tv).text = "= ${it.result}"

                history_layout.addView(historyView)

            }

        } }).start()


        //TODO 디비에서 모든 기록 가져오기
        //TODO 뷰에 모든 기록 할당하기
    }

    fun closeHistoryButton(v:View){
        history_text.isGone = true

    }

    fun clearHistoryButton(v :View){
        history_layout.removeAllViews()
        Thread(Runnable {
            db.historyDao().deleteAll()
        }).start()
        //TODO 모든 기록 삭제
        //TODO 뷰에서 모든 기록 삭제


    }

    private fun calculateExpression(): String{
        //숫자와 연사자 숫자 결과를 가져와서 result_tv에 넣기위해서 반환하는 함수
        val expressionTexts = expression_tv.text.split(" ")
        if(hasOperator.not() || expressionTexts.size != 3){
            //아직 연산자를 누른 적이 없거나 || 무조건 숫자 + 연산자 + 숫자가 있어야함
            return ""
            //예외가 발생함
        }else if (expressionTexts[0].isNumber().not() ||expressionTexts[2].isNumber().not()){
            //첫번째 숫자가 아닐 경우 숫자로 치환했을때 에러가 나올 수 있음
            // 아 결과 값이 연산자가 나올 수 있을 경우를 말하는 것 아닐까?
            return ""
        }
        val exp1 = expressionTexts[0].toBigInteger()
        val exp2 = expressionTexts[2].toBigInteger()
        val op = expressionTexts[1]

        return when (op){
            "+" -> (exp1 + exp2).toString()
            "-" -> (exp1 - exp2).toString()
            "*" -> (exp1 * exp2).toString()
            "/" -> (exp1 / exp2).toString()
            "%" -> (exp1 % exp2).toString()
            else -> ""
        }
               }
    //TODO 이거 다시 이해하자
    fun String.isNumber(): Boolean {
        //Boolean 은 논리 자료형 ture와 false로 나눌 수 있음 그니까
        //문자열 isnumber함수는 true 또는 false로 나누는 거임
        return try{

            this.toBigInteger()
            // this = String을 뜻함
            //toBigInteger은 21억이 넘는 수까지 지원
            return true
        } catch (e: NumberFormatException){
            false
            //ToBigInteger null일 경우 NullPointerException을 반환
            //null이 아닐경우는 NumberFormatException을 반환
        }

    }
    //TODO 이것도 다시 이해하자






}