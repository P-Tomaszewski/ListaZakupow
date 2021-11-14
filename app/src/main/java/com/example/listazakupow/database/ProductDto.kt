package com.example.listazakupow.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.listazakupow.model.Product

@Entity(tableName = "product")
data class ProductDto(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val name: String,
    val price: Double,
    val amount: Double,
    val done: Boolean
) {
    fun toModel() = Product(
        id,
        name,
        price,
        amount,
        done
    )
}