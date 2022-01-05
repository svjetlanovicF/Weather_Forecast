package location

import android.Manifest
import android.content.Context
import android.content.IntentSender
import android.content.pm.PackageManager
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.weatherforecast.MainActivity
import helper.WeatherForecastHelper
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.google.android.gms.tasks.Task

class MyTask(private val context: Context) {

    private var locationRequest: LocationRequest = LocationRequest.create()
    private var fusedLocationClient: FusedLocationProviderClient
    private var builder: LocationSettingsRequest.Builder
    private var settingsClient: SettingsClient
    private var myLocationCallback: LocationCallback = LocationCallback(context)

    init {
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        locationRequest.interval = WeatherForecastHelper.convert(10)
        locationRequest.fastestInterval = WeatherForecastHelper.convert(1)
        builder =
            LocationSettingsRequest.Builder().addLocationRequest(locationRequest)
        settingsClient = LocationServices.getSettingsClient(context)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
    }

    private val requestCheckSettings = 1
    private val myPermissionRequestAccessFineLocation = 1

    private var permissionRequested: Boolean = false
    private var settingsChangeRequested: Boolean = false


    fun startLocationUpdates() {

        if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)
            == PackageManager.PERMISSION_GRANTED
        ) {
            val task: Task<LocationSettingsResponse> =
                settingsClient.checkLocationSettings(builder.build())

            task.addOnSuccessListener {
                    getLastKnownLocation()
            }

            task.addOnFailureListener { exception ->
                if (exception is ResolvableApiException) {
                    try {
                        if (!settingsChangeRequested) {
                            val resolvable: ResolvableApiException = exception
                            resolvable.startResolutionForResult(
                                (context as MainActivity),
                                requestCheckSettings
                            )

                            settingsChangeRequested = true
                        } else {
                            Toast.makeText(
                                context,
                                "This app needs location data. Please enable location in Settings",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    } catch (sendEx: IntentSender.SendIntentException) {
                        // Ignore the error.
                    }
                }
            }
        } else {
            if (!permissionRequested) {
                ActivityCompat.requestPermissions(
                    context as MainActivity,
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                    myPermissionRequestAccessFineLocation
                )
                permissionRequested = true
            } else {
                Toast.makeText(
                    context,
                    "App does not have permission for location data",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
        fusedLocationClient.requestLocationUpdates(locationRequest, myLocationCallback, null)
    }

    fun stopLocationUpdates() {
        fusedLocationClient.removeLocationUpdates(myLocationCallback)
    }

    private fun getLastKnownLocation() {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)
            == PackageManager.PERMISSION_GRANTED
        ) {
            fusedLocationClient.lastLocation
                .addOnSuccessListener { location ->

                    if (location != null) {
                        if(context is MainActivity){
                            context.getWeatherDataFromApi(location)
                        }
                    }
                }
        }
    }

}