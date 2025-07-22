package tlt.th.co.toyotaleasing.modules.custloan.loanmodule.carloan

import android.app.Activity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import android.view.View
import kotlinx.android.synthetic.main.fragment_loan_car_loan_upload_doc.*
import kotlinx.android.synthetic.main.fragment_loan_car_loan_upload_doc.btn_next_confirm
import kotlinx.android.synthetic.main.fragment_loan_car_loan_upload_doc.map_online_require_upper_sheet
import kotlinx.android.synthetic.main.loan_car_loan_upper_sheet.view.*
import tlt.th.co.toyotaleasing.common.base.BaseActivity
import tlt.th.co.toyotaleasing.modules.custloan.loanmodule.center.CarLoanFragment
import tlt.th.co.toyotaleasing.common.dialog.MsgdescNormalDialog
import tlt.th.co.toyotaleasing.modules.custloan.MenuStepController
import tlt.th.co.toyotaleasing.modules.custloan.loanmodule.carloan.common.LoanUploadDocAdapter
import tlt.th.co.toyotaleasing.modules.custloan.loanmodule.custinfo.CInfoCreditResultActivity
import tlt.th.co.toyotaleasing.view.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import tlt.th.co.toyotaleasing.R
import tlt.th.co.toyotaleasing.analytics.AnalyticsManager
import tlt.th.co.toyotaleasing.analytics.AnalyticsScreenName


class LoanUploadDocumentActivity   : BaseActivity() , LoanUploadDocAdapter.Listener  , MsgdescNormalDialog.Listener {

    private val data_extra by lazy {
        intent?.getStringExtra(REF_ID) ?: ""
    }

    private val data_url by lazy {
        intent?.getStringExtra(REF_URL) ?: ""
    }

    private val data_desc by lazy {
        intent?.getStringExtra(REF_DESC) ?: ""
    }

    private val isFromAddOther by lazy {
        intent.getBooleanExtra(OTHER, false)
    }

    private val viewModel by lazy {
        ViewModelProviders.of(this).get(LoanUploadDocumentViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_loan_car_loan_upload_doc)
       // AnalyticsManager.trackScreen(AnalyticsScreenName.ONLINE_ADDDOC)
        initViewModel()
        initInstance()

        viewModel.getSumDocumentRequire(data_extra)

    }

    override fun onTopicClick(index: Int, item: LoanUploadDocumentViewModel.DocItem) {
        if(!isFromAddOther)
            SyncDocumentActivity.start(this@LoanUploadDocumentActivity, data_extra , item.dOCTYPE , item.dOCDESC ) //  item
        else
            SyncDocumentActivity.openWithText(this@LoanUploadDocumentActivity, data_extra,"",item.dOCTYPE , item.dOCDESC ,  data_desc , true   )
    }

    fun initInstance() {

        if(isFromAddOther) {
            AnalyticsManager.trackScreen(AnalyticsScreenName.ONLINE_ADDDOC)
            map_online_require_upper_sheet.visibility = View.GONE
            title_step.visibility = View.GONE
        }else{

            map_online_require_upper_sheet.visibility = View.VISIBLE
            map_online_require_upper_sheet.stpUploadIMG.background = ContextCompat.getDrawable(this, R.drawable.step_upload_active)
            map_online_require_upper_sheet.step1.setTextColor(ContextCompat.getColor(this@LoanUploadDocumentActivity, R.color.cherry_red))
            map_online_require_upper_sheet.txt_ref_value.text = data_extra
        }

        txt_description.text =   data_desc
        if(data_desc.equals("")){
            lay_description.visibility = View.GONE
        }else{
            lay_description.visibility = View.VISIBLE
        }

        swipe_refresh.isEnabled = false
        swipe_refresh.isRefreshing = false
        recycler_view.layoutManager = LinearLayoutManager(this@LoanUploadDocumentActivity)


        btn_next_confirm.setOnClickListener {
//            if(isFromAddOther){
//                AnalyticsManager.onlineCreditAddDoc()
//            }
//            viewModel.SubmitDocUpload(data_extra)
//            LoanSelectBankNDIDActivity.start(this@LoanUploadDocumentActivity , data_extra , "")
        }
    }

    private fun initViewModel() {

        viewModel.whenLoading.observe(this, Observer {
            swipe_refresh.isRefreshing = it!!
            toggleLoadingScreenDialog(it!!)
        })

        viewModel.whenDataLoaded.observe(this, Observer {
            it?.let {
                swipe_refresh.isRefreshing = false
                recycler_view?.apply {
                    layoutManager = LinearLayoutManager(context)
                    layoutManager = GridLayoutManager(this@LoanUploadDocumentActivity, 2)
                    adapter = LoanUploadDocAdapter(it?: listOf(), this@LoanUploadDocumentActivity)
                }
            }
        })

        viewModel.whenSyncSuccessData.observe(this, Observer {
            it?.let {
                AnalyticsManager.onlineAdddoc()
                MenuStepController.open(this@LoanUploadDocumentActivity, it.ref_id,it.step , it.ref_url )
            }
        })

        viewModel.whenSyncFailureShowMsg.observe(this, Observer {
            toggleLoadingScreenDialog(false)
        })

        viewModel.whenDataLoadedMessage.observe(this, Observer {
            it?.let {
                MsgdescNormalDialog.show(
                        fragmentManager = supportFragmentManager,
                        description = it ,
                        confirmButtonMessage = getString(R.string.dialog_button_ok)
                )
            }
        })
    }

    override fun onDialogConfirmClick() {
    }

    override fun onDialogCancelClick() {
    }

    companion object {

        const val REF_ID = "REF_ID"
        const val REF_URL = "REF_URL"
        const val REF_DESC = "REF_DESC"
        const val OTHER = "OTHER"

        fun start(activity: Activity?, data: String , url : String ) {
            val intent = Intent(activity, LoanUploadDocumentActivity::class.java)
            intent.putExtra(REF_ID, data)
            intent.putExtra(REF_URL, url)
            activity!!.startActivity(intent)
        }

        fun OPEN(activity: Context?, data: String , url : String ) {
            val intent = Intent(activity, LoanUploadDocumentActivity::class.java)
            intent.putExtra(REF_ID, data)
            intent.putExtra(REF_URL, url)
            activity!!.startActivity(intent)
        }

        fun openWithText(activity: Context?, data: String ,  url : String  , txtDes : String , flagAdd : Boolean ) {
            val intent = Intent(activity, LoanUploadDocumentActivity::class.java)
            intent.putExtra(REF_ID, data)
            intent.putExtra(REF_URL, url)
            intent.putExtra(OTHER, flagAdd)
            intent.putExtra(REF_DESC, txtDes)
            activity!!.startActivity(intent)
        }
    }

}
