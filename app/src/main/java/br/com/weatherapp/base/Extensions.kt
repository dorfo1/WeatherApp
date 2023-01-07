package br.com.weatherapp.base

import android.animation.Animator
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.core.content.ContextCompat
import br.com.weatherapp.R
import br.com.weatherapp.model.WeatherCondition
import br.com.weatherapp.model.WeatherType
import com.airbnb.lottie.LottieAnimationView
import org.koin.core.scope.Scope
import retrofit2.Retrofit
import java.time.LocalDate

fun Context.hideKeyboard(view: View) {
    val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
}

fun LottieAnimationView.doOnEndAnimation(callback: () -> Unit) {
    addAnimatorListener(object : Animator.AnimatorListener {
        override fun onAnimationStart(p0: Animator) {}
        override fun onAnimationEnd(p0: Animator) {
            callback.invoke()
        }

        override fun onAnimationCancel(p0: Animator) {}
        override fun onAnimationRepeat(p0: Animator) {}
    })
}

inline fun <reified T> Scope.resolveRetrofit(retrofit: Retrofit): T {
    return retrofit.create(T::class.java)
}


fun Double.asCelsius(): String {
    val celsius = this - 273
    return "${celsius.toInt()} °C"
}

fun Int.asAirHumidity(): String = "$this %"

fun Double.windAsKm(): String = "$this km/h"

fun View.gone() {
    this.visibility = View.GONE
}

fun View.show() {
    this.visibility = View.VISIBLE
}


fun String.isNight(): Boolean {
    return try {
        val hour = this.take(2)
        return hour.toInt() < 6 || hour.toInt() > 18;
    } catch (ex: Exception) {
        false
    }
}

fun String.getDayAndMonthName(): String? {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val date = LocalDate.parse(this)
        "${date.dayOfMonth}, ${date.month.name}"
    } else {
        null
    }
}

fun String.getDayAndName(): String? {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val date = LocalDate.parse(this)
        return "${date.dayOfWeek.name} - ${date.dayOfMonth}"
    } else {
        null
    }
}

fun String.isToday(): Boolean {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val date = LocalDate.parse(this)
        date.dayOfYear == LocalDate.now().dayOfYear
    } else {
        false
    }
}

fun String.getDay(): String? {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val date = LocalDate.parse(this)
        date.dayOfMonth.toString()
    } else {
        null
    }
}

fun String.getMonth(): String? {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val date = LocalDate.parse(this)
        date.month.name.take(3)
    } else {
        null
    }
}

fun String.isTomorrow(): Boolean {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val date = LocalDate.parse(this)
        date.dayOfYear == LocalDate.now().dayOfYear.plus(1)
    } else {
        false
    }
}

fun Context.checkIfPermissionsAreGranted(vararg permissions: String): Boolean {
    for (p in permissions) {
        if (ContextCompat.checkSelfPermission(this, p) != PackageManager.PERMISSION_GRANTED) {
            return false
        }
    }
    return true
}


fun WeatherType.getWeatherAnimation(isNight: Boolean): Int {
    return when (this.type) {
        WeatherCondition.CLEAR_SKY.value -> if (isNight) R.raw.weather_night else R.raw.weather_sunny
        WeatherCondition.FEW_CLOUDS.value -> if (isNight) R.raw.weather_cloudynight else R.raw.weather_partly_cloudy
        WeatherCondition.RAIN.value, WeatherCondition.DRIZZLE.value -> if (isNight) R.raw.weather_rainynight else R.raw.weather_partly_shower
        WeatherCondition.THUNDERSTORM.value -> R.raw.weather_thunder
        WeatherCondition.SNOW.value -> R.raw.weather_snow
        WeatherCondition.MIST.value -> R.raw.foggy
        else -> R.raw.foggy
    }
}