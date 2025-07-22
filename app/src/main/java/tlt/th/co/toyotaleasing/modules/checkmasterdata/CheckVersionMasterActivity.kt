package tlt.th.co.toyotaleasing.modules.checkmasterdata

import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.os.Bundle
import tlt.th.co.toyotaleasing.R
import tlt.th.co.toyotaleasing.common.base.BaseActivity
import tlt.th.co.toyotaleasing.modules.main.MainViewModel

class CheckVersionMasterActivity : BaseActivity() {

    private val viewModel by lazy {
        ViewModelProviders.of(this).get(MainViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_check_version_master)

        initViewModel()

        viewModel.getIntialByApi()
    }

    private fun initViewModel() {
        viewModel.whenLoading.observe(this, Observer {
            toggleLoadingScreenDialog(it!!)
        })

        viewModel.whenUserIsCustomer.observe(this, Observer {
            finish()
        })

        viewModel.whenUserIsNotCustomer.observe(this, Observer {
            finish()
        })

        viewModel.whenNoInternetConnection.observe(this, Observer {
            finish()
        })
    }

    companion object {
        fun start(context: Context?) {
            val intent = Intent(context, CheckVersionMasterActivity::class.java)
            context?.startActivity(intent)
        }
    }
}
