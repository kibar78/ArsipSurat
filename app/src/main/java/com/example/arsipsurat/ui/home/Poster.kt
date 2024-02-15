package com.example.arsipsurat.ui.home

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Poster(
    val name: String,
    val photo: Int
): Parcelable
