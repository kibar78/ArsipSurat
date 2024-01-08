package com.example.arsipsurat.ui.surat_keluar

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.arsipsurat.data.model.SuratKeluarItem
import com.example.arsipsurat.data.repository.Result
import com.example.arsipsurat.data.repository.SuratRepository
import kotlinx.coroutines.launch

class SuratKeluarViewModel(private val suratRepository: SuratRepository) : ViewModel() {
//    private val _suratkeluar = MutableLiveData<List<SuratKeluarItem?>?>()
//    val suratKeluar : LiveData<List<SuratKeluarItem?>?> = _suratkeluar
//
//    private val _isLoading = MutableLiveData<Boolean>()
//    val isLoading : LiveData<Boolean> = _isLoading

    private val _uiStateSuratKeluar = MutableLiveData<Result<List<SuratKeluarItem?>>>()
    val uiStateKeluar : LiveData<Result<List<SuratKeluarItem?>>> = _uiStateSuratKeluar

    companion object {
        private val TAG = "SuratKeluarViewModel"
        private val PERIHAL = ""
    }

    init {
        getPerihalKeluar(PERIHAL)
    }

    fun getPerihalKeluar(perihalkeluar: String) {
        _uiStateSuratKeluar.value = Result.Loading
        viewModelScope.launch {
            try {
                _uiStateSuratKeluar.value = Result.Success(suratRepository.getSuratKeluar(perihalkeluar).suratKeluar!!)
            }catch (e: Exception){
                _uiStateSuratKeluar.value = Result.Error(e.message.toString())
            }
        }
    }
}