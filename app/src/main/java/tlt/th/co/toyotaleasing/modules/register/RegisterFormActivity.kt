package tlt.th.co.toyotaleasing.modules.register

import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import android.text.InputType
import kotlinx.android.synthetic.main.activity_register_form.*
import tlt.th.co.toyotaleasing.R
import tlt.th.co.toyotaleasing.analytics.AnalyticsManager
import tlt.th.co.toyotaleasing.analytics.AnalyticsScreenName
import tlt.th.co.toyotaleasing.common.base.BaseActivity
import tlt.th.co.toyotaleasing.common.extension.enableErrorMessage
import tlt.th.co.toyotaleasing.common.extension.gone
import tlt.th.co.toyotaleasing.common.extension.ifTrue
import tlt.th.co.toyotaleasing.common.extension.visible
import tlt.th.co.toyotaleasing.manager.ContextManager
import tlt.th.co.toyotaleasing.modules.selectphone.PhoneNumber
import tlt.th.co.toyotaleasing.modules.selectphone.SelectPhoneActivity


class RegisterFormActivity : BaseActivity(), SelectRegisterDialogFragment.Listener {

    private lateinit var phoneList: ArrayList<PhoneNumber>
    private var currentFlow = TYPE_SIMPLY

    private val viewModel by lazy {
        ViewModelProviders.of(this).get(RegisterFormViewModel::class.java)
    }

    override fun onPause() {
        super.onPause()
        AnalyticsManager.trackScreen(AnalyticsScreenName.REGISTER_CUSTOMER_INFO)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register_form)

        initVideModel()
        initInstances()

        viewModel.getDefaultData()
    }

    private fun initVideModel() {
        viewModel.whenLoading.observe(this, Observer {
            toggleLoadingScreenDialog(it!!)
        })

        viewModel.whenGetDefaultData.observe(this, Observer {
            it?.let {
                input_register_form_email.setText(it.email)
                input_register_form_citizen_id.setText(it.idCard)

                it.isStaffApp.ifTrue { supportForStaff(it) }
            }
        })

        viewModel.whenEmailInvalid.observe(this, Observer {
            textinput_email.enableErrorMessage(getString(R.string.error_email_pattern))
        })

//        viewModel.whenIdCardInvalid.observe(this, Observer {
//            textinput_simply_id.enableErrorMessage(getString(R.string.error_idcard_pattern))
//        })

         // siri 25/10/2019
        viewModel.whenSimplyCardInvalid.observe(this, Observer {
            textinput_simply_id.enableErrorMessage(getString(R.string.error_simplycard_pattern))
        })

        viewModel.whenContractInvalid.observe(this, Observer {
            textinput_contract.enableErrorMessage(getString(R.string.error_contract_id_pattern))
        })

        viewModel.whenIdCardInvalid.observe(this, Observer {
            textinput_id_card.enableErrorMessage(getString(R.string.error_idcard_passport_pattern))
        })



        viewModel.whenPrepareDataLoaded.observe(this, Observer {
            if (phoneList.isNotEmpty()) {
                SelectPhoneActivity.open(this@RegisterFormActivity, phoneList)
            }
        })

        viewModel.whenPhoneNumberListLoaded.observe(this, Observer {
            this.phoneList = it!!
            val email = input_register_form_email.text.toString().trim()
            val simplyId = when (currentFlow) {
                TYPE_SIMPLY -> input_register_form_citizen_id.text.toString().trim()
                TYPE_ID -> input_register_form_id_card.text.toString().trim()
                TYPE_PASSPORT -> input_register_form_id_card.text.toString().trim()
                else -> input_register_form_contract.text.toString().trim()
            }

            viewModel.register(email, simplyId)
        })

        viewModel.whenGetPhoneNumberError.observe(this, Observer {
            if (it!!) {
                AnalyticsManager.trackScreenError(AnalyticsScreenName.REGISTER_CUSTOMER_INFO_ERROR)
                AlertDialog.Builder(this)
                        .setTitle(R.string.get_phone_error_title)
                        .setMessage(R.string.get_phone_error_description)
                        .setPositiveButton(R.string.dialog_button_ok) { dialog, _ ->
                           dialog!!.dismiss()
                        }
                        .show()
            }
        })
    }

    private fun initInstances() {
        onSimplyIdTypeClicked()
        toolbar.setOnRightMenuTitleClickListener {
            SelectRegisterDialogFragment.show(supportFragmentManager)
        }

        textinput_email.setOnClickListener {
            AnalyticsManager.formCustomerInformationEmail(input_register_form_email.text.toString().trim())
        }
        textinput_simply_id.setOnClickListener {
            AnalyticsManager.formCustomerInformationIdPassport(input_register_form_citizen_id.text.toString().trim())
        }

        btn_submit_register_form.setOnClickListener {
            AnalyticsManager.formCustomerInformationNext()
            val email = input_register_form_email.text.toString().trim()
            val simplyId = when (currentFlow) {
                TYPE_SIMPLY -> input_register_form_citizen_id.text.toString().trim()
                TYPE_ID -> input_register_form_id_card.text.toString().trim()
                TYPE_PASSPORT -> input_register_form_id_card.text.toString().trim()
                else -> input_register_form_contract.text.toString().trim()
            }

            viewModel.getPhonenumberList(email, simplyId, currentFlow)
        }

        radio_input_type.setOnCheckedChangeListener { _, _ ->
            if (radio_id_card_number.isChecked) {
                setCitizenIdEditText()
            } else {
                setPassportEditText()
            }
        }

        notice_dialog_fragment_btn.setOnClickListener {
            SimplyCardDialogFragment.show(supportFragmentManager)
        }

    }

    override fun onSimplyIdTypeClicked() {
        currentFlow = TYPE_SIMPLY
        register_id_card_group.gone()
        register_contract_number_group.gone()
        register_simply_id_type_group.visible()
        page_title_register_form.text = ContextManager.getInstance().getStringByRes(R.string.registerform_title_header)
    }

    override fun onIdCardTypeClicked() {
        currentFlow = TYPE_ID
        register_simply_id_type_group.gone()
        register_contract_number_group.gone()
        register_id_card_group.visible()
        page_title_register_form.text = ContextManager.getInstance().getStringByRes(R.string.registerform_fill_email_type_title)
    }

    override fun onContractNumberTypeClicked() {
        currentFlow = TYPE_CONTRACT
        register_simply_id_type_group.gone()
        register_id_card_group.gone()
        register_contract_number_group.visible()
        input_register_form_contract.hint = getString(R.string.registerform_contract_hint)
        page_title_register_form.text = ContextManager.getInstance().getStringByRes(R.string.registerform_fill_email_type_title)
    }

    private fun setCitizenIdEditText() {
        currentFlow = TYPE_ID
        input_register_form_id_card.setText("")
        textinput_id_card.hint = getString(R.string.registerform_id_card_hint)
        input_register_form_id_card.inputType = InputType.TYPE_CLASS_NUMBER
//        input_register_form_id_card.setMaskFormat("# - #### - ##### - ## - #")
    }

    private fun setPassportEditText() {
        currentFlow = TYPE_PASSPORT
        input_register_form_id_card.setText("")
        textinput_id_card.hint = getString(R.string.registerform_passport_hint)
        input_register_form_id_card.inputType = InputType.TYPE_CLASS_TEXT
//        input_register_form_id_card.setMaskFormat("####################")
    }


    private fun supportForStaff(it: RegisterFormViewModel.Model) {

    }

    companion object {

        const val TYPE_SIMPLY = "SIMPLY_ID"
        const val TYPE_ID = "ID_CARD"
        const val TYPE_PASSPORT = "PASSPORT"
        const val TYPE_CONTRACT = "CONTRACT_NUMBER"

        fun open(context: Context) {
            val intent = Intent(context, RegisterFormActivity::class.java)
            context.startActivity(intent)
        }
    }
}
