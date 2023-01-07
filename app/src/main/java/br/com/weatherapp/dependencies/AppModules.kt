package br.com.weatherapp.dependencies

import br.com.weatherapp.base.resolveRetrofit
import br.com.weatherapp.data.api.WeatherApi
import br.com.weatherapp.data.repository.WeatherRepositorImpl
import br.com.weatherapp.data.repository.WeatherRepository
import br.com.weatherapp.features.main.search.SearchViewModel
import br.com.weatherapp.features.main.home.HomeViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

object AppModules {

    val moduleDependencies = module {
        viewModel { HomeViewModel(get()) }
        viewModel { SearchViewModel(get()) }

        single<WeatherRepository> { WeatherRepositorImpl(get())}
        single<WeatherApi> { resolveRetrofit(get()) }
    }
}