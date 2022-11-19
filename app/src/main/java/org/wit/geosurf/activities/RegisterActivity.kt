package org.wit.geosurf.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import com.google.android.material.snackbar.Snackbar
import org.wit.geosurf.R
import org.wit.geosurf.activities.GeosurfListActivity
import org.wit.geosurf.databinding.ActivityLoginBinding
import org.wit.geosurf.databinding.ActivityRegisterBinding
import org.wit.geosurf.main.MainApp
import org.wit.geosurf.models.UserModel

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private lateinit var geosurfIntentLauncher : ActivityResultLauncher<Intent>
    lateinit var app : MainApp

    var user = UserModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        registerGeosurfCallback()

        app = application as MainApp

        binding.registerButton.setOnClickListener {
            user.username = binding.username.text.toString()
            if (user.username.length < 3) {
                user.username = ""
                binding.username.setError("Username must be 3 characters or more")
            }
            user.password = binding.password.text.toString()
            if (user.password.length < 3) {
                user.password = ""
                binding.password.setError("Password must be 3 characters or more")
            }
            if(!user.username.isNullOrBlank() && !user.password.isNullOrBlank()){
                app.users.createUser(user.copy())
                Snackbar
                    .make(it, "User Created", Snackbar.LENGTH_LONG)
                    .show()
                val launcherIntent = Intent(this, WelcomeActivity::class.java)
                geosurfIntentLauncher.launch(launcherIntent)
            } else {
                Snackbar
                    .make(it, "User not created - fill all fields", Snackbar.LENGTH_LONG)
                    .show()
            }
        }
    }

    private fun registerGeosurfCallback() {
        geosurfIntentLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult())
            {  }
    }
}