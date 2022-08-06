package com.example.cmpt362project.models

import com.google.firebase.database.IgnoreExtraProperties
import android.os.Parcel
import android.os.Parcelable

@IgnoreExtraProperties
data class ChangeNotification(
    val changedBy: String = "",
    val changeType: String = "",
    val changedItemName: String = "",
): Parcelable {

    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) = with(parcel) {
        parcel.writeString(changedBy)
        parcel.writeString(changeType)
        parcel.writeString(changedItemName)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ChangeNotification> {
        override fun createFromParcel(parcel: Parcel): ChangeNotification {
            return ChangeNotification(parcel)
        }

        override fun newArray(size: Int): Array<ChangeNotification?> {
            return arrayOfNulls(size)
        }
    }
}
