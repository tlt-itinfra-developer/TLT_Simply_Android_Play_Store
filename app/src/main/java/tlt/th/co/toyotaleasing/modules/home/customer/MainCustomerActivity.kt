package tlt.th.co.toyotaleasing.modules.home.customer

import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import android.view.View
import kotlinx.android.synthetic.main.activity_main_customer.*
import kotlinx.android.synthetic.main.widget_sidebar_menu.*
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import tlt.th.co.toyotaleasing.BuildConfig
import tlt.th.co.toyotaleasing.R
import tlt.th.co.toyotaleasing.modules.custloan.loanmodule.center.CarLoanFragment
import tlt.th.co.toyotaleasing.analytics.AnalyticsManager
import tlt.th.co.toyotaleasing.analytics.AnalyticsScreenName
import tlt.th.co.toyotaleasing.common.base.BaseActivity
import tlt.th.co.toyotaleasing.common.eventbus.*
import tlt.th.co.toyotaleasing.common.extension.ifFalse
import tlt.th.co.toyotaleasing.common.extension.loadImageByUri
import tlt.th.co.toyotaleasing.common.extension.showToast
import tlt.th.co.toyotaleasing.common.listener.OnHambergerClickListener
import tlt.th.co.toyotaleasing.manager.BusManager

import tlt.th.co.toyotaleasing.modules.checkstatusfrombank.CheckStatusFromBankActivity
import tlt.th.co.toyotaleasing.modules.contactus.ContactUsMainFragment
import tlt.th.co.toyotaleasing.modules.custloan.loanmodule.center.LoanAuthenActivity
import tlt.th.co.toyotaleasing.modules.document.DocumentDownloadFragment
import tlt.th.co.toyotaleasing.modules.ecommerce.ECommerceActiviy
import tlt.th.co.toyotaleasing.modules.faq.main.FAQFragment
import tlt.th.co.toyotaleasing.modules.installment.CarInstallmentFragment
import tlt.th.co.toyotaleasing.modules.insurance.InsuranceFragment
import tlt.th.co.toyotaleasing.modules.insurance.other.InsuranceOtherFragment
import tlt.th.co.toyotaleasing.modules.livechat.LiveChatActivity
import tlt.th.co.toyotaleasing.modules.location.main.LocationFragment
import tlt.th.co.toyotaleasing.modules.newsandpromotion.NewsAndPromotionFragment
import tlt.th.co.toyotaleasing.modules.ownership.OwnershipTransferTrackingFragment
import tlt.th.co.toyotaleasing.modules.pincode.AuthPincodeActivity
import tlt.th.co.toyotaleasing.modules.pincode.IsExitDialogFragment
import tlt.th.co.toyotaleasing.modules.policy.PolicyFragment
import tlt.th.co.toyotaleasing.modules.policy.PrivacyPolicyFragment
import tlt.th.co.toyotaleasing.modules.qrcode.allcar.QRAllCarFragment
import tlt.th.co.toyotaleasing.modules.setting.main.SettingFragment
import tlt.th.co.toyotaleasing.modules.sidebarmenu.SidebarMenuViewModel
import tlt.th.co.toyotaleasing.modules.sidebarmenu.SidebarMenuWidget
import tlt.th.co.toyotaleasing.modules.skippayment.SkipPaymentFragment
import tlt.th.co.toyotaleasing.modules.tax.TaxFragment
import tlt.th.co.toyotaleasing.modules.tutorial.TutorialActivity
import tlt.th.co.toyotaleasing.modules.web.WebActivity
import tlt.th.co.toyotaleasing.util.ExternalAppUtils

class MainCustomerActivity : BaseActivity(), OnHambergerClickListener,
        IsExitDialogFragment.Listener {

    private val sidebarMenuViewModel by lazy {
        ViewModelProviders.of(this).get(SidebarMenuViewModel::class.java)
    }

    private val defaultMenuPosition by lazy {
        intent.getIntExtra(MENU_POSITION_EXTRA, INSTALLMENT_MENU_POSITION)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_customer)
        BusManager.subscribe(this)

        // check show app tutorial state
        sidebarMenuViewModel.checkShowTutorial()

        listenerViewModel()
        initInstances()
    }

    private fun initInstances() {


        layout_sidebar_menu.setOnSidebarMenuCloseClicked(View.OnClickListener {
            AnalyticsManager.trackScreenSideBar(AnalyticsScreenName.MAIN_MENU)
            AnalyticsManager.mainMenuCloseClicked()
            drawer_layout.closeDrawers()
        })


        layout_sidebar_menu.setOnSidebarMenuInteractionListener(onSidebarMenuListener)
    }

    private fun listenerViewModel() {
        sidebarMenuViewModel.whenDataLoaded.observe(this, Observer {
            layout_sidebar_menu.initMenus(it!!)
        })

        sidebarMenuViewModel.whenLogoutSuccess.observe(this, Observer {
            if (it!!) {
                AuthPincodeActivity.startWithResult(this@MainCustomerActivity)
            }
        })

        sidebarMenuViewModel.whenCheckShowTutorial.observe(this, Observer {
            if (it!!) {
               // startActivity(Intent(this, TutorialActivity::class.java))
            }
            sidebarMenuViewModel.getUserProfile()
        })

        sidebarMenuViewModel.whenCheckIdentity.observe(this, Observer {
            it?.let{
                    LoanAuthenActivity.open(this@MainCustomerActivity)
            }
        })
    }

    override fun onHambergerClick() {
        drawer_layout.openDrawer(Gravity.START!!, true)
    }

    private val onSidebarMenuListener = object : SidebarMenuWidget.OnSidebarMenuInteractionListener {

        override fun onVersionClicked(txt: String) {
            if (txt == "2" || txt == "1") {
                showToast(txt)
            } else {
                val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager?
                val clip = ClipData.newPlainText("Source Text", txt)
                if (clipboard != null && clip != null)
                    clipboard!!.setPrimaryClip(clip!!)
//
//                var clipboardManager = ( getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager ).primaryClip
//                val clipData = ClipData.newPlainText("Source Text", txt)
//                clipboardManager = clipData
            }
        }

        override fun onAfterInteraction() {
            drawer_layout.closeDrawers()
        }

        override fun onInstallmentClicked() {
            AnalyticsManager.mainMenuInstallmentClicked()
        }

        override fun onTaxClicked() {
            AnalyticsManager.mainMenuTaxClicked()
        }

        override fun onInsuranceClicked() {
            AnalyticsManager.mainMenuInsuranceClicked()
        }

        override fun onInsuranceOtherClicked() {
            AnalyticsManager.mainMenuInsuranceClicked()
            ExternalAppUtils.openByLink(  this@MainCustomerActivity ,  BuildConfig.OTHER_INSURANCE_URL)
        }


        override fun onQRCodeClicked() {
            AnalyticsManager.mainMenuQrClicked()
        }



        override fun onCalculateInstallmentClick() {
            AnalyticsManager.mainMenuLoanCalculationClicked()
            WebActivity.startWebCalculate(this@MainCustomerActivity)
        }

        override fun onNewsClicked() {
            AnalyticsManager.mainMenuNewsClicked()
        }

        override fun onContactUsClicked() {
            AnalyticsManager.mainMenuContactUsClicked()
        }

        override fun onMandatoriumClicked() {
//            AnalyticsManager.mainMenuContactUsClicked()
//            val master = MasterDataManager.getInstance().getSideMenu()
//            val url = master!!.get(0).mENULINK
//            WebActivity.startWithMadatorium(this@MainCustomerActivity , url )

            BusManager.observe(ReloadSkippaymentEvent())
        }


        override fun onOwnershipTranferClicked() {
            AnalyticsManager.trackScreen(AnalyticsScreenName.OWNERSHIP_TRANSFER_TRACKING)
            BusManager.observe(ReloadTrackingOwnershipEvent())
        }


        override fun onECommerceClicked() {
            AnalyticsManager.trackScreen(AnalyticsScreenName.E_COMMERECE)
            ECommerceActiviy.startEcommerce(this@MainCustomerActivity)
        }


        override fun onOfficeLocationClicked() {
            AnalyticsManager.mainMenuLocationClicked()
        }

        override fun onLiveChatClicked() {
            AnalyticsManager.mainMenuChatClicked()
            LiveChatActivity.start(this@MainCustomerActivity)
        }

        override fun onDownloadClicked() {
            AnalyticsManager.mainMenuDownloadDocClicked()
        }

        override fun onFAQClicked() {
            AnalyticsManager.mainMenuFaqClicked()
        }

        override fun onTermAndConditionClicked() {
            AnalyticsManager.mainMenuTermConditionClicked()
        }

        override fun onPrivacyPolicyClicked() {
        }

        override fun onSettingClicked() {
            AnalyticsManager.mainMenuSettingClicked()
        }

        override fun onCarLoanClicked() {
//            AnalyticsManager.carLoanClicked()
//            if(sidebarMenuViewModel.isCustomer() ){
//                showHideFragment(findFragment(CarLoanFragment::class.java)
//                        ?: CarLoanFragment.newInstance())
//            }else{

                sidebarMenuViewModel.getIdentity()

//            }
//            showHideFragment(findFragment(CarLoanFragment::class.java)
//                    ?: CarLoanFragment.newInstance())
        }


        override fun onSignoutClicked() {
            AnalyticsManager.mainMenuSignoutClicked()
            /**
             * Call No.59 API: logoutByCustomer
             */
            sidebarMenuViewModel.logoutByCustomer()
//            AuthPincodeActivity.startWithResult(this@MainCustomerActivity)
        }

        override fun onDebugClicked() {
            BusManager.observe(DeviceLogonEvent())
//            DebugActivity.start(this@MainCustomerActivity)
        }

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onChangeImageProfileReceived(event: ChangeImageProfileEvent) {
        ic_profile.loadImageByUri(event.uri)
    }

    override fun onIsExitCancelClicked() {

    }

    override fun onIsExitConfirmClicked() {
        finishAffinity()
    }

    override fun onDestroy() {
        super.onDestroy()
        BusManager.unsubscribe(this)
    }

    companion object {
        const val DATA_POSITION_EXTRA = "DATA_POSITION_EXTRA"
        private const val MENU_POSITION_EXTRA = "MENU_POSITION_EXTRA"


        const val INSTALLMENT_MENU_POSITION = 0
        const val TAX_MENU_POSITION = 1
        const val INSURANCE_MENU_POSITION = 2
        const val CAR_LOAN = 4
        const val CONTACTUS_MENU_POSITION = 3
        const val SETTING_MENU_POSITION = 4
        const val NEWS_PROMOTION_MENU_POSITION = 5
        const val QRCODE_MENU_POSITION = 6
        const val OFFICE_LOCATION_MENU_POSITION = 8
        const val DOCUMENT_DOWNLOAD_MENU_POSITION = 9
        const val FAQ_MENU_POSITION = 10
        const val POLICY_MENU_POSITION = 11

        fun start(context: Context) {
            val intent = Intent(context, MainCustomerActivity::class.java).apply {
                putExtra(MENU_POSITION_EXTRA, INSTALLMENT_MENU_POSITION)
            }

            context.startActivity(intent)
        }

        fun startWithClearStack(context: Context?) {
            startWithClearStack(context, INSTALLMENT_MENU_POSITION)
        }

        fun startWithClearStack(context: Context?, position: Int = INSTALLMENT_MENU_POSITION) {
            startWithClearStack(context, position, Bundle())
        }

        fun startWithClearStack(context: Context?,
                                position: Int = INSTALLMENT_MENU_POSITION,
                                data: Bundle) {
            val intent = Intent(context, MainCustomerActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                putExtra(MENU_POSITION_EXTRA, position)
                putExtra(DATA_POSITION_EXTRA, data)
            }
            context?.startActivity(intent)
        }

        fun startWithClearStackByDeeplink(context: Context?,
                                          position: Int = INSTALLMENT_MENU_POSITION,
                                          data: Bundle) {
            val intent = Intent(context, MainCustomerActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                putExtra(MENU_POSITION_EXTRA, position)
                putExtra(DATA_POSITION_EXTRA, data)
            }
            context?.startActivity(intent)
        }


        fun openInsightWithDeep(context: Context?,
                                position: Int ,
                                data: Bundle) {
            val intent = Intent(context, MainCustomerActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                putExtra(MENU_POSITION_EXTRA, position)
                putExtra(DATA_POSITION_EXTRA, data)
            }
            context?.startActivity(intent)
        }
    }
}
