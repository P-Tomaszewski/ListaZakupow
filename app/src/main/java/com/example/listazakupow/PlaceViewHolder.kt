package com.example.listazakupow

import androidx.recyclerview.widget.RecyclerView
import com.example.listazakupow.databinding.ItemCardPlaceBinding
import com.example.listazakupow.databinding.ItemCardProductBinding
import com.example.listazakupow.model.Place

class PlaceViewHolder(private val binding: ItemCardPlaceBinding): RecyclerView.ViewHolder(binding.root) {
    fun bind(place: Place){
        with(binding){
            name.text = place.name
            decription.text = place.loc
            loc.text = place.loc
        }
    }
}