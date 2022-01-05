package airpollution

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherforecast.R
import com.example.weatherforecast.databinding.AirPollutionItemBinding

class AirPollutionViewHolder (item: View): RecyclerView.ViewHolder(item) {
    val binding = AirPollutionItemBinding.bind(item)
}