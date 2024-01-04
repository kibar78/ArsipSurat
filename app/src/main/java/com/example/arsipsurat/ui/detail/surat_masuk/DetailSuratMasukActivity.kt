package com.example.arsipsurat.ui.detail.surat_masuk

import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.example.arsipsurat.R
import com.example.arsipsurat.data.model.SuratMasukItem
import com.example.arsipsurat.databinding.ActivityDetailSuratMasukBinding
import com.example.arsipsurat.ui.detail.surat_masuk.image.SectionsPagerAdapter
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class DetailSuratMasukActivity : AppCompatActivity() {

    private lateinit var binding : ActivityDetailSuratMasukBinding

    companion object{
        const val EXTRA_SURAT = "extra_surat"

        private val TAB_TITLES = intArrayOf(
            R.string.tab_text_1,
            R.string.tab_text_2
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailSuratMasukBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val actionBar = supportActionBar
        actionBar!!.title = "Detail Surat Masuk"
        actionBar.setDisplayHomeAsUpEnabled(true)

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

            val sectionsPagerAdapter = SectionsPagerAdapter(this)
            sectionsPagerAdapter.image = surat
            val viewPager: ViewPager2 = findViewById(R.id.view_pager)
            viewPager.adapter = sectionsPagerAdapter
            val tabs: TabLayout = findViewById(R.id.tabs)
            TabLayoutMediator(tabs, viewPager) { tab, position ->
                tab.text = resources.getString(TAB_TITLES[position])
            }.attach()
        }
    }
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}