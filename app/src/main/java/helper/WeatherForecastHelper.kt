package helper

import android.icu.text.DecimalFormat
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

class WeatherForecastHelper {

    companion object {

        fun getCelsius(farenhajt: Double): String {
            val format = DecimalFormat("#")
            return format.format(farenhajt - 273.15).toString()
        }

        fun convert(minutes: Long): Long {
            return (minutes * 60) * 1000
        }

        fun formatDateTime(time: Long): String{
            val format: DateFormat = SimpleDateFormat("dd.MM.yyyy. HH:mm", Locale.GERMAN)
            val date = Date(time)
            return format.format(date)
        }

        fun format(timeMillis: Long, formatString: String): String{
            val time = timeMillis * 1000L
            val date = Date(time)
            val format = SimpleDateFormat(formatString, Locale.getDefault())
            format.timeZone = TimeZone.getTimeZone("GMT")
            return format.format(date)
        }
    }
}