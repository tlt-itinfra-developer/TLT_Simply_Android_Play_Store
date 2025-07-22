package tlt.th.co.toyotaleasing.modules.pincode

import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import android.text.InputType
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_forgot_pincode.*
import tlt.th.co.toyotaleasing.R
import tlt.th.co.toyotaleasing.common.base.BaseActivity
import tlt.th.co.toyotaleasing.common.extension.enableErrorMessage
import tlt.th.co.toyotaleasing.common.extension.gone
import tlt.th.co.toyotaleasing.common.extension.ifTrue
import tlt.th.co.toyotaleasing.common.extension.visible
import tlt.th.co.toyotaleasing.manager.ContextManager
import tlt.th.co.toyotaleasing.modules.register.SelectRegisterDialogFragment
import tlt.th.co.toyotaleasing.modules.register.SimplyCardDialogFragment
import tlt.th.co.toyotaleasing.modules.selectphone.SelectPhoneActivity

class ForgotPincodeActivity : BaseActivity(), SelectRegisterDialogFragment.Listener {

    private val viewModel by lazy {
        ViewModelProviders.of(this).get(ForgotPincodeViewModel::class.java)
    }
    private var currentFlow = TYPE_SIMPLY

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_pincode)

        initViewModel()
        initInstances()

        viewModel.getData()
    }

    private fun initViewModel() {
        viewModel.whenDataLoaded.observe(this, Observer {
            it?.let {
                edittext_id_card.setText(it.idCard)
                it.isStaffApp.ifTrue { supportForStaff(it) }
            }
        })

        viewModel.whenLoading.observe(this, Observer {
            toggleLoadingScreenDialog(it!!)
        })

        viewModel.whenFormInvalid.observe(this, Observer {
            textinput_simply_id.enableErrorMessage(getString(R.string.error_simplycard_pattern))
        })

        // siri 25/10/2019
        viewModel.whenSimplyCardInvalid.observe(this, Observer {
            textinput_simply_id.enableErrorMessage(getString(R.string.error_simplycard_pattern))
        })

        viewModel.whenContractInvalid.observe(this, Observer {
            textinput_simply_id.enableErrorMessage(getString(R.string.error_contract_id_pattern))
        })

        viewModel.whenIdCardInvalid.observe(this, Observer {
            textinput_simply_id.enableErrorMessage(getString(R.string.error_idcard_passport_pattern))
        })

        viewModel.whenForgotPincideFalure.observe(this, Observer {
            //            Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
                AlertDialog.Builder(this)
                        .setTitle(R.string.get_phone_error_title)
                        .setMessage(R.string.get_phone_error_description)
                        .setPositiveButton(R.string.dialog_button_ok) { dialog, _ ->
                           dialog!!.dismiss()
                        }
                        .show()

        })

        viewModel.whenForgotPincideSuccess.observe(this, Observer {
            viewModel.getPhonenumberList()
//            SelectPhoneActivity.start(this@ForgotPincodeActivity)
        })

        viewModel.whenPhoneNumberListLoaded.observe(this, Observer {
            SelectPhoneActivity.open(this@ForgotPincodeActivity, it!!)
        })
    }

    private fun initInstances() {
        toolbar.setOnRightMenuTitleClickListener {
            SelectRegisterDialogFragment.show(supportFragmentManager)
        }

        btn_confirm.setOnClickListener {
//            val idCard = edittext_id_card.text.toString().trim()

            val idCard = when (currentFlow) {
                TYPE_SIMPLY -> edittext_id_card.text.toString().trim()
                TYPE_ID -> edittext_id_card.text.toString().trim()
                TYPE_PASSPORT -> edittext_id_card.text.toString().trim()
                else -> edittext_id_card.text.toString().trim()
            }

            viewModel.formSubmit(idCard ,currentFlow )
        }

        notice_dialog_fragment_btn.setOnClickListener {
            SimplyCardDialogFragment.show(supportFragmentManager)
        }
    }

    override fun onSimplyIdTypeClicked() {
        currentFlow = TYPE_SIMPLY
        txt_welcome.text = ContextManager.getInstance().getStringByRes(R.string.forgotpincode_title_header)
        image_1.visible()
        page_notice_register_form.visible()
        notice_dialog_fragment_btn.visible()
        textinput_simply_id.hint = ContextManager.getInstance().getStringByRes(R.string.registerform_customer_id_hint)
        textinput_simply_id.enableErrorMessage("")
        edittext_id_card.hint = ContextManager.getInstance().getStringByRes(R.string.registerform_customer_id_hint)
        edittext_id_card.setText("")
        edittext_id_card.inputType = InputType.TYPE_CLASS_NUMBER
        txt_description.gone()
    }

    override fun onIdCardTypeClicked() {
        currentFlow = TYPE_ID
        txt_welcome.text = ContextManager.getInstance().getStringByRes(R.string.forgotpincode_title_id_card)
        image_1.gone()
        page_notice_register_form.gone()
        notice_dialog_fragment_btn.gone()
        textinput_simply_id.hint = ContextManager.getInstance().getStringByRes(R.string.registerform_id_card_hint)
        textinput_simply_id.enableErrorMessage("")
        edittext_id_card.hint = ContextManager.getInstance().getStringByRes(R.string.registerform_id_card_hint)
        edittext_id_card.setText("")
        edittext_id_card.inputType = InputType.TYPE_CLASS_NUMBER
        txt_description.visible()
    }

    override fun onContractNumberTypeClicked() {
        currentFlow = TYPE_CONTRACT
        txt_welcome.text = ContextManager.getInstance().getStringByRes(R.string.forgotpincode_title_contract)
        image_1.gone()
        page_notice_register_form.gone()
        notice_dialog_fragment_btn.gone()
        textinput_simply_id.hint = ContextManager.getInstance().getStringByRes(R.string.registerform_contract_hint)
        textinput_simply_id.enableErrorMessage("")
        edittext_id_card.hint = ContextManager.getInstance().getStringByRes(R.string.registerform_contract_hint)
        edittext_id_card.setText("")
        edittext_id_card.inputType = InputType.TYPE_CLASS_TEXT
        txt_description.gone()
    }

    private fun supportForStaff(it: ForgotPincodeViewModel.Model) {

    }

    companion object {

        const val TYPE_SIMPLY = "SIMPLY_ID"
        const val TYPE_ID = "ID_CARD"
        const val TYPE_PASSPORT = "PASSPORT"
        const val TYPE_CONTRACT = "CONTRACT_NUMBER"

        fun start(context: Context?) {
            val intent = Intent(context, ForgotPincodeActivity::class.java)
            context?.startActivity(intent)
        }
    }
}
