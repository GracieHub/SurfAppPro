package org.wit.geosurf.views.login
 
import android.content.Intent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import org.wit.geosurf.views.register.RegisterView
import org.wit.geosurf.main.MainApp
import org.wit.geosurf.views.geosurflist.GeosurfListView
import com.google.firebase.auth.FirebaseAuth
import org.wit.geosurf.models.GeosurfFireStore



class LoginPresenter(private val view: LoginView) {

    var app: MainApp = view.application as MainApp
    var auth: FirebaseAuth = FirebaseAuth.getInstance()
    var fireStore: GeosurfFireStore? = null
    private lateinit var loginIntentLauncher : ActivityResultLauncher<Intent>
    private lateinit var registerIntentLauncher : ActivityResultLauncher<Intent>

    init{
        app = view.application as MainApp
        registerGeosurfCallback()
        registerRegisterCallback()
        if (app.geosurfs is GeosurfFireStore) {
            fireStore = app.geosurfs as GeosurfFireStore
        }
    }

    fun doLogin(username: String, password: String) {
        view.showProgress()
        auth.signInWithEmailAndPassword(username, password).addOnCompleteListener(view!!) { task ->
            if (task.isSuccessful) {
                    if (fireStore != null) {
                        fireStore!!.fetchGeosurfs {
                            view.hideProgress()
                            val launcherIntent = Intent(view, GeosurfListView::class.java)
                            loginIntentLauncher.launch(launcherIntent)
                        }
                    } else {
                        view.hideProgress()
                        val launcherIntent = Intent(view, GeosurfListView::class.java)
                        loginIntentLauncher.launch(launcherIntent)
                    }
            } else {
                view.hideProgress()
                view.showSnackBar("Login failed: ${task.exception?.message}")
            }
            view.hideProgress()
        }
    }

    fun doSignUp(email: String, password: String) {
        val launcherIntent = Intent(view, GeosurfListView::class.java)
        loginIntentLauncher.launch(launcherIntent)
    }

    fun doRegister() {
        val launcherIntent = Intent(view, RegisterView::class.java)
        registerIntentLauncher.launch(launcherIntent)
    }

    private fun registerGeosurfCallback() {
        loginIntentLauncher =
            view.registerForActivityResult(ActivityResultContracts.StartActivityForResult())
            {  }
    }
    private fun registerRegisterCallback() {
        registerIntentLauncher =
            view.registerForActivityResult(ActivityResultContracts.StartActivityForResult())
            {  }
    }

}