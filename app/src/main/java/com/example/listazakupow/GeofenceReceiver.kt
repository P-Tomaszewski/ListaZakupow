package com.example.listazakupow

import android.content.BroadcastReceiver
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.util.Log
import com.google.android.gms.location.Geofence
import com.google.android.gms.location.GeofenceStatusCodes
import com.google.android.gms.location.GeofencingEvent

class GeofenceReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val geoEvent = GeofencingEvent.fromIntent(intent)
        if (geoEvent.hasError()) {
            val errorMessage = GeofenceStatusCodes
                .getStatusCodeString(geoEvent.errorCode)
            Log.e(TAG, errorMessage)
            return
        }
        val triggering = geoEvent.triggeringGeofences
        for( geo in triggering){
            Log.i("geofence", "Geofence z id: ${geo.requestId} aktywny.")
        }
        if(geoEvent.geofenceTransition == Geofence.GEOFENCE_TRANSITION_ENTER){
            Log.i("geofences", "Wejscie: ${geoEvent.triggeringLocation}")
        } else if(geoEvent.geofenceTransition == Geofence.GEOFENCE_TRANSITION_EXIT){
            Log.i("geofences", "Wyjscie: ${geoEvent.triggeringLocation}")
        } else {
            Log.e("geofences", "Error")
        }
    }
}