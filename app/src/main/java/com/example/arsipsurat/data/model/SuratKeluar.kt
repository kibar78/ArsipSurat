package com.example.arsipsurat.data.model

import com.google.gson.annotations.SerializedName

data class SuratKeluar(
    @SerializedName("tgl_catat") val tglCatat: String,
    @SerializedName("tgl_surat") val tglSurat: String,
    @SerializedName("no_surat") val noSurat: String,
    @SerializedName("lampiran") val lampiran: String,
    @SerializedName("dikirim_kepada") val dikirimKepada: String,
    @SerializedName("perihal") val perihal: String,
    @SerializedName("keterangan") val keterangan: String,
    @SerializedName("image_surat") val imageSurat: String
)