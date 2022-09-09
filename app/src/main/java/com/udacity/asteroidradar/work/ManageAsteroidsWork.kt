package com.udacity.asteroidradar.work

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.udacity.asteroidradar.database.AsteroidDatabase
import com.udacity.asteroidradar.network.getNextSevenDaysFormattedDates
import com.udacity.asteroidradar.repository.AsteroidRepository
import retrofit2.HttpException
import timber.log.Timber

class ManageAsteroidsWork (appContext: Context, params: WorkerParameters): CoroutineWorker(appContext, params) {
    companion object {
        const val WORK_NAME = "ManageAsteroidsWork"
    }

    override suspend fun doWork(): Result {
        Timber.d("worker doing work")
        val database = AsteroidDatabase.getInstance(applicationContext)
        val repository = AsteroidRepository(database)

        return try {
            database.asteroidDao.deleteOldData(getNextSevenDaysFormattedDates()[0])
            repository.refreshTodaysAsteroids()
            Result.success()
        } catch (e: HttpException) {
            Result.retry()
        }
    }

}