package org.wit.geosurf.views.splash

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import org.wit.geosurf.R

class SplashScreenView : AppCompatActivity() {

    lateinit var presenter: SplashScreenPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        presenter = SplashScreenPresenter(this)
        presenter.doSplashScreen()
    }
}