package com.example.cmpt362project.models

import android.os.Parcel
import android.os.Parcelable

data class GroupChat (
    var chatId: String = "Default",
    var groupId: String = "Default",
    var lastUpdateTimestamp: Long = 0) :Parcelable{

    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readLong()!!,
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) = with(parcel) {
        parcel.writeString(chatId)
        parcel.writeString(groupId)
        parcel.writeLong(lastUpdateTimestamp)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<GroupChat> {
        override fun createFromParcel(parcel: Parcel): GroupChat {
            return GroupChat(parcel)
        }

        override fun newArray(size: Int): Array<GroupChat?> {
            return arrayOfNulls(size)
        }
    }

}