package com.example.listazakupow.model

import com.google.firebase.firestore.IgnoreExtraProperties

@IgnoreExtraProperties
data class Product(
    var id: String,
    var name: String,
    var price: String,
    var amount: String,
    var done: Boolean
)