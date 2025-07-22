package tlt.th.co.toyotaleasing.modules.installmentdetail

import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import android.text.method.LinkMovementMethod
import kotlinx.android.synthetic.main.activity_installment_detail.*
import tlt.th.co.toyotaleasing.R
import tlt.th.co.toyotaleasing.analytics.AnalyticsManager
import tlt.th.co.toyotaleasing.analytics.AnalyticsScreenName
import tlt.th.co.toyotaleasing.common.base.BaseActivity
import tlt.th.co.toyotaleasing.common.extension.gone
import tlt.th.co.toyotaleasing.common.extension.setTextWithHtml
import tlt.th.co.toyotaleasing.common.extension.visible
import tlt.th.co.toyotaleasing.modules.payment.cart.installment.CartInstallmentActivity

class InstallmentDetailActivity : BaseActivity() {

    private val viewModel by lazy {
        ViewModelProviders.of(this).get(InstallmentDetailViewModel::class.java)
    }

    private val flagPayProcess by lazy {
        intent.getStringExtra(FLAG_PAY_PROCESS)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_installment_detail)

        initViewModel()
        initInstances()

        viewModel.getInstallmentDetail()
    }

    private fun initViewModel() {
        viewModel.whenLoading.observe(this, Observer {
            toggleLoadingScreenDialog(it!!)
        })

        viewModel.whenDataLoaded.observe(this, Observer {
            setupDataIntoViews(it!!)
        })

        viewModel.whenDisplayUIByStatus.observe(this, Observer {

        })
    }

    private fun initInstances() {
        title_note.movementMethod = LinkMovementMethod.getInstance()
        recycler_view.layoutManager = LinearLayoutManager(this@InstallmentDetailActivity)
        recycler_view.adapter = DebtAdapter(ArrayList())

        title_note.setOnClickListener {
            AnalyticsManager.installmentDetailCall()
        }

        btn_payment_now.setOnClickListener {
            AnalyticsManager.installmentDetailCheckingPayment()
            CartInstallmentActivity.start(this)
        }

        btn_payment_now.isEnabled = false
    }

    private fun setupDataIntoViews(it: InstallmentDetailViewModel.Model) {
        txt_date.text = it.date
        txt_car_license.text = it.carLicense
        txt_fullname.text = it.fullname
        txt_car_no.text = it.carNo
        txt_payment.text = getString(R.string.installment_detail_currency, it.payment)
        txt_payment_method.text = it.paymentMethod
        txt_next_payment.text = getString(R.string.installment_detail_next_due_date, it.nextPayment)
        txt_total_payment.setTextWithHtml(getString(R.string.installment_detail_total_payment,
                it.totalPayment,
                "#f26c4f"))

        if (it.totalPayment == "0.00") {
            btn_payment_now.isEnabled = false
        }

//        if(it.followupfee!="") {
//            title_followup_fee.visible()
//            txt_followup_fee.text = getString(R.string.currency, it.followupfee)
//        }else{
//            title_followup_fee.gone()
//            txt_followup_fee.gone()
//        }

        when (it.status) {
            InstallmentDetailViewModel.Status.NORMAL -> normalContractStatus(isShow = true, it = it)
            InstallmentDetailViewModel.Status.DIRECT_DEBIT -> directDebitContractStatus(isShow = false, it = it)
            InstallmentDetailViewModel.Status.CLOSED -> closedContractStatus()
            InstallmentDetailViewModel.Status.LEGAL -> legalAndOtherContractStatus()
            InstallmentDetailViewModel.Status.OTHER -> legalAndOtherContractStatus()
        }

        if (it.debtList.isNotEmpty()) {
            recycler_view.visible()
            divier2.visible()

            val adapter = recycler_view.adapter as DebtAdapter
            adapter.addItems(it.debtList)
        }
    }

    private fun normalContractStatus(isShow: Boolean, it: InstallmentDetailViewModel.Model) {
        title_note.text = getString(R.string.installment_detail_contact_staff_normal)
        if (isShow) {
            card_section_3.visible()
            btn_payment_now.visible()
        } else {
            card_section_3.gone()
            btn_payment_now.gone()
        }
        card_section_4.gone()
        card_section_5.gone()
        txt_direct_debit_description.gone()
    }

    private fun directDebitContractStatus(isShow: Boolean, it: InstallmentDetailViewModel.Model) {
        title_note.text = getString(R.string.installment_detail_contact_staff_normal)
        if (isShow) {
            card_section_3.visible()
            btn_payment_now.visible()
        } else {
            card_section_3.gone()
            btn_payment_now.gone()
        }
        card_section_4.gone()
        card_section_5.visible()

        txt_account_bank.text = it.accountBank.trim()
        txt_account_number.text = it.accountNumber.trim()

        title_account_bank.visible()
        title_account_number.visible()
        txt_account_bank.visible()
        txt_account_number.visible()

        txt_direct_debit_description.visible()
    }

    private fun closedContractStatus() {
        title_note.text = getString(R.string.installment_detail_contact_staff)
        card_section_4.gone()
    }

    private fun legalAndOtherContractStatus() {
        title_note.text = getString(R.string.installment_detail_contact_staff)
        card_section_2.gone()
        card_section_3.gone()
        card_section_4.visible()
        btn_payment_now.gone()
    }

    override fun onPause() {
        super.onPause()
        AnalyticsManager.trackScreen(AnalyticsScreenName.INSTALLMENT_DETAIL)
    }

    companion object {
        const val FLAG_PAY_PROCESS = "flagPayProcess"

        fun open(context: Context, flagPayProcess: String) {
            val intent = Intent(context, InstallmentDetailActivity::class.java)
            intent.putExtra(FLAG_PAY_PROCESS, flagPayProcess)
            context.startActivity(intent)
        }
    }
}
