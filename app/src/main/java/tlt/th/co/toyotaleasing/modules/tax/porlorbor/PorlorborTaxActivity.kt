package tlt.th.co.toyotaleasing.modules.tax.porlorbor

import android.app.Activity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_tax_porlorbor.*
import tlt.th.co.toyotaleasing.R
import tlt.th.co.toyotaleasing.analytics.AnalyticsManager
import tlt.th.co.toyotaleasing.analytics.AnalyticsScreenName
import tlt.th.co.toyotaleasing.common.base.BaseActivity
import tlt.th.co.toyotaleasing.common.extension.gone
import tlt.th.co.toyotaleasing.common.extension.ifTrue
import tlt.th.co.toyotaleasing.common.extension.visible

class PorlorborTaxActivity : BaseActivity() {

    private val viewModel by lazy {
        ViewModelProviders.of(this).get(PorlorborTaxViewModel::class.java)
    }

    private val isPaymentFlow by lazy {
        intent.getBooleanExtra(IS_PAYMENT_FLOW, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tax_porlorbor)

        initViewModel()
        initInstances()

        viewModel.getData(isPaymentFlow)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode != Activity.RESULT_OK) {
            return
        }

        when (requestCode) {
        }
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

    private fun setupDataIntoViews(it: PorlorborTaxViewModel.Model) {
        date_tv.text = it.currentDate

        if (it.isSkip) {
            txt_skip.visible()
        } else {
            txt_skip.gone()
        }

        when (it.status) {
            PorlorborTaxViewModel.Status.NOT_ATTACH_DOCUMENT -> {
                status_title_tv.text = getString(R.string.attach_ctpl_status_title, getString(R.string.attach_ctpl_not_attach))
                status_title_tv.setCompoundDrawablesWithIntrinsicBounds(ContextCompat.getDrawable(this, R.drawable.ic_document_warning), null, null, null)
            }
            PorlorborTaxViewModel.Status.WRONG -> {
                status_title_tv.text = getString(R.string.attach_ctpl_status_title, getString(R.string.insurance_status_incorrect))
                status_title_tv.setCompoundDrawablesWithIntrinsicBounds(ContextCompat.getDrawable(this, R.drawable.ic_document_incorrect), null, null, null)
            }
            else -> {
                status_title_tv.text = getString(R.string.attach_ctpl_status_title, getString(R.string.attach_ctpl_attach))
                status_title_tv.setCompoundDrawablesWithIntrinsicBounds(ContextCompat.getDrawable(this, R.drawable.ic_document_pending), null, null, null)
            }
        }
    }

    private fun supportForStaff(it: PorlorborTaxViewModel.Model) {

    }

    private fun initInstances() {
        attach_btn.setOnClickListener {
            AnalyticsManager.attachComInsuranceConfirm()
            nextScreen(isSaveImage = true)
        }

        attach_upload_widget.setOnAddImageListener {
            AnalyticsManager.attachComInsuranceUpload()

        }

        attach_upload_widget.onImageChanged {
            if (it.isEmpty()) {
                txt_skip.setTextColor(ContextCompat.getColor(this@PorlorborTaxActivity, R.color.brownish_grey))
                attach_btn.isEnabled = false
                return@onImageChanged
            }

            txt_skip.setTextColor(ContextCompat.getColor(this@PorlorborTaxActivity, R.color.brownish_grey_light))
            attach_btn.isEnabled = true
        }

        txt_skip.setOnClickListener {
            AnalyticsManager.attachComInsuranceSkip()
            nextScreen(isSaveImage = false)
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

    override fun onPause() {
        super.onPause()
        AnalyticsManager.trackScreen(AnalyticsScreenName.TAX_COM_INSURANCE_UPLOAD)
    }

    private fun finishWithResult() {
        val imageList: ArrayList<String> = arrayListOf()
        val intent = Intent()
        attach_upload_widget.imageUriList.forEach {
            imageList.add(it.toString())
        }
        intent.putStringArrayListExtra(IMAGE_LIST, imageList)
        setResult(Activity.RESULT_OK, intent)
        finish()
    }

    companion object {
        const val REQUEST_CODE = 100
        const val IS_PAYMENT_FLOW = "isPaymentFlow"
        const val IMAGE_LIST = "imageList"

        fun startWithResult(context: Activity?, isPaymentFlow: Boolean) {
            val intent = Intent(context, PorlorborTaxActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT)
            intent.putExtra(IS_PAYMENT_FLOW, isPaymentFlow)
            context?.startActivityForResult(intent, REQUEST_CODE)
        }

        fun startWithResult(fragment: Fragment?) {
            val intent = Intent(fragment?.context, PorlorborTaxActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT)
            fragment?.startActivityForResult(intent, REQUEST_CODE)
        }
    }
}
