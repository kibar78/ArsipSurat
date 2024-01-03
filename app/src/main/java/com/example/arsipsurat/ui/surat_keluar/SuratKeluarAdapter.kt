package com.example.arsipsurat.ui.surat_keluar

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.arsipsurat.data.model.SuratKeluarItem
import com.example.arsipsurat.databinding.ItemSuratBinding

class SuratKeluarAdapter(private val listSuratKeluar: List<SuratKeluarItem?>,):
    RecyclerView.Adapter<SuratKeluarAdapter.ViewHolderSuratKeluar>(){

    class ViewHolderSuratKeluar(var binding: ItemSuratBinding): RecyclerView.ViewHolder(binding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderSuratKeluar {
        val binding = ItemSuratBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolderSuratKeluar(binding)
    }

    override fun getItemCount(): Int = listSuratKeluar.size

    override fun onBindViewHolder(holder: ViewHolderSuratKeluar, position: Int) {
        val perihal = listSuratKeluar[position]?.perihal
        //val dariMana = listSuratKeluar[position]?.dariMana
        val tglSurat = listSuratKeluar[position]?.tglSurat
        val imageSurat = listSuratKeluar[position]?.imageSurat
        holder.binding.tvPerihal.text = perihal
        //holder.binding.tvDariMana.text = dariMana
        holder.binding.tvTglSurat.text = tglSurat
        Glide.with(holder.itemView)
            .load(imageSurat)
            .into(holder.binding.imageSurat)
    }
}