package org.wit.geosurf.views.login
 
import android.content.Intent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import org.wit.geosurf.views.register.RegisterView
import org.wit.geosurf.main.MainApp
import org.wit.geosurf.views.geosurflist.GeosurfListView

class LoginPresenter(private val view: LoginView) {

    var app: MainApp
    private lateinit var geosurfIntentLauncher : ActivityResultLauncher<Intent>
    private lateinit var registerIntentLauncher : ActivityResultLauncher<Intent>

    init{
        app = view.application as MainApp
        registerGeosurfCallback()
        registerRegisterCallback()
    }

    fun doLogin(username: String, password: String): Boolean {
        return if (app.users.login(username, password)) {
            val launcherIntent = Intent(view, GeosurfListView::class.java)
            geosurfIntentLauncher.launch(launcherIntent)
            true
        } else {
            false
        }
    }

    fun doRegister() {
        val launcherIntent = Intent(view, RegisterView::class.java)
        registerIntentLauncher.launch(launcherIntent)
    }

    private fun registerGeosurfCallback() {
        geosurfIntentLauncher =
            view.registerForActivityResult(ActivityResultContracts.StartActivityForResult())
            {  }
    }
    private fun registerRegisterCallback() {
        registerIntentLauncher =
            view.registerForActivityResult(ActivityResultContracts.StartActivityForResult())
            {  }
    }

}