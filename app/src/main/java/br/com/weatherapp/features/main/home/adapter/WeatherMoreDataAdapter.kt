package br.com.weatherapp.features.main.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import br.com.weatherapp.base.asCelsius
import br.com.weatherapp.base.getWeatherAnimation
import br.com.weatherapp.base.isNight
import br.com.weatherapp.databinding.ItemWeatherDataViewBinding
import br.com.weatherapp.model.WeatherData

class WeatherMoreDataAdapter(
    private val list: List<WeatherData>
) : RecyclerView.Adapter<WeatherMoreDataAdapter.WeatherViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeatherViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemWeatherDataViewBinding.inflate(inflater, parent, false)
        return WeatherViewHolder(binding)
    }

    override fun onBindViewHolder(holder: WeatherViewHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount(): Int = list.size

    class WeatherViewHolder(private val binding: ItemWeatherDataViewBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: WeatherData) {
            with(binding) {
                tvHour.text = data.getHour()
                tvTemp.text = data.temperature.temp.asCelsius()
                lottieWeather.setAnimation(
                    data.weatherType.first().getWeatherAnimation(data.getHour().isNight())
                )
                lottieWeather.playAnimation()
            }
        }
    }
}