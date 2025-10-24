package com.metalinspect.app.presentation

import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.metalinspect.app.R
import com.metalinspect.app.presentation.ui.InspectionDetailFragment
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class InspectionDetailFragmentUiTest {

    @Test
    fun launches_detail_and_handles_missing_args_gracefully() {
        val scenario = launchFragmentInContainer<InspectionDetailFragment>(themeResId = R.style.Theme_MetalInspect)
        scenario.moveToState(androidx.lifecycle.Lifecycle.State.RESUMED)
        // No assertion; this validates the fragment doesn't crash without args and handles error state.
    }
}
