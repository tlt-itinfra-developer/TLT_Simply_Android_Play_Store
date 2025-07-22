package tlt.th.co.toyotaleasing.modules.tax.torloraor

import android.app.Activity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.core.content.ContextCompat

import kotlinx.android.synthetic.main.activity_tax_torloraor.*
import tlt.th.co.toyotaleasing.R
import tlt.th.co.toyotaleasing.analytics.AnalyticsManager
import tlt.th.co.toyotaleasing.analytics.AnalyticsScreenName
import tlt.th.co.toyotaleasing.common.base.BaseActivity
import tlt.th.co.toyotaleasing.common.extension.gone
import tlt.th.co.toyotaleasing.common.extension.ifTrue
import tlt.th.co.toyotaleasing.common.extension.visible
import tlt.th.co.toyotaleasing.modules.tax.porlorbor.PorlorborTaxActivity
import tlt.th.co.toyotaleasing.util.ExternalAppUtils
import java.net.URLEncoder

class TorloraorTaxActivity : BaseActivity() {

    private val viewModel by lazy {
        ViewModelProviders.of(this).get(TorloraorTaxViewModel::class.java)
    }

    private val isPaymentFlow by lazy {
        intent.getBooleanExtra(IS_PAYMENT_FLOW, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tax_torloraor)

        initViewModel()
        initInstances()

        viewModel.getData(isPaymentFlow)
    }

    private fun initViewModel() {
        viewModel.whenDataLoaded.observe(this, Observer {
            it?.let {
                setupDataIntoViews(it)
                it.isStaffApp.ifTrue { supportForStaff(it) }
            }
        })

        viewModel.whenImagesLoaded.observe(this, Observer {
            imageHistories.setData(it!!)
        })
    }

    private fun initInstances() {
        status_title_tv.text = getString(R.string.attach_inspection_status_title, getString(R.string.attach_inspection_attach))

        attach_btn.setOnClickListener {
            AnalyticsManager.attachInspectCerConfirm()
            nextScreen(isSaveImage = true)
        }

        attach_upload_widget.setOnAddImageListener {
            AnalyticsManager.attachInspectCerUpload()
        }

        attach_upload_widget.onImageChanged {
            if (it.isEmpty()) {
                txt_skip.setTextColor(ContextCompat.getColor(this@TorloraorTaxActivity, R.color.brownish_grey))
                attach_btn.isEnabled = false
                return@onImageChanged
            }

            txt_skip.setTextColor(ContextCompat.getColor(this@TorloraorTaxActivity, R.color.brownish_grey_light))
            attach_btn.isEnabled = true
        }

        txt_skip.setOnClickListener {
            AnalyticsManager.attachInspectCerSkip()
            nextScreen(isSaveImage = false)
        }

        val textLine = URLEncoder.encode("งานทะเบียน", "UTF-8");
        attach_via_line.setOnClickListener {
            AnalyticsManager.attachInspectCerLine()
            try {
                ExternalAppUtils.openByLink(this@TorloraorTaxActivity, "")
            } catch (e: Exception) {
                ExternalAppUtils.openByLink(this@TorloraorTaxActivity, "")
                e.printStackTrace()
            }
        }

        attach_via_email.setOnClickListener {
            AnalyticsManager.attachInspectCerEmail()
            sendEmail()
        }

    }

    override fun onPause() {
        super.onPause()
        AnalyticsManager.trackScreen(AnalyticsScreenName.TAX_INSPECTIONCER_UPLOAD)
    }

    private fun sendEmail() {
        val emailUrl = ""
        val emailIntent = Intent(Intent.ACTION_VIEW)
        emailIntent.data = Uri.parse(emailUrl)
        startActivity(emailIntent)
    }

    private fun supportForStaff(it: TorloraorTaxViewModel.Model) {

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode != RESULT_OK) {
            return
        }

        when (requestCode) {

        }
    }

    private fun nextScreen(isSaveImage: Boolean = false) {
        val items = mutableListOf<String>()

        viewModel.whenImagesLoaded.value?.forEach { history ->
            history.images.forEach { base64 ->
                items.add(base64)
            }
        }

        val newImages = if (isSaveImage) {
            attach_upload_widget.getImageList()
        } else {
            listOf()
        }

        items.addAll(newImages)

        viewModel.saveDocumentState(newImages)
        viewModel.saveDocument(items)

        finishWithResult()
    }

    private fun setupDataIntoViews(it: TorloraorTaxViewModel.Model) {
        date_tv.text = it.currentDate

        if (it.isSkip) {
            txt_skip.visible()
        } else {
            txt_skip.gone()
        }

        when (it.status) {
            TorloraorTaxViewModel.Status.NOT_ATTACH_DOCUMENT -> {
                status_title_tv.text = getString(R.string.attach_ctpl_status_title, getString(R.string.attach_ctpl_not_attach))
                status_title_tv.setCompoundDrawablesWithIntrinsicBounds(ContextCompat.getDrawable(this, R.drawable.ic_document_warning), null, null, null)
            }
            TorloraorTaxViewModel.Status.WRONG -> {
                status_title_tv.text = getString(R.string.attach_ctpl_status_title, getString(R.string.insurance_status_incorrect))
                status_title_tv.setCompoundDrawablesWithIntrinsicBounds(ContextCompat.getDrawable(this, R.drawable.ic_document_incorrect), null, null, null)
            }
            else -> {
                status_title_tv.text = getString(R.string.attach_ctpl_status_title, getString(R.string.attach_ctpl_attach))
                status_title_tv.setCompoundDrawablesWithIntrinsicBounds(ContextCompat.getDrawable(this, R.drawable.ic_document_pending), null, null, null)
            }
        }
    }

    private fun finishWithResult() {
        val imageList: ArrayList<String> = arrayListOf()
        val intent = Intent()
        attach_upload_widget.imageUriList.forEach {
            imageList.add(it.toString())
        }
        intent.putStringArrayListExtra(PorlorborTaxActivity.IMAGE_LIST, imageList)
        setResult(Activity.RESULT_OK, intent)
        finish()
    }

    companion object {
        const val REQUEST_CODE = 101
        const val IS_PAYMENT_FLOW = "isPaymentFlow"

        fun startWithResult(context: Activity?, isPaymentFlow: Boolean) {
            val intent = Intent(context, TorloraorTaxActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT)
            intent.putExtra(IS_PAYMENT_FLOW, isPaymentFlow)
            context?.startActivityForResult(intent, TorloraorTaxActivity.REQUEST_CODE)
        }

        fun startWithResult(fragment: Fragment?) {
            val intent = Intent(fragment?.context, TorloraorTaxActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT)
            fragment?.startActivityForResult(intent, TorloraorTaxActivity.REQUEST_CODE)
        }
    }
}
