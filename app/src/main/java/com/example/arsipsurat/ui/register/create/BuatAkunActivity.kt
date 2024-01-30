package com.example.arsipsurat.ui.register.create

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Toast
import com.example.arsipsurat.R
import com.example.arsipsurat.data.model.user.create.CreateUserResponse
import com.example.arsipsurat.data.model.user.create.ParamCreateUser
import com.example.arsipsurat.data.remote.ApiConfig
import com.example.arsipsurat.databinding.ActivityBuatAkunBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class BuatAkunActivity : AppCompatActivity() {

    private lateinit var binding: ActivityBuatAkunBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBuatAkunBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val actionBar = supportActionBar
        actionBar!!.title = "Buatkan Akun Unit Satuan"
        actionBar.setDisplayHomeAsUpEnabled(true)

        val category = arrayOf("waka","kanit reskrim","kanit samapta","kanit intelkam","kanit binmas","kasi umum",
            "kasi hukum","kanit lantas","kanit propam","kanit spkt I","kanit spkt II","kanit spkt III","kasi humas")
        val adapter = ArrayAdapter(this,R.layout.dropdown_item, category)
        (binding.textFieldLevel.editText as? AutoCompleteTextView)?.setAdapter(adapter)

        binding.btnDaftarAkun.setOnClickListener {
            val username = binding.edtUsername.text.toString()
            val password = binding.edtPassword.text.toString()
            val level = binding.autoCompleteTextViewLevel.text.toString()

            var isEmptyFields = false
            when{
                username.isEmpty()->{
                    isEmptyFields = true
                    binding.edtUsername.error = "Tidak Boleh Kosong"
                }
                password.isEmpty()->{
                    isEmptyFields = true
                    binding.edtPassword.error = "Tidak Boleh Kosong"
                }
                else->{
                    createUser(user = ParamCreateUser(
                        username,password,level
                    ))
                }
            }
        }
    }

    private fun createUser(user: ParamCreateUser){
        val client = ApiConfig.getApiService().createUser(user)
        client.enqueue(object : Callback<CreateUserResponse>{
            override fun onResponse(
                call: Call<CreateUserResponse>,
                response: Response<CreateUserResponse>
            ) {
                val responseBody = response.body()
                if (response.isSuccessful && responseBody != null){
                    Toast.makeText(this@BuatAkunActivity,"Berhasil Buat Akun", Toast.LENGTH_SHORT).show()
                    finish()
                }
                else{
                    Toast.makeText(this@BuatAkunActivity,"Gagal Buat Akun", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<CreateUserResponse>, t: Throwable) {
                Toast.makeText(this@BuatAkunActivity,"Gagal Buat Akun", Toast.LENGTH_SHORT).show()
            }

        })
    }
}