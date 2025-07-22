package tlt.th.co.toyotaleasing.modules.fingerprint

import android.app.Dialog
import android.os.Bundle
import android.os.Handler
import androidx.fragment.app.FragmentManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import com.github.ajalt.reprint.core.AuthenticationFailureReason
import com.github.ajalt.reprint.core.AuthenticationListener
import com.github.ajalt.reprint.core.Reprint
import kotlinx.android.synthetic.main.fragment_dialog_fingerprint_retry.*
import tlt.th.co.toyotaleasing.R
import tlt.th.co.toyotaleasing.analytics.AnalyticsManager
import tlt.th.co.toyotaleasing.analytics.AnalyticsScreenName
import tlt.th.co.toyotaleasing.common.base.BaseDialogFragment
import tlt.th.co.toyotaleasing.common.extension.gone
import tlt.th.co.toyotaleasing.common.extension.visible

class FingerprintAuthDialogFragment : BaseDialogFragment() {

    private val TIME_TO_DISMISS_MESSAGE = 3000L
    private val ERROR_TOO_MANY_ATTEMPT = 7
    private lateinit var listener: FingerprintAuthDialogFragment.Listener

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
       dialog!!.window!!.requestFeature(Window.FEATURE_NO_TITLE)
       dialog!!.window!!.setBackgroundDrawableResource(android.R.color.transparent)
        return dialog
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_dialog_fingerprint_retry, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initInstances()
    }

    override fun onResume() {
        super.onResume()
        startFingerprintScan()
       dialog!!.window!!.setLayout(resources.displayMetrics.widthPixels, resources.displayMetrics.heightPixels)
    }

    override fun onPause() {
        super.onPause()
        AnalyticsManager.trackScreenDialog(AnalyticsScreenName.LOGIN_TOUCH_ID)
    }

    override fun onStop() {
        cancelFingerprintScan()
        super.onStop()
    }

    private fun initInstances() {
        btn_cancel.setOnClickListener {
            AnalyticsManager.loginTouchIdCancelClicked()
            listener.onFingerprintAuthCancel()
            fragmentManager?.let { dismiss() }
        }
    }

    private fun startFingerprintScan() {
        Reprint.authenticate(onFingerprintAuthenListener)
    }

    private fun cancelFingerprintScan() {
        Reprint.cancelAuthentication()
    }

    private val onFingerprintAuthenListener = object : AuthenticationListener {
        override fun onSuccess(moduleTag: Int) {
            AnalyticsManager.loginTouchIdScan()
            showSuccess()
        }

        override fun onFailure(failureReason: AuthenticationFailureReason, fatal: Boolean,
                               errorMessage: CharSequence, moduleTag: Int, errorCode: Int) {
            AnalyticsManager.trackScreenError(AnalyticsScreenName.LOGIN_TOUCH_ID_FAIL)
            AnalyticsManager.loginTouchIdFailCancelClicked()
            showError(failureReason, fatal, errorMessage, errorCode)
        }
    }

    private fun showError(failureReason: AuthenticationFailureReason, fatal: Boolean, errorMessage: CharSequence, errorCode: Int) {
        img_scan_fail?.visible()
        txt_please_try_again?.visible()

        Handler().postDelayed({
            img_scan_fail?.gone()
            img_scan_success?.gone()
            txt_please_try_again?.gone()

            txt_description?.text = getString(R.string.fingerprint_dialog_description)

            if (errorCode == ERROR_TOO_MANY_ATTEMPT) {
                fragmentManager?.let { dismiss() }
                return@postDelayed
            }
        }, TIME_TO_DISMISS_MESSAGE)
    }

    private fun showSuccess() {
        img_scan_fail?.gone()
        img_scan_success?.visible()
        txt_please_try_again?.gone()
        btn_cancel?.gone()

        txt_description?.text = getString(R.string.fingerprint_scan_success)

        Reprint.cancelAuthentication()

        Handler().postDelayed({
            listener.onFingerprintAuthSuccess()
            fragmentManager?.let { dismiss() }
        }, TIME_TO_DISMISS_MESSAGE)
    }

    companion object {
        private const val TAG = "FingerprintAuthDialogFragment"

        fun newInstance() = FingerprintAuthDialogFragment()

        fun show(fragmentManager: FragmentManager, listener: Listener) {
            val fragment = newInstance()
            fragment.listener = listener
            fragment.show(fragmentManager!!, TAG)
        }
    }

    interface Listener {
        fun onFingerprintAuthSuccess()
        fun onFingerprintAuthCancel()
    }
}