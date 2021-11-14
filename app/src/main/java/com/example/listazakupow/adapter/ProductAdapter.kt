package com.example.listazakupow.adapter

import android.app.AlertDialog
import android.os.Looper
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.os.HandlerCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.listazakupow.MainActivity
import com.example.listazakupow.ProductViewHolder
import com.example.listazakupow.database.ProductDatabase
import com.example.listazakupow.databinding.ItemCardProductBinding
import com.example.listazakupow.model.Product
import kotlin.concurrent.thread

class ProductAdapter(private val db:ProductDatabase, val mainActivity: MainActivity): RecyclerView.Adapter<ProductViewHolder>() {
    private var data: List<Product> = emptyList()

    private val main = HandlerCompat.createAsync(Looper.getMainLooper())

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val view = ItemCardProductBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ProductViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        holder.bind(data[position])

        holder.itemView.setOnLongClickListener{
            val item = data[holder.layoutPosition].id
            removeItem(item)
        }

        holder.itemView.setOnClickListener{
            val item = data[holder.layoutPosition].id
            mainActivity.setupAddButton2(item)
        }
    }

    private fun removeItem(position: Long): Boolean
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

    fun deleteItem(position: Long){
        thread {
            db.products.deleteProductById(position)
            load()
        }
    }

    fun load() = thread{
        data = db.products.getAll().map { it.toModel() }
        main.post{
            notifyDataSetChanged()
        }
    }

    override fun getItemCount(): Int = data.size
}