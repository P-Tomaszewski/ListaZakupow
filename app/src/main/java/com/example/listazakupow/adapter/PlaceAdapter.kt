package com.example.listazakupow.adapter

import android.app.AlertDialog
import android.os.Build
import android.os.Looper
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.core.os.HandlerCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.listazakupow.PlaceViewHolder
import com.example.listazakupow.activity.map.PlaceListActivity
import com.example.listazakupow.databinding.ItemCardPlaceBinding
import com.example.listazakupow.model.Place
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlin.concurrent.thread

class PlaceAdapter(val mainActivity: PlaceListActivity): RecyclerView.Adapter<PlaceViewHolder>() {

    private val main = HandlerCompat.createAsync(Looper.getMainLooper())
    private var places: List<Place> = listOf()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaceViewHolder {
        val view = ItemCardPlaceBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return PlaceViewHolder(view)
    }

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
                main.post{
                    notifyDataSetChanged()
                }
            }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: PlaceViewHolder, position: Int) {
        holder.bind(places[position])

        holder.itemView.setOnLongClickListener {
            val item = places[holder.layoutPosition].id
            removeItem(item)
        }


//        holder.itemView.setOnClickListener{
//            val item = products[holder.layoutPosition].id
//            mainActivity.setupAddButton2(item)
//        }
    }
//
    @RequiresApi(Build.VERSION_CODES.O)
    private fun removeItem(position: String): Boolean
    {
        val builder = AlertDialog.Builder(mainActivity)
        builder.setMessage("Are you sure you want to Delete?")
            .setCancelable(false)
            .setPositiveButton("Yes") { _, _ ->
                deleteItem(position)
            }
            .setNegativeButton("No") { dialog, _ ->
                dialog.dismiss()
            }
        val alert = builder.create()
        alert.show()
        return true
    }

@RequiresApi(Build.VERSION_CODES.O)
fun deleteItem(position: String){
        thread {

            val db: FirebaseFirestore
            db = Firebase.firestore
            db.collection("place").document(position)
                .delete()
            load()
        }
    }

//    fun load() = thread{
//        products = db.products.getAll().map { it.toModel() }
//        main.post{
//            notifyDataSetChanged()
//        }
//    }

    override fun getItemCount(): Int = places.size
}