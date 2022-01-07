package com.example.listazakupow.activity.map

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.location.Criteria
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.example.listazakupow.R
import com.example.listazakupow.databinding.ActivityAddPlaceBinding
import com.example.listazakupow.model.Place
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.util.concurrent.Executors
import kotlin.concurrent.thread


class AddPlaceActivity : AppCompatActivity() {
    private val pool by lazy {
        Executors.newSingleThreadExecutor()
    }

    val db = Firebase.firestore

    private val view by lazy {
        ActivityAddPlaceBinding.inflate(layoutInflater)
    }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(view.root)



        val id: String = (intent.extras?.get("id") ?: "") as String
        if (!id.equals("")) {
            setupSave(true, id)
            templateWithData(id)
        } else
            setupSave(false, "")
    }

    private fun templateWithData(id: String) = thread {
        val db: FirebaseFirestore
        db = Firebase.firestore

        db.collection("place").document(id).get()
            .addOnSuccessListener { document ->
                findViewById<EditText>(R.id.place_name).setText(document.data?.getValue("name").toString())
                findViewById<EditText>(R.id.place_description).setText(document.data?.getValue("description").toString())
                findViewById<EditText>(R.id.place_radius).setText(document.data?.getValue("radius").toString())
            }
    }

    private fun setupSave(edit: Boolean, id: String) = view.saveButton.setOnClickListener {
         val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        val criteria = Criteria()
        criteria.isAltitudeRequired = true
        criteria.accuracy = Criteria.ACCURACY_FINE
        criteria.powerRequirement = Criteria.NO_REQUIREMENT
        var provider = locationManager.getProviders(true)

         var location: Location?
         location = null
        if(checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            location = locationManager.getLastKnownLocation(provider.get(2))
        } else {
            requestPermissions(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),1)
        }
        if (!edit) { //new
            val place = Place(
                id = "",
                name = view.placeName.text.toString(),
                description = view.placeDescription.text.toString(),
                latitude = location?.latitude.toString(),
                longitude = location?.longitude.toString(),
                radius = view.placeRadius.text.toString()
            )
            pool.submit {
                db.collection("place")
                    .add(place)
                    .addOnSuccessListener { prodRef ->
                        Log.d("place", "DocumentSnapshot added with ID: ${prodRef.id}")
                    }
                    .addOnFailureListener { e ->
                        Log.w("place", "Error adding product", e)
                    }
                setResult(Activity.RESULT_OK)
                finish()
            }


        } else { //update
            val place = Place(
                id,
                name = view.placeName.text.toString(),
                description = view.placeDescription.text.toString(),
                latitude = location?.latitude.toString(),
                longitude = location?.longitude.toString(),
                radius = view.placeRadius.text.toString()
            )
            pool.submit {
                db.collection("place")
                    .document(id).set(place)
                setResult(Activity.RESULT_OK)
                finish()
            }
        }
        }
}

//    private fun toDoubleNum(strNumber: String?): Double {
//        return if (strNumber != null && strNumber.isNotEmpty()) {
//            try {
//                strNumber.toDouble()
//            } catch (e: Exception) {
//                (-1).toDouble()
//            }
//        } else 0.0
//    }
