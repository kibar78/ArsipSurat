package com.example.arsipsurat.data.repository

import com.example.arsipsurat.data.model.SuratKeluarResponse
import com.example.arsipsurat.data.model.SuratMasukResponse
import com.example.arsipsurat.data.remote.ApiService

class SuratRepository private constructor(
    private val apiService: ApiService
){
    suspend fun getSuratMasuk(perihalSuratMasuk: String): SuratMasukResponse{
        return apiService.getPerihalMasuk(perihalSuratMasuk)
    }

    suspend fun getSuratKeluar(perihalSuratKeluar: String): SuratKeluarResponse{
        return apiService.getPerihalKeluar(perihalSuratKeluar)
    }

    companion object{
        private const val TAG = "SuratMasukRepository"

        @Volatile
        private var instance: SuratRepository? = null
        fun getInstance(
            apiService: ApiService
        ): SuratRepository = instance?: synchronized(this){
            instance?: SuratRepository(apiService)
        }.also { instance = it }
    }
}