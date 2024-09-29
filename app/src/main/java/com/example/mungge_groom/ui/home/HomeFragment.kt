package com.example.mungge_groom.ui.home

import androidx.fragment.app.Fragment
import com.example.mungge_groom.R
import com.example.mungge_groom.ui.base.BaseFragment
import com.example.mungge_groom.ui.base.FragmentAdapter
import com.example.mungge_groom.databinding.FragmentHomeBinding
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding>(R.layout.fragment_home) {
    private lateinit var pageList: List<Fragment>
    override fun setLayout() {
        setPageFragment()
    }

    private fun setPageFragment() {
        pageList = listOf(Toggle1Fragment(), Toggle2Fragment())

        val fragmentStateAdapter = FragmentAdapter(requireActivity())
        fragmentStateAdapter.fragments = pageList
        binding.pager.adapter = fragmentStateAdapter

        val tabTitle = listOf("match", "map")

        TabLayoutMediator(binding.tab, binding.pager) { tab, position ->
            tab.text = tabTitle[position]
        }.attach()

        binding.tab.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(p0: TabLayout.Tab?) {
                p0?.let {
                    binding.pager.currentItem = it.position
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
    }


}