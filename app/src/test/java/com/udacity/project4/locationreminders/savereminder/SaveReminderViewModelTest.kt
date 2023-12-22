package com.udacity.project4.locationreminders.savereminder

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.common.truth.Truth.assertThat
import com.udacity.project4.locationreminders.MainCoroutineRule
import com.udacity.project4.locationreminders.data.FakeDataSource
import com.udacity.project4.locationreminders.getOrAwaitValue
import com.udacity.project4.R
import com.udacity.project4.locationreminders.reminderslist.ReminderDataItem

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.pauseDispatcher
import kotlinx.coroutines.test.resumeDispatcher
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.core.context.stopKoin

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
class SaveReminderViewModelTest {


    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()



    private lateinit var remindersRepository: FakeDataSource

    //Subject under test
    private lateinit var viewModel: SaveReminderViewModel

    @Before
    fun setupViewModel() {
        remindersRepository = FakeDataSource()
        viewModel = SaveReminderViewModel(ApplicationProvider.getApplicationContext(), remindersRepository)
    }

    @After
    fun tearDown() {
        stopKoin()
    }

    @Test
    fun saveReminder_saveDataShowsLoadingAtStartAndHidesWhenDone() {
        mainCoroutineRule.pauseDispatcher()

        val reminder = ReminderDataItem("Pyay", "Description", "Pyay City", 18.840981231922036, 95.2579702569604)

        viewModel.validateAndSaveReminder(reminder)
        assertThat(viewModel.showLoading.getOrAwaitValue()).isTrue()

        mainCoroutineRule.resumeDispatcher()
        assertThat(viewModel.showLoading.getOrAwaitValue()).isFalse()
    }

//    @Test
//    fun viewModel_savingValidItemSucceeds() {
//        val reminder = ReminderDataItem("Pyay", "Description", "Pyay City", 18.840981231922036, 95.2579702569604)
//        viewModel.validateAndSaveReminder(reminder)
//        assertThat(viewModel.showToast.getOrAwaitValue()).isEqualTo(R.string.reminder_saved)
//    }

    @Test
    fun validateEnteredData_EmptyLocationShowErrorSnackBar() {
        val reminder = ReminderDataItem("Pyay", "Description", "", 18.840981231922036, 95.2579702569604)

        assertThat(viewModel.validateEnteredData(reminder)).isFalse()
        assertThat(viewModel.showSnackBarInt.getOrAwaitValue()).isEqualTo(R.string.err_select_location)
    }

    @Test
    fun validateEnteredData_EmptyTitleShowErrorSnackBar() {
        val reminder = ReminderDataItem("", "Description", "Pyay City", 18.840981231922036, 95.2579702569604)

        assertThat(viewModel.validateEnteredData(reminder)).isFalse()
        assertThat(viewModel.showSnackBarInt.getOrAwaitValue()).isEqualTo(R.string.err_enter_title)
    }



}