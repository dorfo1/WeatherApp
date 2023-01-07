package br.com.weatherapp.features.main.search

import br.com.weatherapp.data.repository.WeatherRepository
import br.com.weatherapp.features.main.home.HomeViewModel
import br.com.weatherapp.kointest.KoinTestRule
import io.mockk.mockk
import org.junit.Rule
import org.junit.Test
import org.koin.dsl.module

class SearchFragmentTest{

    private val repository = mockk<WeatherRepository>(relaxed = true)
    private val viewModel = SearchViewModel(repository)
    private val module = module {
        single { repository }
        single { viewModel }
    }

    @get:Rule
    val koinTest = KoinTestRule(listOf(module))

    @Test
    fun whenSearchScreenOpens_checkIntialState(){
        withTest {} launch {
        } check {
            checkSearchFieldIsShow()
        }
    }

    @Test
    fun whenSearchEmptyCity_checkEmptyErrorMessage(){
        withTest {} launch {
            performActionImeClick()
        } check {
            checkSearchFieldIsShow()
            checkEmptyErrorDisplayed()
        }
    }

    @Test
    fun whenWheatherFetchCityError_checkErrorMessage(){
        withTest {
            withFetchCityError()
        } launch {
            insertText("Text")
            performActionImeClick()
        } check {
            checkSearchFieldIsShow()
            checkCityErrorDisplayed()
        }
    }

    @Test
    fun whenWheatherFetchWeatherError_checkErrorView(){
        withTest {
            withFetchWeatherError()
        } launch {
            insertText("Text")
            performActionImeClick()
        } check {
            checkTryAgainButtonDisplayed()
            checkWeatherErrorDisplayed()
        }
    }

    @Test
    fun whenWheatherFetchWeatherError_checkSuccess(){
        withTest {
            withFetchWeatherSuccess()
        } launch {
            insertText("Text")
            performActionImeClick()
        } check {
            checkWeatherDataIsVisible()
        }
    }
}