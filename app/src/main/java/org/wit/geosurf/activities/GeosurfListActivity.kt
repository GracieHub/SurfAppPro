package org.wit.geosurf.activities

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import org.wit.geosurf.R
import org.wit.geosurf.adapters.GeosurfAdapter
import org.wit.geosurf.adapters.GeosurfListener
import org.wit.geosurf.databinding.ActivityGeosurfListBinding
import org.wit.geosurf.main.MainApp
import org.wit.geosurf.models.GeosurfModel


class GeosurfListActivity : AppCompatActivity(), GeosurfListener {

    lateinit var app: MainApp
    private lateinit var binding: ActivityGeosurfListBinding
    private lateinit var refreshIntentLauncher : ActivityResultLauncher<Intent>
    private lateinit var mapIntentLauncher : ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGeosurfListBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.toolbarAdd.title = title
        setSupportActionBar(binding.toolbarAdd)

        app = application as MainApp

        val layoutManager = LinearLayoutManager(this)
        binding.recyclerView.layoutManager = layoutManager
        loadGeosurfs()

        registerRefreshCallback()
        registerMapCallback()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.item_add -> {
                val launcherIntent = Intent(this, GeosurfActivity::class.java)
                refreshIntentLauncher.launch(launcherIntent)
            }
                R.id.item_map -> {
                    val launcherIntent = Intent(this, GeosurfMapsActivity::class.java)
                    mapIntentLauncher.launch(launcherIntent)
                }
            }
            return super.onOptionsItemSelected(item)
        }

    override fun onGeosurfClick(geosurf: GeosurfModel) {
        val launcherIntent = Intent(this, GeosurfActivity::class.java)
        launcherIntent.putExtra("geosurf_edit", geosurf)
        refreshIntentLauncher.launch(launcherIntent)
    }

    private fun registerRefreshCallback() {
        refreshIntentLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult())
            { loadGeosurfs() }
    }

    private fun registerMapCallback() {
        mapIntentLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult())
            { }
    }


    private fun loadGeosurfs() {
        showGeosurfs(app.geosurfs.findAll())
    }

    fun showGeosurfs (geosurfs: List<GeosurfModel>) {
        binding.recyclerView.adapter = GeosurfAdapter(geosurfs, this)
        binding.recyclerView.adapter?.notifyDataSetChanged()
    }
}