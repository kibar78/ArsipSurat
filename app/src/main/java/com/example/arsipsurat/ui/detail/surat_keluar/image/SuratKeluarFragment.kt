package com.example.arsipsurat.ui.detail.surat_keluar.image

import android.content.Context
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Base64
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.example.arsipsurat.R
import com.example.arsipsurat.data.SharedPreferences
import com.example.arsipsurat.data.model.SuratKeluarItem
import com.example.arsipsurat.data.model.SuratMasukItem
import com.example.arsipsurat.databinding.FragmentSuratBinding
import com.google.gson.Gson

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [SuratKeluarFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class SuratKeluarFragment : Fragment() {

    private var _binding : FragmentSuratBinding? = null
    private val binding get() = _binding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentSuratBinding.inflate(inflater, container, false)
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

        val imageArgs = suratKeluar.imageSurat ?: ""

        var image = if (Patterns.WEB_URL.matcher(imageArgs).matches()) {
            imageArgs
        }
        else {
            Base64.decode(imageArgs, Base64.DEFAULT)
        }

        binding?.ivSuratKeluar?.let {
            Glide.with(binding?.ivSuratKeluar!!)
                .load(image)
                .listener(object : RequestListener<Drawable> {
                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: Target<Drawable>,
                        isFirstResource: Boolean
                    ): Boolean {
                        return false
                    }

                    override fun onResourceReady(
                        resource: Drawable,
                        model: Any,
                        target: Target<Drawable>?,
                        dataSource: DataSource,
                        isFirstResource: Boolean
                    ): Boolean {
                        image = null
                        return false
                    }

                })
                .into(it)
        }
    }

    companion object {
        const val IMAGE_SURAT = "image_surat"
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment SuratFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            SuratKeluarFragment()
    }
}