package tlt.th.co.toyotaleasing.modules.contract.specialoffer

import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_special_offer.*
import tlt.th.co.toyotaleasing.R
import tlt.th.co.toyotaleasing.common.base.BaseActivity
import tlt.th.co.toyotaleasing.common.extension.showToast
import tlt.th.co.toyotaleasing.modules.contract.contract.ContractViewModel

class SpecialOfferActivity : BaseActivity() {

    private val viewModel by lazy {
        ViewModelProviders.of(this).get(SpecialOfferViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_special_offer)
        initInstance()
        initViewModel()
    }

    private fun initInstance() {
        viewModel.getPrivilege()
        recycler_view.isNestedScrollingEnabled = false
        recycler_view.layoutManager = LinearLayoutManager(this)
        recycler_view.adapter = SpecialOfferAdapter()
    }

    private fun initViewModel() {
        viewModel.whenLoading.observe(this, Observer {
            toggleLoadingScreenDialog(it!!)
        })

        viewModel.whenPrivilegeLoaded.observe(this, Observer {
            recycler_view.adapter.apply {
                val adapter = this as SpecialOfferAdapter
                adapter.updateItem(it!!)
            }
        })

        viewModel.whenPrivilegeIsEmpty.observe(this, Observer {
            showToast(it!!)
        })

        viewModel.whenDataLoaded.observe(this, Observer {
            setDataIntoView(it!!)
        })
    }

    private fun setDataIntoView(data: ContractViewModel.Model) {
        car_contract_date.text = data.date
        car_license_txt.text = data.carLicense
        name_txt.text = data.fullname
        txt_car_no.text = data.carNo
        txt_car_model.text = data.carModel
    }

    companion object {
        fun start(context: Context?) {
            context?.startActivity(Intent(context, SpecialOfferActivity::class.java))
        }
    }
}
