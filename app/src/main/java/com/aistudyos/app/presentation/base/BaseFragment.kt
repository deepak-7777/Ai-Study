package com.aistudyos.app.presentation.base

import android.content.res.Configuration
import android.graphics.Color
import android.os.Build
import android.view.View
import android.view.Window
import androidx.fragment.app.Fragment

open class BaseFragment(layout: Int) : Fragment(layout) {

    override fun onResume() {
        super.onResume()
        setupStatusBar()
    }

    private fun setupStatusBar() {
        val window: Window = requireActivity().window

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.decorView.systemUiVisibility =
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN

            window.statusBarColor = Color.TRANSPARENT
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val decorView: View = window.decorView
            var flags = decorView.systemUiVisibility

            if (isDarkModeOn()) {
                flags = flags and View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR.inv()
            } else {
                flags = flags or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            }

            decorView.systemUiVisibility = flags
        }
    }

    private fun isDarkModeOn(): Boolean {
        val nightModeFlags =
            resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
        return nightModeFlags == Configuration.UI_MODE_NIGHT_YES
    }
}