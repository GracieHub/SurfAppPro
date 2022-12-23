package org.wit.geosurf.views.geosurflist

import android.view.LayoutInflater
import android.annotation.SuppressLint
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import org.wit.geosurf.databinding.CardGeosurfBinding
import org.wit.geosurf.models.GeosurfModel

interface GeosurfListener {
    fun onGeosurfClick(geosurf: GeosurfModel)
}

class GeosurfAdapter constructor
    (private var geosurfs: List<GeosurfModel>,
     private val listener: GeosurfListener) :
    RecyclerView.Adapter<GeosurfAdapter.MainHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainHolder {
        val binding = CardGeosurfBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)

        return MainHolder(binding)
    }

    override fun onBindViewHolder(holder: MainHolder, position: Int) {
        val geosurf = geosurfs[holder.adapterPosition]
        holder.bind(geosurf, listener)
    }

    override fun getItemCount(): Int = geosurfs.size

    class MainHolder(private val binding : CardGeosurfBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(geosurf: GeosurfModel, listener: GeosurfListener) {
            binding.geosurfTitle.text = geosurf.title
            // binding.geosurfAbilityLevel.text = geosurf.abilityLevel
            binding.geosurfDescription.text = ("Description: " + geosurf.description)
            binding.geosurfCounty.text =("County: " + geosurf.county)
            binding.geosurfDate.text = ("Date added: " +geosurf.date)
            if (geosurf.image != "") {
                Picasso.get().load(geosurf.image).resize(250, 250).into(binding.imageIcon)
            }
            binding.root.setOnClickListener { listener.onGeosurfClick(geosurf) }

        }
    }
}
