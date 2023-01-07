package br.com.weatherapp.features.main.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.weatherapp.base.Resource
import br.com.weatherapp.data.repository.WeatherRepository
import br.com.weatherapp.model.WeatherResponse
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class HomeViewModel(
    private val weatherRepository: WeatherRepository,
) : ViewModel() {

    private val _homeState = MutableLiveData<HomeState>()
    val homeState: LiveData<HomeState> get() = _homeState

    private var location: Pair<Double, Double>? = null

    fun fetchWeather() {
        location?.let {
            viewModelScope.launch {
                weatherRepository.fetchWeather(it.first, it.second).collect {
                    when (it) {
                        is Resource.Error -> _homeState.postValue(HomeState.WeatherDataError)
                        is Resource.Loading -> _homeState.postValue(HomeState.Loading)
                        is Resource.Success -> it.data?.let { data ->
                            _homeState.postValue(HomeState.ShowWeather(data))
                        } ?: kotlin.run { _homeState.postValue(HomeState.WeatherDataError) }
                    }
                }
            }
        }
    }

    fun setLocation(lat: Double, long: Double) {
        location = Pair(lat, long)
    }

    fun handlePermission(permissions: Map<String,Boolean>) {
        if (permissions.values.contains(false)) {
            _homeState.postValue(HomeState.PermissionDenied)
        } else {
            _homeState.postValue(HomeState.GetLocation)
        }
    }

    fun handlePermissionIsGranted(permissionsGranted: Boolean) {
        if (permissionsGranted) {
            _homeState.postValue(HomeState.GetLocation)
        } else {
            _homeState.postValue(HomeState.AskForPermission)
        }
    }

    sealed interface HomeState {
        object PermissionDenied : HomeState
        object GetLocation : HomeState
        object AskForPermission : HomeState
        object Loading : HomeState
        class ShowWeather(val data: WeatherResponse) : HomeState
        object WeatherDataError : HomeState
    }
}
