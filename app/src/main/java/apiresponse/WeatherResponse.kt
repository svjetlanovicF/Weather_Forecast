package apiresponse

data class WeatherResponse(
    var main: MainData,
    var weather: ArrayList<WeatherData>,
    var name: String
)

data class MainData(
    var temp: Double,
    var feels_like: Double,
    var temp_min: Double,
    var temp_max: Double
)

data class WeatherData(
    var id: Int,
    var main: String,
    var description: String,
    var icon: String
)

