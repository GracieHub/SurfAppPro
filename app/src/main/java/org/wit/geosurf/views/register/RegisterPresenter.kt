package org.wit.geosurf.views.register

import android.content.Intent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import com.google.firebase.auth.FirebaseAuth
import org.wit.geosurf.main.MainApp
import org.wit.geosurf.views.geosurflist.GeosurfListView
import org.wit.geosurf.models.GeosurfFireStore


class RegisterPresenter (val view: RegisterView) {

    var app: MainApp = view.application as MainApp
    var fireStore: GeosurfFireStore? = null
    var auth: FirebaseAuth = FirebaseAuth.getInstance()
    private lateinit var geosurfIntentLauncher : ActivityResultLauncher<Intent>

    init {
        registerGeosurfCallback()
        if (app.geosurfs is GeosurfFireStore) {
            fireStore = app.geosurfs as GeosurfFireStore
        }
    }

    fun doRegisterUser(email: String, password: String) {
        view.showProgress()
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(view!!) { task ->
            if (task.isSuccessful) {
                fireStore!!.fetchGeosurfs {
                    view.hideProgress()
                    val launcherIntent = Intent(view, GeosurfListView::class.java)
                    geosurfIntentLauncher.launch(launcherIntent)
                }
            } else {
                view.hideProgress()
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