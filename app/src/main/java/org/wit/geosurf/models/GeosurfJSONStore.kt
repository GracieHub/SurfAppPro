package org.wit.geosurf.models

import android.content.Context
import android.net.Uri
import com.google.gson.*
import com.google.gson.reflect.TypeToken
import org.wit.geosurf.helpers.*
import timber.log.Timber
import java.lang.reflect.Type
import java.util.*

const val JSON_FILE = "geosurfs.json"
val gsonBuilder: Gson = GsonBuilder().setPrettyPrinting()
    .registerTypeAdapter(Uri::class.java, UriParser())
    .create()
val listType: Type = object : TypeToken<ArrayList<GeosurfModel>>() {}.type

fun generateRandomId(): Long {
    return Random().nextLong()
}

class GeosurfJSONStore(private val context: Context) : GeosurfStore {

    var geosurfs = mutableListOf<GeosurfModel>()

    init {
        if (exists(context, JSON_FILE)) {
            deserialize()
        }
    }

    override fun findAll(): MutableList<GeosurfModel> {
        logAll()
        return geosurfs
    }

    override fun create(geosurf: GeosurfModel) {
        geosurf.id = generateRandomId()
        geosurfs.add(geosurf)
        serialize()
    }


    override fun update(geosurf: GeosurfModel) {
        val geosurfsList = findAll() as ArrayList<GeosurfModel>
        var foundGeosurf: GeosurfModel? = geosurfsList.find { p -> p.id == geosurf.id }
        if (foundGeosurf != null) {
            foundGeosurf.title = geosurf.title
            foundGeosurf.description = geosurf.description
            foundGeosurf.image = geosurf.image
            foundGeosurf.lat = geosurf.lat
            foundGeosurf.lng = geosurf.lng
            foundGeosurf.zoom = geosurf.zoom
            foundGeosurf.date = geosurf.date
            foundGeosurf.abilityLevel = geosurf.abilityLevel
            foundGeosurf.rating = geosurf.rating

        }
        serialize()
    }

    override fun delete(geosurf: GeosurfModel) {
        geosurfs.remove(geosurf)
        serialize()
    }

    private fun serialize() {
        val jsonString = gsonBuilder.toJson(geosurfs, listType)
        write(context, JSON_FILE, jsonString)
    }

    private fun deserialize() {
        val jsonString = read(context, JSON_FILE)
        geosurfs = gsonBuilder.fromJson(jsonString, listType)
    }

    private fun logAll() {
        geosurfs.forEach { Timber.i("$it") }
    }
}

class UriParser : JsonDeserializer<Uri>,JsonSerializer<Uri> {
    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): Uri {
        return Uri.parse(json?.asString)
    }

    override fun serialize(
        src: Uri?,
        typeOfSrc: Type?,
        context: JsonSerializationContext?
    ): JsonElement {
        return JsonPrimitive(src.toString())
    }
}