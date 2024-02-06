package com.example.arsipsurat.data.remote

import com.example.arsipsurat.data.model.surat_keluar.DeleteSuratKeluar
import com.example.arsipsurat.data.model.surat_masuk.DeleteSuratMasuk
import com.example.arsipsurat.data.model.surat_keluar.PostSuratKeluarResponse
import com.example.arsipsurat.data.model.surat_masuk.PostSuratMasukResponse
import com.example.arsipsurat.data.model.surat_keluar.SuratKeluarItem
import com.example.arsipsurat.data.model.surat_keluar.SuratKeluarResponse
import com.example.arsipsurat.data.model.disposisi.ParamUpdateDisposisi
import com.example.arsipsurat.data.model.surat_masuk.SuratMasukItem
import com.example.arsipsurat.data.model.surat_masuk.SuratMasukResponse
import com.example.arsipsurat.data.model.user.LoginResponse
import com.example.arsipsurat.data.model.user.LoginUser
import com.example.arsipsurat.data.model.user.ReadResponse
import com.example.arsipsurat.data.model.user.create.CreateUserResponse
import com.example.arsipsurat.data.model.user.create.ParamCreateUser
import com.example.arsipsurat.data.model.user.delete.DeleteAkun
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.HTTP
import retrofit2.http.Headers
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Query

interface ApiService {

    //Surat Masuk
    @Headers("ngrok-skip-browser-warning: 123")
    @GET("SURAT/surat_masuk/search")
    suspend fun getPerihalMasuk(@Query("perihal")perihalSuratMasuk: String): SuratMasukResponse

    @Multipart
    @Headers("ngrok-skip-browser-warning: 1234")
    @POST("SURAT/surat_masuk/create")
    fun createSuratMasuk(
        @Part("tgl_penerimaan") tglPenerimaan: RequestBody,
        @Part("tgl_surat") tglSurat: RequestBody,
        @Part("no_surat") noSurat: RequestBody,
        @Part("kategori") kategori: RequestBody,
        @Part("dari_mana") dariMana: RequestBody,
        @Part("perihal") perihal: RequestBody,
        @Part("keterangan") keterangan: RequestBody,
        @Part lampiran : MultipartBody.Part?,
        @Part imageSurat : MultipartBody.Part?
    ): Call<PostSuratMasukResponse>

    @Multipart
    @Headers("ngrok-skip-browser-warning: 1234")
    @POST("SURAT/surat_masuk/update")
    fun updateSuratMasuk(
        @Part("id") id: RequestBody,
        @Part("tgl_penerimaan") tglPenerimaan: RequestBody,
        @Part("tgl_surat") tglSurat: RequestBody,
        @Part("no_surat") noSurat: RequestBody,
        @Part("kategori") kategori: RequestBody,
        @Part("dari_mana") dariMana: RequestBody,
        @Part("perihal") perihal: RequestBody,
        @Part("keterangan") keterangan: RequestBody,
        @Part lampiran: MultipartBody.Part?,
        @Part imageSurat: MultipartBody.Part?
    ): Call<SuratMasukItem>

    @Headers("ngrok-skip-browser-warning: 1234")
    @HTTP(method = "DELETE", path = "SURAT/surat_masuk/delete", hasBody = true)
    suspend fun deleteSuratMasuk(@Body param: DeleteSuratMasuk): PostSuratMasukResponse


    //Surat Keluar
    @Headers("ngrok-skip-browser-warning: 123")
    @GET("SURAT/surat_keluar/search")
    suspend fun getPerihalKeluar(@Query("perihal")perihalSuratKeluar: String): SuratKeluarResponse

    @Multipart
    @Headers("ngrok-skip-browser-warning: 1234")
    @POST("SURAT/surat_keluar/create")
    fun createSuratKeluar(
        @Part("tgl_catat") tglCatat: RequestBody,
        @Part("tgl_surat") tglSurat: RequestBody,
        @Part("no_surat") noSurat: RequestBody,
        @Part("kategori") kategori: RequestBody,
        @Part("dikirim_kepada") dikirimKepada: RequestBody,
        @Part("perihal") perihal: RequestBody,
        @Part("keterangan") keterangan: RequestBody,
        @Part lampiran : MultipartBody.Part?,
        @Part imageSurat : MultipartBody.Part?
    ): Call<PostSuratKeluarResponse>

    @Multipart
    @Headers("ngrok-skip-browser-warning: 1234")
    @POST("SURAT/surat_keluar/update")
    fun updateSuratKeluar(
        @Part("id") id: RequestBody,
        @Part("tgl_catat") tglCatat: RequestBody,
        @Part("tgl_surat") tglSurat: RequestBody,
        @Part("no_surat") noSurat: RequestBody,
        @Part("kategori") kategori: RequestBody,
        @Part("dikirim_kepada") dikirimKepada: RequestBody,
        @Part("perihal") perihal: RequestBody,
        @Part("keterangan") keterangan: RequestBody,
        @Part lampiran: MultipartBody.Part?,
        @Part imageSurat: MultipartBody.Part?
    ): Call<SuratKeluarItem>

    @Headers("ngrok-skip-browser-warning: 1234")
    @HTTP(method = "DELETE", path = "SURAT/surat_keluar/delete", hasBody = true)
    suspend fun deleteSuratKeluar(@Body param: DeleteSuratKeluar): PostSuratKeluarResponse

    //Disposisi
    @Headers("ngrok-skip-browser-warning: 1234")
    @POST("SURAT/surat_masuk/update_disposisi")
    fun updateDisposisi(@Body disposisi: ParamUpdateDisposisi): Call<SuratMasukItem>

    //login & user
    @Headers("ngrok-skip-browser-warning: 1234")
    @POST("SURAT/user/login")
    fun login(@Body loginUser: LoginUser): Call<LoginResponse>

    @Multipart
    @Headers("ngrok-skip-browser-warning: 1234")
    @POST("SURAT/user/update")
    fun updateUser(
        @Part("id") id: RequestBody,
        @Part("username") username: RequestBody,
        @Part("password") password: RequestBody,
        @Part("nama_lengkap") namaLengkap: RequestBody,
        @Part("email") email: RequestBody,
        @Part("bidang_pekerjaan") bidangPekerjaan: RequestBody,
        @Part("no_hp") noHp: RequestBody,
        @Part imageProfile : MultipartBody.Part?
    ): Call<LoginResponse>

    @Headers("ngrok-skip-browser-warning: 123")
    @GET("SURAT/user/read")
    suspend fun getUser(@Query("id")id: String): ReadResponse

    @Headers("ngrok-skip-browser-warning: 1234")
    @POST("SURAT/user/create")
    fun createUser(@Body user: ParamCreateUser): Call<CreateUserResponse>

    @Headers("ngrok-skip-browser-warning: 1234")
    @HTTP(method = "DELETE", path = "SURAT/user/delete", hasBody = true)
    suspend fun deleteUser(@Body param: DeleteAkun): CreateUserResponse
}