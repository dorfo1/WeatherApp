package br.com.weatherapp.features.main.search

import android.content.Context
import androidx.fragment.app.testing.FragmentScenario
import br.com.weatherapp.R
import br.com.weatherapp.base.Resource
import br.com.weatherapp.data.repository.WeatherRepository
import br.com.weatherapp.extensions.convertFileToObject
import br.com.weatherapp.extensions.insertText
import br.com.weatherapp.extensions.isDisplayed
import br.com.weatherapp.extensions.isVisible
import br.com.weatherapp.extensions.perfomImeAction
import br.com.weatherapp.model.WeatherCityException
import br.com.weatherapp.model.WeatherCityResponse
import br.com.weatherapp.model.WeatherDataException
import io.mockk.coEvery
import kotlinx.coroutines.flow.flow
import org.koin.test.KoinTest
import org.koin.test.inject

fun SearchFragmentTest.withTest(func: SearchFragmentRobot.() -> Unit) =
    SearchFragmentRobot().apply(func)

class SearchFragmentRobot : KoinTest {

    private val weatherRepository: WeatherRepository by inject()
    private val context: Context by inject()

    fun withFetchWeatherSuccess() {
        val response = "city_weather.json".convertFileToObject<WeatherCityResponse>()
        coEvery { weatherRepository.fetchWeather(any()) } returns flow {
            emit(Resource.Success(response))
        }
    }

    fun withFetchWeatherError() {
        coEvery { weatherRepository.fetchWeather(any()) } returns flow {
            emit(Resource.Error(WeatherDataException))
        }
    }

    fun withFetchCityError() {
        coEvery { weatherRepository.fetchWeather(any()) } returns flow {
            emit(Resource.Error(WeatherCityException))
        }
    }

    infix fun launch(func: SearchFragmentRobot.() -> Unit): SearchFragmentRobot {
        FragmentScenario.launchInContainer(
            SearchFragment::class.java,
            themeResId = R.style.Theme_WeatherApp_NoActionBar
        )
        return this.apply(func)
    }

    fun insertText(text: String) = R.id.edit_text.insertText(text)

    fun performActionImeClick() = R.id.edit_text.perfomImeAction()

    infix fun check(func: SearchFragmentRobot.Result.() -> Unit) = Result().apply(func)

    inner class Result {
        fun checkEmptyErrorDisplayed() = "Preencha o campo para buscar".isDisplayed()

        fun checkCityErrorDisplayed() = "Falha ao encontrar o clima da cidade".isDisplayed()

        fun checkWeatherErrorDisplayed() = "Falha ao buscar informações sobre o clima".isDisplayed()

        fun checkTryAgainButtonDisplayed() = R.id.btn_try_again.isVisible()

        fun checkSearchFieldIsShow() = R.id.search_et.isVisible()

        fun checkWeatherDataIsVisible() = R.id.rv_weather.isVisible()
    }
}
