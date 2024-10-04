package com.example.mungge_groom.ui.home

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentManager
import androidx.navigation.fragment.findNavController
import com.example.mungge_groom.R
import com.example.mungge_groom.ui.base.BaseFragment
import com.example.mungge_groom.databinding.FragmentToggle2Binding
import com.example.mungge_groom.ui.listener.onClickBottomSheetDialogListener
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.MapView
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class Toggle2Fragment : BaseFragment<FragmentToggle2Binding>(R.layout.fragment_toggle2),onClickBottomSheetDialogListener,
    OnMapReadyCallback {

    private lateinit var mapView: MapView
    private lateinit var googleMap: GoogleMap

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            enableMyLocation()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mapView = binding.mapView
        mapView.onCreate(savedInstanceState)
        mapView.getMapAsync(this)
    }

    override fun setLayout() {
        setOnClick()
    }

    private fun setOnClick(){
        binding.floatingStart.tag = "start"
        binding.floatingStart.setOnClickListener {
            when (binding.floatingStart.tag) {
                "start" -> {
                    with(binding) {
                        // 상태를 "pause"으로 변경하고 이미지 변경
                        floatingStart.tag = "pause"
                        floatingStart.foreground =
                            ContextCompat.getDrawable(requireContext(), R.drawable.ic_button_pause)
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
                }
            }
        }

        binding.floatingSquare.setOnClickListener{
            with(binding) {
                floatingStart.tag = "start"
                floatingStart.foreground =
                    ContextCompat.getDrawable(requireContext(), R.drawable.ic_button_start)
                floatingStart.visibility = View.VISIBLE
                floatingPlay.visibility = View.GONE
                floatingSquare.visibility = View.GONE
            }
        }

        binding.floatingPlay.setOnClickListener{
            with(binding) {
                floatingStart.tag = "pause"
                floatingStart.foreground =
                    ContextCompat.getDrawable(requireContext(), R.drawable.ic_button_pause)
                floatingStart.visibility = View.VISIBLE
                floatingPlay.visibility = View.GONE
                floatingSquare.visibility = View.GONE
            }
        }

        binding.ex.setOnClickListener{
            showBottomSheetDialog()
        }
    }

    override fun onMapReady(map: GoogleMap) {
        googleMap = map
        enableMyLocation()
    }

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
                        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 15f))
                    }
                }
        } else {
            // 권한 요청
            requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }

    private fun showBottomSheetDialog(){
        val modal = HomeBottomSheetDialog(this)
        modal.show(requireActivity().supportFragmentManager, "HomeBottomSheet")
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

    override fun onClick() {
        // 바텀 네비게이션의 선택 상태 변경
        val mainActivity = activity as MainActivity
        mainActivity.setBottomNavMoveItem(R.id.chatListFragment,R.id.action_chatListFragment_to_chatRoomFragment)
    }
}
