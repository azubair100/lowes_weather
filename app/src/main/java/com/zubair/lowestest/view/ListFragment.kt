package com.zubair.lowestest.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.zubair.lowestest.R
import com.zubair.lowestest.databinding.FragmentListBinding
import com.zubair.lowestest.injection.component.Injectable
import com.zubair.lowestest.model.storage.Forecast
import com.zubair.lowestest.util.Constants.ARG_CITY_NAME
import com.zubair.lowestest.util.shown
import com.zubair.lowestest.viewmodel.WeatherViewModel
import com.zubair.lowestest.viewmodel.WeatherViewModel.RequestState.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Inject

@ExperimentalCoroutinesApi
class ListFragment : Fragment(), Injectable {

    data class Args(val cityName: String) {
        companion object {
            fun fromBundle(bundle: Bundle) = Args(
                cityName = bundle.getString(ARG_CITY_NAME) ?: error("city name required")
            )
        }

        val bundle: Bundle get() = bundleOf(ARG_CITY_NAME to cityName)
    }

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel: WeatherViewModel by viewModels { viewModelFactory }

    private var binding: FragmentListBinding? = null

    private lateinit var listAdapter: ForecastListAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentListBinding.inflate(inflater, container, false)
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
            viewModel.getForecast(it)
            (activity as MainActivity).supportActionBar?.title = it
            listAdapter = ForecastListAdapter(arrayListOf(), it)
        }
        viewModel.forecastList.observe(viewLifecycleOwner, Observer(::populateForecastList))
        viewModel.requestState.observe(viewLifecycleOwner, Observer(::observeRequestStates))

        binding?.run {
            forecastRecycler.apply {
                layoutManager = LinearLayoutManager(context)
                adapter = listAdapter
            }
        }

    }

    private fun observeRequestStates(requestState: WeatherViewModel.RequestState?) {
        when (requestState) {
            CurrentlyLoading -> showHideViews(showProgressBar = true)
            NonEmptyResult -> showHideViews(showList = true)
            RequestFailedGeneral -> {
                showHideViews(showStatusText = true, text = getString(R.string.empty_list))
            }
            RequestFailedConnectivity -> {
                showHideViews(
                    showStatusText = true,
                    text = getString(R.string.no_connection)
                )
            }
            RequestFailedGeneral -> {
                showHideViews(showStatusText = true, text = getString(R.string.unknown_error))
            }
            else -> showHideViews(showStatusText = true, text = getString(R.string.unknown_error))
        }
    }


    private fun populateForecastList(list: List<Forecast>?) {
        list?.let{ newList ->
            listAdapter.updateList(newList)
            showHideViews(showList = true)
        }
    }

    private fun showHideViews(
        showList: Boolean = false,
        showStatusText: Boolean = false,
        showProgressBar: Boolean = false,
        text: String? = null
    ) {
        binding?.run {
            forecastRecycler.shown = showList
            statusText.shown = showStatusText
            progressBar.shown = showProgressBar
            text?.let {
                statusText.text = it
            }
        }
    }

}