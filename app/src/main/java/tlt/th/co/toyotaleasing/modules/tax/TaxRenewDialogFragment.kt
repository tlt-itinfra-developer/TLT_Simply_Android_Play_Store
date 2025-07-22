package tlt.th.co.toyotaleasing.modules.tax

import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import kotlinx.android.synthetic.main.fragment_dialog_tax_renew.*
import tlt.th.co.toyotaleasing.R
import tlt.th.co.toyotaleasing.analytics.AnalyticsManager
import tlt.th.co.toyotaleasing.analytics.AnalyticsScreenName
import tlt.th.co.toyotaleasing.common.base.BaseDialogFragment
import tlt.th.co.toyotaleasing.manager.LocalizeManager
import tlt.th.co.toyotaleasing.util.CalendarUtils

class TaxRenewDialogFragment : BaseDialogFragment() {

    private val listener by lazy {
        targetFragment as Listener
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
       dialog!!.window!!.requestFeature(Window.FEATURE_NO_TITLE)
       dialog!!.window!!.setBackgroundDrawableResource(android.R.color.transparent)
        return dialog
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_dialog_tax_renew, container, false)
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
        AnalyticsManager.trackScreenDialog(AnalyticsScreenName.TAX_UPDATE_YEAR)
    }

    private fun initInstances() {
        val yearList = Array(2) { i ->
            val currentThaiYear = CalendarUtils.getCurrentYear().toInt()
            if (LocalizeManager.isThai()) {
                (currentThaiYear + 543 + 1 - i).toString()
            } else {
                (currentThaiYear + 1 - i).toString()
            }
        }

        filter_picker.apply {
            minValue = 0
            maxValue = yearList.size - 1
            displayedValues = yearList
            wrapSelectorWheel = false
        }

        btn_confirm.setOnClickListener {
            fragmentManager?.let { dismiss() }
            listener.onTaxRenewDialogResult(yearList[filter_picker.value])
        }
    }

    companion object {
        private const val TAG = "TaxRenewDialogFragment"

        fun newInstance() = TaxRenewDialogFragment()

        fun show(fragmentManager: FragmentManager, targetFragment: Fragment) {
            newInstance().apply {
                setTargetFragment(targetFragment, 1)
                show(fragmentManager, TAG)
            }
        }
    }

    interface Listener {
        fun onTaxRenewDialogResult(year: String)
    }
}