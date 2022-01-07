package com.example.listazakupow.adapter

import android.os.Build
import android.os.Looper
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.core.os.HandlerCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.listazakupow.PlaceViewHolder
import com.example.listazakupow.activity.map.MapActivity
import com.example.listazakupow.activity.map.PlaceListActivity
import com.example.listazakupow.databinding.ItemCardPlaceBinding
import com.example.listazakupow.model.Place
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlin.concurrent.thread

class MapAdapter(val mainActivity: MapActivity) {
    private val main = HandlerCompat.createAsync(Looper.getMainLooper())
    var places: List<Place> = listOf()

    @RequiresApi(Build.VERSION_CODES.O)
    fun load() = thread{
        val db: FirebaseFirestore
        db = Firebase.firestore
        var placesTemp = mutableListOf<Place>()
        db.collection("place")
            .get()
            .addOnSuccessListener { result ->
                for (place in result) {
                    var productTemp = Place(
                        place.id,
                        place.data.getValue("name").toString(),
                        place.data.getValue("description").toString(),
                        place.data.getValue("latitude").toString(),
                        place.data.getValue("longitude").toString(),
                        place.data.getValue("radius").toString()
                    )
                    placesTemp.add(productTemp)
                }
                places = placesTemp
            }
    }
}