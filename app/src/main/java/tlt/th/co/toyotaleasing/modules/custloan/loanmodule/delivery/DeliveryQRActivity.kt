package tlt.th.co.toyotaleasing.modules.custloan.loanmodule.delivery

import android.app.Activity
import androidx.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.os.Bundle
import tlt.th.co.toyotaleasing.R
import tlt.th.co.toyotaleasing.common.base.BaseActivity
import tlt.th.co.toyotaleasing.common.dialog.NormalDialogFragment
import tlt.th.co.toyotaleasing.modules.custloan.loanmodule.center.CarLoanFragment


class DeliveryQRActivity  : BaseActivity(), NormalDialogFragment.Listener {

    private val viewModel by lazy {
        ViewModelProviders.of(this).get(DeliveryQRViewModel::class.java)
    }

    private val stepPosition by lazy {
        intent?.getStringExtra(STEP_POSITION_EXTRA)?: "1"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_loan_sts_ver_confirm)

//        initTablayout()


    }

    private fun initTablayout() {

    }


    override fun onDialogConfirmClick() {
        setResult(Activity.RESULT_OK)
        finish()
    }

    override fun onDialogCancelClick() {

    }

    companion object {
        const val UNIQUE_ID = "UNIQUE_ID"
        const val DATA_STEP_POSITION_EXTRA = "DATA_STEP_POSITION_EXTRA"
        private const val STEP_POSITION_EXTRA = "STEP_POSITION_EXTRA"



        fun start(activity: Activity?, item: String , position : Int) {
            val intent = Intent(activity, DeliveryQRActivity::class.java)
            intent.putExtra(UNIQUE_ID, item)
            intent.putExtra(STEP_POSITION_EXTRA, position)
            activity!!.startActivity(intent)
        }

        fun show(context: Context, position : String ) {
            val intent = Intent(context, DeliveryQRActivity::class.java)
            intent.putExtra(STEP_POSITION_EXTRA, position)
            context.startActivity(intent)
        }


    }
}