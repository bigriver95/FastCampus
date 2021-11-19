package com.example.faststudy

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat

class AlarmReceiver: BroadcastReceiver() {

    companion object{
        const val NOTIFICATION_CHANNEL_ID = "1000"
        const val NOTIFICATION_ID = 100
    }
    // 실제로 null이 내려오지 않기 때문에 '?'을 뺌
    override fun onReceive(context: Context, intent: Intent?) {

        createNotificationChannel(context)
        notifyNotification(context)
    }



    private fun createNotificationChannel(context: Context) {
        //context 하는일이 안드로이드 앱이 환경에서 글로벌 정보나 안드로이드 api난 시스템이 관리하는 정보
        // ex) 쉐어드 프리퍼런스, 리소스 파일에 접근한다거나 필요한 객체입니다.
        // 액티비트 자체가 context라고 할 수 있음
        //Broadcast에서 백그라운드에서 가져오는 개념이라 this를 사용 할 수 없음
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            // 크거나 같으면 notificationChannel이  같은것이기 때문에
            val notificationChannel = NotificationChannel(
                "1000",
                "기상 알림",
                //매우 중요한 알람이니까
            NotificationManager.IMPORTANCE_HIGH
            )
            NotificationManagerCompat.from(context).createNotificationChannel(notificationChannel)
        }
    }

    private fun notifyNotification(context: Context) {
        with(NotificationManagerCompat.from(context)){
            val build = NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID)
                .setContentTitle("알림")
                .setContentText("주인님 일어나세요")
                .setSmallIcon(R.drawable.ic_notifications)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
            notify(NOTIFICATION_ID, build.build())

        }
    }

}