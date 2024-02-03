package com.example.arsipsurat.ui.detail.surat_keluar.image

import android.content.Context
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Base64
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.example.arsipsurat.R
import com.example.arsipsurat.data.SharedPreferences
import com.example.arsipsurat.data.model.SuratKeluarItem
import com.example.arsipsurat.data.remote.ApiConfig
import com.example.arsipsurat.databinding.FragmentLampiran2Binding
import com.example.arsipsurat.ui.detail.surat_masuk.image.LampiranFragment
import com.google.gson.Gson

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
/**
 * A simple [Fragment] subclass.
 * Use the [LampiranKeluarFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class LampiranKeluarFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private var _binding : FragmentLampiran2Binding? = null
    private val binding get() = _binding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentLampiran2Binding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val sharedPreferences = context?.getSharedPreferences(
            getString(R.string.shared_preferences_name_keluar),
            Context.MODE_PRIVATE
        )
        val gson = Gson()
        val suratKeluar = gson.fromJson(sharedPreferences?.getString(SharedPreferences.KEY_CURRENT_SURAT_KELUAR, ""), SuratKeluarItem::class.java)

        val imageUrl = ApiConfig.BASE_URL + IMAGE_LAMPIRAN
        val image = imageUrl + "/" + suratKeluar.lampiran
        binding?.ivLampiran.let {
            Glide.with(requireActivity())
                .load(image)
                .into(binding?.ivLampiran!!)
        }

    }

    companion object {
        const val IMAGE_LAMPIRAN = "/SURAT/assets/surat_keluar"
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment LampiranFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            LampiranKeluarFragment()
            }
    }
