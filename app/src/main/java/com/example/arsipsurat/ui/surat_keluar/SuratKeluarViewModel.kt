package com.example.arsipsurat.ui.surat_keluar

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.arsipsurat.data.model.surat_keluar.SuratKeluarItem
import com.example.arsipsurat.data.repository.Result
import com.example.arsipsurat.data.repository.SuratRepository
import com.example.arsipsurat.utils.Event
import kotlinx.coroutines.launch

class SuratKeluarViewModel(private val suratRepository: SuratRepository) : ViewModel() {
//    private val _suratkeluar = MutableLiveData<List<SuratKeluarItem?>?>()
//    val suratKeluar : LiveData<List<SuratKeluarItem?>?> = _suratkeluar
//
//    private val _isLoading = MutableLiveData<Boolean>()
//    val isLoading : LiveData<Boolean> = _isLoading

    private val _uiStateSuratKeluar = MutableLiveData<Result<List<SuratKeluarItem?>>>()
    val uiStateKeluar : LiveData<Result<List<SuratKeluarItem?>>> = _uiStateSuratKeluar

    private val _deleteDataSuccess = MutableLiveData<Event<Boolean>>()
    val deleteDataSuccess : LiveData<Event<Boolean>> = _deleteDataSuccess

    companion object {
        private val TAG = "SuratKeluarViewModel"
        val PERIHAL = ""
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
    fun delete(suratKeluarItem: SuratKeluarItem){
        viewModelScope.launch {
            try {
                _deleteDataSuccess.postValue(
                    suratRepository.deleteSuratKeluar(suratKeluarItem)
                )
            }catch (e: Exception){
                e.printStackTrace()
                _deleteDataSuccess.postValue(Event(false))
            }
            getPerihalKeluar(PERIHAL)
        }
    }
}