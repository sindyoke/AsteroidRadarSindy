package com.udacity.asteroidradar.database

import androidx.lifecycle.Transformations.map
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.udacity.asteroidradar.domain.Asteroid

/**
 * Database entities go in this file. These are responsible for reading and writing from the
 * database.
 */

@Entity(tableName = "asteroids")
data class DbAsteroid (@PrimaryKey val id: Long, val codename: String, val closeApproachDate: String,
                       val absoluteMagnitude: Double, val estimatedDiameter: Double,
                       val relativeVelocity: Double, val distanceFromEarth: Double,
                       val isPotentiallyHazardous: Boolean)

fun List<DbAsteroid>.asDomainModel(): List<Asteroid> {
    return map {
        Asteroid(
            id = it.id,
            codename = it.codename,
            closeApproachDate = it.closeApproachDate,
            absoluteMagnitude = it.absoluteMagnitude,
            estimatedDiameter = it.estimatedDiameter,
            relativeVelocity = it.relativeVelocity,
            distanceFromEarth = it.distanceFromEarth,
            isPotentiallyHazardous = it.isPotentiallyHazardous
        )
    }
}

fun DbAsteroid.asDomainModel(): Asteroid {
    return Asteroid(
            id = this.id,
            codename = this.codename,
            closeApproachDate = this.closeApproachDate,
            absoluteMagnitude = this.absoluteMagnitude,
            estimatedDiameter = this.estimatedDiameter,
            relativeVelocity = this.relativeVelocity,
            distanceFromEarth = this.distanceFromEarth,
            isPotentiallyHazardous = this.isPotentiallyHazardous
        )

}
