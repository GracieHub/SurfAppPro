package org.wit.geosurf.models

import android.content.Context
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class GeosurfFireStore(val context: Context) : GeosurfStore {
    val geosurfs = ArrayList<GeosurfModel>()
    lateinit var userId: String
    var db: DatabaseReference = FirebaseDatabase.getInstance().reference


    override suspend fun findAll(): List<GeosurfModel> {
        return geosurfs
    }

    override suspend fun findById(id: Long): GeosurfModel? {
        val foundGeosurf: GeosurfModel? = geosurfs.find { p -> p.id == id }
        return foundGeosurf
    }

    override suspend fun findGeosurfById(geosurfId: Long): GeosurfModel? {
        var foundGeosurf: GeosurfModel? = geosurfs.find { t -> t.id == geosurfId }
        return foundGeosurf
    }

    override suspend fun create(geosurf: GeosurfModel) {
        val key = db.child("users").child(userId).child("geosurfs").push().key
        key?.let {
            geosurf.fbId = key
            geosurfs.add(geosurf)
            db.child("users").child(userId).child("geosurfs").child(key).setValue(geosurf)
        }
    }

    override suspend fun update(geosurf: GeosurfModel) {
        var foundGeosurf: GeosurfModel? = geosurfs.find { p -> p.fbId == geosurf.fbId }
        if (foundGeosurf != null) {
            foundGeosurf.title = geosurf.title
            foundGeosurf.description = geosurf.description
            foundGeosurf.image = geosurf.image
            foundGeosurf.county = geosurf.county
            foundGeosurf.image = geosurf.image
            foundGeosurf.lat = geosurf.lat
            foundGeosurf.lng = geosurf.lng
            foundGeosurf.zoom = geosurf.zoom
            foundGeosurf.date = geosurf.date
            foundGeosurf.abilityLevel = geosurf.abilityLevel
            foundGeosurf.rating =geosurf.rating
        }
        db.child("users").child(userId).child("geosurfs").child(geosurf.fbId).setValue(geosurf)

    }

    override suspend fun delete(geosurf: GeosurfModel) {
        db.child("users").child(userId).child("geosurfs").child(geosurf.fbId).removeValue()
        geosurfs.remove(geosurf)
    }

    override suspend fun clear() {
        geosurfs.clear()
    }

    fun fetchGeosurfs(geosurfsReady: () -> Unit) {
        val valueEventListener = object : ValueEventListener {
            override fun onCancelled(dataSnapshot: DatabaseError) {
            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                dataSnapshot!!.children.mapNotNullTo(geosurfs) {
                    it.getValue<GeosurfModel>(
                        GeosurfModel::class.java
                    )
                }
                geosurfsReady()
            }
        }
        userId = FirebaseAuth.getInstance().currentUser!!.uid
        db = FirebaseDatabase.getInstance("\n" + "https://surfapppro-default-rtdb.europe-west1.firebasedatabase.app").reference
        geosurfs.clear()
        db.child("users").child(userId).child("geosurfs")
            .addListenerForSingleValueEvent(valueEventListener)
    }
}