package com.example.arsipsurat.ui.detail.surat_masuk.image

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.arsipsurat.R
import com.example.arsipsurat.data.SharedPreferences
import com.example.arsipsurat.data.model.surat_masuk.SuratMasukItem
import com.example.arsipsurat.databinding.FragmentDetailSuratMasukBinding
import com.google.gson.Gson

/**
 * A simple [Fragment] subclass.
 * Use the [DetailSuratMasukFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class DetailSuratMasukFragment : Fragment() {

    private var _binding: FragmentDetailSuratMasukBinding? = null
    private val binding get() = _binding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDetailSuratMasukBinding.inflate(inflater,container,false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val sharedPreferences = context?.getSharedPreferences(
            getString(R.string.shared_preferences_name),
            Context.MODE_PRIVATE
        )
        val gson = Gson()
        val suratMasuk = gson.fromJson(sharedPreferences?.getString(SharedPreferences.KEY_CURRENT_SURAT_MASUK, ""), SuratMasukItem::class.java)

        suratMasuk.let {suratMasukItem ->
            binding?.tvTanggalPenerimaan?.text = suratMasukItem.tglPenerimaan
            binding?.tvTglSurat?.text = suratMasukItem.tglSurat
            binding?.tvNoSurat?.text = suratMasukItem.noSurat
            binding?.tvKategoriSurat?.text = suratMasukItem.kategori
            binding?.tvDariMana?.text = suratMasukItem.dariMana
            binding?.tvPerihal?.text = suratMasukItem.perihal
            binding?.tvKeterangan?.text = suratMasukItem.keterangan

            //Disposisi
            binding?.tvKlasifikasi?.text = suratMasukItem.klasifikasi
            binding?.tvDerajat?.text = suratMasukItem.derajat
            binding?.tvNomorAgenda?.text = suratMasukItem.nomorAgenda
            binding?.tvIsiDisposisi?.text = suratMasukItem.isiDisposisi
            binding?.tvDiteruskanKepada?.text = suratMasukItem.diteruskanKepada
        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment DetailSuratMasukFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance() =
            DetailSuratMasukFragment
    }
}