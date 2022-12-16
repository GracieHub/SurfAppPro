package org.wit.geosurf.models

interface GeosurfStore {
    fun findAll(): List<GeosurfModel>
    fun create(geosurf: GeosurfModel)
    fun update(geosurf: GeosurfModel)
    fun delete(geosurf: GeosurfModel)
    fun findById(id:Long) : GeosurfModel?

}