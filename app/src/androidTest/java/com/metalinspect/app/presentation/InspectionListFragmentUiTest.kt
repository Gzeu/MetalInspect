package com.metalinspect.app.presentation

import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.metalinspect.app.R
import com.metalinspect.app.presentation.ui.InspectionListFragment
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class InspectionListFragmentUiTest {

    @Test
    fun launches_and_shows_empty_state_without_crash() {
        val scenario = launchFragmentInContainer<InspectionListFragment>(themeResId = R.style.Theme_MetalInspect)
        // For now, this simply ensures no crash on start. Enhancements can add
        // Espresso checks for RecyclerView visibility and empty placeholders.
        scenario.moveToState(androidx.lifecycle.Lifecycle.State.RESUMED)
    }
}
