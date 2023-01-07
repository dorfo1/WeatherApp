package br.com.weatherapp.features.splash

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import br.com.weatherapp.R
import br.com.weatherapp.base.doOnEndAnimation
import br.com.weatherapp.features.main.MainActivity
import com.airbnb.lottie.LottieAnimationView

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        val lottie = findViewById<LottieAnimationView>(R.id.lottie_splash)

        lottie.doOnEndAnimation {
            startActivity(
                Intent(this@SplashActivity,MainActivity::class.java)
            )
            finish()
        }
    }
}
