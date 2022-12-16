package org.wit.geosurf.views.geosurflist

import android.content.Intent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import org.wit.geosurf.main.MainApp
import org.wit.geosurf.models.GeosurfModel
import org.wit.geosurf.views.geosurf.GeosurfView
import org.wit.geosurf.views.map.GeosurfMapView


class GeosurfListPresenter(val view: GeosurfListView) {

    var app: MainApp
    private lateinit var refreshIntentLauncher: ActivityResultLauncher<Intent>
    private lateinit var mapIntentLauncher: ActivityResultLauncher<Intent>

    init {
        app = view.application as MainApp
        registerMapCallback()
        registerRefreshCallback()
    }

    fun getGeosurfs() = app.geosurfs.findAll()

    fun doAddGeosurf() {
        val launcherIntent = Intent(view,GeosurfView::class.java)
        refreshIntentLauncher.launch(launcherIntent)
    }

    fun doEditGeosurf(geosurf: GeosurfModel) {
        val launcherIntent = Intent(view, GeosurfView::class.java)
        launcherIntent.putExtra("geosurf_edit", geosurf)
        mapIntentLauncher.launch(launcherIntent)
    }

    fun doShowGeosurfsMap() {
        val launcherIntent = Intent(view, GeosurfMapView::class.java)
        refreshIntentLauncher.launch(launcherIntent)
    }

    private fun registerRefreshCallback() {
        refreshIntentLauncher =
            view.registerForActivityResult(ActivityResultContracts.StartActivityForResult())
            { getGeosurfs() }
    }

    private fun registerMapCallback() {
        mapIntentLauncher =
            view.registerForActivityResult(ActivityResultContracts.StartActivityForResult())
            { }
    }
}