package com.example.arsipsurat.data.repository

import com.example.arsipsurat.data.model.DeleteSuratKeluar
import com.example.arsipsurat.data.model.DeleteSuratMasuk
import com.example.arsipsurat.data.model.LoginResponse
import com.example.arsipsurat.data.model.SuratKeluarItem
import com.example.arsipsurat.data.model.SuratKeluarResponse
import com.example.arsipsurat.data.model.SuratMasukItem
import com.example.arsipsurat.data.model.SuratMasukResponse
import com.example.arsipsurat.data.model.UserLogin
import com.example.arsipsurat.data.remote.ApiService
import com.example.arsipsurat.utils.Event

class SuratRepository private constructor(
    private val apiService: ApiService
){
    suspend fun getSuratMasuk(perihalSuratMasuk: String): SuratMasukResponse{
        return apiService.getPerihalMasuk(perihalSuratMasuk)
    }

    suspend fun getSuratKeluar(perihalSuratKeluar: String): SuratKeluarResponse{
        return apiService.getPerihalKeluar(perihalSuratKeluar)
    }

    suspend fun deleteSuratMasuk(suratMasukItem: SuratMasukItem): Event<Boolean> {
        if (suratMasukItem.id == null) return Event(false)

        val response = apiService.deleteSuratMasuk(
            DeleteSuratMasuk(suratMasukItem.id)
        )

        return Event(response.message?.contains("deleted") == true)
    }

    suspend fun deleteSuratKeluar(suratKeluarItem: SuratKeluarItem): Event<Boolean>{
        if (suratKeluarItem.id == null) return Event(false)

        val response = apiService.deleteSuratKeluar(
            DeleteSuratKeluar(suratKeluarItem.id)
        )
        return Event(response.message?.contains("deleted") == true)
    }

    suspend fun userLogin(userLogin: UserLogin): LoginResponse{
        return apiService.login(userLogin)
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