package com.example.mungge_groom.ui.home

import android.Manifest
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.location.Location
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.mungge_groom.R
import com.example.mungge_groom.data.model.MarkerData
import com.example.mungge_groom.data.model.RunningCrewData
import com.example.mungge_groom.data.model.RunningData
import com.example.mungge_groom.data.response.User
import com.example.mungge_groom.databinding.FragmentToggle2Binding
import com.example.mungge_groom.databinding.MarkerToggle2MapBinding
import com.example.mungge_groom.extention.GlobalApplication
import com.example.mungge_groom.ui.base.BaseFragment
import com.example.mungge_groom.ui.chat.ChatRoomFragment
import com.example.mungge_groom.ui.listener.onClickBottomSheetDialogListener
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.net.CookieManager

@AndroidEntryPoint
class Toggle2Fragment : BaseFragment<FragmentToggle2Binding>(R.layout.fragment_toggle2),
    onClickBottomSheetDialogListener,
    OnMapReadyCallback,
    GoogleMap.OnMarkerClickListener {
    private val homeViewModel: HomeViewModel by activityViewModels()
    private lateinit var mapView: MapView
    private var googleMap: GoogleMap? = null
    private val customMarkers = mutableListOf<Marker>()

    var me : User? = null
    private val requiredPermissions = mutableListOf(
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.FOREGROUND_SERVICE
    ).apply {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            add(Manifest.permission.POST_NOTIFICATIONS)
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            add(Manifest.permission.FOREGROUND_SERVICE_LOCATION)
        }
    }

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        if (permissions.all { it.value }) {
            enableMyLocation()
        } else {
            Toast.makeText(requireContext(), "필요한 권한이 거부되었습니다.", Toast.LENGTH_SHORT).show()
        }
    }

    @RequiresApi(Build.VERSION_CODES.P)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mapView = binding.mapView
        mapView.onCreate(savedInstanceState)
        mapView.getMapAsync(this)
        homeViewModel.getMap()
        setMe()
        setLayout()

        homeViewModel.postNotifications()

    }

    @RequiresApi(Build.VERSION_CODES.P)
    override fun setLayout() {
        observeLifeCycle()
        setOnClick()
        val cookieManager = CookieManager.getDefault()
        if (cookieManager != null && cookieManager is CookieManager) {
            val cookieStore = cookieManager.cookieStore
            val cookies = cookieStore.cookies
            cookies.forEach { cookie ->
                Log.d("Cookie", "Name: ${cookie.name}, Value: ${cookie.value}")
            }
        } else {
            Log.e("CookieManager", "CookieManager is not initialized properly")
        }
    }

    private fun observeLifeCycle() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.CREATED) {
                homeViewModel.map.collectLatest {
                    Log.d("맵", it.message)
                }
            }
        }
    }
    private fun showCustomDialog(timeString : String, distance : String, pace: String) {
        // 사용자 정의 레이아웃을 가져옴
        val inflater: LayoutInflater = LayoutInflater.from(requireContext())
        val dialogView: View = inflater.inflate(R.layout.custom_dialog_layout, null)

        // 다이얼로그의 텍스트뷰 설정
        val textView1: TextView = dialogView.findViewById(R.id.textView1)
        val textView2: TextView = dialogView.findViewById(R.id.textView2)
        val textView3: TextView = dialogView.findViewById(R.id.textView3)

        textView1.text = "시간 : $timeString"
        textView2.text = "거리 : $distance"
        textView3.text = "페이스 : $pace"

        // AlertDialog 빌더를 사용하여 다이얼로그 생성
        val dialogBuilder = AlertDialog.Builder(requireContext())
            .setView(dialogView)
            .setPositiveButton("확인") { dialog, _ ->
                dialog.dismiss()
            }
            .setNegativeButton("취소") { dialog, _ ->
                dialog.dismiss()
            }

        // 다이얼로그 표시
        val alertDialog = dialogBuilder.create()
        alertDialog.show()
    }
    @RequiresApi(Build.VERSION_CODES.P)
    private fun setOnClick() {
        binding.floatingStart.tag = "start"
        binding.floatingStart.setOnClickListener {
            when (binding.floatingStart.tag) {
                "start" -> {
                    if (checkPermissions()) {
                        startRunning()
                    } else {
                        requestPermissions()
                    }
                }

                "pause" -> {
                    pauseRunning()
                }
            }
        }

        binding.floatingSquare.setOnClickListener {
            stopRunning()
        }

        binding.floatingPlay.setOnClickListener {
            resumeRunning()
        }

    }

    private fun checkPermissions(): Boolean {
        return requiredPermissions.all {
            ContextCompat.checkSelfPermission(
                requireContext(),
                it
            ) == PackageManager.PERMISSION_GRANTED
        }
    }

    private fun requestPermissions() {
        requestPermissionLauncher.launch(requiredPermissions.toTypedArray())
    }
    private fun setMe() {
        lifecycleScope.launch {
            GlobalApplication.instance.tokenManager.getUser(requireContext())
                .collect { users ->
                    if (users != null) {
                        me = users  // user 초기화
                    }
                }
        }
    }
    private fun startRunning() {
        with(binding) {
            floatingStart.tag = "pause"
            floatingStart.foreground =
                ContextCompat.getDrawable(requireContext(), R.drawable.ic_button_pause)
        }
        enableMyLocation()
        val intent = Intent(requireContext(), ServiceRun::class.java)
        ContextCompat.startForegroundService(requireContext(), intent)
        ServiceRun.getInstance()
    }

    private fun pauseRunning() {
        with(binding) {
            floatingStart.tag = "stop"
            floatingPlay.visibility = View.VISIBLE
            floatingSquare.visibility = View.VISIBLE
            floatingStart.visibility = View.GONE
        }
        ServiceRun.getInstance()?.pauseRunning()
        Toast.makeText(requireContext(), "일시 중지되었습니다.", Toast.LENGTH_SHORT).show()
    }

    private fun stopRunning() {
        with(binding) {
            floatingStart.tag = "start"
            floatingStart.foreground =
                ContextCompat.getDrawable(requireContext(), R.drawable.ic_button_start)
            floatingStart.visibility = View.VISIBLE
            floatingPlay.visibility = View.GONE
            floatingSquare.visibility = View.GONE
        }

        ServiceRun.getInstance()?.stopRunning()

        // user_id가 null인 경우 빈 문자열로 처리
        val userId = GlobalApplication.instance.user?.user_id?.toString() ?: ""
// distance, duration, pace, cal이 null이면 기본값을 제공
        val distance = GlobalApplication.instance.distance
        val duration = GlobalApplication.instance.duration
        val pace = GlobalApplication.instance.pace
        val cal = GlobalApplication.instance.cal


        Log.d("로그","$userId, $distance, $duration, $pace, $cal")
        showCustomToast("시간 : $duration\n거리 : $distance \n페이스 : $pace")

        GlobalApplication.instance.distance1 += distance.toDouble()
        GlobalApplication.instance.pace3 += pace.toDouble()
        GlobalApplication.instance.cal4 += cal.toDouble()

        lifecycleScope.launch {
            val tm = GlobalApplication.instance.tokenManager
            tm.saveUserdi(GlobalApplication.instance.distance1.toString())
            tm.saveUserdi(GlobalApplication.instance.pace3.toString())
            tm.saveUserdi(GlobalApplication.instance.cal4.toString())
        }

        GlobalApplication.instance.distance = "0"
        GlobalApplication.instance.duration = "0"
        GlobalApplication.instance.pace = "0"
        GlobalApplication.instance.cal = "0"

        homeViewModel.postRunning(
            RunningData(
                userId,
                distance,
                duration,
                pace,
                cal
            )
        )
    }
    private fun parseTimeToSeconds(timeString: String): Long {
        val parts = timeString.split(":")
        if (parts.size != 3) throw IllegalArgumentException("Invalid time format")

        val hours = parts[0].toLong()
        val minutes = parts[1].toLong()
        val seconds = parts[2].toLong()

        return hours * 3600 + minutes * 60 + seconds
    }
    private fun resumeRunning() {
        with(binding) {
            floatingStart.tag = "pause"
            floatingStart.foreground =
                ContextCompat.getDrawable(requireContext(), R.drawable.ic_button_pause)
            floatingStart.visibility = View.VISIBLE
            floatingPlay.visibility = View.GONE
            floatingSquare.visibility = View.GONE
        }
        ServiceRun.getInstance()?.resumeRunning()
        Toast.makeText(requireContext(), "재시작되었습니다.", Toast.LENGTH_SHORT).show()
    }

    override fun onMapReady(map: GoogleMap) {
        googleMap = map
        checkAndRequestPermissions()

        val markerDataList = listOf(
            MarkerData(
                37.59404, 127.1313, RunningCrewData(
                    "박지원",
                    "박지원 입니다.",
                    "5",
                    "40",
                    "0.22",
                    "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAMgAAADICAMAAACahl6sAAAAIVBMVEXY2Njz8/Pq6urv7+/h4eHb29vo6Oje3t7j4+Pt7e3p6ekmc3lwAAADMElEQVR4nO2bC3KDMAxEMeab+x+4JZQBEkhBlq2NZt8JvGOtPkZUFSGEEEIIIYQQQgghhBBCCCEEnXbo6hjDLzHW3dBan0dEO9ThjfrrxDQHKv60NNZnu0ETz2Q8w+xbpPQfZTyl9NZnvEB7GlS7AIP3SnNFxgR4fHVXdYTQWZ/1A+14XUcII2x4tf+6fE8EVXJXB6qS+zpAldzyx8Jofep3buSrLXC563L9eAWsnrRSHSFg2eRSX3JMbX32Lb1cRwhIHaQg865E69OviJ0+g+P3pAsBupLEC8G5koSUNQOSuBJqyAJGLRnShQzWGp4kRxZKbKXrCMFaw4SCRTBMomARDJMIB5E9CGOJgtcx3J7Yn8wgdCluhGjogMi/FEIhmXBjdjdC3BRENy2Km6bRTRvvZrDyM+q6eXxw8xzk5oHOz5Opm0dsP58V3Hzo8fPpzc3HUD+fp90sDPhZ4fCzVONnzcnN4pmfVUA/y5mVm3XZys8Cs5+V8srNkv+Ek98uJpz8CDPh5NekGRc/ixFCCCHky2mb4TGO8cLkHuM4PoYGsGfph1r0Ih/rAWc2Oexz74DRE59PHre0GE8pvcoiykxnF2O96Ln3nNFGSqMs4ymlfIT9/1Qio/ADS6vojVe6gilMZUXrnFKbKfeeqiWUed5OXti4QgHTZ3THltxv9fnDaiFveOVKukfkTMRJmxr3yaakiM23ZLJ8cR2ZlBjoyKKksD8W1H1ipENdiWQ/QwflrYJidfAd1b2bQn3JMYrdiknCWlFLXSo/VqSgZRNDg8wo2STzPHgFlZnRPLAmNILLNGMtKGQus5K+J73Am5X0PckL9MYlZCW1mJin3oXEFAzikIk0l8BcSOKVAF1I2pVA1JCFlFpiffY9ch0wuXdGnoFVvnPqIf6JCaJd3CJtHQH69z3Sbh4ssuSxZX3ud2Q6oKrhjKwmwllEahI4i0hNAjJSbZGNV9anPkKiA64cTkhKIlijNSNptwCTlixtPawPfcRDIARoyl2RzLtuhACWEVkhcSPE+szHUAgaFIIGhaBBIWhQCBoUggaFoEEhaFAIGhSCBoWgQSFoUAgap8f9Ac1KQOtCVp1TAAAAAElFTkSuQmCC"
                )
            ),
            MarkerData(
                37.59444, 127.1322, RunningCrewData(
                    "전유선",
                    "전유선 입니다.",
                    "3",
                    "64",
                    "0.53",
                    "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAMgAAADICAMAAACahl6sAAAAIVBMVEXY2Njz8/Pq6urv7+/h4eHb29vo6Oje3t7j4+Pt7e3p6ekmc3lwAAADMElEQVR4nO2bC3KDMAxEMeab+x+4JZQBEkhBlq2NZt8JvGOtPkZUFSGEEEIIIYQQQgghhBBCCCEEnXbo6hjDLzHW3dBan0dEO9ThjfrrxDQHKv60NNZnu0ETz2Q8w+xbpPQfZTyl9NZnvEB7GlS7AIP3SnNFxgR4fHVXdYTQWZ/1A+14XUcII2x4tf+6fE8EVXJXB6qS+zpAldzyx8Jofep3buSrLXC563L9eAWsnrRSHSFg2eRSX3JMbX32Lb1cRwhIHaQg865E69OviJ0+g+P3pAsBupLEC8G5koSUNQOSuBJqyAJGLRnShQzWGp4kRxZKbKXrCMFaw4SCRTBMomARDJMIB5E9CGOJgtcx3J7Yn8wgdCluhGjogMi/FEIhmXBjdjdC3BRENy2Km6bRTRvvZrDyM+q6eXxw8xzk5oHOz5Opm0dsP58V3Hzo8fPpzc3HUD+fp90sDPhZ4fCzVONnzcnN4pmfVUA/y5mVm3XZys8Cs5+V8srNkv+Ek98uJpz8CDPh5NekGRc/ixFCCCHky2mb4TGO8cLkHuM4PoYGsGfph1r0Ih/rAWc2Oexz74DRE59PHre0GE8pvcoiykxnF2O96Ln3nNFGSqMs4ymlfIT9/1Qio/ADS6vojVe6gilMZUXrnFKbKfeeqiWUed5OXti4QgHTZ3THltxv9fnDaiFveOVKukfkTMRJmxr3yaakiM23ZLJ8cR2ZlBjoyKKksD8W1H1ipENdiWQ/QwflrYJidfAd1b2bQn3JMYrdiknCWlFLXSo/VqSgZRNDg8wo2STzPHgFlZnRPLAmNILLNGMtKGQus5K+J73Am5X0PckL9MYlZCW1mJin3oXEFAzikIk0l8BcSOKVAF1I2pVA1JCFlFpiffY9ch0wuXdGnoFVvnPqIf6JCaJd3CJtHQH69z3Sbh4ssuSxZX3ud2Q6oKrhjKwmwllEahI4i0hNAjJSbZGNV9anPkKiA64cTkhKIlijNSNptwCTlixtPawPfcRDIARoyl2RzLtuhACWEVkhcSPE+szHUAgaFIIGhaBBIWhQCBoUggaFoEEhaFAIGhSCBoWgQSFoUAgap8f9Ac1KQOtCVp1TAAAAAElFTkSuQmCC"
                )
            ),
            MarkerData(
                37.5947312, 127.1327993, RunningCrewData(
                    "임수미",
                    "임수미 입니다.",
                    "5",
                    "35",
                    "0.11",
                    "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAMgAAADICAMAAACahl6sAAAAIVBMVEXY2Njz8/Pq6urv7+/h4eHb29vo6Oje3t7j4+Pt7e3p6ekmc3lwAAADMElEQVR4nO2bC3KDMAxEMeab+x+4JZQBEkhBlq2NZt8JvGOtPkZUFSGEEEIIIYQQQgghhBBCCCEEnXbo6hjDLzHW3dBan0dEO9ThjfrrxDQHKv60NNZnu0ETz2Q8w+xbpPQfZTyl9NZnvEB7GlS7AIP3SnNFxgR4fHVXdYTQWZ/1A+14XUcII2x4tf+6fE8EVXJXB6qS+zpAldzyx8Jofep3buSrLXC563L9eAWsnrRSHSFg2eRSX3JMbX32Lb1cRwhIHaQg865E69OviJ0+g+P3pAsBupLEC8G5koSUNQOSuBJqyAJGLRnShQzWGp4kRxZKbKXrCMFaw4SCRTBMomARDJMIB5E9CGOJgtcx3J7Yn8wgdCluhGjogMi/FEIhmXBjdjdC3BRENy2Km6bRTRvvZrDyM+q6eXxw8xzk5oHOz5Opm0dsP58V3Hzo8fPpzc3HUD+fp90sDPhZ4fCzVONnzcnN4pmfVUA/y5mVm3XZys8Cs5+V8srNkv+Ek98uJpz8CDPh5NekGRc/ixFCCCHky2mb4TGO8cLkHuM4PoYGsGfph1r0Ih/rAWc2Oexz74DRE59PHre0GE8pvcoiykxnF2O96Ln3nNFGSqMs4ymlfIT9/1Qio/ADS6vojVe6gilMZUXrnFKbKfeeqiWUed5OXti4QgHTZ3THltxv9fnDaiFveOVKukfkTMRJmxr3yaakiM23ZLJ8cR2ZlBjoyKKksD8W1H1ipENdiWQ/QwflrYJidfAd1b2bQn3JMYrdiknCWlFLXSo/VqSgZRNDg8wo2STzPHgFlZnRPLAmNILLNGMtKGQus5K+J73Am5X0PckL9MYlZCW1mJin3oXEFAzikIk0l8BcSOKVAF1I2pVA1JCFlFpiffY9ch0wuXdGnoFVvnPqIf6JCaJd3CJtHQH69z3Sbh4ssuSxZX3ud2Q6oKrhjKwmwllEahI4i0hNAjJSbZGNV9anPkKiA64cTkhKIlijNSNptwCTlixtPawPfcRDIARoyl2RzLtuhACWEVkhcSPE+szHUAgaFIIGhaBBIWhQCBoUggaFoEEhaFAIGhSCBoWgQSFoUAgap8f9Ac1KQOtCVp1TAAAAAElFTkSuQmCC"
                )
            ),
            MarkerData(
                37.5946668, 127.1325747, RunningCrewData(
                    "권수연",
                    "권수연 입니다.",
                    "1",
                    "14",
                    "0.24",
                    "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAMgAAADICAMAAACahl6sAAAAIVBMVEXY2Njz8/Pq6urv7+/h4eHb29vo6Oje3t7j4+Pt7e3p6ekmc3lwAAADMElEQVR4nO2bC3KDMAxEMeab+x+4JZQBEkhBlq2NZt8JvGOtPkZUFSGEEEIIIYQQQgghhBBCCCEEnXbo6hjDLzHW3dBan0dEO9ThjfrrxDQHKv60NNZnu0ETz2Q8w+xbpPQfZTyl9NZnvEB7GlS7AIP3SnNFxgR4fHVXdYTQWZ/1A+14XUcII2x4tf+6fE8EVXJXB6qS+zpAldzyx8Jofep3buSrLXC563L9eAWsnrRSHSFg2eRSX3JMbX32Lb1cRwhIHaQg865E69OviJ0+g+P3pAsBupLEC8G5koSUNQOSuBJqyAJGLRnShQzWGp4kRxZKbKXrCMFaw4SCRTBMomARDJMIB5E9CGOJgtcx3J7Yn8wgdCluhGjogMi/FEIhmXBjdjdC3BRENy2Km6bRTRvvZrDyM+q6eXxw8xzk5oHOz5Opm0dsP58V3Hzo8fPpzc3HUD+fp90sDPhZ4fCzVONnzcnN4pmfVUA/y5mVm3XZys8Cs5+V8srNkv+Ek98uJpz8CDPh5NekGRc/ixFCCCHky2mb4TGO8cLkHuM4PoYGsGfph1r0Ih/rAWc2Oexz74DRE59PHre0GE8pvcoiykxnF2O96Ln3nNFGSqMs4ymlfIT9/1Qio/ADS6vojVe6gilMZUXrnFKbKfeeqiWUed5OXti4QgHTZ3THltxv9fnDaiFveOVKukfkTMRJmxr3yaakiM23ZLJ8cR2ZlBjoyKKksD8W1H1ipENdiWQ/QwflrYJidfAd1b2bQn3JMYrdiknCWlFLXSo/VqSgZRNDg8wo2STzPHgFlZnRPLAmNILLNGMtKGQus5K+J73Am5X0PckL9MYlZCW1mJin3oXEFAzikIk0l8BcSOKVAF1I2pVA1JCFlFpiffY9ch0wuXdGnoFVvnPqIf6JCaJd3CJtHQH69z3Sbh4ssuSxZX3ud2Q6oKrhjKwmwllEahI4i0hNAjJSbZGNV9anPkKiA64cTkhKIlijNSNptwCTlixtPawPfcRDIARoyl2RzLtuhACWEVkhcSPE+szHUAgaFIIGhaBBIWhQCBoUggaFoEEhaFAIGhSCBoWgQSFoUAgap8f9Ac1KQOtCVp1TAAAAAElFTkSuQmCC"
                )
            ),
            MarkerData(
                37.59373, 127.1331, RunningCrewData(
                    "조원희",
                    "조원희 입니다.",
                    "2",
                    "56",
                    "0.77",
                    "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAMgAAADICAMAAACahl6sAAAAIVBMVEXY2Njz8/Pq6urv7+/h4eHb29vo6Oje3t7j4+Pt7e3p6ekmc3lwAAADMElEQVR4nO2bC3KDMAxEMeab+x+4JZQBEkhBlq2NZt8JvGOtPkZUFSGEEEIIIYQQQgghhBBCCCEEnXbo6hjDLzHW3dBan0dEO9ThjfrrxDQHKv60NNZnu0ETz2Q8w+xbpPQfZTyl9NZnvEB7GlS7AIP3SnNFxgR4fHVXdYTQWZ/1A+14XUcII2x4tf+6fE8EVXJXB6qS+zpAldzyx8Jofep3buSrLXC563L9eAWsnrRSHSFg2eRSX3JMbX32Lb1cRwhIHaQg865E69OviJ0+g+P3pAsBupLEC8G5koSUNQOSuBJqyAJGLRnShQzWGp4kRxZKbKXrCMFaw4SCRTBMomARDJMIB5E9CGOJgtcx3J7Yn8wgdCluhGjogMi/FEIhmXBjdjdC3BRENy2Km6bRTRvvZrDyM+q6eXxw8xzk5oHOz5Opm0dsP58V3Hzo8fPpzc3HUD+fp90sDPhZ4fCzVONnzcnN4pmfVUA/y5mVm3XZys8Cs5+V8srNkv+Ek98uJpz8CDPh5NekGRc/ixFCCCHky2mb4TGO8cLkHuM4PoYGsGfph1r0Ih/rAWc2Oexz74DRE59PHre0GE8pvcoiykxnF2O96Ln3nNFGSqMs4ymlfIT9/1Qio/ADS6vojVe6gilMZUXrnFKbKfeeqiWUed5OXti4QgHTZ3THltxv9fnDaiFveOVKukfkTMRJmxr3yaakiM23ZLJ8cR2ZlBjoyKKksD8W1H1ipENdiWQ/QwflrYJidfAd1b2bQn3JMYrdiknCWlFLXSo/VqSgZRNDg8wo2STzPHgFlZnRPLAmNILLNGMtKGQus5K+J73Am5X0PckL9MYlZCW1mJin3oXEFAzikIk0l8BcSOKVAF1I2pVA1JCFlFpiffY9ch0wuXdGnoFVvnPqIf6JCaJd3CJtHQH69z3Sbh4ssuSxZX3ud2Q6oKrhjKwmwllEahI4i0hNAjJSbZGNV9anPkKiA64cTkhKIlijNSNptwCTlixtPawPfcRDIARoyl2RzLtuhACWEVkhcSPE+szHUAgaFIIGhaBBIWhQCBoUggaFoEEhaFAIGhSCBoWgQSFoUAgap8f9Ac1KQOtCVp1TAAAAAElFTkSuQmCC"
                )
            )
        )
        addCustomMarker(requireContext(), markerDataList)
        googleMap?.setOnMarkerClickListener(this)

    }

    private fun checkAndRequestPermissions() {
        val permissionsToRequest = requiredPermissions.filter {
            ContextCompat.checkSelfPermission(
                requireContext(),
                it
            ) != PackageManager.PERMISSION_GRANTED
        }
        if (permissionsToRequest.isEmpty()) {
            enableMyLocation()
        } else {
            requestPermissions()
        }
    }

    private fun enableMyLocation() {
        googleMap?.let { map ->
            try {
                map.isMyLocationEnabled = true
                LocationServices.getFusedLocationProviderClient(requireActivity()).lastLocation
                    .addOnSuccessListener { location: Location? ->
                        location?.let {
                            val currentLocation = LatLng(it.latitude, it.longitude)
                            map.addMarker(MarkerOptions().position(currentLocation).title("내 위치"))
                            map.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 15f))
                        }
                    }
            } catch (e: SecurityException) {
                Toast.makeText(requireContext(), "위치 권한이 필요합니다.", Toast.LENGTH_SHORT).show()
            }
        } ?: run {
            Toast.makeText(requireContext(), "지도가 아직 준비되지 않았습니다.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun convertToBitmapFromView(context: Context): Bitmap {
        val binding = MarkerToggle2MapBinding.inflate(LayoutInflater.from(context))
        with(binding.root) {
            measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED)
            layout(0, 0, measuredWidth, measuredHeight)
            val bitmap = Bitmap.createBitmap(measuredWidth, measuredHeight, Bitmap.Config.ARGB_8888)
            val canvas = android.graphics.Canvas(bitmap)
            draw(canvas)
            return bitmap
        }
    }

    private fun addCustomMarker(context: Context, markerDataList: List<MarkerData>) {
        googleMap?.let { map ->
            for (markerData in markerDataList) {
                val bitmap = convertToBitmapFromView(context)
                val markerOptions = MarkerOptions()
                    .position(LatLng(markerData.latitude, markerData.longitude))
                    .icon(BitmapDescriptorFactory.fromBitmap(bitmap))

                val marker = map.addMarker(markerOptions)

                marker?.let {
                    customMarkers.add(it)
                    it.tag = markerData
                }
            }
        }
    }

    private fun showBottomSheetDialog(runningCrewData: RunningCrewData?) {
        val modal = HomeBottomSheetDialog(this, runningCrewData)
        modal.show(requireActivity().supportFragmentManager, "HomeBottomSheet")
    }

    private fun showCustomToast(t : String) {
        val tMg = CustomToast(requireContext())
        tMg.showToast("런닝이 종료되었습니다.\n$t", Toast.LENGTH_SHORT)
    }

    // MapView 생명주기 메서드들
    override fun onStart() {
        super.onStart()
        mapView.onStart()
    }

    override fun onResume() {
        super.onResume()
        mapView.onResume()
    }

    override fun onPause() {
        super.onPause()
        mapView.onPause()
    }

    override fun onStop() {
        super.onStop()
        mapView.onStop()
    }

    override fun onDestroy() {
        super.onDestroy()
        mapView.onDestroy()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        mapView.onSaveInstanceState(outState)
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView.onLowMemory()
    }

    override fun onClick(name: String) {
        val mainActivity = activity as MainActivity
        mainActivity.setBottomNavSelectedItem(R.id.chatListFragment)
        Intent(requireActivity(), ChatRoomFragment::class.java).apply {
            putExtra("name", name)
            startActivity(this)
        }
    }

    override fun onMarkerClick(marker: Marker): Boolean {
        if (marker in customMarkers) {
            val markerData = marker.tag as? MarkerData
            showBottomSheetDialog(markerData?.crewData)
            return true
        }
        return false
    }
}