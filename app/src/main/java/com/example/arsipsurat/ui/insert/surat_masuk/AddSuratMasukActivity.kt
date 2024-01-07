package com.example.arsipsurat.ui.insert.surat_masuk

import android.nfc.Tag
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.arsipsurat.R
import com.example.arsipsurat.databinding.ActivityAddSuratMasukBinding
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class AddSuratMasukActivity : AppCompatActivity(), View.OnClickListener,
    DatePickerFragment.DialogDateListener{

    companion object{
        private const val DATE_PENERIMAAN_PICKER_TAG = "DatePickerPenerimaan"
        private const val DATE_SURAT_PICKER_TAG = "DatePickerSurat"
    }

    private var _binding: ActivityAddSuratMasukBinding? = null
    private val binding get() = _binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityAddSuratMasukBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        binding?.btnTglPenerimaan?.setOnClickListener(this)
        binding?.btnTglSurat?.setOnClickListener(this)
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
        }
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