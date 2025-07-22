package tlt.th.co.toyotaleasing.modules.tax

import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_tax_document_delivery.*
import kotlinx.android.synthetic.main.layout_tax_document_delivery_sending.*
import tlt.th.co.toyotaleasing.R
import tlt.th.co.toyotaleasing.analytics.AnalyticsManager
import tlt.th.co.toyotaleasing.analytics.AnalyticsScreenName
import tlt.th.co.toyotaleasing.common.base.BaseActivity
import tlt.th.co.toyotaleasing.common.extension.gone
import tlt.th.co.toyotaleasing.common.extension.ifTrue
import tlt.th.co.toyotaleasing.common.extension.visible
import android.content.ClipData
import kotlinx.android.synthetic.main.layout_tax_document_delivery_no_data.view.*
import tlt.th.co.toyotaleasing.util.ExternalAppUtils
import androidx.appcompat.app.AlertDialog
import android.view.LayoutInflater
import android.view.Gravity
import android.view.WindowManager
import kotlinx.android.synthetic.main.fragment_dialog_ems_copied.view.*
import java.net.URLEncoder


class TaxDocumentDeliveryActivity : BaseActivity() {

    private val viewModel by lazy {
        ViewModelProviders.of(this).get(TaxDocumentDeliveryViewModel::class.java)
    }

    private val isLock by lazy {
        intent.getBooleanExtra(FLAG_IS_LOCK, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tax_document_delivery)

        initViewModel()
        initInstances()

    }

    override fun onPause() {
        super.onPause()
        AnalyticsManager.trackScreen(AnalyticsScreenName.TAX_LABEL_DELIVERY_STATUS)
    }

    private fun initViewModel() {
        viewModel.whenLoading.observe(this, Observer {
            toggleLoadingScreenDialog(it!!)
        })

        viewModel.whenDataLoaded.observe(this, Observer {
            it?.let {
                setupDataIntoViews(it)
                it.isStaffApp.ifTrue { supportForStaff(it) }
            }
        })
    }

    private fun setupDataIntoViews(it: TaxDocumentDeliveryViewModel.Model) {
        txt_delivery_date.text = it.deliveryDate
        if (it.registerNumber.isNullOrEmpty()) {
            txt_register_number.text = ""
        } else {
            txt_register_number.text = getString(R.string.tax_document_register_number, it.registerNumber)

            val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager?
            val clip = ClipData.newPlainText("Copied",  it.registerNumber)
            if (clipboard != null && clip != null)
                clipboard!!.setPrimaryClip(clip!!)
//            var clipboardManager = ( getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager ).primaryClip
//            val clipData = ClipData.newPlainText("Copied", it.registerNumber)
//            clipboardManager = clipData
            showCopied()
        }
        txt_address_to_delivery.text = it.addressToDelivery
//        txt_thaipost_track_website.setOnClickListener { AnalyticsManager.taxLabelPostlink() }

        var thaiPostUrl = it.thaipostTrackWebsite
        txt_thaipost_track_website.setOnClickListener {
            TaxWebActivity.startExternalWeb(this@TaxDocumentDeliveryActivity , thaiPostUrl  )
        }

        title_error_tax_label.text = ""
        txt_error_description.gone()
        when (it.deliverStatus) {
            TaxDocumentDeliveryViewModel.Status.NOT_IN_PROCESS -> {
                state_delivery.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.traking_non_process))
                txt_in_between_proceed.text = ""
                txt_transport_tax_label.text = ""
                layout_tax_document_no_data.visible()
                layout_tax_document_sending.gone()
            }
            TaxDocumentDeliveryViewModel.Status.PAY -> {
                state_delivery.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.traking_start_process))
                txt_in_between_proceed.text = it.inBetweenProceed
                txt_transport_tax_label.text = it.transportTaxLabel
//                layout_tax_document_no_data.gone()
//                layout_tax_document_sending.visible()
                layout_tax_document_no_data.visible()
                layout_tax_document_sending.gone()
            }
            TaxDocumentDeliveryViewModel.Status.HOLD -> {
                state_delivery.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.traking_error_process))
                txt_in_between_proceed.text = it.inBetweenProceed
                txt_transport_tax_label.text = it.transportTaxLabel
                title_error_tax_label.text = it.holdMsg
                txt_error_tax_label.text = it.holdDate
                if(it.holdDesc == "" )
                    txt_error_description.gone()
                else
                    txt_error_description.visible()
                txt_error_description.text = it.holdDesc
                layout_tax_document_no_data.visible()
                layout_tax_document_sending.gone()
            }
            TaxDocumentDeliveryViewModel.Status.AFTER_PAY -> {
                state_delivery.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.traking_end_process))
                txt_in_between_proceed.text = it.inBetweenProceed
                txt_transport_tax_label.text = it.transportTaxLabel
                layout_tax_document_no_data.gone()
                layout_tax_document_sending.visible()
            }
        }

        layout_tax_document_no_data.txt_contact_tlt.setOnClickListener{
            try {
                val textLine = URLEncoder.encode("งานทะเบียน", "UTF-8");
                ExternalAppUtils.openByLink(this@TaxDocumentDeliveryActivity, ""+textLine)
//                ExternalAppUtils.openByLink(activity!!, "http://nav.cx/oYc9GcR")
            } catch (e: Exception) {
                val textLine = URLEncoder.encode("งานทะเบียน", "UTF-8");
                ExternalAppUtils.openByLink(this@TaxDocumentDeliveryActivity, ""+textLine)
                e.printStackTrace()
            }
        }


    }

    private fun showCopied() {
        try {

            val mDialogView = LayoutInflater.from(this).inflate(R.layout.fragment_dialog_ems_copied, null)
            val mBuilder = AlertDialog.Builder(this,R.style.dialogBlur).setView(mDialogView)
            val mAlertDialog = mBuilder.show()
            if (mAlertDialog.getWindow() != null) {
                val window = mAlertDialog.getWindow()
                window!!.setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT)
                window!!.setGravity(Gravity.CENTER)
            }
            mDialogView.lay_cpoied.setOnClickListener {
                mAlertDialog.dismiss()
            }

        } catch (e: Exception) {
            e.message
        }
    }



    private fun initInstances() {
        viewModel.getTaxDocumentDeliveryData()
    }

    private fun supportForStaff(it: TaxDocumentDeliveryViewModel.Model) {

    }

    companion object {
        const val FLAG_IS_LOCK = "isLockScreen"
        fun open(context: Context, isLock: Boolean) {
            val intent = Intent(context, TaxDocumentDeliveryActivity::class.java)
            intent.putExtra(FLAG_IS_LOCK, isLock)
            context.startActivity(intent)
        }
    }
}
