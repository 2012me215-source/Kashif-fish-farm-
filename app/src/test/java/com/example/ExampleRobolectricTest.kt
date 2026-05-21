package com.example

import android.content.Context
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.room.Room
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import com.example.data.FarmDatabase
import com.example.data.FarmRepository
import com.example.ui.FarmAppScreen
import com.example.viewmodel.FarmViewModel
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import org.robolectric.annotation.GraphicsMode

@RunWith(RobolectricTestRunner::class)
@GraphicsMode(GraphicsMode.Mode.NATIVE)
@Config(sdk = [36])
class ExampleRobolectricTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private lateinit var database: FarmDatabase
    private lateinit var repository: FarmRepository
    private lateinit var viewModel: FarmViewModel

    @Before
    fun setUp() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        database = Room.inMemoryDatabaseBuilder(context, FarmDatabase::class.java)
            .allowMainThreadQueries()
            .build()
        repository = FarmRepository(database.farmDao())
        viewModel = FarmViewModel(repository)
    }

    @After
    fun tearDown() {
        if (::database.isInitialized) {
            database.close()
        }
    }

    @Test
    fun testMainActivityLaunch() {
        ActivityScenario.launch(MainActivity::class.java).use { scenario ->
            scenario.onActivity { activity ->
                assertNotNull(activity)
            }
        }
    }

    @Test
    fun testAppStartingAndNavigation() {
        composeTestRule.setContent {
            FarmAppScreen(viewModel = viewModel)
        }

        // Wait for Compose idle to ensure everything is rendered
        composeTestRule.waitForIdle()

        // Click on the Fish & Sales navigation item
        composeTestRule.onNodeWithTag("nav_fish_sales").performClick()
        composeTestRule.waitForIdle()

        // Click on the Expenses navigation item
        composeTestRule.onNodeWithTag("nav_expenses").performClick()
        composeTestRule.waitForIdle()

        // Click back to Dashboard
        composeTestRule.onNodeWithTag("nav_dashboard").performClick()
        composeTestRule.waitForIdle()
    }

    @Test
    fun readStringFromContext() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val appName = context.getString(R.string.app_name)
        assertEquals("Kashif Aqua Farm", appName)
    }
}
