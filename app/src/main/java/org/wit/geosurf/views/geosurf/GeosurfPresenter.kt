package org.wit.geosurf.views.geosurf

import android.annotation.SuppressLint
import android.content.Intent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.squareup.picasso.Picasso
import org.wit.geosurf.databinding.ActivityGeosurfBinding
import org.wit.geosurf.helpers.showImagePicker
import org.wit.geosurf.main.MainApp
import org.wit.geosurf.models.Location
import org.wit.geosurf.models.GeosurfModel
import org.wit.geosurf.views.editlocation.EditLocationView
import timber.log.Timber
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import org.wit.geosurf.views.editlocation.checkLocationPermissions


class GeosurfPresenter ( val view: GeosurfView) {

    var geosurf = GeosurfModel()
    lateinit var app: MainApp
    lateinit var binding: ActivityGeosurfBinding
    var locationService: FusedLocationProviderClient =
        LocationServices.getFusedLocationProviderClient(view)
    private lateinit var requestPermissionLauncher: ActivityResultLauncher<String>
    private lateinit var imageIntentLauncher: ActivityResultLauncher<Intent>
    private lateinit var mapIntentLauncher: ActivityResultLauncher<Intent>
    val location = Location(52.245696, -7.131902, 15f)
    var edit = false

    init {

        doPermissionLauncher()
        registerImagePickerCallback()
        registerMapCallback()

        binding = ActivityGeosurfBinding.inflate(view.layoutInflater)
        app = view.application as MainApp
        if (view.intent.hasExtra("geosurf_edit")) {
            edit = true
            geosurf = view.intent.extras?.getParcelable("geosurf_edit")!!
            view.showGeosurf(geosurf)
        } else {
            if (checkLocationPermissions(view)) {
                doSetCurrentLocation()
            }
            geosurf.lat = location.lat
            geosurf.lng = location.lng
        }
        doPermissionLauncher()
        registerImagePickerCallback()
        registerMapCallback()
    }

    fun doAddOrSave(
        title: String,
        description: String,
        date: String,
        abilityLevel: String,
        rating: Float
    ) {
        geosurf.title = title
        geosurf.description = description
        geosurf.rating = rating
        geosurf.date = date
        geosurf.abilityLevel = abilityLevel
        if (edit) {
            app.geosurfs.update(geosurf.copy())
        } else {
            app.geosurfs.create(geosurf.copy())
        }
        view.finish()
    }

    fun doSelectImage() {
        showImagePicker(imageIntentLauncher)
    }

    fun doSetLocation() {
        if (geosurf.zoom != 0f) {
            location.lat = geosurf.lat
            location.lng = geosurf.lng
            location.zoom = geosurf.zoom
        }
        val launcherIntent = Intent(view, EditLocationView::class.java)
            .putExtra("location", location)
        mapIntentLauncher.launch(launcherIntent)
    }

    fun cacheGeosurf(
        title: String,
        description: String,
        rating: Float,
        date: String,
        abilityLevel: String
    ) {
        geosurf.title = title
        geosurf.description = description
        geosurf.rating = rating
        geosurf.date = date
        geosurf.abilityLevel = abilityLevel
    }

    fun doDelete() {
        app.geosurfs.delete(geosurf)
        view.finish()
    }

    fun doCancel() {
        view.finish()
    }

    fun doHome() {
        view.finish()
    }


    fun locationUpdate(lat: Double, lng: Double) {
        geosurf.lat = lat
        geosurf.lng = lng
        geosurf.zoom = 15f
        // view.showTravelmark(travelmark)
    }

    @SuppressLint("MissingPermission")
    fun doSetCurrentLocation() {
        Timber.i("setting location from doSetLocation")
        locationService.lastLocation.addOnSuccessListener {
            locationUpdate(it.latitude, it.longitude)
        }
    }

    private fun registerImagePickerCallback() {
        imageIntentLauncher =
            view.registerForActivityResult(ActivityResultContracts.StartActivityForResult())
            { result ->
                when (result.resultCode) {
                    AppCompatActivity.RESULT_OK -> {
                        if (result.data != null) {
                            Timber.i("Got Result ${result.data!!.data}")
                            geosurf.image = result.data!!.data!!
                            view.updateImage(geosurf.image)
                            Picasso.get()
                                .load(geosurf.image)
                                .into(binding.geosurfImage)
                        } // end of if
                    }
                    AppCompatActivity.RESULT_CANCELED -> {}
                    else -> {}
                }
            }
    }

    private fun registerMapCallback() {
        mapIntentLauncher =
            view.registerForActivityResult(ActivityResultContracts.StartActivityForResult())
            { result ->
                when (result.resultCode) {
                    AppCompatActivity.RESULT_OK -> {
                        if (result.data != null) {
                            Timber.i("Got Location ${result.data.toString()}")
                            val location =
                                result.data!!.extras?.getParcelable<Location>("location")!!
                            Timber.i("Location == $location")
                            geosurf.lat = location.lat
                            geosurf.lng = location.lng
                            geosurf.zoom = location.zoom
                        } // end of if
                    }
                    AppCompatActivity.RESULT_CANCELED -> {}
                    else -> {}
                }
            }
    }

    private fun doPermissionLauncher() {
        Timber.i("permission check called")
        requestPermissionLauncher =
            view.registerForActivityResult(ActivityResultContracts.RequestPermission())
            { isGranted: Boolean ->
                if (isGranted) {
                    doSetCurrentLocation()
                } else {
                    locationUpdate(location.lat, location.lng)
                }
            }
    }
}
