package com.example.arsipsurat.ui.profile

import androidx.lifecycle.ViewModel
import com.example.arsipsurat.data.model.user.LoginResponse

class ProfileViewModel : ViewModel() {
    var listUser : List<LoginResponse?> = listOf()
}