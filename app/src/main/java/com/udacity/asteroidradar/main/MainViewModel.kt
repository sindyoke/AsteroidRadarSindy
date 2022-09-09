package com.udacity.asteroidradar.main

import android.app.Application
import androidx.lifecycle.*
import com.udacity.asteroidradar.BuildConfig
import com.udacity.asteroidradar.database.AsteroidDatabase
import com.udacity.asteroidradar.util.Constants
import com.udacity.asteroidradar.domain.PictureOfDay
import com.udacity.asteroidradar.domain.Asteroid
import com.udacity.asteroidradar.network.AsteroidApi
import com.udacity.asteroidradar.repository.AsteroidRepository
import kotlinx.coroutines.launch
import retrofit2.HttpException
import timber.log.Timber
import java.io.IOException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

enum class Filter { WEEK, TODAY, ALL }

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val asteroidsRepository = AsteroidRepository(AsteroidDatabase.getInstance(application))

    private val _dailyImage = MutableLiveData<PictureOfDay>()
    val dailyImage: LiveData<PictureOfDay>
        get() = _dailyImage

    val list = asteroidsRepository.filteredAsteroids

    private val _navigateToAsteroidDetail = MutableLiveData<Asteroid>()
    val navigateToAsteroidDetail: LiveData<Asteroid>
        get() = _navigateToAsteroidDetail

    init {
        refreshAsteroidsFromRepository()
        loadImageOfDay()
    }

    private fun refreshAsteroidsFromRepository() {
        viewModelScope.launch {
            try {
                asteroidsRepository.setFilter(Filter.WEEK)
                asteroidsRepository.refreshAsteroids()
            } catch (ex: UnknownHostException) {
                Timber.e("UnknownHostException: $ex")
            } catch (ex: HttpException) {
                Timber.e("HttpException: $ex")
            } catch (ex: IOException) {
                Timber.e("IOException: $ex")
            } catch (ex: SocketTimeoutException) {
                Timber.e("SocketTimeoutException: $ex")
            }
        }
    }

    private fun loadImageOfDay() {
        viewModelScope.launch {
            try {
                val pictureOfDay = AsteroidApi.retrofitService.getDailyImage(Constants.API_KEY)

                if (pictureOfDay.mediaType == "image") {
                    _dailyImage.value = pictureOfDay
                }
            } catch (ex: UnknownHostException) {
                Timber.e("UnknownHostException: $ex")
            } catch (ex: HttpException) {
                Timber.e("HttpException: $ex")
            } catch (ex: IOException) {
                Timber.e("IOException: $ex")
            } catch (ex: SocketTimeoutException) {
                Timber.e("SocketTimeoutException: $ex")
            }
        }
    }

    fun showAsteroidDetails(asteroid: Asteroid) {
        _navigateToAsteroidDetail.value = asteroid
    }

    fun showAsteroidDetailsComplete() {
        null.also { _navigateToAsteroidDetail.value = it }
    }

    fun updateFilter(filter: Filter) {
        asteroidsRepository.setFilter(filter)
    }
}
