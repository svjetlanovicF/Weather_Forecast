package weeklyforecast

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import apiresponse.DailyData
import com.example.weatherforecast.R
import helper.WeatherForecastHelper
import com.squareup.picasso.Picasso

class WeeklyForecastAdapter(private val context: Context, private var weeklyList: List<DailyData>) :
    RecyclerView.Adapter<WeeklyForecastViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeeklyForecastViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item, parent, false)
        return WeeklyForecastViewHolder(view)
    }

    override fun onBindViewHolder(holder: WeeklyForecastViewHolder, position: Int) {
        holder.binding.apply {
            dayTv.text = WeatherForecastHelper.format(weeklyList[position].dt, "dd.MM.yyyy.")
            sunsetTv.text = context.getString(
                R.string.hour, WeatherForecastHelper.format(weeklyList[position].sunset, "HH:mm")
            )
            sunriseTv.text = context.getString(
                R.string.hour, WeatherForecastHelper.format(weeklyList[position].sunrise, "HH:mm")
            )
            maxTempTv.text = context.getString(
                R.string.temperature,
                WeatherForecastHelper.getCelsius(weeklyList[position].temp.max)
            )
            minTempTv.text = context.getString(
                R.string.temperature,
                WeatherForecastHelper.getCelsius(weeklyList[position].temp.min)
            )
            windTv.text = weeklyList[position].wind_speed.toString()
            val iconId = weeklyList[position].weather[0].icon
            val url: String = context.getString(R.string.image_url, iconId)
            Picasso.get().load(url).into(icon)
        }
    }

    override fun getItemCount(): Int {
        return weeklyList.size
    }
}