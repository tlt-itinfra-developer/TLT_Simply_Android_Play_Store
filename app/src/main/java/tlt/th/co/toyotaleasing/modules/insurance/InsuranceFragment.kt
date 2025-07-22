package tlt.th.co.toyotaleasing.modules.insurance

import android.app.Activity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.core.content.ContextCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_insurance.*
import kotlinx.android.synthetic.main.layout_insurance_customer_nodata.*
import kotlinx.android.synthetic.main.layout_insurance_customer_normal.*
import kotlinx.android.synthetic.main.layout_insurance_customer_normal.view.*
import kotlinx.android.synthetic.main.layout_insurance_customer_prohibit.*
import kotlinx.android.synthetic.main.layout_insurance_main_profile.view.*
import kotlinx.android.synthetic.main.layout_insurance_menu_data_status_quotation.view.*
import kotlinx.android.synthetic.main.layout_insurance_noncustomer.*
import tlt.th.co.toyotaleasing.R
import tlt.th.co.toyotaleasing.analytics.AnalyticsManager
import tlt.th.co.toyotaleasing.analytics.AnalyticsScreenName
import tlt.th.co.toyotaleasing.common.base.BaseFragment
import tlt.th.co.toyotaleasing.common.extension.*
import tlt.th.co.toyotaleasing.common.listener.OnHambergerClickListener

import tlt.th.co.toyotaleasing.modules.filtercar.FilterCarActivity
import tlt.th.co.toyotaleasing.modules.home.customer.MainCustomerActivity
import tlt.th.co.toyotaleasing.modules.insurance.hotline.HotLineActivity
import tlt.th.co.toyotaleasing.modules.insurance.information.InsuranceInformationActivity
import tlt.th.co.toyotaleasing.modules.insurance.paymentdetail.PaymentDetailActivity
import tlt.th.co.toyotaleasing.modules.insurance.quotation.QuotationActivity
import tlt.th.co.toyotaleasing.modules.insurance.status.InsuranceStatusActivity
import tlt.th.co.toyotaleasing.modules.insurance.tibclub.TIBClubActivity
import tlt.th.co.toyotaleasing.util.ExternalAppUtils

class InsuranceFragment : BaseFragment() {

    private lateinit var contractNoExtra: String

    private val viewModel by lazy {
        ViewModelProviders.of(this).get(InsuranceViewModel::class.java)
    }

    private val onHambergerClickListener by lazy {
        context as OnHambergerClickListener
    }

    private val tibCode by lazy {
        activity?.intent
                ?.getBundleExtra(MainCustomerActivity.DATA_POSITION_EXTRA)
                ?.getString(TIBClubActivity.TIB_CODE_EXTRA) ?: ""
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_insurance, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViewModel()
        initInstances()

        tibCode.isNotEmpty().ifTrue {
            TIBClubActivity.startByDeeplink(
                    context = context,
                    tibCode = tibCode
            )
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == QuotationActivity.REQUEST_CODE
                && resultCode == Activity.RESULT_OK) {
            // Do Something
        }
    }

    private fun initViewModel() {
        viewModel.whenLoading.observe(this, Observer {
            toggleLoadingScreenDialog(it!!, this)
        })

        viewModel.whenBannerLoaded.observe(this, Observer {
            widget_banner.setBanners(it ?: listOf())
        })

        viewModel.whenDataLoaded.observe(this, Observer {
            it?.let {
                initDataIntoViews(it)
                it.isStaffApp.ifTrue { supportForStaff(it) }
            }
        })
    }

    private fun supportForStaff(it: InsuranceViewModel.Model) {

    }

    private fun initInstances() {
        contractNoExtra = activity?.intent
                ?.getBundleExtra(MainCustomerActivity.DATA_POSITION_EXTRA)
                ?.getString(CONTRACT_NO_EXTRA) ?: ""

        toolbar.setOnHambergerMenuClickListener {
            AnalyticsManager.insuranceMainMenuHome()
            onHambergerClickListener.onHambergerClick()
        }

        toolbar.setOnRightMenuTitleClickListener {
            AnalyticsManager.insuranceMainSelector()
            FilterCarActivity.open(context!!)
        }


        txt_insurance_accident.setOnClickListener {
            AnalyticsManager.tibClaimCenter()
            HotLineActivity.start(context, HotLineActivity.ACCIDENT)
        }

        btn_insurance_emergency.setOnClickListener {
            AnalyticsManager.insuranceMainEmerCall()
            HotLineActivity.start(context, HotLineActivity.EMERGENCY)
        }


//        btn_insurance_dealer.setOnClickListener {
//            AnalyticsManager.insuranceMainDealerLocation()
//            HotLineActivity.start(context, HotLineActivity.DEALER)
//        }
//        txt_insurance_dealer.setOnClickListener {
//            AnalyticsManager.insuranceMainDealerLocation()
//            HotLineActivity.start(context, HotLineActivity.DEALER)
//        }

        btn_insurance_contact.setOnClickListener {
            AnalyticsManager.tibContactUs()
            HotLineActivity.start(context, HotLineActivity.CONTACT)
        }

        //set listener for normal case
        layout_insurance_customer_normal.btn_insurance_data.setOnClickListener {
            AnalyticsManager.insuranceMainInfor()
            InsuranceInformationActivity.start(context)
        }
        layout_insurance_customer_normal.btn_insurance_status.setOnClickListener {
            AnalyticsManager.insuranceMainPolicyStatus()
            InsuranceStatusActivity.start(context)
        }
        layout_insurance_customer_normal.layout_insurance_menu_data_status_quotation.btn_insurance_quotation.setOnClickListener {
//            AnalyticsManager.insuranceMainQuotationForm()
//            QuotationActivity.startWithResult(this)
            AnalyticsManager.tibContactUs()
            HotLineActivity.start(context, HotLineActivity.CONTACT)
        }

        layout_insurance_customer_normal.txt_insurance_data.setOnClickListener {
            AnalyticsManager.tibInsuranceInfor()
            InsuranceInformationActivity.start(context)
        }
        layout_insurance_customer_normal.txt_insurance_status.setOnClickListener {
            AnalyticsManager.tibInsuracePolicy()
            InsuranceStatusActivity.start(context)
        }
        layout_insurance_customer_normal.layout_insurance_menu_data_status_quotation.txt_insurance_quotation.setOnClickListener {
//            AnalyticsManager.insuranceMainQuotationForm()
//            QuotationActivity.startWithResult(this)
            AnalyticsManager.tibContactUs()
            HotLineActivity.start(context, HotLineActivity.CONTACT)
        }

        //set listener for no data case
        layout_insurance_customer_nodata.btn_insurance_data.setOnClickListener {
            AnalyticsManager.tibInsuranceInfor()
            InsuranceInformationActivity.start(context)
        }
        layout_insurance_customer_nodata.btn_insurance_status.setOnClickListener {
            AnalyticsManager.insuranceMainPolicyStatus()
            InsuranceStatusActivity.start(context)
        }
        layout_insurance_customer_nodata.layout_insurance_menu_data_status_quotation.btn_insurance_quotation.setOnClickListener {
//            AnalyticsManager.insuranceMainQuotationForm()
//            QuotationActivity.startWithResult(this)
            AnalyticsManager.tibContactUs()
            HotLineActivity.start(context, HotLineActivity.CONTACT)
        }
        layout_insurance_customer_nodata.txt_insurance_data.setOnClickListener {
            AnalyticsManager.tibInsuranceMain()
            InsuranceInformationActivity.start(context)
        }
        layout_insurance_customer_nodata.txt_insurance_status.setOnClickListener {
            AnalyticsManager.tibInsuracePolicy()
            InsuranceStatusActivity.start(context)
        }
        layout_insurance_customer_nodata.layout_insurance_menu_data_status_quotation.txt_insurance_quotation.setOnClickListener {
//            AnalyticsManager.insuranceMainQuotationForm()
//            QuotationActivity.startWithResult(this)
            AnalyticsManager.tibContactUs()
            HotLineActivity.start(context, HotLineActivity.CONTACT)
        }
        title_insurance_nodata.setOnClickListener {
            AnalyticsManager.tibCall()
            ExternalAppUtils.openDialApp(context, "026605999")
        }

        //set listener for after pay case
        layout_insurance_customer_after_pay.btn_insurance_data.setOnClickListener {
            AnalyticsManager.tibInsuranceMain()
            InsuranceInformationActivity.start(context)
        }
        layout_insurance_customer_after_pay.btn_insurance_status.setOnClickListener {
            AnalyticsManager.tibInsuracePolicy()
            InsuranceStatusActivity.start(context)
        }
        layout_insurance_customer_after_pay.layout_insurance_menu_data_status_quotation.btn_insurance_quotation.setOnClickListener {
//            AnalyticsManager.insuranceMainQuotationForm()
//            QuotationActivity.startWithResult(this)
            AnalyticsManager.tibContactUs()
            HotLineActivity.start(context, HotLineActivity.CONTACT)
        }
        layout_insurance_customer_after_pay.txt_insurance_data.setOnClickListener {
            AnalyticsManager.tibInsuranceInfor()
            InsuranceInformationActivity.start(context)
        }
        layout_insurance_customer_after_pay.txt_insurance_status.setOnClickListener {
            AnalyticsManager.tibInsuracePolicy()
            InsuranceStatusActivity.start(context)
        }
        layout_insurance_customer_after_pay.layout_insurance_menu_data_status_quotation.txt_insurance_quotation.setOnClickListener {
//            AnalyticsManager.insuranceMainQuotationForm()
//            QuotationActivity.startWithResult(this)
            AnalyticsManager.tibContactUs()
            HotLineActivity.start(context, HotLineActivity.CONTACT)
        }

        //set listener for checking case
        layout_insurance_customer_checking.btn_insurance_data.setOnClickListener {
            AnalyticsManager.tibInsuranceInfor()
            InsuranceInformationActivity.start(context)
        }
        layout_insurance_customer_checking.btn_insurance_status.setOnClickListener {
            AnalyticsManager.tibInsuracePolicy()
            InsuranceStatusActivity.start(context)
        }
        layout_insurance_customer_checking.layout_insurance_menu_data_status_quotation.btn_insurance_quotation.setOnClickListener {
//            AnalyticsManager.insuranceMainQuotationForm()
//            QuotationActivity.startWithResult(this)
            AnalyticsManager.tibContactUs()
            HotLineActivity.start(context, HotLineActivity.CONTACT)
        }
        layout_insurance_customer_checking.txt_insurance_data.setOnClickListener {
            AnalyticsManager.tibInsuranceInfor()
            InsuranceInformationActivity.start(context)
        }
        layout_insurance_customer_checking.txt_insurance_status.setOnClickListener {
            AnalyticsManager.tibInsuracePolicy()
            InsuranceStatusActivity.start(context)
        }
        layout_insurance_customer_checking.layout_insurance_menu_data_status_quotation.txt_insurance_quotation.setOnClickListener {
//            AnalyticsManager.insuranceMainQuotationForm()
//            QuotationActivity.startWithResult(this)
            AnalyticsManager.tibContactUs()
            HotLineActivity.start(context, HotLineActivity.CONTACT)
        }

        //set listener for non-customer case
        layout_insurance_noncustomer.btn_insurance_quotation.setOnClickListener {
//            AnalyticsManager.insuranceMainQuotationForm()
//            QuotationActivity.startWithResult(this)
            AnalyticsManager.tibContactUs()
            HotLineActivity.start(context, HotLineActivity.CONTACT)
        }
        layout_insurance_noncustomer.txt_insurance_quotation.setOnClickListener {
//            AnalyticsManager.insuranceMainQuotationForm()
//            QuotationActivity.startWithResult(this)
            AnalyticsManager.tibContactUs()
            HotLineActivity.start(context, HotLineActivity.CONTACT)
        }
    }

    private fun initDataIntoViews(model: InsuranceViewModel.Model) {
        layout_insurance_customer_normal.txt_insurance_license.text = model.license
        layout_insurance_customer_normal.txt_insurance_fullname.text = model.fullname
        layout_insurance_customer_normal.txt_insurance_contract_number.text = getString(R.string.insurance_contract_number, model.contractNumber)
        layout_insurance_customer_normal.txt_insurance_customer_date.text = model.currentDate
        txt_insurance_pay.setTextWithHtml(getString(R.string.insurance_total_pay,
                model.totalPay,
                ContextCompat.getColor(context!!, R.color.terracotta_200).toString()
        ))

        layout_insurance_customer_nodata.txt_insurance_license.text = model.license
        layout_insurance_customer_nodata.txt_insurance_fullname.text = model.fullname
        layout_insurance_customer_nodata.txt_insurance_contract_number.text = getString(R.string.insurance_contract_number, model.contractNumber)
        layout_insurance_customer_nodata.txt_insurance_customer_date.text = model.currentDate

        layout_insurance_customer_after_pay.txt_insurance_license.text = model.license
        layout_insurance_customer_after_pay.txt_insurance_fullname.text = model.fullname
        layout_insurance_customer_after_pay.txt_insurance_contract_number.text = getString(R.string.insurance_contract_number, model.contractNumber)
        layout_insurance_customer_after_pay.txt_insurance_customer_date.text = model.currentDate

        layout_insurance_customer_checking.txt_insurance_license.text = model.license
        layout_insurance_customer_checking.txt_insurance_fullname.text = model.fullname
        layout_insurance_customer_checking.txt_insurance_contract_number.text = getString(R.string.insurance_contract_number, model.contractNumber)
        layout_insurance_customer_checking.txt_insurance_customer_date.text = model.currentDate

        txt_insurance_noncustomer_date.text = model.currentDate

        txt_insurance_prohibit_date.text = model.currentDate

        img_club_tib.loadImageByUrl(model.tibClubImageUrl)

        btn_club_detail.setOnClickListener {
            AnalyticsManager.insuranceMainTibClub()
            TIBClubActivity.start(context)
        }

        btn_insurance_pay.isEnabled = model.totalPay != "0.00"

        when (model.status) {
            InsuranceViewModel.Status.NORMAL -> normalStatus()
            InsuranceViewModel.Status.CHECKING -> checkingStatus()
            InsuranceViewModel.Status.AFTER_PAY -> afterPayStatus()
            InsuranceViewModel.Status.PROHIBIT -> prohibitStatus()
            InsuranceViewModel.Status.NON_CUSTOMER -> nonCustomerStatus()
            else -> noDataStatus()
        }

        layout_insurance_customer_nodata.btn_contact_via.setOnClickListener {
            ExternalAppUtils.openDialApp(context, "026605999")
        }

    }

    private fun normalStatus() {
        layout_insurance_customer_normal.visible()
        layout_insurance_noncustomer.gone()
        layout_insurance_customer_after_pay.gone()
        layout_insurance_customer_checking.gone()
        layout_insurance_customer_prohibit.gone()
        layout_insurance_customer_nodata.gone()

        btn_insurance_pay.setOnClickListener {
            AnalyticsManager.insuranceMainCheckingPayment()
            PaymentDetailActivity.start(context)
        }

        layout_insurance_customer_normal.btn_contact_via.setOnClickListener {
            AnalyticsManager.tibCall()
            ExternalAppUtils.openDialApp(context, "026605999")
        }

    }

    private fun checkingStatus() {
        layout_insurance_customer_checking.visible()

        layout_insurance_noncustomer.gone()
        layout_insurance_customer_after_pay.gone()
        layout_insurance_customer_normal.gone()
        layout_insurance_customer_prohibit.gone()
        layout_insurance_customer_nodata.gone()
    }

    private fun afterPayStatus() {
        layout_insurance_customer_after_pay.visible()

        layout_insurance_noncustomer.gone()
        layout_insurance_customer_normal.gone()
        layout_insurance_customer_checking.gone()
        layout_insurance_customer_prohibit.gone()
        layout_insurance_customer_nodata.gone()
    }

    private fun prohibitStatus() {
        layout_insurance_customer_prohibit.visible()

        layout_insurance_noncustomer.gone()
        layout_insurance_customer_normal.gone()
        layout_insurance_customer_checking.gone()
        layout_insurance_customer_after_pay.gone()
        layout_insurance_customer_nodata.gone()
    }

    private fun nonCustomerStatus() {
        layout_insurance_noncustomer.visible()
        toolbar.hideTextRightMenu()
        layout_insurance_customer_normal.gone()
        layout_insurance_customer_prohibit.gone()
        layout_insurance_customer_checking.gone()
        layout_insurance_customer_after_pay.gone()
        layout_insurance_customer_nodata.gone()

        layout_insurance_noncustomer.btn_contact_via.setOnClickListener {
            AnalyticsManager.tibCall()
            ExternalAppUtils.openDialApp(context, "026605999")
        }
    }

    private fun noDataStatus() {
        layout_insurance_customer_nodata.visible()

        layout_insurance_noncustomer.gone()
        layout_insurance_customer_normal.gone()
        layout_insurance_customer_prohibit.gone()
        layout_insurance_customer_checking.gone()
        layout_insurance_customer_after_pay.gone()
    }

    companion object {
        const val CONTRACT_NO_EXTRA = "CONTRACT_NO_EXTRA"

        fun newInstance() = InsuranceFragment()

        fun startByDeeplink(context: Context?,
                            contractNo: String) {
            MainCustomerActivity.startWithClearStack(
                    context = context,
                    position = MainCustomerActivity.INSURANCE_MENU_POSITION,
                    data = Bundle().apply {
                        putString(CONTRACT_NO_EXTRA, contractNo)
                    }
            )
        }
    }
}
