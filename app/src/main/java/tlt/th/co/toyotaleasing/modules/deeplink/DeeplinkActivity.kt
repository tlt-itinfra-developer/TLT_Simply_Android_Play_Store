package tlt.th.co.toyotaleasing.modules.deeplink

import android.os.Bundle
import com.google.gson.Gson
import tlt.th.co.toyotaleasing.common.base.BaseActivity
import tlt.th.co.toyotaleasing.common.extension.getHostByDeeplink
import tlt.th.co.toyotaleasing.common.extension.getQueryParameterByDeeplink

import tlt.th.co.toyotaleasing.manager.db.UserManager
import tlt.th.co.toyotaleasing.model.response.NotificationJsonResponse
import tlt.th.co.toyotaleasing.modules.contract.ContractDetailActivity
import tlt.th.co.toyotaleasing.modules.custloan.loanmodule.center.CarLoanFragment
import tlt.th.co.toyotaleasing.modules.customer.MyCarFragment
import tlt.th.co.toyotaleasing.modules.faq.main.FAQFragment
import tlt.th.co.toyotaleasing.modules.home.customer.MainCustomerActivity
import tlt.th.co.toyotaleasing.modules.home.noncustomer.MainNonCustomerActivity
import tlt.th.co.toyotaleasing.modules.installment.CarInstallmentFragment
import tlt.th.co.toyotaleasing.modules.insurance.InsuranceFragment
import tlt.th.co.toyotaleasing.modules.insurance.tibclub.TIBClubActivity
import tlt.th.co.toyotaleasing.modules.noncustomer.RegisterFragment
import tlt.th.co.toyotaleasing.modules.tax.TaxFragment

class DeeplinkActivity : BaseActivity() {

    private val USER_PAGE = "user"
    private val FAQ_PAGE = "faq"
    private val MYCAR_PAGE = "my_car"
    private val TAX_PAGE = "car_tax"
    private val INSURANCE_PAGE = "car_insurance"
    private val TIB_PAGE = "tib_club_detail"
    private val CAR_LOAN = "tltsimply.page.link"
    private val ENGINE_RESULT = "car_loan"
    private val ETC = "etc"

    private val NAV_PARAM = "nav"
    private val ACTION_PARAM = "action"
    private val CONTRACT_NO_PARAM = "contract_no"
    private val TIB_PARAM = "tib_code"
    private val JSON_STRING = "jsonString"
    private val UNIQUE_ID = "unique_id"
    private val REF_ID = "ref_id"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val host = getHostByDeeplink()

        // Check Schema for Online App
//        if(!host.equals(CAR_LOAN)) {
//
//            UserManager.getInstance().minusUnreadNotification()
//
//        }

        when (host) {
            FAQ_PAGE -> {
                val jsonString = getQueryParameterByDeeplink(JSON_STRING)
                val item = Gson().fromJson(jsonString, NotificationJsonResponse::class.java)
                val nav = getQueryParameterByDeeplink(NAV_PARAM) ?: ""

                FAQFragment.startByDeeplink(
                        context = this@DeeplinkActivity,
                        topic = nav,
                        item = item,
                        isShowDialog = !isAppForegroundState()
                )
            }
            MYCAR_PAGE -> {
                val nav = getQueryParameterByDeeplink(NAV_PARAM) ?: ""
                val action = getQueryParameterByDeeplink(ACTION_PARAM) ?: ""
                val contractNo = getQueryParameterByDeeplink(CONTRACT_NO_PARAM) ?: ""
                val jsonString = getQueryParameterByDeeplink(JSON_STRING)
                val item = Gson().fromJson(jsonString, NotificationJsonResponse::class.java)

                if (nav == "contract" && action == "refinance") {

                    if (isAppForegroundState()) {
                        ContractDetailActivity.start(
                                context = this,
                                contractNumber = contractNo,
                                position = ContractDetailActivity.REFINANCE_POSITION
                        )
                        finish()
                    } else {
                        CarInstallmentFragment.startByDeeplinkRefinanceDialog(
                                context = this@DeeplinkActivity,
                                item = item,
                                contractNo = contractNo
                        )
                    }

                } else {
                    MyCarFragment.startByDeeplink(
                            context = this@DeeplinkActivity,
                            nav = nav,
                            action = action,
                            contractNo = contractNo
                    )
                    finish()
                }
            }
            TAX_PAGE -> {
                //DatabaseManager.getInstance().updateNotifyDeeplinkState(isNotifyDeeplinkState = true)
                val contractNo = getQueryParameterByDeeplink(CONTRACT_NO_PARAM) ?: ""

                TaxFragment.startByDeeplink(
                        context = this@DeeplinkActivity,
                        contractNo = contractNo
                )
                finish()
            }
            INSURANCE_PAGE -> {
                //DatabaseManager.getInstance().updateNotifyDeeplinkState(isNotifyDeeplinkState = true)
                val contractNo = getQueryParameterByDeeplink(CONTRACT_NO_PARAM) ?: ""

                InsuranceFragment.startByDeeplink(
                        context = this@DeeplinkActivity,
                        contractNo = contractNo
                )

                if (isAppForegroundState()) {
                    finish()
                    return
                }
                finish()
            }
            TIB_PAGE -> {
                val tibcode = getQueryParameterByDeeplink(TIB_PARAM) ?: ""

                if (isAppForegroundState()) {
                    TIBClubActivity.startByDeeplink(
                            context = this@DeeplinkActivity,
                            tibCode = tibcode
                    )
                    finish()
                    return
                } else {
                    if (UserManager.getInstance().isCustomer()) {
                        MainCustomerActivity.startWithClearStackByDeeplink(
                                context = this,
                                position = MainCustomerActivity.INSURANCE_MENU_POSITION,
                                data = Bundle().apply {
                                    putString(TIBClubActivity.TIB_CODE_EXTRA, tibcode)
                                }
                        )
                    } else {
                        MainNonCustomerActivity.openWithClearStack(
                                context = this,
                                position = MainNonCustomerActivity.INSURANCE_MENU_POSITION,
                                data = Bundle().apply {
                                    putString(TIBClubActivity.TIB_CODE_EXTRA, tibcode)
                                }
                        )
                    }
                }
            }
            CAR_LOAN -> {
                //DatabaseManager.getInstance().updateNotifyDeeplinkState(isNotifyDeeplinkState = true)
                val unique = getQueryParameterByDeeplink(UNIQUE_ID) ?: ""
                CarLoanFragment.startByInsightDeeplink(
                        context = this,
                        item = unique
                )
                finish()

            }
            ENGINE_RESULT -> {
                val refID = getQueryParameterByDeeplink(REF_ID) ?: ""
                //DatabaseManager.getInstance().updateNotifyDeeplinkState(isNotifyDeeplinkState = true)
                val unique = getQueryParameterByDeeplink(UNIQUE_ID) ?: ""
                CarLoanFragment.startByInsightDeeplink(
                        context = this,
                        item = refID
                )
                finish()
            }
            ETC -> {
                val jsonString = getQueryParameterByDeeplink(JSON_STRING)
                val item = Gson().fromJson(jsonString, NotificationJsonResponse::class.java)

                if (isAppForegroundState()) {
                    if (UserManager.getInstance().isCustomer()) {
                        CarInstallmentFragment.startByDeeplinkForeground(
                                context = this,
                                tabPosition = CarInstallmentFragment.NOTIFY_TAB_POSITION
                        )
                        return
                    } else {
                        MainNonCustomerActivity.openWithClearStack(
                                context = this,
                                position = MainNonCustomerActivity.NOTIFY_FOR_NON_CUSTOMER
                        )
                        return
                    }
                }

                if (UserManager.getInstance().isCustomer()) {
                    CarInstallmentFragment.startByDeeplinkDialog(
                            context = this,
                            item = item,
                            isShowButtonDialog = false,
                            isShowDialog = !isAppForegroundState()
                    )
                } else {
                    RegisterFragment.startByDeeplinkDialog(
                            context = this,
                            item = item,
                            isShowButtonDialog = false,
                            isShowDialog = !isAppForegroundState()
                    )
                }
            }
            else -> {
                val jsonString = getQueryParameterByDeeplink(JSON_STRING)
                val item = Gson().fromJson(jsonString, NotificationJsonResponse::class.java)

                if (isAppForegroundState()) {
                    CarInstallmentFragment.startByDeeplinkForeground(
                            context = this,
                            tabPosition = CarInstallmentFragment.NOTIFY_TAB_POSITION
                    )
                    return
                }

                CarInstallmentFragment.startByDeeplinkDialog(
                        context = this,
                        item = item,
                        isShowButtonDialog = false,
                        isShowDialog = true)
            }
        }
    }

    private fun isAppForegroundState() = false

}
