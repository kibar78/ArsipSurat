package com.example.arsipsurat.data.remote

import com.example.arsipsurat.data.model.DeleteSuratKeluar
import com.example.arsipsurat.data.model.DeleteSuratMasuk
import com.example.arsipsurat.data.model.ParamUpdateSuratKeluar
import com.example.arsipsurat.data.model.ParamUpdateSuratMasuk
import com.example.arsipsurat.data.model.PostSuratKeluarResponse
import com.example.arsipsurat.data.model.PostSuratMasukResponse
import com.example.arsipsurat.data.model.SuratKeluar
import com.example.arsipsurat.data.model.SuratKeluarResponse
import com.example.arsipsurat.data.model.SuratMasuk
import com.example.arsipsurat.data.model.UpdateSuratKeluarResponse
import com.example.arsipsurat.data.model.UpdateSuratMasukResponse
import com.example.arsipsurat.data.model.disposisi.ParamUpdateDisposisi
import com.example.arsipsurat.data.model.disposisi.UpdateDisposisiResponse
import com.example.arsipsurat.data.model.surat_masuk.SuratMasukResponse
import com.example.arsipsurat.data.model.user.LoginResponse
import com.example.arsipsurat.data.model.user.LoginUser
import com.example.arsipsurat.data.model.user.ParamUpdateUser
import com.example.arsipsurat.data.model.user.ReadResponse
import com.example.arsipsurat.data.model.user.UpdateUserResponse
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
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Query

interface ApiService {

    //Suart Masuk
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
    @PUT("SURAT/surat_masuk/update")
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
    ): Call<UpdateSuratMasukResponse>

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
    @PUT("SURAT/surat_keluar/update")
    fun updateSuratKeluar(
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
    ): Call<UpdateSuratKeluarResponse>

    @Headers("ngrok-skip-browser-warning: 1234")
    @HTTP(method = "DELETE", path = "SURAT/surat_keluar/delete", hasBody = true)
    suspend fun deleteSuratKeluar(@Body param: DeleteSuratKeluar): PostSuratKeluarResponse

    @Headers("ngrok-skip-browser-warning: 1234")
    @HTTP(method = "PUT", path = "SURAT/surat_masuk/update_disposisi", hasBody = true)
    fun updateDisposisi(@Body disposisi: ParamUpdateDisposisi): Call<UpdateDisposisiResponse>

    //login
    @Headers("ngrok-skip-browser-warning: 1234")
    @POST("SURAT/user/login")
    fun login(@Body loginUser: LoginUser): Call<LoginResponse>

    @Headers("ngrok-skip-browser-warning: 1234")
    @PUT("SURAT/user/update")
    fun updateUser(
        @Part("id") id: RequestBody,
        @Part("username") username: RequestBody,
        @Part("password") password: RequestBody,
        @Part("nama_lengkap") namaLengkap: RequestBody,
        @Part("email") email: RequestBody,
        @Part("no_hp") noHp: RequestBody,
        @Part("level") level: RequestBody,
        @Part imageProfile : MultipartBody.Part?
    ): Call<UpdateUserResponse>

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