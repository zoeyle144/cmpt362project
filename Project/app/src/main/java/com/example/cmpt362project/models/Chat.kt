package com.example.cmpt362project.models

import android.os.Parcel
import android.os.Parcelable

data class Chat (
    var chatId: String = "Default",
    var user1: String = "Default",
    var user2: String = "Default",
    var lastUpdateTimestamp: Long = 0) :Parcelable{

    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readLong()!!,
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) = with(parcel) {
        parcel.writeString(chatId)
        parcel.writeString(user1)
        parcel.writeString(user2)
        parcel.writeLong(lastUpdateTimestamp)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Chat> {
        override fun createFromParcel(parcel: Parcel): Chat {
            return Chat(parcel)
        }

        override fun newArray(size: Int): Array<Chat?> {
            return arrayOfNulls(size)
        }
    }

}