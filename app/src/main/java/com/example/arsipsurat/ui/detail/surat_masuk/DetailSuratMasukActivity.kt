package com.example.arsipsurat.ui.detail.surat_masuk

import android.os.Build
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.arsipsurat.data.model.SuratMasukItem
import com.example.arsipsurat.databinding.ActivityDetailSuratMasukBinding

class DetailSuratMasukActivity : AppCompatActivity() {

    private lateinit var binding : ActivityDetailSuratMasukBinding

    private val detailSuratMasukViewModel by viewModels<DetailSuratMasukViewModel>()

    companion object{
        const val EXTRA_SURAT = "extra_surat"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailSuratMasukBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val surat = if (Build.VERSION.SDK_INT >= 33){
            intent.getParcelableExtra<SuratMasukItem>(EXTRA_SURAT, SuratMasukItem::class.java)
        } else{
            intent.getParcelableExtra<SuratMasukItem>(EXTRA_SURAT)
        }

        if (surat != null){
            binding.tvTanggalPenerimaan.text = surat.tglPenerimaan
            binding.tvTglSurat.text = surat.tglSurat
            binding.tvNoSurat.text = surat.noSurat
            binding.tvDariMana.text = surat.dariMana
            binding.tvPerihal.text = surat.perihal
            binding.tvKeterangan.text = surat.keterangan

            Glide.with(binding.ivSuratMasuk)
                .load(surat.imageSurat)
                .into(binding.ivSuratMasuk)
            Glide.with(binding.ivLampiran)
                .load(surat.lampiran)
                .into(binding.ivLampiran)
        }




            //detailSuratMasukViewModel.getDetailSuratMasuk(perihal.toString())

//            detailSuratMasukViewModel.detailSuratMasuk.observe(this){detailSurat->
//                binding.tvTglPenerimaan.text = detailSurat.tglPenerimaan
//                binding.tvTglSurat.text = detailSurat.tglSurat
//                binding.tvNoSurat.text = detailSurat.noSurat
//                binding.tvDariMana.text = detailSurat.dariMana
//                binding.tvPerihal.text = detailSurat.perihal
//                binding.tvKeterangan.text = detailSurat.keterangan
//
//                detailSuratMasukViewModel.isLoading.observe(this){isLoading->
//                    binding.pbLoading.visibility = if (isLoading) View.VISIBLE else View.GONE
//                }
//            }
        }
    }