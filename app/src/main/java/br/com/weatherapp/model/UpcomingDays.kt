package br.com.weatherapp.model

import android.os.Build
import java.time.LocalDate

data class UpcomingDays(
    val date: String,
    val maxTemp: Double,
    val minTemp: Double,
    val weather: WeatherType
) {
    companion object {
        // should compare with local date object
        fun fromWeatherDataList(data: List<WeatherData>): List<UpcomingDays> {
            val upcomingDays = mutableListOf<UpcomingDays>()
            var date = ""
            for (weather in data) {
                if (weather.getDay() != date) {
                    date = weather.getDay()
                    val list = data.filter { date == it.getDay() }
                    val maxTemp = list.maxOfOrNull { it.temperature.max }
                    val minTemp = list.minOfOrNull { it.temperature.min }
                    upcomingDays.add(
                        UpcomingDays(
                            date = date,
                            maxTemp = maxTemp ?: 0.0,
                            minTemp = minTemp ?: 0.0,
                            weather = weather.weatherType.first()
                        )
                    )
                }
            }
            return upcomingDays
        }
    }
}