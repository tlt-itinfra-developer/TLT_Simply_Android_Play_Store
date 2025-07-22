package tlt.th.co.toyotaleasing.modules.custloan.loanmodule.basicdetail

import android.app.Activity
import androidx.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_loan_basic_cust_profile.*
import kotlinx.android.synthetic.main.loan_require_upper_sheet.view.*

import tlt.th.co.toyotaleasing.R
import tlt.th.co.toyotaleasing.common.base.BaseActivity
import tlt.th.co.toyotaleasing.common.dialog.NormalDialogFragment
import tlt.th.co.toyotaleasing.modules.custloan.loanmodule.carloan.GuidlineRequireDocActivity
import tlt.th.co.toyotaleasing.modules.custloan.loanmodule.center.CarLoanFragment


class BasicCustProfileActivity  : BaseActivity(), NormalDialogFragment.Listener {

    private val viewModel by lazy {
        ViewModelProviders.of(this).get(BasicCustProfileViewModel::class.java)
    }

    private val data_extra by lazy {
        intent?.getStringExtra(REF_ID) ?: ""
    }

    private val data_url by lazy {
        intent?.getStringExtra(REF_URL) ?: ""
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_loan_basic_cust_profile)

        initInstance()
        initViewModel()


    }

    fun initInstance() {

        map_online_require_upper_sheet.stepCustomerProfile.background = ContextCompat.getDrawable(this, R.drawable.shape_circle_loan_menu)
        map_online_require_upper_sheet.stepCustomerProfile.setTextColor(ContextCompat.getColor(this@BasicCustProfileActivity, R.color.white))
        map_online_require_upper_sheet.step4.setTextColor(ContextCompat.getColor(this@BasicCustProfileActivity, R.color.cherry_red))
        map_online_require_upper_sheet.txt_ref_value.text = data_extra


        btn_next_confirm.setOnClickListener{
            GuidlineRequireDocActivity.start(this@BasicCustProfileActivity, data_extra )
        }

    }


    fun initViewModel(){


    }
    override fun onDialogConfirmClick() {
        setResult(Activity.RESULT_OK)
        finish()
    }

    override fun onDialogCancelClick() {

    }

    companion object {
        const val REF_ID = "REF_ID"
        const val REF_URL = "REF_URL"

        fun start(activity: Activity?, data: String , url : String ) {
            val intent = Intent(activity, BasicCustProfileActivity::class.java)
            intent.putExtra(REF_ID, data)
            intent.putExtra(REF_URL, url)
            activity!!.startActivity(intent)
        }
    }
}