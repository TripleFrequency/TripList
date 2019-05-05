package me.michaelhaas.triplist.ui

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.*
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.scrollTo
import androidx.test.espresso.contrib.RecyclerViewActions.scrollToPosition
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.filters.LargeTest
import androidx.test.rule.ActivityTestRule
import androidx.test.runner.AndroidJUnit4
import me.michaelhaas.triplist.R
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.Matchers.allOf
import org.hamcrest.TypeSafeMatcher
import org.junit.Assert
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@LargeTest
class CreateUserTripTest {

    @get:Rule
    var activityTestRule = ActivityTestRule(MainActivity::class.java)

    @Test
    fun testCreateUserTripFlow() {
        val initialUserTripCount =
            activityTestRule.activity.findViewById<RecyclerView>(R.id.user_trip_recycler).adapter?.itemCount ?: 0

        // Click "All Trips" tab
        val allTripsButton = onView(
            allOf(
                childAtPosition(
                    childAtPosition(
                        withId(R.id.tabs_trips),
                        0
                    ),
                    1
                ),
                isDisplayed()
            )
        )
        allTripsButton.perform(click())

        // Open first Trip in list
        val firstTrip = onView(
            allOf(
                withId(R.id.trip_container),
                childAtPosition(
                    withId(R.id.user_trip_recycler),
                    0
                ),
                isDisplayed()
            )
        )
        firstTrip.perform(click())

        // Open editor
        var editorFab = onView(
            allOf(
                withId(R.id.trip_fab),
                isDisplayed()
            )
        )
        editorFab.perform(click())

        // Open start date picker
        val startDateEdit = onView(
            allOf(
                withId(R.id.trip_start_date),
                isDisplayed()
            )
        )
        startDateEdit.perform(click())

        // Use default value (today)
        val startDateClosePickerButton = onView(
            withId(android.R.id.button1)
        )
        startDateClosePickerButton.perform(scrollTo(), click())

        // Open end date picker
        val endDateEdit = onView(
            allOf(
                withId(R.id.trip_end_date),
                isDisplayed()
            )
        )
        endDateEdit.perform(click())

        // Use default value (today)
        val endDateClosePickerButton = onView(
            withId(android.R.id.button1)
        )
        endDateClosePickerButton.perform(scrollTo(), click())

        // Save trip
        val saveButton = onView(
            allOf(
                withId(R.id.trip_save_button),
                isDisplayed()
            )
        )
        saveButton.perform(click())

        // Go back to trip list
        pressBack()

        // Go back to user trip list
        pressBack()

        // Verify trip now exists in list
        val newUserTrip = onView(
            allOf(
                withId(R.id.trip_container),
                childAtPosition(
                    withId(R.id.user_trip_recycler),
                    initialUserTripCount
                ),
                isDisplayed()
            )
        )
        newUserTrip.perform(click())

        // Open editor
        editorFab = onView(
            allOf(
                withId(R.id.trip_fab),
                isDisplayed()
            )
        )
        editorFab.perform(click())

        // Click trash button
        val trashButton = onView(
            allOf(
                withId(R.id.trip_delete_button),
                isDisplayed()
            )
        )
        trashButton.perform(click())

        // Click delete button in dialog
        val deleteButton = onView(
            allOf(
                withId(android.R.id.button1),
                isDisplayed()
            )
        )
        deleteButton.perform(click())
    }

    private fun childAtPosition(
        parentMatcher: Matcher<View>, position: Int
    ): Matcher<View> {

        return object : TypeSafeMatcher<View>() {
            override fun describeTo(description: Description) {
                description.appendText("Child at position $position in parent ")
                parentMatcher.describeTo(description)
            }

            public override fun matchesSafely(view: View): Boolean {
                val parent = view.parent
                return parent is ViewGroup && parentMatcher.matches(parent)
                        && view == parent.getChildAt(position)
            }
        }
    }
}