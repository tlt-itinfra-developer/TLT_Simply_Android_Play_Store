package tlt.th.co.toyotaleasing.model.request

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

 class CheckedAnnoucementRequest {
         @SerializedName("PKEY1")
         @Expose
         var pKEY1: String = ""
         @SerializedName("PKEY2")
         @Expose
         var pKEY2: String = ""
         @SerializedName("PKEY3")
         @Expose
         var pKEY3: String = ""
         @SerializedName("PSELECT")
         @Expose
         var pSELECT: String = ""

         companion object {
                 fun build(admID: String): CheckedAnnoucementRequest {
                         return CheckedAnnoucementRequest().apply {
                                 pSELECT = admID
                         }
                 }
         }

 }


