package tlt.th.co.toyotaleasing.model.response


import com.google.gson.annotations.SerializedName

data class GetVerifyResponse(
    @SerializedName("ADDRESS")
    var address: Address,
    @SerializedName("DELIVERCAR")
    var delivercar: Delivercar,
    @SerializedName("MCI")
    var mCI: MCI,
    @SerializedName("MAILING")
    var mailing: Mailing,
    @SerializedName("MAIN")
    var main: Main,
    @SerializedName("REGIS")
    var regis: Regis

) {

    data class Delivercar(
        @SerializedName("APPOINTMENT")
        var appointment: String = "",
        @SerializedName("DUE_DATE")
        var dueDate: String = "",
        @SerializedName("OFFER_SHOWROOM_CODE")
        var showroomCode: String = "",
        @SerializedName("SHOWROOM_NAME")
        var showroomName: String,
        @SerializedName("OFFER_DEALERCODE")
        var offer_dealercode: String
    )

    data class MCI(
        @SerializedName("CAR_PRICE")
        var carPrice: String = "",
        @SerializedName("DOWN_PAYMENT")
        var downPayment: String = "",
        @SerializedName("GRADE")
        var grade: String = "",
        @SerializedName("INSTALLMENT")
        var installment: String = "",
        @SerializedName("INTERESTS")
        var interests: String = "",
        @SerializedName("MODEL")
        var model: String = "",
        @SerializedName("PAYMENT_TERM")
        var paymentTerms: String,
        @SerializedName("CONTRACT_HANDLING_FEE")
        var contractHandlingFee: String,
        @SerializedName("OTHER_EXPENSE")
        var otherexpense: String
    )


    data class Main(
        @SerializedName("LOAN_NO")
        var loanNo: String = "",
        @SerializedName("NAME")
        var name: String = "",
        @SerializedName("STATUS_APP")
        var statusApp: String
    )


    data class Regis(
        @SerializedName("AMPHUR_CODE")
        var aMPHURCODE: String = "",
        @SerializedName("AMPHUR")
        var aMPHUR: String = "",
        @SerializedName("POSTCODE")
        var pOSTCODE: String = "",
        @SerializedName("PROVINCE_CODE")
        var pROVINCECODE: String = "",
        @SerializedName("PROVINCE")
        var pROVINCE: String = "",
        @SerializedName("REAL_ADDRESS")
        var rEALADDRESS: String = "" ,
        @SerializedName("PLAT")
        var pLat: String = "",
        @SerializedName("PLONG")
        var pLong: String = ""
    )

    data class Address(
            @SerializedName("AMPHUR_CODE")
            var aMPHURCODE: String = "",
            @SerializedName("AMPHUR")
            var aMPHUR: String = "",
            @SerializedName("POSTCODE")
            var pOSTCODE: String = "",
            @SerializedName("PROVINCE_CODE")
            var pROVINCECODE: String = "",
            @SerializedName("PROVINCE")
            var pROVINCE: String = "",
            @SerializedName("REAL_ADDRESS")
            var rEALADDRESS: String = "",
            @SerializedName("PLAT")
            var pLat: String = "",
            @SerializedName("PLONG")
            var pLong: String = ""
    )


    data class Mailing(
            @SerializedName("AMPHUR_CODE")
            var aMPHURCODE: String = "",
            @SerializedName("AMPHUR")
            var aMPHUR: String = "",
            @SerializedName("POSTCODE")
            var pOSTCODE: String = "",
            @SerializedName("PROVINCE_CODE")
            var pROVINCECODE: String = "",
            @SerializedName("PROVINCE")
            var pROVINCE: String = "",
            @SerializedName("REAL_ADDRESS")
            var rEALADDRESS: String = "",
            @SerializedName("PLAT")
            var pLat: String = "",
            @SerializedName("PLONG")
            var pLong: String = ""
    )
}