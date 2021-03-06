package com.rikkei.tranning.basekotlin.model

import android.os.Parcelable
import com.google.firebase.database.IgnoreExtraProperties
import kotlinx.parcelize.Parcelize

@Parcelize
@IgnoreExtraProperties
data class User(
    var Date: String? = "",
    var Email: String = "",
    var Description: String = "",
    var Id: String = "",
    var Name: String = "",
    var Password: String = "",
    var Phone: String = "",
    var PhotoUrl: String = "",
) : Parcelable {

    override fun toString(): String {
        return "User(Date=$Date, Email='$Email', Description='$Description', Id='$Id', Name='$Name', Password='$Password', Phone='$Phone', PhotoUrl='$PhotoUrl')"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as User

        if (Date != other.Date) return false
        if (Email != other.Email) return false
        if (Description != other.Description) return false
        if (Id != other.Id) return false
        if (Name != other.Name) return false
        if (Password != other.Password) return false
        if (Phone != other.Phone) return false
        if (PhotoUrl != other.PhotoUrl) return false

        return true
    }

    override fun hashCode(): Int {
        var result = Date?.hashCode() ?: 0
        result = 31 * result + Email.hashCode()
        result = 31 * result + Description.hashCode()
        result = 31 * result + Id.hashCode()
        result = 31 * result + Name.hashCode()
        result = 31 * result + Password.hashCode()
        result = 31 * result + Phone.hashCode()
        result = 31 * result + PhotoUrl.hashCode()
        return result
    }
}
