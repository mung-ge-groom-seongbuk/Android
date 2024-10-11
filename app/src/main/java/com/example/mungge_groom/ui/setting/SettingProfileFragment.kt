package com.example.mungge_groom.ui.setting

import android.Manifest
import android.content.ContentResolver
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.mungge_groom.R
import com.example.mungge_groom.data.response.User
import com.example.mungge_groom.databinding.FragmentSettingProfileBinding
import com.example.mungge_groom.extention.GlobalApplication
import com.example.mungge_groom.ui.account.AccountViewModel
import com.example.mungge_groom.ui.base.BaseFragment
import com.example.mungge_groom.ui.mypage.MypageViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import java.io.FileOutputStream

class SettingProfileFragment :
    BaseFragment<FragmentSettingProfileBinding>(R.layout.fragment_setting_profile) {
    private val accountViewModel: AccountViewModel by activityViewModels()
    var uri: Uri? = null
    lateinit var user: User
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
            if (result.resultCode == AppCompatActivity.RESULT_OK) {
                uri = result.data?.data
                uri?.let {
                    GlobalApplication.loadProfileImage(binding.profileImage, it)
                    sendServer()
                }
            }
        }

    override fun setLayout() {
        initProfile()
        binding.profileImage.setOnClickListener {
            checkAndRequestPermission()
        }
        binding.settingAccountIdBt.setOnClickListener {

        }
        binding.settingAccountPwBt.setOnClickListener {

        }

    }


    private fun initProfile() {
        lifecycleScope.launch {
            GlobalApplication.instance.tokenManager.getUser(requireContext()).collect { users ->
                if (users != null) {
                    user = users  // user 초기화
                    GlobalApplication.loadProfileImage(
                        binding.profileImage,
                        user.profile_picture
                            ?: "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAMgAAADICAMAAACahl6sAAAAIVBMVEXY2Njz8/Pq6urv7+/h4eHb29vo6Oje3t7j4+Pt7e3p6ekmc3lwAAADMElEQVR4nO2bC3KDMAxEMeab+x+4JZQBEkhBlq2NZt8JvGOtPkZUFSGEEEIIIYQQQgghhBBCCCEEnXbo6hjDLzHW3dBan0dEO9ThjfrrxDQHKv60NNZnu0ETz2Q8w+xbpPQfZTyl9NZnvEB7GlS7AIP3SnNFxgR4fHVXdYTQWZ/1A+14XUcII2x4tf+6fE8EVXJXB6qS+zpAldzyx8Jofep3buSrLXC563L9eAWsnrRSHSFg2eRSX3JMbX32Lb1cRwhIHaQg865E69OviJ0+g+P3pAsBupLEC8G5koSUNQOSuBJqyAJGLRnShQzWGp4kRxZKbKXrCMFaw4SCRTBMomARDJMIB5E9CGOJgtcx3J7Yn8wgdCluhGjogMi/FEIhmXBjdjdC3BRENy2Km6bRTRvvZrDyM+q6eXxw8xzk5oHOz5Opm0dsP58V3Hzo8fPpzc3HUD+fp90sDPhZ4fCzVONnzcnN4pmfVUA/y5mVm3XZys8Cs5+V8srNkv+Ek98uJpz8CDPh5NekGRc/ixFCCCHky2mb4TGO8cLkHuM4PoYGsGfph1r0Ih/rAWc2Oexz74DRE59PHre0GE8pvcoiykxnF2O96Ln3nNFGSqMs4ymlfIT9/1Qio/ADS6vojVe6gilMZUXrnFKbKfeeqiWUed5OXti4QgHTZ3THltxv9fnDaiFveOVKukfkTMRJmxr3yaakiM23ZLJ8cR2ZlBjoyKKksD8W1H1ipENdiWQ/QwflrYJidfAd1b2bQn3JMYrdiknCWlFLXSo/VqSgZRNDg8wo2STzPHgFlZnRPLAmNILLNGMtKGQus5K+J73Am5X0PckL9MYlZCW1mJin3oXEFAzikIk0l8BcSOKVAF1I2pVA1JCFlFpiffY9ch0wuXdGnoFVvnPqIf6JCaJd3CJtHQH69z3Sbh4ssuSxZX3ud2Q6oKrhjKwmwllEahI4i0hNAjJSbZGNV9anPkKiA64cTkhKIlijNSNptwCTlixtPawPfcRDIARoyl2RzLtuhACWEVkhcSPE+szHUAgaFIIGhaBBIWhQCBoUggaFoEEhaFAIGhSCBoWgQSFoUAgap8f9Ac1KQOtCVp1TAAAAAElFTkSuQmCC"
                    )

                    // user 초기화 후 UI 업데이트
                    binding.settingAccountId2Tv.text = user.nickname
                    binding.settingAccountPw2Tv.text = user.intro
                } else {
                    // User가 null인 경우 처리
                    Toast.makeText(requireContext(), "User 정보를 가져올 수 없습니다.", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }
    }

    private fun sendServer() {
        if (uri == null) {
            uri = if (user.profile_picture != null) {
                Uri.parse(user.profile_picture)
            } else {
                // profile_picture가 null일 때 처리
                Toast.makeText(requireContext(), "프로필 사진이 없습니다.", Toast.LENGTH_SHORT).show()
                return
            }
        }
        lifecycleScope.launch {
            val email = user.email
            sendServerDataWithProfile(
                requireActivity().contentResolver,
                uri!!,  // null 검증 후 처리
                binding.settingAccountId2Tv.text.toString(),
                binding.settingAccountPw2Tv.text.toString(),
                email
            )
        }
    }


    private fun sendServerDataWithProfile(
        contentResolver: ContentResolver,
        fileUri: Uri,
        intro: String,
        nickname: String,
        email: String
    ) {
        // 로컬 파일 처리
        val profilePicture = prepareCompressedFilePart("profile_picture", fileUri, contentResolver)
        val introBody = createPartFromString(intro)
        val nicknameBody = createPartFromString(nickname)
        val emailBody = createPartFromString(email)

        accountViewModel.postUpdateProfile(profilePicture, introBody, nicknameBody, emailBody)
    }

    private fun checkAndRequestPermission() {
        val permission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            Manifest.permission.READ_MEDIA_IMAGES
        } else {
            Manifest.permission.READ_EXTERNAL_STORAGE
        }

        when {
            ContextCompat.checkSelfPermission(
                requireContext(),
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
        AlertDialog.Builder(requireContext())
            .setTitle("권한 필요")
            .setMessage("이미지를 선택하기 위해 저장소 접근 권한이 필요합니다.")
            .setPositiveButton("확인") { _, _ -> galleryPermissionLauncher.launch(permission) }
            .setNegativeButton("취소", null)
            .show()
    }

    private fun showPermissionDeniedDialog() {
        AlertDialog.Builder(requireContext())
            .setTitle("권한 거부됨")
            .setMessage("이미지를 선택하려면 설정에서 저장소 접근 권한을 허용해주세요.")
            .setPositiveButton("설정으로 이동") { _, _ -> openAppSettings() }
            .setNegativeButton("취소", null)
            .show()
    }

    private fun openAppSettings() {
        Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
            data = Uri.fromParts("package", requireContext().packageName, null)
            startActivity(this)
        }
    }

    fun createPartFromString(value: String): RequestBody {
        return value.toRequestBody("text/plain".toMediaTypeOrNull())
    }


    private fun prepareCompressedFilePart(
        partName: String,
        fileUri: Uri,
        contentResolver: ContentResolver
    ): MultipartBody.Part {
        val inputStream = contentResolver.openInputStream(fileUri)
        val tempFile = File(requireActivity().cacheDir, "compressed_image_file.jpg")

        inputStream?.use { input ->
            val bitmap = BitmapFactory.decodeStream(input)
            val resizedBitmap =
                Bitmap.createScaledBitmap(bitmap, bitmap.width / 2, bitmap.height / 2, true)
            FileOutputStream(tempFile).use { output ->
                resizedBitmap.compress(Bitmap.CompressFormat.JPEG, 80, output)
            }
        }
        val requestFile = tempFile.asRequestBody("image/*".toMediaTypeOrNull())
        return MultipartBody.Part.createFormData(partName, tempFile.name, requestFile)
    }


}