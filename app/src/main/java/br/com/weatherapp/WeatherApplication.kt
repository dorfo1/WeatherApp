package br.com.weatherapp

import android.app.Application
import br.com.weatherapp.dependencies.AppModules
import br.com.weatherapp.dependencies.NetworkModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class WeatherApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@WeatherApplication)
            modules(AppModules.moduleDependencies, NetworkModule.moduleDependencies)
        }
    }
}