package com.example.arsipsurat.data.model.disposisi

import com.google.gson.annotations.SerializedName

data class ParamUpdateDisposisi(
    @SerializedName("id") val id: Int,
    @SerializedName("klasifikasi") val klasifikasi: String,
    @SerializedName("derajat") val derajat: String,
    @SerializedName("nomor_agenda") val nomorAgenda: String,
    @SerializedName("isi_disposisi") val isiDisposisi: String,
    @SerializedName("diteruskan_kepada") val diteruskanKepada: String
)
