package tlt.th.co.toyotaleasing.common.dialog

import android.app.Dialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.FragmentManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import kotlinx.android.synthetic.main.fragment_dialog_callcenter.*
import tlt.th.co.toyotaleasing.R
import tlt.th.co.toyotaleasing.analytics.AnalyticsManager
import tlt.th.co.toyotaleasing.analytics.AnalyticsScreenName
import tlt.th.co.toyotaleasing.common.base.BaseDialogFragment
import tlt.th.co.toyotaleasing.manager.ContextManager


class CallCenterDialogFragment : BaseDialogFragment() {

    private val phoneNumber by lazy {
        arguments?.getString(PHONE_NUMBER, "") ?: ""
    }

    private val displayPhoneNumber by lazy {
        arguments?.getString(DISPLAY_PHONE_NUMBER, "") ?: ""
    }

    private val openBy by lazy {
        arguments?.getInt(OPEN_BY, 0) ?: SELECT_PHONE
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
       dialog!!.window!!.requestFeature(Window.FEATURE_NO_TITLE)
       dialog!!.window!!.setBackgroundDrawableResource(android.R.color.transparent)
        return dialog
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_dialog_callcenter, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        TLTTextView.text = ContextManager.getInstance().getApplicationContext().getString(R.string.callcenter_dialog_description, displayPhoneNumber)

        btn_cancel.setOnClickListener {

            when(openBy) {
                SELECT_PHONE -> { AnalyticsManager.selectPhoneCancelClicked() }
                CONTACT_US_MAIN -> { AnalyticsManager.contactMainCallCenterDialogCancel() }
                LOCATION -> {}
                LOCATION_DETAIL -> { AnalyticsManager.mapDealerDetailCancel() }
            }
            fragmentManager?.let { dismiss() }
        }

        btn_call.setOnClickListener {
            when(openBy) {
                SELECT_PHONE -> { AnalyticsManager.selectPhoneCallClicked() }
                CONTACT_US_MAIN -> { AnalyticsManager.contactMainCallCenterDialogCall() }
                LOCATION -> {}
                LOCATION_DETAIL -> { AnalyticsManager.mapDealerDetailCall() }
            }

            fragmentManager?.let {
                val intent = Intent(Intent.ACTION_DIAL,
                        Uri.fromParts("tel", phoneNumber, null))
                startActivity(intent)
            }
        }
    }

    override fun onResume() {
        super.onResume()
       dialog!!.window!!.setLayout(resources.displayMetrics.widthPixels, resources.displayMetrics.heightPixels)
    }

    override fun onPause() {
        super.onPause()
        AnalyticsManager.trackScreenDialog(AnalyticsScreenName.REGISTER_SELECT_PHONE_CALL)
    }

    companion object {
        val TAG = this::class.java.simpleName
        private const val DISPLAY_PHONE_NUMBER = "displayPhoneNumber"
        private const val PHONE_NUMBER = "phoneNumber"
        private const val OPEN_BY = "openBy"

        const val SELECT_PHONE = 0
        const val LOCATION = 1
        const val CONTACT_US_MAIN = 2
        const val LOCATION_DETAIL = 3

        fun newInstance() = CallCenterDialogFragment()

        fun show(fragmentManager: FragmentManager, phoneNumber: String = "",
                 displayPhoneNumber: String = "",
                 openBy: Int = SELECT_PHONE) {
            newInstance().apply {
                arguments = Bundle().apply {
                    putString(PHONE_NUMBER, phoneNumber)
                    putString(DISPLAY_PHONE_NUMBER, displayPhoneNumber)
                    putInt(OPEN_BY, openBy)
                }
                show(fragmentManager, TAG)
            }
        }
    }
}