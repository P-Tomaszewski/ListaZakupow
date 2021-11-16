package com.example.listazakupow.activity

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import com.example.listazakupow.R
import com.example.listazakupow.databinding.ActivityOptionsBinding



class OptionsActivity : AppCompatActivity() {

    private val view by lazy {
        ActivityOptionsBinding.inflate(layoutInflater)
    }

    lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(view.root)
        val spinner: Spinner = findViewById(R.id.spinner)
        ArrayAdapter.createFromResource(
            this,
            R.array.background_color,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinner.adapter = adapter
        }
        sharedPreferences = getSharedPreferences("pref",Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        view.button.setOnClickListener{
            val str = view.spinner.selectedItem.toString()
            val userName = view.listUserName.text.toString()
            editor.putString("color", str)
            editor.putString("userNameList", userName)
            editor.apply()
        }
    }


}