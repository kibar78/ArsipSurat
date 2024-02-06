package com.example.arsipsurat.ui.detail.surat_keluar.image

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.arsipsurat.R
import com.example.arsipsurat.data.SharedPreferences
import com.example.arsipsurat.data.model.surat_keluar.SuratKeluarItem
import com.example.arsipsurat.databinding.FragmentDetailSuratKeluarBinding
import com.google.gson.Gson

class DetailSuratKeluarFragment : Fragment() {
    private var _binding: FragmentDetailSuratKeluarBinding? = null
    private val binding get() = _binding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDetailSuratKeluarBinding.inflate(inflater,container,false)
        // Inflate the layout for this fragment
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val sharedPreferences = context?.getSharedPreferences(
            getString(R.string.shared_preferences_name_keluar),
            Context.MODE_PRIVATE
        )
        val gson = Gson()
        val suratKeluar = gson.fromJson(sharedPreferences?.getString(SharedPreferences.KEY_CURRENT_SURAT_KELUAR, ""), SuratKeluarItem::class.java)

        suratKeluar.let { suratKeluarItem ->
            binding?.tvTanggalCatat?.text = suratKeluarItem.tglCatat
            binding?.tvTglSurat?.text = suratKeluarItem.tglSurat
            binding?.tvNoSurat?.text = suratKeluarItem.noSurat
            binding?.tvKategoriSurat?.text = suratKeluarItem.kategori
            binding?.tvTujuanSurat?.text = suratKeluarItem.dikirimKepada
            binding?.tvPerihal?.text = suratKeluarItem.perihal
            binding?.tvKeterangan?.text = suratKeluarItem.keterangan
        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment DetailSuratKeluarFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance() =
            DetailSuratKeluarFragment()
    }
}