package br.com.weatherapp.features.main.home

import br.com.weatherapp.data.repository.WeatherRepository
import br.com.weatherapp.kointest.KoinTestRule
import io.mockk.mockk
import org.junit.Rule
import org.junit.Test
import org.koin.dsl.module

class HomeFragmentTest {

    private val repository = mockk<WeatherRepository>(relaxed = true)
    private val homeViewModel = HomeViewModel(repository)
    private val module = module {
        single { repository }
        single { homeViewModel }
    }

    @get:Rule
    val koinTest = KoinTestRule(listOf(module))

    @Test
    fun shouldShowPermissionDeniedView_WhenPermissionDenied() {
        withTest {
            withPermissionsDenied()
        } launch {
        } check {
            checkPermissionDeniedMessageIsDisplayed()
            checkPermissionButtonIsDisplayed()
        }
    }

    @Test
    fun shouldShowWeatherErrorView_WhenServerError() {
        withTest {
            withLocation()
            withPermissionsGranted()
            withServerError()
        } launch {
        } check {
            checkErrorMessageIsDisplayed()
            checkTryAgainButtonIsDisplayed()
        }
    }

    @Test
    fun shouldShowWeatherView_WhenServerSuccess() {
        withTest {
            withPermissionsGranted()
            withLocation()
            withFetchWeatherSuccess()
        } launch {
        } check {
            checkTemperatureIsVisible()
            checkHoursRecyclerIsVisible()
            checkHoursDayTextIsVisible()
            checkWindTextIsVisible()
            checkAirHumidityTextIsVisible()
        }
    }

    @Test
    fun shouldShowMoreWeatherDataView_WhenServerSuccess() {
        withTest {
            withPermissionsGranted()
            withLocation()
            withFetchWeatherSuccess()
        } launch {
            clickCalendar()
        } check {
            checkMoreDataIsDisplayed()
        }
    }
}