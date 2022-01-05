package weeklyforecast

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherforecast.R
import com.example.weatherforecast.databinding.ListItemBinding

class WeeklyForecastViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
    val binding = ListItemBinding.bind(itemView)
}