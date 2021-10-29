package com.example.faststudy

import android.content.Context
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.edit
import kotlinx.android.synthetic.main.activity_secret.*
import kotlin.math.max
import kotlin.math.min

class Secret : AppCompatActivity() {

    private var ChangePaswordMode = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_secret)

        password_1.apply {
            minValue = 0
            maxValue = 9
        }
        password_2.apply {
            minValue = 0
            maxValue = 9
        }
        password_3.apply {
            minValue = 0
            maxValue = 9

            password_4.apply {
                minValue = 0
                maxValue = 9
            }
        }
        password_4.apply {
            minValue = 0
            maxValue = 9

            password_4.apply {
                minValue = 0
                maxValue = 9
            }
        }

        open_bt.setOnClickListener {
            if(ChangePaswordMode){ Toast.makeText(this,"비밀번호 변경 중",Toast.LENGTH_SHORT).show()
                return@setOnClickListener }

            val passwordPrefernces = getSharedPreferences("password",Context.MODE_PRIVATE)
            val passwordUser = "${password_1.value}${password_2.value}${password_3.value}${password_4.value}"

            if(passwordPrefernces.getString("password","0000").equals(passwordUser)){
            }else{
                FailDialog()
            //실패
            }

        }

        change_bt.setOnClickListener {
            val passwordPreferences = getSharedPreferences("password",Context.MODE_PRIVATE)
            val passwordUser = "${password_1.value}${password_2.value}${password_3.value}${password_4.value}"
        if(ChangePaswordMode){
            passwordPreferences.edit(true){
                //edit을 저장하는 법은 두가지가 있는데
                //commit : 저장될때까지 기다린다.
                //apply : 그냥 저장하라고 넘긴다.
                putString("password",passwordUser)
            } //번호를 저장하는 기능

            ChangePaswordMode = false
            change_bt.setBackgroundColor(Color.BLACK)

        }else{
            //chahgePasswordMode 가 활성화 :: 비밀번호가 맞는지를 체크
            if(passwordPreferences.getString("password","0000").equals(passwordUser)){
                ChangePaswordMode = true
                Toast.makeText(this,"변경할 패스워드를 입력해주세요",Toast.LENGTH_SHORT).show()
                change_bt.setBackgroundColor(Color.RED)
            }else{
                FailDialog()
                //실패
            }
        }
        }

    }

    private fun FailDialog(){
        AlertDialog.Builder(this)
            .setTitle("실패!!")
            .setMessage("비밀번호가 잘못되었습니다.")
            .setPositiveButton("확인"){ _, _ -> }
            .create()
            .show()
    }
}