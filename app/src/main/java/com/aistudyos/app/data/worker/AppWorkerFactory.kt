package com.aistudyos.app.data.worker

import androidx.work.DelegatingWorkerFactory
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AppWorkerFactory @Inject constructor() : DelegatingWorkerFactory()
