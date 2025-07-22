package tlt.th.co.toyotaleasing.modules.custloan.loanmodule.carloan

import android.Manifest
import android.app.Activity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import kotlinx.android.synthetic.main.activity_loan_guidline.*
import tlt.th.co.toyotaleasing.R
import tlt.th.co.toyotaleasing.common.base.BaseActivity
import tlt.th.co.toyotaleasing.modules.custloan.loanmodule.basicdetail.BasicIDCardFrontActivity
import tlt.th.co.toyotaleasing.modules.custloan.loanmodule.center.common.GuildlineRequireAdapter


class GuidlineRequireDocActivity  : BaseActivity()  {


    private val data_extra by lazy {
        intent?.getStringExtra(DATA_EXTRA)?: ""
    }


    private val viewModel by lazy {
        ViewModelProviders.of(this).get(GuidlineRequireDocViewModel::class.java)
    }


    private fun userGrantPermission(activity: Activity?, callback: () -> Unit) {
        Dexter.withActivity(activity)
                .withPermissions(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .withListener(object : MultiplePermissionsListener {
                    override fun onPermissionsChecked(report: MultiplePermissionsReport) {
                        if (report.isAnyPermissionPermanentlyDenied) {
                            return
                        }

                        callback.invoke()
                    }

                    override fun onPermissionRationaleShouldBeShown(permissions: List<PermissionRequest>, token: PermissionToken) {
                        token.continuePermissionRequest()
                    }
                }).check()
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_loan_guidline)
        initViewModel()
        initInstances()

    }
    private fun initInstances() {

        viewModel.getData(data_extra)

        btn_next_confirm.setOnClickListener{
             LoanUploadDocumentActivity.start(this@GuidlineRequireDocActivity , data_extra ,"")
        }
    }
    private fun initViewModel() {

        viewModel.whenDataLoaded.observe(this, Observer {
            it?.let {
                setupDataIntoViews(it)
            }
        })
    }

    private fun setupDataIntoViews(it: GuidlineRequireDocViewModel.Model?) {
        recycler_view.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = GuildlineRequireAdapter(it?.items ?: listOf())
        }
    }

    companion object {
        const val DATA_EXTRA = "DATA_EXTRA"

        fun start(context: Context?, item: String) {
            val intent = Intent(context, GuidlineRequireDocActivity::class.java)
            intent.putExtra(DATA_EXTRA, item)
            context?.startActivity(intent)
        }


    }
}