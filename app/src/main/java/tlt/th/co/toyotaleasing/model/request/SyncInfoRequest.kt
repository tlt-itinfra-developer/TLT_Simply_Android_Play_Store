package tlt.th.co.toyotaleasing.model.request


import com.google.gson.annotations.SerializedName

class SyncInfoRequest{
    @SerializedName("PKEY1")
    var pKEY1: String = ""
    @SerializedName("PKEY10")
    var pKEY10: String = ""
    @SerializedName("PKEY11")
    var pKEY11: String = ""
    @SerializedName("PKEY12")
    var pKEY12: String = ""
    @SerializedName("PKEY13")
    var pKEY13: String = ""
    @SerializedName("PKEY14")
    var pKEY14: String = ""
    @SerializedName("PKEY15")
    var pKEY15: String = ""
    @SerializedName("PKEY16")
    var pKEY16: String = ""
    @SerializedName("PKEY2")
    var pKEY2: String = ""
    @SerializedName("PKEY3")
    var pKEY3: String = ""
    @SerializedName("PKEY4")
    var pKEY4: String = ""
    @SerializedName("PKEY5")
    var pKEY5: String = ""
    @SerializedName("PKEY6")
    var pKEY6: String = ""
    @SerializedName("PKEY7")
    var pKEY7: String = ""
    @SerializedName("PKEY8")
    var pKEY8: String = ""
    @SerializedName("PKEY9")
    var pKEY9 : String = ""

    companion object {
    fun build(refID: String ,
                laserID : String ,
                householdID : String ,
                address : String ,
                postcode : String ,
                lat : String ,
                long : String ,
                amphur : String ,
                amphurCode : String  ,
                province : String ,
                provinceCode : String ,
                name : String ,
                surname : String ): SyncInfoRequest {
        return SyncInfoRequest().apply {
            pKEY2 = refID
            pKEY1 = laserID
            pKEY3 = address
            pKEY4 =  amphur
            pKEY5 = amphurCode
            pKEY6 =  province
            pKEY7 =  provinceCode
            pKEY8 =  postcode
            pKEY9 = lat
            pKEY10 = long
            pKEY11 = householdID
            pKEY12 = name
            pKEY13 = surname
        }
    }
}
}