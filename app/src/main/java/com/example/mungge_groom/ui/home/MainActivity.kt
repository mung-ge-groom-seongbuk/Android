package com.example.mungge_groom.ui.home

import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.mungge_groom.R
import com.example.mungge_groom.databinding.ActivityMainBinding
import com.example.mungge_groom.ui.account.AccountViewModel
import com.example.mungge_groom.ui.base.BaseActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding>(R.layout.activity_main) {
    private lateinit var navHostController: NavController
    private lateinit var accountViewModel: AccountViewModel
    override fun setLayout() {
        setBottomNav()
        setViewModel()
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
    }
}
