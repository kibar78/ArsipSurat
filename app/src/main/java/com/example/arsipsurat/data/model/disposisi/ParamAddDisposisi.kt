package com.example.arsipsurat.data.model.disposisi

import com.google.gson.annotations.SerializedName

data class ParamAddDisposisi(
    @SerializedName("klasifikasi") val klasifikasi: String,
    @SerializedName("derajat") val derajat: String,
    @SerializedName("nomor_agenda") val nomorAgenda: String,
    @SerializedName("tanggal_diterima") val tanggalDiterima: String,
    @SerializedName("surat_dari") val suratDari: String,
    @SerializedName("nomor_surat") val nomorSurat: String,
    @SerializedName("tanggal_surat") val tanggal_surat: String,
    @SerializedName("perihal") val perihal: String,
    @SerializedName("isi_disposisi") val isiDisposisi: String,
    @SerializedName("diteruskan_kepada") val diteruskanKepada: String
)
