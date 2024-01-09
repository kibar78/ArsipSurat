package com.example.arsipsurat.ui.insert.surat_masuk

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Base64
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.arsipsurat.R
import com.example.arsipsurat.data.model.PostSuratMasukResponse
import com.example.arsipsurat.data.model.SuratMasuk
import com.example.arsipsurat.data.remote.ApiConfig
import com.example.arsipsurat.databinding.ActivityAddSuratMasukBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class AddSuratMasukActivity : AppCompatActivity(), View.OnClickListener,
    DatePickerFragment.DialogDateListener{

    companion object{
        private const val DATE_PENERIMAAN_PICKER_TAG = "DatePickerPenerimaan"
        private const val DATE_SURAT_PICKER_TAG = "DatePickerSurat"

        private val IMAGE_SURAT_PICKCODE = 1000
        private val IMAGE_LAMPIRAN_PICKCODE = 2000
    }

    private var _binding: ActivityAddSuratMasukBinding? = null
    private val binding get() = _binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityAddSuratMasukBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        binding?.btnTglPenerimaan?.setOnClickListener(this)
        binding?.btnTglSurat?.setOnClickListener(this)
        binding?.btnSubmit?.setOnClickListener(this)

        binding?.btnSurat?.setOnClickListener {
            var i = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            startActivityForResult(i, 123)
        }

        binding?.btnLampiran?.setOnClickListener {
            var intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            startActivityForResult(intent, 321)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK && requestCode == IMAGE_SURAT_PICKCODE){
            val imageUri = data?.data
            binding?.ivSurat?.setImageURI(data?.data)
            imageUri?.let {
                val bitmap = getBitmapFromUri(it)
                val base64String = bitmapToBase64(bitmap)
                Log.d("Base64", base64String)
            }
        }
        if (requestCode == Activity.RESULT_OK && requestCode == IMAGE_LAMPIRAN_PICKCODE)
            binding?.ivLampiran?.setImageURI(data?.data)

        if (requestCode == 123){
            val bmp = data?.extras?.get("data") as? Bitmap
            binding?.ivSurat?.setImageBitmap(bmp)
            val base64String = bitmapToBase64(bmp)
            Log.d("Base64", base64String)

        }else if (requestCode == 321) {
            val bmp2 = data?.extras?.get("data") as? Bitmap
            binding?.ivLampiran?.setImageBitmap(bmp2)
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
    fun getBitmapFromUri(uri: Uri): Bitmap? {
        return try {
            val inputStream = contentResolver.openInputStream(uri)
            BitmapFactory.decodeStream(inputStream)
        } catch (e: IOException) {
            e.printStackTrace()
            null
        }
    }

    override fun onClick(p0: View?) {
        when(p0?.id){
            R.id.btn_tgl_penerimaan->{
                val datePickerFragment = DatePickerFragment()
                datePickerFragment.show(supportFragmentManager, DATE_PENERIMAAN_PICKER_TAG)
            }

            R.id.btn_tgl_surat->{
                val datePickerFragment = DatePickerFragment()
                datePickerFragment.show(supportFragmentManager, DATE_SURAT_PICKER_TAG)
            }
            R.id.btn_submit->{
                val tglPenerimaan = binding?.btnTglPenerimaan?.text.toString()
                val tglSurat = binding?.btnTglSurat?.text.toString()
                val noSurat = binding?.edtNoSurat?.text.toString()
                val asalSurat = binding?.edtAsalSurat?.text.toString()
                val perihal = binding?.edtPerihal?.text.toString()
                val keterangan = binding?.edtKeterangan?.text.toString()
                val suratMasuk = binding?.ivSurat.toString()
                val lampiran = binding?.ivLampiran.toString()

                postSuratMasuk(suratMasuk = SuratMasuk(
                    tglPenerimaan,tglSurat,noSurat,lampiran,asalSurat,perihal,keterangan,suratMasuk)
                )
            }
        }
    }

    private fun postSuratMasuk(suratMasuk: SuratMasuk) {
        val client = ApiConfig.getApiService().postSuratMasuk(suratMasuk)
        client.enqueue(object : Callback<PostSuratMasukResponse>{
            override fun onResponse(
                call: Call<PostSuratMasukResponse>,
                response: Response<PostSuratMasukResponse>
            ) {
                val responseBody = response.body()
                if (response.isSuccessful && responseBody != null){

                }
            }
            override fun onFailure(call: Call<PostSuratMasukResponse>, t: Throwable) {
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
            DATE_PENERIMAAN_PICKER_TAG->{
                binding?.btnTglPenerimaan?.text = dateFormat.format(calendar.time)
            }
            DATE_SURAT_PICKER_TAG->{
                binding?.btnTglSurat?.text = dateFormat.format(calendar.time)
            }
        }
    }
}