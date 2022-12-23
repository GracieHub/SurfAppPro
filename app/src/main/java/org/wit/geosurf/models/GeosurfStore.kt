package org.wit.geosurf.models

interface GeosurfStore {
    suspend fun findAll(): List<GeosurfModel>
    suspend fun create(geosurf: GeosurfModel)
    suspend fun update(geosurf: GeosurfModel)
    suspend fun delete(geosurf: GeosurfModel)
    suspend fun findById(id:Long) : GeosurfModel?
    suspend fun findGeosurfById(id:Long) : GeosurfModel?
    suspend fun clear()

}