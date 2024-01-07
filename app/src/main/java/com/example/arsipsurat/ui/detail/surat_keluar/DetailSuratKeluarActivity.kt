package com.example.arsipsurat.ui.detail.surat_keluar

import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.example.arsipsurat.R
import com.example.arsipsurat.data.model.SuratKeluarItem
import com.example.arsipsurat.databinding.ActivityDetailSuratKeluarBinding
import com.example.arsipsurat.ui.detail.surat_keluar.image.SectionPagerKeluarAdapter
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class DetailSuratKeluarActivity : AppCompatActivity() {

    private lateinit var binding : ActivityDetailSuratKeluarBinding

    companion object{
        const val EXTRA_SURAT = "surat_keluar"

        private val TAB_TITLES = intArrayOf(
            R.string.tab_text_1,
            R.string.tab_text_2
        )
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailSuratKeluarBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val actionBar = supportActionBar
        actionBar!!.title = "Detail Surat Keluar"
        actionBar.setDisplayHomeAsUpEnabled(true)

        val suratkeluar = if (Build.VERSION.SDK_INT >= 33){
            intent.getParcelableExtra(EXTRA_SURAT, SuratKeluarItem::class.java)
        } else{
            intent.getParcelableExtra(EXTRA_SURAT)
        }

        if (suratkeluar != null){
            binding.tvTanggalCatat.text = suratkeluar.tglCatat
            binding.tvTglSurat.text = suratkeluar.tglSurat
            binding.tvNoSurat.text = suratkeluar.noSurat
            binding.tvTujuanSurat.text = suratkeluar.dikirimKepada
            binding.tvPerihal.text = suratkeluar.perihal
            binding.tvKeterangan.text = suratkeluar.keterangan

            val sectionsPagerAdapter = SectionPagerKeluarAdapter(this)
            sectionsPagerAdapter.imageKeluar = suratkeluar
            val viewPager: ViewPager2 = findViewById(R.id.view_pager_keluar)
            viewPager.adapter = sectionsPagerAdapter
            val tabs: TabLayout = findViewById(R.id.tabs_keluar)
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