package com.example.arsipsurat.ui.register

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.arsipsurat.data.model.SuratMasukItem
import com.example.arsipsurat.data.model.user.UserItem
import com.example.arsipsurat.databinding.ItemUserBinding

class RegisterAdapter: RecyclerView.Adapter<RegisterAdapter.ViewHolderRegister>() {
    class ViewHolderRegister(var binding: ItemUserBinding): RecyclerView.ViewHolder(binding.root)

    var listUser : List<UserItem?> = listOf()

    var onLongClick : (userItem: UserItem) -> Unit = {}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderRegister {
        val binding = ItemUserBinding.inflate(LayoutInflater.from(parent.context), parent, false )
        return ViewHolderRegister(binding)
    }

    override fun getItemCount(): Int = listUser.size

    override fun onBindViewHolder(holder: ViewHolderRegister, position: Int) {
        val username = listUser[position]?.username
        val level = listUser[position]?.level
        val passowrd = listUser[position]?.password

        holder.binding.tvUsername.text = username
        holder.binding.tvLevel.text = level
        holder.binding.tvPassword.text = passowrd

        holder.itemView.setOnClickListener {v->

        }
        holder.itemView.setOnLongClickListener {
            onLongClick(listUser[position]!!)
            return@setOnLongClickListener true
        }
    }


}