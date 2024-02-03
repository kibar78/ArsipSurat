package com.example.arsipsurat.ui.update.surat_keluar

import android.annotation.SuppressLint
import android.app.Activity
import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.provider.OpenableColumns
import android.util.Base64
import android.util.Log
import android.util.Patterns
import android.view.View
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
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
import com.example.arsipsurat.ui.insert.upload.UploadRequestBody
import com.example.arsipsurat.ui.update.surat_masuk.UpdateSuratMasukActivity
import com.example.arsipsurat.utils.DateFormat
import com.example.arsipsurat.utils.DatePickerFragment
import com.google.gson.Gson
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class UpdateSuratKeluarActivity: AppCompatActivity(),
    View.OnClickListener, DatePickerFragment.DialogDateListener, UploadRequestBody.UploadCallback {
    companion object{
        private const val DATE_CATAT_PICKER_TAG = "DatePickerPenerimaan"
        private const val DATE_SURAT_PICKER_TAG = "DatePickerSurat"

        private const val IMAGE_SURAT_PICKCODE = 3000
        private const val IMAGE_LAMPIRAN_PICKCODE = 4000

        const val IMAGE_SURAT = "/SURAT/assets/surat_keluar"
    }

    private var _binding: ActivityAddSuratKeluarBinding? = null
    private val binding get() = _binding
    private var selectedImageSurat: Uri? = null
    private var selectedImageLampiran: Uri? = null

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

        binding?.ivSurat?.setOnClickListener {
            openImageSurat()
        }

        binding?.ivLampiran?.setOnClickListener {
            openImageLampiran()
        }

        val category = arrayOf("Surat Keputusan","Surat Permohonan","Surat Kuasa",
            "Surat Pengantar","Surat Perintah","Surat Undangan","Surat Edaran")
        val adapter = ArrayAdapter(this,R.layout.dropdown_item, category)
        (binding?.textField?.editText as? AutoCompleteTextView)?.setAdapter(adapter)

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

                val imageUrlSurat = ApiConfig.BASE_URL + IMAGE_SURAT
                val imageSurat = imageUrlSurat + "/" + suratKeluar.imageSurat
                binding.ivSurat.let {
                    Glide.with(this)
                        .load(imageSurat)
                        .into(binding.ivSurat)
                }

                val imageUrlLampiran = ApiConfig.BASE_URL + IMAGE_SURAT
                val imageLampiran = imageUrlLampiran + "/" + suratKeluar.lampiran
                binding.ivLampiran.let {
                    Glide.with(this)
                        .load(imageLampiran)
                        .into(binding.ivLampiran)
                }
            }
        }
    }

    private fun openImageSurat(){
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, IMAGE_SURAT_PICKCODE)
    }
    private fun openImageLampiran(){
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, IMAGE_LAMPIRAN_PICKCODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK){
            if (requestCode == IMAGE_SURAT_PICKCODE) {
                selectedImageSurat = data?.data
                binding?.ivSurat?.setImageURI(selectedImageSurat)
            }
            else if (requestCode == IMAGE_LAMPIRAN_PICKCODE){
                selectedImageLampiran = data?.data
                binding?.ivLampiran?.setImageURI(selectedImageLampiran)
            }
        }
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
                val category = binding?.autoCompleteTextView?.text.toString()
                val tujuanSurat = binding?.edtTujuanSurat?.text.toString()
                val perihal = binding?.edtPerihal?.text.toString()
                val keterangan = binding?.edtKeterangan?.text.toString()

                doUpdateImage(tglCatat,tglSurat,noSurat,category,tujuanSurat,perihal,keterangan)
            }
        }
    }

    @SuppressLint("Recycle")
    private fun doUpdateImage(tglCatat: String,
                              tglSurat: String,
                              noSurat: String,
                              kategori: String,
                              dikirimKepada: String,
                              perihal: String,
                              keterangan: String)
    {
        if (selectedImageSurat == null || selectedImageLampiran == null) {
            Toast.makeText(applicationContext, "Pilih gambar terlebih dahulu", Toast.LENGTH_SHORT).show()
            return
        }
        val parcelFileDescriptor =
            contentResolver.openFileDescriptor(selectedImageLampiran!!, "r",null)?: return
        val inputStreamLampiran = FileInputStream(parcelFileDescriptor.fileDescriptor)
        val fileLampiran = File(cacheDir,contentResolver.getFileName(selectedImageLampiran!!))
        val outputStream = FileOutputStream(fileLampiran)
        inputStreamLampiran.copyTo(outputStream)

        val parcelFileDescriptor2 =
            contentResolver.openFileDescriptor(selectedImageSurat!!,"r",null)?: return
        val inputStreamSurat = FileInputStream(parcelFileDescriptor2.fileDescriptor)
        val fileSurat = File(cacheDir,contentResolver.getFileName(selectedImageSurat!!))
        val outputStream2 = FileOutputStream(fileSurat)
        inputStreamSurat.copyTo(outputStream2)

        binding?.progressBar?.progress = 0
        val imageLampiran = UploadRequestBody(fileLampiran,"lampiran", this)
        val imageSurat = UploadRequestBody(fileSurat,"image_surat", this)

        val idSurat = suratKeluar?.id ?: 0
        val id = RequestBody.create("multipart/form-data".toMediaTypeOrNull(),idSurat.toString())
        val ctglCatat = RequestBody.create("multipart/form-data".toMediaTypeOrNull(),tglCatat)
        val ctglSurat = RequestBody.create("multipart/form-data".toMediaTypeOrNull(),tglSurat)
        val cnoSurat = RequestBody.create("multipart/form-data".toMediaTypeOrNull(),noSurat)
        val ccategory = RequestBody.create("multipart/form-data".toMediaTypeOrNull(),kategori)
        val cdikirimKepada = RequestBody.create("multipart/form-data".toMediaTypeOrNull(),dikirimKepada)
        val cperihal = RequestBody.create("multipart/form-data".toMediaTypeOrNull(),perihal)
        val cketerangan = RequestBody.create("multipart/form-data".toMediaTypeOrNull(),keterangan)
        val lampiranPart = MultipartBody.Part.createFormData("lampiran", fileLampiran.name, imageLampiran)
        val imageSuratPart = MultipartBody.Part.createFormData("image_surat", fileSurat.name, imageSurat)

        ApiConfig.getApiService().updateSuratKeluar(
            id,
            ctglCatat,
            ctglSurat,
            cnoSurat,
            ccategory,
            cdikirimKepada,
            cperihal,
            cketerangan,
            lampiranPart,
            imageSuratPart
        ).enqueue(object : Callback<UpdateSuratKeluarResponse>{
            override fun onResponse(
                call: Call<UpdateSuratKeluarResponse>,
                response: Response<UpdateSuratKeluarResponse>
            ) {
                if (response.isSuccessful){
                    Toast.makeText(this@UpdateSuratKeluarActivity,"Berhasil Menambahkan Surat Keluar", Toast.LENGTH_SHORT).show()
                    Log.i("AddSuratKeluar","onSuccess: ${response.isSuccessful}")
                    finish()
                }
                else{
                    Toast.makeText(this@UpdateSuratKeluarActivity,"Gagal Menambahkan Surat Keluar", Toast.LENGTH_SHORT).show()
                }
            }
            override fun onFailure(call: Call<UpdateSuratKeluarResponse>, t: Throwable) {
                Log.d("UpdateSuratKeluar", "onFailure: ${t.message}")
            }

        })
    }

    private fun ContentResolver.getFileName(fileUri: Uri): String {
        var name = ""
        val returnCursor = this.query(fileUri,null,null,null,null)
        if (returnCursor != null){
            val nameIndex = returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
            returnCursor.moveToFirst()
            name = returnCursor.getString(nameIndex)
            returnCursor.close()
        }
        return name
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

    override fun onProgressUpdate(percentage: Int) {
        binding?.progressBar?.progress = percentage
    }
}