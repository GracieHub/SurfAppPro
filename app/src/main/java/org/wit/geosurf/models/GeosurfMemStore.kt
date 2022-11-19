package org.wit.geosurf.models

import timber.log.Timber.i

var lastId = 0L

internal fun getId(): Long {
    return lastId++
}

class GeosurfMemStore : GeosurfStore {

    val geosurfs = ArrayList<GeosurfModel>()

    override fun findAll(): List<GeosurfModel> {
        return geosurfs
    }

    override fun create(geosurf: GeosurfModel) {
        geosurf.id = getId()
        geosurfs.add(geosurf)
        logAll()
    }

    override fun update(geosurf: GeosurfModel) {
        var foundGeosurf: GeosurfModel? = geosurfs.find { p -> p.id == geosurf.id }
        if (foundGeosurf != null) {
            foundGeosurf.title = geosurf.title
            foundGeosurf.description = geosurf.description
            foundGeosurf.image = geosurf.image
            foundGeosurf.lat = geosurf.lat
            foundGeosurf.lng = geosurf.lng
            foundGeosurf.zoom = geosurf.zoom
            foundGeosurf.date = geosurf.date
            foundGeosurf.abilityLevel = geosurf.abilityLevel
            foundGeosurf.rating =geosurf.rating

            logAll()
        }
    }

    private fun logAll() {
        geosurfs.forEach { i("$it") }
    }

    override fun delete(geosurf: GeosurfModel) {
        geosurfs.remove(geosurf)
    }
}

