package tlt.th.co.toyotaleasing.modules.staff.main.nonstaff

import android.Manifest
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.common.api.ResultCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsResult
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import tlt.th.co.toyotaleasing.R
import tlt.th.co.toyotaleasing.common.base.BaseActivity
import tlt.th.co.toyotaleasing.common.listener.OnHambergerClickListener
import tlt.th.co.toyotaleasing.manager.DeeplinkManager
import tlt.th.co.toyotaleasing.modules.sidebarmenu.SidebarMenuViewModel
import tlt.th.co.toyotaleasing.modules.verifyemail.VerifyEmailFailActivity
import tlt.th.co.toyotaleasing.modules.verifyemail.VerifyEmailSuccessActivity

class MainNonStaffActivity : BaseActivity(),
        OnHambergerClickListener,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        ResultCallback<LocationSettingsResult> {

    private lateinit var mGoogleApiClient: GoogleApiClient
    private lateinit var locationRequest: LocationRequest

    private val sidebarMenuViewModel by lazy {
        ViewModelProviders.of(this).get(SidebarMenuViewModel::class.java)
    }

    private val defaultMenuPosition by lazy {
        intent.getIntExtra(MENU_POSITION_EXTRA, REGISTER_MENU_POSITION)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_non_staff)

        checkPermission(
                success = { initGoogleApiClient() },
                failure = {}
        )

        initViewModel()
        initInstance()

        sidebarMenuViewModel.getUserProfile()

        handleDeeplink()
    }

    private fun initInstance() {

    }

    private fun initViewModel() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onHambergerClick() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onConnected(p0: Bundle?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onConnectionSuspended(p0: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onConnectionFailed(p0: ConnectionResult) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onResult(p0: LocationSettingsResult) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
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

    companion object {
        const val DATA_POSITION_EXTRA = "DATA_POSITION_EXTRA"
        private const val MENU_POSITION_EXTRA = "MENU_POSITION_EXTRA"

        const val REGISTER_MENU_POSITION = 0
        const val INSURANCE_MENU_POSITION = 1
        const val CONTACTUS_MENU_POSITION = 2
        const val NEWS_PROMOTION_MENU_POSITION = 3
        const val OFFICE_LOCATION_MENU_POSITION = 4
        const val DOCUMENT_DOWNLOAD_MENU_POSITION = 5
        const val FAQ_MENU_POSITION = 6
        const val SETTING_MENU_POSITION = 7
        const val POLICY_MENU_POSITION = 8
        const val NON_CUSTOMER_MENU_POSITION = 9
        const val NOTIFY_FOR_NON_CUSTOMER = 12
    }
}
