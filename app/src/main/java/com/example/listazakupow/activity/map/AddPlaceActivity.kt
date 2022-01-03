package com.example.listazakupow.activity.map

import android.app.Activity
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
                findViewById<EditText>(R.id.place_place).setText(document.data?.getValue("place").toString())
            }
    }

    private fun setupSave(edit: Boolean, id: String) = view.saveButton.setOnClickListener {

        if (!edit) { //new
            val place = Place(
                id = "",
                name = view.placeName.text.toString(),
                description = view.placeDescription.text.toString(),
                loc = view.placePlace.text.toString(),
                radius = "brak"
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
                loc = view.placePlace.text.toString(),
                radius = "brak"
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
