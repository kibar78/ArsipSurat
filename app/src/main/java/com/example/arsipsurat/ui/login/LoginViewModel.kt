package com.example.arsipsurat.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.arsipsurat.data.model.LoginResponse
import com.example.arsipsurat.data.model.UserLogin
import com.example.arsipsurat.data.repository.SuratRepository
import com.example.arsipsurat.data.repository.Result
import kotlinx.coroutines.launch

class LoginViewModel(private val repository: SuratRepository) : ViewModel(){

    private val _uiStateLogin = MutableLiveData<Result<LoginResponse>>()
    val uiStatelogin : LiveData<Result<LoginResponse>> = _uiStateLogin

    fun login(userLogin: UserLogin){
        _uiStateLogin.value = Result.Loading
        viewModelScope.launch {
            try {
                _uiStateLogin.value = Result.Success(repository.userLogin(userLogin))
            }catch (e: Exception){
                _uiStateLogin.value = Result.Error(e.message.toString())
            }
        }
    }
}