package com.example.mungge_groom.ui.home

import android.Manifest
import android.app.AlertDialog
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import com.example.mungge_groom.R
import com.example.mungge_groom.data.model.RunningData
import com.example.mungge_groom.data.response.User
import com.example.mungge_groom.extention.GlobalApplication
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

class ServiceRun : Service() {
    val LOCATION_PERMISSION_REQUEST_CODE = 1001
    private val handler = Handler(Looper.getMainLooper())
    private val locationUpdateInterval: Long = 1000 // 1초 간격으로 위치 업데이트
    private var lastUpdateTime: Long = 0 // 시작 시간 저장

    companion object {
        private var instance: ServiceRun? = null
        fun getInstance(): ServiceRun? {
            if (instance == null) {
                instance = ServiceRun()
            }
            return instance!!
        }
    }

    private fun sendRunningData(time: String, distance: String, pace: String) {
        val cal = distance.toDouble() * 60.0
        GlobalApplication.instance.cal = cal.toString()
        GlobalApplication.instance.distance = distance
        GlobalApplication.instance.duration = time
        GlobalApplication.instance.pace = pace

        Log.d("okhttp","${GlobalApplication.instance.duration} ${GlobalApplication.instance.distance} ${GlobalApplication.instance.pace} ${GlobalApplication.instance.cal} ${GlobalApplication.instance.user}")
    }

    private var isRunning = false
    private var isPaused = false
    private var runningThread: Thread? = null
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private var startlat = 0.0
    private var startlon = 0.0
    var time = 0.0
    var km = 0 // Int 타입으로 변경하여 정수만 저장
    private var pace = ""

    override fun onCreate() {
        super.onCreate()
        instance = this // 서비스 인스턴스를 저장
        fusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(applicationContext)
    }

    @RequiresApi(Build.VERSION_CODES.O)
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

    private var startTime: Long = 0

    @RequiresApi(Build.VERSION_CODES.O)
    private fun startRunning() {
        isRunning = true
        startTime = System.currentTimeMillis() // 시작 시간 기록
        lastUpdateTime = startTime
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        fusedLocationProviderClient.lastLocation.addOnSuccessListener { location ->
            location?.let {
                startlat = it.latitude
                startlon = it.longitude
            }
        }.addOnFailureListener {
            Toast.makeText(applicationContext, "측정 오류", Toast.LENGTH_SHORT).show()
        }

        handler.post(locationUpdateRunnable) // Runnable 실행
    }


    private val locationUpdateRunnable = object : Runnable {
        override fun run() {
            if (isRunning && !isPaused) {
                if (ActivityCompat.checkSelfPermission(
                        applicationContext,
                        Manifest.permission.ACCESS_FINE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                        applicationContext,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED
                ) {

                    return
                }
                fusedLocationProviderClient.lastLocation.addOnSuccessListener { location ->
                    location?.let {
                        val currentLat = it.latitude
                        val currentLon = it.longitude

                        // 누적 거리 계산
                        km += calculateDistance(startlat, startlon, currentLat, currentLon).toInt()

                        // 현재 위치를 다음 시작 위치로 설정
                        startlat = currentLat
                        startlon = currentLon

                        // 시간 업데이트
                        val currentTime = System.currentTimeMillis()
                        time = (currentTime - lastUpdateTime) / 1000.0 // 초 단위 시간

                        // 페이스 업데이트
                        pace = calculatePace(km.toDouble(), time)
                    }
                }.addOnFailureListener {
                    Toast.makeText(applicationContext, "측정 오류", Toast.LENGTH_SHORT).show()
                }
                val currentTime = System.currentTimeMillis()
                time = (currentTime - startTime) / 1000.0 // 총 경과 시간 (초)
                // 1초 후에 다시 실행
                handler.postDelayed(this, locationUpdateInterval)
            }
        }
    }

    fun pauseRunning() {
        isPaused = true
    }

    fun resumeRunning() {
        isPaused = false
        handler.post(locationUpdateRunnable) // Runnable 재실행
    }


    fun stopRunning() {
        isRunning = false
        isPaused = false
        handler.removeCallbacks(locationUpdateRunnable)

        val totalTime = time
        val totalKm = km / 1000.0 // 미터를 킬로미터로 변환
        pace = calculatePace(totalKm * 1000, totalTime)

        val timeString = formatTime(totalTime.toLong())
        val distanceString = String.format("%.2f", totalKm)

        Toast.makeText(
            applicationContext,
            "시간: $timeString, 거리: ${distanceString}km, 페이스: $pace",
            Toast.LENGTH_LONG
        ).show()
        sendRunningData(timeString,distanceString,pace)

        // 데이터 초기화
        time = 0.0
        km = 0
        pace = ""
        startTime = 0
    }

    private fun formatTime(seconds: Long): String {
        val hours = seconds / 3600
        val minutes = (seconds % 3600) / 60
        val secs = seconds % 60
        return String.format("%02d:%02d:%02d", hours, minutes, secs)
    }

    override fun onDestroy() {
        super.onDestroy()
        stopRunning()
        instance = null
    }

    //1. 위도와 경도로 거리 구하는 방법 (Haversine Formula)
    //위도와 경도를 이용해 두 지점 간의 거리를 구하려면 Haversine 공식을 사용
    // 이 공식은 구형 지구를 가정하여 두 지점 사이의 거리를 계산하는 데 사용
    private fun calculateDistance(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Double {
        val earthRadius = 6371e3 // 지구 반지름(미터)

        val dLat = Math.toRadians(lat2 - lat1)
        val dLon = Math.toRadians(lon2 - lon1)

        val a = sin(dLat / 2) * sin(dLat / 2) +
                cos(Math.toRadians(lat1)) * cos(Math.toRadians(lat2)) *
                sin(dLon / 2) * sin(dLon / 2)
        val c = 2 * atan2(sqrt(a), sqrt(1 - a))

        return earthRadius * c // 결과 거리 (미터)
    }

    // 페이스 구하는 공식
    private fun calculatePace(distanceInMeters: Double, timeInSeconds: Double): String {
        if (distanceInMeters < 1) {
            // 거리가 1미터 미만인 경우 페이스를 계산하지 않음
            return "0"
        }

        val distanceInKilometers = distanceInMeters / 1000.0
        val timeInMinutes = timeInSeconds / 60.0
        val pace = timeInMinutes / distanceInKilometers

        // 페이스를 "분:초" 형식으로 반환
        val minutes = pace.toInt()
        val seconds = ((pace - minutes) * 60).toInt()
        return "$minutes"
    }


    override fun onBind(intent: Intent?): IBinder? {
        return null
    }
}