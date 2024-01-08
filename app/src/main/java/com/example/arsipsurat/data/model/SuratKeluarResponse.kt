package com.example.arsipsurat.data.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

data class SuratKeluarResponse(

	@field:SerializedName("surat_keluar")
	val suratKeluar: List<SuratKeluarItem?>? = null
)
@Parcelize
data class SuratKeluarItem(

	@field:SerializedName("keterangan")
	val keterangan: String? = null,

	@field:SerializedName("tgl_surat")
	val tglSurat: String? = null,

	@field:SerializedName("no_surat")
	val noSurat: String? = null,

	@field:SerializedName("lampiran")
	val lampiran: String? = null,

	@field:SerializedName("image_surat")
	val imageSurat: String? = null,

	@field:SerializedName("tgl_penerimaan")
	val tglCatat: String? = null,

	@field:SerializedName("dari_mana")
	val dikirimKepada: String? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("perihal")
	val perihal: String? = null
): Parcelable
