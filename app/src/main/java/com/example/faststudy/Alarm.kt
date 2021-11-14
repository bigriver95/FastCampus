package com.example.faststudy

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.android.synthetic.main.alarm.*

class Alarm : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.alarm)

        initFirebase()
    }

    //파이어베이스 사용법
    private fun initFirebase(){
        FirebaseMessaging.getInstance().token
            .addOnCompleteListener { task ->
                if(task.isSuccessful){
                    firebaseToken.text = task.result }
            }
        /*notification 은안드로이드 8.0이상 일때만 채널을 만들어서 활동을 해줘야함
        * 모든 알림을 채널에 할당해야한다 메세지를 발송하기전에 생성되어야함
        * 채널을 반복해서 만들더라도 이미 채널이 생성된 상태라면 다시 생성되는 경우는 없기에 앱을 실행 할때 채널을 생성해야함
        * notification을 시작할때 호출을 하기 때문에 그 직전에 해주면 됨*/

        /* 채널을 만들때 중요도 설정
           -   8.0이상에 채널의 중요도
           -   7.0이하는 notify마다 중요도를 설정해줘야함


         */





    }

}