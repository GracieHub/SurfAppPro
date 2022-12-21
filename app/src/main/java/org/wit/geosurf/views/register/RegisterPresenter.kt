package org.wit.geosurf.views.register

import android.content.Intent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import com.google.firebase.auth.FirebaseAuth
import org.wit.geosurf.main.MainApp
import org.wit.geosurf.views.geosurflist.GeosurfListView

class RegisterPresenter (val view: RegisterView) {

    var app: MainApp
    private lateinit var geosurfIntentLauncher : ActivityResultLauncher<Intent>

    init {
        app = view.application as MainApp
        registerGeosurfCallback()
    }
    var auth: FirebaseAuth = FirebaseAuth.getInstance()

    fun doRegisterUser(email: String, password: String) {
        view.showProgress()
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(view!!) { task ->
            if (task.isSuccessful) {
                val launcherIntent = Intent(view, GeosurfListView::class.java)
                geosurfIntentLauncher.launch(launcherIntent)
            } else {
                view.showSnackBar("Login failed: ${task.exception?.message}")
            }
            view.hideProgress()
        }
    }

    private fun registerGeosurfCallback() {
        geosurfIntentLauncher =
            view.registerForActivityResult(ActivityResultContracts.StartActivityForResult())
            {  }
    }
}