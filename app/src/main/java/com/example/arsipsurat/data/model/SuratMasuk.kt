package com.example.arsipsurat.data.model

import com.google.gson.annotations.SerializedName
import okhttp3.MultipartBody

data class SuratMasuk(
    @SerializedName("tgl_penerimaan") val tglPenerimaan: String,
    @SerializedName("tgl_surat") val tglSurat: String,
    @SerializedName("no_surat") val noSurat: String,
    @SerializedName("kategori") val kategori: String,
    @SerializedName("lampiran") val lampiran: MultipartBody.Part,
    @SerializedName("dari_mana") val dariMana: String,
    @SerializedName("perihal") val perihal: String,
    @SerializedName("keterangan") val keterangan: String,
    @SerializedName("image_surat") val imageSurat: MultipartBody.Part
)
