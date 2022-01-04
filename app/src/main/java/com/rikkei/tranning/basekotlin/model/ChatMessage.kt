package com.rikkei.tranning.basekotlin.model

import android.os.Parcelable
import com.google.firebase.database.IgnoreExtraProperties
import kotlinx.parcelize.Parcelize

@Parcelize
@IgnoreExtraProperties
data class ChatMessage(
    var Message: String? = "",
    var SenderID: String? = "",
    var Time: String? = "",
    var Date: String? = "",
    var ReceiverID: String? = ""
) : Parcelable {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ChatMessage

        if (Message != other.Message) return false
        if (SenderID != other.SenderID) return false
        if (Time != other.Time) return false
        if (Date != other.Date) return false
        if (ReceiverID != other.ReceiverID) return false

        return true
    }

    override fun hashCode(): Int {
        var result = Message?.hashCode() ?: 0
        result = 31 * result + (SenderID?.hashCode() ?: 0)
        result = 31 * result + (Time?.hashCode() ?: 0)
        result = 31 * result + (Date?.hashCode() ?: 0)
        result = 31 * result + (ReceiverID?.hashCode() ?: 0)
        return result
    }

    override fun toString(): String {
        return "ChatMessage(Message=$Message, SenderID=$SenderID, Time=$Time, Date=$Date, ReceiverID=$ReceiverID)"
    }
}