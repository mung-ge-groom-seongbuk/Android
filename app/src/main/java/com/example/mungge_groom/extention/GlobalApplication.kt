package com.example.mungge_groom.extention


import android.app.Application
import android.content.Context
import android.util.Log
import android.widget.ImageView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltAndroidApp
class GlobalApplication : Application() {

    @OptIn(DelicateCoroutinesApi::class)
    override fun onCreate() {
        super.onCreate()
        instance = this

        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w("토큰", "Fetching FCM registration token failed", task.exception)
                return@OnCompleteListener
            }

            // 새로운 FCM 등록 토큰 가져오기
            val token = task.result

            // 토큰 로그와 토스트 메시지
            val msg = token.toString()
            Log.d("토큰", msg)
            GlobalScope.launch(Dispatchers.IO) {
                tokenManager.saveFireBaseTokenId(msg)
            }

            Toast.makeText(applicationContext, msg, Toast.LENGTH_SHORT).show()
        })
    }

    @Inject
    lateinit var tokenManager: TokenManager

    companion object {
        lateinit var instance: GlobalApplication
            private set

        // 이미지를 맞추어 로드
        fun loadImage(imageView: ImageView, source: Any) {
            Glide.with(instance)
                .load(source)
                .into(imageView)
        }


        // 프로필 이미지 (동그란 이미지) 지정
        fun loadProfileImage(imageView: ImageView, source: Any) {
            Glide.with(instance)
                .load(source)
                .apply(RequestOptions.bitmapTransform(CircleCrop()))
                .into(imageView)
        }


        // 이미지를 맞게 조정하여 로드
        fun loadCropImage(imageView: ImageView, source: Any) {
            Glide.with(instance)
                .load(source)
                .centerCrop()
                .into(imageView)
        }


        // 네 개의 변이 Rounded로 처리되어있는 사각형
        fun loadCropRoundedSquareImage(context: Context, imageView: ImageView, source: Any, rounded : Int) {
            val density = context.resources.displayMetrics.density
            val roundedCorners = RoundedCorners((rounded * density).toInt())

            Glide.with(context)
                .load(source)
                .apply(RequestOptions().transform(CenterCrop(), roundedCorners))
                .into(imageView)
        }
    }
}