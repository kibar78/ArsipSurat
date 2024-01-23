package com.example.arsipsurat.ui.detail.surat_masuk.disposisi

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.arsipsurat.databinding.ActivityDisposisiBinding

class DisposisiActivity : AppCompatActivity() {

    private var _binding : ActivityDisposisiBinding? = null
    private val binding get() = _binding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityDisposisiBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        val actionBar = supportActionBar
        actionBar!!.title = "Disposi Surat Masuk"
        actionBar.setDisplayHomeAsUpEnabled(true)

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