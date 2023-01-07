package br.com.weatherapp.data.repository

import br.com.weatherapp.base.Resource
import br.com.weatherapp.data.api.WeatherApi
import br.com.weatherapp.model.WeatherCityException
import br.com.weatherapp.model.WeatherCityResponse
import br.com.weatherapp.model.WeatherDataException
import br.com.weatherapp.model.WeatherResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException

interface WeatherRepository {
    suspend fun fetchWeather(lat: Double, long: Double): Flow<Resource<WeatherResponse>>

    suspend fun fetchWeather(cityName: String): Flow<Resource<WeatherCityResponse>>
}

class WeatherRepositorImpl(private val weatherApi: WeatherApi) : WeatherRepository {
    override suspend fun fetchWeather(lat: Double, long: Double): Flow<Resource<WeatherResponse>> =
        flow {
            emit(Resource.Loading())
            emit(Resource.Success(weatherApi.fetchWeather(lat, long)))
        }.catch {
            emit(Resource.Error(Exception(it)))
        }

    override suspend fun fetchWeather(cityName: String): Flow<Resource<WeatherCityResponse>> =
        flow {
            val countryCode = "BR"
            emit(Resource.Loading())
            emit(Resource.Success(weatherApi.fetchWeatherByCity("$cityName,$countryCode")))
        }.catch {
            if (it is HttpException) {
                if (it.code() == 500) {
                    emit(Resource.Error(WeatherDataException))
                } else {
                    emit(Resource.Error(WeatherCityException))
                }
            } else {
                emit(Resource.Error(WeatherDataException))
            }
        }

}