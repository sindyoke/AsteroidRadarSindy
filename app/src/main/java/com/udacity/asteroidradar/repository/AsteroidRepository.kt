package com.udacity.asteroidradar.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.switchMap
import com.udacity.asteroidradar.BuildConfig
import com.udacity.asteroidradar.util.Constants
import com.udacity.asteroidradar.network.AsteroidApi
import com.udacity.asteroidradar.network.getNextSevenDaysFormattedDates
import com.udacity.asteroidradar.network.parseAsteroidsJsonResult
import com.udacity.asteroidradar.database.AsteroidDatabase
import com.udacity.asteroidradar.database.asDomainModel
import com.udacity.asteroidradar.domain.Asteroid
import com.udacity.asteroidradar.main.Filter
import com.udacity.asteroidradar.network.asDatabaseModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import timber.log.Timber
import java.util.*

class AsteroidRepository(private val database: AsteroidDatabase) {

    private val selectedFilter = MutableLiveData<Filter>()

    private val asteroidsWeek: LiveData<List<Asteroid>> = Transformations.map(
        database.asteroidDao.getAsteroidsForWeek(getNextSevenDaysFormattedDates()[0])
    ) {
        it.asDomainModel()
    }

    private val asteroidsToday: LiveData<List<Asteroid>> = Transformations.map(
        database.asteroidDao.getAsteroidsForToday(getNextSevenDaysFormattedDates()[0])
    ) {
        it.asDomainModel()
    }

    private val asteroidsAll: LiveData<List<Asteroid>> = Transformations.map(
        database.asteroidDao.getAsteroidsAll()
    ) {
        it.asDomainModel()
    }

    suspend fun refreshAsteroids() {
        Timber.d("refreshing asteroids")
        withContext(Dispatchers.IO) {
            val listResponse = AsteroidApi.retrofitService.getAsteroids(
                getNextSevenDaysFormattedDates()[0], "", Constants.API_KEY
            )
            val asteroidsList = parseAsteroidsJsonResult(JSONObject(listResponse))
            database.asteroidDao.insertAll(asteroidsList.asDatabaseModel())
            Timber.d("inserting refreshed asteroids to database")
        }
    }

    suspend fun refreshTodaysAsteroids() {
        Timber.d("refreshing today's asteroids")
        withContext(Dispatchers.IO) {
            val listResponse = AsteroidApi.retrofitService.getAsteroids(
                getNextSevenDaysFormattedDates()[0], getNextSevenDaysFormattedDates()[0], Constants.API_KEY
            )
            val asteroidsList = parseAsteroidsJsonResult(JSONObject(listResponse))
            database.asteroidDao.insertAll(asteroidsList.asDatabaseModel())
            Timber.d("inserting refreshed today's asteroids to database")
        }
    }

    val filteredAsteroids: LiveData<List<Asteroid>> = selectedFilter.switchMap {
        when(it) {
            Filter.ALL -> asteroidsAll
            Filter.TODAY -> asteroidsToday
            else -> asteroidsWeek
        }
    }

    fun setFilter(filter: Filter) {
        selectedFilter.value = filter
    }
}