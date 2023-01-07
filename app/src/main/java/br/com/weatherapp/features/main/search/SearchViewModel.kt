package br.com.weatherapp.features.main.search

import androidx.annotation.StringRes
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.weatherapp.R
import br.com.weatherapp.base.Resource
import br.com.weatherapp.data.repository.WeatherRepository
import br.com.weatherapp.model.WeatherCityException
import br.com.weatherapp.model.WeatherCityResponse
import br.com.weatherapp.model.WeatherResponse
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class SearchViewModel(
    private val weatherRepository: WeatherRepository
) : ViewModel() {

    private val _searchState = MutableLiveData<SearchState>()
    val searchState: LiveData<SearchState> get() = _searchState

    fun fetchWeatherFrom(city: String) {
        if (city.isEmpty()) {
            _searchState.postValue(SearchState.Error(R.string.empty_search_message))
            return
        }
        viewModelScope.launch {
            weatherRepository.fetchWeather(city).collect {
                when (it) {
                    is Resource.Error -> {
                        when (it.exception) {
                            is WeatherCityException -> _searchState.postValue(
                                SearchState.Error(R.string.city_search_message_error)
                            )
                            else -> _searchState.postValue(SearchState.WeatherError)
                        }
                    }
                    is Resource.Loading -> _searchState.postValue(SearchState.Loading)
                    is Resource.Success -> it.data?.let { response ->
                        _searchState.postValue(SearchState.WeatherSuccess(response))
                    } ?: kotlin.run {
                        _searchState.postValue(SearchState.WeatherError)
                    }
                }
            }
        }
    }

    sealed interface SearchState {
        class WeatherSuccess(val data: WeatherCityResponse) : SearchState
        object WeatherError : SearchState
        object Loading : SearchState
        class Error(@StringRes val message: Int) : SearchState
    }
}