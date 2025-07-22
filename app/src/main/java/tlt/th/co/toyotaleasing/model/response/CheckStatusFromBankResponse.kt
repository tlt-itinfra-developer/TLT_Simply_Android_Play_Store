package tlt.th.co.toyotaleasing.model.response

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import tlt.th.co.toyotaleasing.manager.LocalizeManager

class CheckStatusFromBankResponse(
        @SerializedName("PP_ID") var ppId: String? = "",
        @SerializedName("FLAG_PPSTATUS") var flagPpStatus: String? = "",
        @SerializedName("TYPE") var type: String? = "",
        @SerializedName("BANK_CODE") var bankCode: String? = "",
        @SerializedName("BANK_NAME_TH") var bankNameTh: String? = "",
        @SerializedName("BANK_NAME_EN") var bankNameEn: String? = "",
        @SerializedName("EXT_CONTRACT") var extContract: String? = "",
        @SerializedName("C_REG_NO") var cRegNo: String? = "",
        @SerializedName("AMOUNT") var amount: String? = "",
        @SerializedName("CUST_NAME") var customerName: String? = "",
        @SerializedName("CURRENT_DATE") var currentDate: String? = ""
) : Parcelable {

    constructor(parcel: Parcel) : this(
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString()) {
    }

    fun getBankName() = if (LocalizeManager.isThai()) {
        bankNameTh ?: ""
    } else {
        bankNameEn ?: ""
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(ppId)
        parcel.writeString(flagPpStatus)
        parcel.writeString(type)
        parcel.writeString(bankCode)
        parcel.writeString(bankNameTh)
        parcel.writeString(bankNameEn)
        parcel.writeString(extContract)
        parcel.writeString(cRegNo)
        parcel.writeString(amount)
        parcel.writeString(customerName)
        parcel.writeString(currentDate)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<CheckStatusFromBankResponse> {
        override fun createFromParcel(parcel: Parcel): CheckStatusFromBankResponse {
            return CheckStatusFromBankResponse(parcel)
        }

        override fun newArray(size: Int): Array<CheckStatusFromBankResponse?> {
            return arrayOfNulls(size)
        }
    }

}