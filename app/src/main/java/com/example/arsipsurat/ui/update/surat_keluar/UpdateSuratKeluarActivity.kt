package com.example.arsipsurat.ui.update.surat_keluar

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.util.Base64
import android.util.Log
import android.util.Patterns
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.arsipsurat.R
import com.example.arsipsurat.data.SharedPreferences
import com.example.arsipsurat.data.model.ParamUpdateSuratKeluar
import com.example.arsipsurat.data.model.SuratKeluarItem
import com.example.arsipsurat.data.model.UpdateSuratKeluarResponse
import com.example.arsipsurat.data.remote.ApiConfig
import com.example.arsipsurat.databinding.ActivityAddSuratKeluarBinding
import com.example.arsipsurat.utils.DateFormat
import com.example.arsipsurat.utils.DatePickerFragment
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.ByteArrayOutputStream
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class UpdateSuratKeluarActivity: AppCompatActivity(),
    View.OnClickListener, DatePickerFragment.DialogDateListener {
    companion object{
        private const val DATE_CATAT_PICKER_TAG = "DatePickerPenerimaan"
        private const val DATE_SURAT_PICKER_TAG = "DatePickerSurat"

        private const val IMAGE_SURAT_PICKCODE = 3000
        private const val IMAGE_LAMPIRAN_PICKCODE = 4000
    }

    private var _binding: ActivityAddSuratKeluarBinding? = null
    private val binding get() = _binding
    private var base64Lampiran = ""
    private var base64Surat = ""

    private var suratKeluar : SuratKeluarItem? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityAddSuratKeluarBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        val actionBar = supportActionBar
        actionBar!!.title = "Ubah Surat keluar"
        actionBar.setDisplayHomeAsUpEnabled(true)

        binding?.btnTglCatat?.setOnClickListener(this)
        binding?.btnTglSurat?.setOnClickListener(this)
        binding?.btnSubmit?.setOnClickListener(this)

        binding?.btnSurat?.setOnClickListener {
            var i = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            startActivityForResult(i, IMAGE_SURAT_PICKCODE)
        }
        binding?.btnLampiran?.setOnClickListener {
            var intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            startActivityForResult(intent, IMAGE_LAMPIRAN_PICKCODE)
        }

        val sharedPreferences = getSharedPreferences(
            getString(R.string.shared_preferences_name_keluar),
            Context.MODE_PRIVATE
        )

        val gson = Gson()
        suratKeluar = gson.fromJson(sharedPreferences.getString(SharedPreferences.KEY_CURRENT_SURAT_KELUAR, ""), SuratKeluarItem::class.java)

        suratKeluar?.let { suratKeluar ->
            binding?.let { binding ->
                binding.btnTglCatat.text = DateFormat.format(suratKeluar.tglCatat ?: "", "yyyy-MM-dd")
                binding.btnTglSurat.text = DateFormat.format(suratKeluar.tglSurat ?: "", "yyyy-MM-dd")
                binding.edtNoSurat.setText(suratKeluar.noSurat)
                binding.edtTujuanSurat.setText(suratKeluar.dikirimKepada)
                binding.edtPerihal.setText(suratKeluar.perihal)
                binding.edtKeterangan.setText(suratKeluar.keterangan)

                base64Lampiran = suratKeluar.lampiran ?: ""
                if (base64Lampiran.isNotEmpty()) {
                    val imageLampiran = if (Patterns.WEB_URL.matcher(base64Lampiran).matches()) {
                        base64Lampiran
                    }
                    else {
                        Base64.decode(base64Lampiran, Base64.DEFAULT)
                    }

                    Glide.with(binding.root.context)
                        .load(imageLampiran)
                        .into(binding.ivLampiran)
                }

                base64Surat = suratKeluar.imageSurat ?: ""
                if (base64Surat.isNotEmpty()) {
                    val imageSurat = if (Patterns.WEB_URL.matcher(base64Surat).matches()) {
                        base64Surat
                    }
                    else {
                        Base64.decode(base64Surat, Base64.DEFAULT)
                    }

                    Glide.with(binding.root.context)
                        .load(imageSurat)
                        .into(binding.ivSurat)
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == IMAGE_SURAT_PICKCODE) {
                val bmp = data?.extras?.get("data") as? Bitmap
                binding?.ivSurat?.setImageBitmap(bmp)
                val base64String = bitmapToBase64(bmp)
                base64Surat = base64String
                Log.d("Base64", base64String)
            }
            else if (requestCode == IMAGE_LAMPIRAN_PICKCODE) {
                val bmp = data?.extras?.get("data") as? Bitmap
                binding?.ivLampiran?.setImageBitmap(bmp)
                val base64String = bitmapToBase64(bmp)
                base64Lampiran = base64String
                Log.d("Base64", base64String)
            }
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
    override fun onClick(p0: View?) {
        when(p0?.id){
            R.id.btn_tgl_catat->{
                val datePickerFragment = DatePickerFragment()
                datePickerFragment.show(supportFragmentManager,
                    DATE_CATAT_PICKER_TAG
                )
            }

            R.id.btn_tgl_surat->{
                val datePickerFragment = DatePickerFragment()
                datePickerFragment.show(supportFragmentManager,
                    DATE_SURAT_PICKER_TAG
                )
            }
            R.id.btn_submit->{
                val tglCatat = binding?.btnTglCatat?.text.toString()
                val tglSurat = binding?.btnTglSurat?.text.toString()
                val noSurat = binding?.edtNoSurat?.text.toString()
                val tujuanSurat = binding?.edtTujuanSurat?.text.toString()
                val perihal = binding?.edtPerihal?.text.toString()
                val keterangan = binding?.edtKeterangan?.text.toString()

                updateSuratKeluar(
                    ParamUpdateSuratKeluar(
                        suratKeluar?.id ?: 0,
                        tglCatat,
                        tglSurat,
                        noSurat,
                        base64Lampiran,
                        tujuanSurat,
                        perihal,
                        keterangan,
                        base64Surat
                    )
                )
            }
        }
    }

    private fun updateSuratKeluar(suratKeluar: ParamUpdateSuratKeluar) {
        val client = ApiConfig.getApiService().updateSuratKeluar(suratKeluar)
        client.enqueue(object : Callback<UpdateSuratKeluarResponse> {
            override fun onResponse(
                call: Call<UpdateSuratKeluarResponse>,
                response: Response<UpdateSuratKeluarResponse>
            ) {
                val responseBody = response.body()
                if (response.isSuccessful && responseBody != null){
                    val sharedPreferences = getSharedPreferences(
                        getString(R.string.shared_preferences_name_keluar),
                        Context.MODE_PRIVATE
                    )
                    val editor = sharedPreferences.edit()
                    val gson = Gson()
                    editor.putString(SharedPreferences.KEY_CURRENT_SURAT_KELUAR, gson.toJson(suratKeluar))
                    editor.apply()

                    Toast.makeText(this@UpdateSuratKeluarActivity,"Berhasil Mengubah Surat Keluar", Toast.LENGTH_SHORT).show()
                    finish()
                    Log.i("AddSuratKeluar","onSuccess: ${response.isSuccessful}")
                }
            }
            override fun onFailure(call: Call<UpdateSuratKeluarResponse>, t: Throwable) {
                Log.e("AddSuratKeluar", "onFailure: ${t.message}")
            }
        })
    }

    override fun onDialogDateSet(tag: String?, year: Int, month: Int, dayOfMonth: Int) {
        val calendar = Calendar.getInstance()
        calendar.set(year, month, dayOfMonth)

        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

        when(tag){
            DATE_CATAT_PICKER_TAG ->{
                binding?.btnTglCatat?.text = dateFormat.format(calendar.time)
            }
            DATE_SURAT_PICKER_TAG ->{
                binding?.btnTglSurat?.text = dateFormat.format(calendar.time)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}