package tlt.th.co.toyotaleasing.modules.tax.gas

import android.app.Activity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.core.content.ContextCompat
import android.text.Editable
import android.text.TextWatcher
import kotlinx.android.synthetic.main.activity_tax_gas.*
import kotlinx.android.synthetic.main.layout_lottie_loading_screen.*
import tlt.th.co.toyotaleasing.R
import tlt.th.co.toyotaleasing.analytics.AnalyticsManager
import tlt.th.co.toyotaleasing.common.base.BaseActivity
import tlt.th.co.toyotaleasing.common.extension.*

class GasTaxActivity : BaseActivity() {

    private val viewModel by lazy {
        ViewModelProviders.of(this).get(GasTaxViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tax_gas)

        initViewModel()
        initInstance()

        viewModel.getData()
    }

    private fun initInstance() {
        status_title_tv.text = getString(R.string.attach_ctpl_status_title, getString(R.string.attach_ctpl_not_attach))

        attach_btn.setOnClickListener {
            AnalyticsManager.attachNgvConfirm()
            viewModel.verifyEMSNumber(ems_number_edit_text.text.toString().trim())
        }

        ems_number_edit_text.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (group_ems_sent.isVisible()) {
                    return
                }

                attach_btn.isEnabled = s?.isNotEmpty() ?: false
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
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

        viewModel.whenEmsVerifyFail.observe(this, Observer {
            showToast(message = getString(R.string.gas_upload_fail))
        })

        viewModel.whenEmsVerifySuccess.observe(this, Observer {
            showToast(message = getString(R.string.gas_upload_success))

            finishWithResult()
        })
    }

    private fun setupDataIntoViews(it: GasTaxViewModel.Model) {
        date_tv.text = it.currentDate

        when (it.status) {
            GasTaxViewModel.Status.NOT_ATTACH_DOCUMENT -> {
                status_title_tv.text = getString(R.string.attach_ctpl_status_title, getString(R.string.attach_ctpl_not_attach))
                status_title_tv.setCompoundDrawablesWithIntrinsicBounds(ContextCompat.getDrawable(this, R.drawable.ic_document_warning), null, null, null)
            }
            GasTaxViewModel.Status.WRONG -> {
                status_title_tv.text = getString(R.string.attach_ctpl_status_title, getString(R.string.insurance_status_incorrect))
                status_title_tv.setCompoundDrawablesWithIntrinsicBounds(ContextCompat.getDrawable(this, R.drawable.ic_document_incorrect), null, null, null)
            }
            else -> {
                status_title_tv.text = getString(R.string.attach_ctpl_status_title, getString(R.string.attach_ctpl_attach))
                status_title_tv.setCompoundDrawablesWithIntrinsicBounds(ContextCompat.getDrawable(this, R.drawable.ic_document_pending), null, null, null)
            }
        }

        if (it.defaultEms.isEmpty()) {
            showEmsForm()
        } else {
            showEmsSent()

            textview_ems_number.setOnClickListener {
                showEmsForm()
            }
        }

        ems_number_edit_text.setText(it.defaultEms)
        textview_ems_number.text = it.defaultEms
    }

    private fun supportForStaff(it: GasTaxViewModel.Model) {

    }

    private fun finishWithResult() {
        setResult(Activity.RESULT_OK)
        finish()
    }

    private fun showEmsSent() {
        group_ems_form.gone()
        group_ems_sent.visible()
    }

    private fun showEmsForm() {
        group_ems_sent.gone()
        group_ems_form.visible()
    }

    companion object {
        const val REQUEST_CODE = 102

        fun startWithResult(fragment: Fragment?) {
            val intent = Intent(fragment?.context, GasTaxActivity::class.java)
            fragment?.startActivityForResult(intent, GasTaxActivity.REQUEST_CODE)
        }
    }
}
