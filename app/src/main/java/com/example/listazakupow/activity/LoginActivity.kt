package com.example.listazakupow.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.listazakupow.MainActivity
import com.example.listazakupow.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        val binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val fbAuth = FirebaseAuth.getInstance()

        binding.login?.setOnClickListener{
            fbAuth.signInWithEmailAndPassword(
                binding.username.text.toString(),
                binding.password.text.toString()
            ).addOnSuccessListener {
                Toast.makeText(
                    this,
                    "Witaj ${fbAuth.currentUser!!.email}!",
                    Toast.LENGTH_SHORT
                ).show()
                startActivity(Intent(this, MainActivity::class.java))
            }.addOnFailureListener{
                Toast.makeText(
                    this,
                    "Logowanie sie nie powiodło",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        binding.register.setOnClickListener{
            fbAuth.createUserWithEmailAndPassword(
                binding.username.text.toString(),
                binding.password.text.toString()
            ).addOnSuccessListener {
                Toast.makeText(
                    this,
                    "Rejestracja sie powiodła",
                    Toast.LENGTH_SHORT
                ).show()
            }.addOnFailureListener{
                Toast.makeText(
                    this,
                    "Rejestracje sie nie powiodla",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

    }
}