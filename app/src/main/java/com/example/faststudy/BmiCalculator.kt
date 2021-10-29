package com.example.faststudy

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import kotlinx.android.synthetic.main.bmi_calculator.*
import kotlin.math.pow

class BmiCalculator : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.bmi_calculator)


        bmi_ok.setOnClickListener{

            if (stature.text.isEmpty() || weight.text.isEmpty()){
                Toast.makeText(this,"값을 입력해줘",Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val a = stature.text.toString().toInt()
            val b = weight.text.toString().toInt()
            val bmi = b/ (a / 100.0).pow(2.0)
            val text = when{
                bmi >= 35.0 -> "고도비만"
                bmi >= 30.0 -> "중정도 비만"
                bmi >= 25.0 -> "경도 비만"
                bmi >= 23.0 -> "과체중"
                bmi >= 18.5 -> "정상체중"
                else -> "저체중"
            }
            val intent = Intent(this, BmiResult::class.java)
            intent.putExtra("bmi",bmi.toString())
            intent.putExtra("text",text)
            startActivity(intent)





        }
            }
        }