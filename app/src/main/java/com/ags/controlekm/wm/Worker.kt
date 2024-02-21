package com.ags.controlekm.wm

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.ags.controlekm.ui.views.serviceManager.viewModel.ServiceViewModel

class Worker(
    context: Context,
    workerParams: WorkerParameters,
    private val serviceViewModel: ServiceViewModel
): Worker(context, workerParams) {

    override fun doWork(): Result {

        

        return Result.success()
    }

}