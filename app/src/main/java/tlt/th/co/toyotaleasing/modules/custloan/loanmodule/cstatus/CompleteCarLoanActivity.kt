package tlt.th.co.toyotaleasing.modules.custloan.loanmodule.cstatus

import android.app.Activity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_complete_carloan.*
import tlt.th.co.toyotaleasing.R
import tlt.th.co.toyotaleasing.analytics.AnalyticsManager
import tlt.th.co.toyotaleasing.analytics.AnalyticsScreenName
import tlt.th.co.toyotaleasing.common.base.BaseActivity
import tlt.th.co.toyotaleasing.common.extension.gone
import tlt.th.co.toyotaleasing.common.extension.visible
import tlt.th.co.toyotaleasing.modules.custloan.MenuStepController
import tlt.th.co.toyotaleasing.modules.custloan.loanmodule.center.CarLoanFragment
import tlt.th.co.toyotaleasing.util.ExternalAppUtils


class CompleteCarLoanActivity : BaseActivity() {

    private val viewModel by lazy {
        ViewModelProviders.of(this).get(CompleteCarLoanViewModel::class.java)
    }

    private val data_extra by lazy {
        intent?.getStringExtra(REF_ID) ?: ""
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_complete_carloan)
        AnalyticsManager.trackScreen(AnalyticsScreenName.ONLINE_ENDGAME)
        initViewModel()
        initInstances()

    }


    private fun initViewModel() {
        viewModel.whenLoading.observe(this, Observer {
            toggleLoadingScreenDialog(it!!)
        })


        viewModel.whenSyncSuccessData.observe(this, Observer {
            it?.let{
                MenuStepController.open(this@CompleteCarLoanActivity, it.ref_id , it.step  , it.ref_url)
            }
        })


        viewModel.whenDocLoaded.observe(this, Observer {
            it?.let{
                    updateUI(it)
            }
        })

//        viewModel.whenDocLoaded.observe(this, Observer {
//            it?.let{
//                ViewEContractActivity.open(this@CompleteCarLoanActivity ,  data_extra , it.pdfName , it.pdfFile )
//            }
//        })

        viewModel.whenLinkLoaded.observe(this, Observer {
            it?.let{
                    ExternalAppUtils.openByLink(this, it)
            }
        })
    }

    private fun initInstances() {

        txt_ref_value.text = data_extra
        toolbar.setTitle("")
        viewModel.getEContract(data_extra)

        btn_next_confirm.setOnClickListener {
            CarLoanFragment.startByInsight(
                    context = this@CompleteCarLoanActivity, authen = "Y"
            )
        }

        btn_view_ecotract.setOnClickListener {
//             viewModel.getEContract(data_extra)
            AnalyticsManager.onlineEndViewEcontract()
            ViewEContractActivity.open(this@CompleteCarLoanActivity ,  data_extra , "ECONTRACT" )
        }


        bt_view_manual.setOnClickListener {
            viewModel.getDigitalHandbook()
        }


        btn_view_installment.setOnClickListener {
            AnalyticsManager.onlineEndViewInstalment()
            ViewEContractActivity.open(this@CompleteCarLoanActivity ,  data_extra , "INSTALLMENT")
        }

    }

    private fun updateUI(econtractModel: CompleteCarLoanViewModel.EcontractModel) {

        if(econtractModel.FLAG_INSTALLMENT == "Y"){
            divider1.visible()
            txt_view_installment.visible()
            btn_view_installment.visible()
        }else{
            divider1.gone()
            txt_view_installment.gone()
            btn_view_installment.gone()
        }
    }

    companion object {
        const val REF_ID = "REF_ID"
        const val REF_URL = "REF_URL"
        const val PDF_NAME = "PDF_NAME"
        const val PDF_BASE64 = "PDF_BASE64"

        fun start(activity: Activity?, data: String , url : String ) {
            val intent = Intent(activity, CompleteCarLoanActivity::class.java)
            intent.putExtra(REF_ID, data)
            intent.putExtra(REF_URL, url)
            activity!!.startActivity(intent)
        }

    }
}
