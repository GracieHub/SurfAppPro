package org.wit.geosurf.views.geosurflist

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import org.wit.geosurf.R
import org.wit.geosurf.databinding.ActivityGeosurfListBinding
import org.wit.geosurf.main.MainApp
import org.wit.geosurf.models.GeosurfModel

class GeosurfListView : AppCompatActivity(), GeosurfListener {

    lateinit var app: MainApp
    private lateinit var binding: ActivityGeosurfListBinding
    lateinit var presenter: GeosurfListPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGeosurfListBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.toolbarAdd.title = title
        setSupportActionBar(binding.toolbarAdd)
        presenter = GeosurfListPresenter(this)
        app = application as MainApp

        val layoutManager = LinearLayoutManager(this)
        binding.recyclerView.layoutManager = layoutManager
        loadGeosurfs()

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.item_add -> { presenter.doAddGeosurf() }
            R.id.item_map -> { presenter.doShowGeosurfsMap() }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onGeosurfClick(geosurf: GeosurfModel) {
        presenter.doEditGeosurf(geosurf)

    }

    @SuppressLint("NotifyDataSetChanged")
    private fun loadGeosurfs() {
        binding.recyclerView.adapter = GeosurfAdapter(presenter.getGeosurfs(),this)
        binding.recyclerView.adapter?.notifyDataSetChanged()
    }
}