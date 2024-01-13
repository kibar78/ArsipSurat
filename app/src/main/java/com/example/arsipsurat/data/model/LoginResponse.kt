package com.example.arsipsurat.data.model

import com.google.gson.annotations.SerializedName

data class LoginResponse(

	@field:SerializedName("no_hp")
	val noHp: String? = null,

	@field:SerializedName("level")
	val level: String? = null,

	@field:SerializedName("nama_lengkap")
	val namaLengkap: String? = null,

	@field:SerializedName("email")
	val email: String? = null,

	@field:SerializedName("bidang_pekerjaan")
	val bidangPekerjaan: String? = null,

	@field:SerializedName("username")
	val username: String? = null
)
