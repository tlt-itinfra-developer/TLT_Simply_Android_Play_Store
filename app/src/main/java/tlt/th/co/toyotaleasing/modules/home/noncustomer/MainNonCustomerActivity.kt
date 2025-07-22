package tlt.th.co.toyotaleasing.modules.home.noncustomer

import android.Manifest
import android.app.Activity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import android.view.View
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.common.api.ResultCallback
import com.google.android.gms.location.*
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import kotlinx.android.synthetic.main.activity_main_non_customer.*
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import tlt.th.co.toyotaleasing.BuildConfig
import tlt.th.co.toyotaleasing.R
import tlt.th.co.toyotaleasing.analytics.AnalyticsManager
import tlt.th.co.toyotaleasing.analytics.AnalyticsScreenName
import tlt.th.co.toyotaleasing.common.base.BaseActivity
import tlt.th.co.toyotaleasing.common.eventbus.UpdateBadgeNotificationEvent
import tlt.th.co.toyotaleasing.common.extension.ifFalse
import tlt.th.co.toyotaleasing.common.extension.showToast
import tlt.th.co.toyotaleasing.common.listener.OnHambergerClickListener
import tlt.th.co.toyotaleasing.manager.BusManager
import tlt.th.co.toyotaleasing.manager.DeeplinkManager
import tlt.th.co.toyotaleasing.manager.db.UserManager
import tlt.th.co.toyotaleasing.modules.contactus.ContactUsMainFragment
import tlt.th.co.toyotaleasing.modules.debug.DebugActivity
import tlt.th.co.toyotaleasing.modules.document.DocumentDownloadFragment
import tlt.th.co.toyotaleasing.modules.ecommerce.ECommerceActiviy
import tlt.th.co.toyotaleasing.modules.faq.main.FAQFragment
import tlt.th.co.toyotaleasing.modules.insurance.InsuranceFragment
import tlt.th.co.toyotaleasing.modules.insurance.other.InsuranceOtherFragment
import tlt.th.co.toyotaleasing.modules.livechat.LiveChatActivity
import tlt.th.co.toyotaleasing.modules.location.main.LocationFragment
import tlt.th.co.toyotaleasing.modules.main.Convert
import tlt.th.co.toyotaleasing.modules.newsandpromotion.NewsAndPromotionFragment
import tlt.th.co.toyotaleasing.modules.noncustomer.*
import tlt.th.co.toyotaleasing.modules.policy.PolicyFragment
import tlt.th.co.toyotaleasing.modules.policy.PrivacyPolicyFragment
import tlt.th.co.toyotaleasing.modules.setting.main.SettingFragment
import tlt.th.co.toyotaleasing.modules.sidebarmenu.SidebarMenuViewModel
import tlt.th.co.toyotaleasing.modules.sidebarmenu.SidebarMenuWidget
import tlt.th.co.toyotaleasing.modules.verifyemail.VerifyEmailFailActivity
import tlt.th.co.toyotaleasing.modules.verifyemail.VerifyEmailSuccessActivity
import tlt.th.co.toyotaleasing.modules.web.WebActivity
import tlt.th.co.toyotaleasing.util.ExternalAppUtils

class MainNonCustomerActivity : BaseActivity(),
        OnHambergerClickListener,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        ResultCallback<LocationSettingsResult> {

    private lateinit var mGoogleApiClient: GoogleApiClient
    private lateinit var locationRequest: LocationRequest
    private var authenLoan: Boolean = false


    private val sidebarMenuViewModel by lazy {
        ViewModelProviders.of(this).get(SidebarMenuViewModel::class.java)
    }

    private val defaultMenuPosition by lazy {
        intent.getIntExtra(MENU_POSITION_EXTRA, REGISTER_MENU_POSITION)
    }

//    private val authenLoan by lazy {
//        intent.getBooleanExtra(AUTHEN_LOAN , false )
//    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_non_customer)

        checkPermission(success = {
            initGoogleApiClient()
        }, failure = {

        })

        initViewModel()
        initInstances()

        sidebarMenuViewModel.getUserProfile()

        handleDeeplink()
    }

    private fun initGoogleApiClient() {
        mGoogleApiClient = GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener {

                }.build()
        mGoogleApiClient.connect()

        locationRequest = LocationRequest.create()
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        locationRequest.interval = 30 * 1000
        locationRequest.fastestInterval = 5 * 1000
    }

    private fun initViewModel() {


        Convert.getProjectHashKey(this)

        sidebarMenuViewModel.whenDataLoaded.observe(this, Observer {
            layout_sidebar_menu.initMenus(it!!)
        })

//        sidebarMenuViewModel.whenCheckIdentity.observe(this, Observer {
//            it?.let{
//                if(it){
//                    showHideFragment(findFragment(CarLoanFragment::class.java)
//                            ?: CarLoanFragment.newInstance())
//                }else{
//                    LoanAuthenActivity.open(this@MainNonCustomerActivity)
//                }
//            }
//        })
    }



    private fun updateViewInfo() {


    }

    private fun initInstances() {

        updateViewInfo()

        layout_sidebar_menu.setOnSidebarMenuCloseClicked(View.OnClickListener {
            AnalyticsManager.trackScreenSideBar(AnalyticsScreenName.MAIN_MENU_NON_CUS)
            AnalyticsManager.mainMenuNonCloseClicked()
            drawer_layout.closeDrawers()
        })

        layout_sidebar_menu.setOnSidebarMenuInteractionListener(onSidebarMenuListener)

        layout_sidebar_menu.setOnRegisterClicked(View.OnClickListener {
            AnalyticsManager.mainMenuNonRegisterClicked()

            drawer_layout.closeDrawers()
        })

        layout_sidebar_menu.setBadgeNotification(UserManager.getInstance().getUnreadNotification().toInt())
        layout_sidebar_menu.setOnNotificationClicked(View.OnClickListener {
            AnalyticsManager.mainMenuNonNotificatoinClicked()

            drawer_layout.closeDrawers()
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
//                var clipboardManager = ( getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager).primaryClip
//                val clipData = ClipData.newPlainText("Source Text",txt)
//                clipboardManager = clipData
            }
        }

        override fun onAfterInteraction() {
            drawer_layout.closeDrawers()
        }

        override fun onInstallmentClicked() {
            AnalyticsManager.mainMenuNonInstallmentClicked()
        }

        override fun onTaxClicked() {
            AnalyticsManager.mainMenuNonTaxClicked()
        }

        override fun onInsuranceClicked() {
            AnalyticsManager.mainMenuNonInsuranceClicked()
        }

        override fun onInsuranceOtherClicked() {
            AnalyticsManager.tibInsuranceOther()
            ExternalAppUtils.openByLink(  this@MainNonCustomerActivity ,  BuildConfig.OTHER_INSURANCE_URL)
        }

        override fun onQRCodeClicked() {
            AnalyticsManager.mainMenuNonQrClicked()
        }

        override fun onCalculateInstallmentClick() {
            AnalyticsManager.mainMenuNonLoanCalculationClicked()
            WebActivity.startWebCalculate(this@MainNonCustomerActivity)
        }



        override fun onNewsClicked() {
            AnalyticsManager.mainMenuNonNewsClicked()
        }

        override fun onContactUsClicked() {
            AnalyticsManager.mainMenuNonContactUsClicked()
        }

        override fun onOfficeLocationClicked() {
            AnalyticsManager.mainMenuNonLocationClicked()
        }

        override fun onLiveChatClicked() {
            AnalyticsManager.mainMenuNonChatClicked()
            LiveChatActivity.start(this@MainNonCustomerActivity)
        }

        override fun onDownloadClicked() {
            AnalyticsManager.mainMenuNonDownloadDocClicked()
        }

        override fun onFAQClicked() {
            AnalyticsManager.mainMenuNonFaqClicked()
        }

        override fun onTermAndConditionClicked() {
            AnalyticsManager.mainMenuNonTermConditionClicked()
        }

        override fun onPrivacyPolicyClicked() {
        }

        override fun onSettingClicked() {
            AnalyticsManager.mainMenuNonSettingClicked()
        }


        override fun onCarLoanClicked() {
//            AnalyticsManager.carLoanClicked()
//            if(sidebarMenuViewModel.isCustomer() ){
//                showHideFragment(findFragment(CarLoanFragment::class.java)
//                        ?: CarLoanFragment.newInstance())
//            }else{

//            sidebarMenuViewModel.getIdentity()

//            }
//            showHideFragment(findFragment(CarLoanFragment::class.java)
//                    ?: CarLoanFragment.newInstance())
        }

//        override fun onCarLoanClicked() {
//            showHideFragment(findFragment(CarLoaNonCustomerFragment::class.java)
//                    ?: CarLoaNonCustomerFragment.newInstance())
//        }


        override fun onMandatoriumClicked() {

        }

        override fun onOwnershipTranferClicked() {

        }

        override fun onECommerceClicked() {
            AnalyticsManager.trackScreen(AnalyticsScreenName.E_COMMERECE)
            ECommerceActiviy.startEcommerce(this@MainNonCustomerActivity)
        }

        override fun onSignoutClicked() {
        }

        override fun onDebugClicked() {
            DebugActivity.start(this@MainNonCustomerActivity)
        }
    }

    private fun handleDeeplink() {
        if (DeeplinkManager.getInstance().isOpenVerifyEmailFailure(this)) {
            VerifyEmailFailActivity.open(this)
        }

        if (DeeplinkManager.getInstance().isOpenVerifyEmailSuccess(this)) {
            VerifyEmailSuccessActivity.open(this)
        }

        DeeplinkManager.getInstance().clearDeeplinkData(this)
    }

    override fun onStart() {
        super.onStart()
        BusManager.subscribe(this)
    }

    override fun onStop() {
        super.onStop()
        BusManager.unsubscribe(this)
    }

    private fun checkPermission(success: () -> Unit, failure: () -> Unit) {
        Dexter.withActivity(this)
                .withPermissions(
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.ACCESS_FINE_LOCATION
                ).withListener(object : MultiplePermissionsListener {
                    override fun onPermissionsChecked(report: MultiplePermissionsReport) {
                        if (report.areAllPermissionsGranted()) {
                            success.invoke()
                            return
                        }
                        failure.invoke()
                    }

                    override fun onPermissionRationaleShouldBeShown(permissions: List<PermissionRequest>, token: PermissionToken) {
                        token.continuePermissionRequest()
                    }
                }).check()
    }


    override fun onResult(locationSettingsResult: LocationSettingsResult) {
        val status = locationSettingsResult.status
        when (status.statusCode) {
            LocationSettingsStatusCodes.SUCCESS -> {

            }
            LocationSettingsStatusCodes.RESOLUTION_REQUIRED -> {
                status.startResolutionForResult(this@MainNonCustomerActivity, 100)
            }
            LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE -> {

            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 100) {
            if (resultCode == Activity.RESULT_OK) {

            }
        }
    }

    override fun onConnected(p0: Bundle?) {
        val builder = LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest)
        builder.setAlwaysShow(true)
        val result = LocationServices.SettingsApi.checkLocationSettings(
                mGoogleApiClient,
                builder.build()
        )
        result.setResultCallback(this)
    }

    override fun onConnectionSuspended(p0: Int) {

    }

    override fun onConnectionFailed(p0: ConnectionResult) {

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onBadgeNotificationReceived(event: UpdateBadgeNotificationEvent) {
        if (UserManager.getInstance().getUnreadNotification().toInt() > 0) {
            layout_sidebar_menu.setBadgeNotification(UserManager.getInstance().getUnreadNotification().toInt())
        } else {
            UserManager.getInstance().setZeroUnreadNotification()
            layout_sidebar_menu.setBadgeNotification(0)
        }
    }

    companion object {
        const val DATA_POSITION_EXTRA = "DATA_POSITION_EXTRA"
        private const val MENU_POSITION_EXTRA = "MENU_POSITION_EXTRA"

        const val REGISTER_MENU_POSITION = 0
        const val INSURANCE_MENU_POSITION = 1
        const val CAR_LOAN = 3
        const val CONTACTUS_MENU_POSITION = 4
        const val NEWS_PROMOTION_MENU_POSITION = 5
        const val OFFICE_LOCATION_MENU_POSITION = 6
        const val DOCUMENT_DOWNLOAD_MENU_POSITION = 7
        const val FAQ_MENU_POSITION = 8
        const val SETTING_MENU_POSITION = 9
        const val POLICY_MENU_POSITION = 10
        const val NON_CUSTOMER_MENU_POSITION = 11
        const val NOTIFY_FOR_NON_CUSTOMER = 14



        fun open(context: Context?) {
            val intent = Intent(context, MainNonCustomerActivity::class.java).apply {
                putExtra(MENU_POSITION_EXTRA, REGISTER_MENU_POSITION)
            }
            context?.startActivity(intent)
        }

        fun openWithClearStack(context: Context?) {
            openWithClearStack(context, REGISTER_MENU_POSITION)
        }

        fun openWithClearStack(context: Context?, position: Int = REGISTER_MENU_POSITION) {
            val intent = Intent(context, MainNonCustomerActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                putExtra(MENU_POSITION_EXTRA, position)
            }
            context?.startActivity(intent)
        }

        fun openWithClearStack(context: Context?,
                               position: Int = REGISTER_MENU_POSITION,
                               data: Bundle) {
            val intent = Intent(context, MainNonCustomerActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                putExtra(MENU_POSITION_EXTRA, position)
                putExtra(DATA_POSITION_EXTRA, data)
            }
            context?.startActivity(intent)
        }

        fun openInsightWithDeep(context: Context?,
                                position: Int ,
                                authen : String ,
                                data: Bundle) {
            val intent = Intent(context, MainNonCustomerActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                putExtra(MENU_POSITION_EXTRA, position)
                putExtra(DATA_POSITION_EXTRA, data)
            }
            context?.startActivity(intent)
        }

    }
}
