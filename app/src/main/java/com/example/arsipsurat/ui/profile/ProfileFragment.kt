package com.example.arsipsurat.ui.profile

import android.content.Context
import android.graphics.drawable.Drawable
import androidx.lifecycle.ViewModelProvider
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
import com.example.arsipsurat.data.model.LoginResponse
import com.example.arsipsurat.data.model.SuratKeluarItem
import com.example.arsipsurat.databinding.FragmentProfileBinding
import com.google.gson.Gson

class ProfileFragment : Fragment() {

    companion object {
        fun newInstance() = ProfileFragment()
    }

    private var _binding : FragmentProfileBinding? = null
    private val binding get() = _binding

    private lateinit var viewModel: ProfileViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentProfileBinding.inflate(layoutInflater, container, false)
        return binding?.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(ProfileViewModel::class.java)
        // TODO: Use the ViewModel
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val sharedPreferences = context?.getSharedPreferences(
            getString(R.string.shared_preferences_name_login),
            Context.MODE_PRIVATE
        )
        val gson = Gson()
        val listUser = gson.fromJson(
            sharedPreferences?.getString
                (SharedPreferences.KEY_CURRENT_USER_LOGIN, ""),
            LoginResponse::class.java
        )

        listUser.let {
            binding?.tvUsername?.text = listUser.username
            binding?.tvEmail?.text = listUser.email
            binding?.tvNoHp?.text = listUser.noHp
            binding?.tvBidangPekerjaan?.text = listUser.bidangPekerjaan
            binding?.tvName?.text = listUser.namaLengkap
            Glide.with(binding?.profilImage!!)
                .load(listUser.imageProfile)
                .into(binding?.profilImage!!)
        }

    }
}