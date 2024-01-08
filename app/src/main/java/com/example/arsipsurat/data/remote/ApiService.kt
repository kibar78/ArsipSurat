package com.example.arsipsurat.data.remote

import com.example.arsipsurat.data.model.PostSuratMasukResponse
import com.example.arsipsurat.data.model.SuratKeluarResponse
import com.example.arsipsurat.data.model.SuratMasukResponse
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Query

interface ApiService {
    @Headers("ngrok-skip-browser-warning: 123")
    @GET("SURAT/surat_masuk/search")
    suspend fun getPerihalMasuk(@Query("perihal")perihalSuratMasuk: String): SuratMasukResponse

    @Headers("ngrok-skip-browser-warning: 123")
    @GET("SURAT/surat_keluar/search")
    suspend fun getPerihalKeluar(@Query("perihal")perihalSuratKeluar: String): SuratKeluarResponse

    @FormUrlEncoded
    @Headers("ngrok-skip-browser-warning: 1234")
    @POST("SURAT/surat_masuk/create")
    fun postSuratMasuk(
        @Field("id") id: Int,
        @Field("tgl_penerimaan") tglPenerimaan: String,
        @Field("tgl_surat") tglSurat: String,
        @Field("no_surat") noSurat: String,
        @Field("lampiran") lampiran: String,
        @Field("dari_mana") dariMana: String,
        @Field("perihal") perihal: String,
        @Field("keterangan") keterangan: String,
        @Field("image_surat") imageSurat: String
    ): Call<PostSuratMasukResponse>
}