package com.example.mungge_groom.ui.home

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import com.example.mungge_groom.R
import com.example.mungge_groom.databinding.FragmentToggle2Binding
import com.example.mungge_groom.ui.base.BaseFragment
import com.example.mungge_groom.ui.chat.ChatRoomFragment
import com.example.mungge_groom.ui.listener.onClickBottomSheetDialogListener
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class Toggle2Fragment : BaseFragment<FragmentToggle2Binding>(R.layout.fragment_toggle2),
    onClickBottomSheetDialogListener,
    OnMapReadyCallback {

    private lateinit var mapView: MapView
    private lateinit var googleMap: GoogleMap

    @RequiresApi(Build.VERSION_CODES.P)
    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val locationGranted = permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true
        val foregroundServiceGranted = permissions[Manifest.permission.FOREGROUND_SERVICE] == true
        val notificationsGranted = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            permissions[Manifest.permission.POST_NOTIFICATIONS] == true
        } else {
            true
        }

        // 권한이 모두 허용되었는지 확인하고 enableMyLocation 호출
        if (locationGranted && foregroundServiceGranted && notificationsGranted) {
            if (::googleMap.isInitialized) { // 지도가 초기화되었는지 확인
                enableMyLocation()
            }
        } else {
            // 권한이 거부된 경우 알림 표시
            Toast.makeText(requireContext(), "위치, 서비스 및 알림 권한이 필요합니다.", Toast.LENGTH_SHORT).show()
        }
    }



    @RequiresApi(Build.VERSION_CODES.P)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mapView = binding.mapView
        mapView.onCreate(savedInstanceState)
        mapView.getMapAsync(this)
        requestPermissions()
    }

    @RequiresApi(Build.VERSION_CODES.P)
    override fun setLayout() {
        setOnClick()
    }

    @RequiresApi(Build.VERSION_CODES.P)
    private fun setOnClick() {
        binding.floatingStart.tag = "start"
        binding.floatingStart.setOnClickListener {
            when (binding.floatingStart.tag) {
                "start" -> {
                    if (ContextCompat.checkSelfPermission(
                            requireContext(),
                            Manifest.permission.ACCESS_FINE_LOCATION
                        ) == PackageManager.PERMISSION_GRANTED &&
                        ContextCompat.checkSelfPermission(
                            requireContext(),
                            Manifest.permission.FOREGROUND_SERVICE
                        ) == PackageManager.PERMISSION_GRANTED &&
                        (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q || ContextCompat.checkSelfPermission(
                            requireContext(),
                            Manifest.permission.FOREGROUND_SERVICE_LOCATION
                        ) == PackageManager.PERMISSION_GRANTED)
                    ) {
                        // 권한이 부여된 경우 서비스 시작
                        with(binding) {
                            floatingStart.tag = "pause"
                            floatingStart.foreground =
                                ContextCompat.getDrawable(
                                    requireContext(),
                                    R.drawable.ic_button_pause
                                )
                        }
                        enableMyLocation()
                        val intent = Intent(requireContext(), ServiceRun::class.java)
                        ContextCompat.startForegroundService(requireContext(), intent)
                    } else {
                        // 권한 요청
                        val permissions = mutableListOf(
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.FOREGROUND_SERVICE,
                            Manifest.permission.POST_NOTIFICATIONS
                        )
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                            permissions.add(Manifest.permission.FOREGROUND_SERVICE_LOCATION)
                        }
                        requestPermissionLauncher.launch(permissions.toTypedArray())
                    }
                }

                "pause" -> {
                    with(binding) {
                        floatingStart.tag = "stop"
                        // 상태를 "stop"로 변경하고 이미지 변경
                        floatingPlay.visibility = View.VISIBLE
                        floatingSquare.visibility = View.VISIBLE
                        floatingStart.visibility = View.GONE
                    }
                    ServiceRun.getInstance()?.pauseRunning()
                    Toast.makeText(requireContext(), "일시 중지되었습니다.", Toast.LENGTH_SHORT).show()
                }
            }
        }

        binding.floatingSquare.setOnClickListener {
            with(binding) {
                floatingStart.tag = "start"
                floatingStart.foreground =
                    ContextCompat.getDrawable(requireContext(), R.drawable.ic_button_start)
                floatingStart.visibility = View.VISIBLE
                floatingPlay.visibility = View.GONE
                floatingSquare.visibility = View.GONE
                showCustomToast()
                ServiceRun.getInstance()?.stopRunning()
            }
        }

        binding.floatingPlay.setOnClickListener {
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

        binding.ex.setOnClickListener {
            showBottomSheetDialog()
        }
    }

    @RequiresApi(Build.VERSION_CODES.P)
    override fun onMapReady(map: GoogleMap) {
        googleMap = map
        enableMyLocation() // 지도가 준비된 후에만 위치를 활성화합니다.
    }


    @RequiresApi(Build.VERSION_CODES.P)
    private fun enableMyLocation() {
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            googleMap.isMyLocationEnabled = true

            // 현재 위치 가져오기
            LocationServices.getFusedLocationProviderClient(requireActivity()).lastLocation
                .addOnSuccessListener { location: Location? ->
                    location?.let {
                        val currentLocation = LatLng(it.latitude, it.longitude)
                        googleMap.addMarker(MarkerOptions().position(currentLocation).title("내 위치"))
                        googleMap.moveCamera(
                            CameraUpdateFactory.newLatLngZoom(
                                currentLocation,
                                15f
                            )
                        )
                    }
                }
        } else {
            // 권한이 거부된 경우 다시 권한 요청
            requestPermissions()
        }
    }

    @RequiresApi(Build.VERSION_CODES.P)
    private fun requestPermissions() {
        // 권한 요청 팝업 띄우기
        val permissionsToRequest = mutableListOf<String>()

        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            permissionsToRequest.add(Manifest.permission.ACCESS_FINE_LOCATION)
        }

        // Android 13 이상에서 알림 권한 확인 및 요청
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
            ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            permissionsToRequest.add(Manifest.permission.POST_NOTIFICATIONS)
        }

        // 필요한 권한이 있을 경우 권한 요청
        if (permissionsToRequest.isNotEmpty()) {
            requestPermissionLauncher.launch(permissionsToRequest.toTypedArray())
        } else {
            // 모든 권한이 이미 허용되어 있다면 알림 표시
            Toast.makeText(requireContext(), "모든 권한이 이미 허용되었습니다.", Toast.LENGTH_SHORT).show()
        }
    }



    private fun showBottomSheetDialog() {
        val modal = HomeBottomSheetDialog(this)
        modal.show(requireActivity().supportFragmentManager, "HomeBottomSheet")
    }

    private fun showCustomToast() {
        val tMg = CustomToast(requireContext())
        tMg.showToast("런닝이 종료되었습니다.", Toast.LENGTH_SHORT)
    }

    // MapView의 생명주기 메서드 관리
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
        // 바텀 네비게이션의 선택 상태 변경
        val mainActivity = activity as MainActivity
        mainActivity.setBottomNavSelectedItem(R.id.chatListFragment)
        Intent(requireActivity(), ChatRoomFragment::class.java).apply {
            putExtra("name", name)
            startActivity(this)
        }
    }
}
