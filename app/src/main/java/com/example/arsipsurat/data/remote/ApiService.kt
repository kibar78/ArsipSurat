package com.example.arsipsurat.data.remote

import com.example.arsipsurat.data.model.SuratKeluarResponse
import com.example.arsipsurat.data.model.SuratMasukResponse
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface ApiService {
    @Headers("ngrok-skip-browser-warning: 123")
    @GET("SURAT/surat_masuk/search")
    suspend fun getPerihalMasuk(@Query("perihal")perihalSuratMasuk: String): SuratMasukResponse

    @Headers("ngrok-skip-browser-warning: 1234")
    @GET("SURAT/surat_keluar/search")
    suspend fun getPerihalKeluar(@Query("perihal")perihalSuratKeluar: String): SuratKeluarResponse

}