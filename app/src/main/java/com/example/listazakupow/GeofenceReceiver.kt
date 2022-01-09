package com.example.listazakupow

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.google.android.gms.location.Geofence
import com.google.android.gms.location.GeofenceStatusCodes
import com.google.android.gms.location.GeofencingEvent

class GeofenceReceiver : BroadcastReceiver() {
    val requestCode = 1
    var id = 1
    override fun onReceive(context: Context, intent: Intent) {
        val geoEvent = GeofencingEvent.fromIntent(intent)
        createNotificationChannel(context)
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

            val pendingIntent = PendingIntent.getActivity(
                context,
                requestCode,
                intent,
                PendingIntent.FLAG_CANCEL_CURRENT
            )
            val not = NotificationCompat.Builder(context, "12")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("Wszedłeś w obszar: " + intent.getStringExtra("name"))
                .setContentText(intent.getStringExtra("name"))
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .build()

            val notificationManager = NotificationManagerCompat.from(context)
            notificationManager.notify(id++, not)

            Log.i("geofences", "Wejscie: ${geoEvent.triggeringLocation}")
        } else if(geoEvent.geofenceTransition == Geofence.GEOFENCE_TRANSITION_EXIT){
            Log.i("geofences", "Wyjscie: ${geoEvent.triggeringLocation}")
        } else {
            Log.e("geofences", "Error")
        }
    }

    fun createNotificationChannel( context: Context){
        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.O)
            return
        val notificationChannel = NotificationChannel(
            "12",
            "asd",
            NotificationManager.IMPORTANCE_DEFAULT
        )
        notificationChannel.description = "lok"

        val notificationManager = NotificationManagerCompat.from(context)
        notificationManager.createNotificationChannel(notificationChannel)
    }
}