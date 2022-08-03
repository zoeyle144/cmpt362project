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
import com.example.cmpt362project.ui.home.HomeFragment
import com.example.cmpt362project.viewModels.BoardListViewModel
import com.example.cmpt362project.viewModels.CategoryListViewModel


class NotificationService : LifecycleService() {
    private lateinit var boardListViewModel: BoardListViewModel
    private lateinit var categoryListViewModel: CategoryListViewModel
    private lateinit var boardList: List<Board>
    private lateinit var categoryList: List<Category>

    companion object {
        const val NOTIFICATION_ID = 777
        var isServiceRunning = false
    }

    override fun onCreate() {
        super.onCreate()
        boardListViewModel = BoardListViewModel()
        categoryListViewModel = CategoryListViewModel()
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

        boardListViewModel.fetchBoards()
        boardListViewModel.boardsLiveData.observe(this){
            if (it != boardList){
                val intentHomeFrag = Intent(this, MainActivity::class.java)
                val pendingIntent: PendingIntent = PendingIntent.getActivity(this, 0, intentHomeFrag, PendingIntent.FLAG_IMMUTABLE)
                var builder = NotificationCompat.Builder(this, "channel-777")
                    .setSmallIcon(R.drawable.empty_profile_pic)
                    .setContentTitle("Board List Notification")
                    .setContentText("Board List has changed")
                    .setStyle(
                        NotificationCompat.BigTextStyle()
                            .bigText("Board Name of Last Board: ${it[it.size-1].boardName}"))
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    .setContentIntent(pendingIntent)
                    .setAutoCancel(true)

//        with(NotificationManagerCompat.from(this)){
//            notify(1, builder.build())
//        }
                startForeground(NOTIFICATION_ID, builder.build())
            }
            boardList = it
        }

    }

    fun stopMyService() {
        stopForeground(true)
        stopSelf()
        isServiceRunning = false
    }
}