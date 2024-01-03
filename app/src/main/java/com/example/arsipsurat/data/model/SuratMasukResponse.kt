package com.example.arsipsurat.data.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

data class SuratMasukResponse(

	@field:SerializedName("surat_masuk")
	val suratMasuk: List<SuratMasukItem?>? = null
)
@Parcelize
data class SuratMasukItem(

	@field:SerializedName("keterangan")
	val keterangan: String? = null,

	@field:SerializedName("tgl_penerimaan")
	val tglPenerimaan: String? = null,

	@field:SerializedName("tgl_surat")
	val tglSurat: String? = null,

	@field:SerializedName("no_surat")
	val noSurat: String? = null,

	@field:SerializedName("lampiran")
	val lampiran: String? = null,

	@field:SerializedName("image_surat")
	val imageSurat: String? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("dari_mana")
	val dariMana: String? = null,

	@field:SerializedName("perihal")
	val perihal: String? = null
):Parcelable
