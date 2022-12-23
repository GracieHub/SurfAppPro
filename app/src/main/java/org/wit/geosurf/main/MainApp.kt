package org.wit.geosurf.main

import android.app.Application
import org.wit.geosurf.models.*
import timber.log.Timber
import timber.log.Timber.i

class MainApp : Application() {

    lateinit var geosurfs: GeosurfStore
 //   lateinit var users: UserStore


    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
   //     geosurfs = GeosurfJSONStore(applicationContext)
   //     users = UserJSONStore(applicationContext)
        geosurfs = GeosurfFireStore(applicationContext)

        i("Geosurf started")
    }
}