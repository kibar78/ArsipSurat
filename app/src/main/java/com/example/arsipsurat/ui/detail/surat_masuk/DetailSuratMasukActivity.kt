package com.example.arsipsurat.ui.detail.surat_masuk

import android.annotation.SuppressLint
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
import com.example.arsipsurat.data.model.surat_masuk.SuratMasukItem
import com.example.arsipsurat.data.model.user.LoginResponse
import com.example.arsipsurat.databinding.ActivityDetailSuratMasukBinding
import com.example.arsipsurat.ui.detail.surat_masuk.image.SectionsPagerAdapter
import com.example.arsipsurat.ui.update.surat_masuk.UpdateSuratMasukActivity
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.google.gson.Gson

class DetailSuratMasukActivity : AppCompatActivity() {

    private lateinit var binding : ActivityDetailSuratMasukBinding

    companion object{
        private val TAB_TITLES = intArrayOf(
            R.string.tab_text_1,
            R.string.tab_text_2,
            R.string.tab_text_3,
        )

        private val TAB_ICON = intArrayOf(
            R.drawable.ic_description_24,
            R.drawable.ic_surat_masuk_black,
            R.drawable.ic_attachment_24
        )
    }

    private var suratMasuk : SuratMasukItem? = null

    private var userLogin : LoginResponse? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailSuratMasukBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val actionBar = supportActionBar
        actionBar!!.title = "Detail Surat Masuk"
        actionBar.setDisplayHomeAsUpEnabled(true)

    }

    @SuppressLint("UseCompatLoadingForDrawables")
    override fun onResume() {
        super.onResume()
        val sharedPreferences = getSharedPreferences(
            getString(R.string.shared_preferences_name),
            Context.MODE_PRIVATE
        )
        val gson = Gson()

        suratMasuk = gson.fromJson(sharedPreferences.getString(
            SharedPreferences.KEY_CURRENT_SURAT_MASUK, ""),
            SuratMasukItem::class.java)

            val sectionsPagerAdapter = SectionsPagerAdapter(this)
            sectionsPagerAdapter.image = suratMasuk
            val viewPager: ViewPager2 = binding.viewPager
            viewPager.adapter = sectionsPagerAdapter
            val tabs: TabLayout = binding.tabs
            TabLayoutMediator(tabs, viewPager) { tab, position ->
                tab.text = resources.getString(TAB_TITLES[position])
                tab.icon = resources.getDrawable(TAB_ICON[position])
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
            LoginResponse::class.java)

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
                val intent = Intent(this, UpdateSuratMasukActivity::class.java)
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