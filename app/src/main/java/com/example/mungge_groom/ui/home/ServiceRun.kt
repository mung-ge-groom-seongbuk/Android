package com.example.mungge_groom.ui.home

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import com.example.mungge_groom.R

class ServiceRun : Service() {

    companion object {
        private var instance: ServiceRun? = null

        fun getInstance(): ServiceRun? {
            return instance
        }
    }

    private var isRunning = false
    private var isPaused = false
    private var runningThread: Thread? = null


    override fun onCreate() {
        super.onCreate()
        instance = this // 서비스 인스턴스를 저장
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        startForeground(1, createNotification())
        startRunning()
        return START_STICKY
    }

    private fun createNotification(): Notification {
        val channelId = "RunningServiceChannel"
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "Running Service",
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = "러닝 서비스에서 사용하는 알림 채널입니다."
            }
            val manager = getSystemService(NotificationManager::class.java)
            // 알림 채널을 실제로 생성하는 코드
            manager.createNotificationChannel(channel)
        }
        return NotificationCompat.Builder(this, channelId)
            .setContentTitle("러닝 메이트")
            .setContentText("러닝 데이터를 측정 중입니다.")
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .build()
    }


    private fun startRunning() {
        isRunning = true
        runningThread = Thread {
            var count = 0
            while (isRunning) {
                try {
                    if (!isPaused) {
                        count++
                        Log.d("카운트", "$count")
                    }
                    Thread.sleep(1000)
                } catch (e: InterruptedException) {
                    e.printStackTrace()
                }
            }
        }
        runningThread?.start()
    }

    fun pauseRunning() {
        isPaused = true
    }

    fun resumeRunning() {
        isPaused = false
    }

    fun stopRunning() {
        isRunning = false
        isPaused = false // 일시 중지 상태 재설정
        runningThread?.interrupt()
        runningThread = null
    }

    override fun onDestroy() {
        super.onDestroy()
        stopRunning() // 인스턴스를 해제하기 전에 실행 중인 스레드를 중지
        instance = null
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }
}