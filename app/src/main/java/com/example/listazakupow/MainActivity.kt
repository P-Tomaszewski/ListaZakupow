package com.example.listazakupow

import android.app.ActionBar
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Size
import android.view.ViewGroup
import androidx.annotation.ColorInt
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Room
import com.example.listazakupow.activity.AddProductActivity
import com.example.listazakupow.activity.OptionsActivity
import com.example.listazakupow.adapter.ProductAdapter
import com.example.listazakupow.databinding.ActivityMainBinding
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

private const val REQUEST_ADD_PRODUCT = 2

class MainActivity : AppCompatActivity() {
    private lateinit var db: FirebaseFirestore
    private val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    private val productAdapter by lazy {
        ProductAdapter(this)
    }

    lateinit var sharedPreferences: SharedPreferences

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setupProductList()
        setupAddButton()
        setupSettingsButton()
        sharedPreferences = getSharedPreferences("pref", Context.MODE_PRIVATE)
    }

    override fun onStart() {
        super.onStart()
        binding.recyclerView.setBackgroundColor(Color.parseColor(sharedPreferences.getString("color", "White")))
        binding.userName.text = sharedPreferences.getString("userNameList", "User")
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun setupProductList() {
            db = Firebase.firestore
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

    private fun setupSettingsButton() = binding.buttonSettings.setOnClickListener {
        val intent = Intent(this, OptionsActivity::class.java)
        startActivity(
            intent
        )
    }

    fun setupAddButton2(item: String) {
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