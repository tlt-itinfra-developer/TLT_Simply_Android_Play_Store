package tlt.th.co.toyotaleasing.model.response

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.Gson

class NotificationJsonResponse(
        var title: String = "",
        var message: String = "",
        var imageUrl: String = "",
        var flagCheckBox: String = "Y",
        var notifyType: String = "",
        var sequenceId: String = ""
) : Parcelable {

    constructor(parcel: Parcel) : this(
            parcel.readString()!!,
            parcel.readString()!!,
            parcel.readString()!!,
            parcel.readString()!!,
            parcel.readString()!!,
            parcel.readString()!!) {
    }

    fun toJsonString(): String {
        return Gson().toJson(this)
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(title)
        parcel.writeString(message)
        parcel.writeString(imageUrl)
        parcel.writeString(flagCheckBox)
        parcel.writeString(notifyType)
        parcel.writeString(sequenceId)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<NotificationJsonResponse> {
        override fun createFromParcel(parcel: Parcel): NotificationJsonResponse {
            return NotificationJsonResponse(parcel)
        }

        override fun newArray(size: Int): Array<NotificationJsonResponse?> {
            return arrayOfNulls(size)
        }
    }
}