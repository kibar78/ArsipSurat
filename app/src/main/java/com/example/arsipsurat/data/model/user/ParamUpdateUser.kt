package com.example.arsipsurat.data.model.user

import com.google.gson.annotations.SerializedName

data class ParamUpdateUser(
    @SerializedName("id") val id: Int,
    @SerializedName("nama_lengkap") val namaLengkap: String,
    @SerializedName("email") val email: String,
    @SerializedName("username") val username: String,
    @SerializedName("password") val password: String,
    @SerializedName("bidang_pekerjaan") val bidang_pekerjaan: String,
    @SerializedName("no_hp") val noHp: String,
    @SerializedName("level") val level: String,
    @SerializedName("image_profile") val imageProfile: String
)
