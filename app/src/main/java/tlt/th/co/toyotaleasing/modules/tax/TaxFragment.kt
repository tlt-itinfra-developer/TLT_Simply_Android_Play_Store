package tlt.th.co.toyotaleasing.modules.tax

import android.annotation.SuppressLint
import android.app.Activity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.core.content.ContextCompat
import android.text.method.LinkMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_tax.*
import kotlinx.android.synthetic.main.layout_tax_after_payment.*
import kotlinx.android.synthetic.main.layout_tax_duedate_payment.*
import kotlinx.android.synthetic.main.layout_tax_duedate_payment.view.*
import kotlinx.android.synthetic.main.layout_tax_updating.*
import tlt.th.co.toyotaleasing.R
import tlt.th.co.toyotaleasing.analytics.AnalyticsManager
import tlt.th.co.toyotaleasing.analytics.AnalyticsScreenName
import tlt.th.co.toyotaleasing.common.base.BaseFragment
import tlt.th.co.toyotaleasing.common.dialog.NormalDialogFragment
import tlt.th.co.toyotaleasing.common.extension.gone
import tlt.th.co.toyotaleasing.common.extension.ifTrue
import tlt.th.co.toyotaleasing.common.extension.setTextWithHtml
import tlt.th.co.toyotaleasing.common.extension.visible
import tlt.th.co.toyotaleasing.common.listener.OnHambergerClickListener

import tlt.th.co.toyotaleasing.manager.db.PaymentManager
import tlt.th.co.toyotaleasing.model.response.GetDataTaxResponse
import tlt.th.co.toyotaleasing.modules.editaddress.taxdelivery.EditTaxDeliveryAddressActivity
import tlt.th.co.toyotaleasing.modules.filtercar.FilterCarActivity
import tlt.th.co.toyotaleasing.modules.home.customer.MainCustomerActivity
import tlt.th.co.toyotaleasing.modules.payment.cart.tax.CartTaxActivity
import tlt.th.co.toyotaleasing.modules.tax.gas.GasTaxActivity
import tlt.th.co.toyotaleasing.modules.tax.porlorbor.PorlorborTaxActivity
import tlt.th.co.toyotaleasing.modules.tax.porlorbor.PorlorborTaxDialogFragment
import tlt.th.co.toyotaleasing.modules.tax.torloraor.TorloraorTaxActivity
import tlt.th.co.toyotaleasing.util.ExternalAppUtils
import java.net.URLEncoder

class TaxFragment : BaseFragment(),
        TaxRenewDialogFragment.Listener,
        PorlorborTaxDialogFragment.Listener {

    private lateinit var contractNoExtra: String

    private val imageUploadViewModel by lazy {
        ViewModelProviders.of(this).get(ImageUploadViewModel::class.java)
    }

    private val viewModel by lazy {
        ViewModelProviders.of(this).get(TaxViewModel::class.java)
    }

    private val onHambergerClickListener by lazy {
        context as OnHambergerClickListener
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_tax, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViewModel()
        initInstances()
    }

//    override fun onSupportVisible() {
//        super.onSupportVisible()
//        //DatabaseManager.getInstance().updateNotifyDeeplinkState(isNotifyDeeplinkState = false)
//        viewModel.getTaxData(contractNoExtra)
//        contractNoExtra = ""
//    }
//
//    override fun onSupportInvisible() {
//        super.onSupportInvisible()
//        AnalyticsManager.trackScreen(AnalyticsScreenName.TAX_MAIN)
//    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PorlorborTaxActivity.REQUEST_CODE
                && resultCode == Activity.RESULT_OK) {
            imageUploadViewModel.uploadByPorlorbor()
            PaymentManager.deletePorlorborDocsState()
            viewModel.getTaxData()
            return
        }

        if (requestCode == TorloraorTaxActivity.REQUEST_CODE
                && resultCode == Activity.RESULT_OK) {
            imageUploadViewModel.uploadByTorloraor()
            PaymentManager.deleteTorloraorDocsState()
            viewModel.getTaxData()
            return
        }

        if (requestCode == GasTaxActivity.REQUEST_CODE
                && resultCode == Activity.RESULT_OK) {
            viewModel.getTaxData()
            return
        }
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

        viewModel.whenUpdateTaxSuccess.observe(this, Observer {
            if (it!!) {
                NormalDialogFragment.show(
                        fragmentManager = fragmentManager!!,
                        description = activity!!.getString(R.string.tax_slide_update_tax_success),
                        confirmButtonMessage = activity!!.getString(R.string.dialog_button_ok)
                )
            }
        })
    }

    private fun initInstances() {

        btn_edit_address.gone()

        contractNoExtra = activity?.intent
                ?.getBundleExtra(MainCustomerActivity.DATA_POSITION_EXTRA)
                ?.getString(CONTRACT_NO_EXTRA, "") ?: ""

        link_tlt_lineid.movementMethod = LinkMovementMethod.getInstance()
        title_note.movementMethod = LinkMovementMethod.getInstance()

        link_tlt_lineid.setOnClickListener { AnalyticsManager.taxMainCall() }
        title_note.setOnClickListener { AnalyticsManager.taxMainCall() }

        toolbar.setOnHambergerMenuClickListener {
            AnalyticsManager.taxMainMainMenu()
            onHambergerClickListener.onHambergerClick()
        }

        toolbar.setOnRightMenuTitleClickListener {
            FilterCarActivity.open(context!!)
        }

        slider_update_tax.onCompleteListener {
            AnalyticsManager.taxMainUpdateYear()
            TaxRenewDialogFragment.show(fragmentManager!!, this)
        }

        ic_sending.setOnClickListener {
            AnalyticsManager.taxMainTaxLabelStatus()
            TaxDocumentDeliveryActivity.open(context!!, viewModel.isShowLockTaxDocument)
        }

        title_check_transport.setOnClickListener {
            AnalyticsManager.taxMainTaxLabelStatus()
            TaxDocumentDeliveryActivity.open(context!!, viewModel.isShowLockTaxDocument)
        }

        btn_tax_document_delivery.setOnClickListener {
            AnalyticsManager.taxMainTaxLabelStatus()
            TaxDocumentDeliveryActivity.open(context!!, viewModel.isShowLockTaxDocument)
        }

        btn_edit_address.setOnClickListener {
            AnalyticsManager.taxMainTaxLabelReceiveAddress()
            EditTaxDeliveryAddressActivity.open(context!!)
        }

        tax_duedate_btn_against_the_tax.setOnClickListener {
            AnalyticsManager.taxMainPayment()
            CartTaxActivity.start(context)
        }

        layout_tax_process.onPorlorborClickListener {
            AnalyticsManager.taxMainInsuranceChecking()
            if (viewModel.isPorlorborRequesting()) {
                PorlorborTaxDialogFragment.show(fragmentManager!!, this)
                return@onPorlorborClickListener
            }

            PorlorborTaxActivity.startWithResult(this)
        }

        layout_tax_process.onTorloraorClickListener {
            AnalyticsManager.taxMainInspectCerChecking()
            TorloraorTaxActivity.startWithResult(this)
        }

        layout_tax_process.onGasClickListener {
            AnalyticsManager.taxMainNgvCerChecking()
            GasTaxActivity.startWithResult(this)
        }

        link_tlt_lineid.setOnClickListener{
            try {
                val textLine = URLEncoder.encode("งานทะเบียน", "UTF-8");
                ExternalAppUtils.openByLink(activity, ""+textLine)
//                ExternalAppUtils.openByLink(activity!!, "http://nav.cx/oYc9GcR")
            } catch (e: Exception) {
                val textLine = URLEncoder.encode("งานทะเบียน", "UTF-8");
                ExternalAppUtils.openByLink(activity, ""+textLine)
                e.printStackTrace()
            }
        }

    }

    @SuppressLint("StringFormatMatches")
    private fun setupDataIntoViews(it: TaxViewModel.Model) {
        txt_car_license.text = it.carLicense
        txt_fullname.text = it.fullname
        txt_contract_number.text = getString(R.string.tax_contract_number, it.contractNumber)
        txt_tax_expire.text = it.taxExpire
        tax_duedate_txt_payment_date.text = it.paymentDate
        tax_update_txt_payment_date.text = it.paymentDate
        tax_duedate_txt_amount_tax.text = getString(R.string.tax_currency, it.amountTax)
        tax_duedate_txt_service_charge.text = getString(R.string.tax_currency, it.serviceCharge)
        tax_duedate_txt_aol.text = getString(R.string.tax_currency, it.aol)
        tax_duedate_txt_penalty.text = getString(R.string.tax_currency, it.penalty)
        tax_duedate_txt_total_payment.setTextWithHtml(getString(R.string.tax_total_payment, it.totalPayment, ContextCompat.getColor(context!!, R.color.terracotta_200)))

        tax_after_paid_txt_payment_date.text = it.paymentDate
        txt_address_to_transport.text = it.addressToTransport

        layout_tax_process.setData(it.statusDocument)
        title_staff_contact.text = it.taxDes

        if(it.flagTracking == "Y"){
            card_check_transport.visible()
        }else{
            card_check_transport.gone()
        }

        when (it.status) {
            TaxViewModel.Status.DUE_DATE -> duedatePayment(it.dataTax)
            TaxViewModel.Status.CLOSE -> cannotPayment()
            TaxViewModel.Status.LEGAL -> cannotPayment()
            TaxViewModel.Status.OTHER -> cannotPayment()
            TaxViewModel.Status.AFTER_PAYMENT -> afterPayment()
            TaxViewModel.Status.EXPIRE -> taxOver3Years()
            TaxViewModel.Status.UPDATE -> updatingPayment(it.dataTax)
        }

        if (it.isShowHeaderWarning) {
            layout_tax_duedate_payment.header_layout.visible()
            layout_tax_process.setVisibilityHeaderLayout(View.VISIBLE)
        } else {
            layout_tax_duedate_payment.header_layout.gone()
            layout_tax_process.setVisibilityHeaderLayout(View.GONE)
        }
    }

    private fun supportForStaff(it: TaxViewModel.Model) {

    }

    private fun updatingPayment(dataTax: GetDataTaxResponse) {
        slider_update_tax.visible()
        title_slider_update_tax.visible()
        title_cannot_pay.gone()
        layout_tax_after_payment.gone()
        layout_tax_duedate_payment.gone()
//        card_check_transport.visible()
        card_staff_contact.gone()
        layout_update_payment.visible()

        if (dataTax.flagPORLORBOR!!.toLowerCase() == "n" &&
                dataTax.flagTORLORAOR!!.toLowerCase() == "n" &&
                dataTax.flagGAS!!.toLowerCase() == "n") {
            layout_tax_process.gone()
        } else {
            layout_tax_process.visible()
        }
    }

    private fun duedatePayment(dataTax: GetDataTaxResponse) {
        slider_update_tax.visible()
        title_slider_update_tax.visible()
        title_cannot_pay.gone()
        layout_tax_after_payment.gone()
        layout_tax_duedate_payment.visible()
//        card_check_transport.visible()
        card_staff_contact.gone()
        layout_tax_process.gone()
        layout_update_payment.gone()

        if (dataTax.cVEHICLEAGE!!.toFloat().toInt() < 8 &&
                dataTax.cVEHICLEGAS!!.toLowerCase() == "normal") {
            header_layout.gone()
        } else {
            header_layout.visible()
        }
    }

    private fun cannotPayment() {
        slider_update_tax.gone()
        title_slider_update_tax.gone()
        title_cannot_pay.visible()
        layout_tax_after_payment.gone()
        layout_tax_duedate_payment.gone()
//        card_check_transport.gone()
        card_staff_contact.visible()
        layout_tax_process.gone()
        layout_update_payment.gone()
    }

    private fun taxOver3Years() {
        slider_update_tax.visible()
        title_slider_update_tax.visible()
        title_cannot_pay.gone()
        layout_tax_after_payment.gone()
        layout_tax_duedate_payment.gone()
//        card_check_transport.gone()
        card_staff_contact.visible()
        layout_tax_process.gone()
        layout_update_payment.gone()
    }

    private fun afterPayment() {
        slider_update_tax.visible()
        title_slider_update_tax.visible()
        title_cannot_pay.gone()
        layout_tax_after_payment.gone()
        layout_tax_duedate_payment.gone()
//        card_check_transport.visible()
        card_staff_contact.gone()
        layout_tax_process.visible()
        layout_update_payment.gone()
    }

    override fun onTaxRenewDialogResult(year: String) {
        AnalyticsManager.updateTaxYearEnter()
        viewModel.updateTaxYear(year)
    }

    override fun onBuyPorlorborCicked() {
        CartTaxActivity.start(context = context, isPorlorborBuyNow = true)
    }

    override fun onAttachPorlorborClicked() {
        PorlorborTaxActivity.startWithResult(this)
    }

    companion object {
        private const val CONTRACT_NO_EXTRA = "CONTRACT_NO_EXTRA"

        fun newInstance() = TaxFragment()

        fun startByDeeplink(context: Context?,
                            contractNo: String) {
            MainCustomerActivity.startWithClearStack(
                    context = context,
                    position = MainCustomerActivity.TAX_MENU_POSITION,
                    data = Bundle().apply {
                        putString(CONTRACT_NO_EXTRA, contractNo)
                    }
            )
        }
    }
}
