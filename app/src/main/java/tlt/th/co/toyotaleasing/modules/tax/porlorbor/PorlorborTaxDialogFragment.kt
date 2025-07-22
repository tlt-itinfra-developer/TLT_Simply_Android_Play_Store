package tlt.th.co.toyotaleasing.modules.tax.porlorbor

import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import kotlinx.android.synthetic.main.fragment_dialog_insurance.*
import tlt.th.co.toyotaleasing.R
import tlt.th.co.toyotaleasing.analytics.AnalyticsManager
import tlt.th.co.toyotaleasing.analytics.AnalyticsScreenName
import tlt.th.co.toyotaleasing.common.base.BaseDialogFragment

class PorlorborTaxDialogFragment : BaseDialogFragment() {

    private val listener by lazy {
        targetFragment as PorlorborTaxDialogFragment.Listener
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
       dialog!!.window!!.requestFeature(Window.FEATURE_NO_TITLE)
       dialog!!.window!!.setBackgroundDrawableResource(android.R.color.transparent)
        return dialog
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_dialog_insurance, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initInstances()
    }

    override fun onResume() {
        super.onResume()
       dialog!!.window!!.setLayout(resources.displayMetrics.widthPixels, resources.displayMetrics.heightPixels)
    }

    override fun onPause() {
        super.onPause()
        AnalyticsManager.trackScreenDialog(AnalyticsScreenName.TAX_COMINSURANCE_SELECTPOPUP)
    }

    private fun initInstances() {
        btn_buy.setOnClickListener {
            AnalyticsManager.attachInspectCerPopupBuy()
            fragmentManager?.let {
                dismiss()
                listener.onBuyPorlorborCicked()
            }
        }

        btn_attach.setOnClickListener {
            AnalyticsManager.attachInspectCerPopupUpload()
            fragmentManager?.let {
                dismiss()
                listener.onAttachPorlorborClicked()
            }
        }
    }

    companion object {
        private val TAG = this::class.java.simpleName!!

        fun newInstance() = PorlorborTaxDialogFragment()

        fun show(fragmentManager: FragmentManager, targetFragment: Fragment) {
            newInstance().apply {
                setTargetFragment(targetFragment, 1)
                show(fragmentManager, TAG)
            }
        }
    }

    interface Listener {
        fun onBuyPorlorborCicked()
        fun onAttachPorlorborClicked()
    }
}