package com.example.arsipsurat.ui.surat_masuk

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.arsipsurat.data.model.SuratMasukItem
import com.example.arsipsurat.data.repository.Result
import com.example.arsipsurat.data.repository.SuratRepository
import kotlinx.coroutines.launch

class SuratMasukViewModel(private val suratRepository: SuratRepository) : ViewModel() {
//    private val _suratMasuk = MutableLiveData<List<SuratMasukItem?>?>()
//    val suratMasuk : LiveData<List<SuratMasukItem?>?> = _suratMasuk
//
//    private val _isLoading = MutableLiveData<Boolean>()
//    val isLoading : LiveData<Boolean> = _isLoading

    private val _uiStateSuratMasuk = MutableLiveData<Result<List<SuratMasukItem?>>>()
    val uiStateSuratMasuk : LiveData<Result<List<SuratMasukItem?>>> = _uiStateSuratMasuk

    companion object {
        private val TAG = "SuratMasukViewModel"
        val PERIHAL = ""
    }

    fun getPerihalMasuk(perihal: String) {
        _uiStateSuratMasuk.value = Result.Loading
        viewModelScope.launch {
            try {
                _uiStateSuratMasuk.value = Result.Success(suratRepository.getSuratMasuk(perihal).suratMasuk!!)
            } catch (e: Exception){
                _uiStateSuratMasuk.value = Result.Error(e.message.toString())
            }
        }
    }
}