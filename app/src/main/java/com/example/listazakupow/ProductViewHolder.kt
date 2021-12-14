package com.example.listazakupow

import androidx.recyclerview.widget.RecyclerView
import com.example.listazakupow.databinding.ItemCardProductBinding
import com.example.listazakupow.model.Product

class ProductViewHolder(private val viewBinding: ItemCardProductBinding): RecyclerView.ViewHolder(viewBinding.root) {
    fun bind(product: Product){
        with(viewBinding){
            name.text = product.name
            price.text = product.price
            amount.text = product.amount
            done.isChecked = product.done
        }
    }
}