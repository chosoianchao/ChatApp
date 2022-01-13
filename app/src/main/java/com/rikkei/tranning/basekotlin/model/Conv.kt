package com.rikkei.tranning.basekotlin.model

import android.os.Parcelable
import com.google.firebase.database.IgnoreExtraProperties
import kotlinx.parcelize.Parcelize

@Parcelize
@IgnoreExtraProperties
data class Conv(var seen: Boolean? = null, var timestamp: Long? = null) : Parcelable {

    override fun toString(): String {
        return "Conv(seen=$seen, timestamp=$timestamp)"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Conv

        if (seen != other.seen) return false
        if (timestamp != other.timestamp) return false

        return true
    }

    override fun hashCode(): Int {
        var result = seen?.hashCode() ?: 0
        result = 31 * result + (timestamp?.hashCode() ?: 0)
        return result
    }

}