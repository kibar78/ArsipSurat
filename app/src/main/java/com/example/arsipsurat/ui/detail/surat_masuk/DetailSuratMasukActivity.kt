package com.example.arsipsurat.ui.detail.surat_masuk

import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
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
        const val EXTRA_SURAT = "extra_masuk"

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

        val suratMasuk = if (Build.VERSION.SDK_INT >= 33){
            intent.getParcelableExtra(EXTRA_SURAT, SuratMasukItem::class.java)
        } else{
            intent.getParcelableExtra(EXTRA_SURAT)
        }

        if (suratMasuk != null){
            binding.tvTanggalPenerimaan.text = suratMasuk.tglPenerimaan
            binding.tvTglSurat.text = suratMasuk.tglSurat
            binding.tvNoSurat.text = suratMasuk.noSurat
            binding.tvDariMana.text = suratMasuk.dariMana
            binding.tvPerihal.text = suratMasuk.perihal
            binding.tvKeterangan.text = suratMasuk.keterangan

            val sectionsPagerAdapter = SectionsPagerAdapter(this)
            sectionsPagerAdapter.image = suratMasuk
            val viewPager: ViewPager2 = findViewById(R.id.view_pager)
            viewPager.adapter = sectionsPagerAdapter
            val tabs: TabLayout = findViewById(R.id.tabs)
            TabLayoutMediator(tabs, viewPager) { tab, position ->
                tab.text = resources.getString(TAB_TITLES[position])
            }.attach()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater : MenuInflater = menuInflater
        inflater.inflate(R.menu.option_menu, menu)
        return true

    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId){
            R.id.menu_edit->{
                true
            }

            else -> super.onOptionsItemSelected(item)
        }

    }
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}