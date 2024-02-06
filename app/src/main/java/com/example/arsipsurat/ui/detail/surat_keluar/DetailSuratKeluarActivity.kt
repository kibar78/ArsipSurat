package com.example.arsipsurat.ui.detail.surat_keluar

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.example.arsipsurat.R
import com.example.arsipsurat.data.SharedPreferences
import com.example.arsipsurat.data.model.surat_keluar.SuratKeluarItem
import com.example.arsipsurat.data.model.user.LoginResponse
import com.example.arsipsurat.databinding.ActivityDetailSuratKeluarBinding
import com.example.arsipsurat.ui.detail.surat_keluar.image.SectionPagerKeluarAdapter
import com.example.arsipsurat.ui.update.surat_keluar.UpdateSuratKeluarActivity
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.google.gson.Gson

class DetailSuratKeluarActivity : AppCompatActivity() {

    private lateinit var binding : ActivityDetailSuratKeluarBinding

    companion object{
        private val TAB_TITLES = intArrayOf(
            R.string.tab_text_1,
            R.string.tab_text_2,
            R.string.tab_text_3
        )
    }
    private var suratKeluar : SuratKeluarItem? = null

    private var userLogin : LoginResponse? = null
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

            val sectionsPagerAdapter = SectionPagerKeluarAdapter(this)
            sectionsPagerAdapter.imageKeluar = suratKeluar
            val viewPager: ViewPager2 = binding.viewPagerKeluar
            viewPager.adapter = sectionsPagerAdapter
            val tabs: TabLayout = binding.tabsKeluar
            TabLayoutMediator(tabs, viewPager) { tab, position ->
                tab.text = resources.getString(TAB_TITLES[position])
            }.attach()

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val sharedPreferences = getSharedPreferences(
            getString(R.string.shared_preferences_name_login),
            Context.MODE_PRIVATE
        )
        val gson = Gson()
        userLogin = gson.fromJson(
            sharedPreferences?.getString
                (SharedPreferences.KEY_CURRENT_USER_LOGIN, ""),
            LoginResponse::class.java
        )
        userLogin.let {userLogin->
            if (userLogin?.level == "Pimpinan"){
                return false
            }
            else{
                val inflater : MenuInflater = menuInflater
                inflater.inflate(R.menu.option_menu, menu)
            }
        }
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