package com.example.arsipsurat.data.model

import com.google.gson.annotations.SerializedName

data class SuratKeluarResponse(
	@field:SerializedName("surat_keluar")
	val suratKeluar: List<SuratKeluarItem?>? = null
)

data class SuratKeluarItem(
	@field:SerializedName("keterangan")
	val keterangan: String? = null,

	@field:SerializedName("tgl_penerimaan")
	val tglPenerimaan: String? = null,

	@field:SerializedName("tgl_surat")
	val tglSurat: String? = null,

	@field:SerializedName("no_surat")
	val noSurat: String? = null,

	@field:SerializedName("lampiran")
	val lampiran: Any? = null,

	@field:SerializedName("image_surat")
	val imageSurat: Any? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("dari_mana")
	val dariMana: String? = null,

	@field:SerializedName("perihal")
	val perihal: String? = null
)

