package com.zubair.lowestest.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.zubair.lowestest.R
import com.zubair.lowestest.databinding.FragmentCityNameBinding
import com.zubair.lowestest.injection.component.Injectable
import com.zubair.lowestest.viewmodel.WeatherViewModel
import javax.inject.Inject

class CityNameFragment : Fragment(), Injectable {

    private var binding: FragmentCityNameBinding? = null

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel: WeatherViewModel by viewModels{ viewModelFactory }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentCityNameBinding.inflate(inflater, container, false)
        this.binding = binding
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding?.run{
            lookUpButton.setOnClickListener {
                val text = cityText.text.toString()
                if(text.isNotBlank()){
                    viewModel.getForecast(text.toLowerCase())
                    findNavController().navigate(R.id.navigate_to_list_fragment,
                    ListFragment.Args(cityName = text.toLowerCase()).bundle)
                }
                else{
                    Toast.makeText(requireContext(), "Need a city name", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    override fun onDestroyView() {
        binding = null
        super.onDestroyView()
    }

}