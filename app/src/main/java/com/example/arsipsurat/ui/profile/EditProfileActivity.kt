package com.example.arsipsurat.ui.profile

import android.annotation.SuppressLint
import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.provider.OpenableColumns
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresExtension
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import com.bumptech.glide.Glide
import com.example.arsipsurat.R
import com.example.arsipsurat.data.SharedPreferences
import com.example.arsipsurat.data.model.user.LoginResponse
import com.example.arsipsurat.data.remote.ApiConfig
import com.example.arsipsurat.databinding.ActivityEditProfileBinding
import com.example.arsipsurat.ui.insert.upload.UploadRequestBody
import com.google.gson.Gson
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Date

class EditProfileActivity : AppCompatActivity(), View.OnClickListener, UploadRequestBody.UploadCallback {

    companion object{

        private const val GET_IMAGE_PROFILE = "/SURAT/assets/user"
    }

    private lateinit var binding : ActivityEditProfileBinding

    private lateinit var photoFile: File

    private var takePhotoUri : Uri? = null

    private var userLogin : LoginResponse? = null

    private lateinit var currentPhotoPath: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val actionBar = supportActionBar
        actionBar!!.title = "Edit Profile"
        actionBar.setDisplayHomeAsUpEnabled(true)

        binding.btnImageProfile.setOnClickListener{
            takePictureCamera()
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

                val imageUrl = ApiConfig.BASE_URL + GET_IMAGE_PROFILE
                val image = imageUrl + "/" + userLogin.imageProfile
                binding.ivProfile.let {
                    Glide.with(this)
                        .load(image)
                        .into(binding.ivProfile)

                }
            }
        }
    }

    @SuppressLint("Recycle")
    private fun doTakePhoto(){
        if(takePhotoUri == null){
            Toast.makeText(applicationContext,"Take Photo",Toast.LENGTH_SHORT).show()
            return
        }
        val parcelFileDescriptor =
            contentResolver.openFileDescriptor(takePhotoUri!!, "r",null)?: return
        val inputStreamLampiran = FileInputStream(parcelFileDescriptor.fileDescriptor)
        val fileLampiran = File(cacheDir,contentResolver.getFileName(takePhotoUri!!))
        val outputStream = FileOutputStream(fileLampiran)
        inputStreamLampiran.copyTo(outputStream)
    }

    val REQUEST_TAKE_PHOTO = 1
    private fun takePictureCamera() {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            // Ensure that there's a camera activity to handle the intent
            takePictureIntent.resolveActivity(packageManager)?.also {
                // Create the File where the photo should go
                photoFile = createImageFile()
                // Continue only if the File was successfully created
                photoFile?.also {
                    val photoURI: Uri = FileProvider.getUriForFile(
                        applicationContext,
                        "com.example.android.fileprovider",
                        it
                    )
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                    startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO)
                }
            }
        }
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

                doTakePhoto()
            }
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_TAKE_PHOTO && resultCode == RESULT_OK){
            val uri = FileProvider.getUriForFile(applicationContext,"com.example.android.fileprovider"
                ,photoFile)
            takePhotoUri = uri
            binding.ivProfile.setImageURI(takePhotoUri)
        }
    }

    @Throws(IOException::class)
    private fun createImageFile(): File {
        // Create an image file name
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val storageDir: File = getExternalFilesDir(Environment.DIRECTORY_PICTURES)!!
        return File.createTempFile(
            "JPEG_${timeStamp}_", /* prefix */
            ".jpg", /* suffix */
            storageDir /* directory */
        ).apply {
            // Save a file: path for use with ACTION_VIEW intents
            currentPhotoPath = absolutePath
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun onProgressUpdate(percentage: Int) {

    }
}