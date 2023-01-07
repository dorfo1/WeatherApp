package br.com.weatherapp.base

import android.app.Dialog
import android.content.Context
import android.widget.LinearLayout
import androidx.recyclerview.widget.RecyclerView
import br.com.weatherapp.R
import br.com.weatherapp.model.UpcomingDays

class UpcomingDaysView(
    context: Context,
    upComingDays: List<UpcomingDays>,
) : Dialog(context, R.style.FullScreenDialog) {

    init {
        val linearLayout = LinearLayout(context)
        linearLayout.orientation = LinearLayout.VERTICAL
        val params = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.MATCH_PARENT
        )

        val view = layoutInflater.inflate(R.layout.layout_upcoming_days_list, null)

        val recyclerView = view.findViewById<RecyclerView>(R.id.rv_upcoming_days)
        val upcomingDaysAdapter = UpcomingDaysAdapter(items = upComingDays)

        recyclerView.adapter = upcomingDaysAdapter

        linearLayout.addView(view, params)
        addContentView(linearLayout, params)
    }
}

