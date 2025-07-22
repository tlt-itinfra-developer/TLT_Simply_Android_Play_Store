package tlt.th.co.toyotaleasing.modules.custloan.loanmodule.delivery

import android.app.Activity
import androidx.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import kotlinx.android.synthetic.main.fragment_loan_signature.*
import tlt.th.co.toyotaleasing.R
import tlt.th.co.toyotaleasing.common.base.BaseActivity
import tlt.th.co.toyotaleasing.modules.custloan.loanmodule.basicdetail.BasicCustProfileActivity
import tlt.th.co.toyotaleasing.modules.custloan.loanmodule.ndid.LoanEconsentActivity
import tlt.th.co.toyotaleasing.util.ImageUtils


class SignatureEConsentActivity : BaseActivity() {

    private val data_extra by lazy {
        intent?.getStringExtra(REF_ID) ?: ""
    }

    private val data_url by lazy {
        intent?.getStringExtra(REF_URL) ?: ""
    }


    private val viewModel by lazy {
        ViewModelProviders.of(this).get(SignatureEConsentViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_loan_signature)

        initInstance()
    }


    fun initInstance() {


        btn_sign_submit.setOnClickListener {

        }

        btn_sign_clear.setOnClickListener {
        }


    }

    companion object {

        const val REF_ID = "REF_ID"
        const val REF_URL = "REF_URL"

        fun start(activity: Activity?, data: String , url : String ) {
            val intent = Intent(activity, SignatureEConsentActivity::class.java)
            intent.putExtra(REF_ID, data)
            intent.putExtra(REF_URL, url)
            activity!!.startActivity(intent)
        }
    }

}