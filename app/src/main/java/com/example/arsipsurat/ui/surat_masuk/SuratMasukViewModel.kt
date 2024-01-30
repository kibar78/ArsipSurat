package com.example.arsipsurat.ui.surat_masuk

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.arsipsurat.data.model.SuratMasukItem
import com.example.arsipsurat.data.repository.Result
import com.example.arsipsurat.data.repository.SuratRepository
import com.example.arsipsurat.utils.Event
import kotlinx.coroutines.launch

class SuratMasukViewModel(private val suratRepository: SuratRepository) : ViewModel() {

    private val _uiStateSuratMasuk = MutableLiveData<Result<List<SuratMasukItem?>>>()
    val uiStateSuratMasuk : LiveData<Result<List<SuratMasukItem?>>> = _uiStateSuratMasuk

    private val _deleteDataSuccess = MutableLiveData<Event<Boolean>>()
    val deleteDataSuccess : LiveData<Event<Boolean>> = _deleteDataSuccess

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

    fun delete(suratMasukItem: SuratMasukItem) {
        viewModelScope.launch {
            try {
                _deleteDataSuccess.postValue(
                    suratRepository.deleteSuratMasuk(suratMasukItem)
                )
            }
            catch (e: Exception) {
                e.printStackTrace()
                _deleteDataSuccess.postValue(Event(false))
            }

            getPerihalMasuk(PERIHAL)
        }
    }
}