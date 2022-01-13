package com.rikkei.tranning.basekotlin.model

import android.os.Parcelable
import com.google.firebase.database.IgnoreExtraProperties
import kotlinx.parcelize.Parcelize

@Parcelize
@IgnoreExtraProperties
data class Message(
    var message: String? = "",
    var from: String? = "",
    var seen: String? = "",
    var time: String? = "",
    var type: String? = ""
) : Parcelable {

    override fun toString(): String {
        return "Message(message=$message, from=$from, seen=$seen, time=$time, type=$type)"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Message

        if (message != other.message) return false
        if (from != other.from) return false
        if (seen != other.seen) return false
        if (time != other.time) return false
        if (type != other.type) return false

        return true
    }

    override fun hashCode(): Int {
        var result = message?.hashCode() ?: 0
        result = 31 * result + (from?.hashCode() ?: 0)
        result = 31 * result + (seen?.hashCode() ?: 0)
        result = 31 * result + (time?.hashCode() ?: 0)
        result = 31 * result + (type?.hashCode() ?: 0)
        return result
    }
}