package com.example.arsipsurat.ui.detail.surat_masuk.disposisi

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.arsipsurat.R
import com.example.arsipsurat.data.SharedPreferences
import com.example.arsipsurat.data.model.disposisi.ParamUpdateDisposisi
import com.example.arsipsurat.data.model.disposisi.UpdateDisposisiResponse
import com.example.arsipsurat.data.model.surat_masuk.SuratMasukItem
import com.example.arsipsurat.data.remote.ApiConfig
import com.example.arsipsurat.databinding.ActivityAddDisposisiBinding
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UpdateDisposisiActivity: AppCompatActivity(),View.OnClickListener  {

    private var _binding: ActivityAddDisposisiBinding? = null
    private val binding get() = _binding

    private var suratMasuk : SuratMasukItem? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityAddDisposisiBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        val actionBar = supportActionBar
        actionBar!!.title = "Update Disposisi"
        actionBar.setDisplayHomeAsUpEnabled(true)

        binding?.btnSubmit?.setOnClickListener(this)

        val klasifikasi = arrayOf("Biasa","-")
        val adapterKlasifikasi = ArrayAdapter(this, R.layout.dropdown_item, klasifikasi)
        (binding?.textFieldKlasifikasi?.editText as? AutoCompleteTextView)?.setAdapter(adapterKlasifikasi)

        val derajat = arrayOf("Biasa","Kilat","-")
        val adapterDerajat = ArrayAdapter(this, R.layout.dropdown_item, derajat)
        (binding?.textFieldDerajat?.editText as? AutoCompleteTextView)?.setAdapter(adapterDerajat)

        val sharedPreferences = getSharedPreferences(
            getString(R.string.shared_preferences_name),
            Context.MODE_PRIVATE
        )
        val gson = Gson()
        suratMasuk = gson.fromJson(sharedPreferences.getString(SharedPreferences.KEY_CURRENT_SURAT_MASUK, ""), SuratMasukItem::class.java)

        suratMasuk?.let { suratMasuk ->
            binding?.let { binding ->
                binding.edtNomorAgenda.setText(suratMasuk.nomorAgenda)
                binding.edtIsiDisposisi.setText(suratMasuk.isiDisposisi)
            }
        }

    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
    private fun updateDisposisi(disposisi: ParamUpdateDisposisi){
        val client = ApiConfig.getApiService().updateDisposisi(disposisi)
        client.enqueue(object : Callback<UpdateDisposisiResponse> {
            override fun onResponse(
                call: Call<UpdateDisposisiResponse>,
                response: Response<UpdateDisposisiResponse>
            ) {
                val responseBody = response.body()

                if (response.isSuccessful && responseBody != null){
                    val sharedPreferences = getSharedPreferences(
                        getString(R.string.shared_preferences_name),
                        Context.MODE_PRIVATE
                    )
                    val editor = sharedPreferences.edit()
                    val gson = Gson()
                    editor.putString(SharedPreferences.KEY_CURRENT_SURAT_MASUK, gson.toJson(disposisi))
                    editor.apply()
                    Toast.makeText(this@UpdateDisposisiActivity,"Berhasil Update Disposisi", Toast.LENGTH_SHORT).show()
                    finish()
                    Log.i("AddDisposisi","onSuccess: ${response.isSuccessful}")
                }
            }
            override fun onFailure(call: Call<UpdateDisposisiResponse>, t: Throwable) {
                Toast.makeText(this@UpdateDisposisiActivity,"Gagal Update Disposisi", Toast.LENGTH_SHORT).show()
                Log.e("AddDisposisi", "onFailure: ${t.message}")
            }
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onClick(p0: View?) {
        when(p0?.id){
            R.id.btn_submit->{
                val klasifikasi = binding?.autoCompleteTextViewKlasifikasi?.text.toString()
                val derajat = binding?.autoCompleteTextViewDerajat?.text.toString()
                val nomorAgenda = binding?.edtNomorAgenda?.text.toString()
                val isiDisposisi = binding?.edtIsiDisposisi?.text.toString()
                val diteruskanKepada = submitCheckBox()

                var isEmptyFields = false
                when {
                    nomorAgenda.isEmpty() -> {
                        isEmptyFields = true
                        binding?.edtNomorAgenda?.error = "Tidak Boleh Kosong"
                    }

                    isiDisposisi.isEmpty() -> {
                        isEmptyFields = true
                        binding?.edtIsiDisposisi?.error = "Tidak Boleh Kosong"
                    }
                    else->{
                        updateDisposisi(
                            ParamUpdateDisposisi(
                                suratMasuk?.id ?: 0,
                                klasifikasi,
                                derajat,
                                nomorAgenda,
                                isiDisposisi,
                                diteruskanKepada
                            )
                        )
                    }
                }
            }
        }
    }
    private fun submitCheckBox(): String{
        val checkBoxWakaStatus = if (binding?.checkBoxWaka?.isChecked == true) "Waka" else ""
        val checkBoxKanitReskrimStatus = if (binding?.checkBoxKanitReskrim?.isChecked == true) "Kanit Reskrim" else ""
        val checkBoxKanitSamaptaStatus = if (binding?.checkBoxKanitSamapta?.isChecked == true) "Kanit Samapta" else ""
        val checkBoxKanitIntelkamStatus = if (binding?.checkBoxKanitIntelkam?.isChecked == true) "Kanit Intelkam" else ""
        val checkBoxKanitBinmasStatus = if (binding?.checkBoxKanitBinmas?.isChecked == true) "Kanit Binmas" else ""
        val checkBoxKasiUmumStatus = if (binding?.checkBoxKasiUmum?.isChecked == true) "Kasi Umum" else ""
        val checkBoxKasiHukumStatus = if (binding?.checkBoxKasiHukum?.isChecked == true) "Kasi Hukum" else ""
        val checkBoxKanitLantasStatus = if (binding?.checkBoxKanitLantas?.isChecked == true) "Kanit Lantas" else ""
        val checkBoxKanitPropamStatus = if (binding?.checkBoxKanitPropam?.isChecked == true) "Kanit Propam" else ""
        val checkBoxKanitSpktIStatus = if (binding?.checkBoxKanitSpktI?.isChecked == true) "Kanit SPKT I" else ""
        val checkBoxKanitSpktIIStatus = if (binding?.checkBoxKanitSpktII?.isChecked == true) "Kanit SPKT II" else ""
        val checkBoxKanitSpktIIIStatus = if (binding?.checkBoxKanitSpktIII?.isChecked == true) "Kanit SPKT III" else ""
        val checkBoxKasiHumasStatus = if (binding?.checkBoxKasiHumas?.isChecked == true) "Kasi Humas" else ""

        val result = listOf(
            checkBoxWakaStatus, checkBoxKanitReskrimStatus, checkBoxKanitSamaptaStatus,
            checkBoxKanitIntelkamStatus, checkBoxKanitBinmasStatus, checkBoxKasiUmumStatus,
            checkBoxKasiHukumStatus, checkBoxKanitLantasStatus, checkBoxKanitPropamStatus,
            checkBoxKanitSpktIStatus, checkBoxKanitSpktIIStatus, checkBoxKanitSpktIIIStatus,
            checkBoxKasiHumasStatus
        ).filter { it.isNotEmpty() }.joinToString(", ")

        return result
    }
}