package com.ags.controlekm.workers

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

@HiltWorker
class FinishService @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted params: WorkerParameters,
): CoroutineWorker(appContext, params){

    override suspend fun doWork(): Result {
        TODO("Not yet implemented")
    }

}