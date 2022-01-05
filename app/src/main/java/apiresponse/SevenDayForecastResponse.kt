package apiresponse

data class SevenDayForecastResponse(var daily: ArrayList<DailyData>)

data class DailyData(
    var dt: Long,
    var sunrise: Long,
    var sunset: Long,
    var temp: DailyTemp,
    var wind_speed: Double,
    var weather: ArrayList<WeatherIcon>
)

data class DailyTemp(var min: Double,
                     var max: Double)

data class WeatherIcon(var icon: String)

