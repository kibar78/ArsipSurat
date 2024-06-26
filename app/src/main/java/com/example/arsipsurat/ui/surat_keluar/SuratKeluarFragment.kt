package com.example.arsipsurat.ui.surat_keluar

import android.annotation.SuppressLint
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
import com.example.arsipsurat.data.model.surat_keluar.SuratKeluarItem
import com.example.arsipsurat.data.model.user.LoginResponse
import com.example.arsipsurat.data.repository.Result
import com.example.arsipsurat.databinding.FragmentSuratKeluarBinding
import com.example.arsipsurat.ui.insert.surat_keluar.AddSuratKeluarActivity
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson

class SuratKeluarFragment : Fragment() {

    private var _binding: FragmentSuratKeluarBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    val suratkeluarAdapter = SuratKeluarAdapter()

    private var userLogin : LoginResponse? = null

    private val viewModelSuratKeluar by viewModels<SuratKeluarViewModel>(){
        ViewModelFactory.getInstance(requireActivity())
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSuratKeluarBinding.inflate(inflater, container, false)
        val root: View = binding.root
        return root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModelSuratKeluar.uiStateKeluar.observe(viewLifecycleOwner){uiStateSuratKeluar->
            when(uiStateSuratKeluar){
                is Result.Loading->{
                    showLoading(true)
                }
                is Result.Success->{
                    setSurat(uiStateSuratKeluar.data)
                    showLoading(false)
                }
                is Result.Error->{
                    Toast.makeText(requireActivity(), uiStateSuratKeluar.error, Toast.LENGTH_SHORT).show()
                    showLoading(false)
                }
            }
        }

        viewModelSuratKeluar.deleteDataSuccess.observe(viewLifecycleOwner){
            it.getContentIfNotHandled()?.let {
                if (it) {
                    val mySnackbar = Snackbar.make(binding.root, "Surat keluar telah dihapus", Snackbar.LENGTH_SHORT)
                    mySnackbar.show()
                }
                else {
                    val mySnackbar = Snackbar.make(binding.root, "Surat keluar gagal dihapus", Snackbar.LENGTH_SHORT)
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
                    viewModelSuratKeluar.getPerihalKeluar(searchView.text.toString())
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
                    val intent = Intent(requireActivity(), AddSuratKeluarActivity::class.java)
                    startActivity(intent)
            }
        }

        }
        viewModelSuratKeluar.getPerihalKeluar(SuratKeluarViewModel.PERIHAL)
    }

    override fun onResume() {
        viewModelSuratKeluar.getPerihalKeluar(SuratKeluarViewModel.PERIHAL)
        super.onResume()
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun setSurat(dataSurat: List<SuratKeluarItem?>) {
        binding.rvSuratKeluar.layoutManager = LinearLayoutManager(requireActivity())
        suratkeluarAdapter.listSuratKeluar = dataSurat
        binding.rvSuratKeluar.adapter = suratkeluarAdapter
        binding.rvSuratKeluar.visibility = View.VISIBLE
        binding.rvSuratKeluar.setHasFixedSize(true)

        val sharedPreferences = context?.getSharedPreferences(
            getString(R.string.shared_preferences_name_login),
            Context.MODE_PRIVATE
        )
        val gson = Gson()
        userLogin = gson.fromJson(
            sharedPreferences?.getString
                (SharedPreferences.KEY_CURRENT_USER_LOGIN, ""),
            LoginResponse::class.java)

        userLogin.let {userLogin->
            if (userLogin?.level == "Pimpinan"){
                suratkeluarAdapter.onLongClick = {
                    Toast.makeText(requireActivity(),"Tidak Bisa dihapus", Toast.LENGTH_SHORT).show()
                }
            }
            else{
                suratkeluarAdapter.onLongClick = {
                    val alertDialog = AlertDialog.Builder(requireContext()).apply {
                        setTitle("Peringatan")
                        setMessage("Apa anda yakin untuk menghapus data ini?\nSurat Keluar : ${it.noSurat}")
                        setNegativeButton("Tidak") { dialog, _ ->
                            dialog.dismiss()
                        }
                        setPositiveButton("Ya") { _, _ ->
                            binding.rvSuratKeluar.visibility = View.INVISIBLE
                            showLoading(true)
                            viewModelSuratKeluar.delete(it)
                        }
                    }
                    alertDialog.show()
                }
                suratkeluarAdapter.notifyDataSetChanged()
            }
        }

    }
    private fun showLoading(isLoading: Boolean){
        if (isLoading) {
            binding.pbLoadingSuratKeluar.visibility = View.VISIBLE
            binding.rvSuratKeluar.visibility = View.INVISIBLE
        }
        else {
            binding.pbLoadingSuratKeluar.visibility = View.GONE
            binding.rvSuratKeluar.visibility = View.VISIBLE
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}