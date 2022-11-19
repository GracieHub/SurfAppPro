package org.wit.geosurf.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import com.google.android.material.snackbar.Snackbar
import org.wit.geosurf.activities.RegisterActivity
import org.wit.geosurf.R
import org.wit.geosurf.databinding.ActivityLoginBinding
import org.wit.geosurf.main.MainApp
import timber.log.Timber.i
import org.wit.geosurf.models.UserModel


class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var geosurfIntentLauncher : ActivityResultLauncher<Intent>
    private lateinit var registerIntentLauncher : ActivityResultLauncher<Intent>

    lateinit var app : MainApp

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        registerGeosurfCallback()
        registerRegisterCallback()
        app = application as MainApp

        binding.login.setOnClickListener {
            var username = binding.username.text.toString()
            var password = binding.password.text.toString()
            if(app.users.login(username, password)) {
                val launcherIntent = Intent(this, WelcomeActivity::class.java)
                geosurfIntentLauncher.launch(launcherIntent)
            }else{
                Snackbar
                    .make(it, R.string.warning_incorrect_credentials, Snackbar.LENGTH_LONG)
                    .show()
            }
        }

        binding.registerMessage.setOnClickListener {
            val launcherIntent = Intent(this, RegisterActivity::class.java)
            registerIntentLauncher.launch(launcherIntent)
        }
    }

    private fun registerGeosurfCallback() {
        geosurfIntentLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult())
            {  }
    }
    private fun registerRegisterCallback() {
        registerIntentLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult())
            {  }
    }
}