package tlt.th.co.toyotaleasing.model.request


import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

 class SyncPersonalRequest{
    @SerializedName("PKEY1")
    @Expose
    var pKEY1: String  = ""
    @SerializedName("PKEY10")
    @Expose
    var pKEY10: String  = ""
    @SerializedName("PKEY11")
    @Expose
    var pKEY11: String  = ""
    @SerializedName("PKEY12")
    @Expose
    var pKEY12: String  = ""
    @SerializedName("PKEY13")
    @Expose
    var pKEY13: String  = ""
    @SerializedName("PKEY14")
    @Expose
    var pKEY14: String  = ""
    @SerializedName("PKEY15")
    @Expose
    var pKEY15: String  = ""
    @SerializedName("PKEY16")
    @Expose
    var pKEY16: String  = ""
    @SerializedName("PKEY17")
    @Expose
    var pKEY17: String  = ""
    @SerializedName("PKEY18")
    @Expose
    var pKEY18: String  = ""
     @SerializedName("PKEY19")
     @Expose
     var pKEY19: String  = ""
     @SerializedName("PKEY20")
     @Expose
     var pKEY20: String  = ""
     @SerializedName("PKEY21")
     @Expose
     var pKEY21: String  = ""
     @SerializedName("PKEY22")
     @Expose
     var pKEY22: String  = ""
     @SerializedName("PKEY23")
     @Expose
     var pKEY23: String  = ""
    @SerializedName("PKEY2")
    @Expose
    var pKEY2: String  = ""
    @SerializedName("PKEY3")
    @Expose
    var pKEY3: String  = ""
    @SerializedName("PKEY4")
    @Expose
    var pKEY4: String  = ""
    @SerializedName("PKEY5")
    @Expose
    var pKEY5: String  = ""
    @SerializedName("PKEY6")
    @Expose
    var pKEY6: String  = ""
    @SerializedName("PKEY7")
    @Expose
    var pKEY7: String  = ""
    @SerializedName("PKEY8")
    @Expose
    var pKEY8: String  = ""
    @SerializedName("PKEY9")
    @Expose
    var pKEY9: String  = ""
    @SerializedName("PSELECT")
    @Expose
    var pSELECT: String  = ""

    companion object {
     fun build(ref_no: String ,
               marital_status : String ,
               marital_id : String ,
               prop_type : String ,
               prop_id : String ,
               living_year: String ,
               living_month: String ,
               sub_occupation : String ,
               sub_occupation_id: String ,
               emp_year : String ,
               emp_month : String ,
               income : String ,
               outcome : String ,
               address : String ,
               postcode : String ,
               lat : String ,
               long : String ,
               amphur : String ,
               amphurCode : String  ,
               province : String ,
               provinceCode : String ,
               company_detail : String
     ): SyncPersonalRequest {
         return SyncPersonalRequest().apply {
             pKEY2 = ref_no
             pKEY3 = marital_status
             pKEY4 = marital_id
             pKEY5 = address
             pKEY6 = amphur
             pKEY7 = amphurCode
             pKEY8 = province
             pKEY9 = provinceCode
             pKEY10 = postcode
             pKEY11 = prop_type
             pKEY12 = prop_id
             pKEY13 = living_year
             pKEY14 = living_month
             pKEY15 = sub_occupation
             pKEY16 = sub_occupation_id
             pKEY17 = emp_year
             pKEY18 = emp_month
             pKEY19 = income
             pKEY20 = outcome
             pKEY21 = lat
             pKEY22 = long
             pKEY23 = company_detail
         }
     }
    }
}