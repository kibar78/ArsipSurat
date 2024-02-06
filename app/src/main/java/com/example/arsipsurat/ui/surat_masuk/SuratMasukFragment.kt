package com.example.arsipsurat.ui.surat_masuk

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.arsipsurat.R
import com.example.arsipsurat.data.SharedPreferences
import com.example.arsipsurat.data.ViewModelFactory
import com.example.arsipsurat.data.model.surat_masuk.SuratMasukItem
import com.example.arsipsurat.data.model.user.LoginResponse
import com.example.arsipsurat.data.repository.Result
import com.example.arsipsurat.databinding.FragmentSuratMasukBinding
import com.example.arsipsurat.ui.insert.surat_keluar.AddSuratKeluarActivity
import com.example.arsipsurat.ui.insert.surat_masuk.AddSuratMasukActivity
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson

class SuratMasukFragment : Fragment() {

    private var _binding: FragmentSuratMasukBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    val suratAdapter = SuratMasukAdapter()

    private var userLogin : LoginResponse? = null

    private val viewModelSuratMasuk by viewModels<SuratMasukViewModel>(){
        ViewModelFactory.getInstance(requireActivity())
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSuratMasukBinding.inflate(inflater, container, false)
        val root: View = binding.root
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModelSuratMasuk.uiStateSuratMasuk.observe(viewLifecycleOwner){uiStateSuratMasuk->
            when(uiStateSuratMasuk){
                is Result.Loading->{
                    showLoading(true)
                }
                is Result.Success->{
                    setSurat(uiStateSuratMasuk.data)
                    showLoading(false)
                }
                is Result.Error->{
                    Toast.makeText(requireActivity(), uiStateSuratMasuk.error, Toast.LENGTH_SHORT).show()
                    showLoading(false)
                }
            }
        }

        viewModelSuratMasuk.deleteDataSuccess.observe(viewLifecycleOwner) {
            it.getContentIfNotHandled()?.let {
                if (it) {
                    val mySnackbar = Snackbar.make(binding.root, "Surat masuk telah dihapus", Snackbar.LENGTH_SHORT)
                    mySnackbar.show()
                }
                else {
                    val mySnackbar = Snackbar.make(binding.root, "Surat masuk gagal dihapus", Snackbar.LENGTH_SHORT)
                    mySnackbar.show()
                }
            }
        }

        with(binding){
            searchView.setupWithSearchBar(searchBar)
            searchView
                .editText
                .setOnEditorActionListener {textView, actionId, event ->
                    searchBar.text = searchView.text
                    searchView.hide()
                    viewModelSuratMasuk.getPerihalMasuk(searchView.text.toString())
                    false
                }
        }

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
        userLogin.let {userLogin->
            if (userLogin?.level == "Pimpinan"){
                binding.fab.hide()
            }
            else{
                binding.fab.setOnClickListener{
                    val intent = Intent(requireActivity(), AddSuratMasukActivity::class.java)
                    startActivity(intent)
                }
            }
        }
    }

    override fun onResume() {
        viewModelSuratMasuk.getPerihalMasuk(SuratMasukViewModel.PERIHAL)
        super.onResume()
    }

    private fun setSurat(dataSurat: List<SuratMasukItem?>){
        binding.rvSuratMasuk.layoutManager = LinearLayoutManager(requireActivity())
        suratAdapter.listSuratMasuk = dataSurat
        binding.rvSuratMasuk.adapter = suratAdapter
        binding.rvSuratMasuk.visibility = View.VISIBLE
        binding.rvSuratMasuk.setHasFixedSize(true)

        val sharedPreferences = context?.getSharedPreferences(
            getString(R.string.shared_preferences_name_login),
            Context.MODE_PRIVATE
        )
        val gson = Gson()
        userLogin = gson.fromJson(
            sharedPreferences?.getString
                (SharedPreferences.KEY_CURRENT_USER_LOGIN, ""),
            LoginResponse::class.java)

        userLogin.let { userLogin->
            if (userLogin?.level == "Pimpinan"){
                suratAdapter.onLongClick = {
                    Toast.makeText(requireActivity(),"Tidak Bisa dihapus", Toast.LENGTH_SHORT).show()
                }
            }
            else{
                suratAdapter.onLongClick = {
                    val alertDialog = AlertDialog.Builder(requireContext()).apply {
                        setTitle("Peringatan")
                        setMessage("Apa anda yakin untuk menghapus data ini?\nSurat Masuk : ${it.noSurat}")
                        setNegativeButton("Tidak") { dialog, _ ->
                            dialog.dismiss()
                        }
                        setPositiveButton("Ya") { _, _ ->
                            binding.rvSuratMasuk.visibility = View.INVISIBLE
                            showLoading(true)
                            viewModelSuratMasuk.delete(it)
                        }
                    }
                    alertDialog.show()
                }
                suratAdapter.notifyDataSetChanged()
            }
        }
    }

    private fun showLoading(isLoading: Boolean){
        if (isLoading) {
            binding.pbLoading.visibility = View.VISIBLE
            binding.rvSuratMasuk.visibility = View.INVISIBLE
        }
        else {
            binding.pbLoading.visibility = View.GONE
            binding.rvSuratMasuk.visibility = View.VISIBLE
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}