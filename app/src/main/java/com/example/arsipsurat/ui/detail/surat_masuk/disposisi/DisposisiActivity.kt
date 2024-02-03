package com.example.arsipsurat.ui.detail.surat_masuk.disposisi

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.example.arsipsurat.R
import com.example.arsipsurat.data.SharedPreferences
import com.example.arsipsurat.data.model.surat_masuk.SuratMasukItem
import com.example.arsipsurat.data.model.user.LoginResponse
import com.example.arsipsurat.databinding.ActivityDisposisiBinding
import com.google.gson.Gson

class DisposisiActivity : AppCompatActivity() {

    private var _binding : ActivityDisposisiBinding? = null
    private val binding get() = _binding

    private var suratMasuk : SuratMasukItem? = null

    private var userLogin : LoginResponse? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityDisposisiBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        val actionBar = supportActionBar
        actionBar!!.title = "Disposi Surat Masuk"
        actionBar.setDisplayHomeAsUpEnabled(true)

    }

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

        suratMasuk?.let { disposisi->
            binding?.tvKlasifikasi?.text = disposisi.klasifikasi
            binding?.tvDerajat?.text = disposisi.derajat
            binding?.tvNomorAgenda?.text = disposisi.nomorAgenda
            binding?.tvIsiDisposisi?.text = disposisi.isiDisposisi
            binding?.tvDiteruskanKepada?.text = disposisi.diteruskanKepada
        }
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
        userLogin.let { userLogin->
            if (userLogin?.level == "admin"){
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
                val intent = Intent(this, UpdateDisposisiActivity::class.java)
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
    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}