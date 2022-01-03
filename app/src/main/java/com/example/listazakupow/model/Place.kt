package com.example.listazakupow.model

import com.google.firebase.firestore.IgnoreExtraProperties
@IgnoreExtraProperties
data class Place (
        var id: String,
        var name: String,
        var description: String,
        var loc: String,
        var radius: String
    )
