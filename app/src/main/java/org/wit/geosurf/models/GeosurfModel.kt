package org.wit.geosurf.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import android.widget.DatePicker
import java.time.LocalDate


@Parcelize
data class GeosurfModel(var id: Long = 0,
                        var fbId: String = "",
                        var image: String = "",
                        var title: String = "",
                        var description: String = "",
                        var lat : Double = 0.0,
                        var lng: Double = 0.0,
                        var county: String = "",
                        var zoom: Float = 0f,
                        var abilityLevel: String = "",
                        var date: String = "",
                        var rating: Float = 0f) : Parcelable



@Parcelize
data class Location(var lat: Double = 0.0,
                    var lng: Double = 0.0,
                    var zoom: Float = 0f) : Parcelable