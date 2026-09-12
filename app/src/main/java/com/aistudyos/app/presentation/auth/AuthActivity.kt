package com.aistudyos.app.presentation.auth

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment
import com.aistudyos.app.R
import com.aistudyos.app.data.local.prefs.SessionManager
import com.aistudyos.app.databinding.ActivityAuthBinding
import com.aistudyos.app.presentation.main.MainActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class AuthActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAuthBinding

    @Inject lateinit var sessionManager: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAuthBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.auth_nav_host) as NavHostFragment

        val navController = navHostFragment.navController

        // ✅ CRITICAL FIX: wait until fragment is resumed
        navHostFragment.viewLifecycleOwnerLiveData.observe(this) { owner ->
            if (owner != null) {

                lifecycleScope.launch {

                    val loggedIn = sessionManager.isLoggedIn().first()
                    if (loggedIn) {
                        goToMain()
                        return@launch
                    }

                    val onboardingDone = sessionManager.isOnboardingDone().first()

                    // ✅ SAFE navigation
                    if (!onboardingDone &&
                        navController.currentDestination != null
                    ) {
                        navController.navigate(R.id.onboardingFragment)
                    }
                }
            }
        }
    }

    fun goToMain() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
}