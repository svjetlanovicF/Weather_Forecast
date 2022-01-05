package airpollution

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import apiresponse.DailyPollution
import com.example.weatherforecast.R
import helper.WeatherForecastHelper

class AirPollutionAdapter(private val context: Context, private var airPollution: List<DailyPollution>):
    RecyclerView.Adapter<AirPollutionViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AirPollutionViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.air_pollution_item, parent, false)
        return AirPollutionViewHolder(view)
    }

    override fun onBindViewHolder(holder: AirPollutionViewHolder, position: Int) {
        holder.binding.apply {
            dayTv.text = WeatherForecastHelper.format(airPollution[position].dt, "dd.MM.yyyy. HH:mm")
            indexTv.text = airPollution[position].main.aqi.toString()
            pm25Tv.text = airPollution[position].components.pm2_5.toString()
            pm10Tv.text = airPollution[position].components.pm10.toString()
        }
    }

    override fun getItemCount(): Int {
        return airPollution.size
    }
}