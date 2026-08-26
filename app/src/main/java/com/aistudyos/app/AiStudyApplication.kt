package com.aistudyos.app

import android.app.Application
import androidx.work.Configuration
import com.aistudyos.app.data.worker.AppWorkerFactory
import com.cloudinary.android.MediaManager
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class AiStudyApplication : Application(), Configuration.Provider {

    @Inject
    lateinit var workerFactory: AppWorkerFactory

    override fun onCreate() {
        super.onCreate()

        // 🔥 Cloudinary INIT (MOST IMPORTANT)
        val config = mapOf(
            "cloud_name" to "dmsbaxkle"
        )

        MediaManager.init(this, config)
    }

    override val workManagerConfiguration: Configuration
        get() = Configuration.Builder()
            .setWorkerFactory(workerFactory)
            .build()
}