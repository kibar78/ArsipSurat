package com.example.arsipsurat.ui.surat_keluar

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.arsipsurat.R
import com.example.arsipsurat.data.SharedPreferences
import com.example.arsipsurat.data.model.surat_keluar.SuratKeluarItem
import com.example.arsipsurat.databinding.ItemSuratBinding
import com.example.arsipsurat.ui.detail.surat_keluar.DetailSuratKeluarActivity
import com.google.gson.Gson

class SuratKeluarAdapter(): RecyclerView.Adapter<SuratKeluarAdapter.ViewHolderSuratKeluar>(){

    class ViewHolderSuratKeluar(var binding: ItemSuratBinding): RecyclerView.ViewHolder(binding.root)

        var onLongClick : (suratKeluarItem: SuratKeluarItem) -> Unit = {}
        var listSuratKeluar: List<SuratKeluarItem?> = listOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderSuratKeluar {
        val binding = ItemSuratBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolderSuratKeluar(binding)
    }

    override fun getItemCount(): Int = listSuratKeluar.size

    override fun onBindViewHolder(holder: ViewHolderSuratKeluar, position: Int) {

        val tglSurat = listSuratKeluar[position]?.tglSurat
        val perihal = listSuratKeluar[position]?.perihal
        val noSurat  = listSuratKeluar[position]?.noSurat

        holder.binding.tvTanggalSurat.text = tglSurat
        holder.binding.tvPerihal.text = perihal
        holder.binding.tvNomorSurat.text = noSurat

        holder.itemView.setOnClickListener { v->
            val sharedPreferences = v.context.getSharedPreferences(
                v.context.getString(R.string.shared_preferences_name_keluar),
                Context.MODE_PRIVATE
            )
            val editor = sharedPreferences.edit()
            val gson = Gson()
            editor.putString(SharedPreferences.KEY_CURRENT_SURAT_KELUAR, gson.toJson(listSuratKeluar[position]))
            editor.apply()

            val intent = Intent(v.context, DetailSuratKeluarActivity::class.java)
            v.context.startActivity(intent)
        }
        holder.itemView.setOnLongClickListener {
            onLongClick(listSuratKeluar[position]!!)
            return@setOnLongClickListener true
        }
    }
}