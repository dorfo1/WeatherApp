package br.com.weatherapp.base

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import br.com.weatherapp.R
import br.com.weatherapp.databinding.ItemFirstDayViewBinding
import br.com.weatherapp.databinding.ItemUpcomingDaysViewBinding
import br.com.weatherapp.model.UpcomingDays

class UpcomingDaysAdapter(private val items: List<UpcomingDays>) :
    RecyclerView.Adapter<UpcomingDaysAdapter.UpcomingDaysViewHolder>() {

    companion object {
        const val FIRST_VIEW_TYPE = 1
        const val UP_COMING_VIEW_TYPE = 2
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UpcomingDaysViewHolder {
        val inflater = LayoutInflater.from(parent.context)

        val holder = if (viewType == FIRST_VIEW_TYPE) {
            val binding = ItemFirstDayViewBinding.inflate(inflater, parent, false)
            FirstDayViewHolder(binding)
        } else {
            val binding = ItemUpcomingDaysViewBinding.inflate(inflater, parent, false)
            NextDaysViewHolder(binding)
        }

        return holder
    }

    override fun onBindViewHolder(holder: UpcomingDaysViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size

    override fun getItemViewType(position: Int): Int {
        return when (position) {
            0 -> FIRST_VIEW_TYPE
            else -> UP_COMING_VIEW_TYPE
        }
    }

    class NextDaysViewHolder(private val binding: ItemUpcomingDaysViewBinding) :
        UpcomingDaysViewHolder(binding) {

        override fun bind(upcomingDays: UpcomingDays) {
            with(binding) {
                date.text = upcomingDays.date.getDayAndName()
                maxTemp.text = upcomingDays.maxTemp.asCelsius()
                minTemp.text = upcomingDays.minTemp.asCelsius()
                icon.setAnimation(upcomingDays.weather.getWeatherAnimation(false))
            }
        }
    }

    class FirstDayViewHolder(private val binding: ItemFirstDayViewBinding) :
        UpcomingDaysViewHolder(binding) {

        override fun bind(upcomingDays: UpcomingDays) {
            with(binding) {
                maxTemp.text = upcomingDays.maxTemp.asCelsius()
                minTemp.text = upcomingDays.minTemp.asCelsius()
                icon.setAnimation(upcomingDays.weather.getWeatherAnimation(false))

                date.text = if (upcomingDays.date.isToday()) {
                    binding.root.context.getString(
                        R.string.today_date_message,
                        upcomingDays.date.getDay(),
                        upcomingDays.date.getMonth()
                    )
                } else if (upcomingDays.date.isTomorrow()) {
                    binding.root.context.getString(
                        R.string.tomorrow_date_message,
                        upcomingDays.date.getDay(),
                        upcomingDays.date.getMonth()
                    )
                } else {
                    upcomingDays.date.getDayAndName()
                }
            }
        }
    }


    abstract class UpcomingDaysViewHolder(viewBinding: ViewBinding) :
        RecyclerView.ViewHolder(viewBinding.root) {
        abstract fun bind(upcomingDays: UpcomingDays)
    }
}