package com.example.faststudy

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import kotlinx.android.synthetic.main.photo_frame.*
import java.util.*
import kotlin.concurrent.timer

class PhotoFrame : AppCompatActivity() {

    private var currentPosition = 0

    private val photoList = mutableListOf<Uri>()

    private val timer: Timer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.photo_frame)

        Log.d("PhotoFrame", "실행")
        photoTimer()
        getPhotoUriIntent()
        // 코드 추상화를 시켜주고 실질적으로 무엇을 하는지는 직접 클릭해서 보이게 하는것이 가장 좋다!!!

        /*lifecycle
         onCreate = 액티비티가 런치가 되었을때 처음으로 호출된 함수이다~ 메인 함수당
         onResume = onPause 에서 돌아 왔을때
         onPause = 다른 앱을 보는 상황일때
         onStop = 꺼지기 전단계
         onDesstory = 꺼지는 단계*/



    }

    private fun photoTimer() {

        kotlin.concurrent.timer(period = 5 * 1000){
            runOnUiThread{
                val current = currentPosition
                val next = if(photoList.size <= currentPosition + 1) 0 else currentPosition + 1
                //리스트 사이즈가 포지션 사이즈+ 1 보다 작거나 같으면 0을 아니면 +1을 봔한 시켜라
                //그렇다면 컬렌트포지션이란 무엇인가???

                Log.d("PhotoFrame", "5초 지나감")
                photo_background.setImageURI(photoList[current])
                photo_view.alpha = 0f
                photo_view.setImageURI(photoList[next])
                photo_view.animate()
                    .alpha(1.0f)
                    .setDuration(1000)
                    .start()

                currentPosition = next

            }
        }

    }

    private fun getPhotoUriIntent(){
        val size = intent.getIntExtra("photoListSize",0)
        for( i  in 0.. size){
            intent.getStringExtra("photo$i")?.let{
                photoList.add(Uri.parse(it))
            }
        }
    }

    override fun onStop() {
        super.onStop()
        Log.d("PhotoFrame", "취소")
        timer?.cancel()
    }

    override fun onStart() {
        super.onStart()
        Log.d("PhotoFrame", "시작")
        photoTimer()
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("PhotoFrame", "종료")
        timer?.cancel()
    }

}