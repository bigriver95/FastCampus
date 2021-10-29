package com.example.faststudy

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.bmi_result.*

class BmiResult : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.bmi_result)

        if(intent.hasExtra("bmi")) {
            val bb = intent.getStringExtra("bmi")
            bmi_re.text = "BMI 수치 = ${bb}"
        }
            if(intent.hasExtra("text"))
                bmi_re_text.text = intent.getStringExtra("text")
    }
}