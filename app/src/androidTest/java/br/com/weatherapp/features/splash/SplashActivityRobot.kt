package br.com.weatherapp.features.splash

import androidx.test.core.app.ActivityScenario
import br.com.weatherapp.extensions.isDisplayed


fun SplashActivityTest.withTest(func: SplashActivityRobot.() -> Unit) =
    SplashActivityRobot().apply(func)

class SplashActivityRobot {

    infix fun launch(func: SplashActivityRobot.() -> Unit): SplashActivityRobot {
        ActivityScenario.launch(SplashActivity::class.java)
        return this.apply(func)
    }


    infix fun check(func: SplashActivityRobot.Result.() -> Unit) = Result().apply(func)

    inner class Result {
        fun checkSplashText() = "WeatherApp".isDisplayed()
    }
}