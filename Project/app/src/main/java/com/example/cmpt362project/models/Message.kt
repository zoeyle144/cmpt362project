package com.example.cmpt362project.models

import android.os.Parcel
import android.os.Parcelable

data class Message (
    var chatId: String = "Default",
    var sender: String = "Default",
    var senderUsername: String = "Default",
    var message: String = "Default",
    var timestamp: Long = 0L): Parcelable{

    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readLong()!!,
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) = with(parcel) {
        parcel.writeString(chatId)
        parcel.writeString(sender)
        parcel.writeString(senderUsername)
        parcel.writeString(message)
        parcel.writeLong(timestamp)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Message> {
        override fun createFromParcel(parcel: Parcel): Message {
            return Message(parcel)
        }

        override fun newArray(size: Int): Array<Message?> {
            return arrayOfNulls(size)
        }
    }

}