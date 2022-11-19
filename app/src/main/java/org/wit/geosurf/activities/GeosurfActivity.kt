package org.wit.geosurf.activities

import android.app.DatePickerDialog
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.Marker
import com.google.android.material.snackbar.Snackbar
import com.squareup.picasso.MemoryPolicy
import com.squareup.picasso.Picasso
import org.wit.geosurf.R
import org.wit.geosurf.databinding.ActivityGeosurfBinding
import org.wit.geosurf.helpers.showImagePicker
import org.wit.geosurf.main.MainApp
import org.wit.geosurf.models.Location
import org.wit.geosurf.models.GeosurfModel
import timber.log.Timber
import timber.log.Timber.i
import android.view.View
import android.widget.*
import java.util.*
import android.widget.Spinner as Spinner


class GeosurfActivity : AppCompatActivity() {

    private lateinit var binding: ActivityGeosurfBinding
    var geosurf = GeosurfModel()
    lateinit var app: MainApp
    private lateinit var imageIntentLauncher : ActivityResultLauncher<Intent>
    private lateinit var mapIntentLauncher : ActivityResultLauncher<Intent>
    //var location = Location(52.245696, -7.139102, 15f)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var edit = false
        binding = ActivityGeosurfBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.toolbarAdd.title = title
        setSupportActionBar(binding.toolbarAdd)
        app = application as MainApp
        i("Geosurf Activity started...")

        //array of ability levels declared
        val abilityLevels = resources.getStringArray(R.array.abilityLevels)

        // calendar declaration
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)

        // Add to surfspot button click listener
        binding.btnAdd.setOnClickListener() {
            geosurf.title = binding.geosurfTitle.text.toString()
            geosurf.description = binding.description.text.toString()
            geosurf.date = binding.geosurfDate.text.toString()
            geosurf.abilityLevel = binding.geosurfAbilityLevel.toString()
            geosurf.rating = binding.geosurfRating.rating


            if (geosurf.title.isEmpty()) {
                Snackbar.make(it,R.string.enter_geosurf_title, Snackbar.LENGTH_LONG)
                    .show()
            } else {
                if (edit) {
                    app.geosurfs.update(geosurf.copy())
                } else {
                    app.geosurfs.create(geosurf.copy())
                }
            }
            i("add Button Pressed: $geosurf")
            setResult(RESULT_OK)
            finish()
        }
        // edit surfspot
        if (intent.hasExtra("geosurf_edit")) {
            edit = true
            geosurf = intent.extras?.getParcelable("geosurf_edit")!!
            binding.geosurfTitle.setText(geosurf.title)
            binding.description.setText(geosurf.description)
            binding.geosurfDate.text = geosurf.date
            binding.datepicker.setText(R.string.update_date)
            binding.geosurfRating.setRating(geosurf.rating)



            binding.btnAdd.setText(R.string.save_geosurf)
            Picasso.get()
                .load(geosurf.image)
                .into(binding.geosurfImage)
            if (geosurf.image != Uri.EMPTY) {
                binding.chooseImage.setText(R.string.change_geosurf_image)
            }
        }

        // Datepicker Click Listener
        binding.datepicker.setOnClickListener {
            val dpd = DatePickerDialog(
                this, { _, mYear, mMonth, mDay ->
                    val mMonth = mMonth + 1
                    binding.geosurfDate.text = "$mDay/$mMonth/$mYear"
                },
                year,
                month,
                day
            )
            //show dialog
            dpd.show()
        }

        binding.chooseImage.setOnClickListener {
            showImagePicker(imageIntentLauncher)
        }

        //ability level spinner
        val geosurfAbilityLevel = findViewById<Spinner>(R.id.geosurfAbilityLevel)
        if (geosurfAbilityLevel != null) {
            val adapter = ArrayAdapter(
                this,
                android.R.layout.simple_spinner_dropdown_item, abilityLevels
            )
            geosurfAbilityLevel.adapter = adapter

            binding.geosurfAbilityLevel.onItemSelectedListener = object :
                AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>,
                    view: View, position: Int, id: Long
                ) {
                    Toast.makeText(
                        applicationContext,
                        getString(R.string.selected_abilityLevel) + " " +
                                "" + abilityLevels[position], Toast.LENGTH_SHORT
                    ).show()
                }

                override fun onNothingSelected(parent: AdapterView<*>) {
                    // write code to perform some action
                }
            }

        }

        binding.geosurfLocation.setOnClickListener {
            val location = Location(52.245696, -7.139102, 15f)
            if (geosurf.zoom != 0f) {
                location.lat =  geosurf.lat
                location.lng = geosurf.lng
                location.zoom = geosurf.zoom
            }
            val launcherIntent = Intent(this, MapActivity::class.java)
                .putExtra("location", location)
            mapIntentLauncher.launch(launcherIntent)
        }
        registerImagePickerCallback()
        registerMapCallback()
    }
    //Oncreate function end



    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_geosurf, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.item_delete -> {
                app.geosurfs.delete(geosurf)
                finish()
            }
            R.id.item_cancel -> {
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun registerImagePickerCallback() {
        imageIntentLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult())
            { result ->
                when(result.resultCode){
                    RESULT_OK -> {
                        if (result.data != null) {
                            i("Got Result ${result.data!!.data}")
                            geosurf.image = result.data!!.data!!
                            Picasso.get()
                                .load(geosurf.image)
                                .into(binding.geosurfImage)
                            binding.chooseImage.setText(R.string.change_geosurf_image)
                        } // end of if
                    }
                    RESULT_CANCELED -> { } else -> { }
                }
            }
    }

    private fun registerMapCallback() {
        mapIntentLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult())
            { result ->
                when (result.resultCode) {
                    RESULT_OK -> {
                        if (result.data != null) {
                            i("Got Location ${result.data.toString()}")
                            val location =
                                result.data!!.extras?.getParcelable<Location>("location")!!
                            i("Location == $location")
                            geosurf.lat = location.lat
                            geosurf.lng = location.lng
                            geosurf.zoom = location.zoom
                        } // end of if
                    }
                    RESULT_CANCELED -> {}
                    else -> {}
                }
            }

    }

    fun clickDataPicker(view: View) {
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)

        val dpd = DatePickerDialog(this, DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
            // Display Selected date in Toast
            Toast.makeText(this, """$dayOfMonth - ${monthOfYear + 1} - $year""", Toast.LENGTH_LONG).show()

        }, year, month, day)
        dpd.show()
    }
}