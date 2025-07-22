package tlt.th.co.toyotaleasing.modules.editaddress.taxdelivery

import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import kotlinx.android.synthetic.main.fragment_edit_tax_delivery_email.*
import kotlinx.android.synthetic.main.layout_lottie_loading_screen.*
import tlt.th.co.toyotaleasing.R
import tlt.th.co.toyotaleasing.analytics.AnalyticsManager
import tlt.th.co.toyotaleasing.common.base.BaseFragment
import tlt.th.co.toyotaleasing.common.extension.*

class EditTaxDeliveryEmailFragment : BaseFragment(),
        EditTaxEmailSuccessDialogFragment.Listener {

    private val viewModel by lazy {
        ViewModelProviders.of(this).get(EditTaxDeliveryEmailViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_edit_tax_delivery_email, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViewModel()
        initInstances()

        viewModel.getInitialData()
    }

    private fun initViewModel() {
        viewModel.whenLoading.observe(this, Observer {
            toggleLoadingScreenDialog(it!!, this)
        })

        viewModel.whenDataLoaded.observe(this, Observer {
            it?.let {
                setupDataIntoViews(it)
                it.isStaffApp.ifTrue { supportForStaff(it) }
            }
        })

        viewModel.whenEmailInvalidFormat.observe(this, Observer {
            textinput_email.enableErrorMessage(getString(R.string.error_email_pattern))
        })

        viewModel.whenPhonenumberInvalidFormat.observe(this, Observer {
            textinput_phonenumber.enableErrorMessage(getString(R.string.error_not_empty))
        })

        viewModel.whenSubmitFormFailure.observe(this, Observer {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
        })

        viewModel.whenSubmitFormSuccess.observe(this, Observer {
            EditTaxEmailSuccessDialogFragment.show(fragmentManager!!, this)
        })
    }

    private fun initInstances() {
        textinput_email.enableClearErrorWhenTextChanged()
        textinput_phonenumber.enableClearErrorWhenTextChanged()

        btn_confirm.setOnClickListener {
            AnalyticsManager.taxReceiveEmailConfirm()
            val email = edittext_email.text.toString().trim()
            val phoneNumber = edittext_phonenumber.text.toString().trim()

            viewModel.submitForm(email, phoneNumber)
        }
    }

    private fun setupDataIntoViews(it: EditTaxDeliveryEmailViewModel.Model) {
        edittext_email.setText(it.email)
        edittext_phonenumber.setText(it.phoneNumber)
    }

    private fun supportForStaff(it: EditTaxDeliveryEmailViewModel.Model) {

    }

    override fun onEditTaxEmailSuccessConfirmClick() {
        activity?.finish()
    }

    companion object {
        fun newInstance() = EditTaxDeliveryEmailFragment()
    }
}
