package org.wit.geosurf.views.register

import android.content.Intent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import org.wit.geosurf.main.MainApp
import org.wit.geosurf.models.UserModel
import org.wit.geosurf.views.geosurflist.GeosurfListView

class RegisterPresenter (val view: RegisterView) {

    var app: MainApp
    private lateinit var geosurfIntentLauncher : ActivityResultLauncher<Intent>

    init {
        app = view.application as MainApp
        registerGeosurfCallback()
    }

    fun doRegisterUser(user: UserModel): Boolean {
        return if (!user.username.isNullOrBlank() && !user.password.isNullOrBlank()) {
            app.users.createUser(user)
            val launcherIntent = Intent(view, GeosurfListView::class.java)
            geosurfIntentLauncher.launch(launcherIntent)
            true
        } else {
            false
        }
    }

    private fun registerGeosurfCallback() {
        geosurfIntentLauncher =
            view.registerForActivityResult(ActivityResultContracts.StartActivityForResult())
            {  }
    }
}