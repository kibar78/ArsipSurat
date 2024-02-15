package com.example.arsipsurat.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.arsipsurat.databinding.ItemHomeHorizontalBinding

class HomeAdapter(private val listPoster: ArrayList<Poster>): RecyclerView.Adapter<HomeAdapter.ListViewHolder>() {
    class ListViewHolder(var binding: ItemHomeHorizontalBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val binding = ItemHomeHorizontalBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(binding)
    }

    override fun getItemCount(): Int = listPoster.size

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val (name, photo) = listPoster[position]
        holder.binding.ivPoster.setImageResource(photo)
    }
}