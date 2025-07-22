package tlt.th.co.toyotaleasing.modules.custloan.loanmodule.cstatus

import android.Manifest
import android.app.Activity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import tlt.th.co.toyotaleasing.R
import tlt.th.co.toyotaleasing.common.base.BaseActivity
import tlt.th.co.toyotaleasing.util.PdfUtils
import kotlinx.android.synthetic.main.activity_preview_e_contract.*
import kotlinx.android.synthetic.main.activity_preview_e_contract.pdfView
import kotlinx.android.synthetic.main.activity_preview_e_contract.txt_ref_value
import kotlinx.android.synthetic.main.activity_preview_pdf.*
import tlt.th.co.toyotaleasing.modules.custloan.MenuStepController


class ViewEContractActivity : BaseActivity() {

    private val viewModel by lazy {
        ViewModelProviders.of(this).get(ViewEContractViewModel::class.java)
    }

    private val data_extra by lazy {
        intent?.getStringExtra(REF_ID) ?: ""
    }


    private val type by lazy {
        intent?.getStringExtra(TYPE) ?: ""
    }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_preview_pdf)
        initViewModel()
        initInstances()


        userGrantPermission(this@ViewEContractActivity){
//            viewModel.showPDF( pdf_name , pdf_file)
            viewModel.getEContract(data_extra , type)
        }
    }


    private fun initViewModel() {
        viewModel.whenLoading.observe(this, Observer {
            toggleLoadingScreenDialog(it!!)
        })

        viewModel.whenOpenPdf.observe(this, Observer {
            val uri =  PdfUtils.getPdfUri(this@ViewEContractActivity, it)

        })///storage/emulated/0/tlt_pdf/REF202000001397201120.pdf.pdf

        viewModel.whenSyncSuccessData.observe(this, Observer {
            it?.let{
                MenuStepController.open(this@ViewEContractActivity, it.ref_id , it.step  , it.ref_url)
            }
        })

    }

    private fun initInstances() {

        txt_ref_value.text = data_extra

        btn_back.setOnClickListener {
            CompleteCarLoanActivity.start(this@ViewEContractActivity , data_extra , "")
        }

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

    companion object {
        const val REF_ID = "REF_ID"
        const val REF_URL = "REF_URL"
        const val TYPE = "TYPE"

        fun start(activity: Activity?, data: String , url : String ) {
            val intent = Intent(activity, ViewEContractActivity::class.java)
            intent.putExtra(REF_ID, data)
            intent.putExtra(REF_URL, url)
            activity!!.startActivity(intent)
        }

        fun open(activity: Activity?, data: String , type: String  ) {
            val intent = Intent(activity, ViewEContractActivity::class.java)
            intent.putExtra(REF_ID, data)
            intent.putExtra(TYPE, type)
            activity!!.startActivity(intent)
        }
    }
}
