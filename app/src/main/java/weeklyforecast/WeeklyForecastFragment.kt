package weeklyforecast

import android.location.Location
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.weatherforecast.MainActivity
import com.example.weatherforecast.R
import apiresponse.SevenDayForecastResponse
import com.example.weatherforecast.databinding.FragmentWeeklyForecastBinding
import com.google.gson.Gson
import okhttp3.*
import java.io.IOException

class WeeklyForecastFragment : Fragment() {

    private var weeklyForecastBinding: FragmentWeeklyForecastBinding? = null
    private val binding get() = weeklyForecastBinding!!

    private lateinit var weeklyForecastAdapter: WeeklyForecastAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        weeklyForecastBinding =
            FragmentWeeklyForecastBinding.inflate(layoutInflater, container, false)

        binding.recycleView.layoutManager = LinearLayoutManager(activity)
            if ((activity as MainActivity).getLocation() != null) {
                (activity as MainActivity).getLocation()
            }
            (activity as MainActivity).getLocation()?.let { getWeeklyDataFromApi(it) }
        return weeklyForecastBinding?.root
    }

    private fun getWeeklyDataFromApi(location: Location) {
        val weatherUrl = getString(
            R.string.get_weekly_weather_url,
            location.latitude.toString(),
            location.longitude.toString()
        )
        val okHttpClient = OkHttpClient()
        val request: Request = Request.Builder().url(weatherUrl).build()

        okHttpClient.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
            }

            override fun onResponse(call: Call, response: Response) {
                response.use {
                    if (!response.isSuccessful) throw IOException("Unexpected code $response")
                    else {
                        val responseBody = response.body?.string()
                        val gson = Gson()
                        val sevenDayResponse = gson.fromJson(responseBody, SevenDayForecastResponse::class.java)
                        activity?.runOnUiThread {
                            weeklyForecastAdapter = WeeklyForecastAdapter(
                                requireContext(),
                                sevenDayResponse.daily.subList(1, sevenDayResponse.daily.size)
                            )
                            binding.recycleView.adapter = weeklyForecastAdapter
                        }
                    }
                }
            }

        })
    }

}