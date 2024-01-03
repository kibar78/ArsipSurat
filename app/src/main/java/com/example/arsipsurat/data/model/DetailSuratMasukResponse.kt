package com.example.arsipsurat.data.model

import com.google.gson.annotations.SerializedName

data class DetailSuratMasukResponse(

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
)
