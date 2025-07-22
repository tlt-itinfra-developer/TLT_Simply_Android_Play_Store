package tlt.th.co.toyotaleasing.modules.insurance.hotline.contactus

import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.Toast

import kotlinx.android.synthetic.main.fragment_hotline_contact_us.*
import tlt.th.co.toyotaleasing.R
import tlt.th.co.toyotaleasing.analytics.AnalyticsManager
import tlt.th.co.toyotaleasing.analytics.AnalyticsScreenName
import tlt.th.co.toyotaleasing.common.base.BaseFragment
import tlt.th.co.toyotaleasing.common.dialog.NormalDialogFragment
import tlt.th.co.toyotaleasing.common.extension.gone
import tlt.th.co.toyotaleasing.common.extension.visible
import tlt.th.co.toyotaleasing.modules.tax.ImageUploadViewModel
import tlt.th.co.toyotaleasing.util.ExternalAppUtils

class ContactUsFragment : BaseFragment(), NormalDialogFragment.Listener {
    private val imageUploadViewModel by lazy {
        ViewModelProviders.of(this).get(ImageUploadViewModel::class.java)
    }

    private val viewModel by lazy {
        ViewModelProviders.of(this).get(ContactUsViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_hotline_contact_us, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViewModel()
        initInstances()

        viewModel.getData()
    }

    private fun initInstances() {


        hotline_contact_us_topic_spinner.onItemSelectedListener = object : OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                checkEnableButton()
            }
        }

        edittext_tel.addTextChangedListener(onEditTextChanged)
        hotline_contact_us_detail_et.addTextChangedListener(onEditTextChanged)

        hotline_contact_us_upload_widget.setOnAddImageListener {
        }

        hotline_contact_us_send_btn.setOnClickListener {
            AnalyticsManager.insuranceContactQuestionSendQuestion()
            viewModel.sendContactUsData(
                    edittext_fullname.text.toString(),
                    edittext_tel.text.toString(),
                    edittext_email.text.toString(),
                    hotline_contact_us_topic_spinner.getSelectedPositionWithoutHint(),
                    hotline_contact_us_detail_et.text.toString()
            )
        }

        txt_contact_line_link.setOnClickListener {
            AnalyticsManager.insuranceTibClubLine()
            try {
                ExternalAppUtils.openByLink(context, "")
            } catch (e: Exception) {
                ExternalAppUtils.openByLink(context, "")
                e.printStackTrace()
            }
        }

        txt_contact_call_cente_link.setOnClickListener {
            try {
                ExternalAppUtils.openDialApp(context, "")
            } catch (e: Exception) {

                e.printStackTrace()
            }
        }

        txt_company_website_link.setOnClickListener {
            try {
                ExternalAppUtils.openByLink(context, "")
            } catch (e: Exception) {
                ExternalAppUtils.openByLink(context, "")
            }
        }

    }

    private fun initViewModel() {
        viewModel.whenLoading.observe(this, Observer {
            toggleLoadingScreenDialog(it!!, this)
        })

        viewModel.whenDataLoaded.observe(this, Observer {
            setupDataIntoViews(it!!)
        })

        viewModel.whenSendTIBQuestionFail.observe(this, Observer {
            Toast.makeText(context, "SendTIBQuestion Fail", Toast.LENGTH_SHORT).show()
        })

        viewModel.whenSendTIBQuestionSuccess.observe(this, Observer { tcaId ->
            imageUploadViewModel.uploadByTIBAsk(
                    base64List = hotline_contact_us_upload_widget.getImageList(),
                    tcaId = tcaId ?: ""
            )
        })

        imageUploadViewModel.whenImageUploading.observe(this, Observer {
            toggleLoadingScreenDialog(it!!, this)
        })

        imageUploadViewModel.whenImageUploadFail.observe(this, Observer {

        })

        imageUploadViewModel.whenImageUploadSuccess.observe(this, Observer {
            NormalDialogFragment.show(
                    fragmentManager = fragmentManager!!,
                    description = getString(R.string.quotation_staff_contact_message_dialog),
                    confirmButtonMessage = getString(R.string.refinance_diaglog_btn_confirm)
            )
        })
    }

    private fun setupDataIntoViews(it: ContactUsViewModel.Model) {
        edittext_fullname.setText(it.name)
        edittext_tel.setText(it.tel)
        edittext_email.setText(it.email)

        hotline_contact_us_topic_spinner.setItems(it.contactTitleList)

        when (it.status) {
            ContactUsViewModel.Status.CUSTOMER -> {
                customerStatus()
            }
            else -> {
                nonCustomerStatus()
            }
        }
    }

    override fun onDialogConfirmClick() {
        activity?.finish()
    }

    override fun onDialogCancelClick() {

    }

    private fun customerStatus() {
        group_customer.gone()
    }

    private fun nonCustomerStatus() {
        group_customer.visible()
    }

    private fun checkEnableButton() {
        val phonenumber = edittext_tel.text.toString().trim()
        val detail = hotline_contact_us_detail_et.text.toString().trim()
        val position = hotline_contact_us_topic_spinner.selectedItemPosition

        val isEnable = phonenumber.isNotEmpty() && detail.isNotEmpty() && position != 0

        hotline_contact_us_send_btn.isEnabled = isEnable
    }

    private val onEditTextChanged = object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            checkEnableButton()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
    }

    companion object {
        fun newInstance() = ContactUsFragment()
    }
}