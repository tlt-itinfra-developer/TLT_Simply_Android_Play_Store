package tlt.th.co.toyotaleasing.manager.db

import tlt.th.co.toyotaleasing.manager.LocalizeManager
import tlt.th.co.toyotaleasing.model.entity.masterdata.*
import tlt.th.co.toyotaleasing.model.response.DocSumUploadedResponse
import tlt.th.co.toyotaleasing.util.CalendarUtils
import java.util.*

class MasterDataManager {

    fun updateVersion(version: String = "") {
        //DatabaseManager.getInstance().saveMasterDataVersion(version)
    }

    fun getCurrentVersion() = DatabaseManager.getInstance().getMasterDataVersion()

    fun getTermAndConditionList() = null

    fun getDisclosureList() = null

    fun getProvinceList() = DatabaseManager.getInstance().getProvinceList()!!.sortedBy {
         if (LocalizeManager.isThai()) {
             it.pROVINCENAMETH
        } else {
             it.pROVINCENAMEEN
        }
    }

    fun getAmphurByProvinceCode(provinceCode: String) = DatabaseManager.getInstance()
            .getAmphurListByProvinceCode(provinceCode)!!
            .sortedBy {
                if (LocalizeManager.isThai()) {
                    it.aMPHURNAMETH
                } else {
                    it.aMPHURNAMEEN
                }
            }

    /// Get ProvinceCode & AmphurCode by Postcode

    fun getAmphurProvince(postcode : String) = DatabaseManager.getInstance().getAmphurProvinceList(postcode = postcode )!!.sortedBy {
        if (LocalizeManager.isThai()) {
            it.aMPHURNAMETH
        } else {
            it.aMPHURNAMEEN
        }
    }



    fun getInsuranceCompanyList() = null

    fun getInsurancePolicyList() = null

    fun getPaymentMethodList() = null

    fun getTIBClub() = null

    fun getBankList() = null


    fun getBankNameByBankCode(bCode: String): String {
        return ""
    }

    fun getRefinanceImage() = null

    fun getTIBQuestionList() = null

    fun getHotlineList() = null

    fun getPayCodeList() = null

    fun getHeadquaterList() = null

    fun getDealerList() = null

    fun getDealerOfferCode(code :String) = null

    fun getFAQTypeList() = null

    fun getFAQListByType(type: String) = null

    fun getDocumentDownloadList() = null

    fun getQuestionAndAnswerTopics() = null

    fun getInsurancePolicies() = null


    fun getSideMenu() = null

    fun getPrivacy() = null

    fun getPayHistoryList(): List<String> {
        return listOf()
    }

    /*fun getDealerNameByCode(code: String): List<DealerInfo> {
        return DatabaseManager.getInstance().findAllBy(DealerInfo::class.java)
                ?.filter {
                    it.dealerCode == code
                } ?: getDealerNameByName("gg")
    }

    fun getDealerNameByName(name: String): List<DealerInfo> {

    }*/



    // For Online App

    fun getWebCarloanList() = null
    fun getSyncDocTypeList(): List<SyncImage> {
        return listOf(
            SyncImage().apply {
                dOCTYPE = "BANK"
                dOCDESCTH = "สมุดบัญชีธนาคาร"
                dOCDESCEN = "Bank Book"
                mAXUPLOAD = "9"
                mINUPLOAD = "0"
                dOCREMARK = "IMG"
            },
            SyncImage().apply {
                dOCTYPE = "HOME"
                dOCDESCTH = "สำเนาทะเบียนบ้าน"
                dOCDESCEN = "House Registration"
                mAXUPLOAD = "9"
                mINUPLOAD = "0"
                dOCREMARK = "IMG"
            },
            SyncImage().apply {
                dOCTYPE = "INCOME"
                dOCDESCTH = "เอกสารแสดงรายได้"
                dOCDESCEN = "Income Document"
                mAXUPLOAD = "9"
                mINUPLOAD = "0"
                dOCREMARK = "IMG"
            },
            SyncImage().apply {
                dOCTYPE = "OTHER"
                dOCDESCTH = "เอกสารอื่นๆ"
                dOCDESCEN = "Other Documents"
                mAXUPLOAD = "9"
                mINUPLOAD = "0"
                dOCREMARK = "IMG"
            },
            SyncImage().apply {
                dOCTYPE = "REGISTRATION"
                dOCDESCTH = "สำเนาทะเบียนรถ/เล่มทะเบียน"
                dOCDESCEN = "Vehicle Registration"
                mAXUPLOAD = "9"
                mINUPLOAD = "0"
                dOCREMARK = "IMG"
            }
        )
    }

    fun getSyncDocTypeListV2(): List<SyncImage> {
        return listOf(
            SyncImage().apply {
                dOCTYPE = "BANK"
                dOCDESCTH = "สมุดบัญชีธนาคาร"
                dOCDESCEN = "Bank Book"
                mAXUPLOAD = "3"
                mINUPLOAD = "0"
                dOCREMARK = "PDF"
            },
            SyncImage().apply {
                dOCTYPE = "BANK"
                dOCDESCTH = "สมุดบัญชีธนาคาร"
                dOCDESCEN = "Bank Book"
                mAXUPLOAD = "9"
                mINUPLOAD = "1"
                dOCREMARK = "IMG"
            },
            SyncImage().apply {
                dOCTYPE = "HOME"
                dOCDESCTH = "สำเนาทะเบียนบ้าน"
                dOCDESCEN = "House Registration"
                mAXUPLOAD = "3"
                mINUPLOAD = "0"
                dOCREMARK = "PDF"
            },
            SyncImage().apply {
                dOCTYPE = "HOME"
                dOCDESCTH = "สำเนาทะเบียนบ้าน"
                dOCDESCEN = "House Registration"
                mAXUPLOAD = "9"
                mINUPLOAD = "0"
                dOCREMARK = "IMG"
            },
            SyncImage().apply {
                dOCTYPE = "INCOME"
                dOCDESCTH = "เอกสารแสดงรายได้"
                dOCDESCEN = "Income Document"
                mAXUPLOAD = "3"
                mINUPLOAD = "0"
                dOCREMARK = "PDF"
            },
            SyncImage().apply {
                dOCTYPE = "INCOME"
                dOCDESCTH = "เอกสารแสดงรายได้"
                dOCDESCEN = "Income Document"
                mAXUPLOAD = "9"
                mINUPLOAD = "1"
                dOCREMARK = "IMG"
            },
            SyncImage().apply {
                dOCTYPE = "OTHER"
                dOCDESCTH = "เอกสารอื่นๆ"
                dOCDESCEN = "Other Documents"
                mAXUPLOAD = "11"
                mINUPLOAD = "0"
                dOCREMARK = "IMG"
            },
            SyncImage().apply {
                dOCTYPE = "REGISTRATION"
                dOCDESCTH = "สำเนาทะเบียนรถ/เล่มทะเบียน"
                dOCDESCEN = "Vehicle Registration"
                mAXUPLOAD = "9"
                mINUPLOAD = "0"
                dOCREMARK = "IMG"
            }
        )
    }




    fun getMaritalList() = null
    fun getOccupationList() = null
    fun getPropertyList() = null
    fun getSubOccupationList() = null
    fun getDealerOnlineList() = null


    fun getAlertHintInfo() = null

    fun getWebCarLoan() = null

    companion object {
        private val masterDataManager = MasterDataManager()

        fun getInstance() = masterDataManager
    }
}