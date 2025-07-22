package tlt.th.co.toyotaleasing.modules.paymenthistory

import android.Manifest
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener
import kotlinx.android.synthetic.main.fragment_payment_history.*
import kotlinx.android.synthetic.main.layout_mycar_status_legal_other.*
import tlt.th.co.toyotaleasing.R
import tlt.th.co.toyotaleasing.analytics.AnalyticsManager
import tlt.th.co.toyotaleasing.analytics.AnalyticsScreenName
import tlt.th.co.toyotaleasing.common.base.BaseFragment
import tlt.th.co.toyotaleasing.common.extension.gone
import tlt.th.co.toyotaleasing.common.extension.visible
import tlt.th.co.toyotaleasing.manager.LocalizeManager
import tlt.th.co.toyotaleasing.modules.notify.FilterDialogFragment
import tlt.th.co.toyotaleasing.util.PdfUtils


class PaymentHistoryFragment : BaseFragment(), PaymentHistoryAdapter.Listener {

    private var yearFilter: String = ""

    private val viewModel by lazy {
        ViewModelProviders.of(this).get(PaymentHistoryViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_payment_history, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViewModel()
        initInstances()
    }

    private fun initInstances() {
        recycler_view.layoutManager = LinearLayoutManager(context)
        recycler_view.adapter = PaymentHistoryAdapter(this)
    }

    private fun initViewModel() {
        viewModel.whenLoading.observe(this, Observer {
            toggleLoadingScreenDialog(it!!, this)
        })

        viewModel.whenDataLoaded.observe(this, Observer {
            setupDataIntoViews(it!!)
        })

        viewModel.whenOpenPdf.observe(this, Observer {
            PdfUtils.openPdf(context, it)
        })

        viewModel.whenLockData.observe(this, Observer {
            if (it!!) {
                recycler_view.gone()
                layout_not_found.visible()
            }
        })
    }

    private fun setupDataIntoViews(item: PaymentHistoryViewModel.Model) {
        txt_car_license.text = item.carLicense
        date.text = item.date


        recycler_view.adapter.apply {
            val adapter = this as PaymentHistoryAdapter
            adapter.updateItems(item.notifyList)
        }

        when (item.status) {
            PaymentHistoryViewModel.Status.FOUND -> {
                recycler_view.visible()
                layout_not_found.gone()
            }
            PaymentHistoryViewModel.Status.LOCK -> {
                recycler_view.gone()
                layout_not_found.visible()
            }
            else -> {
                layout_not_found.visible()
                recycler_view.gone()
                img.gone()
                txt_legal_description.visible()
                txt_legal_description.text = activity!!.getString(R.string.payment_history_no_data)
                txt_legal_description.setOnClickListener {
                    AnalyticsManager.historyCall()
                }
            }
        }

        btn_filter.setOnClickListener {
            AnalyticsManager.historyFilter()
            FilterDialogFragment.show(
                    fragmentManager!!,
                    getString(R.string.notify_filter_dialog_title),
                    item.filterList,
                    onResultFilterListener
            )
        }
    }

    private val onResultFilterListener = object : FilterDialogFragment.Listener {
        override fun onResult(index: Int, selectContent: String) {

            AnalyticsManager.historyFilterYear()
            AnalyticsManager.historyFilterDone()

            yearFilter = if (selectContent == "ทั้งหมด" || selectContent == "All") {
                ""
            } else {
                if (LocalizeManager.isThai()) {
                    (selectContent.toInt() - 543).toString()
                } else {
                    selectContent
                }
            }
            txt_filter.text = selectContent
            viewModel.getPaymentHistories(yearFilter = yearFilter)
        }
    }

    override fun onDownloadReceipt(position: Int) {
        Dexter.withActivity(activity)
                .withPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .withListener(object : PermissionListener {
                    override fun onPermissionGranted(response: PermissionGrantedResponse) {
                        viewModel.downloadReceipt(position)
                    }

                    override fun onPermissionDenied(response: PermissionDeniedResponse) {

                    }

                    override fun onPermissionRationaleShouldBeShown(permission: PermissionRequest, token: PermissionToken) {
                        token.continuePermissionRequest()
                    }
                }).check()
    }

    companion object {
        fun newInstance() = PaymentHistoryFragment()
    }
}
