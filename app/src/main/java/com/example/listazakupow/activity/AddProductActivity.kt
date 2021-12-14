package com.example.listazakupow.activity

import android.app.Activity
import android.content.ComponentName
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.CheckBox
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.example.listazakupow.R
import com.example.listazakupow.databinding.ActivityAddProductBinding
import com.example.listazakupow.model.Product
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.util.concurrent.Executors
import kotlin.concurrent.thread

class AddProductActivity : AppCompatActivity() {
    private val pool by lazy {
        Executors.newSingleThreadExecutor()
    }

    val db = Firebase.firestore

    private val view by lazy {
        ActivityAddProductBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(view.root)


        val id: String = (intent.extras?.get("id") ?: "") as String
        if (!id.equals("")) {
            setupSave(true, id)
            templateWithData(id)
        } else
            setupSave(false, "")
    }

    private fun templateWithData(id: String) = thread {
        val db: FirebaseFirestore
        db = Firebase.firestore

        db.collection("product").document(id).get()
            .addOnSuccessListener { document ->
                findViewById<EditText>(R.id.product_name).setText(document.data?.getValue("name").toString())
                findViewById<EditText>(R.id.product_amount).setText(document.data?.getValue("amount").toString())
                findViewById<EditText>(R.id.product_price).setText(document.data?.getValue("price").toString())
                findViewById<CheckBox>(R.id.product_done_edit).isChecked = document.data?.getValue("done") as Boolean
            }
    }

    private fun setupSave(edit: Boolean, id: String) = view.saveButton.setOnClickListener {
        val broadcastIntent = Intent()
        broadcastIntent.component = ComponentName(
            "com.example.receiver3",
            "com.example.receiver3.MyReceiver"
        )

        if (!edit) { //new
            val productDto = Product(
                id = "",
                name = view.productName.text.toString(),
                amount = view.productAmount.text.toString(),
                price = view.productPrice.text.toString(),
                done = view.productDoneEdit.isChecked
            )
            pool.submit {
                db.collection("product")
                    .add(productDto)
                    .addOnSuccessListener { prodRef ->
                        Log.d("product", "DocumentSnapshot added with ID: ${prodRef.id}")
                    }
                    .addOnFailureListener { e ->
                        Log.w("product", "Error adding product", e)
                    }
                setResult(Activity.RESULT_OK)
                finish()
//                broadcastIntent.putExtra(
//                    "productId",
//                    productId
//                )
//                broadcastIntent.putExtra(
//                    "name",
//                    productDto.name
//                )
//                sendBroadcast(broadcastIntent)
            }


        } else { //update
            val productDto = Product(
                id,
                name = view.productName.text.toString(),
                amount = view.productAmount.text.toString(),
                price = view.productPrice.text.toString(),
                done = view.productDoneEdit.isChecked
            )
            pool.submit {
                db.collection("product")
                    .document(id).set(productDto)
                setResult(Activity.RESULT_OK)
                finish()
            }
        }
        }
}

//    private fun toDoubleNum(strNumber: String?): Double {
//        return if (strNumber != null && strNumber.isNotEmpty()) {
//            try {
//                strNumber.toDouble()
//            } catch (e: Exception) {
//                (-1).toDouble()
//            }
//        } else 0.0
//    }
