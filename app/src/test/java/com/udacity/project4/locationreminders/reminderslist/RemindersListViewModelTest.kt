package com.udacity.project4.locationreminders.reminderslist

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.udacity.project4.locationreminders.MainCoroutineRule
import com.udacity.project4.locationreminders.data.FakeDataSource
import kotlinx.coroutines.ExperimentalCoroutinesApi
import com.google.common.truth.Truth.assertThat
import com.udacity.project4.locationreminders.data.dto.ReminderDTO
import com.udacity.project4.locationreminders.getOrAwaitValue
import kotlinx.coroutines.test.pauseDispatcher
import kotlinx.coroutines.test.resumeDispatcher
import kotlinx.coroutines.test.runBlockingTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.core.context.stopKoin

@RunWith(AndroidJUnit4::class)
@ExperimentalCoroutinesApi
class RemindersListViewModelTest {

    //TODO: provide testing to the RemindersListViewModel and its live data objects
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    private lateinit var remindersRepository: FakeDataSource

    private lateinit var viewModel: RemindersListViewModel

    @Before
    fun setupViewModel() {
        remindersRepository = FakeDataSource()
        viewModel = RemindersListViewModel(ApplicationProvider.getApplicationContext(), remindersRepository)
    }

    @After
    fun tearDown() {
        stopKoin()
    }

    @Test
    fun loadReminders_loadingStatusShowOrHide() {
        mainCoroutineRule.pauseDispatcher()

        viewModel.loadReminders()

        assertThat(viewModel.showLoading.getOrAwaitValue()).isTrue()

        mainCoroutineRule.resumeDispatcher()

        assertThat(viewModel.showLoading.getOrAwaitValue()).isFalse()

    }

    @Test
    fun loadReminders_loadingCompletedShowData() = mainCoroutineRule.runBlockingTest  {
        val reminder = ReminderDTO("Pyay", "Description", "Pyay City", 18.840981231922036, 95.2579702569604)


        remindersRepository.saveReminder(reminder)
        viewModel.loadReminders()

        assertThat(viewModel.remindersList.getOrAwaitValue()).isNotEmpty()
    }

    @Test
    fun loadReminders_loadingCompletedShowError() {
        mainCoroutineRule.pauseDispatcher()

        remindersRepository.setReturnError(true)

        viewModel.loadReminders()

        mainCoroutineRule.resumeDispatcher()

        assertThat(viewModel.showSnackBar.getOrAwaitValue()).isEqualTo("Error getting reminders")
    }

}