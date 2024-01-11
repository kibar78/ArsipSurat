package com.example.arsipsurat.data

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.arsipsurat.data.di.Injection
import com.example.arsipsurat.data.repository.SuratRepository
import com.example.arsipsurat.ui.login.LoginViewModel
import com.example.arsipsurat.ui.surat_keluar.SuratKeluarViewModel
import com.example.arsipsurat.ui.surat_masuk.SuratMasukViewModel

class ViewModelFactory private constructor(private val suratMasukRepository: SuratRepository):
    ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SuratMasukViewModel::class.java)) {
            return SuratMasukViewModel(suratMasukRepository) as T
        }else if(modelClass.isAssignableFrom(SuratKeluarViewModel::class.java)){
            return SuratKeluarViewModel(suratMasukRepository) as T
        }else if (modelClass.isAssignableFrom(LoginViewModel::class.java)){
            return LoginViewModel(suratMasukRepository) as T
        }
            throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
        }
    companion object {
        @Volatile
        private var instance: ViewModelFactory? = null
        fun getInstance(context: Context): ViewModelFactory =
            instance ?: synchronized(this) {
                instance ?: ViewModelFactory(Injection.provideRepository(context))
            }.also { instance = it }
    }
    }