package apiresponse

data class AirPollutionResponse(var list: ArrayList<DailyPollution>)

data class DailyPollution(
    var main: Main,
    var components: Components,
    var dt: Long
)

data class Main(var aqi: Int)

data class Components(var pm2_5: Double,
                      var pm10: Double)
