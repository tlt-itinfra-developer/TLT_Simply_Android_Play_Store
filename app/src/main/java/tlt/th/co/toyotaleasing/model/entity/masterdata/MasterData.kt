package tlt.th.co.toyotaleasing.model.entity.masterdata

import com.google.gson.annotations.SerializedName


open class MasterData  {
    @SerializedName("Termscon")
    var termscon: Termscon? = null
    @SerializedName("Disclosure")
    var disclosure: Disclosure? = null
    @SerializedName("Advert")
    var advert: Advert? = null
    @SerializedName("Dealer_info")
    var dealerInfo: DealerInfo? = null
    @SerializedName("Contact_us")
    var contactUs: ContactUs? = null
    @SerializedName("Alert_wording")
    var alertWording: AlertWording? = null
    @SerializedName("Mailing_a")
    var mailingA: MailingA? = null
    @SerializedName("Ins_company")
    var insCompany: InsCompany? = null
    @SerializedName("Ins_policy")
    var insPolicy: InsPolicy? = null
    @SerializedName("Hotline")
    var hotline: Hotline? = null
    @SerializedName("TIB_club")
    var tIBClub: TIBClub? = null
    @SerializedName("TIB_question")
    var tIBQuestion: TIBQuestion? = null
    @SerializedName("Pay_methode")
    var payMethode: PayMethod? = null
    @SerializedName("Pay_his")
    var payHis: PayHis? = null
    @SerializedName("Pay_code")
    var payCode: PayCode? = null
    @SerializedName("Bank_info")
    var bankInfo: BankInfo? = null
    @SerializedName("Doc_load")
    var docLoad: DocLoad? = null
    @SerializedName("RefinanceImg")
    var refinanceImage: RefinanceImage? = null
    @SerializedName("Marital")
    var marital: Marital? = null
    @SerializedName("SyncImage")
    var syncImage: SyncImage? = null
    @SerializedName("Property")
    var property: Property? = null
    @SerializedName("Occupation")
    var occupation: Occupation? = null
    @SerializedName("SubOccupation")
    var subOccupation: SubOccupation? = null
    @SerializedName("Dealer_Online")
    var dealerOnline: DealerOnline? = null
    @SerializedName("Web_Online")
    var webCarloan: WebCarloan? = null
    @SerializedName("SideMenu")
    var sideMenu: SideMenu? = null
    @SerializedName("PrivacyPolicy")
    var privacyPolicy: PrivacyPolicy? = null

}