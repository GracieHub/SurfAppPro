package org.wit.geosurf.models

import android.content.Context
import android.graphics.Bitmap
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import org.wit.geosurf.helpers.readImageFromPath
import timber.log.Timber.i
import java.io.ByteArrayOutputStream
import java.io.File

class GeosurfFireStore(val context: Context) : GeosurfStore {
    val geosurfs = ArrayList<GeosurfModel>()
    lateinit var userId: String
    var db: DatabaseReference = FirebaseDatabase.getInstance().reference
    lateinit var st: StorageReference


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
            updateImage(geosurf)

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
        if(geosurf.image.length > 0){
            updateImage(geosurf)
        }
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
        st = FirebaseStorage.getInstance("gs://surfapppro.appspot.com").reference
        db = FirebaseDatabase.getInstance("\n" + "https://surfapppro-default-rtdb.europe-west1.firebasedatabase.app").reference
        geosurfs.clear()
        db.child("users").child(userId).child("geosurfs")
            .addListenerForSingleValueEvent(valueEventListener)
    }

    fun updateImage(geosurf: GeosurfModel) {
        if (geosurf.image != "") {
            val fileName = File(geosurf.image)
            val imageName = fileName.getName()

            var imageRef = st.child(userId + '/' + imageName)
            val baos = ByteArrayOutputStream()
            val bitmap = readImageFromPath(context, geosurf.image)

            bitmap?.let {
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
                val data = baos.toByteArray()
                val uploadTask = imageRef.putBytes(data)
                uploadTask.addOnFailureListener {
                    println(it.message)
                }.addOnSuccessListener { taskSnapshot ->
                    taskSnapshot.metadata!!.reference!!.downloadUrl.addOnSuccessListener {
                        geosurf.image = it.toString()
                        db.child("users").child(userId).child("geosurfs").child(geosurf.fbId).setValue(geosurf)
                    }
                }.addOnFailureListener {
                    var errorMessage = it.message
                    i("Failure: $errorMessage")
                }
            }
        }
    }
}