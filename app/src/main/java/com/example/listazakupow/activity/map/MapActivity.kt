package com.example.listazakupow.activity.map

import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.annotation.SuppressLint
import android.app.PendingIntent
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.example.listazakupow.GeofenceReceiver
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.CircleOptions
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.example.listazakupow.R
import com.example.listazakupow.adapter.MapAdapter
import com.example.listazakupow.databinding.ActivityMapBinding
import com.google.android.gms.location.Geofence
import com.google.android.gms.location.GeofencingRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationServices.getGeofencingClient


class MapActivity : AppCompatActivity(), OnMapReadyCallback {
    lateinit var map: GoogleMap
    val mapFragment: SupportMapFragment
        get() = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment


    private val binding by lazy {
        ActivityMapBinding.inflate(layoutInflater)
    }

    private val mapAdapter by lazy {
        MapAdapter(this)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {

        mapAdapter.load()
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
        map = googleMap
        val geofencingClient = getGeofencingClient(this)
        var id = 0
        var geo: Geofence
        if (checkSelfPermission(ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            if (mapAdapter.places.size != 0) {
                for (i in 0..mapAdapter.places.size - 1) {
                    val latLng = LatLng(
                        mapAdapter.places[i].latitude.toDouble(),
                        mapAdapter.places[i].longitude.toDouble(),
                    )
                    val radius = mapAdapter.places[i].radius
                    map.addMarker(
                        MarkerOptions().position(latLng).title(mapAdapter.places[i].name)
                    )
                    writeCircle(latLng, radius)
                    geo = Geofence.Builder().setRequestId("Geo${id++}")
                        .setCircularRegion(
                            latLng.latitude,
                            latLng.longitude,
                            radius.toFloat()
                        )
                        .setExpirationDuration(60*60*1000)
                        .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER
                                or Geofence.GEOFENCE_TRANSITION_EXIT)
                        .build()
                    geofencingClient?.addGeofences(getGeofancingRequest(geo), getGeofencePendingIntent())
                        .addOnSuccessListener {
                            Toast.makeText(
                                this, "Geofence dodano", Toast.LENGTH_SHORT
                            ).show()
                        }
                        .addOnFailureListener {
                            Toast.makeText(
                                this,
                                "Geofence nie został dodany", Toast.LENGTH_SHORT
                            ).show()
                        }
                }
                map.isMyLocationEnabled = true
            }
        } else {
            requestPermissions(arrayOf(ACCESS_FINE_LOCATION), 1)
        }
    }

    private fun getGeofancingRequest(geofence: Geofence): GeofencingRequest{
            return GeofencingRequest.Builder()
                .setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER)
                .addGeofence(geofence)
                .build()
    }

    private fun getGeofencePendingIntent(): PendingIntent {
        return PendingIntent.getBroadcast(
            this,
            0,
            Intent(this, GeofenceReceiver::class.java),
            PendingIntent.FLAG_UPDATE_CURRENT)

    }

    private fun writeCircle(latLng: LatLng, radius: String) {
        val circle = CircleOptions()
            .strokeColor(Color.RED)
            .radius(radius.toDouble())
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
        if (requestCode == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            map.isMyLocationEnabled = true
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }
}