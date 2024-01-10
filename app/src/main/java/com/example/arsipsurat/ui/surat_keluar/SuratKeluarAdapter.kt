package com.example.arsipsurat.ui.surat_keluar

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.arsipsurat.data.model.SuratKeluarItem
import com.example.arsipsurat.databinding.ItemSuratBinding
import com.example.arsipsurat.ui.detail.surat_keluar.DetailSuratKeluarActivity

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
        val keterangan  = listSuratKeluar[position]?.keterangan

        holder.binding.tvTanggalSurat.text = tglSurat
        holder.binding.tvPerihal.text = perihal
        holder.binding.tvKeterangan.text = keterangan

        holder.itemView.setOnClickListener { v->
            val intent = Intent(v.context, DetailSuratKeluarActivity::class.java)
            intent.putExtra(DetailSuratKeluarActivity.EXTRA_SURAT, listSuratKeluar[position])
            v.context.startActivity(intent)
        }
        holder.itemView.setOnLongClickListener {
            onLongClick(listSuratKeluar[position]!!)
            return@setOnLongClickListener true
        }
    }
}