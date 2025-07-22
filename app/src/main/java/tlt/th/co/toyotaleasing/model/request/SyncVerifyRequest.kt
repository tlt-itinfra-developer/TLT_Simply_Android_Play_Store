package tlt.th.co.toyotaleasing.model.request


import com.google.gson.annotations.SerializedName

data class SyncVerifyRequest(
    @SerializedName("PKEY1")  var pKEY1: String? = "",
    @SerializedName("PKEY2")  var pKEY2: DealerCar = DealerCar()  ,
    @SerializedName("PKEY3")  var pKEY3: Address= Address()  ,
    @SerializedName("PKEY4") var pKEY4: Address = Address()  ,
    @SerializedName("PKEY5") var pKEY5: Address = Address()  ,
    @SerializedName("PSELECT") var pSELECT: String? = ""
){
    data class DealerCar(
            @SerializedName("PKEY1") var pKEY1: String = "",
            @SerializedName("PKEY2") var pKEY2: String = "",
            @SerializedName("PKEY3") var pKEY3: String = "",
            @SerializedName("PKEY4") var pKEY4: String = "",
            @SerializedName("PKEY5") var pKEY5: String = ""
    ) {
        companion object {
            fun build(showroomID: String = "",
                      showroom: String = "",
                      appointment: String = "",
                      duedate: String = ""): DealerCar {
                return DealerCar().apply {
                    pKEY1 = showroomID
                    pKEY2 = showroom
                    pKEY3 = appointment
                    pKEY4 = duedate
                }
            }
        }
    }

    data class Address(
            @SerializedName("PKEY1") var pKEY1: String = "",
            @SerializedName("PKEY2") var pKEY2: String = "",
            @SerializedName("PKEY3") var pKEY3: String = "",
            @SerializedName("PKEY4") var pKEY4: String = "",
            @SerializedName("PKEY5") var pKEY5: String = "",
            @SerializedName("PKEY6") var pKEY6: String = "",
            @SerializedName("PKEY7") var pKEY7: String = "" ,
            @SerializedName("PKEY8") var pKEY8: String = "",
            @SerializedName("PKEY9") var pKEY9: String = "",
            @SerializedName("PKEY10") var pKEY10: String = ""
    ) {
        companion object {
            fun build(address: String = "",
                      district: String = "",
                      districtCode : String = "",
                      amphur : String = "",
                      amphurCode : String = "",
                      province: String = "",
                      provinceCode: String = "",
                      postcode: String = "",
                      lat: String = "",
                      lng: String = ""): Address {
                return Address().apply {
                    pKEY1 = address
                    pKEY2 = district
                    pKEY3 = districtCode
                    pKEY4 = amphur
                    pKEY5 = amphurCode
                    pKEY6 = province
                    pKEY7 = provinceCode
                    pKEY8 = postcode
                    pKEY9 = lat
                    pKEY10 = lng
                }
            }
        }
    }

    companion object {
        fun build(refNo : String ,
                  dealerCar: DealerCar,
                  current: Address  ,
                  regis: Address ,
                  mailing: Address ): SyncVerifyRequest {
            return SyncVerifyRequest().apply {
                pKEY2 = dealerCar
                pKEY3 = current
                pKEY4 = regis
                pKEY5 = mailing
                pKEY1 = refNo
            }
        }
    }

}
