package br.com.weatherapp.model

import com.google.gson.annotations.SerializedName

data class WeatherCityResponse(
    @SerializedName("list")
    val data: List<WeatherData>,
)

data class WeatherResponse(
    @SerializedName("list")
    val data: List<WeatherData>,
    val city: WeatherCity
)

data class WeatherData(
    @SerializedName("main")
    val temperature: Temperature,
    @SerializedName("weather")
    val weatherType: List<WeatherType>,
    val wind: Wind,
    @SerializedName("dt_txt")
    val time: String
) {
    fun getDay() = try {
        time.split(" ").first()
    } catch (ex: Exception) {
        ""
    }

    fun getHour() = try {
        time.split(" ").last().dropLast(3)
    } catch (ex: Exception) {
        ""
    }
}

data class Temperature(
    val temp: Double,
    @SerializedName("temp_min")
    val min: Double,
    @SerializedName("temp_max")
    val max: Double,
    val humidity: Int
)

data class Wind(
    val speed: Double
)

data class WeatherType(
    @SerializedName("main")
    val type: String,
    val description: String
)

data class WeatherCity(
    @SerializedName("name")
    val city: String
)

enum class WeatherCondition(val value: String) {
    CLEAR_SKY("Clear"),
    FEW_CLOUDS("Clouds"),
    RAIN("Rain"),
    DRIZZLE("Drizzle"),
    THUNDERSTORM("Thunderstorm"),
    SNOW("Snow"),
    MIST("Mist"),
}

object WeatherCityException : Exception()
object WeatherDataException : Exception()