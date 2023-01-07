package br.com.weatherapp.extensions

import android.content.res.AssetManager
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.ViewInteraction
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.pressImeActionButton
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.platform.app.InstrumentationRegistry
import br.com.weatherapp.actions.waitId
import br.com.weatherapp.model.WeatherCityResponse
import com.google.gson.Gson
import java.util.*

fun Int.waitAndCheckVisibile(): ViewInteraction =
    onView(withId(this)).perform(waitId(this))
        .check(matches(ViewMatchers.withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)))

fun Int.click(): ViewInteraction = onView(withId(this)).perform(ViewActions.click())

fun Int.hasText(text: String): ViewInteraction =
    onView(withId(this)).check(matches(ViewMatchers.withText(text)))

fun Int.insertText(text: String): ViewInteraction =
    onView(withId(this)).perform(ViewActions.typeText(text))

fun Int.perfomImeAction(): ViewInteraction = onView(withId(this)).perform(pressImeActionButton())

fun Int.isGone(): ViewInteraction = onView(withId(this))
    .check(matches(ViewMatchers.withEffectiveVisibility(ViewMatchers.Visibility.GONE)))

fun Int.isVisible(): ViewInteraction = onView(withId(this))
    .check(matches(ViewMatchers.withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)))

fun String.isDisplayed(): ViewInteraction = onView(withText(this))
    .check(matches(ViewMatchers.withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)))


fun AssetManager.readAssetsFile(fileName: String): String =
    open(fileName).bufferedReader().use { it.readText() }

inline fun <reified T> String.convertFileToObject(): T {
    val inputStream =
        InstrumentationRegistry.getInstrumentation().context.resources.assets.readAssetsFile(this)
    val s = Scanner(inputStream).useDelimiter("\\A")
    val json = if (s.hasNext()) s.next() else ""
    return Gson().fromJson(json, T::class.java)
}

