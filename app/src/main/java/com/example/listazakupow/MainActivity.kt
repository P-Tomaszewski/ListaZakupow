package com.example.listazakupow

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Room
import com.example.listazakupow.activity.AddProductActivity
import com.example.listazakupow.adapter.ProductAdapter
import com.example.listazakupow.database.ProductDatabase
import com.example.listazakupow.databinding.ActivityMainBinding
private const val REQUEST_ADD_PRODUCT = 2

class MainActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    private val productAdapter by lazy {
        ProductAdapter(db, this)
    }

    private val db by lazy {
        Room.databaseBuilder(this, ProductDatabase::class.java, "product").build()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setupProductList()
        setupAddButton()
    }

    private fun setupProductList() {
        binding.recyclerView.apply {
            adapter = productAdapter
            layoutManager = LinearLayoutManager(context)
        }
        productAdapter.load()
    }

    private fun setupAddButton() = binding.buttonProductAdd.setOnClickListener {
        val intent = Intent(this, AddProductActivity::class.java)
        startActivity(
            intent
        )
    }

    fun setupAddButton2(item: Long) {
        var intent = Intent(this, AddProductActivity::class.java)
        startActivityForResult(intent.putExtra("id", item), REQUEST_ADD_PRODUCT)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_ADD_PRODUCT && resultCode == Activity.RESULT_OK) {
            productAdapter.load()
        } else
            super.onActivityResult(requestCode, resultCode, data)
    }
}