package tlt.th.co.toyotaleasing.worker

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters

class AnalyticsUploadWorker (appContext: Context, workerParams: WorkerParameters) : Worker(appContext, workerParams) {

    override fun doWork(): Result {
        return  Result.success()
    }

    private fun uploadAnalyticsToServer() {

    }
}