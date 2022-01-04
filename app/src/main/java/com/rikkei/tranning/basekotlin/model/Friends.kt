package com.rikkei.tranning.basekotlin.model

import android.os.Parcelable
import com.google.firebase.database.IgnoreExtraProperties
import kotlinx.parcelize.Parcelize

@Parcelize
@IgnoreExtraProperties
data class Friends(
    var Date: String? = "",
    var Description: String = "",
    var Time: String = "",
    var Id: String = "",
    var Name: String = "",
    var PhotoUrl: String = "",
) : Parcelable {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Friends

        if (Date != other.Date) return false
        if (Description != other.Description) return false
        if (Time != other.Time) return false
        if (Id != other.Id) return false
        if (Name != other.Name) return false
        if (PhotoUrl != other.PhotoUrl) return false

        return true
    }

    override fun hashCode(): Int {
        var result = Date?.hashCode() ?: 0
        result = 31 * result + Description.hashCode()
        result = 31 * result + Time.hashCode()
        result = 31 * result + Id.hashCode()
        result = 31 * result + Name.hashCode()
        result = 31 * result + PhotoUrl.hashCode()
        return result
    }

    override fun toString(): String {
        return "Friends(Date=$Date, Description='$Description', Time='$Time', Id='$Id', Name='$Name', PhotoUrl='$PhotoUrl')"
    }

}