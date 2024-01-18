package com.example.arsipsurat.ui.detail.surat_keluar

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.example.arsipsurat.R
import com.example.arsipsurat.data.SharedPreferences
import com.example.arsipsurat.data.model.SuratKeluar
import com.example.arsipsurat.data.model.SuratKeluarItem
import com.example.arsipsurat.data.model.SuratMasukItem
import com.example.arsipsurat.databinding.ActivityDetailSuratKeluarBinding
import com.example.arsipsurat.ui.detail.surat_keluar.image.SectionPagerKeluarAdapter
import com.example.arsipsurat.ui.update.surat_keluar.UpdateSuratKeluarActivity
import com.example.arsipsurat.ui.update.surat_masuk.UpdateSuratMasukActivity
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.google.gson.Gson

class DetailSuratKeluarActivity : AppCompatActivity() {

    private lateinit var binding : ActivityDetailSuratKeluarBinding

    companion object{
        private val TAB_TITLES = intArrayOf(
            R.string.tab_text_1,
            R.string.tab_text_2
        )
    }
    private var suratKeluar : SuratKeluarItem? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailSuratKeluarBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val actionBar = supportActionBar
        actionBar!!.title = "Detail Surat Keluar"
        actionBar.setDisplayHomeAsUpEnabled(true)
    }

    override fun onResume() {
        super.onResume()
        val sharedPreferences = getSharedPreferences(
            getString(R.string.shared_preferences_name_keluar),
            Context.MODE_PRIVATE
        )
        val gson = Gson()
        suratKeluar = gson.fromJson(sharedPreferences.getString(
                SharedPreferences.KEY_CURRENT_SURAT_KELUAR, ""),
            SuratKeluarItem::class.java
        )

        suratKeluar?.let { suratkeluar ->
            binding.tvTanggalCatat.text = suratkeluar.tglCatat
            binding.tvTglSurat.text = suratkeluar.tglSurat
            binding.tvNoSurat.text = suratkeluar.noSurat
            binding.tvKategoriSurat.text = suratkeluar.kategori
            binding.tvTujuanSurat.text = suratkeluar.dikirimKepada
            binding.tvPerihal.text = suratkeluar.perihal
            binding.tvKeterangan.text = suratkeluar.keterangan

            val sectionsPagerAdapter = SectionPagerKeluarAdapter(this)
            sectionsPagerAdapter.imageKeluar = suratKeluar
            val viewPager: ViewPager2 = findViewById(R.id.view_pager_keluar)
            viewPager.adapter = sectionsPagerAdapter
            val tabs: TabLayout = findViewById(R.id.tabs_keluar)
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
                val intent = Intent(this, UpdateSuratKeluarActivity::class.java)
                startActivity(intent)
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