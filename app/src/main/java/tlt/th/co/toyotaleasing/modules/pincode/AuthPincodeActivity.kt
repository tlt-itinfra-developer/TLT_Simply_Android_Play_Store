package tlt.th.co.toyotaleasing.modules.pincode

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
import tlt.th.co.toyotaleasing.common.extension.*
import tlt.th.co.toyotaleasing.manager.ContextManager
import tlt.th.co.toyotaleasing.modules.fingerprint.FingerprintAuthDialogFragment
import tlt.th.co.toyotaleasing.modules.home.customer.MainCustomerActivity
import tlt.th.co.toyotaleasing.view.KeyboardNumberWidget
import android.graphics.drawable.Drawable
import android.view.Gravity
import android.view.WindowManager
import android.app.Dialog
import android.graphics.drawable.ColorDrawable
import android.provider.Settings
import androidx.appcompat.app.AppCompatActivity
import tlt.th.co.toyotaleasing.modules.custloan.loanmodule.carloan.LoanUploadDocumentActivity
import tlt.th.co.toyotaleasing.util.ExternalAppUtils


class AuthPincodeActivity : AppCompatActivity(),
        AuthAttemptOverLimitDialogFragment.Listener,
        IsExitDialogFragment.Listener {

    private val TIME_TO_DISMISS_INVALID_MESSAGE = 3000L
    private var latitude: String = ""
    private var longitude: String = ""
    private var isForceUpdate: Boolean = false
    private var forceUpdateMessage: String = ""


    private val viewModel by lazy {
        ViewModelProviders.of(this).get(AuthPincodeViewModel::class.java)
    }

    private val isForgotPincodeEnabled by lazy {
        intent?.getBooleanExtra(IS_FORGOT_MENU_ENABLE_EXTRA, true) ?: true
    }

    private val isClearStackEnabled by lazy {
        intent?.getBooleanExtra(IS_CLEAR_STACK_EXTRA, false) ?: false
    }

//    override fun onPause() {
//        super.onPause()
//      //  AnalyticsManager.trackScreen(AnalyticsScreenName.LOGIN_PINCODE)
//    }

    @SuppressLint("MissingPermission")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth_pincode)

        initViewModel()
        initInstances()

  //      viewModel.checkApplicationStatus()


    }

//    override fun onResume() {
//        super.onResume()
////        isForceUpdate.ifTrue {
////            AlertDialog.Builder(this)
////                    .setCancelable(false)
////                    .setMessage(forceUpdateMessage)
////                    .setPositiveButton(ContextManager.getInstance().getStringByRes(R.string.dialog_button_ok)) {dialog, which ->
////                       dialog!!.dismiss()
////                        startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(ContextManager.getInstance().getStringByRes(R.string.google_play_store_link))))
////                    }
////                    .show()
////        }
//    }

    private fun initViewModel() {
        viewModel.whenLoading.observe(this, Observer {
            //toggleLoadingScreenDialog(it!!)
        })

        viewModel.whenShowFingerprintAuth.observe(this, Observer {
            if (!isForgotPincodeEnabled) {
                return@Observer
            }

            FingerprintAuthDialogFragment.show(supportFragmentManager, object : FingerprintAuthDialogFragment.Listener {
                override fun onFingerprintAuthCancel() {

                }

                override fun onFingerprintAuthSuccess() {
                    viewModel.loginByFingerprint()
                }
            })
        })

        viewModel.whenAuthFailure.observe(this, Observer {
            AnalyticsManager.trackScreenError(AnalyticsScreenName.LOGIN_PINCODE_FAIL)
            txt_pincode_title.invisible()
            layout_pincode_invalid.visible()
            txt_pincode_invalid.text = it

            pincode_view.clearPincode()

            Handler().postDelayed({
                layout_pincode_invalid?.gone()
                txt_pincode_title?.visible()
            }, TIME_TO_DISMISS_INVALID_MESSAGE)
        })

        viewModel.whenAuthAttemptOverLimit.observe(this, Observer {
            AuthAttemptOverLimitDialogFragment.show(supportFragmentManager)
        })

        viewModel.whenAuthSuccess.observe(this, Observer { pincode: String? ->
                //MainCustomerActivity.startWithClearStack(this)
            pincode_view.clearPincode()
            LoanUploadDocumentActivity.start(this@AuthPincodeActivity , "", "" )
                return@Observer
        })

        viewModel.whenCheckApplicationFail.observe(this, Observer {
            showToast(it!!)
        })

        viewModel.whenCheckApplicationSuccess.observe(this, Observer {
            showPopupCheckApplication(it!!)

        })

        viewModel.whenCallAnnounceEmpty.observe(this, Observer {
            viewModel.checkFingerprintSetting()
        })


        viewModel.whenCallAnnounceSuccess.observe(this, Observer {
            it?.let {
                if(it.size>0)
                    for (item  in it) {
                        var  url : Uri   = Uri.parse(item.urlLink)
                        showAnnouncement(url ,item.linkRef)
                    }
            }

        })

    }
    private fun showAnnouncement(uri : Uri , linkRef : String) {
        try {
            val mDialogView = LayoutInflater.from(this).inflate(R.layout.fragment_dialog_announcement, null)
            val mBuilder = AlertDialog.Builder(this,R.style.dialogBlur).setView(mDialogView)
            mDialogView.img_announcement.loadImageByUri(uri)
            val mAlertDialog = mBuilder.show()
            mDialogView.popup_btn_close.setOnClickListener {
                mAlertDialog.dismiss()
                viewModel.checkFingerprintSetting()
            }

            mDialogView.img_announcement.setOnClickListener {
                if(linkRef != "") {
                    ExternalAppUtils.openByLink(this, linkRef)
                    mAlertDialog.dismiss()
                }
            }
        } catch (e: Exception) {
            e.message
        }
    }


    private fun showPopupCheckApplication(it: AuthPincodeViewModel.Model) {
        isForceUpdate = it.isShowForceUpdatePopup
        forceUpdateMessage = it.forceUpdateMessage

        val forceDialogBuilder = AlertDialog.Builder(this)
        forceDialogBuilder.setCancelable(false)
        forceDialogBuilder.setMessage(it.forceUpdateMessage)
        forceDialogBuilder.setPositiveButton(ContextManager.getInstance().getStringByRes(R.string.dialog_button_ok)) {dialog, which ->
           dialog!!.dismiss()
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(ContextManager.getInstance().getStringByRes(R.string.google_play_store_link))))
        }
        val forceDialog = forceDialogBuilder.create()

        val noticeDialogBuilder = AlertDialog.Builder(this)
        noticeDialogBuilder.setCancelable(false)
        noticeDialogBuilder.setMessage(it.NoticeMessage)
        noticeDialogBuilder.setPositiveButton(ContextManager.getInstance().getStringByRes(R.string.dialog_button_ok)) {dialog, which ->
           dialog!!.dismiss()
            viewModel.GetAnnouncement()
//            viewModel.checkFingerprintSetting()
        }
        val noticeDialog = noticeDialogBuilder.create()

        if (it.isShowForceUpdatePopup && !forceDialog.isShowing) {
            forceDialog.show()
            val forceButton = forceDialog.getButton(AlertDialog.BUTTON_POSITIVE)
            forceButton.setTextColor(ContextCompat.getColor(this, R.color.cherry_red))
        } else if (it.isShowNoticePopup && !forceDialog.isShowing && !noticeDialog.isShowing) {
            noticeDialog.show()
            val noticeButton = noticeDialog.getButton(AlertDialog.BUTTON_POSITIVE)
            noticeButton.setTextColor(ContextCompat.getColor(this, R.color.cherry_red))
        } else {
            viewModel.GetAnnouncement()
//            viewModel.checkFingerprintSetting()
        }
    }

    private fun initInstances() {
        txt_forgot_pin.setOnClickListener {
            AnalyticsManager.loginPincodeForgotClicked()
            ForgotPincodeActivity.start(this)
        }

        pincode_view.setOnPincodeCompleteListener { pincode ->
            AnalyticsManager.loginPincodeClicked()
            viewModel.loginByPincode(pincode)
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

        txt_forgot_pin.gone()
    }

    override fun onIsExitCancelClicked() {

    }

    override fun onIsExitConfirmClicked() {
        finishAffinity()
    }

    override fun onAuthAttemptOverLimitConfirmClicked() {
        ForgotPincodeActivity.start(this)
    }

    companion object {
        const val AUTH_CODE = 88
        const val AUTH_PINCODE_EXTRA = "AUTH_PINCODE_EXTRA"
        private const val IS_FORGOT_MENU_ENABLE_EXTRA = "IS_FORGOT_MENU_ENABLE_EXTRA"
        private const val IS_CLEAR_STACK_EXTRA = "IS_CLEAR_STACK_EXTRA"

        fun startWithResult(activity: Activity) {
            val intent = Intent(activity, AuthPincodeActivity::class.java)
            activity.startActivityForResult(intent, AUTH_CODE)
        }

        fun startWithResult(fragment: Fragment, isForgotPinEnable: Boolean = true) {
            val intent = Intent(fragment.activity, AuthPincodeActivity::class.java).apply {
                putExtra(IS_FORGOT_MENU_ENABLE_EXTRA, isForgotPinEnable)
            }
            fragment.startActivityForResult(intent, AUTH_CODE)
        }

        fun startWithClearStack(activity: Activity) {
            val intent = Intent(activity, AuthPincodeActivity::class.java).apply {
                putExtra(IS_CLEAR_STACK_EXTRA, true)
            }
            activity.startActivityForResult(intent, AUTH_CODE)
        }
    }
}
