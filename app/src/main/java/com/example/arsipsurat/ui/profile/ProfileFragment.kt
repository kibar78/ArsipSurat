package com.example.arsipsurat.ui.profile

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.arsipsurat.R
import com.example.arsipsurat.data.model.LoginResponse
import com.example.arsipsurat.databinding.FragmentProfileBinding

class ProfileFragment : Fragment() {

    companion object {
        fun newInstance() = ProfileFragment()
    }

    private var _binding : FragmentProfileBinding? = null
    private val binding get() = _binding

    private lateinit var viewModel: ProfileViewModel

    private var listUser : LoginResponse? = null
    

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

        val username = listUser?.username
        val namaLengkap = listUser?.namaLengkap
        val noHp = listUser?.noHp
        val email = listUser?.email
        val bidangPekerjaan = listUser?.bidangPekerjaan
        val image = listUser?.imageProfile

        binding?.tvName?.text = namaLengkap
        binding?.tvUsername?.text = username
        binding?.tvNoHp?.text = noHp
    }

}