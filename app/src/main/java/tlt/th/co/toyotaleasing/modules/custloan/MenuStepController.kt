package tlt.th.co.toyotaleasing.modules.custloan

import android.content.Context
import android.content.Intent
import android.os.Bundle
import tlt.th.co.toyotaleasing.common.base.BaseActivity
import tlt.th.co.toyotaleasing.modules.custloan.loanmodule.basicdetail.*
import tlt.th.co.toyotaleasing.modules.custloan.loanmodule.carloan.*
import tlt.th.co.toyotaleasing.modules.custloan.loanmodule.center.CarLoanFragment
import tlt.th.co.toyotaleasing.modules.custloan.loanmodule.cstatus.CompleteCarLoanActivity
import tlt.th.co.toyotaleasing.modules.custloan.loanmodule.cstatus.PreviewEContractActivity
import tlt.th.co.toyotaleasing.modules.custloan.loanmodule.cstatus.SummaryLoanActivity
import tlt.th.co.toyotaleasing.modules.custloan.loanmodule.cstatus.VerifyandConfirmActivity
import tlt.th.co.toyotaleasing.modules.custloan.loanmodule.custinfo.CInfoCreditConsiderationActivity
import tlt.th.co.toyotaleasing.modules.custloan.loanmodule.custinfo.CInfoCreditResultActivity
import tlt.th.co.toyotaleasing.modules.custloan.loanmodule.ndid.*

class MenuStepController : BaseActivity() {


    private val refID by lazy {
        intent?.getStringExtra(REF_ID)?: ""
//        "RF-201900000002"
    }

    private val stepIDPosition by lazy {
        intent?.getStringExtra(MAIN_ID_EXTRA) ?: ""
    }

    private val refURL by lazy {
        intent?.getStringExtra(REF_URL)?: ""
    }

    private val ndidID by lazy {
        intent?.getStringExtra(NDID) ?: ""
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

          when (stepIDPosition) {
              "IDCARD"-> {
                  BasicIDCardFrontActivity.start(this@MenuStepController , refID  , refURL)
              }"LIVENESS"-> {
                  BasicLivenessActivity.start(this@MenuStepController , refID , refURL)
              }"DOPAINFO"-> {
                  BasicInformationActivity.start(this@MenuStepController , refID , refURL)
              }"PROFILE"-> {
                  BasicCustProfileActivity.start(this@MenuStepController , refID , refURL)
              }"DOCUPLOAD"-> {
                  LoanUploadDocumentActivity.start(this@MenuStepController , refID, refURL )
              }"PERSONINFO"-> {
                  LoanPersonalInfomationActivity.start(this@MenuStepController , refID , refURL)
              }"CDD"-> {
                 LoanPersonalCDDctivity.start(this@MenuStepController , refID , refURL)
              }"NDIDTERM"-> {
                  LoanTermsConNDIDActivity.start(this@MenuStepController , refID , refURL )
              }"ECONSENTTERM"-> {
                  LoanEconsentActivity.start(this@MenuStepController , refID , refURL)
              }"WAITNDID"-> {
                LoanWaitNDIDActivity.start(this@MenuStepController , refID , refURL)
              }"NDIDKYC"-> {
                LoanSelectBankNDIDActivity.start(this@MenuStepController , refID , refURL , ndidID)
              }"DENGINE"-> {
                 CInfoCreditConsiderationActivity.start(this@MenuStepController , refID , refURL)
              }"DENGINERESULT"-> {
                 CInfoCreditResultActivity.start(this@MenuStepController , refID , refURL)
              }"VERIFY"-> {
                VerifyandConfirmActivity.start(this@MenuStepController , refID , refURL)
              }"EXPENSES"-> {
                SummaryLoanActivity.start(this@MenuStepController , refID , refURL)
              }"REVIEWECONTRACT"-> {
                 PreviewEContractActivity.start(this@MenuStepController , refID , refURL)
              }"WAITNDID_ECONTRACT"-> {
                 LoanEContractWaitNDIDActivity.start(this@MenuStepController , refID , refURL)
              }"NDIDKYC_ECONTRACT"-> {
                 LoanEContractSelectBankNDIDActivity.start(this@MenuStepController , refID , refURL  , ndidID)
              }"END_GAME"-> {
                 CompleteCarLoanActivity.start(this@MenuStepController , refID , refURL)
          }else -> {
                CarLoanFragment.startByInsightDeeplink(context = this@MenuStepController, item = ""
              )
             }
          }
    }

    companion object {
        const val REF_ID = "REF_ID"
        const val MAIN_ID_EXTRA = "MAIN_ID_EXTRA"
        const val REF_URL = "REF_URL"
        const val NDID = "NDID"


        fun open(context: Context, refid : String, step_id : String , ref_url : String , ndid : String = "") {
            val intent = Intent(context, MenuStepController::class.java)
            intent.putExtra(REF_ID,refid)
            intent.putExtra(MAIN_ID_EXTRA,step_id)
            intent.putExtra(REF_URL,ref_url)
            intent.putExtra(NDID,ndid)
            context.startActivity(intent)
        }
    }

}
