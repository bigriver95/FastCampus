package com.example.a

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.PendingIntent.FLAG_UPDATE_CURRENT
import android.content.Context
import android.content.Intent
import android.os.Build
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.faststudy.Alarm
import com.example.faststudy.NotificationType
import com.example.faststudy.Quote
import com.example.faststudy.R
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class MyFirebaseMessagingService : FirebaseMessagingService() {

    // 토큰은 언제든 변경이 쉽게 될 수 있음 1)새 기기에 앱 복원 2)사용자가 앱 삭제/ 재설치 3)사용자가 앱 데이터 소거
    // 실제 라이브에서는 무리가 있을 수 있음 onNewToken : 호출 될때마다 해당 토큰을 갱싱 해주는 처리를 해줘야함
    override fun onNewToken(p0: String) {
        super.onNewToken(p0)
    }


    // 파이어베이스 메시지를 수신할때 마다 이것을 호출함 모든 처리를 여기서함.
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)

        createNotificationChannel()
        //enum과 이름이 맞으면 동일한 값을 전달해준다.
        val type = remoteMessage.data["type"]
            ?.let{NotificationType.valueOf(it)}
        val title = remoteMessage.data["title"]
        val message = remoteMessage.data["message"]

        // type이 안맞아서 error가 발생할 수 있고 데이터가 없을 수 있기 때문에
        type ?: return


        NotificationManagerCompat.from(this)
            .notify(type.id, createNotification(type, title, message))

    }

    private fun createNotificationChannel(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val channel = NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_DEFAULT)
            // 버전 마다 중요도가 다르기 때문에 좀 봐야함 특히 알림 부분!!

            channel.description = CHANNEL_DESCRIPTION

            (getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager)
                .createNotificationChannel(channel)
            //채널이 생성 되지 않았음 아직
            //토큰을 복사해서 넣어서 실행을 시켜주면
        }
    }

    private fun createNotification(
        type : NotificationType,
        title:String?, message:String?
    ): Notification {

        val intent = Intent(this, Alarm::class.java).apply {
            putExtra("notificationType","${type.title}타입")
            addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
            // 기존의 액티비티를 쌓는 방식은 스텐다드 방식 ->
            // B의 화면이 떠있는 상태에서 또 B의 화면이 갱신 되면 이상하게 보일 수 있기 때문에
        }

        //FLAG_UPDATE_CURRENT 는 PendingIntent 정의된 상수임
        //PendingIntent = 누군가에게 Intent를 다룰 수 있는 권한을 주는거
        val pendingIntent = PendingIntent.getActivity(this, type.id, intent, FLAG_UPDATE_CURRENT)

        val notificationBuilder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_notifications)
            .setContentTitle(title)
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            //메세지를 탭을 하게되면 자동으로 삭제하게 해줌
            .setAutoCancel(true)

        when(type){
            NotificationType.NORMAL -> Unit
            NotificationType.EXPANDABLE -> {
                notificationBuilder.setStyle(
                    NotificationCompat.BigTextStyle()
                        .bigText(
                            "🤦🏻🧖🏻🙅🏻👗 👘 🥻 🩴 🩱 🩲 🩳 👙 " +
                                    "👚 👛 👜 👝 🎒 👞 👟 🥾 🥿 👠 👡 🩰 👢 👑" +
                                    "\uD83D\uDC6F\uD83D\uDC6F\uD83D\uDC6F\uD83D\uDC6F\uD83D\uDC6F"

                )
                )
            }
            NotificationType.CUSTOM -> {
                notificationBuilder.setStyle(
                    NotificationCompat.DecoratedCustomViewStyle())
                    .setCustomContentView(
                        RemoteViews(
                            packageName,
                            R.layout.view_custom_notification
                        ).apply {
                            setTextViewText(R.id.title_notification, title)
                            setTextViewText(R.id.message_notification, message)
                        }
                    )
            }
            }

        return  notificationBuilder.build()
        }




    companion object{
        private const val CHANNEL_NAME = "Emoji Party"
        private const val CHANNEL_DESCRIPTION = "Emoji Party를 위한 채널"
        private const val CHANNEL_ID = "Channel ID"
    }
}