package tlt.th.co.toyotaleasing.modules.contract.refinance

import android.app.Dialog
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.FragmentManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import kotlinx.android.synthetic.main.fragment_dialog_refinance.*
import tlt.th.co.toyotaleasing.R
import tlt.th.co.toyotaleasing.analytics.AnalyticsManager
import tlt.th.co.toyotaleasing.analytics.AnalyticsScreenName
import tlt.th.co.toyotaleasing.common.base.BaseDialogFragment

class RefinanceDialogFragment : BaseDialogFragment() {

    private val viewModel by lazy {
        ViewModelProviders.of(this).get(RefinanceDialogViewModel::class.java)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
       dialog!!.window!!.requestFeature(Window.FEATURE_NO_TITLE)
       dialog!!.window!!.setBackgroundDrawableResource(android.R.color.transparent)
        return dialog
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_dialog_refinance, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        btn_confirm.setOnClickListener {
            AnalyticsManager.payoffInterestEnterClicked()
            AnalyticsManager.refinanceInterestEnterClicked()
            viewModel.requestRefinance()
            fragmentManager?.let { dismiss() }
        }
    }

    override fun onResume() {
        super.onResume()
       dialog!!.window!!.setLayout(resources.displayMetrics.widthPixels, resources.displayMetrics.heightPixels)
    }

    override fun onPause() {
        super.onPause()
        AnalyticsManager.trackScreenDialog(AnalyticsScreenName.INSTALLMENT_REFINANCE_INTEREST)
        AnalyticsManager.trackScreenDialog(AnalyticsScreenName.INSTALLMENT_PAYOFF_INTEREST)
    }

    companion object {
        val TAG = this::class.java.simpleName

        fun newInstance() = RefinanceDialogFragment()

        fun show(fragmentManager: FragmentManager) {
            newInstance().show(fragmentManager!!, TAG)
        }
    }
}