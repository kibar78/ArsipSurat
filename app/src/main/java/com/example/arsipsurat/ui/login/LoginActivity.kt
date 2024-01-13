package com.example.arsipsurat.ui.login

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.arsipsurat.data.model.LoginResponse
import com.example.arsipsurat.data.model.LoginUser
import com.example.arsipsurat.data.remote.ApiConfig
import com.example.arsipsurat.databinding.ActivityLoginBinding
import com.example.arsipsurat.ui.MainActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {

    private var _binding : ActivityLoginBinding? = null
    private val binding get() = _binding
    private var user = ""
    private var pass = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        binding?.btnLogin?.setOnClickListener {
            val username = binding?.edtUsername?.text.toString()
            val password = binding?.edtPassword?.text.toString()
            loginUser(userLogin = LoginUser(username, password))
        }
        supportActionBar?.hide()
    }

    private fun loginUser(userLogin: LoginUser){
        val client = ApiConfig.getApiService().login(userLogin)
        client.enqueue(object : Callback<LoginResponse>{
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                val responseBody = response.body()
                if (response.isSuccessful && responseBody != null){
                    startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                    Log.i("LoginActivity", "onSuccess: ${response.isSuccessful}")
                }
            }
            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                Log.e("LoginActivity","onFailure: ${t.message.toString()}")
            }
        })
    }

    private fun showLoading(isLoading: Boolean){
        binding?.pbLoading?.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}