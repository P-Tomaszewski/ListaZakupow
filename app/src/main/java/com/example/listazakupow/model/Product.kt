package com.example.listazakupow.model

data class Product(
    val id: Long,
    val name: String,
    val price: Double,
    val amount: Double,
    val done: Boolean
)