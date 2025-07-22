package tlt.th.co.toyotaleasing.modules.contract.contract

import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.text.method.LinkMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_car_contract.*
import tlt.th.co.toyotaleasing.R
import tlt.th.co.toyotaleasing.analytics.AnalyticsManager
import tlt.th.co.toyotaleasing.analytics.AnalyticsScreenName
import tlt.th.co.toyotaleasing.common.base.BaseFragment
import tlt.th.co.toyotaleasing.common.extension.*
import tlt.th.co.toyotaleasing.modules.contract.specialoffer.SpecialOfferActivity
import tlt.th.co.toyotaleasing.modules.editaddress.installment.EditInstallmentAddressActivity
import tlt.th.co.toyotaleasing.modules.location.main.LocationFragment

class ContractFragment : BaseFragment() {

    private val SCROLL_POSITION_STATE = "scroll_position"

    private val viewModel by lazy {
        ViewModelProviders.of(this).get(ContractViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_car_contract, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViewModel()
        initInstances()

        viewModel.getDataInstallment()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        savedInstanceState?.let {
            nestedscrollview.scrollY = savedInstanceState.getInt(SCROLL_POSITION_STATE, 0)
        }
    }

    private fun initViewModel() {

        viewModel.whenDataLoaded.observe(this, Observer {
            it?.let {
                setupDataIntoViews(it)
                it.isStaffApp.ifTrue { supportForStaff(it) }
            }
        })

        viewModel.whenShowContractStatus.observe(this, Observer {
            when (it) {
                ContractViewModel.Status.NORMAL -> normalContractStatus()
                ContractViewModel.Status.DEBT -> debtContractStatus()
                ContractViewModel.Status.CLOSED -> closedContractStatus()
                ContractViewModel.Status.LEGAL -> legalContractStatus()
                ContractViewModel.Status.OTHER -> otherContractStatus()
            }
        })
    }

    private fun supportForStaff(it: ContractViewModel.Model) {

    }

    private fun initInstances() {

        btn_edit_address.setOnClickListener {
            AnalyticsManager.myContractCustomerAddressClicked()
            EditInstallmentAddressActivity.open(context!!)
        }

        special_offer_linear.setOnClickListener {
            AnalyticsManager.myContractRecommendationClicked()
            SpecialOfferActivity.start(activity)
        }

        title_change_address.movementMethod = LinkMovementMethod.getInstance()

        layout_contract_the_dealer.setOnClickListener {
            AnalyticsManager.myContractDealerContractClicked()
            LocationFragment.startByDeeplink(
                    context = context,
                    fromContractDetail = true
            )
        }

    }

    private fun setupDataIntoViews(data: ContractViewModel.Model) {
        txt_date.text = data.date
        txt_car_license.text = data.carLicense
        txt_fullname.text = data.fullname
        txt_car_no.text = data.carNo
        txt_car_model.text = data.carModel
        txt_contract_date.text = data.contractDate
        txt_due_date.text = data.dueDate //getString(R.string.car_contract_txt_due_date, data.dueDate)
        txt_payment_per_installment.text = getString(R.string.currency, data.paymentPerInstallment)
        txt_installment_period.text = getString(R.string.car_contract_installment, data.installmentPeriod)
        txt_amount_unpaid_installments.text = getString(R.string.car_contract_installment, data.amountUnpaidInstallments)
        txt_unpaid_installments.text = getString(R.string.currency, data.unpaidInstallments)
        txt_total_paid_installment.text = getString(R.string.currency, data.totalPaidInstallment)
        txt_balance.text = getString(R.string.currency, data.balance)
        txt_late_payment_penalty.text = getString(R.string.currency, data.latePaymentPenalty)
        txt_tel.text = getString(R.string.car_contract_phonenumber, data.customerPhoneNumber)
        txt_email.text = getString(R.string.car_contract_email, data.customerEmail)
        txt_address.text = data.customerAddress
        txt_suggesnt_new_car.text = getString(R.string.car_contract_suggest_new_car, data.suggestNewCar)
        txt_toyota_place.text = data.toyotaPlace
        txt_special_offer.text = getString(R.string.car_contract_special_offer_detail)
        if(data.followupfee!="") {
            title_followup_fee.visible()
            txt_followup_fee.text = getString(R.string.currency, data.followupfee)
        }else{
            title_followup_fee.gone()
            txt_followup_fee.gone()
        }

        /**
         * Set image from URL
         */
        bg_toyota_place.loadImageByUrl(data.imageToyotaPlaceUrl,
                R.drawable.bg_car_contract_dealer,
                R.drawable.bg_car_contract_dealer)
    }

    private fun normalContractStatus() {

    }

    private fun debtContractStatus() {

    }

    private fun closedContractStatus() {

    }

    private fun legalContractStatus() {
        layout_special_offer.gone()
        btn_edit_address.invisible()
        title_change_address.visible()
        layout_car_contract_detail_section_2.gone()
        layout_car_contract_detail_section_3.gone()
        layout_car_contract_detail_section_4.gone()
        layout_car_contract_detail_section_4.gone()
        layout_offer_to_get_new_car.gone()
    }

    private fun otherContractStatus() {
        layout_special_offer.gone()
        layout_car_contract_detail_section_2.gone()
        layout_car_contract_detail_section_3.gone()
        layout_car_contract_detail_section_4.gone()
        title_change_address.gone()
        layout_where_to_send_document.gone()
        layout_offer_to_get_new_car.gone()
        layout_contract_the_dealer.gone()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(SCROLL_POSITION_STATE, nestedscrollview.scrollY)
    }

    companion object {
        fun newInstance() = ContractFragment()
    }
}
