package com.example.arsipsurat.ui.surat_masuk

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.arsipsurat.R
import com.example.arsipsurat.data.SharedPreferences
import com.example.arsipsurat.data.model.surat_masuk.SuratMasukItem
import com.example.arsipsurat.data.remote.ApiConfig
import com.example.arsipsurat.databinding.ItemSuratBinding
import com.example.arsipsurat.ui.detail.surat_masuk.DetailSuratMasukActivity
import com.google.gson.Gson

class SuratMasukAdapter: RecyclerView.Adapter<SuratMasukAdapter.ViewHolder>() {
    class ViewHolder(var binding: ItemSuratBinding) : RecyclerView.ViewHolder(binding.root)

    var onLongClick : (suratMasukItem: SuratMasukItem) -> Unit = {}
    var listSuratMasuk: List<SuratMasukItem?> = listOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemSuratBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = listSuratMasuk.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val tgl_surat = listSuratMasuk[position]?.tglSurat
        val perihal = listSuratMasuk[position]?.perihal
        val keterangan = listSuratMasuk[position]?.keterangan

        holder.binding.tvTanggalSurat.text = tgl_surat
        holder.binding.tvPerihal.text = perihal
        holder.binding.tvKeterangan.text = keterangan


        holder.itemView.setOnClickListener { v ->
            val sharedPreferences = v.context.getSharedPreferences(
                v.context.getString(R.string.shared_preferences_name),
                Context.MODE_PRIVATE
            )
            val editor = sharedPreferences.edit()
            val gson = Gson()
            editor.putString(SharedPreferences.KEY_CURRENT_SURAT_MASUK, gson.toJson(listSuratMasuk[position]))
            editor.apply()

            val intent = Intent(v.context, DetailSuratMasukActivity::class.java)
            v.context.startActivity(intent)
        }
        holder.itemView.setOnLongClickListener {
            onLongClick(listSuratMasuk[position]!!)
            return@setOnLongClickListener true
        }
    }
}