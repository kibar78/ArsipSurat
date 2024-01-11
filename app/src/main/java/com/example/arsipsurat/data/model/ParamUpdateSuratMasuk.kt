package com.example.arsipsurat.data.model

import com.google.gson.annotations.SerializedName

data class ParamUpdateSuratMasuk(
    @SerializedName("id") val id: Int,
    @SerializedName("tgl_penerimaan") val tglPenerimaan: String,
    @SerializedName("tgl_surat") val tglSurat: String,
    @SerializedName("no_surat") val noSurat: String,
    @SerializedName("lampiran") val lampiran: String,
    @SerializedName("dari_mana") val dariMana: String,
    @SerializedName("perihal") val perihal: String,
    @SerializedName("keterangan") val keterangan: String,
    @SerializedName("image_surat") val imageSurat: String
)
