package com.example.weatherforecast

import airpollution.AirPollutionFragment
import android.content.Intent
import android.location.Location
import android.net.Uri
import android.os.Bundle
import android.view.MenuItem
import android.widget.FrameLayout
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.FragmentTransaction
import apiresponse.WeatherResponse
import com.example.weatherforecast.databinding.ActivityMainBinding
import com.google.android.material.navigation.NavigationView
import com.google.gson.Gson
import location.MyTask
import okhttp3.*
import weeklyforecast.WeeklyForecastFragment
import java.io.IOException
import java.util.*

class
MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var task: MyTask

    private lateinit var toggle: ActionBarDrawerToggle
    private lateinit var weatherForecastFragment: WeatherForecastFragment
    private lateinit var fragmentTransaction: FragmentTransaction
    private var weatherResponse: WeatherResponse? = null
    private var time: Long = 0
    private var location: Location? = null

    fun getWeatherResponse(): WeatherResponse?{
        return weatherResponse
    }

    fun getTime(): Long{
        return time
    }

    fun getLocation(): Location?{
        return location
    }

    fun setLocation(location: Location){
        this.location = location
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        weatherForecastFragment = WeatherForecastFragment()
        fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(binding.fragmentContainer.id, weatherForecastFragment).commitNow()

        task = MyTask(this)

        toggle =
            ActionBarDrawerToggle(
                this,
                binding.drawerLayout,
                R.string.open_drawer,
                R.string.close_drawer
            )
        binding.drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        binding.navigationView.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.home -> {
                        if (!weatherForecastFragment.isAdded) {
                            fragmentTransaction = supportFragmentManager.beginTransaction()
                            fragmentTransaction.replace(
                                binding.fragmentContainer.id,
                                weatherForecastFragment
                            ).commit()
                            binding.drawerLayout.closeDrawer(GravityCompat.START)
                        }
                    true
                }
                R.id.weekly_weather -> {
                    fragmentTransaction = supportFragmentManager.beginTransaction()
                    fragmentTransaction.replace(binding.fragmentContainer.id, WeeklyForecastFragment()).addToBackStack(null).commit()
                    binding.drawerLayout.closeDrawer(GravityCompat.START)
                    true
                }
                R.id.air_pollution -> {
                    fragmentTransaction = supportFragmentManager.beginTransaction()
                    fragmentTransaction.replace(binding.fragmentContainer.id, AirPollutionFragment()).addToBackStack(null).commit()
                    binding.drawerLayout.closeDrawer(GravityCompat.START)
                    true
                }
                R.id.vega -> {
                    intent = Intent()
                    intent.action = Intent.ACTION_VIEW
                    intent.addCategory(Intent.CATEGORY_BROWSABLE)
                    intent.data = Uri.parse("https://www.beta.vegait.rs/")
                    startActivity(intent)
                    binding.drawerLayout.closeDrawer(GravityCompat.START)
                    true
                }
                else -> false
            }
        }
  }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (toggle.onOptionsItemSelected(item)) {
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    fun getWeatherDataFromApi(location: Location) {
        val weatherUrl = getString(
            R.string.get_weather_url,
            location.latitude.toString(),
            location.longitude.toString()
        )
        this.location = location
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
                        val wResponse = gson.fromJson(responseBody, WeatherResponse::class.java)
                        weatherResponse = wResponse
                        time = location.time
                        this@MainActivity.runOnUiThread {
                            weatherForecastFragment.updateUi(wResponse, time)
                        }
                    }
                }
            }
        })
    }

    override fun onResume() {
        super.onResume()
        task.startLocationUpdates()
    }

    override fun onPause() {
        super.onPause()
        task.stopLocationUpdates()
    }
}
