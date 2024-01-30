package com.example.arsipsurat.data.model.user.create

import com.google.gson.annotations.SerializedName

data class ParamCreateUser(
    @SerializedName("username") val username: String,
    @SerializedName("password") val password: String,
    @SerializedName("level") val level: String
)
