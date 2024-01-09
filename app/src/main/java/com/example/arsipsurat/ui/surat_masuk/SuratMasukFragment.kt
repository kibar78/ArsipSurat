package com.example.arsipsurat.ui.surat_masuk

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.arsipsurat.data.ViewModelFactory
import com.example.arsipsurat.data.model.SuratMasukItem
import com.example.arsipsurat.data.repository.Result
import com.example.arsipsurat.databinding.FragmentSuratMasukBinding
import com.example.arsipsurat.ui.insert.surat_masuk.AddSuratMasukActivity
import com.google.android.material.snackbar.Snackbar

class SuratMasukFragment : Fragment() {

    private var _binding: FragmentSuratMasukBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    val suratAdapter = SuratMasukAdapter()


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

//        viewModelSuratMasuk.suratMasuk.observe(requireActivity()){suratMasuk->
//            if (suratMasuk != null) {
//                setSurat(suratMasuk)
//            }
//        }
//
//        viewModelSuratMasuk.isLoading.observe(requireActivity()){
//            showLoading(it)
//        }

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

        binding.fab.setOnClickListener{
            val intent = Intent(requireActivity(), AddSuratMasukActivity::class.java)
            startActivity(intent)
        }
        viewModelSuratMasuk.getPerihalMasuk(SuratMasukViewModel.PERIHAL)

    }

    private fun setSurat(dataSurat: List<SuratMasukItem?>){
        binding.rvSuratMasuk.layoutManager = LinearLayoutManager(requireActivity())
        suratAdapter.listSuratMasuk = dataSurat
        binding.rvSuratMasuk.adapter = suratAdapter
        binding.rvSuratMasuk.visibility = View.VISIBLE
        binding.rvSuratMasuk.setHasFixedSize(true)
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

    private fun showLoading(isLoading: Boolean){
        binding.pbLoading.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}