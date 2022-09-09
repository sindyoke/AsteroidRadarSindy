package com.udacity.asteroidradar

import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.udacity.asteroidradar.database.AsteroidDao
import com.udacity.asteroidradar.database.AsteroidDatabase
import org.junit.After
import org.junit.Before
import org.junit.runner.RunWith
import java.io.IOException

@RunWith(AndroidJUnit4::class)
class AsteriodDatabaseTest {

    private lateinit var asteroidDao: AsteroidDao
    private lateinit var db: AsteroidDatabase

    @Before
    fun createDb() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        // Using an in-memory database because the information stored here disappears when the
        // process is killed.
        db = Room.inMemoryDatabaseBuilder(context, AsteroidDatabase::class.java)
            // Allowing main thread queries, just for testing.
            .allowMainThreadQueries()
            .build()
        asteroidDao = db.asteroidDao
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

//        @Test
//        @Throws(Exception::class)
//        fun insertAndGetNight() {
//            val night = DbAsteroid()
//            asteroidDao.insert()
//            val tonight = asteroidDao.getAsteroidsFromToday()
//            Assert.assertEquals(tonight?.sleepQuality, -1)
//        }

}