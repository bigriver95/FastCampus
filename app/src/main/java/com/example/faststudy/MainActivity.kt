package com.example.faststudy

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    
    bmi_bt.setOnClickListener {
        val bmi = Intent(this,BmiCalculator::class.java)
        startActivity(bmi)
    }
    lotto_bt.setOnClickListener{
        val lotto = Intent(this,Lotto::class.java)
        startActivity(lotto)
    }
        secret_bt.setOnClickListener {
            val secret = Intent(this,Secret::class.java)
            startActivity(secret)
        }
        calculator_bt.setOnClickListener {
            val calculator = Intent(this,Calculator::class.java)
            startActivity(calculator)
        }
    }

}