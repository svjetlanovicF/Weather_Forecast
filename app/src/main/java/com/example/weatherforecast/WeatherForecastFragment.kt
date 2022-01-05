package com.example.weatherforecast

import airpollution.AirPollutionFragment
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.FragmentTransaction
import apiresponse.WeatherResponse
import com.example.weatherforecast.databinding.FragmentWeatherForecastBinding
import com.squareup.picasso.Picasso
import helper.WeatherForecastHelper

class WeatherForecastFragment : Fragment() {

    private var weatherForecastBinding: FragmentWeatherForecastBinding? = null
    private val binding get() = weatherForecastBinding!!

    private lateinit var currentLocationTv: TextView
    private lateinit var dateTimeTv: TextView
    private lateinit var temperatureTv: TextView
    private lateinit var feelsLikeTv: TextView
    private lateinit var weatherConditionTv: TextView
    private lateinit var currentStateIv: ImageView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        weatherForecastBinding =
            FragmentWeatherForecastBinding.inflate(layoutInflater, container, false)

        weatherConditionTv = binding.weatherConditionsTv
        dateTimeTv = binding.dateAndTimeTv
        temperatureTv = binding.temperatureTv
        feelsLikeTv = binding.feelsLikeTv
        currentStateIv = binding.currentStateIv
        currentLocationTv = binding.currentLocationTv

        if ((activity as MainActivity).getWeatherResponse() != null && (activity as MainActivity).getTime() > 0) {
            (activity as MainActivity).getWeatherResponse()
                ?.let { updateUi(it, (activity as MainActivity).getTime()) }
        }
        binding.checkButton.setOnClickListener {
            val airPollutionFragment = AirPollutionFragment()
            val ft: FragmentTransaction =
                (activity as MainActivity).supportFragmentManager.beginTransaction()
            ft.replace(R.id.fragment_container, airPollutionFragment).addToBackStack(null).commit()
        }
        return weatherForecastBinding?.root
    }

    companion object

    fun updateUi(weatherResponse: WeatherResponse, time: Long) {

        if(::temperatureTv.isInitialized){
            temperatureTv.text = context?.getString(
                R.string.temperature, WeatherForecastHelper.getCelsius(weatherResponse.main.temp)
            )
        }
        if(::feelsLikeTv.isInitialized){
            feelsLikeTv.text = context?.getString(
                R.string.feels_like, WeatherForecastHelper.getCelsius(weatherResponse.main.feels_like)
            )
        }
        if(::weatherConditionTv.isInitialized){
            weatherConditionTv.text =
                resources.getString(R.string.description, weatherResponse.weather[0].description)
            if (!weatherConditionTv.equals("")) {
                weatherConditionTv.visibility = View.VISIBLE
        }
        }
        if(::currentLocationTv.isInitialized){
            currentLocationTv.text = weatherResponse.name
        }
        if(::currentStateIv.isInitialized){
            val icon: String = weatherResponse.weather[0].icon
            val imageUrl: String? = context?.getString(R.string.image_url, icon)
            Picasso.get().load(imageUrl).into(currentStateIv)
        }
        dateTimeTv.text = WeatherForecastHelper.formatDateTime(time)
    }

    override fun onDestroy() {
        super.onDestroy()
        weatherForecastBinding = null
    }
}