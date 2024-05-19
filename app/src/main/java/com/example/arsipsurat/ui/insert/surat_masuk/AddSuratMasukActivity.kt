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
import com.example.arsipsurat.data.model.surat_masuk.PostSuratMasukResponse
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
        private const val IMAGE_LAMPIRAN_2_PICKCODE = 3000
        private const val IMAGE_LAMPIRAN_3_PICKCODE = 4000
    }

    private var _binding: ActivityAddSuratMasukBinding? = null
    private val binding get() = _binding

    private var selectedImageSurat: Uri? = null
    private var selectedImageLampiran: Uri? = null
    private var selectedImageLampiran_2: Uri? = null
    private var selectedImageLampiran_3: Uri? = null
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

        binding?.ivLampiran2?.setOnClickListener{
            openImageLampiran2()
        }

        binding?.ivLampiran3?.setOnClickListener{
            openImageLampiran3()
        }

        val category = arrayOf("Surat Keputusan","Surat Permohonan","Surat Kuasa",
            "Surat Pengantar","Surat Perintah","Surat Undangan","Surat Edaran")
        val adapter = ArrayAdapter(this,R.layout.dropdown_item, category)
        (binding?.textField?.editText as? AutoCompleteTextView)?.setAdapter(adapter)

        val lokasiFile = arrayOf("A1","A2","A3","A4","A5","A6","A7")
        val adapterLokasi = ArrayAdapter(this,R.layout.dropdown_item, lokasiFile)
        (binding?.textFieldLokasi?.editText as? AutoCompleteTextView)?.setAdapter(adapterLokasi)
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

    private fun openImageLampiran2(){
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, IMAGE_LAMPIRAN_2_PICKCODE)
    }

    private fun openImageLampiran3(){
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, IMAGE_LAMPIRAN_3_PICKCODE)
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
            else if (requestCode == IMAGE_LAMPIRAN_2_PICKCODE){
                selectedImageLampiran_2 = data?.data
                binding?.ivLampiran2?.setImageURI(selectedImageLampiran_2)
            }
            else if (requestCode == IMAGE_LAMPIRAN_3_PICKCODE){
                selectedImageLampiran_3 = data?.data
                binding?.ivLampiran3?.setImageURI(selectedImageLampiran_3)
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
                val noSurat = binding?.edtNoSurat?.text.toString()
                val asalSurat = binding?.edtAsalSurat?.text.toString()
                val perihal = binding?.edtPerihal?.text.toString()
                val keterangan = binding?.edtKeterangan?.text.toString()

                doUploadImage(
                    binding?.btnTglPenerimaan?.text.toString(),
                    binding?.btnTglSurat?.text.toString(),
                    binding?.edtNoSurat?.text.toString(),
                    binding?.autoCompleteTextView?.text.toString(),
                    binding?.edtAsalSurat?.text.toString(),
                    binding?.edtPerihal?.text.toString(),
                    binding?.edtKeterangan?.text.toString(),
                    binding?.autoCompleteTextViewLokasi?.text.toString()
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
                             lokasiFile: String,
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
           contentResolver.openFileDescriptor(selectedImageLampiran_2!!, "r",null)?: return
       val inputStreamLampiran2 = FileInputStream(parcelFileDescriptor2.fileDescriptor)
       val fileLampiran2 = File(cacheDir,contentResolver.getFileName(selectedImageLampiran_2!!))
       val outputStream2 = FileOutputStream(fileLampiran2)
       inputStreamLampiran.copyTo(outputStream2)

       val parcelFileDescriptor3 =
           contentResolver.openFileDescriptor(selectedImageLampiran_3!!, "r",null)?: return
       val inputStreamLampiran3 = FileInputStream(parcelFileDescriptor3.fileDescriptor)
       val fileLampiran3 = File(cacheDir,contentResolver.getFileName(selectedImageLampiran_3!!))
       val outputStream3 = FileOutputStream(fileLampiran3)
       inputStreamLampiran.copyTo(outputStream3)

       val parcelFileDescriptor4 =
           contentResolver.openFileDescriptor(selectedImageSurat!!,"r",null)?: return
            val inputStreamSurat = FileInputStream(parcelFileDescriptor4.fileDescriptor)
            val fileSurat = File(cacheDir,contentResolver.getFileName(selectedImageSurat!!))
            val outputStream4 = FileOutputStream(fileSurat)
            inputStreamSurat.copyTo(outputStream4)

        binding?.progressBar?.progress = 0
        val imageLampiran = UploadRequestBody(fileLampiran,"lampiran", this)
        val imageLampiran_2 = UploadRequestBody(fileLampiran2,"lampiran_2", this)
        val imageLampiran_3 = UploadRequestBody(fileLampiran3,"lampiran_3", this)
        val imageSurat = UploadRequestBody(fileSurat,"image_surat", this)

        val ctglPenerimaan = RequestBody.create("multipart/form-data".toMediaTypeOrNull(),tglPenerimaan)
        val ctglSurat = RequestBody.create("multipart/form-data".toMediaTypeOrNull(),tglSurat)
        val cnoSurat = RequestBody.create("multipart/form-data".toMediaTypeOrNull(),noSurat)
        val ccategory = RequestBody.create("multipart/form-data".toMediaTypeOrNull(),kategori)
        val casalSurat = RequestBody.create("multipart/form-data".toMediaTypeOrNull(),dariMana)
        val cperihal = RequestBody.create("multipart/form-data".toMediaTypeOrNull(),perihal)
        val cketerangan = RequestBody.create("multipart/form-data".toMediaTypeOrNull(),keterangan)
        val clokasiFile = RequestBody.create("multipart/form-data".toMediaTypeOrNull(),lokasiFile)
        val lampiranPart = MultipartBody.Part.createFormData("lampiran", fileLampiran.name, imageLampiran)
        val lampiranPart2 = MultipartBody.Part.createFormData("lampiran_2", fileLampiran2.name, imageLampiran_2)
        val lampiranPart3 = MultipartBody.Part.createFormData("lampiran_3", fileLampiran3.name, imageLampiran_3)
        val imageSuratPart = MultipartBody.Part.createFormData("image_surat", fileSurat.name, imageSurat)

        ApiConfig.getApiService().createSuratMasuk(
            ctglPenerimaan,
            ctglSurat,
            cnoSurat,
            ccategory,
            casalSurat,
            cperihal,
            cketerangan,
            clokasiFile,
            lampiranPart,
            lampiranPart2,
            lampiranPart3,
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
                    finish()
                }else{
                    Toast.makeText(this@AddSuratMasukActivity,"Gagal Menambahkan Surat Masuk", Toast.LENGTH_SHORT).show()
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


