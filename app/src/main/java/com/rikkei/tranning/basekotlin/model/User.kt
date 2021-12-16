package com.rikkei.tranning.basekotlin.model

import java.io.Serializable


data class User(
    var uid: String? = null,
    var email: String,
    var password: String,
    var name: String = "",
    var dob: String = "",
    var avatar: String = "",
) : Serializable
