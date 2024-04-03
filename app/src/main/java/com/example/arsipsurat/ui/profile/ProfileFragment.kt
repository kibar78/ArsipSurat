package com.example.arsipsurat.ui.profile

import android.content.Context
import android.content.Intent
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
import com.example.arsipsurat.data.model.user.LoginResponse
import com.example.arsipsurat.data.remote.ApiConfig
import com.example.arsipsurat.databinding.FragmentProfileBinding
import com.example.arsipsurat.ui.login.LoginActivity
import com.google.gson.Gson

class ProfileFragment : Fragment() {

    companion object {
        fun newInstance() = ProfileFragment()
        private const val GET_IMAGE_PROFILE = "/SURAT/assets/user"
    }

    private var _binding : FragmentProfileBinding? = null
    private val binding get() = _binding

    private lateinit var viewModel: ProfileViewModel

    private var userLogin : LoginResponse? = null

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

        binding?.btnEditProfile?.setOnClickListener {
            val intent = Intent(requireActivity(), EditProfileActivity::class.java)
            startActivity(intent)
        }
        binding?.btnLogout?.setOnClickListener {
            val intent = Intent(requireActivity(), LoginActivity::class.java)
            startActivity(intent)
            requireActivity().finish()
        }
    }

    override fun onResume() {
        super.onResume()

        val sharedPreferences = context?.getSharedPreferences(
            getString(R.string.shared_preferences_name_login),
            Context.MODE_PRIVATE
        )
        val gson = Gson()
        userLogin = gson.fromJson(
            sharedPreferences?.getString
                (SharedPreferences.KEY_CURRENT_USER_LOGIN, ""),
            LoginResponse::class.java
        )

        userLogin.let { userLogin->
            binding?.tvUsername?.text = userLogin?.username
            binding?.tvPassword?.text = userLogin?.password
            binding?.tvLevel?.text = userLogin?.level
            binding?.tvEmail?.text = userLogin?.email
            binding?.tvNoHp?.text = userLogin?.noHp
            binding?.tvBidangPekerjaan?.text = userLogin?.bidangPekerjaan
            binding?.tvNamaLengkap?.text = userLogin?.namaLengkap

            val imageUrl = ApiConfig.BASE_URL + GET_IMAGE_PROFILE
            val image = imageUrl + "/" + userLogin?.imageProfile
            binding?.ivProfile.let {
                Glide.with(this)
                    .load(image)
                    .into(binding?.ivProfile!!)

            }
        }
    }
}