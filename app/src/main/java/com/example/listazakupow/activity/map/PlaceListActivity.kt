package com.example.listazakupow.activity.map

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.listazakupow.activity.AddProductActivity
import com.example.listazakupow.adapter.PlaceAdapter
import com.example.listazakupow.databinding.ActivityPlaceListBinding
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

private const val REQUEST_ADD_PRODUCT = 2

class PlaceListActivity : AppCompatActivity() {
    private lateinit var db: FirebaseFirestore
    private val binding by lazy {
        ActivityPlaceListBinding.inflate(layoutInflater)
    }

    private val placeAdapter by lazy {
        PlaceAdapter(this)
    }


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setupProductList()
//        setupAddButton()
//        setupMapButton()
    }


    @RequiresApi(Build.VERSION_CODES.O)
    private fun setupProductList() {
        db = Firebase.firestore
        binding.recyclerView.apply {
            adapter = placeAdapter
            layoutManager = LinearLayoutManager(context)
        }
        placeAdapter.load()
    }

//    private fun setupAddButton() = binding.buttonProductAdd.setOnClickListener {
//        val intent = Intent(this, AddProductActivity::class.java)
//        startActivity(
//            intent
//        )
//    }
//
//    private fun setupMapButton() = binding.buttonMap.setOnClickListener {
//        val intent = Intent(this, MapActivity::class.java)
//        startActivityForResult(
//            intent, REQUEST_ADD_PRODUCT
//        )
//    }
//
//    fun setupAddButton2(item: String) {
//        var intent = Intent(this, AddProductActivity::class.java)
//        startActivityForResult(intent.putExtra("id", item), REQUEST_ADD_PRODUCT)
//    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_ADD_PRODUCT && resultCode == Activity.RESULT_OK) {
            placeAdapter.load()
        } else
            super.onActivityResult(requestCode, resultCode, data)
    }
}