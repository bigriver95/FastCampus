package com.example.faststudy

import android.app.AlarmManager
import android.app.PendingIntent
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.faststudy.model.AlarmDisplayModel
import kotlinx.android.synthetic.main.activity_alarm.*
import java.util.*
import kotlin.time.hours

/*Alarm Bacground 작업 > 앱을 닫을때도 실행이 가능하도록 만들어야함.
  Immediate tasks 즉시 실행하는 작업
  Thread
  Handler
  kotlin coroutines

  Deferred tasks 지연된 작업
    - WorkManager
  Exact tasks 정시에 실행하는 작업
    - AlarmManger

    AlarmManager
    - Real Time(실제시간)으로 실행시키는 방법
    - Elapsed Time(기기가 부팅 된지부터 얼마나 지났는지)으로 실행시키는 방법

    알림앱
    지정된 시간에 알림이 울리게 할 수 있음
    지정된 시간 이후에는 매일 같은 시간에 반복되게 알림이 울리게 할 수 있음.*/

class Alarm : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_alarm)

        // step0 뷰를 초기화해주기
        initOnOffButton()
        initChangeAlarmTimeButton()

        val model = fetchDataFromSharedPreferences()
        renderView(model)

        // step1 데이터를 가져오기
        // step2 뷰에 데이터를 그려주기

    }



    private fun initOnOffButton() {
        onOff_bt.setOnClickListener {
            // 저장한 데이터를 확인한다
        val model = it.tag as? AlarmDisplayModel ?: return@setOnClickListener
            val newModel = saveAlarmModel(model.hour,model.minute, model.onOff.not() )
            renderView(newModel)

            if(newModel.onOff){
             //켜진경우 -> 알람을 등록
                val calender = Calendar.getInstance().apply {
                    set(android.icu.util.Calendar.HOUR_OF_DAY, newModel.hour)
                    set(android.icu.util.Calendar.MINUTE, newModel.minute)
                    if(before(Calendar.getInstance())){
                        add(Calendar.DATE, 1)
                    }
                }

                val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
                val intent = Intent(this, AlarmReceiver::class.java)
                val PendingIntent = PendingIntent.getBroadcast(this, ALARM_REQUEST_CODE,
                    intent, PendingIntent.FLAG_UPDATE_CURRENT)

                alarmManager.setInexactRepeating(
                    AlarmManager.RTC_WAKEUP,
                    calender.timeInMillis,
                    //하루있다가 실행 됨
                    AlarmManager.INTERVAL_DAY,
                    PendingIntent
                //REALTIME_WAKEUP은 핸드폰을 켜졌을때부터의 시간
                )
            }else{
                canceledAlarm()
                // 알람을 제거

            }
            // 온오프에 따라 작업을 처리한다
            // 오프 -> 알람을 제거
            // 온 -> 알람을 등록
            // 데이터를 저장한다.
        }
    }

    private fun initChangeAlarmTimeButton() {
     changeAlarm_bt.setOnClickListener {

         val calendar = Calendar.getInstance()
         //TimePickDialog 띄어줘서 시간을 설정을 하도록 하게끔 하고, 그 시간을 가져와서 TODO 데이터를 저장기능!!
         TimePickerDialog(this, { picker, hour, minute ->

             // TODO 데이터를 보여주는 기능
             val model = saveAlarmModel(hour,minute, false)
             renderView(model)

             // 저장을하고 모델을 구성하는 함수까지 처리해줬다.
             // TODO 이미등록된 알람이 있다면 취소 하는 기능
             canceledAlarm()
             // pendingIntent를 가져오는 부분에 알람이 등록이 되어 있지 않다면 널일 수 도 있기에


         },calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), false).show()

         // 뷰를 업데이트 한다
         // 기존에 있던 알람을 삭제한다.
     }


    }

    private fun saveAlarmModel(
        hour : Int,
        minute: Int,
        onOff:Boolean
    ): AlarmDisplayModel {

        val model = AlarmDisplayModel(
            hour = hour,
            minute = minute,
            onOff = onOff)
        val sharedPreferences = getSharedPreferences(SHARED_TIME_KEY, Context.MODE_PRIVATE)

        // 같이 작성할 수 있는 경우를 묶어서 해보았다.
        with(sharedPreferences.edit()){
            putString(SHARED_TIME_KEY, model.makeDataForDB())
            putBoolean(ONOFF_KEY, model.onOff)
            commit()
        }


        return model
    }

    private fun fetchDataFromSharedPreferences():AlarmDisplayModel {
        val sharedPreferences = getSharedPreferences(SHARED_TIME_KEY, Context.MODE_PRIVATE)
        val timeDBValue = sharedPreferences.getString(ALARM_KEY, "9:30") ?: "9:30"
        //String은 null 값이 존재 할 수 있음
        val onOffDBValue = sharedPreferences.getBoolean(ONOFF_KEY, false)
        //스플릿을 통해서 split을 쓰면 list형태로 바뀌게 되는데 ":" 앞에는 hour로 뒤에는 ":" mintue로
        val alarmData = timeDBValue.split(":")

        val alarmModel = AlarmDisplayModel(
            hour = alarmData[0].toInt(),
            minute = alarmData[1].toInt(),
            onOff = onOffDBValue
        )
        //보정 보정

        //Intent를 보낼때
        val pendingIntent = PendingIntent.getBroadcast(this, ALARM_REQUEST_CODE,
            Intent(this, AlarmReceiver::class.java),
            //FLAG_NO_CREATE = 있으면 가져오고 없으면 안만들 거임!! 라는 기능
            PendingIntent.FLAG_NO_CREATE)
        // 보정 보정 예외처리
        // 알람은 꺼져있는데, 데이터는 켜져 있는 경우
        if((pendingIntent == null) and alarmModel.onOff) {
            alarmModel.onOff = false
        // 알람은 켜져있는데, 데이터는 꺼져있는 경우
            //알람을 취소함
        }else if ((pendingIntent != null) and alarmModel.onOff.not()){
            pendingIntent.cancel()
        }
        return alarmModel
    }


    private fun renderView(model: AlarmDisplayModel) {
        ampmTextView.apply {
            text = model.ampmText
        }
        timeTextView.apply {
            text = model.timeText
        }
        onOff_bt.apply{
            text = model.onOffText
            tag = model
        }
    }

    private fun canceledAlarm(){
        val pendingIntent = PendingIntent.getBroadcast(this, ALARM_REQUEST_CODE,
            Intent(this, AlarmReceiver::class.java),
            //FLAG_NO_CREATE = 있으면 가져오고 없으면 안만들 거임!! 라는 기능
            PendingIntent.FLAG_NO_CREATE)
        pendingIntent?.cancel()

    }
    companion object{
        private const val ALARM_KEY = "alarm"
        private const val ONOFF_KEY = "onOff"
        private const val SHARED_TIME_KEY = "time"
        private const val ALARM_REQUEST_CODE = 1000
    }

}