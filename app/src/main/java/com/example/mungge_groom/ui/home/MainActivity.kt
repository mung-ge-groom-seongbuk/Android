package com.example.mungge_groom.ui.home

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.mungge_groom.R
import com.example.mungge_groom.databinding.ActivityMainBinding
import com.example.mungge_groom.extention.GlobalApplication
import com.example.mungge_groom.ui.account.AccountViewModel
import com.example.mungge_groom.ui.base.BaseActivity
import com.example.mungge_groom.ui.chat.ChatViewModel
import com.example.mungge_groom.ui.mypage.MypageViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding>(R.layout.activity_main) {
    private lateinit var navHostController: NavController
    private lateinit var accountViewModel: AccountViewModel
    private lateinit var chatViewModel: ChatViewModel
    private lateinit var homeViewModel: HomeViewModel
    private lateinit var mypageViewModel : MypageViewModel
    private lateinit var fusedLocationClient: FusedLocationProviderClient
private lateinit var jwtViewModel: JwtViewModel
    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        if (permissions.all { it.value }) {
            // 모든 권한이 허용된 경우
            Toast.makeText(this, "모든 권한이 허용되었습니다.", Toast.LENGTH_SHORT).show()
        } else {
            // 권한이 거부된 경우
            Toast.makeText(this, "필요한 권한이 거부되었습니다.", Toast.LENGTH_SHORT).show()
            showPermissionRationaleDialog()  // 권한 거부 시 안내 다이얼로그 추가
        }
    }
    private fun showPermissionRationaleDialog() {
        AlertDialog.Builder(this)
            .setTitle("권한 필요")
            .setMessage("이 기능을 사용하려면 위치 권한이 필요합니다. 설정에서 권한을 허용해 주세요.")
            .setPositiveButton("설정") { _, _ ->
                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                val uri = Uri.fromParts("package", packageName, null)
                intent.data = uri
                startActivity(intent)
            }
            .setNegativeButton("취소", null)
            .show()
    }
    private val requiredPermissions = mutableListOf(
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION
    ).apply {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            add(Manifest.permission.POST_NOTIFICATIONS)
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            add(Manifest.permission.ACCESS_BACKGROUND_LOCATION)
        }
    }

    @SuppressLint("MissingPermission")
    private fun getLastKnownLocation() {
        fusedLocationClient.lastLocation
            .addOnSuccessListener { location: Location? ->
                location?.let {
                    val latLng = LatLng(it.latitude, it.longitude)
                    Log.d("Location", "위도: ${latLng.latitude}, 경도: ${latLng.longitude}")
                    homeViewModel.getMatches("${latLng.latitude}","${latLng.longitude}")
                } ?: run {
                    Toast.makeText(this, "위치를 찾을 수 없습니다.", Toast.LENGTH_SHORT).show()
                }
            }
    }

    override fun setLayout() {
        setProfile()
        setBottomNav()
        setViewModel()
        observeLifeCycle()
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        getLastKnownLocation()
    }

    private fun setBottomNav() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navHostController = navHostFragment.findNavController()
        binding.bottomNavigationView.setupWithNavController(navHostController)
    }
    fun setBottomNavSelectedItem(itemId: Int) {
        binding.bottomNavigationView.selectedItemId = itemId
    }
    private fun setViewModel(){
        accountViewModel = ViewModelProvider(this)[AccountViewModel::class.java]
        chatViewModel = ViewModelProvider(this)[ChatViewModel::class.java]
        homeViewModel = ViewModelProvider(this)[HomeViewModel::class.java]
        mypageViewModel = ViewModelProvider(this)[MypageViewModel::class.java]
        jwtViewModel = ViewModelProvider(this)[JwtViewModel::class.java]
        mypageViewModel.getUsers()
    }
    private fun setProfile() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.CREATED) {
                mypageViewModel.getUsersData.collectLatest {
                    val tm = GlobalApplication.instance.tokenManager

                    GlobalApplication.instance.distance1 = tm.getUserd().first()?.toDouble() ?: 0.0
                    GlobalApplication.instance.duration2 = tm.getUserdd().first()?.toDouble() ?: 0.0
                    GlobalApplication.instance.pace3 = tm.getUserddd().first()?.toDouble() ?: 0.0
                    GlobalApplication.instance.cal4 = tm.getUserdddd().first()?.toDouble() ?: 0.0


                    val email =
                        GlobalApplication.instance.tokenManager.getUserId()
                            .first()
                    val user = it.find { user ->
                        user.email == email
                    }
                    if (user != null) {
                        GlobalApplication.instance.tokenManager.saveUser(user,applicationContext)
                        GlobalApplication.instance.user = user
                    }
                }
            }
        }
    }
    private fun observeLifeCycle(){
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.CREATED){
                mypageViewModel.getUsersData.collectLatest {
                    Log.d("유저",it.toString())
                }
            }
        }
    }
}
