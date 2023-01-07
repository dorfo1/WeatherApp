package br.com.weatherapp.features.splash

import org.junit.Test

class SplashActivityTest {

    @Test
    fun whenSplashOpens_checkAppNameText() {
        withTest { } launch {} check {
            checkSplashText()
        }
    }
}