package com.example.arsipsurat.ui.login

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.arsipsurat.R
import com.example.arsipsurat.data.SharedPreferences
import com.example.arsipsurat.data.model.user.LoginResponse
import com.example.arsipsurat.data.model.user.LoginUser
import com.example.arsipsurat.data.remote.ApiConfig
import com.example.arsipsurat.databinding.ActivityLoginBinding
import com.example.arsipsurat.ui.MainActivity
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.security.MessageDigest
import javax.crypto.Cipher
import javax.crypto.spec.SecretKeySpec

class LoginActivity : AppCompatActivity() {

    private var _binding : ActivityLoginBinding? = null
    private val binding get() = _binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        showLoading(false)

        binding?.btnLogin?.setOnClickListener { v->

            val username = binding?.edtUsername?.text.toString()
            val password = binding?.edtPassword?.text.toString()

            var isEmptyFields = false
            if (username.isEmpty()){
                isEmptyFields = true
                binding?.edtUsername?.error = "Masukkan Username Anda"
            }
            if (password.isEmpty()){
                isEmptyFields = true
                binding?.edtPassword?.error = "Masukkan Password Anda"
            }
            if (!isEmptyFields){
                loginUser(userLogin = LoginUser(username, password))
                showLoading(true)
            }

        }
        supportActionBar?.hide()
    }

    private fun loginUser(userLogin: LoginUser){
        val client = ApiConfig.getApiService().login(userLogin)
        client.enqueue(object : Callback<LoginResponse>{
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                val responseBody = response.body()
                if (response.isSuccessful && responseBody != null){
                    if (responseBody.level == "Admin" || responseBody.level == "Pimpinan"){
                        startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                        Log.i("LoginActivity", "onSuccess: ${response.isSuccessful}")

                        val sharedPreferences = this@LoginActivity.getSharedPreferences(
                            this@LoginActivity.getString(R.string.shared_preferences_name_login),
                            Context.MODE_PRIVATE
                        )
                        val editor = sharedPreferences.edit()
                        val gson = Gson()
                        editor.putString(SharedPreferences.KEY_CURRENT_USER_LOGIN, gson.toJson(
                            responseBody))
                        editor.apply()
                        finish()
                    }
                    else{
                        showLoading(false)
                        Toast.makeText(this@LoginActivity,"Hanya Admin & Pimpinan yang dapat menggunakan aplikasi ini", Toast.LENGTH_SHORT).show()
                    }
                }
                else{
                    showLoading(false)
                    Toast.makeText(this@LoginActivity,"Username atau Password Salah", Toast.LENGTH_SHORT).show()
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