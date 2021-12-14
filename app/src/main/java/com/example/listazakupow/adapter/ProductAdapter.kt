package com.example.listazakupow.adapter

import android.app.AlertDialog
import android.os.Build
import android.os.Looper
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.core.os.HandlerCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.listazakupow.MainActivity
import com.example.listazakupow.ProductViewHolder
import com.example.listazakupow.databinding.ItemCardProductBinding
import com.example.listazakupow.model.Product
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlin.concurrent.thread

class ProductAdapter(val mainActivity: MainActivity): RecyclerView.Adapter<ProductViewHolder>() {

    private val main = HandlerCompat.createAsync(Looper.getMainLooper())
    private var products: List<Product> = listOf()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val view = ItemCardProductBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ProductViewHolder(view)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun load() = thread{
        val db: FirebaseFirestore
        db = Firebase.firestore
        var productsTemp = mutableListOf<Product>()
        db.collection("product")
            .get()
            .addOnSuccessListener { result ->
                for (product in result) {
                    var productTemp = Product(
                        product.id,
                        product.data.getValue("name").toString(),
                        product.data.getValue("amount").toString(),
                        product.data.getValue("price").toString(),
                        product.data.getValue("done") as Boolean,
                    )
                    productsTemp.add(productTemp)
                }
                products = productsTemp
                main.post{
                    notifyDataSetChanged()
                }
            }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        holder.bind(products[position])

        holder.itemView.setOnLongClickListener {
            val item = products[holder.layoutPosition].id
            removeItem(item)
        }


        holder.itemView.setOnClickListener{
            val item = products[holder.layoutPosition].id
            mainActivity.setupAddButton2(item)
        }
    }
//
    @RequiresApi(Build.VERSION_CODES.O)
    private fun removeItem(position: String): Boolean
    {
        val builder = AlertDialog.Builder(mainActivity)
        builder.setMessage("Are you sure you want to Delete?")
            .setCancelable(false)
            .setPositiveButton("Yes") { _, _ ->
                deleteItem(position)
            }
            .setNegativeButton("No") { dialog, _ ->
                dialog.dismiss()
            }
        val alert = builder.create()
        alert.show()
        return true
    }

@RequiresApi(Build.VERSION_CODES.O)
fun deleteItem(position: String){
        thread {

            val db: FirebaseFirestore
            db = Firebase.firestore
            db.collection("product").document(position)
                .delete()
            load()
        }
    }

//    fun load() = thread{
//        products = db.products.getAll().map { it.toModel() }
//        main.post{
//            notifyDataSetChanged()
//        }
//    }

    override fun getItemCount(): Int = products.size
}