package org.wit.geosurf.views.login

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import org.wit.geosurf.R
import org.wit.geosurf.databinding.ActivityLoginBinding
import org.wit.geosurf.main.MainApp

class LoginView : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    lateinit var app : MainApp
    lateinit var presenter: LoginPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        app = application as MainApp
        presenter = LoginPresenter(this)

        binding.login.setOnClickListener {
            var username = binding.username.text.toString()
            if (username.length < 3) {
                username = ""
                binding.username.setError("Username must be 3 characters or more")
            }
            var password = binding.password.text.toString()
            if (password.length < 3) {
                password = ""
                binding.password.setError("Password must be 3 characters or more")
            }
            if(presenter.doLogin(username, password)) {
                Snackbar
                    .make(it, R.string.login_success, Snackbar.LENGTH_LONG)
                    .show()
            } else {
                Snackbar
                    .make(it, R.string.warning_incorrect_credentials, Snackbar.LENGTH_LONG)
                    .show()
            }
        }

        binding.registerMessage.setOnClickListener {
            presenter.doRegister()
        }
    }

}