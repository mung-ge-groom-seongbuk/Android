package com.example.mungge_groom.ui.account

import android.Manifest
import android.content.ContentResolver
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.example.mungge_groom.R
import com.example.mungge_groom.databinding.ActivityStartBinding
import com.example.mungge_groom.extention.GlobalApplication
import com.example.mungge_groom.ui.base.BaseActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import java.io.FileOutputStream

@AndroidEntryPoint
class StartActivity : BaseActivity<ActivityStartBinding>(R.layout.activity_start) {
    private val accountViewModel: AccountViewModel by viewModels()
    var uri: Uri? = null
    private val galleryPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                openImagePicker()
            } else {
                showPermissionDeniedDialog()
            }
        }

    private val imageLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                uri = result.data?.data
                uri?.let {
                    GlobalApplication.loadProfileImage(binding.profileImage, it)
                }
            }
        }

    override fun setLayout() {
        binding.profileImage.setOnClickListener {
            checkAndRequestPermission()
        }
        binding.button2.setOnClickListener {
            sendServer()
        }
    }

    private fun sendServer() {
        if (uri == null) {
            Toast.makeText(this, "프로필 이미지를 선택해주세요.", Toast.LENGTH_SHORT).show()
            return
        }

        lifecycleScope.launch {
            val email = GlobalApplication.instance.tokenManager.getUserId().first() ?: ""
            sendServerDataWithProfile(
                contentResolver,
                uri!!,  // null 검증 후 처리
                binding.nameInput.text.toString(),
                binding.introInput.text.toString(),
                email
            )
            startActivityWithClear(LoginActivity::class.java)
            Toast.makeText(this@StartActivity, "회원가입 성공", Toast.LENGTH_SHORT)
                .show()
        }
    }
    private fun checkAndRequestPermission() {
        val permission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            Manifest.permission.READ_MEDIA_IMAGES
        } else {
            Manifest.permission.READ_EXTERNAL_STORAGE
        }

        when {
            ContextCompat.checkSelfPermission(
                this,
                permission
            ) == PackageManager.PERMISSION_GRANTED -> {
                openImagePicker()
            }

            shouldShowRequestPermissionRationale(permission) -> {
                showPermissionRationaleDialog(permission)
            }

            else -> {
                galleryPermissionLauncher.launch(permission)
            }
        }
    }

    private fun openImagePicker() {
        val intent = Intent(Intent.ACTION_GET_CONTENT).apply {
            type = "image/*"
        }
        imageLauncher.launch(intent)
    }

    private fun showPermissionRationaleDialog(permission: String) {
        AlertDialog.Builder(this)
            .setTitle("권한 필요")
            .setMessage("이미지를 선택하기 위해 저장소 접근 권한이 필요합니다.")
            .setPositiveButton("확인") { _, _ -> galleryPermissionLauncher.launch(permission) }
            .setNegativeButton("취소", null)
            .show()
    }

    private fun showPermissionDeniedDialog() {
        AlertDialog.Builder(this)
            .setTitle("권한 거부됨")
            .setMessage("이미지를 선택하려면 설정에서 저장소 접근 권한을 허용해주세요.")
            .setPositiveButton("설정으로 이동") { _, _ -> openAppSettings() }
            .setNegativeButton("취소", null)
            .show()
    }

    private fun openAppSettings() {
        Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
            data = Uri.fromParts("package", packageName, null)
            startActivity(this)
        }
    }

    fun createPartFromString(value: String): RequestBody {
        return value.toRequestBody("text/plain".toMediaTypeOrNull())
    }


    private fun sendServerDataWithProfile(
        contentResolver: ContentResolver,
        fileUri: Uri,
        intro: String,
        nickname: String,
        email: String
    ) {
        // 프로필 사진 파일 준비
        val profilePicture = prepareCompressedFilePart("profile_picture", fileUri, contentResolver)
        // 나머지 RequestBody 필드들 준비
        val introBody = createPartFromString(intro)
        val nicknameBody = createPartFromString(nickname)
        val emailBody = createPartFromString(email)

        accountViewModel.postUpdateProfile(profilePicture,introBody,nicknameBody,emailBody)
    }

    private fun prepareCompressedFilePart(
        partName: String,
        fileUri: Uri,
        contentResolver: ContentResolver
    ): MultipartBody.Part {
        val inputStream = contentResolver.openInputStream(fileUri)
        val tempFile = File(cacheDir, "compressed_image_file.jpg")

        inputStream?.use { input ->
            val bitmap = BitmapFactory.decodeStream(input)
            val resizedBitmap = Bitmap.createScaledBitmap(bitmap, bitmap.width / 2, bitmap.height / 2, true)
            FileOutputStream(tempFile).use { output ->
                resizedBitmap.compress(Bitmap.CompressFormat.JPEG, 80, output)
            }
        }
        val requestFile = tempFile.asRequestBody("image/*".toMediaTypeOrNull())
        return MultipartBody.Part.createFormData(partName, tempFile.name, requestFile)
    }

}