package com.aistudyos.app.presentation.main

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.aistudyos.app.R
import com.aistudyos.app.databinding.ActivityMainBinding
import com.aistudyos.app.presentation.auth.AuthActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.main_nav_host) as NavHostFragment
        navController = navHostFragment.navController

        binding.bottomNav.setupWithNavController(navController)

        // 🔥 START: hide by default
        binding.bottomNav.visibility = View.GONE

        navController.addOnDestinationChangedListener { _, destination, _ ->

            when (destination.id) {

                // ✅ Only allow these screens to show nav (but don't force show)
                R.id.dashboardFragment,
                R.id.subjectListFragment,
                R.id.settingsFragment -> {
                    binding.bottomNav.visibility = View.VISIBLE
                }

                else -> {
                    binding.bottomNav.visibility = View.GONE
                }
            }

            setupStatusBar(destination.id)
        }
    }

    // 🔥 Called from Fragment when data is ready
    fun showBottomNav() {
        binding.bottomNav.visibility = View.VISIBLE
    }

    fun hideBottomNav() {
        binding.bottomNav.visibility = View.GONE
    }

    private fun setupStatusBar(destinationId: Int) {

        val window = this.window

        window.statusBarColor = getColor(R.color.bg_card)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            var flags = window.decorView.systemUiVisibility
            flags = flags or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            window.decorView.systemUiVisibility = flags
        }
    }

    fun logout() {
        startActivity(Intent(this, AuthActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        })
    }
}