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
import kotlinx.android.synthetic.main.fragment_dialog_fingerprint_setting.*
import tlt.th.co.toyotaleasing.R
import tlt.th.co.toyotaleasing.analytics.AnalyticsManager
import tlt.th.co.toyotaleasing.analytics.AnalyticsScreenName
import tlt.th.co.toyotaleasing.common.base.BaseDialogFragment
import tlt.th.co.toyotaleasing.common.extension.gone
import tlt.th.co.toyotaleasing.common.extension.visible
import tlt.th.co.toyotaleasing.modules.home.customer.MainCustomerActivity

class FingerprintSettingDialogFragment : BaseDialogFragment() {

    private lateinit var listener: Listener
    private val ERROR_TOO_MANY_ATTEMPT = 7
    private val TIME_TO_DISMISS_MESSAGE = 3000L

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
       dialog!!.window!!.requestFeature(Window.FEATURE_NO_TITLE)
       dialog!!.window!!.setBackgroundDrawableResource(android.R.color.transparent)
        return dialog
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_dialog_fingerprint_setting, container, false)
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

    override fun onStop() {
        cancelFingerprintScan()
        AnalyticsManager.trackScreen(AnalyticsScreenName.REGISTER_SET_TOUCH_ID)
        super.onStop()
    }

    private fun initInstances() {
        btn_confirm.setOnClickListener {
            AnalyticsManager.touchIdMainPageClicked()
            MainCustomerActivity.startWithClearStack(context!!)
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
            AnalyticsManager.touchIdScan()
            showSuccess()
        }

        override fun onFailure(failureReason: AuthenticationFailureReason, fatal: Boolean,
                               errorMessage: CharSequence, moduleTag: Int, errorCode: Int) {
            showError(failureReason, fatal, errorMessage, errorCode)
        }
    }

    private fun showError(failureReason: AuthenticationFailureReason, fatal: Boolean, errorMessage: CharSequence, errorCode: Int) {
        img_scan_fail?.visible()
        btn_confirm?.gone()
        txt_fingerprint_description?.text = getString(R.string.fingerprint_scan_fail)

        Handler().postDelayed({
            img_scan_fail?.gone()
            txt_fingerprint_description?.text = getString(R.string.fingerprint_wait_scan)

            if (errorCode == ERROR_TOO_MANY_ATTEMPT) {
                fragmentManager?.let { dismiss() }
                return@postDelayed
            }
        }, TIME_TO_DISMISS_MESSAGE)
    }

    private fun showSuccess() {
        img_scan_fail?.gone()
        img_scan_success?.visible()
        btn_confirm?.visible()
        txt_fingerprint_description?.text = getString(R.string.fingerprint_scan_success)

        Reprint.cancelAuthentication()

        Handler().postDelayed({
            listener.onFingerprintSuccess()
            fragmentManager?.let { dismiss() }
        }, TIME_TO_DISMISS_MESSAGE)
    }

    companion object {
        private val TAG = this::class.java.simpleName

        fun newInstance() = FingerprintSettingDialogFragment()

        fun show(fragmentManager: FragmentManager, listener: FingerprintSettingDialogFragment.Listener) {
            val fragment = newInstance()
            fragment.listener = listener
            fragment.show(fragmentManager!!, TAG)
        }
    }

    interface Listener {
        fun onFingerprintSuccess()
    }
}