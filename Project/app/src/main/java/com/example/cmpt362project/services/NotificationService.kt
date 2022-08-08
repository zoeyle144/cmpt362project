package com.example.cmpt362project.services

import android.app.PendingIntent
import android.content.Intent
import android.os.IBinder
import androidx.core.app.NotificationCompat
import androidx.lifecycle.LifecycleService
import com.example.cmpt362project.MainActivity
import com.example.cmpt362project.R
import com.example.cmpt362project.models.Board
import com.example.cmpt362project.models.Category
import com.example.cmpt362project.viewModels.ChangeNotificationListViewModel


class NotificationService : LifecycleService() {
    private lateinit var changeNotificationListViewModel: ChangeNotificationListViewModel
    private lateinit var boardList: List<Board>
    private lateinit var categoryList: List<Category>

    companion object {
        const val NOTIFICATION_ID = 777
        var isServiceRunning = false
    }

    override fun onCreate() {
        super.onCreate()
        changeNotificationListViewModel = ChangeNotificationListViewModel()
        boardList = ArrayList()
        categoryList = ArrayList()
        startServiceWithNotification()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)
        if (intent != null && intent.action == "Horizon Notification") {
            startServiceWithNotification()
        } else stopMyService()
        return START_STICKY
    }

    // In case the service is deleted or crashes some how
    override fun onDestroy() {
        isServiceRunning = false
        super.onDestroy()
    }

    override fun onBind(intent: Intent): IBinder? {
        super.onBind(intent)
        // Used only in case of bound services.
        return null
    }

    fun startServiceWithNotification() {
        if (isServiceRunning) return
        isServiceRunning = true

        changeNotificationListViewModel.fetchChangeNotifications("-N8nxMBOuplwJJx6iNFn")
        var pastFirst = false
        changeNotificationListViewModel.changeNotificationsLiveData.observe(this){
            if (pastFirst){
                val lastIt = it.size - 1
                val intentHomeFrag = Intent(this, MainActivity::class.java)
                val pendingIntent: PendingIntent = PendingIntent.getActivity(this, 0, intentHomeFrag, PendingIntent.FLAG_IMMUTABLE)
                val builder = NotificationCompat.Builder(this, "channel-777")
                    .setSmallIcon(R.drawable.ic_baseline_playlist_add_check_24)
                    .setContentTitle("Change Notification")
                    .setContentText("Last change is: ${it[lastIt].changeType}")
                    .setStyle(
                        NotificationCompat.BigTextStyle()
                            .bigText("Last change is:\n${it[lastIt].changeType} - ${it[lastIt].changedItemName}\nby ${it[lastIt].changedBy}"))
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    .setContentIntent(pendingIntent)
                    .setContentIntent(PendingIntent.getActivity(this, 0, Intent(), PendingIntent.FLAG_MUTABLE))
                    .setAutoCancel(true)
                startForeground(NOTIFICATION_ID, builder.build())
            }
            pastFirst = true
        }
    }

    fun stopMyService() {
        stopForeground(true)
        stopSelf()
        isServiceRunning = false
    }
}