package com.example.arsipsurat.ui.register

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.arsipsurat.data.model.user.UserItem
import com.example.arsipsurat.data.repository.Result
import com.example.arsipsurat.data.repository.SuratRepository
import com.example.arsipsurat.ui.surat_masuk.SuratMasukViewModel
import com.example.arsipsurat.utils.Event
import kotlinx.coroutines.launch
import java.net.IDN

class RegisterViewModel(private val suratRepository: SuratRepository): ViewModel() {

    private val _uiStateUser = MutableLiveData<Result<List<UserItem?>>>()
    val uiStateUser : LiveData<Result<List<UserItem?>>> = _uiStateUser

    private val _deleteDataSuccess = MutableLiveData<Event<Boolean>>()
    val deleteDataSuccess : LiveData<Event<Boolean>> = _deleteDataSuccess

    companion object{
        val ID = ""
    }
    fun getUser(id: String){
        _uiStateUser.value = Result.Loading
        viewModelScope.launch {
            try {
                _uiStateUser.value = Result.Success(suratRepository.readUser(id).user!!)
            }catch (e: Exception){
                _uiStateUser.value = Result.Error(e.message.toString())
            }
        }
    }

    fun delete(userItem: UserItem){
        viewModelScope.launch {
            try {
                _deleteDataSuccess.postValue(
                    suratRepository.deleteUser(userItem)
                )
            }
            catch (e: Exception) {
                e.printStackTrace()
                _deleteDataSuccess.postValue(Event(false))
            }
            getUser(ID)
        }
    }
}