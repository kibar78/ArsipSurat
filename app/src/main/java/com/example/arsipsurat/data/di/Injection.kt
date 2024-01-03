package com.example.arsipsurat.data.di

import android.content.Context
import com.example.arsipsurat.data.remote.ApiConfig
import com.example.arsipsurat.data.repository.SuratRepository

object Injection {
    fun provideRepository(context: Context): SuratRepository{
        val apiService = ApiConfig.getApiService()

        return SuratRepository.getInstance(apiService)
    }
}