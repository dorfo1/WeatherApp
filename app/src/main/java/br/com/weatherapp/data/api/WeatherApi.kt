package br.com.weatherapp.data.api

import br.com.weatherapp.model.WeatherCityResponse
import br.com.weatherapp.model.WeatherResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface WeatherApi {

    @GET("forecast")
    suspend fun fetchWeather(@Query("lat") lat: Double, @Query("lon") long: Double): WeatherResponse

    @GET("forecast")
    suspend fun fetchWeatherByCity(@Query("q") city:String): WeatherCityResponse
}