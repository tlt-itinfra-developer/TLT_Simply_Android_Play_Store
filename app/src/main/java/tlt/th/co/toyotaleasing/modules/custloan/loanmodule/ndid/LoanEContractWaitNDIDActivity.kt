package tlt.th.co.toyotaleasing.modules.custloan.loanmodule.ndid

import android.app.Activity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.os.Handler
import kotlinx.android.synthetic.main.fragment_loan_car_ndid_authen.*
import kotlinx.android.synthetic.main.fragment_loan_car_ndid_authen.btn_next_confirm
import tlt.th.co.toyotaleasing.R
import tlt.th.co.toyotaleasing.analytics.AnalyticsManager
import tlt.th.co.toyotaleasing.analytics.AnalyticsScreenName
import tlt.th.co.toyotaleasing.common.base.BaseActivity
import tlt.th.co.toyotaleasing.common.dialog.MsgdescNormalDialog
import tlt.th.co.toyotaleasing.modules.custloan.MenuStepController
import tlt.th.co.toyotaleasing.modules.custloan.loanmodule.center.CarLoanFragment


class LoanEContractWaitNDIDActivity : BaseActivity()  , MsgdescNormalDialog.Listener{


    private val data_extra by lazy {
        intent?.getStringExtra(REF_ID) ?: ""
    }
    private val data_url by lazy {
        intent?.getStringExtra(REF_URL) ?: ""
    }

    private var data_ndid : String  = ""
    private var data_ndid_description : String  = ""
    private var screen_name : String  = "WAITNDID_ECONTRACT"

    private val viewModel by lazy {
        ViewModelProviders.of(this).get(LoanEContractWaitNDIDViewModel::class.java)
    }

    private val FIVE_SECOND = 5000L
    private val handlerAutoCallNDIDRes = Handler()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_loan_car_ndid_authen)
        AnalyticsManager.trackScreen(AnalyticsScreenName.ONLINE_WAIT_NDID)
        initViewModel()
        initInstances()
        viewModel.getWaitNDID(data_extra)
    }

    private fun initInstances() {
        txt_ref_value.text = data_extra

//        toggleLoadingScreenDialog(isEnable = false)

        btn_next_confirm.setOnClickListener {
            viewModel.CheckStepAPI(data_extra ,screen_name)
        }
    }

    private fun initViewModel() {

        viewModel.whenLoading.observe(this, Observer {
            toggleLoadingScreenDialog(it!!)
        })

        viewModel.whenDataAuthenLoaded.observe(this, Observer {
            initMenus(it!!)
        })

        viewModel.whenSyncSuccessData.observe(this, Observer {
            it?.let {
                if (it.step != "WAITNDID_ECONTRACT") {
                    MenuStepController.open(this@LoanEContractWaitNDIDActivity, it.ref_id, it.step, data_ndid_description , data_ndid)
//                LoanWaitNDIDActivity.start(this@LoanEconsentActivity!!, it.ref_id , it.ref_url)
                }
            }
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

    fun initMenus(it: LoanEContractWaitNDIDViewModel.AuthenModel) {

        ipt_tlt_ref.text =  it.rEFID
        ipt_ndid_ref.text = it.nDIDREFID
        //txt_ndid_description.text = it.rEQUESTMSG
        data_ndid = it.nDIDREFID
        data_ndid_description = it.rEQUESTMSGTH + it.nDIDREFID

        when (it.dESCSTATUS) {
            "W" -> btn_next_confirm.visibility = View.VISIBLE
            "Y" -> btn_next_confirm.visibility = View.VISIBLE
            "V" -> btn_next_confirm.visibility = View.GONE
            "N" -> btn_next_confirm.visibility = View.VISIBLE
            else ->  btn_next_confirm.visibility = View.VISIBLE
        }

        if( it.bUTTON  != ""){
            btn_next_confirm.text = it.bUTTON
        }else{
            btn_next_confirm.text = getString(R.string.btn_next)
        }


        if( it.dESC  != ""){
            txt_please_authen.text = it.dESC
        }else{
            txt_please_authen.text = ""
        }

    }

    override fun onResume() {
        super.onResume()
        handlerAutoCallNDIDRes.post(autoVerifyEngine)
    }

    override fun onStop() {
        handlerAutoCallNDIDRes.removeCallbacks(autoVerifyEngine)
        super.onStop()
    }

    private val autoVerifyEngine = object : Runnable {
        override fun run() {
            viewModel.getWaitNDID(data_extra)
            handlerAutoCallNDIDRes.postDelayed(this, FIVE_SECOND)
        }
    }

    override fun onDialogConfirmClick() {
    }

    override fun onDialogCancelClick() {
    }

    companion object {

        const val REF_ID = "REF_ID"
        const val REF_URL = "REF_URL"

        fun start(activity: Activity?, data: String , url : String) {
            val intent = Intent(activity, LoanEContractWaitNDIDActivity::class.java)
            intent.putExtra(REF_ID, data)
            intent.putExtra(REF_URL, url)
            activity!!.startActivity(intent)
        }

        fun OPEN(activity: Context?, data: String , url : String ) {
            val intent = Intent(activity, LoanEContractWaitNDIDActivity::class.java)
            intent.putExtra(REF_ID, data)
            intent.putExtra(REF_URL, url)
            activity!!.startActivity(intent)
        }

    }

}