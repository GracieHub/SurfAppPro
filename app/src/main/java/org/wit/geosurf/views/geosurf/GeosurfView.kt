package org.wit.geosurf.views.geosurf

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.google.android.material.snackbar.Snackbar
import com.squareup.picasso.Picasso
import org.wit.geosurf.R
import org.wit.geosurf.databinding.ActivityGeosurfBinding
import org.wit.geosurf.models.GeosurfModel
import timber.log.Timber.i
import android.view.View
import android.widget.*
import java.util.*
import android.widget.Spinner as Spinner


class GeosurfView : AppCompatActivity() {
    private lateinit var binding: ActivityGeosurfBinding
    private lateinit var presenter: GeosurfPresenter
    var geosurf = GeosurfModel()
    val c = Calendar.getInstance()
    val year = c.get(Calendar.YEAR)
    val month = c.get(Calendar.MONTH)
    val day = c.get(Calendar.DAY_OF_MONTH)


    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGeosurfBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.toolbarAdd.title = title
        setSupportActionBar(binding.toolbarAdd)


        //array of ability levels declared
        val abilityLevels = resources.getStringArray(R.array.abilityLevels)

        presenter = GeosurfPresenter(this)

        // Add to surfspot button click listener
        binding.btnAdd.setOnClickListener() {
            geosurf.title = binding.geosurfTitle.text.toString()
            geosurf.description = binding.description.text.toString()
            geosurf.date = binding.geosurfDate.text.toString()
            geosurf.abilityLevel = binding.geosurfAbilityLevel.toString()
            geosurf.rating = binding.geosurfRating.rating.toFloat()

            if (geosurf.title.isEmpty()) {
                Snackbar.make(it, R.string.enter_geosurf_title, Snackbar.LENGTH_LONG)
                    .show()
            } else {
                presenter.doAddOrSave(
                    geosurf.title,
                    geosurf.description,
                    geosurf.date,
                    geosurf.abilityLevel,
                    geosurf.rating,
                )
            }
            i("add Button Pressed: $geosurf")
            setResult(RESULT_OK)
            finish()
        }

        // edit surfspot
        if (intent.hasExtra("geosurf_edit")) {
            //        edit = true
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

        binding.geosurfLocation.setOnClickListener {
            presenter.doSetLocation()
        }

        // Datepicker Click Listener
        binding.datepicker.setOnClickListener {
            val dpd = DatePickerDialog(
                this,
                { _, Year, Month, Day ->
                    val Month = Month + 1
                    binding.geosurfDate.setText("$Day/$Month/$Year")
                }, year, month, day
            )
            //show dialog
            dpd.show()
        }
        // displays today's date
        val toast = "Today's Date Is : $day/$month/$year"
        Toast.makeText(this, toast, Toast.LENGTH_SHORT).show()
        binding.chooseImage.setOnClickListener {
            presenter.doSelectImage()
        }

        binding.geosurfLocation.setOnClickListener {
            presenter.doSetLocation()
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
                        view: View?,
                        position: Int,
                        id: Long
                    ) {
                        Toast.makeText(
                            applicationContext,
                            getString(R.string.selected_abilityLevel) + " " +
                                    "" + abilityLevels[position], Toast.LENGTH_SHORT
                        ).show()
                    }

                    override fun onNothingSelected(parent: AdapterView<*>?) {
                        TODO("Not yet implemented")
                    }
                }
            }
    }
    //Oncreate function end

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_geosurf, menu)
        return super.onCreateOptionsMenu(menu)
    }

        override fun onOptionsItemSelected(item: MenuItem): Boolean {
            when (item.itemId) {
                R.id.item_cancel -> {
                    presenter.doCancel()
                }
                R.id.item_delete -> {
                    presenter.doDelete()
                }
            }
            return super.onOptionsItemSelected(item)
        }

    fun showGeosurf(geosurf: GeosurfModel) {
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

    fun updateImage(image: Uri){
        i("Image updated")
        Picasso.get()
            .load(image)
            .into(binding.geosurfImage)
        binding.chooseImage.setText(R.string.change_geosurf_image)
    }
}
