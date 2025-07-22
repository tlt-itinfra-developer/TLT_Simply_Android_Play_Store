package tlt.th.co.toyotaleasing.modules.selectphone

import android.os.Parcel
import android.os.Parcelable

data class PhoneNumber(
        val id: String,
        val phoneNumber: String
) : Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readString()!!,
            parcel.readString()!!) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(phoneNumber)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<PhoneNumber> {
        override fun createFromParcel(parcel: Parcel): PhoneNumber {
            return PhoneNumber(parcel)
        }

        override fun newArray(size: Int): Array<PhoneNumber?> {
            return arrayOfNulls(size)
        }
    }
}