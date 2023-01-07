package br.com.weatherapp.features.main.home

import android.Manifest
import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import br.com.weatherapp.base.UpcomingDaysView
import br.com.weatherapp.base.asAirHumidity
import br.com.weatherapp.base.asCelsius
import br.com.weatherapp.base.checkIfPermissionsAreGranted
import br.com.weatherapp.base.getDayAndMonthName
import br.com.weatherapp.base.getWeatherAnimation
import br.com.weatherapp.base.gone
import br.com.weatherapp.base.isNight
import br.com.weatherapp.base.show
import br.com.weatherapp.base.windAsKm
import br.com.weatherapp.databinding.FragmentHomeBinding
import br.com.weatherapp.features.main.home.adapter.WeatherMoreDataAdapter
import br.com.weatherapp.model.UpcomingDays
import br.com.weatherapp.model.WeatherResponse
import com.google.android.gms.location.LocationServices
import org.koin.android.ext.android.inject

class HomeFragment : Fragment() {

    private val viewModel: HomeViewModel by inject()

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var weatherAdapter: WeatherMoreDataAdapter

    private val requestPermissionResultLauncher =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) {
            viewModel.handlePermission(it)
        }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.handlePermissionIsGranted(checkHasLocationPermission())
        setupObservers()
        setupViews()
    }


    @SuppressLint("MissingPermission")
    private fun getLastLocation() {
        LocationServices.getFusedLocationProviderClient(requireActivity()).lastLocation.addOnSuccessListener {
            viewModel.setLocation(it.latitude, it.longitude)
            viewModel.fetchWeather()
        }
    }

    private fun setupViews() {
        with(binding) {
            layoutError.btnTryAgain.setOnClickListener { viewModel.fetchWeather() }
            layoutPermissionDenied.btnRequestPermission.setOnClickListener { askForLocationPermission() }
            ivCalendar.setOnClickListener { showNextDays() }
        }
    }

    private fun setupObservers() {
        with(viewModel) {
            homeState.observe(viewLifecycleOwner) {
                when (it) {
                    is HomeViewModel.HomeState.AskForPermission -> askForLocationPermission()
                    is HomeViewModel.HomeState.GetLocation -> getLastLocation()
                    is HomeViewModel.HomeState.Loading -> showLoading()
                    is HomeViewModel.HomeState.PermissionDenied -> showPermissionNotGrantedView()
                    is HomeViewModel.HomeState.ShowWeather -> showWeather(it.data)
                    is HomeViewModel.HomeState.WeatherDataError -> showWeatherError()
                }
            }
        }
    }

    private fun showWeather(data: WeatherResponse) {
        with(binding) {
            layoutError.root.gone()
            layoutPermissionDenied.root.gone()
            progressBar.gone()
            sucessGroup.show()

            val dayWeather = data.data.first()
            tvCity.text = data.city.city
            tvDay.text = dayWeather.getDay().getDayAndMonthName()
            tvTemperature.text = dayWeather.temperature.temp.asCelsius()
            tvWind.text = dayWeather.wind.speed.windAsKm()
            tvAirHumidity.text = dayWeather.temperature.humidity.asAirHumidity()
            weatherLottie.setAnimation(
                dayWeather.weatherType.first().getWeatherAnimation(dayWeather.getHour().isNight())
            )
            weatherLottie.playAnimation()

            val list = data.data.filter { it.getDay() == dayWeather.getDay() }.take(4).drop(1)
            weatherAdapter = WeatherMoreDataAdapter(list)
            rvMoreData.adapter = weatherAdapter
        }
    }

    private fun showLoading() {
        with(binding) {
            layoutError.root.gone()
            layoutPermissionDenied.root.gone()
            progressBar.show()
            sucessGroup.gone()
        }
    }

    private fun showPermissionNotGrantedView() {
        with(binding) {
            layoutPermissionDenied.root.show()
            layoutError.root.gone()
            progressBar.gone()
            sucessGroup.gone()
        }
    }

    private fun showWeatherError() {
        with(binding) {
            layoutError.root.show()
            layoutPermissionDenied.root.gone()
            progressBar.gone()
            sucessGroup.gone()
        }
    }

    private fun askForLocationPermission() {
        requestPermissionResultLauncher.launch(
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
        )
    }

    private fun checkHasLocationPermission(): Boolean {
        return requireContext().checkIfPermissionsAreGranted(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )
    }

    private fun showNextDays() {
        val weatherData = viewModel.homeState.value
        if (weatherData is HomeViewModel.HomeState.ShowWeather) {
            val first= weatherData.data.data.first()
            val comingDays = weatherData.data.data.filter { first.getDay() != it.getDay() }
            UpcomingDaysView(context = requireContext(),upComingDays = UpcomingDays.fromWeatherDataList(comingDays)).show()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}