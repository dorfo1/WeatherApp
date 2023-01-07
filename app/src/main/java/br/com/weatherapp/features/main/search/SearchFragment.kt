package br.com.weatherapp.features.main.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.NavArgs
import androidx.navigation.fragment.navArgs
import br.com.weatherapp.base.UpcomingDaysAdapter
import br.com.weatherapp.base.gone
import br.com.weatherapp.base.show
import br.com.weatherapp.databinding.FragmentSearchBinding
import br.com.weatherapp.model.UpcomingDays
import br.com.weatherapp.model.WeatherCityResponse
import br.com.weatherapp.model.WeatherResponse
import org.koin.android.ext.android.inject

class SearchFragment : Fragment() {

    private val viewModel: SearchViewModel by inject()
    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupObservers()
        setupListeners()
        with(binding) {
            searchEt.setOnSearchListener {
                viewModel.fetchWeatherFrom(it)
            }
        }
    }

    private fun setupListeners() {
        binding.layoutError.btnTryAgain.setOnClickListener {
            viewModel.fetchWeatherFrom(binding.searchEt.getText())
        }
    }

    private fun setupObservers() {
        viewModel.searchState.observe(viewLifecycleOwner) { state ->
            if (state is SearchViewModel.SearchState.WeatherSuccess || state is SearchViewModel.SearchState.WeatherError) {
                with(binding) {
                    searchEt.clearError()
                    progressBar.gone()
                }
            }

            when (state) {
                is SearchViewModel.SearchState.Error -> handleError(requireContext().getString(state.message))
                is SearchViewModel.SearchState.Loading -> handleLoading()
                is SearchViewModel.SearchState.WeatherError -> handleWeatherError()
                is SearchViewModel.SearchState.WeatherSuccess -> handleSuccess(state.data)
            }
        }
    }

    private fun handleSuccess(data: WeatherCityResponse) {
        with(binding) {
            rvWeather.show()
            layoutError.root.gone()
            rvWeather.adapter = UpcomingDaysAdapter(UpcomingDays.fromWeatherDataList(data.data))
        }
    }

    private fun handleWeatherError() {
        with(binding) {
            rvWeather.gone()
            layoutError.root.show()
        }
    }

    private fun handleLoading() {
        with(binding) {
            rvWeather.gone()
            progressBar.show()
        }
    }

    private fun handleError(message: String) {
        with(binding) {
            searchEt.setError(message)
            rvWeather.gone()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}