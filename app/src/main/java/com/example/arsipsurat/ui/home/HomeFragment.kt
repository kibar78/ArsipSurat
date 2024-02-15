package com.example.arsipsurat.ui.home

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.arsipsurat.R
import com.example.arsipsurat.data.SharedPreferences
import com.example.arsipsurat.data.model.user.LoginResponse
import com.example.arsipsurat.data.remote.ApiConfig
import com.example.arsipsurat.databinding.FragmentHomeBinding
import com.google.gson.Gson

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private var userLogin : LoginResponse? = null

    private lateinit var rvPoster: RecyclerView
    private val list = ArrayList<Poster>()

    companion object{
        private const val GET_IMAGE_PROFILE = "/SURAT/assets/user"
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root
        return root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        list.addAll(getListPoster())
        showPosterList()

        val sharedPreferences = context?.getSharedPreferences(
            getString(R.string.shared_preferences_name_login),
            Context.MODE_PRIVATE
        )
        val gson = Gson()
        userLogin = gson.fromJson(
            sharedPreferences?.getString
                (SharedPreferences.KEY_CURRENT_USER_LOGIN, ""),
            LoginResponse::class.java)

        userLogin.let { userLoginItem->
            binding.let { binding->
                binding.tvNamaLengkap.setText(userLoginItem?.namaLengkap)
                binding.tvLevel.setText(userLoginItem?.level)
                val imageUrl = ApiConfig.BASE_URL + GET_IMAGE_PROFILE
                val image = imageUrl + "/" + userLoginItem?.imageProfile
                binding.ivProfile.let {
                    Glide.with(this)
                        .load(image)
                        .into(binding.ivProfile)
                }
            }
        }
    }
    @SuppressLint("Recycle")
    private fun getListPoster(): ArrayList<Poster>{
        val dataName = resources.getStringArray(R.array.data_name)
        val dataPhoto = resources.obtainTypedArray(R.array.photo)
        val listPoster = ArrayList<Poster>()
        for (i in dataName.indices){
            val poster = Poster(dataName[i],dataPhoto.getResourceId(i,-1))
            listPoster.add(poster)
        }
        return listPoster
    }

    private fun showPosterList(){
        binding.rvHome.layoutManager = LinearLayoutManager(requireActivity(), LinearLayoutManager.HORIZONTAL, false)
        val homeAdapter = HomeAdapter(list)
        binding.rvHome.adapter = homeAdapter
        binding.rvHome.setHasFixedSize(true)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}