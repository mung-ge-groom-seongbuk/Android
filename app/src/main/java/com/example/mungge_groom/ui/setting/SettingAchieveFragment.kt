package com.example.mungge_groom.ui.setting

import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import com.example.mungge_groom.R
import com.example.mungge_groom.databinding.FragmentSettingGoalBinding
import com.example.mungge_groom.ui.base.BaseFragment

class SettingAchieveFragment : BaseFragment<FragmentSettingGoalBinding>(R.layout.fragment_setting_goal){
    override fun setLayout() {

        val spinner = binding.settingGoalTypeSp
        val adapter = ArrayAdapter.createFromResource(
            requireContext(),
            R.array.goal_options,  // strings.xml에 정의된 배열
            android.R.layout.simple_spinner_item  // 기본 스피너 레이아웃
        )

        // 스피너의 드롭다운 레이아웃 설정
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        // 스피너에 어댑터 설정
        spinner.adapter = adapter

        // 스피너 항목 선택 시 이벤트 처리
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                val selectedItem = parent.getItemAtPosition(position).toString()
                // 선택된 항목 처리 로직 추가

            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // 아무 항목도 선택되지 않았을 때 처리
            }
        }

    }
}