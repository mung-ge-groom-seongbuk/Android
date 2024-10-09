package com.example.mungge_groom.ui.account

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.mungge_groom.R
import com.example.mungge_groom.databinding.ActivityStartBinding
import com.example.mungge_groom.ui.base.BaseActivity
import java.io.File

class StartActivity : BaseActivity<ActivityStartBinding>(R.layout.activity_start) {
    private val TAG = "testTAG"
    private var imageFile = File("")
    private val galleryPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()){
            if (it){
                val intent = Intent(Intent.ACTION_PICK)
                intent.setDataAndType(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    "image/*"
                )
                imageLauncher.launch(intent)
            }else
                Log.d(TAG, "deny")
        }

    private val imageLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()){ result ->
            Log.d(TAG, "imageLauncher enter!!")
            if (result.resultCode == RESULT_OK){
                val imageUri = result.data?.data
                imageUri?.let {
                    imageFile = File(getRealPathFromUri(it))
                    Log.d(TAG, imageFile.toString())
                }
            }
        }

    override fun setLayout() {
        binding.profileImage.setOnClickListener{
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
                galleryPermissionLauncher.launch(Manifest.permission.READ_MEDIA_IMAGES)
            else
                galleryPermissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
        }
    }

    private fun getRealPathFromUri(uri: Uri): String {
        var realPath = ""
        val cursor = contentResolver.query(uri, null, null, null, null)
        if (cursor != null) {
            cursor.moveToFirst()
            val idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA)
            if (idx != -1) {
                realPath = cursor.getString(idx)
            } else {
                val documentId = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID))
                realPath = "${MediaStore.Images.Media.EXTERNAL_CONTENT_URI}/$documentId"
            }
            cursor.close()
        }
        return realPath
    }

}