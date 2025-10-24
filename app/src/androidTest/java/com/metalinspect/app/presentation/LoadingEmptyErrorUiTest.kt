package com.metalinspect.app.presentation

import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.metalinspect.app.R
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class LoadingEmptyErrorUiTest {

    @Test
    fun empty_state_visible_when_no_items_and_not_loading() {
        // Launch a blank activity and inflate the fragment via layout id
        ActivityScenario.launch(androidx.activity.ComponentActivity::class.java).use { activity ->
            activity.onActivity {
                it.setContentView(R.layout.fragment_inspection_list)
            }
            onView(withId(R.id.progressLoading)).check(matches(withEffectiveVisibility(Visibility.GONE)))
            onView(withId(R.id.viewEmpty)).check(matches(withEffectiveVisibility(Visibility.VISIBLE)))
            onView(withId(R.id.recyclerInspections)).check(matches(withEffectiveVisibility(Visibility.GONE)))
        }
    }
}
