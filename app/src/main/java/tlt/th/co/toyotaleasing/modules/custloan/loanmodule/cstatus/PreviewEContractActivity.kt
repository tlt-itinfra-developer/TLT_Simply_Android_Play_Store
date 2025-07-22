package tlt.th.co.toyotaleasing.modules.custloan.loanmodule.cstatus

import android.Manifest
import android.app.Activity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import tlt.th.co.toyotaleasing.R
import tlt.th.co.toyotaleasing.common.base.BaseActivity
import tlt.th.co.toyotaleasing.util.PdfUtils
import kotlinx.android.synthetic.main.activity_preview_e_contract.*
import tlt.th.co.toyotaleasing.modules.custloan.loanmodule.basicdetail.common.DopaStatusDialog


class PreviewEContractActivity : BaseActivity()  ,  DopaStatusDialog.Listener    {

    override fun onDialogConfirmClick() {
        LoanAuthPincodeActivity.startWithResultOnline(this@PreviewEContractActivity , data_extra)
    }

    override fun onDialogCancelClick() {

    }

    private val viewModel by lazy {
        ViewModelProviders.of(this).get(PreviewEContractViewModel::class.java)
    }

    private val data_extra by lazy {
        intent?.getStringExtra(REF_ID) ?: ""
    }


    private val pdf_name by lazy {
        intent?.getStringExtra(PDF_NAME) ?: ""
    }

    private val pdf_file by lazy {
        intent?.getStringExtra(PDF_BASE64) ?: ""
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_preview_e_contract)
        initViewModel()
        initInstances()

        userGrantPermission(this@PreviewEContractActivity){
            viewModel.showPDF( data_extra)
        }
    }


    private fun initViewModel() {
        viewModel.whenLoading.observe(this, Observer {
            toggleLoadingScreenDialog(it!!)
        })

        viewModel.whenOpenPdf.observe(this, Observer {
            val uri =  PdfUtils.getPdfUri(this@PreviewEContractActivity, it)

        })

        viewModel.whenDataLoadedFailure.observe(this, Observer {
            Toast.makeText(this, getString(R.string.txt_problem_open_pdf), Toast.LENGTH_SHORT).show()
            SummaryLoanActivity.start(this , data_extra , "")
        })

    }

    private fun initInstances() {

        txt_ref_value.text = data_extra


        btn_cancel.setOnClickListener {
            viewModel.SyncCancelEContract(data_extra)
        }

        btn_next_confirm.setOnClickListener {
            showConfirmPopup()
        }

    }

    fun  showConfirmPopup(){
        try{
            DopaStatusDialog.show(
                    fragmentManager = supportFragmentManager,
                    description = getString(R.string.header_txt_e_contract_confirm) ,
                    confirmButtonMessage = getString(R.string.btn_confirm) ,
                    cancelButtonMessage = getString(R.string.dialog_button_cancel)
            )

        }catch (e : Exception){
            e.message
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
        const val PDF_NAME = "PDF_NAME"
        const val PDF_BASE64 = "PDF_BASE64"

        fun start(activity: Activity?, data: String , url : String ) {
            val intent = Intent(activity, PreviewEContractActivity::class.java)
            intent.putExtra(REF_ID, data)
            intent.putExtra(REF_URL, url)
            activity!!.startActivity(intent)
        }

        fun open(activity: Activity?, data: String ) {
            val intent = Intent(activity, PreviewEContractActivity::class.java)
            intent.putExtra(REF_ID, data)
            activity!!.startActivity(intent)
        }
    }
}
