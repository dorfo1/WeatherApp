package br.com.weatherapp.features.main.home

import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import androidx.core.content.ContextCompat
import androidx.fragment.app.testing.FragmentScenario
import androidx.fragment.app.testing.launchFragment
import androidx.fragment.app.testing.withFragment
import androidx.test.core.app.ActivityScenario
import br.com.weatherapp.R
import br.com.weatherapp.base.Resource
import br.com.weatherapp.data.repository.WeatherRepository
import br.com.weatherapp.extensions.click
import br.com.weatherapp.extensions.convertFileToObject
import br.com.weatherapp.extensions.isDisplayed
import br.com.weatherapp.extensions.isVisible
import br.com.weatherapp.features.main.MainActivity
import br.com.weatherapp.model.WeatherResponse
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.Task
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkObject
import io.mockk.mockkStatic
import kotlinx.coroutines.flow.flow
import org.koin.test.KoinTest
import org.koin.test.inject

fun HomeFragmentTest.withTest(func: HeroesFragmentRobot.() -> Unit) =
    HeroesFragmentRobot().apply { func() }

class HeroesFragmentRobot : KoinTest {

    private val viewModel: HomeViewModel by inject()
    private val repository: WeatherRepository by inject()
    private val context : Context by inject()

    fun withLocation() {
        val locationProvider = mockk<FusedLocationProviderClient>(relaxed = true)
        val task = mockk<Task<Location>>(relaxed = true)
        val location = Location("").apply {
            latitude = 0.0
            longitude = 0.0
        }
        every { task.isSuccessful } returns true
        every { task.result } returns location


        mockkObject(FusedLocationProviderClient::class)
        every { LocationServices.getFusedLocationProviderClient(any()) } returns locationProvider
        every { locationProvider.lastLocation } returns task
        viewModel.setLocation(0.0, 0.0)
    }

    fun withPermissionsGranted() {
        mockkStatic(ContextCompat::class)
        every {
            ContextCompat.checkSelfPermission(
                any(),
                any()
            )
        } returns PackageManager.PERMISSION_GRANTED
    }

    fun withPermissionsDenied() {
        mockkStatic(ContextCompat::class)
        every {
            ContextCompat.checkSelfPermission(
                any(),
                any()
            )
        } returns PackageManager.PERMISSION_DENIED
    }

    fun withServerError() {
        coEvery {
            repository.fetchWeather(any(), any())
        } returns flow { emit(Resource.Error(Exception())) }
    }

    fun withFetchWeatherSuccess() {
        val response = "city_weather.json".convertFileToObject<WeatherResponse>()
        coEvery {
            repository.fetchWeather(any(), any())
        } returns flow { emit(Resource.Success(response)) }
    }

    infix fun launch(func: HeroesFragmentRobot.() -> Unit): HeroesFragmentRobot {
        FragmentScenario.launchInContainer(
            HomeFragment::class.java,
            themeResId = R.style.Theme_WeatherApp_NoActionBar
        )
        return this.apply(func)
    }

    fun clickCalendar() = R.id.iv_calendar.click()


    infix fun check(func: HeroesFragmentRobot.Result.() -> Unit) = Result().apply(func)

    inner class Result {
        fun checkPermissionDeniedMessageIsDisplayed() =
            "Para continuar, você precisa dar permissões de localização".isDisplayed()

        fun checkPermissionButtonIsDisplayed() = R.id.btn_request_permission.isVisible()

        fun checkErrorMessageIsDisplayed() = R.id.tv_error_message.isVisible()

        fun checkTryAgainButtonIsDisplayed() = R.id.btn_try_again.isVisible()

        fun checkTemperatureIsVisible() = R.id.tv_temperature.isVisible()

        fun checkHoursRecyclerIsVisible() = R.id.rv_more_data.isVisible()

        fun checkHoursDayTextIsVisible() = R.id.tv_day.isVisible()

        fun checkWindTextIsVisible() = R.id.tv_wind.isVisible()

        fun checkAirHumidityTextIsVisible() = R.id.tv_air_humidity.isVisible()

        fun checkMoreDataIsDisplayed() = "Próximos dias".isDisplayed()
    }

}