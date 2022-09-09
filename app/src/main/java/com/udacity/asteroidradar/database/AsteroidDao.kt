package com.udacity.asteroidradar.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface AsteroidDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(listOfAsteroids: List<DbAsteroid>)

    @Query("SELECT * FROM asteroids WHERE closeApproachDate >= :startDate ORDER BY closeApproachDate ASC")
    fun getAsteroidsForWeek(startDate: String): LiveData<List<DbAsteroid>>

    @Query("SELECT * FROM asteroids WHERE closeApproachDate = :startDate ORDER BY distanceFromEarth ASC")
    fun getAsteroidsForToday(startDate: String): LiveData<List<DbAsteroid>>

    @Query("SELECT * FROM asteroids ORDER BY closeApproachDate ASC")
    fun getAsteroidsAll(): LiveData<List<DbAsteroid>>

    // delete old
    @Query("DELETE FROM asteroids WHERE closeApproachDate < :today")
    fun deleteOldData(today: String)

}