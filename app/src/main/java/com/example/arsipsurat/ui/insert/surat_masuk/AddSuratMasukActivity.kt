package com.example.arsipsurat.ui.insert.surat_masuk

import android.annotation.SuppressLint
import android.app.Activity
import android.content.ContentResolver
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.OpenableColumns
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.arsipsurat.R
import com.example.arsipsurat.data.model.PostSuratMasukResponse
import com.example.arsipsurat.data.model.SuratMasuk
import com.example.arsipsurat.data.remote.ApiConfig
import com.example.arsipsurat.databinding.ActivityAddSuratMasukBinding
import com.example.arsipsurat.ui.insert.upload.UploadRequestBody
import com.example.arsipsurat.utils.DatePickerFragment
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class AddSuratMasukActivity : AppCompatActivity(), View.OnClickListener,
    DatePickerFragment.DialogDateListener, UploadRequestBody.UploadCallback{

    companion object{
        private const val DATE_PENERIMAAN_PICKER_TAG = "DatePickerPenerimaan"
        private const val DATE_SURAT_PICKER_TAG = "DatePickerSurat"

        private const val IMAGE_SURAT_PICKCODE = 1000
        private const val IMAGE_LAMPIRAN_PICKCODE = 2000
    }

    private var _binding: ActivityAddSuratMasukBinding? = null
    private val binding get() = _binding

    private var selectedImageSurat: Uri? = null
    private var selectedImageLampiran: Uri? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityAddSuratMasukBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        val actionBar = supportActionBar
        actionBar!!.title = "Tambah Surat Masuk"
        actionBar.setDisplayHomeAsUpEnabled(true)


        binding?.btnTglPenerimaan?.setOnClickListener(this)
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
                val category = binding?.autoCompleteTextView?.text.toString()

                doUploadImage(
                    binding?.btnTglPenerimaan?.text.toString(),
                    binding?.btnTglSurat?.text.toString(),
                    binding?.edtNoSurat?.text.toString(),
                    binding?.autoCompleteTextView?.text.toString(),
                    binding?.edtAsalSurat?.text.toString(),
                    binding?.edtPerihal?.text.toString(),
                    binding?.edtKeterangan?.text.toString(),
                )

                var isEmptyFields = false
                when {
                    noSurat.isEmpty() -> {
                        isEmptyFields = true
                        binding?.edtNoSurat?.error = "Tidak Boleh Kosong"
                    }

                    asalSurat.isEmpty() -> {
                        isEmptyFields = true
                        binding?.edtAsalSurat?.error = "Tidak Boleh Kosong"
                    }

                    perihal.isEmpty() -> {
                        isEmptyFields = true
                        binding?.edtPerihal?.error = "Tidak Boleh Kosong"
                    }

                    keterangan.isEmpty() -> {
                        isEmptyFields = true
                        binding?.edtKeterangan?.error = "Tidak Boleh Kosong"
                    }
                    else->{
                    }
                }
            }
        }
    }

   @SuppressLint("Recycle")
   private fun doUploadImage(tglPenerimaan: String,
                             tglSurat: String,
                             noSurat: String,
                             kategori: String,
                             dariMana: String,
                             perihal: String,
                             keterangan: String,
                              ){

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
        val imageLampiran = UploadRequestBody(fileLampiran,"image", this)
        val imageSurat = UploadRequestBody(fileSurat,"image", this)

        val ctglPenerimaan = RequestBody.create("multipart/form-data".toMediaTypeOrNull(),tglPenerimaan)
        val ctglSurat = RequestBody.create("multipart/form-data".toMediaTypeOrNull(),tglSurat)
        val cnoSurat = RequestBody.create("multipart/form-data".toMediaTypeOrNull(),noSurat)
        val ccategory = RequestBody.create("multipart/form-data".toMediaTypeOrNull(),kategori)
        val casalSurat = RequestBody.create("multipart/form-data".toMediaTypeOrNull(),dariMana)
        val cperihal = RequestBody.create("multipart/form-data".toMediaTypeOrNull(),perihal)
        val cketerangan = RequestBody.create("multipart/form-data".toMediaTypeOrNull(),keterangan)
        val lampiranPart = MultipartBody.Part.createFormData("lampiran", fileLampiran.name, imageLampiran)
        val imageSuratPart = MultipartBody.Part.createFormData("imageSurat", fileSurat.name, imageSurat)

        ApiConfig.getApiService().createSuratMasuk(
            ctglPenerimaan,
            ctglSurat,
            cnoSurat,
            ccategory,
            casalSurat,
            cperihal,
            cketerangan,
            lampiranPart,
            imageSuratPart
        ).
        enqueue(object :Callback<PostSuratMasukResponse>{
            override fun onResponse(
                call: Call<PostSuratMasukResponse>,
                response: Response<PostSuratMasukResponse>
            ) {
                if (response.isSuccessful){
                    Toast.makeText(this@AddSuratMasukActivity,"Berhasil Menambahkan Surat Masuk", Toast.LENGTH_SHORT).show()
                    Log.i("AddSuratMasuk","onSuccess: ${response.isSuccessful}")
                }
            }

            override fun onFailure(call: Call<PostSuratMasukResponse>, t: Throwable) {
                Log.e("AddSuratMasuk", "onFailure: ${t.message}")
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

    private fun postSuratMasuk(suratMasuk: SuratMasuk) {
        val client = ApiConfig.getApiService().postSuratMasuk(suratMasuk)
        client.enqueue(object : Callback<PostSuratMasukResponse>{
            override fun onResponse(
                call: Call<PostSuratMasukResponse>,
                response: Response<PostSuratMasukResponse>
            ) {
                val responseBody = response.body()
                if (response.isSuccessful && responseBody != null){
                    Toast.makeText(this@AddSuratMasukActivity,"Berhasil Menambahkan Surat Masuk", Toast.LENGTH_SHORT).show()
                    finish()
                    Log.i("AddSuratMasuk","onSuccess: ${response.isSuccessful}")
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
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun onProgressUpdate(percentage: Int) {
        binding?.progressBar?.progress = percentage
    }
}


