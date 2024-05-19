package com.example.arsipsurat.ui.update.surat_masuk

import android.annotation.SuppressLint
import android.app.Activity
import android.content.ContentResolver
import android.content.Context
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
import com.bumptech.glide.Glide
import com.example.arsipsurat.R
import com.example.arsipsurat.data.SharedPreferences
import com.example.arsipsurat.data.model.surat_masuk.SuratMasukItem
import com.example.arsipsurat.data.remote.ApiConfig
import com.example.arsipsurat.databinding.ActivityAddSuratMasukBinding
import com.example.arsipsurat.ui.insert.upload.UploadRequestBody
import com.example.arsipsurat.utils.DatePickerFragment
import com.google.gson.Gson
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class UpdateSuratMasukActivity : AppCompatActivity(), View.OnClickListener,
    DatePickerFragment.DialogDateListener, UploadRequestBody.UploadCallback{

    companion object{
        private const val DATE_PENERIMAAN_PICKER_TAG = "DatePickerPenerimaan"
        private const val DATE_SURAT_PICKER_TAG = "DatePickerSurat"

        private const val IMAGE_SURAT_PICKCODE = 3000
        private const val IMAGE_LAMPIRAN_PICKCODE = 4000

        const val IMAGE_SURAT = "/SURAT/assets/surat_masuk"
    }

    private var _binding: ActivityAddSuratMasukBinding? = null
    private val binding get() = _binding

    private var selectedImageSurat: Uri? = null
    private var selectedImageLampiran: Uri? = null

    private var suratMasuk : SuratMasukItem? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityAddSuratMasukBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        val actionBar = supportActionBar
        actionBar!!.title = "Ubah Surat Masuk"
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

        val lokasiFile = arrayOf("A1","A2","A3","A4","A5","A6","A7")
        val adapterLokasi = ArrayAdapter(this,R.layout.dropdown_item, lokasiFile)
        (binding?.textFieldLokasi?.editText as? AutoCompleteTextView)?.setAdapter(adapterLokasi)

        val sharedPreferences = getSharedPreferences(
            getString(R.string.shared_preferences_name),
            Context.MODE_PRIVATE
        )
        val gson = Gson()
        suratMasuk = gson.fromJson(sharedPreferences.getString(SharedPreferences.KEY_CURRENT_SURAT_MASUK, ""), SuratMasukItem::class.java)

        suratMasuk.let { suratmasuk->
            binding.let { binding ->
                binding?.btnTglPenerimaan?.setText(suratmasuk?.tglPenerimaan)
                binding?.btnTglSurat?.setText(suratmasuk?.tglSurat)
                binding?.edtNoSurat?.setText(suratmasuk?.noSurat)
                binding?.edtAsalSurat?.setText(suratmasuk?.dariMana)
                binding?.edtPerihal?.setText(suratMasuk?.perihal)
                binding?.edtKeterangan?.setText(suratMasuk?.keterangan)
                val categoryIndex = category.indexOf(suratmasuk?.kategori)
                if (categoryIndex != -1) {
                    (binding?.textField?.editText as? AutoCompleteTextView)?.setText(
                        category[categoryIndex],
                        false
                    )
                }
                val lokasiFileIndex = lokasiFile.indexOf(suratmasuk?.lokasiFile)
                if (lokasiFileIndex != -1 ){
                    (binding?.textFieldLokasi?.editText as? AutoCompleteTextView)?.setText(
                        lokasiFile[lokasiFileIndex],
                        false
                    )
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
                val category = binding?.autoCompleteTextView?.text.toString()
                val asalSurat = binding?.edtAsalSurat?.text.toString()
                val perihal = binding?.edtPerihal?.text.toString()
                val keterangan = binding?.edtKeterangan?.text.toString()
                val lokasiFile = binding?.autoCompleteTextViewLokasi?.text.toString()

                    doUpdateImage(
                        tglPenerimaan,
                        tglSurat,
                        noSurat,
                        category,
                        asalSurat,
                        perihal,
                        keterangan,
                        lokasiFile
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
                    tglPenerimaan.isEmpty()->{
                        isEmptyFields = true
                        Toast.makeText(this,"Pilih Tanggal terlebih dahulu", Toast.LENGTH_SHORT).show()
                    }
                    tglSurat.isEmpty()->{
                        isEmptyFields = true
                        Toast.makeText(this,"Pilih Tanggal terlebih dahulu", Toast.LENGTH_SHORT).show()
                    }
                    category.isEmpty()->{
                        isEmptyFields = true
                        Toast.makeText(this,"Pilih Jenis Surat", Toast.LENGTH_SHORT).show()
                    }
                    selectedImageSurat == null || selectedImageLampiran == null->{
                        isEmptyFields = true
                        Toast.makeText(this,"Pilih Gambar terlebih dahulu", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    @SuppressLint("Recycle")
    private fun doUpdateImage(tglPenerimaan: String,
                              tglSurat: String,
                              noSurat: String,
                              kategori: String,
                              dariMana: String,
                              perihal: String,
                              keterangan: String,
                              lokasiFile: String)
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
        val image = UploadRequestBody(fileSurat,"image_surat", this)

        val idSurat = suratMasuk?.id ?: 0
        Log.d("UpdateSuratMasuk","ID Surat: $idSurat")
        val id = RequestBody.create("multipart/form-data".toMediaTypeOrNull(),idSurat.toString())
        val ctglPenerimaan = RequestBody.create("multipart/form-data".toMediaTypeOrNull(),tglPenerimaan)
        val ctglSurat = RequestBody.create("multipart/form-data".toMediaTypeOrNull(),tglSurat)
        val cnoSurat = RequestBody.create("multipart/form-data".toMediaTypeOrNull(),noSurat)
        val ccategory = RequestBody.create("multipart/form-data".toMediaTypeOrNull(),kategori)
        val casalSurat = RequestBody.create("multipart/form-data".toMediaTypeOrNull(),dariMana)
        val cperihal = RequestBody.create("multipart/form-data".toMediaTypeOrNull(),perihal)
        val cketerangan = RequestBody.create("multipart/form-data".toMediaTypeOrNull(),keterangan)
        val clokasiFile = RequestBody.create("multipart/form-data".toMediaTypeOrNull(),lokasiFile)
        val lampiranPart = MultipartBody.Part.createFormData("lampiran", fileLampiran.name, imageLampiran)
        val imageSuratPart = MultipartBody.Part.createFormData("image_surat", fileSurat.name, image)

        ApiConfig.getApiService().updateSuratMasuk(
            id = id,
            ctglPenerimaan,
            ctglSurat,
            cnoSurat,
            ccategory,
            casalSurat,
            cperihal,
            cketerangan,
            clokasiFile,
            lampiranPart,
            lampiranPart,
            lampiranPart,
            imageSuratPart
        ).
        enqueue(object : Callback<SuratMasukItem> {
            override fun onResponse(
                call: Call<SuratMasukItem>,
                response: Response<SuratMasukItem>
            ) {
                val responseBody = response.body()
                if (response.isSuccessful){
                    val sharedPreferences = getSharedPreferences(
                        getString(R.string.shared_preferences_name),
                        Context.MODE_PRIVATE
                    )
                    val editor = sharedPreferences.edit()
                    val gson = Gson()
                    editor.putString(SharedPreferences.KEY_CURRENT_SURAT_MASUK, gson.toJson(responseBody))
                    editor.apply()
                    Toast.makeText(this@UpdateSuratMasukActivity,"Berhasil Ubah Surat Masuk", Toast.LENGTH_SHORT).show()
                    finish()
                    Log.i("UpdateSuratMasuk","onSuccess: ${response.isSuccessful}")
                }
                else{
                    Toast.makeText(this@UpdateSuratMasukActivity,"Gagal Ubah Surat Masuk", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<SuratMasukItem>, t: Throwable) {
                Log.e("UpdateSuratMasuk", "onFailure: ${t.message}")
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