package com.example.arsipsurat.ui.surat_masuk

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.arsipsurat.data.model.SuratMasukItem
import com.example.arsipsurat.databinding.ItemSuratBinding
import com.example.arsipsurat.ui.detail.surat_masuk.DetailSuratMasukActivity

class SuratMasukAdapter(private val listSuratMasuk: List<SuratMasukItem?>,):
    RecyclerView.Adapter<SuratMasukAdapter.ViewHolder>(){

    class ViewHolder(var binding: ItemSuratBinding): RecyclerView.ViewHolder(binding.root)

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

        holder.itemView.setOnClickListener {v->
            val intent = Intent(v.context, DetailSuratMasukActivity::class.java)
            intent.putExtra(DetailSuratMasukActivity.EXTRA_SURAT, listSuratMasuk[position])
            v.context.startActivity(intent)
        }
    }
}