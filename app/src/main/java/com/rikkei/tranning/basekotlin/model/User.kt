package com.rikkei.tranning.basekotlin.model

import android.os.Parcelable
import com.google.firebase.database.IgnoreExtraProperties
import kotlinx.parcelize.Parcelize

@Parcelize
@IgnoreExtraProperties
data class User(
    var Date: String? = "",
    var Email: String = "",
    var Id: String = "",
    var Name: String = "",
    var Password: String = "",
    var Phone: String = "",
    var PhotoUrl: String = "",
) : Parcelable {

    override fun equals(other: Any?): Boolean {
        if (javaClass != other?.javaClass) {
            return false
        }
        other as User
        when {
            Id != other.Id -> {
                return false
            }
            Email != other.Email -> {
                return false
            }
            Password != other.Password -> {
                return false
            }
            Name != other.Name -> {
                return false
            }
            Phone != other.Phone -> {
                return false
            }
            Date != other.Date -> {
                return false
            }
            PhotoUrl != other.PhotoUrl -> {
                return false
            }
            else -> return true
        }
    }


    override fun toString(): String {
        return "User(Date=$Date, Email='$Email', Id='$Id', Name='$Name', Password='$Password', Phone='$Phone', PhotoUrl='$PhotoUrl')"
    }

    override fun hashCode(): Int {
        var result = Date?.hashCode() ?: 0
        result = 31 * result + Email.hashCode()
        result = 31 * result + Id.hashCode()
        result = 31 * result + Name.hashCode()
        result = 31 * result + Password.hashCode()
        result = 31 * result + Phone.hashCode()
        result = 31 * result + PhotoUrl.hashCode()
        return result
    }
}
