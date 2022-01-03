package com.example.listazakupow.activity.map

import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.CircleOptions
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.example.listazakupow.R
import com.example.listazakupow.databinding.ActivityMapBinding


const val RANGE = 500f

class MapActivity: AppCompatActivity(), OnMapReadyCallback {
    lateinit var map: GoogleMap
    val mapFragment: SupportMapFragment
    get() = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment

    private val binding by lazy {
        ActivityMapBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setupAddPlaceButton()
        showPlaceListButton()
        mapFragment.getMapAsync(this)

    }

        private fun setupAddPlaceButton() = binding.addPlace.setOnClickListener {
        val intent = Intent(this, AddPlaceActivity::class.java)
        startActivity(
            intent
        )
    }

    private fun showPlaceListButton() = binding.placeList.setOnClickListener {
        val intent = Intent(this, PlaceListActivity::class.java)
        startActivity(
            intent
        )
    }

    override fun onMapReady(googleMap: GoogleMap) {
        var l1 = LatLng(52.188206, 20.879588)
        var l2 = LatLng(52.078866, 21.000044)
        var l3 = LatLng(52.345560, 21.231787)
        map = googleMap
       if(checkSelfPermission(ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
           map.addMarker(MarkerOptions().position(l1).title("Nr1"))
           map.isMyLocationEnabled = true
       } else {
           requestPermissions(arrayOf(ACCESS_FINE_LOCATION),1)
       }
        map.setOnMapClickListener { onClicked(it) }
    }

    private fun onClicked(latLng: LatLng) {
        val circle = CircleOptions()
            .strokeColor(Color.RED)
            .radius(RANGE.toDouble())
            .center(latLng)
            .strokeWidth(10f)
        map.addCircle(circle)
    }

    @SuppressLint("MissingPermission")
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if(requestCode == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
            map.isMyLocationEnabled = true
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }
}