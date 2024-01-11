package com.example.arsipsurat.ui.login

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import com.example.arsipsurat.data.ViewModelFactory
import com.example.arsipsurat.data.remote.ApiConfig
import com.example.arsipsurat.data.repository.Result
import com.example.arsipsurat.databinding.ActivityLoginBinding
import com.example.arsipsurat.ui.surat_masuk.SuratMasukViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {

    private lateinit var binding : ActivityLoginBinding

    private val viewModelLogin by viewModels<LoginViewModel> {
        ViewModelFactory.getInstance(application)
    }

    companion object{

    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModelLogin.uiStatelogin.observe(this){uiStateLogin->
            when(uiStateLogin){
                is Result.Loading->{
                    showLoading(true)
                }
                is Result.Success->{
                    binding.btnLogin.setOnClickListener {
                    }
                    showLoading(false)
                }
                is Result.Error->{
                    Toast.makeText(this, uiStateLogin.error, Toast.LENGTH_SHORT).show()
                    showLoading(false)
                }
            }

        }

    }

    private fun login(){
        binding.edtUsername.text.toString()
        binding.edtPassword.text.toString()
    }
    private fun showLoading(isLoading: Boolean){
        binding.pbLoading.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
}