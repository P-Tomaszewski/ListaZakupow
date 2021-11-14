package com.example.listazakupow.database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [ProductDto::class],
    version = 1
)
abstract class ProductDatabase: RoomDatabase(){
    abstract val products: ProductDao
}