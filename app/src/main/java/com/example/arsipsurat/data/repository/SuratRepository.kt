package com.example.arsipsurat.data.repository

import com.example.arsipsurat.data.model.surat_keluar.DeleteSuratKeluar
import com.example.arsipsurat.data.model.surat_masuk.DeleteSuratMasuk
import com.example.arsipsurat.data.model.surat_keluar.SuratKeluarItem
import com.example.arsipsurat.data.model.surat_keluar.SuratKeluarResponse
import com.example.arsipsurat.data.model.surat_masuk.SuratMasukItem
import com.example.arsipsurat.data.model.surat_masuk.SuratMasukResponse
import com.example.arsipsurat.data.model.user.ReadResponse
import com.example.arsipsurat.data.model.user.UserItem
import com.example.arsipsurat.data.model.user.delete.DeleteAkun
import com.example.arsipsurat.data.remote.ApiService
import com.example.arsipsurat.utils.Event

class SuratRepository private constructor(
    private val apiService: ApiService
){
    suspend fun getSuratMasuk(perihalSuratMasuk: String): SuratMasukResponse{
        return apiService.getPerihalMasuk(perihalSuratMasuk)
    }

    suspend fun getSuratKeluar(perihalSuratKeluar: String): SuratKeluarResponse {
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

    suspend fun readUser(id: String): ReadResponse{
        return apiService.getUser(id)
    }

    suspend fun deleteUser(userItem: UserItem): Event<Boolean> {
        if (userItem.id == null) return Event(false)

        val response = apiService.deleteUser(
            DeleteAkun(userItem.id)
        )
        return Event(response.message?.contains("deleted") == true)
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