package org.wit.geosurf.views.geosurflist

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.SearchView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.wit.geosurf.R
import org.wit.geosurf.databinding.ActivityGeosurfListBinding
import org.wit.geosurf.main.MainApp
import org.wit.geosurf.models.GeosurfModel
import timber.log.Timber.i

class GeosurfListView : AppCompatActivity(), GeosurfListener {

    lateinit var app: MainApp
    private lateinit var binding: ActivityGeosurfListBinding
    lateinit var bottomNav : BottomNavigationView
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
        GlobalScope.launch(Dispatchers.Main) {
            loadGeosurfs()
        }

        bottomNav = findViewById(R.id.bottomNav) as BottomNavigationView
        bottomNav.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                android.R.id.home -> {
                    true
                }
                R.id.item_add -> {
                    presenter.doAddGeosurf()
                    i("we got here")
                    true
                }
                R.id.item_map -> {
                    presenter.doShowGeosurfsMap()
                    true
                }
                R.id.item_logout -> {
                    GlobalScope.launch(Dispatchers.IO) {
                        presenter.doLogout()
                    }
                    true
                }
                else -> false
            }
        }

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        // get menu item
        val searchItem: MenuItem = menu.findItem(R.id.search)
        // getting search view of menu item and set query hint
        val searchView: SearchView = searchItem.getActionView() as SearchView
        searchView.setQueryHint(getString(R.string.search_hint))

        // below line is to call set on query text listener method.
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                GlobalScope.launch(Dispatchers.Main) {
                    filter(query)
                }
                //i(msg)
                return false
            }

            override fun onQueryTextChange(msg: String): Boolean {
                GlobalScope.launch(Dispatchers.Main) {
                    filter(msg)
                }
                //i(msg)
                return false
            }
        })
        return true
    }

    override fun onGeosurfClick(geosurf: GeosurfModel) {
        presenter.doEditGeosurf(geosurf)

    }

    @SuppressLint("NotifyDataSetChanged")
    private fun loadGeosurfs() {
        GlobalScope.launch(Dispatchers.Main) {
            showGeosurfs(presenter.getGeosurfs())
        }
    }
    fun showGeosurfs (geosurfs: List<GeosurfModel>) {
        binding.recyclerView.adapter = GeosurfAdapter(geosurfs, this)
        binding.recyclerView.adapter?.notifyDataSetChanged()
    }
    @SuppressLint("NotifyDataSetChanged")
    override fun onResume() {
        GlobalScope.launch(Dispatchers.Main) {
            loadGeosurfs()
        }
        binding.recyclerView.adapter?.notifyDataSetChanged()
        i("recyclerView onResume")
        bottomNav.setSelectedItemId(android.R.id.home)
        super.onResume()
    }

    @SuppressLint("NotifyDataSetChanged")
    private suspend fun filter(text: String) {
        val filteredlist: MutableList<GeosurfModel> = mutableListOf()

        for (item in app.geosurfs.findAll()) {
            if (item.county.toLowerCase().contains(text.toLowerCase())) {
                filteredlist.add(item)
                // i("item added to list")
            }
        }
        if (filteredlist.isEmpty()) {
            Toast.makeText(this, "No Data Found..", Toast.LENGTH_SHORT).show()
            loadGeosurfs()
        } else {
            binding.recyclerView.adapter = GeosurfAdapter(filteredlist, this)
            binding.recyclerView.adapter?.notifyDataSetChanged()
        }
    }
}
