package com.example.arsipsurat.ui.detail.surat_masuk.disposisi

import android.content.Context
import android.os.Bundle
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
import com.example.arsipsurat.data.model.SuratMasukItem
import com.example.arsipsurat.data.model.disposisi.AddDisposisiResponse
import com.example.arsipsurat.data.model.disposisi.ParamAddDisposisi
import com.example.arsipsurat.data.model.disposisi.ParamUpdateDisposisi
import com.example.arsipsurat.data.remote.ApiConfig
import com.example.arsipsurat.databinding.ActivityAddDisposisiBinding
import com.example.arsipsurat.ui.insert.surat_masuk.AddSuratMasukActivity
import com.example.arsipsurat.utils.DateFormat
import com.example.arsipsurat.utils.DatePickerFragment
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class AddDisposisiActivity : AppCompatActivity(),View.OnClickListener,
    DatePickerFragment.DialogDateListener{

    private var _binding : ActivityAddDisposisiBinding? = null
    private val binding get() = _binding

    companion object {
        private const val DATE_PENERIMAAN_PICKER_TAG = "DatePickerPenerimaan"
        private const val DATE_SURAT_PICKER_TAG = "DatePickerSurat"
    }
    private var suratMasuk : SuratMasukItem? = null

    private var checkBox = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityAddDisposisiBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        val actionBar = supportActionBar
        actionBar!!.title = "Add Disposisi"
        actionBar.setDisplayHomeAsUpEnabled(true)

        binding?.btnTglPenerimaan?.setOnClickListener(this)
        binding?.btnTglSurat?.setOnClickListener(this)
        binding?.btnSubmit?.setOnClickListener(this)

        val klasifikasi = arrayOf("Biasa","-")
        val adapterKlasifikasi = ArrayAdapter(this,R.layout.dropdown_item, klasifikasi)
        (binding?.textFieldKlasifikasi?.editText as? AutoCompleteTextView)?.setAdapter(adapterKlasifikasi)

        val derajat = arrayOf("Biasa","Kilat","-")
        val adapterDerajat = ArrayAdapter(this,R.layout.dropdown_item, derajat)
        (binding?.textFieldDerajat?.editText as? AutoCompleteTextView)?.setAdapter(adapterDerajat)

        val sharedPreferences = getSharedPreferences(
            getString(R.string.shared_preferences_name),
            Context.MODE_PRIVATE
        )
        val gson = Gson()
        suratMasuk = gson.fromJson(sharedPreferences.getString(SharedPreferences.KEY_CURRENT_SURAT_MASUK, ""),
            SuratMasukItem::class.java)

        suratMasuk?.let { suratMasuk ->
            binding?.let { binding ->
                binding.btnTglPenerimaan.text = DateFormat.format(suratMasuk.tglPenerimaan ?: "", "yyyy-MM-dd")
                binding.btnTglSurat.text = DateFormat.format(suratMasuk.tglSurat ?: "", "yyyy-MM-dd")
                binding.edtNoSurat.setText(suratMasuk.noSurat)
                binding.edtAsalSurat.setText(suratMasuk.dariMana)
                binding.edtPerihal.setText(suratMasuk.perihal)
            }
        }
    }

    private fun postDisposisi(disposisi: ParamAddDisposisi){
        val client = ApiConfig.getApiService().postDisposisi(disposisi)
        client.enqueue(object : Callback<AddDisposisiResponse> {
            override fun onResponse(
                call: Call<AddDisposisiResponse>,
                response: Response<AddDisposisiResponse>
            ) {
                val responseBody = response.body()
                if (response.isSuccessful && responseBody != null)
                    Toast.makeText(this@AddDisposisiActivity,"Berhasil Menambahkan Disposisi", Toast.LENGTH_SHORT).show()
                finish()
                Log.i("AddDisposisi","onSuccess: ${response.isSuccessful}")
            }

            override fun onFailure(call: Call<AddDisposisiResponse>, t: Throwable) {
                Toast.makeText(this@AddDisposisiActivity,"Gagal Menambahkan Disposisi", Toast.LENGTH_SHORT).show()
                Log.e("AddDisposisi", "onFailure: ${t.message}")
            }
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
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
                val klasifikasi = binding?.autoCompleteTextViewKlasifikasi?.text.toString()
                val derajat = binding?.autoCompleteTextViewDerajat?.text.toString()
                val tglPenerimaan = binding?.btnTglPenerimaan?.text.toString()
                val tglSurat = binding?.btnTglSurat?.text.toString()
                val nomorAgenda = binding?.edtNomorAgenda?.text.toString()
                val nomorSurat = binding?.edtNoSurat?.text.toString()
                val suratDari = binding?.edtAsalSurat?.text.toString()
                val perihal = binding?.edtPerihal?.text.toString()
                val isiDisposisi = binding?.edtIsiDisposisi?.text.toString()
                val diteruskanKepada = submitCheckBox()

                var isEmptyFields = false
                when {
                    nomorAgenda.isEmpty() -> {
                        isEmptyFields = true
                        binding?.edtNomorAgenda?.error = "Tidak Boleh Kosong"
                    }

                    nomorSurat.isEmpty() -> {
                        isEmptyFields = true
                        binding?.edtNoSurat?.error = "Tidak Boleh Kosong"
                    }

                    suratDari.isEmpty() -> {
                        isEmptyFields = true
                        binding?.edtAsalSurat?.error = "Tidak Boleh Kosong"
                    }

                    perihal.isEmpty() -> {
                        isEmptyFields = true
                        binding?.edtPerihal?.error = "Tidak Boleh Kosong"
                    }

                    isiDisposisi.isEmpty() -> {
                        isEmptyFields = true
                        binding?.edtIsiDisposisi?.error = "Tidak Boleh Kosong"
                    }
                    else->{
                        postDisposisi(disposisi = ParamAddDisposisi(
                            klasifikasi,derajat,nomorAgenda,tglPenerimaan,suratDari,nomorSurat,tglSurat,perihal,isiDisposisi,diteruskanKepada
                        ))
                    }                    }
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

    override fun onDialogDateSet(tag: String?, year: Int, month: Int, dayOfMonth: Int) {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val calendar = Calendar.getInstance()
        calendar.set(year, month, dayOfMonth)

        when(tag){
            DATE_PENERIMAAN_PICKER_TAG ->{
                binding?.btnTglPenerimaan?.text = dateFormat.format(calendar.time)
            }
            DATE_SURAT_PICKER_TAG ->{
                binding?.btnTglSurat?.text = dateFormat.format(calendar.time)
            }
        }
    }
}