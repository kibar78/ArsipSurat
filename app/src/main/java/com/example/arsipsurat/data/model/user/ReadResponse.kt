package com.example.arsipsurat.data.model.user

import com.google.gson.annotations.SerializedName

data class ReadResponse(

	@field:SerializedName("user")
	val user: List<UserItem?>? = null
)

data class UserItem(

	@field:SerializedName("password")
	val password: String? = null,

	@field:SerializedName("no_hp")
	val noHp: String? = null,

	@field:SerializedName("level")
	val level: String? = null,

	@field:SerializedName("image_profile")
	val imageProfile: String? = null,

	@field:SerializedName("nama_lengkap")
	val namaLengkap: String? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("email")
	val email: String? = null,

	@field:SerializedName("bidang_pekerjaan")
	val bidangPekerjaan: String? = null,

	@field:SerializedName("username")
	val username: String? = null
)
