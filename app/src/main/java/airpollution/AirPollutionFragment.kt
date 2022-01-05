package airpollution

import android.location.Location
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import apiresponse.AirPollutionResponse
import com.example.weatherforecast.MainActivity
import com.example.weatherforecast.R
import com.example.weatherforecast.databinding.FragmentAirPolutionBinding
import com.google.gson.Gson
import helper.WeatherForecastHelper
import okhttp3.*
import java.io.IOException

class AirPollutionFragment : Fragment() {

    private var fragmentAirPolutionBinding: FragmentAirPolutionBinding? = null
    private val binding get() = fragmentAirPolutionBinding!!

    private lateinit var airPollutionAdapter: AirPollutionAdapter
    private val value: Long =  518400 //six day in seconds

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        fragmentAirPolutionBinding = FragmentAirPolutionBinding.inflate(layoutInflater, container, false)
        binding.airPollutionRecycleview.layoutManager = LinearLayoutManager(activity)
        if((activity as MainActivity).getLocation() != null){
            (activity as MainActivity).getLocation()
        }
        (activity as MainActivity).getTime()
        (activity as MainActivity).getLocation()?.let { getAirPollutionFromApi(it) }

        return fragmentAirPolutionBinding?.root
    }

    private fun getAirPollutionFromApi(location: Location) {

        val start = (System.currentTimeMillis() / 1000) - value

        val weatherUrl = getString(
            R.string.get_air_pollution,
            location.latitude.toString(),
            location.longitude.toString(),
            start.toString(),
            (System.currentTimeMillis() / 1000).toString()
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
                        val airPollutionResponse = gson.fromJson(responseBody, AirPollutionResponse::class.java)
                        activity?.runOnUiThread {
                            val newList = airPollutionResponse.list.filter {
                                dailyPollution -> WeatherForecastHelper.format(
                                dailyPollution.dt,
                                "HH"
                            ) == "12"
                            }
                            airPollutionAdapter = AirPollutionAdapter(requireContext(), newList)
                            binding.airPollutionRecycleview.adapter = airPollutionAdapter
                        }
                    }
                }
            }

        })
    }
}