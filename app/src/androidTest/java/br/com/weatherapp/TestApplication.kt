package br.com.weatherapp

import android.app.Application
import android.content.Context
import android.os.Build
import androidx.test.runner.AndroidJUnitRunner
import com.github.tmurakami.dexopener.DexOpener

class TestApplication : Application() {
    override fun onCreate() {
        super.onCreate()
    }
}

class TestApplicationJUnitRunner : AndroidJUnitRunner() {
    override fun newApplication(cl: ClassLoader, className: String, context: Context): Application {
        // We only need to enable DexOpener for < 28
        // MockK supports for mocking final classes on Android 9+.
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.P) {
            DexOpener.install(this)
        }
        return super.newApplication(cl, TestApplication::class.java.name, context)
    }
}