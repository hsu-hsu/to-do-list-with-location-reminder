package com.udacity.project4.locationreminders.data.local

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.SmallTest;
import com.udacity.project4.locationreminders.data.dto.ReminderDTO

import org.junit.Before;
import org.junit.Rule;
import org.junit.runner.RunWith;

import kotlinx.coroutines.ExperimentalCoroutinesApi;
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.notNullValue
import org.hamcrest.MatcherAssert.assertThat
import org.junit.After
import org.junit.Test

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
//Unit test the DAO
@SmallTest
class RemindersDaoTest {

//    TODO: Add testing implementation to the RemindersDao.kt
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var database: RemindersDatabase

    @Before
    fun initDb() {
        // Using an in-memory database so that the information stored here disappears when the
        // process is killed.
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            RemindersDatabase::class.java
        ).allowMainThreadQueries().build()
    }


    @After
    fun closeDb() = database.close()

    @Test
    fun saveReminderAndGetById() = runBlockingTest {
        val reminder = ReminderDTO("Home", "My Home Location", "My Home", 18.430731733049956, 95.55270436831037)


        database.reminderDao().saveReminder(reminder)

        val result = database.reminderDao().getReminderById(reminder.id)


        assertThat(result as ReminderDTO, notNullValue())
        assertThat(result.id, `is`(reminder.id))
        assertThat(result.title, `is`(reminder.title))
        assertThat(result.description, `is`(reminder.description))
        assertThat(result.location, `is`(reminder.location))
        assertThat(result.latitude, `is`(reminder.latitude))
        assertThat(result.longitude, `is`(reminder.longitude))

    }

    @Test
    fun getAllRemindersFromDb() = runBlockingTest {
        val reminder = ReminderDTO("Wang Mala Hotpot", "Hot pot Shop", "Onnut District", 13.708813801102695, 100.59974272404128)
        val reminder2 = ReminderDTO("Home", "My Home Location", "My Home", 18.430731733049956, 95.55270436831037)

        database.reminderDao().saveReminder(reminder)
        database.reminderDao().saveReminder(reminder2)

        val remindersList = database.reminderDao().getReminders()

        assertThat(remindersList, `is`(notNullValue()))
    }

    @Test
    fun deleteAllReminders() = runBlockingTest {
        val reminder = ReminderDTO("Wang Mala Hotpot", "Hot pot Shop", "Onnut District", 13.708813801102695, 100.59974272404128)
        val reminder2 = ReminderDTO("Home", "My Home Location", "My Home", 18.430731733049956, 95.55270436831037)

        database.reminderDao().saveReminder(reminder)
        database.reminderDao().saveReminder(reminder2)

        database.reminderDao().deleteAllReminders()

        val remindersList = database.reminderDao().getReminders()

        assertThat(remindersList, `is`(emptyList()))
    }

}