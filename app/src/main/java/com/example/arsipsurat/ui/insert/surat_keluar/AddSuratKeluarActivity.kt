package com.example.arsipsurat.ui.insert.surat_keluar

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Base64
import android.util.Log
import android.view.View
import android.widget.Toast
import com.example.arsipsurat.R
import com.example.arsipsurat.data.model.PostSuratKeluarResponse
import com.example.arsipsurat.data.model.SuratKeluar
import com.example.arsipsurat.data.model.SuratMasuk
import com.example.arsipsurat.data.remote.ApiConfig
import com.example.arsipsurat.databinding.ActivityAddSuratKeluarBinding
import com.example.arsipsurat.ui.insert.surat_masuk.AddSuratMasukActivity
import com.example.arsipsurat.ui.insert.surat_masuk.DatePickerFragment
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.ByteArrayOutputStream
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class AddSuratKeluarActivity : AppCompatActivity(), View.OnClickListener,
    DatePickerFragment.DialogDateListener {
    companion object{
        private const val DATE_CATAT_PICKER_TAG = "DatePickerPenerimaan"
        private const val DATE_SURAT_PICKER_TAG = "DatePickerSurat"

        private const val IMAGE_SURAT_PICKCODE = 3000
        private const val IMAGE_LAMPIRAN_PICKCODE = 4000
    }

    private var _binding : ActivityAddSuratKeluarBinding? = null
    private val binding get() = _binding
    private var base64Lampiran = ""
    private var base64Surat = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityAddSuratKeluarBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        val actionBar = supportActionBar
        actionBar!!.title = "Tambah Surat Keluar"
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
    }

    override fun onClick(p0: View?) {
        when(p0?.id){
            R.id.btn_tgl_catat->{
                val datePickerFragment = DatePickerFragment()
                datePickerFragment.show(supportFragmentManager, DATE_CATAT_PICKER_TAG)
            }
            R.id.btn_tgl_surat->{
                val datePickerFragment = DatePickerFragment()
                datePickerFragment.show(supportFragmentManager, DATE_SURAT_PICKER_TAG)
            }
            R.id.btn_submit->{
                val tglCatat = binding?.btnTglCatat?.text.toString()
                val tglSurat = binding?.btnTglSurat?.text.toString()
                val noSurat = binding?.edtNoSurat?.text.toString()
                val tujuanSurat = binding?.edtTujuanSurat?.text.toString()
                val perihal = binding?.edtPerihal?.text.toString()
                val keterangan = binding?.edtKeterangan?.text.toString()

                postSuratKeluar(suratKeluar = SuratKeluar(
                    tglCatat,tglSurat,noSurat,base64Lampiran,tujuanSurat,perihal,keterangan,base64Surat)
                )
                Toast.makeText(this,"Berhasil Menambahkan Surat Keluar", Toast.LENGTH_SHORT).show()
                finish()
            }
        }
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

    fun postSuratKeluar(suratKeluar: SuratKeluar){
        val client = ApiConfig.getApiService().postSuratkeluar(suratKeluar)
        client.enqueue(object : Callback<PostSuratKeluarResponse>{
            override fun onResponse(
                call: Call<PostSuratKeluarResponse>,
                response: Response<PostSuratKeluarResponse>
            ) {
                val responseBody = response.body()
                if (response.isSuccessful && responseBody != null){
                    Log.i("AddSuratKeluar","onSuccess: ${response.isSuccessful}")
                }
            }

            override fun onFailure(call: Call<PostSuratKeluarResponse>, t: Throwable) {
                Log.e("AddSuratMasuk", "onFailure: ${t.message}")
            }

        })
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onDialogDateSet(tag: String?, year: Int, month: Int, dayOfMonth: Int) {
        val calendar = Calendar.getInstance()
        calendar.set(year, month, dayOfMonth)

        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

        when(tag){
            DATE_CATAT_PICKER_TAG->{
                binding?.btnTglCatat?.text = dateFormat.format(calendar.time)
            }
            DATE_SURAT_PICKER_TAG->{
                binding?.btnTglSurat?.text = dateFormat.format(calendar.time)
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}