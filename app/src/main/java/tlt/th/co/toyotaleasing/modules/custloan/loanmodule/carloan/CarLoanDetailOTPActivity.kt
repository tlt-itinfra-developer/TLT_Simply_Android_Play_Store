package tlt.th.co.toyotaleasing.modules.custloan.loanmodule.carloan

import android.app.Activity
import androidx.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_loan_car_loan_otp.*
import tlt.th.co.toyotaleasing.R
import tlt.th.co.toyotaleasing.common.base.BaseActivity
import tlt.th.co.toyotaleasing.common.dialog.NormalDialogFragment
import tlt.th.co.toyotaleasing.modules.custloan.loanmodule.center.CarLoanFragment
import tlt.th.co.toyotaleasing.modules.custloan.loanmodule.custinfo.CInfoInformationActivity
import tlt.th.co.toyotaleasing.view.KeyboardNumberWidget
import tlt.th.co.toyotaleasing.view.OTPEdittext


class CarLoanDetailOTPActivity  : BaseActivity(), NormalDialogFragment.Listener {

    private val viewModel by lazy {
        ViewModelProviders.of(this).get(CarLoanDetailOTPViewModel::class.java)
    }

    private val stepPosition by lazy {
        intent?.getStringExtra(STEP_POSITION_EXTRA)?: "1"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_loan_car_loan_otp)

        keyboard_number_view.setListener(object : KeyboardNumberWidget.Listener {
            override fun onNumberClick(number: String) {
                edittext_otp.addNumber(number)
            }

            override fun onDeleteClick() {
                edittext_otp.deleteNumber()
            }
        })

        edittext_otp.setListener(object : OTPEdittext.Listener {
            override fun onOTPComplete(otp: String) {
                CInfoInformationActivity.start(this@CarLoanDetailOTPActivity, "1" )
            }
        })

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
            val intent = Intent(activity, CarLoanDetailOTPActivity::class.java)
            intent.putExtra(UNIQUE_ID, item)
            intent.putExtra(STEP_POSITION_EXTRA, position)
            activity!!.startActivity(intent)
        }

        fun show(context: Context, position : String ) {
            val intent = Intent(context, CarLoanDetailOTPActivity::class.java)
            intent.putExtra(STEP_POSITION_EXTRA, position)
            context.startActivity(intent)
        }


    }
}