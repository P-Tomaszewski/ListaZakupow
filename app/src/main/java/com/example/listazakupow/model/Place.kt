package com.example.listazakupow.model

import android.location.Location
import com.google.firebase.firestore.IgnoreExtraProperties
@IgnoreExtraProperties
data class Place (
        var id: String,
        var name: String,
        var description: String,
        var latitude: String,
        var longitude: String,
        var radius: String
    )
