package org.wit.geosurf.activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import org.wit.geosurf.R
import org.wit.geosurf.databinding.ActivityWelcomeBinding
import org.wit.geosurf.main.MainApp
import org.wit.geosurf.views.geosurflist.GeosurfListView

class WelcomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityWelcomeBinding
    lateinit var app: MainApp

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWelcomeBinding.inflate(layoutInflater)

        setContentView(R.layout.activity_welcome)
        app = application as MainApp
        binding.toolbar.title = title
        setSupportActionBar(binding.toolbar)
        binding = ActivityWelcomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // To do:   get user email to show on welcome screen
        //val emailId = intent.getStringExtra("email_id")
        //binding.emailId.text = "$emailId"

        binding.app.setOnClickListener {
            startActivity(Intent(this, GeosurfListView::class.java))
        }

        binding.btnLogout.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }
}