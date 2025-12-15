package com.sloth.proactify.ui.main
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.compose.ui.test.assertIsDisplayed

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import com.sloth.proactify.di.appModule
import com.sloth.proactify.ui.theme.ProActifyTheme
import com.sloth.proactify.utils.TestTags
import org.koin.test.KoinTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.koin.core.context.loadKoinModules
import org.koin.test.inject

class MainActivityTest : KoinTest {

    @get:Rule
    val composeRule = createComposeRule()

    private val viewModel: MainViewModel by inject()

    @Before
    fun setUp() {
        // Load Koin modules for testing
        loadKoinModules(appModule())

        // Set the content for testing
        composeRule.setContent {
            ProActifyTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MainCompose()
                }
            }
        }
    }

    @Test
    fun clickToggleFab_isVisible() {
        // Check if the FAB (Floating Action Button) is not displayed initially
        composeRule.onNodeWithTag(TestTags.FAB).assertExists()

        // Simulate a click on the FAB
        composeRule.onNodeWithTag(TestTags.FAB).performClick()

        composeRule.waitUntil(timeoutMillis = 7000) {
            composeRule.onNodeWithTag(TestTags.BOTTOM_SHEET, useUnmergedTree = true).assertExists()
            true // Continue checking until the timeout
        }

        // Now assert that the bottom sheet is displayed
        composeRule.onNodeWithTag(TestTags.BOTTOM_SHEET, useUnmergedTree = true)
            .assertIsDisplayed()
    }
}