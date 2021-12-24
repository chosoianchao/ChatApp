package com.rikkei.tranning.basekotlin.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class User(
    var uid: String? = null,
    var email: String,
    var password: String,
    var name: String = "",
    var dob: String = "",
    var avatar: String = "",
) : Parcelable
