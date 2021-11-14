package com.example.listazakupow.activity

import android.app.Activity
import android.os.Bundle
import android.widget.CheckBox
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.room.Room
import com.example.listazakupow.R
import com.example.listazakupow.database.ProductDatabase
import com.example.listazakupow.database.ProductDto
import com.example.listazakupow.databinding.ActivityAddProductBinding
import java.util.concurrent.Executors
import kotlin.concurrent.thread

class AddProductActivity : AppCompatActivity() {
    private val pool by lazy {
        Executors.newSingleThreadExecutor()
    }
    private val db by lazy {
        Room.databaseBuilder(this, ProductDatabase::class.java, "product").build()
    }
    private val view by lazy {
        ActivityAddProductBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(view.root)
//        setupShareButton()


        val id: Long = (intent.extras?.get("id") ?: -1L) as Long
        if (id != -1L) {
            setupSave(true, id)
            templateWithData(id)
        } else
            setupSave(false, 0L)
    }

    private fun templateWithData(id: Long) = thread {
        val transaction = db.products.getProductById(id)
        findViewById<EditText>(R.id.product_name).setText(transaction.name)
        findViewById<EditText>(R.id.product_amount).setText(transaction.amount.toString())
        findViewById<EditText>(R.id.product_price).setText(transaction.price.toString())
        findViewById<CheckBox>(R.id.product_done_edit)
    }

    private fun setupSave(edit: Boolean, id: Long) = view.saveButton.setOnClickListener {

        if (!edit) { //new
            val paymentDto = ProductDto(
                0,
                name = view.productName.text.toString(),
                amount = toDoubleNum(view.productAmount.text.toString()),
                price = toDoubleNum(view.productPrice.text.toString()),
                done = view.productDoneEdit.isChecked
            )
            pool.submit {
                db.products.insert(paymentDto)
                setResult(Activity.RESULT_OK)
                finish()
            }
        } else { //update
            val productDto = ProductDto(
                id,
                name = view.productName.text.toString(),
                amount = toDoubleNum(view.productAmount.text.toString()),
                price = toDoubleNum(view.productPrice.text.toString()),
                done = view.productDoneEdit.isChecked
            )
            pool.submit {
                db.products.updateProduct(productDto)
                setResult(Activity.RESULT_OK)
                finish()
            }
        }
    }
    private fun toDoubleNum(strNumber: String?): Double {
        return if (strNumber != null && strNumber.isNotEmpty()) {
            try {
                strNumber.toDouble()
            } catch (e: Exception) {
                (-1).toDouble()
            }
        } else 0.0
    }
}