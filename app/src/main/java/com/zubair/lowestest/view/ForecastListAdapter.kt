package com.zubair.lowestest.view

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.zubair.lowestest.R
import com.zubair.lowestest.databinding.ItemViewForecastBinding
import com.zubair.lowestest.model.storage.Forecast
import kotlinx.android.synthetic.main.item_view_forecast.view.*

class ForecastListAdapter(
    private val forecastList: ArrayList<Forecast>,
    private val cityName: String) :
    RecyclerView.Adapter<ForecastListAdapter.ForecastViewHolder>(), ForecastClickListener {

    fun updateList(newList: List<Forecast>) {
        forecastList.clear()
        forecastList.addAll(newList)
        notifyDataSetChanged()
    }

    inner class ForecastViewHolder(private val binding: ItemViewForecastBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Forecast, forecastListener: ForecastClickListener) {
            binding.run {
                forecast = item
                listener = forecastListener
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ForecastViewHolder(
            ItemViewForecastBinding.inflate(
                LayoutInflater.from(parent.context),
                parent, false
            )
        )

    override fun onBindViewHolder(holder: ForecastViewHolder, position: Int) {
        holder.bind(forecastList[position], this)
    }

    override fun getItemCount() = forecastList.size

    override fun onForecastItemClicked(view: View) {
        Log.d("Test", "what's happenign here id clicked ===  ${view.forecastId.text}")
        view.findNavController().navigate(R.id.navigate_to_detail_fragment,
            DetailFragment.Args(view.forecastId.text.toString(), cityName).bundle)
    }
}