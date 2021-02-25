package com.zubair.lowestest.view

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.zubair.lowestest.databinding.FragmentDetailBinding
import com.zubair.lowestest.injection.component.Injectable
import com.zubair.lowestest.model.storage.Forecast
import com.zubair.lowestest.util.Constants.ARG_CITY_NAME
import com.zubair.lowestest.viewmodel.DetailViewModel
import javax.inject.Inject

private const val FORECAST_ID = "FORECAST_ID"
class DetailFragment : Fragment(), Injectable {

    data class Args(val forecastId: String, val cityName: String){
        companion object{
            fun fromBundle(bundle: Bundle) = Args(
                forecastId = bundle.getString(FORECAST_ID) ?: error("forecast id required"),
                cityName = bundle.getString(ARG_CITY_NAME) ?: error("forecast city required")
            )
        }

        val bundle: Bundle get() = bundleOf(
            FORECAST_ID to forecastId,
            ARG_CITY_NAME to cityName
        )
    }

    private var binding: FragmentDetailBinding? = null

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel: DetailViewModel by viewModels { viewModelFactory }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentDetailBinding.inflate(inflater, container, false)
        this.binding = binding
        return binding.root
    }

    override fun onDestroyView() {
        binding = null
        super.onDestroyView()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val args = Args.fromBundle(requireArguments())

        args.cityName.let {
            (activity as MainActivity).supportActionBar?.title = it
        }
        args.forecastId.let {
            Log.d("Test", "what's happenign here id ===  $it")
            viewModel.getDetails(it)
        }
        viewModel.forecastLiveData.observe(viewLifecycleOwner, Observer(::bindAlbum))

    }

    private fun bindAlbum(forecast: Forecast?) {
        forecast?.let{ it -> binding?.forecast = it }
    }


}