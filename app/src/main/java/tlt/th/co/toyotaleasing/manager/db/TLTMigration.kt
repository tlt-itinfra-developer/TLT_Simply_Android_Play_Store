//package tlt.th.co.toyotaleasing.manager.db
//
//import com.google.gson.annotations.SerializedName
//import io.realm.DynamicRealm
//import io.realm.FieldAttribute
//import io.realm.RealmMigration
//import io.realm.*
//
//class TLTMigration : RealmMigration {
//    override fun migrate(realm: DynamicRealm, oldVersion: Long, newVersion: Long) {
//        var oldVersion = oldVersion
//        // DynamicRealm exposes an editable schema
//        val schema = realm.schema
//        // Migrate to version 1: Add Field BANK_LINK to (masterdata > BankInfo)
//        //        Add Field paymentURL to (PaymentEntity)
//        if (oldVersion == 1L) {
//
//            schema.get("BankInfo")!!
//                    .addField("bANKLINK", String::class.java)
//            schema.get("PaymentEntity")!!
//                    .addField("paymentURL" ,  String::class.java, FieldAttribute.REQUIRED)
//            oldVersion++
//
//        }
//        if (oldVersion == 2L) {
//
//            schema.get("SummaryEntity")!!
//                    .addField("cStatus_code" ,  String::class.java, FieldAttribute.REQUIRED)
//            oldVersion++
//
//        }
//
//        if (oldVersion == 3L) {
//
//            schema.get("UserEntity")!!
//                    .addField("isShowAppPAPD" ,  Boolean::class.java, FieldAttribute.REQUIRED)
//            oldVersion++
//
//        }
//
//
//        if (oldVersion == 4L) {
//
//            schema.create("SideMenu")!!
//                    .addField("mENUEN" , String::class.java, FieldAttribute.REQUIRED)
//                    .addField("mENUICON" , String::class.java, FieldAttribute.REQUIRED)
//                    .addField("mENUID" , String::class.java, FieldAttribute.REQUIRED)
//                    .addField("mENULINK" , String::class.java, FieldAttribute.REQUIRED)
//                    .addField("mENUTH" , String::class.java, FieldAttribute.REQUIRED)
//
//            if ( schema.get("SideMenu") != null ) {
//                val bSchema = schema.get("SideMenu")
//                schema.get("MasterData")!!.addRealmListField("sideMenu", bSchema)
//            }
//
//            schema.create("PrivacyPolicy")!!
//                    .addField("pRIVACYPOLICY" , String::class.java)
//                    .addField("rECACTIVE" , String::class.java)
//
//            if ( schema.get("PrivacyPolicy") != null ) {
//                val bSchema = schema.get("PrivacyPolicy")
//                schema.get("MasterData")!!.addRealmListField("privacyPolicy", bSchema)
//            }
//
//            oldVersion++
//        }
//
//
//        if (oldVersion == 5L) {
//
//            schema.get("BankInfo")!!
//                    .addField("dESCRIPTION", String::class.java)
//                    .addField("cHANNEL", String::class.java)
//            schema.get("PaymentEntity")!!
//                    .addField("callbankDesc" ,  String::class.java, FieldAttribute.REQUIRED)
//            oldVersion++
//
//        }
//
//        if (oldVersion == 6L) {
//
//            schema.create("WebCarloan")!!
//                    .addField("cARLOANWEB", String::class.java)
//
//            if ( schema.get("WebCarloan") != null ) {
//                val bSchema = schema.get("WebCarloan")
//                schema.get("MasterData")!!.addRealmListField("webCarloan", bSchema)
//            }
//
////            schema.get("WebCarloan")!!
////                    .addField("cARLOANWEB", String::class.java)
//
//            schema.create("Marital")!!
//                    .addField("mARITALEN", String::class.java)
//                    .addField("mARITALID", String::class.java)
//                    .addField("mARITALTH", String::class.java)
//
//
//            if ( schema.get("Marital") != null ) {
//                val bSchema = schema.get("Marital")
//                schema.get("MasterData")!!.addRealmListField("marital", bSchema)
//            }
//
//            schema.create("Occupation")!!
//                    .addField("employeeTypeID", String::class.java)
//                    .addField("oCID", String::class.java)
//                    .addField("oCNAMEEN", String::class.java)
//                    .addField("oCNAMETH", String::class.java)
//
//            if ( schema.get("Occupation") != null ) {
//                val bSchema = schema.get("Occupation")
//                schema.get("MasterData")!!.addRealmListField("occupation", bSchema)
//            }
//
//            schema.create("Property")!!
//                    .addField("pROPERTYEN", String::class.java)
//                    .addField("pROPERTYID", String::class.java)
//                    .addField("pROPERTYTH", String::class.java)
//
//            if ( schema.get("Property") != null ) {
//                val bSchema = schema.get("Property")
//                schema.get("MasterData")!!.addRealmListField("property", bSchema)
//            }
//
//
//            schema.create("SyncImage")!!
//                    .addField("dOCDESCEN", String::class.java)
//                    .addField("dOCDESCTH", String::class.java)
//                    .addField("dOCREMARK", String::class.java)
//                    .addField("dOCTYPE", String::class.java)
//                    .addField("mAXUPLOAD", String::class.java)
//                    .addField("mINUPLOAD", String::class.java)
//
//            if ( schema.get("SyncImage") != null ) {
//                val bSchema = schema.get("SyncImage")
//                schema.get("MasterData")!!.addRealmListField("syncImage", bSchema)
//            }
//
//            schema.create("SubOccupation")!!
//                    .addField("oCID", String::class.java)
//                    .addField("sUBOCCODE", String::class.java)
//                    .addField("sUBOCNAMEEN", String::class.java)
//                    .addField("sUBOCNAMETH", String::class.java)
//
//            if ( schema.get("SubOccupation") != null ) {
//                val bSchema = schema.get("SubOccupation")
//                schema.get("MasterData")!!.addRealmListField("subOccupation", bSchema)
//            }
//
//            schema.create("PersonalDataEntity")!!
//                    .addField("maritalstatus", String::class.java, FieldAttribute.REQUIRED)
//                    .addField("maritalid", String::class.java , FieldAttribute.REQUIRED)
//                    .addField("realaddress", String::class.java, FieldAttribute.REQUIRED)
//                    .addField("propname", String::class.java, FieldAttribute.REQUIRED)
//                    .addField("propid", String::class.java, FieldAttribute.REQUIRED)
//                    .addField("livyear", String::class.java, FieldAttribute.REQUIRED)
//                    .addField("livmonth", String::class.java, FieldAttribute.REQUIRED)
//                    .addField("suboc", String::class.java, FieldAttribute.REQUIRED)
//                    .addField("subocid", String::class.java, FieldAttribute.REQUIRED)
//                    .addField("oc", String::class.java, FieldAttribute.REQUIRED)
//                    .addField("ocid", String::class.java , FieldAttribute.REQUIRED)
//                    .addField("empyear", String::class.java, FieldAttribute.REQUIRED)
//                    .addField("empmonth", String::class.java, FieldAttribute.REQUIRED)
//                    .addField("income", String::class.java, FieldAttribute.REQUIRED)
//                    .addField("otherincome", String::class.java, FieldAttribute.REQUIRED)
//                    .addField("postcode", String::class.java, FieldAttribute.REQUIRED)
//                    .addField("lat", String::class.java, FieldAttribute.REQUIRED)
//                    .addField("lng", String::class.java, FieldAttribute.REQUIRED)
//                    .addField("amphur", String::class.java, FieldAttribute.REQUIRED)
//                    .addField("amphurCode", String::class.java, FieldAttribute.REQUIRED)
//                    .addField("provice", String::class.java, FieldAttribute.REQUIRED)
//                    .addField("proviceCode", String::class.java, FieldAttribute.REQUIRED)
//
//            schema.create("VeriyConirmDataEntity")!!
//                    .addField("appointment", String::class.java, FieldAttribute.REQUIRED)
//                    .addField("dueDate", String::class.java, FieldAttribute.REQUIRED)
//                    .addField("showroomCode", String::class.java, FieldAttribute.REQUIRED)
//                    .addField("showroomName", String::class.java, FieldAttribute.REQUIRED)
//                    .addField("flagCurrent", Boolean::class.java, FieldAttribute.REQUIRED)
//                    .addField("Curr_aMPHUR", String::class.java, FieldAttribute.REQUIRED)
//                    .addField("Curr_aMPHURCODE", String::class.java, FieldAttribute.REQUIRED)
//                    .addField("Curr_pOSTCODE", String::class.java, FieldAttribute.REQUIRED)
//                    .addField("Curr_pROVINCE", String::class.java, FieldAttribute.REQUIRED)
//                    .addField("Curr_pROVINCECODE", String::class.java, FieldAttribute.REQUIRED)
//                    .addField("Curr_rEALADDRESS", String::class.java, FieldAttribute.REQUIRED)
//                    .addField("Curr_lat", String::class.java, FieldAttribute.REQUIRED)
//                    .addField("Curr_lng", String::class.java, FieldAttribute.REQUIRED)
//                    .addField("Regis_aMPHUR", String::class.java, FieldAttribute.REQUIRED)
//                    .addField("Regis_aMPHURCODE", String::class.java, FieldAttribute.REQUIRED)
//                    .addField("Regis_pOSTCODE", String::class.java, FieldAttribute.REQUIRED)
//                    .addField("Regis_pROVINCE", String::class.java, FieldAttribute.REQUIRED)
//                    .addField("Regis_pROVINCECODE", String::class.java, FieldAttribute.REQUIRED)
//                    .addField("Regis_rEALADDRESS", String::class.java, FieldAttribute.REQUIRED)
//                    .addField("Regis_lat", String::class.java, FieldAttribute.REQUIRED)
//                    .addField("Regis_lng", String::class.java, FieldAttribute.REQUIRED)
//                    .addField("Mailing_aMPHUR", String::class.java, FieldAttribute.REQUIRED)
//                    .addField("Mailing_aMPHURCODE", String::class.java, FieldAttribute.REQUIRED)
//                    .addField("Mailing_pOSTCODE", String::class.java, FieldAttribute.REQUIRED)
//                    .addField("Mailing_pROVINCE", String::class.java, FieldAttribute.REQUIRED)
//                    .addField("Mailing_pROVINCECODE", String::class.java, FieldAttribute.REQUIRED)
//                    .addField("Mailing_rEALADDRESS", String::class.java, FieldAttribute.REQUIRED)
//                    .addField("Mailing_lat", String::class.java, FieldAttribute.REQUIRED)
//                    .addField("Mailing_lng", String::class.java, FieldAttribute.REQUIRED)
//
//            schema.create("DealerOnline")!!
//                    .addField("tYPESYNC", String::class.java)
//                    .addField("dealerNostraId", String::class.java)
//                    .addField("dealerCode", String::class.java)
//                    .addField("name", String::class.java)
//                    .addField("nameEn", String::class.java)
//                    .addField("branchCode", String::class.java)
//                    .addField("branch", String::class.java)
//                    .addField("branchEn", String::class.java)
//                    .addField("aDDRESSTH", String::class.java)
//                    .addField("aDDRESSEN", String::class.java)
//                    .addField("telSsCall", String::class.java)
//                    .addField("telSs", String::class.java)
//                    .addField("telCc", String::class.java)
//                    .addField("telFax", String::class.java)
//                    .addField("email", String::class.java)
//                    .addField("url1", String::class.java)
//                    .addField("url2", String::class.java)
//                    .addField("typeOf", String::class.java)
//                    .addField("openHrTypeOf", String::class.java)
//                    .addField("openHrTypeOfEn", String::class.java)
//                    .addField("typeSr", String::class.java)
//                    .addField("openHrTypeSr", String::class.java)
//                    .addField("openHrTypeSrEn", String::class.java)
//                    .addField("typeSc", String::class.java)
//                    .addField("openHrTypeSc", String::class.java)
//                    .addField("openHrTypeScEn", String::class.java)
//                    .addField("typeBp", String::class.java)
//                    .addField("openHrTypeBp", String::class.java)
//                    .addField("openHrTypeBpEn", String::class.java)
//                    .addField("picUrl1", String::class.java)
//                    .addField("picUrl2", String::class.java)
//                    .addField("picUrl3", String::class.java)
//                    .addField("lat", String::class.java)
//                    .addField("lon", String::class.java)
//                    .addField("rECACTIVE", String::class.java)
//                    .addField("provinceCode", String::class.java)
//                    .addField("amphurCode", String::class.java)
//                    .addField("dealerCodeAmbit", String::class.java)
//                    .addField("showroomCodeAmbit", String::class.java)
//
//            if ( schema.get("DealerOnline") != null ) {
//                val bSchema = schema.get("DealerOnline")
//                schema.get("MasterData")!!.addRealmListField("dealerOnline", bSchema)
//            }
//
//
//            schema.create("LoanLocationDetailEntity")!!
//                    .addField("id", String::class.java, FieldAttribute.REQUIRED)
//                    .addField("company", String::class.java, FieldAttribute.REQUIRED)
//                    .addField("branch", String::class.java, FieldAttribute.REQUIRED)
//                    .addField("distance", String::class.java, FieldAttribute.REQUIRED)
//                    .addField("address", String::class.java, FieldAttribute.REQUIRED)
//                    .addField("tel", String::class.java, FieldAttribute.REQUIRED)
//                    .addField("telForCall", String::class.java, FieldAttribute.REQUIRED)
//                    .addField("time", String::class.java, FieldAttribute.REQUIRED)
//                    .addField("website", String::class.java, FieldAttribute.REQUIRED)
//                    .addField("lat", String::class.java, FieldAttribute.REQUIRED)
//                    .addField("lng", String::class.java, FieldAttribute.REQUIRED)
//
//            schema.create("LoanLocationFilterEntity")!!
//                    .addField("showroomName", String::class.java, FieldAttribute.REQUIRED)
//                    .addField("provinceIndex", Int::class.java, FieldAttribute.REQUIRED)
//                    .addField("amphurIndex", Int::class.java, FieldAttribute.REQUIRED)
//                    .addField("isNearly", Boolean::class.java, FieldAttribute.REQUIRED)
//                    .addField("dealerCode",  String::class.java, FieldAttribute.REQUIRED)
//                    .addField("showroomCode",  String::class.java, FieldAttribute.REQUIRED)
//
//            schema.create("FillInfoDataEntity")!!
//                    .addField("name" ,  String::class.java, FieldAttribute.REQUIRED)
//                    .addField("surname" ,  String::class.java, FieldAttribute.REQUIRED)
//                    .addField("aMPHUR", String::class.java, FieldAttribute.REQUIRED)
//                    .addField("aMPHURCODE", String::class.java, FieldAttribute.REQUIRED)
//                    .addField("lASERID", String::class.java, FieldAttribute.REQUIRED)
//                    .addField("hOUSEHOLDID", String::class.java, FieldAttribute.REQUIRED)
//                    .addField("pOSTCODE", String::class.java, FieldAttribute.REQUIRED)
//                    .addField("pROVINCECODE", String::class.java, FieldAttribute.REQUIRED)
//                    .addField("rEALADDRESS", String::class.java, FieldAttribute.REQUIRED)
//                    .addField("pROVINCE", String::class.java, FieldAttribute.REQUIRED)
//                    .addField("rEFID", String::class.java, FieldAttribute.REQUIRED)
//                    .addField("lng", String::class.java, FieldAttribute.REQUIRED)
//                    .addField("lat", String::class.java, FieldAttribute.REQUIRED)
//
//            schema.create("CacheEContractPDF")!!
//                    .addField("json", String::class.java)
//
//            schema.create("EContractPDFCacheEntity")!!
//                    .addField("json", String::class.java)
//
//            oldVersion++
//        }
//
//        if (oldVersion == 7L) {
//            schema.get("VeriyConirmDataEntity")!!
//                    .addField("flagRegisSameCurrent", Boolean::class.java, FieldAttribute.REQUIRED)
//                    .addField("flagMailingSameCurrent", Boolean::class.java, FieldAttribute.REQUIRED)
//            oldVersion++
//        }
//
//        if (oldVersion == 8L) {
//            schema.get("PersonalDataEntity")!!
//                    .addField("companyDetail", String::class.java, FieldAttribute.REQUIRED)
//            oldVersion++
//        }
//
//
//
//    }
//}
//
//
//
//
