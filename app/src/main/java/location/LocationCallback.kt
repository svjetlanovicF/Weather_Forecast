package location

import android.content.Context
import android.widget.Toast
import com.example.weatherforecast.MainActivity
import com.google.android.gms.location.LocationAvailability
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationResult

class LocationCallback(private val context: Context) : LocationCallback() {

    override fun onLocationResult(locationResult: LocationResult) {
        super.onLocationResult(locationResult)
        for (location in locationResult.locations) {
            if (context is MainActivity) {
                context.getWeatherDataFromApi(location)
                context.setLocation(location)
            }
        }
    }

    override fun onLocationAvailability(locationAvailability: LocationAvailability) {
        super.onLocationAvailability(locationAvailability)
        if (!locationAvailability.isLocationAvailable) {
            Toast.makeText(
                context,
                "This app needs location data. Please enable location in Settings",
                Toast.LENGTH_LONG
            ).show()
        }
    }
}