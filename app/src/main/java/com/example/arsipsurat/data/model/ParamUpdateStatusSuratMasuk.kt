package com.example.arsipsurat.data.model

import com.google.gson.annotations.SerializedName

data class ParamUpdateStatusSuratMasuk (
    @SerializedName("id") val id: Int,
    @SerializedName("data_status") val dataStatus: String
)