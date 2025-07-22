package tlt.th.co.toyotaleasing.modules.custloan.loanmodule.cstatus

import android.app.Activity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_otherexpense_carloan.*
import tlt.th.co.toyotaleasing.R
import tlt.th.co.toyotaleasing.common.base.BaseActivity
import tlt.th.co.toyotaleasing.modules.custloan.loanmodule.cstatus.common.OtherExpense
import tlt.th.co.toyotaleasing.modules.custloan.loanmodule.cstatus.common.OtherExpenseAdapter
import tlt.th.co.toyotaleasing.view.DividerItemDecoration
import java.lang.Exception


class OtherExpenseLoanActivity : BaseActivity() {

    private val viewModel by lazy {
        ViewModelProviders.of(this).get(CompleteCarLoanViewModel::class.java)
    }

    private val data_extra by lazy {
        intent?.getStringExtra(REF_ID) ?: ""
    }

    val data_expenselist : List<OtherExpense>? = SummaryLoanActivity.otherDataList

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_otherexpense_carloan)
        initViewModel()
        initInstances()


    }


    private fun initViewModel() {
        viewModel.whenLoading.observe(this, Observer {
            toggleLoadingScreenDialog(it!!)
        })



    }

    private fun initInstances() {
        try{
            txt_ref_value.text = data_extra
            if(  data_expenselist!= null ) {
                getListOtherExpense()
            }
        }catch (e : Exception){
            e.message
        }

    }

    fun getListOtherExpense() {
        recycler_view.apply {
            layoutManager = LinearLayoutManager(context)
            addItemDecoration(DividerItemDecoration(ContextCompat.getDrawable(context, R.drawable.item_divider_recycler_view)))
            adapter = data_expenselist?.let { OtherExpenseAdapter(it) }
        }
    }

    companion object {
        const val REF_ID = "REF_ID"
        const val REF_URL = "REF_URL"
        const val LISTOTHER = "LISTOTHER"


        fun start(activity: Activity?, data: String, url: String) {
            val intent = Intent(activity, OtherExpenseLoanActivity::class.java)
            intent.putExtra(REF_ID, data)
            intent.putExtra(REF_URL, url)
            activity!!.startActivity(intent)
        }

    }
}
