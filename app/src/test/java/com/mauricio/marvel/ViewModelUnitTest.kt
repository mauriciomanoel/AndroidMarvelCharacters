package com.mauricio.marvel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.mauricio.marvel.characters.adapters.CharacterSeriesAdapter
import com.mauricio.marvel.characters.repositories.CharacterRepository
import com.mauricio.marvel.characters.viewmodels.CharacterViewModel
import com.mauricio.marvel.utils.getOrAwaitValue
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.HiltTestApplication
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import javax.inject.Inject


@HiltAndroidTest
@RunWith(RobolectricTestRunner::class)
@Config(application = HiltTestApplication::class)
@ExperimentalCoroutinesApi
class ViewModelUnitTest {
    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @Mock
    lateinit var viewModel: CharacterViewModel
    @Inject
    lateinit var repository: CharacterRepository

    @Before
    fun setUp() {
        hiltRule.inject()
        Dispatchers.setMain(StandardTestDispatcher())
        viewModel = CharacterViewModel(repository)
    }

    @ExperimentalCoroutinesApi
    @After
    fun setDown() {
        Dispatchers.resetMain() // reset main dispatcher to the original Main dispatcher
    }

    @Test
    fun `Check Total Character`() = runBlocking {
        viewModel.getCharactersInSeries()
        assertEquals(17, viewModel.characters.getOrAwaitValue().size)
    }
}