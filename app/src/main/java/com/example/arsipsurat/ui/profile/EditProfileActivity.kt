package com.example.arsipsurat.ui.profile

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Base64
import android.util.Log
import android.util.Patterns
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresExtension
import com.bumptech.glide.Glide
import com.example.arsipsurat.R
import com.example.arsipsurat.data.SharedPreferences
import com.example.arsipsurat.data.model.user.LoginResponse
import com.example.arsipsurat.data.model.user.ParamUpdateUser
import com.example.arsipsurat.data.model.user.UpdateUserResponse
import com.example.arsipsurat.data.remote.ApiConfig
import com.example.arsipsurat.databinding.ActivityEditProfileBinding
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.ByteArrayOutputStream

class EditProfileActivity : AppCompatActivity(), View.OnClickListener {

    companion object{
        private const val IMAGE_PROFILE = 100
    }

    private lateinit var binding : ActivityEditProfileBinding

    private var base64Profile = ""
    private var userLogin : LoginResponse? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val actionBar = supportActionBar
        actionBar!!.title = "Edit Profile"
        actionBar.setDisplayHomeAsUpEnabled(true)

        binding.btnImageProfile.setOnClickListener{
            val camera = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            startActivityForResult(camera, IMAGE_PROFILE)
        }
        binding.btnSubmit.setOnClickListener(this)

        val sharedPreferences = getSharedPreferences(
            getString(R.string.shared_preferences_name_login),
            Context.MODE_PRIVATE)

        val gson = Gson()
        userLogin = gson.fromJson(sharedPreferences.getString(SharedPreferences.KEY_CURRENT_USER_LOGIN, ""), LoginResponse::class.java)

        userLogin?.let { userLogin->
            binding.let { binding->
                binding.edtUsername.setText(userLogin.username)
                binding.edtPassword.setText(userLogin.password)
                binding.edtEmail.setText(userLogin.email)
                binding.edtNoHp.setText(userLogin.noHp)
                binding.edtBidangPekerjaan.setText(userLogin.bidangPekerjaan)
                binding.edtNamaLengkap.setText(userLogin.namaLengkap)

                base64Profile = userLogin.imageProfile ?: ""
                if (base64Profile.isNotEmpty()){
                    val imageProfile = if (Patterns.WEB_URL.matcher(base64Profile).matches()){
                        base64Profile
                    }
                    else{
                        Base64.decode(base64Profile, Base64.DEFAULT)
                    }
                    Glide.with(binding.root.context)
                        .load(imageProfile)
                        .into(binding.ivProfile)
                }
            }
        }
    }

    @RequiresExtension(extension = Build.VERSION_CODES.R, version = 2)
    override fun onClick(p0: View?) {
        when(p0?.id){
            R.id.btn_submit->{
                val username = binding.edtUsername.text.toString()
                val password = binding.edtPassword.text.toString()
                val namaLengkap = binding.edtNamaLengkap.text.toString()
                val email = binding.edtEmail.text.toString()
                val noHp = binding.edtNoHp.text.toString()
                val level = userLogin?.level.toString()
                val bidangPekerjaan = binding.edtBidangPekerjaan.text.toString()

                updateProfile(ParamUpdateUser(
                    userLogin?.id ?:0,
                    namaLengkap,
                    email,
                    username,
                    password,
                    bidangPekerjaan,
                    noHp,
                    level,
                    base64Profile
                )
                )
            }
        }

    }

    private fun updateProfile(userProfile: ParamUpdateUser){
        val client = ApiConfig.getApiService().updateUser(userProfile)
        client.enqueue(object : Callback<UpdateUserResponse>{
            override fun onResponse(
                call: Call<UpdateUserResponse>,
                response: Response<UpdateUserResponse>
            ) {
                val responseBody = response.body()
                if (response.isSuccessful && responseBody != null){
                    val sharedPreferences = getSharedPreferences(
                        getString(R.string.shared_preferences_name_login),
                        Context.MODE_PRIVATE
                    )
                    val editor = sharedPreferences.edit()
                    val gson = Gson()
                    editor.putString(SharedPreferences.KEY_CURRENT_USER_LOGIN, gson.toJson(userProfile))
                    editor.apply()

                    Toast.makeText(this@EditProfileActivity,"Profile Telah diubah", Toast.LENGTH_SHORT).show()
                    finish()
                    Log.i("EditProfileActivity","onSuccess: ${response.isSuccessful}")
                }
            }

            override fun onFailure(call: Call<UpdateUserResponse>, t: Throwable) {
                Log.e("EditProfileActivity", "onFailure: ${t.message}")
            }

        })
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK){
            val bmp = data?.extras?.get("data") as? Bitmap
            binding?.ivProfile?.setImageBitmap(bmp)
            val base64String = bitmapToBase64(bmp)
            base64Profile = base64String
            Log.d("Base64", base64String)
        }
        super.onActivityResult(requestCode, resultCode, data)
    }
    fun bitmapToBase64(bitmap: Bitmap?): String {
        bitmap?.let {
            val byteArrayOutputStream = ByteArrayOutputStream()
            it.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream)
            val byteArray = byteArrayOutputStream.toByteArray()
            return Base64.encodeToString(byteArray, Base64.DEFAULT)
        }
        return ""
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}