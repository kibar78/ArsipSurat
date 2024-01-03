package com.example.arsipsurat.ui.surat_keluar

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.arsipsurat.data.ViewModelFactory
import com.example.arsipsurat.data.model.SuratKeluarItem
import com.example.arsipsurat.data.repository.Result
import com.example.arsipsurat.databinding.FragmentSuratKeluarBinding

class SuratKeluarFragment : Fragment() {

    private var _binding: FragmentSuratKeluarBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

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

//        viewModelSuratKeluar.suratKeluar.observe(requireActivity()){suratKeluar->
//            if (suratKeluar != null) {
//                setSurat(suratKeluar)
//            }
//        }
//        viewModelSuratKeluar.isLoading.observe(requireActivity()){
//            showLoading(it)
//        }

        viewModelSuratKeluar.uiStateKeluar.observe(requireActivity()){uiStateSuratKeluar->
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
    }

    private fun setSurat(dataSurat: List<SuratKeluarItem?>){
        binding.rvSuratKeluar.layoutManager = LinearLayoutManager(requireActivity())
        val suratKeluarAdapter = SuratKeluarAdapter(dataSurat)
        binding.rvSuratKeluar.adapter = suratKeluarAdapter
        binding.rvSuratKeluar.setHasFixedSize(true)
    }

    private fun showLoading(isLoading: Boolean){
        binding.pbLoadingSuratKeluar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}