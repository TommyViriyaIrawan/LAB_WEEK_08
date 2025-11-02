package com.example.lab_week_08.worker

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters
import androidx.work.Data

class ThirdWorker(
    context: Context,
    workerParams: WorkerParameters
) : Worker(context, workerParams) {

    override fun doWork(): Result {
        val id = inputData.getString(INPUT_DATA_ID)

        Thread.sleep(2000L) // proses dummy 2 detik

        val output = Data.Builder()
            .putString(OUTPUT_DATA_ID, id)
            .build()

        return Result.success(output)
    }

    companion object {
        const val INPUT_DATA_ID = "thirdInId"
        const val OUTPUT_DATA_ID = "thirdOutId"
    }
}
