package com.example.learning

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import kotlinx.android.synthetic.main.activity_main.*

/**
 * 参考：
 * https://developer.android.com/guide/topics/ui/notifiers/notifications?hl=zh-cn
 * https://developer.android.com/training/notify-user/build-notification
 */
class NotificationActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notification)
        addActivityButton(Runnable {

            val channelId = "chanel01"
            val notificationManager = (getSystemService(Context.NOTIFICATION_SERVICE) as? NotificationManager)

            //8.0以及以上系统 必须要创建channel用来表示某一类通知。否则通知弹不出来
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val channel = NotificationChannel(channelId, "chanel01_name", NotificationManager.IMPORTANCE_HIGH)
                notificationManager?.createNotificationChannel(channel)
            }

            val pendingIntent01 = PendingIntent.getActivity(this, 1, Intent(this, NotificationActivity::class.java), PendingIntent.FLAG_UPDATE_CURRENT)
            val pendingIntent02 = PendingIntent.getBroadcast(this, 1, Intent("action broadcast action"), PendingIntent.FLAG_UPDATE_CURRENT)
            val pendingIntent03 = PendingIntent.getBroadcast(this, 1, Intent("content broadcast action"), PendingIntent.FLAG_UPDATE_CURRENT)


            val notifyId = 1 //用同一个id弹出多次通知，则后弹出的通知覆盖前面通知的内容，列表里最终只显示最新的一条通知
            val notification = NotificationCompat.Builder(this, channelId)
                    .setSmallIcon(R.mipmap.ic_launcher) //必须设置项。否则 IllegalArgumentException: Invalid notification (no valid small icon)
                    .setContentTitle("标题")
                    .setContentText("内容" + System.currentTimeMillis())
                    .setContentIntent(pendingIntent03)
//                    .setFullScreenIntent(pendingIntent03, true)
                    .addAction(R.mipmap.ic_launcher, "action01", pendingIntent01)
                    .addAction(R.mipmap.ic_launcher, "action02", pendingIntent02)
                    .setAutoCancel(true) //点击通知后 自动消失
                    .build()
            notificationManager?.notify(notifyId, notification)
        }, "创建")
    }


    private fun addActivityButton(task: Runnable,  description: String) {
        val button = Button(this)
        button.text = description
        button.setOnClickListener {
            task.run()
        }
        ll_content?.addView(button)
    }
}