package tlt.th.co.toyotaleasing.modules.custloan.loanmodule.custinfo

import android.app.Activity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import tlt.th.co.toyotaleasing.view.DividerItemDecoration
import android.view.View
import kotlinx.android.synthetic.main.fragment_geo_streetview.*
import kotlinx.android.synthetic.main.fragment_loan_info_credit_result.*
import kotlinx.android.synthetic.main.fragment_loan_info_credit_result.btn_next_confirm
import kotlinx.android.synthetic.main.fragment_loan_info_credit_result.map_online_require_upper_sheet
import kotlinx.android.synthetic.main.loan_cust_info_upper_sheet.view.*
import kotlinx.android.synthetic.main.status_car_loan_consideration_add_doc.view.*
import kotlinx.android.synthetic.main.status_car_loan_consideration_approve.view.*
import kotlinx.android.synthetic.main.status_car_loan_consideration_best_offer.view.*
import kotlinx.android.synthetic.main.status_car_loan_consideration_reject.view.*
import tlt.th.co.toyotaleasing.common.base.BaseActivity
import tlt.th.co.toyotaleasing.modules.custloan.loanmodule.center.CarLoanFragment
import tlt.th.co.toyotaleasing.modules.custloan.loanmodule.custinfo.common.BestOfferSwipeAdapter
import tlt.th.co.toyotaleasing.R
import tlt.th.co.toyotaleasing.analytics.AnalyticsManager
import tlt.th.co.toyotaleasing.analytics.AnalyticsScreenName
import tlt.th.co.toyotaleasing.common.extension.loadImageByUrl
import tlt.th.co.toyotaleasing.modules.custloan.MenuStepController
import tlt.th.co.toyotaleasing.modules.custloan.loanmodule.carloan.LoanUploadDocumentActivity

class CInfoCreditResultActivity : BaseActivity(), BestOfferSwipeAdapter.Listener   {

    private var dataEngine : CInfoCreditResultViewModel.DecisionEngine? = null

    private val data_extra by lazy {
        intent?.getStringExtra(REF_ID) ?: ""
    }

    private val data_url by lazy {
        intent?.getStringExtra(REF_URL) ?: ""
    }

    private val viewModel by lazy {
        ViewModelProviders.of(this).get(CInfoCreditResultViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_loan_info_credit_result)
        AnalyticsManager.trackScreen(AnalyticsScreenName.ONLINE_CREDIT_RESULT)
        initViewModel()
        initInstance()
        viewModel.getDecisionEngine(data_extra)

    }


    fun initInstance() {
        txt_ref_value.text = data_extra
        map_online_require_upper_sheet.stepCreditRes.background = ContextCompat.getDrawable(this, R.drawable.step_credit_result_active)
        map_online_require_upper_sheet.step2.setTextColor(ContextCompat.getColor(this@CInfoCreditResultActivity, R.color.cherry_red))



        btn_next_confirm.setOnClickListener {
            viewModel.SyncDecisionEngine(engineType = "" , offer_id = "", refID = data_extra)
        }

        btn_cancel.setOnClickListener {
            AnalyticsManager.onlineCreditUnAccept()
           viewModel.CancelDecisionEngine(  engineType = dataEngine!!.status , refID = data_extra )
        }

        btn_submit.setOnClickListener {

            if(dataEngine!!.status == "coffer" && dataEngine!!.flagOffer == "N"  ) {
                AnalyticsManager.onlineCreditAccept()
                LoanUploadDocumentActivity.openWithText(this@CInfoCreditResultActivity, data_extra, "", dataEngine!!.cofferList.ctypedesc, true)
            }else{
                var choice : String  = ""
                if(dataEngine!!.status == "doffer" ||  (dataEngine!!.status == "coffer" && dataEngine!!.flagOffer == "Y" ))  {
                    dataEngine!!.dofferList!!.forEach {
                        AnalyticsManager.onlineCreditDOfferAccept()
                        if(it.flagSelect){
                            choice = it.offerId
                        }
                    }
                }
                if(choice!="" ) {
                    viewModel.SyncDecisionEngine(engineType = dataEngine!!.status, offer_id = choice, refID = data_extra)
                }
            }
        }
//
//        map_online_loan_status_add_doc.btn_next_confirm_with_add_more_image_adddoc.setOnClickListener {
//            LoanUploadDocumentActivity.openWithText(this@CInfoCreditResultActivity ,  data_extra , "" ,  dataEngine!!.cofferList.ctypedesc, true)
//        }



    }

    fun initViewModel() {

        viewModel.whenLoading.observe(this, Observer {
            toggleLoadingScreenDialog(it!!)
        })

        viewModel.whenDataLoaded.observe(this, Observer {
            it?.let {
                initUpdateView(it)
            }
        })

        viewModel.whenSyncSuccessData.observe(this, Observer {
            it?.let {
                MenuStepController.open(this@CInfoCreditResultActivity, it.ref_id,it.step , it.ref_url )
            }
        })
    }

    fun initUpdateView( datas : CInfoCreditResultViewModel.DecisionEngine ){
        dataEngine = datas
        map_online_loan_status_approve.visibility = View.GONE
        map_online_loan_status_add_doc.visibility = View.GONE
        map_online_loan_status_reject.visibility = View.GONE
        map_online_loan_status_best_offer.visibility = View.GONE
        btn_cancel.visibility = View.GONE
        btn_submit.visibility = View.GONE
        btn_next_confirm.visibility = View.GONE
        if(dataEngine!!.status == "approve" ){
            map_online_loan_status_approve.visibility = View.VISIBLE
            btn_next_confirm.visibility  = View.VISIBLE
            map_online_loan_status_approve.title_approve.setText(dataEngine!!.statusHeader)
            map_online_loan_status_approve.txt_description_approve.setText(dataEngine!!.statusDetail)

            if(dataEngine!!.carDetail!= null) {
                map_online_loan_status_approve.txt_unique_id.setText(dataEngine!!.carDetail.rEFID)
                map_online_loan_status_approve.txt_cuurent_sts_detail.setText(dataEngine!!.carDetail.rESDES)
                map_online_loan_status_approve.txt_expire_date.setText(dataEngine!!.carDetail.eXPIREDATE)
                map_online_loan_status_approve.txt_stamp_date_des.setText(dataEngine!!.carDetail.sTAMPDATE)
                map_online_loan_status_approve.title_car_model.setText(dataEngine!!.carDetail.cARMODEL)
                map_online_loan_status_approve.txt_model.setText(dataEngine!!.carDetail.cARGRADE)
                map_online_loan_status_approve.txt_price.setText(dataEngine!!.carDetail.cARPRICE)
                map_online_loan_status_approve.img_car.loadImageByUrl(dataEngine!!.carDetail.cARIMAGE)
            }
        }else if(dataEngine!!.status == "doffer" ){
            map_online_loan_status_best_offer.visibility = View.VISIBLE
            btn_cancel.visibility = View.GONE
            btn_submit.visibility = View.VISIBLE
            map_online_loan_status_best_offer.title.setText(dataEngine!!.statusHeader)
            if(dataEngine!!.flagOffer == "Y") {
                val count = dataEngine!!.dofferList.size
//                dataEngine!!.dofferList.get(count-1).flagSelect = true
                getListBestOffer()
            }
        }else if(dataEngine!!.status == "coffer"  &&  dataEngine!!.flagOffer == "Y"){
            map_online_loan_status_best_offer.visibility = View.VISIBLE
            btn_cancel.visibility = View.VISIBLE
            btn_submit.visibility = View.VISIBLE
            map_online_loan_status_best_offer.title.setText(dataEngine!!.statusHeader)
            val count = dataEngine!!.dofferList.size
            dataEngine!!.dofferList.get(count-1).flagSelect = true
            getListBestOffer()
        }else if(dataEngine!!.status == "coffer" && dataEngine!!.flagOffer == "N" ){
            map_online_loan_status_add_doc.visibility = View.VISIBLE
            map_online_loan_status_add_doc.btn_next_confirm_with_add_more_image_adddoc.visibility = View.GONE
            btn_cancel.visibility = View.VISIBLE
            btn_submit.visibility = View.VISIBLE
            map_online_loan_status_add_doc.title_adddoc.setText(dataEngine!!.statusHeader)
            map_online_loan_status_add_doc.txt_description_adddoc.setText(dataEngine!!.statusDetail)
            map_online_loan_status_add_doc.btn_next_confirm_with_add_more_image_adddoc.visibility = View.GONE
        }else if(dataEngine!!.status == "reject" ){
            map_online_loan_status_reject.visibility = View.VISIBLE
            map_online_loan_status_reject.title_reject.setText(dataEngine!!.statusHeader)
            map_online_loan_status_reject.txt_description_reject.setText(dataEngine!!.statusDetail)
            btn_next_confirm.visibility  = View.VISIBLE
        }
    }

    fun getListBestOffer() {
        map_online_loan_status_best_offer.best_offer_bottom_sheet_rv.apply {
            layoutManager = LinearLayoutManager(context)
            addItemDecoration(DividerItemDecoration(ContextCompat.getDrawable(context, R.drawable.item_divider_recycler_view)))
            adapter = BestOfferSwipeAdapter(dataEngine!!.dofferList
                    ?: listOf(), this@CInfoCreditResultActivity)
        }
    }

    override fun onListClick(index: Int, item: CInfoCreditResultViewModel.dOffer) {
        if(item.flagSelect) {
            dataEngine!!.dofferList!![index].flagSelect = true
        } else {
            dataEngine!!.dofferList!!.forEach { it.flagSelect = false }
            dataEngine!!.dofferList!![index].flagSelect = true
        }
        getListBestOffer()
    }

    companion object {

        const val REF_ID = "REF_ID"
        const val REF_URL = "REF_URL"

        fun start(activity: Activity?, data: String , url : String ) {
            val intent = Intent(activity, CInfoCreditResultActivity::class.java)
            intent.putExtra(REF_ID, data)
            intent.putExtra(REF_URL, url)
            activity!!.startActivity(intent)
        }
    }
}
