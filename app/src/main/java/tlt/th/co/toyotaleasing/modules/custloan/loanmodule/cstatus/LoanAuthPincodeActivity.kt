package tlt.th.co.toyotaleasing.modules.custloan.loanmodule.cstatus

import android.annotation.SuppressLint
import android.app.Activity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import androidx.fragment.app.Fragment
import androidx.core.content.ContextCompat
import androidx.appcompat.app.AlertDialog
import android.view.LayoutInflater
import android.widget.ImageView
import kotlinx.android.synthetic.main.activity_auth_pincode.*
import kotlinx.android.synthetic.main.fragment_dialog_announcement.*
import kotlinx.android.synthetic.main.fragment_dialog_announcement.view.*
import tlt.th.co.toyotaleasing.R
import tlt.th.co.toyotaleasing.analytics.AnalyticsManager
import tlt.th.co.toyotaleasing.analytics.AnalyticsScreenName
import tlt.th.co.toyotaleasing.common.base.BaseActivity
import tlt.th.co.toyotaleasing.common.dialog.MsgdescNormalDialog
import tlt.th.co.toyotaleasing.common.extension.*
import tlt.th.co.toyotaleasing.manager.ContextManager
import tlt.th.co.toyotaleasing.modules.custloan.MenuStepController
import tlt.th.co.toyotaleasing.modules.custloan.loanmodule.carloan.LoanPersonalInfomationActivity
import tlt.th.co.toyotaleasing.modules.custloan.loanmodule.ndid.LoanEContractWaitNDIDActivity
import tlt.th.co.toyotaleasing.modules.fingerprint.FingerprintAuthDialogFragment
import tlt.th.co.toyotaleasing.modules.home.customer.MainCustomerActivity
import tlt.th.co.toyotaleasing.view.KeyboardNumberWidget

import tlt.th.co.toyotaleasing.modules.pincode.AuthAttemptOverLimitDialogFragment


class LoanAuthPincodeActivity : BaseActivity()  , MsgdescNormalDialog.Listener {

    private val TIME_TO_DISMISS_INVALID_MESSAGE = 3000L
    private var latitude: String = ""
    private var longitude: String = ""
    private var isForceUpdate: Boolean = false
    private var forceUpdateMessage: String = ""


    private val data_extra by lazy {
        intent?.getStringExtra(REF_ID) ?: ""
    }

    private val viewModel by lazy {
        ViewModelProviders.of(this).get(LoanAuthPincodeViewModel::class.java)
    }

    private val isOnline by lazy {
        intent?.getBooleanExtra(IS_ONLINE, true) ?: true
    }

    override fun onPause() {
        super.onPause()

    }

    @SuppressLint("MissingPermission")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth_pincode)

        initViewModel()
        initInstances()


    }

    private fun initViewModel() {
        viewModel.whenLoading.observe(this, Observer {
            toggleLoadingScreenDialog(it!!)
        })

        viewModel.whenDataLoadedMessage.observe(this, Observer {
            it?.let {
                MsgdescNormalDialog.show(
                        fragmentManager = supportFragmentManager,
                        description = it ,
                        confirmButtonMessage = getString(R.string.dialog_button_ok)
                )
            }
        })


//        viewModel.whenAuthFailure.observe(this, Observer {
//
//            txt_pincode_title.invisible()
//            layout_pincode_invalid.visible()
//            txt_pincode_invalid.text = it
//
//            pincode_view.clearPincode()
//
//            Handler().postDelayed({
//                layout_pincode_invalid?.gone()
//                txt_pincode_title?.visible()
//            }, TIME_TO_DISMISS_INVALID_MESSAGE)
//        })
//        viewModel.whenAuthAttemptOverLimit.observe(this, Observer {
//            AuthAttemptOverLimitDialogFragment.show(supportFragmentManager)
//        })
//
//        viewModel.whenAuthSuccess.observe(this, Observer { pincode: String? ->
//            viewModel.SyncSubmitEContract(data_extra)
//        })

        viewModel.whenSyncSuccessData.observe(this, Observer {

            it?.let {
                MenuStepController.open(this@LoanAuthPincodeActivity, it.ref_id , it.step , it.ref_url)
            }
        })

        viewModel.whenDataLoadedMessage.observe(this, Observer {
            it?.let {
                txt_pincode_title.invisible()
                pincode_view.clearPincode()
                MsgdescNormalDialog.show(
                        fragmentManager = supportFragmentManager,
                        description = it ,
                        confirmButtonMessage = getString(R.string.dialog_button_ok)
                )
            }
        })
    }


    private fun initInstances() {

        txt_forgot_pin.gone()

        pincode_view.setOnPincodeCompleteListener { pincode ->
            AnalyticsManager.loginPincodeClicked()
            viewModel.SyncSubmitEContract(data_extra , pincode)
        }

        keyboard_number_view.setListener(object : KeyboardNumberWidget.Listener {
            override fun onNumberClick(number: String) {
                pincode_view.addPincode(number)
                viewModel.checkLoginAttemptOverLimit()
            }

            override fun onDeleteClick() {
                pincode_view.removePincode()
            }
        })
    }

    override fun onDialogConfirmClick() {

    }

    override fun onDialogCancelClick() {
    }

    companion object {

        const val IS_ONLINE = "ONLINE"
        const val REF_ID = "REF_ID"

        fun startWithResultOnline(activity: Activity?, data: String ) {
            val intent = Intent(activity, LoanAuthPincodeActivity::class.java)
            intent.putExtra(REF_ID, data)
            activity!!.startActivity(intent)
        }
    }
}
