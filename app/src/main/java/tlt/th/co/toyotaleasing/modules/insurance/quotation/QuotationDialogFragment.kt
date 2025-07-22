package tlt.th.co.toyotaleasing.modules.insurance.quotation

import android.app.Dialog
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import kotlinx.android.synthetic.main.fragment_dialog_quotation_notice.*
import tlt.th.co.toyotaleasing.R
import tlt.th.co.toyotaleasing.analytics.AnalyticsManager
import tlt.th.co.toyotaleasing.analytics.AnalyticsScreenName
import tlt.th.co.toyotaleasing.common.base.BaseDialogFragment

class QuotationDialogFragment : BaseDialogFragment() {

    private val viewModel by lazy {
        ViewModelProviders.of(this).get(QuotationDialogViewModel::class.java)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
       dialog!!.window!!.requestFeature(Window.FEATURE_NO_TITLE)
       dialog!!.window!!.setBackgroundDrawableResource(android.R.color.transparent)
        return dialog
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_dialog_quotation_notice, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViewModel()
        initInstances()

        viewModel.getQuotationPolicies()
    }

    override fun onResume() {
        super.onResume()
       dialog!!.window!!.setLayout(resources.displayMetrics.widthPixels, resources.displayMetrics.heightPixels)
    }

    override fun onPause() {
        super.onPause()
        AnalyticsManager.trackScreenDialog(AnalyticsScreenName.INSURANCE_QUOTATION_FROM_TYPE)
    }

    private fun initViewModel() {
        viewModel.whenDataLoaded.observe(this, Observer {
            setupDataIntoViews(it!!)
        })

        viewModel.whenQuotationPoliciesLoaded.observe(this, Observer {
            val adapter = recycler_view.adapter as QuotationDialogAdapter
            adapter.updateItems(it!!)
        })
    }

    private fun setupDataIntoViews(it: QuotationDialogViewModel.Model) {

    }

    private fun initInstances() {
        recycler_view.layoutManager = LinearLayoutManager(context)
        recycler_view.adapter = QuotationDialogAdapter()

        form_quotation_popup_btn_close.setOnClickListener {
            fragmentManager?.let { dismiss() }
        }
    }

    companion object {
        val TAG = this::class.java.simpleName

        fun newInstance() = QuotationDialogFragment()

        fun show(fragmentManager: FragmentManager) {
            newInstance().show(fragmentManager!!, TAG)
        }
    }
}