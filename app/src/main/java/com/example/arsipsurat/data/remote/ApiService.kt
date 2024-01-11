package com.example.arsipsurat.data.remote

import com.example.arsipsurat.data.model.DeleteSuratKeluar
import com.example.arsipsurat.data.model.DeleteSuratMasuk
import com.example.arsipsurat.data.model.LoginResponse
import com.example.arsipsurat.data.model.ParamUpdateSuratMasuk
import com.example.arsipsurat.data.model.PostSuratKeluarResponse
import com.example.arsipsurat.data.model.PostSuratMasukResponse
import com.example.arsipsurat.data.model.SuratKeluar
import com.example.arsipsurat.data.model.SuratKeluarResponse
import com.example.arsipsurat.data.model.SuratMasuk
import com.example.arsipsurat.data.model.SuratMasukResponse
import com.example.arsipsurat.data.model.UpdateSuratMasukResponse
import com.example.arsipsurat.data.model.UserLogin
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.HTTP
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

    @Headers("ngrok-skip-browser-warning: 1234")
    @POST("SURAT/surat_masuk/create")
    fun postSuratMasuk(@Body suratMasuk: SuratMasuk): Call<PostSuratMasukResponse>

    @Headers("ngrok-skip-browser-warning: 1234")
    @POST("SURAT/surat_keluar/create")
    fun postSuratkeluar(@Body suratKeluar: SuratKeluar): Call<PostSuratKeluarResponse>

    @Headers("ngrok-skip-browser-warning: 1234")
    @HTTP(method = "DELETE", path = "SURAT/surat_masuk/delete", hasBody = true)
    suspend fun deleteSuratMasuk(@Body param: DeleteSuratMasuk): PostSuratMasukResponse

    @Headers("ngrok-skip-browser-warning: 1234")
    @HTTP(method = "DELETE", path = "SURAT/surat_keluar/delete", hasBody = true)
    suspend fun deleteSuratKeluar(@Body param: DeleteSuratKeluar): PostSuratKeluarResponse

    @Headers("ngrok-skip-browser-warning: 1234")
    @POST("SURAT/user/login")
    suspend fun login(@Body userLogin: UserLogin): LoginResponse

    @Headers("ngrok-skip-browser-warning: 1234")
    @HTTP(method = "PUT", path = "SURAT/surat_masuk/update", hasBody = true)
    fun updateSuratMasuk(@Body suratMasuk: ParamUpdateSuratMasuk): Call<UpdateSuratMasukResponse>
}