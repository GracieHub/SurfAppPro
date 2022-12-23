package org.wit.geosurf.models

import timber.log.Timber.i

var lastId = 0L

internal fun getId(): Long {
    return lastId++
}

class GeosurfMemStore : GeosurfStore {

    val geosurfs = ArrayList<GeosurfModel>()

    override suspend fun findAll(): List<GeosurfModel> {
        return geosurfs
    }

    override suspend fun create(geosurf: GeosurfModel) {
        geosurf.id = getId()
        geosurfs.add(geosurf)
        logAll()
    }

    override suspend fun update(geosurf: GeosurfModel) {
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
            foundGeosurf.county = geosurf.county

            logAll()
        }

    }

    private fun logAll() {
        geosurfs.forEach { i("$it") }
    }

    override suspend fun delete(geosurf: GeosurfModel) {
        geosurfs.remove(geosurf)
    }

    override suspend fun findById(id:Long) : GeosurfModel? {
        val foundGeosurf: GeosurfModel? = geosurfs.find { it.id == id }
        return foundGeosurf
    }

    override suspend fun findGeosurfById(geosurfId: Long): GeosurfModel? {
        var foundGeosurf: GeosurfModel? = geosurfs.find { t -> t.id == geosurfId }
        return foundGeosurf
    }

    override suspend fun clear(){
        geosurfs.clear()
    }
}

