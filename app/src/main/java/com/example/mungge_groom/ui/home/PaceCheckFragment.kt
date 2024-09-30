package com.example.mungge_groom.ui.home

import com.example.mungge_groom.R
import com.example.mungge_groom.data.model.PaceCheckDate
import com.example.mungge_groom.databinding.FragmentPaceCheckBinding
import com.example.mungge_groom.ui.base.BaseFragment

class PaceCheckFragment(private val paceCheckDate: PaceCheckDate) : BaseFragment<FragmentPaceCheckBinding>(R.layout.fragment_pace_check){
    override fun setLayout() {
        binding.paceCheck = paceCheckDate
    }
}