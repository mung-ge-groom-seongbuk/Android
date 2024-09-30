package com.example.mungge_groom.ui.mypage

import android.view.View
import androidx.core.content.ContextCompat
import com.example.mungge_groom.R
import com.example.mungge_groom.data.model.PaceCheckDate
import com.example.mungge_groom.databinding.FragmentMyPageBinding
import com.example.mungge_groom.extention.GlobalApplication
import com.example.mungge_groom.ui.base.BaseFragment
import com.example.mungge_groom.ui.base.FragmentAdapter
import com.example.mungge_groom.ui.home.PaceCheckFragment
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MyPageFragment : BaseFragment<FragmentMyPageBinding>(R.layout.fragment_my_page) {

    override fun setLayout() {
        setTabLayout()
        GlobalApplication.loadProfileImage(binding.fragmentMyPageProfileIv,"data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAMgAAADICAMAAACahl6sAAAAIVBMVEXY2Njz8/Pq6urv7+/h4eHb29vo6Oje3t7j4+Pt7e3p6ekmc3lwAAADMElEQVR4nO2bC3KDMAxEMeab+x+4JZQBEkhBlq2NZt8JvGOtPkZUFSGEEEIIIYQQQgghhBBCCCEEnXbo6hjDLzHW3dBan0dEO9ThjfrrxDQHKv60NNZnu0ETz2Q8w+xbpPQfZTyl9NZnvEB7GlS7AIP3SnNFxgR4fHVXdYTQWZ/1A+14XUcII2x4tf+6fE8EVXJXB6qS+zpAldzyx8Jofep3buSrLXC563L9eAWsnrRSHSFg2eRSX3JMbX32Lb1cRwhIHaQg865E69OviJ0+g+P3pAsBupLEC8G5koSUNQOSuBJqyAJGLRnShQzWGp4kRxZKbKXrCMFaw4SCRTBMomARDJMIB5E9CGOJgtcx3J7Yn8wgdCluhGjogMi/FEIhmXBjdjdC3BRENy2Km6bRTRvvZrDyM+q6eXxw8xzk5oHOz5Opm0dsP58V3Hzo8fPpzc3HUD+fp90sDPhZ4fCzVONnzcnN4pmfVUA/y5mVm3XZys8Cs5+V8srNkv+Ek98uJpz8CDPh5NekGRc/ixFCCCHky2mb4TGO8cLkHuM4PoYGsGfph1r0Ih/rAWc2Oexz74DRE59PHre0GE8pvcoiykxnF2O96Ln3nNFGSqMs4ymlfIT9/1Qio/ADS6vojVe6gilMZUXrnFKbKfeeqiWUed5OXti4QgHTZ3THltxv9fnDaiFveOVKukfkTMRJmxr3yaakiM23ZLJ8cR2ZlBjoyKKksD8W1H1ipENdiWQ/QwflrYJidfAd1b2bQn3JMYrdiknCWlFLXSo/VqSgZRNDg8wo2STzPHgFlZnRPLAmNILLNGMtKGQus5K+J73Am5X0PckL9MYlZCW1mJin3oXEFAzikIk0l8BcSOKVAF1I2pVA1JCFlFpiffY9ch0wuXdGnoFVvnPqIf6JCaJd3CJtHQH69z3Sbh4ssuSxZX3ud2Q6oKrhjKwmwllEahI4i0hNAjJSbZGNV9anPkKiA64cTkhKIlijNSNptwCTlixtPawPfcRDIARoyl2RzLtuhACWEVkhcSPE+szHUAgaFIIGhaBBIWhQCBoUggaFoEEhaFAIGhSCBoWgQSFoUAgap8f9Ac1KQOtCVp1TAAAAAElFTkSuQmCC")
    }

    private fun setTabLayout() {
        val pageList = listOf(
            PaceCheckFragment(PaceCheckDate("", "", "")),
            PaceCheckFragment(PaceCheckDate("", "", "")),
            PaceCheckFragment(PaceCheckDate("", "", ""))
        )

        val fragmentStateAdapter = FragmentAdapter(requireActivity())
        fragmentStateAdapter.fragments = pageList
        binding.fragmentMyPageVp.adapter = fragmentStateAdapter

        val tabTitle = listOf("년", "월", "일")

        TabLayoutMediator(binding.fragmentMyPageYearTv, binding.fragmentMyPageVp) { tab, position ->
            tab.text = tabTitle[position]
        }.attach()

        binding.fragmentMyPageYearTv.addOnTabSelectedListener(object :
            TabLayout.OnTabSelectedListener {
            override fun onTabSelected(p0: TabLayout.Tab?) {
                p0?.let {
                    binding.fragmentMyPageVp.currentItem = it.position
                }
            }

            override fun onTabUnselected(p0: TabLayout.Tab?) {
                p0?.let {

                }
            }

            override fun onTabReselected(p0: TabLayout.Tab?) {
                p0?.let {

                }
            }
        })

        val entry_chart = ArrayList<BarEntry>()
        entry_chart.add(BarEntry(1f, 10f))
        entry_chart.add(BarEntry(2f, 40f))
        entry_chart.add(BarEntry(3f, 20f))
        entry_chart.add(BarEntry(4f, 40f))
        entry_chart.add(BarEntry(5f, 40f))
        entry_chart.add(BarEntry(6f, 40f))
        entry_chart.add(BarEntry(7f, 30f))
        setupBarChart(requireView(), binding.chart.id, entry_chart, false, 10, 0.75f)


    }

    private fun setupBarChart(view: View, chartId: Int, entries: ArrayList<BarEntry>, showGrid: Boolean, labelCount: Int, barWidth: Float) {
        val barChart = view.findViewById<BarChart>(chartId)

        // 데이터 셋 생성
        val barDataSet = BarDataSet(entries, "Data Set")
        barDataSet.color = ContextCompat.getColor(view.context, R.color.selector_tab_text) // 막대 색상 설정

        // BarData에 데이터셋 추가
        val barData = BarData(barDataSet)
        barData.barWidth = barWidth

        // 차트에 데이터 설정
        barChart.data = barData

        // X축 라벨 개수 설정
        val xAxis = barChart.xAxis
        xAxis.setDrawGridLines(showGrid) // 그리드 표시 여부
        xAxis.granularity = 1f
        xAxis.labelCount = labelCount

        // Y축 설정 (좌측)
        val leftAxis = barChart.axisLeft
        leftAxis.setDrawGridLines(showGrid) // 그리드 표시 여부
        leftAxis.axisMinimum = 0f // 최소값을 0으로 설정
        leftAxis.axisMaximum = 50f // 최대값을 100으로 설정

        // Y축 설정 (우측 비활성화)
        val rightAxis = barChart.axisRight
        rightAxis.isEnabled = false

        // 애니메이션 설정
        barChart.animateY(1000)

        // 차트 스타일 설정
        barChart.setFitBars(true)
        barChart.invalidate() // 차트 갱신

        barChart.setTouchEnabled(false)

        // 확대/축소 비활성화
        barChart.setScaleEnabled(false)
        barChart.setPinchZoom(false)
    }
}
