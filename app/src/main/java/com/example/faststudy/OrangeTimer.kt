package com.example.faststudy

import android.media.SoundPool
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.widget.SeekBar
import android.widget.TextView
import kotlinx.android.synthetic.main.orange.*

class OrangeTimer : AppCompatActivity() {

    private var countDownTimer: CountDownTimer? = null
    // 앱을 시작하자 말자 바로 실행 하는 게 아님 타이머를 컨트롤 할때 실행 하는 것 이기 때문에 처음은 null 값으로

    private val soundPool = SoundPool.Builder().build()
    private var tickingSoundId: Int? = null
    private var bellSoundId: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.orange)

        bindView()
        initSound()

    }

    override fun onResume() {
        super.onResume()
        soundPool.autoResume()
    }

    override fun onPause() {
        super.onPause()
        soundPool.autoPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        soundPool.release()
    }
    private fun bindView() {

        // 씨크바와 텍스트 뷰 연결
        timer_op.setOnSeekBarChangeListener(
            object : SeekBar.OnSeekBarChangeListener{
                override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                    if(fromUser) {
                        updateRemainTime(progress * 60 * 1000L)
                    }
                    // minutes_tv.text= "%02d".format(progress)
                    // minutes_tv.text= "$progress" 을 한자리수도 0을 붙이고 싶을때 윗방법
                    //사용자가 움직여서 나온 값인지?
                    //코드 상에서 값을 줘서 나온 값인지?
                }
                override fun onStartTrackingTouch(seekBar: SeekBar?) {
                    countDownTimer?.cancel()
                    countDownTimer = null
                }
                override fun onStopTrackingTouch(seekBar: SeekBar?) {

                    //todo 한번 실행 후 다시 실행 할때 타이머를 멈추고 다음 타이머 설정을 기다리게 해야 함
                    seekBar ?: return
                    // 좌측에있는 값이 널 일경우 우측을 리턴한다.
                    countDownTimer(timer_op.progress * 60  * 1000L).start()
                    countDownTimer?.start()
                    tickingSoundId?.let{ soundId ->
                        soundPool.play(soundId, 1F,1F,0, -1, 1F)
                    }

                }
            }
        )
    }
    private fun initSound(){
        tickingSoundId = soundPool.load(this,R.raw.timer_ticking,1)
        bellSoundId = soundPool.load(this,R.raw.timer_bell,1)
    }

    // 카운터다운 타이머
    private fun countDownTimer(initialMillis: Long) =
        object: CountDownTimer(initialMillis, 1000L){
            override fun onTick(millisUntilFinished: Long) {
                updateRemainTime(millisUntilFinished)
                updateSeekBar(millisUntilFinished)

            }

            override fun onFinish() {
                completeCountDown()


            }
        }

    private fun completeCountDown(){
        updateRemainTime(0)
        updateSeekBar(0)

        soundPool.autoPause()
        bellSoundId?.let { soundId ->
            soundPool.play(soundId, 1F,1F,0,0,1F)
        }
    }

    // 기본 UI에서 분과 초를 갱신하는 메서드
    private fun updateRemainTime(remainMillis: Long){

        val reminSconds = remainMillis /1000

        minutes_tv.text = "%02d".format(reminSconds / 60)
        seconds_tv.text = "%02d".format(reminSconds % 60)

    }
    // 1분이 지날때 마다 한칸씩 줄어드는 메서드
    private fun updateSeekBar(remainMillis:Long){
        timer_op.progress = (remainMillis / 1000 / 60).toInt()
    }
    }
