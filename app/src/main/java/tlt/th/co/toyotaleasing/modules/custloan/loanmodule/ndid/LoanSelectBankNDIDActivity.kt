package tlt.th.co.toyotaleasing.modules.custloan.loanmodule.ndid

import android.app.Activity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import androidx.recyclerview.widget.GridLayoutManager
import android.view.View
import kotlinx.android.synthetic.main.fragment_loan_car_ndid_select_bank.*
import tlt.th.co.toyotaleasing.R
import tlt.th.co.toyotaleasing.common.base.BaseActivity
import tlt.th.co.toyotaleasing.common.dialog.MsgdescNormalDialog
import tlt.th.co.toyotaleasing.common.dialog.NDIDdialogFragment
import tlt.th.co.toyotaleasing.modules.custloan.MenuStepController
import tlt.th.co.toyotaleasing.modules.custloan.loanmodule.custinfo.CInfoCreditConsiderationActivity
import tlt.th.co.toyotaleasing.modules.custloan.loanmodule.ndid.common.IDPMethodAdapter


class LoanSelectBankNDIDActivity   : BaseActivity() ,  NDIDdialogFragment.Listener , MsgdescNormalDialog.Listener {


    private val data_extra by lazy {
        intent?.getStringExtra(REF_ID) ?: ""
    }

    private val data_extra_ndid by lazy {
        intent?.getStringExtra(NDID) ?: ""
    }

    private val data_description by lazy {
        intent?.getStringExtra(NDID_DESCRIPTION) ?: ""
    }

    private val viewModel by lazy {
        ViewModelProviders.of(this).get(LoanSelectBankNDIDViewModel::class.java)
    }

    private var dataNDID : LoanSelectBankNDIDViewModel.IDPListModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_loan_car_ndid_select_bank)

        initViewModel()
        initInstance()
        viewModel.getListDP(data_extra , data_extra_ndid) // "6444349660891"
    }


    fun initInstance() {
        initLayoutManager()

        txt_ref_value.text = data_extra
        btn_back.setOnClickListener {
            LoanWaitNDIDActivity.start(this@LoanSelectBankNDIDActivity!!, data_extra ,"")
        }
    }

    fun initViewModel(){

        viewModel.whenLoading.observe(this, Observer {
            toggleLoadingScreenDialog(it!!)
        })


        viewModel.whenDataIDPLoaded.observe(this, Observer { it ->
            it?.let {
                initDPListAdapter(it)

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

        viewModel.whenDataLoadedStep.observe(this, Observer {
            it?.let {
                if(it != "") {
                    MenuStepController.open(this@LoanSelectBankNDIDActivity, data_extra, it, "")
                }
            }
        })

    }

    private fun initLayoutManager() {

        rv_payment_ndid_method_choice.apply {
            layoutManager = GridLayoutManager(this@LoanSelectBankNDIDActivity, 3)
            isNestedScrollingEnabled = false
        }
    }

    private fun initDPListAdapter(dataList: List<LoanSelectBankNDIDViewModel.IDPListModel> = listOf()) {
        status_title_des.visibility = View.GONE
        rv_payment_ndid_method_choice.adapter = IDPMethodAdapter(dataList.toMutableList(), IDPMethodListener)

    }
    private val IDPMethodListener = { position: Int, items: LoanSelectBankNDIDViewModel.IDPListModel ->
        dataNDID = items
        showNDIDpopup()
    }

   fun  showNDIDpopup(){
       try{
           NDIDdialogFragment.show(
                   fragmentManager = supportFragmentManager,
                   description =    getString(R.string.dialog_question_choose_bank_ndid , dataNDID?.marketingName) ,
                   textres1 = data_extra ,
                   textres2 = data_extra_ndid,
                   confirmButtonMessage = getString(R.string.btn_confirm) ,
                   cancelButtonMessage = getString(R.string.btn_cancel)
           )
       }catch (e : Exception){
           e.message
       }
   }

    override fun onDialogCancelClick() {

    }

    override fun onDialogConfirmClick() {
        setResult(Activity.RESULT_OK)
        viewModel.submit( refID =  data_extra,
                          nodeID =  dataNDID!!.nodeId,
                          indCode=  dataNDID!!.indCode,
                          compCode=  dataNDID!!.compCode,
                          ndidID=  data_extra_ndid ,
                          tltmsg = data_description  )
//        finish()
    }

    companion object {
        const val REF_ID = "REF_ID"
        const val REF_URL = "REF_URL"
        const val NDID = "NDID"
        const val  NDID_DESCRIPTION = "NDID_DESCRIPTION"

        fun start(activity: Activity?, data: String , desc : String , ndid : String = "") {
            val intent = Intent(activity, LoanSelectBankNDIDActivity::class.java)
            intent.putExtra(REF_ID, data)
            intent.putExtra(NDID_DESCRIPTION, desc)
            intent.putExtra(NDID, ndid)
            activity!!.startActivity(intent)
        }
    }

}