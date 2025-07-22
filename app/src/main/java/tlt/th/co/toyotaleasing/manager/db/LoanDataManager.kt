package tlt.th.co.toyotaleasing.model.entity



import tlt.th.co.toyotaleasing.modules.custloan.loanmodule.basicdetail.common.FillInfoData
import tlt.th.co.toyotaleasing.modules.custloan.loanmodule.carloan.common.PersonalData
import tlt.th.co.toyotaleasing.modules.custloan.loanmodule.cstatus.common.VerifyConfirmData

object LoanDataManager {

    private const val PERSONAL_STATE = "personaldata"

    private val databaseManager = null

    fun savePersonalList(list: List<PersonalData>) {

        val items = list.map {
            PersonalDataEntity(
                    maritalstatus = it.marital_status ,
                    maritalid =  it.marital_id ,
                    postcode =  it.postcode ,
                    realaddress =  it.real_address ,
                    propname =   it.prop_name ,
                    propid =  it.prop_id ,
                    livyear =  it.liv_year ,
                    livmonth =  it.liv_month ,
                    suboc =  it.sub_oc ,
                    subocid =  it.sub_oc_id ,
                    empyear =  it.emp_year ,
                    empmonth  =  it.emp_month ,
                    income =  it.income ,
                    otherincome =  it.other_income ,
                    amphur = it.aumphur ,
                    amphurCode = it.aumphur_code ,
                    provice = it.province ,
                    proviceCode = it.province_code ,
                    companyDetail = it.company_detail
            )
        }

    }

    fun saveRealAddress(addr : String , postcode : String , lat : String , lng : String ) {

    }

    fun saveLoanVerifyConfirmList(data: VerifyConfirmData ) {

        val list = listOf(data)
        val items =
               list.map{ VeriyConirmDataEntity(
                    appointment = it.appointment ,
                    dueDate = it.dueDate ,
                    showroomCode = it.showroomCode,
                    showroomName = it.showroomName ,
                    flagCurrent = it.flagCurrent ,
                    Curr_aMPHUR = it.Curr_aMPHUR ,
                    Curr_aMPHURCODE = it.Curr_aMPHURCODE ,
                    Curr_pROVINCE = it.Curr_pROVINCE   ,
                    Curr_pROVINCECODE = it.Curr_pROVINCECODE   ,
                    Curr_pOSTCODE = it.Curr_pOSTCODE   ,
                    Curr_rEALADDRESS = it.Curr_rEALADDRESS ,
                    Curr_lat = it.Curr_lat   ,
                    Curr_lng = it.Curr_lng ,
                    Regis_aMPHUR = it.Regis_aMPHUR  ,
                    Regis_aMPHURCODE = it.Regis_aMPHURCODE   ,
                    Regis_pOSTCODE = it.Regis_pOSTCODE   ,
                    Regis_pROVINCE = it.Regis_pROVINCE ,
                    Regis_pROVINCECODE = it.Regis_pROVINCECODE  ,
                    Regis_rEALADDRESS =  it.Regis_rEALADDRESS ,
                    Regis_lat = it.Regis_lat   ,
                    Regis_lng = it.Regis_lng ,
                    Mailing_aMPHUR = it.Regis_aMPHUR  ,
                    Mailing_aMPHURCODE = it.Regis_aMPHURCODE   ,
                    Mailing_pOSTCODE = it.Mailing_pOSTCODE  ,
                    Mailing_pROVINCE= it.Mailing_pROVINCE   ,
                    Mailing_pROVINCECODE = it.Mailing_pROVINCECODE   ,
                    Mailing_rEALADDRESS = it.Mailing_rEALADDRESS ,
                    Mailing_lat = it.Mailing_lat   ,
                    Mailing_lng = it.Mailing_lng
                    )
               }

    }

    fun saveFillInfoList(list: List<FillInfoDataEntity>) {

        val items = list.map {
            FillInfoDataEntity(
                    name = it.name ,
                    surname = it.surname ,
                    aMPHUR =   it.aMPHUR,
                    aMPHURCODE =   it.aMPHURCODE,
                    lASERID = it.lASERID,
                    hOUSEHOLDID =  it.hOUSEHOLDID,
                    pOSTCODE =   it.pOSTCODE,
                    pROVINCE =   it.pROVINCE,
                    pROVINCECODE =  it.pROVINCECODE,
                    rEALADDRESS =   it.rEALADDRESS,
                    rEFID =   it.rEFID,
                    lng  =   it.lng,
                    lat  = it.lat
            )
        }

    }

    fun saveVerConfirmRealAddress(addr : String , postcode : String  , lat : String , lng : String , sub_type : String  ) {
    }


    fun saveBasicRealAddress(addr : String , postcode: String ,  lat : String , lng : String ) {

    }
}
