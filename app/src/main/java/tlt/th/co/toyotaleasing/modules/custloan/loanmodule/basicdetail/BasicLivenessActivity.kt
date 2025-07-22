package tlt.th.co.toyotaleasing.modules.custloan.loanmodule.basicdetail


import android.Manifest
import android.app.Activity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import androidx.core.content.ContextCompat
import android.util.Base64
import android.util.Log
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener

import kotlinx.android.synthetic.main.fragment_loan_require_liveness.*
import kotlinx.android.synthetic.main.loan_require_upper_sheet.view.*
import tlt.th.co.toyotaleasing.R
import tlt.th.co.toyotaleasing.analytics.AnalyticsManager
import tlt.th.co.toyotaleasing.analytics.AnalyticsScreenName
import tlt.th.co.toyotaleasing.common.base.BaseActivity
import tlt.th.co.toyotaleasing.common.dialog.NormalDialogFragment
import tlt.th.co.toyotaleasing.common.extension.gone
import tlt.th.co.toyotaleasing.common.extension.visible
import tlt.th.co.toyotaleasing.modules.custloan.MenuStepController
import tlt.th.co.toyotaleasing.modules.custloan.loanmodule.center.CarLoanFragment
import java.io.ByteArrayOutputStream


class BasicLivenessActivity   : BaseActivity() , NormalDialogFragment.Listener{

    private var imageBitmap: Bitmap? = null

    private var RESULT_LOAD_IMG : Int  = 2

    private val data_extra by lazy {
        intent?.getStringExtra(REF_ID)?:""
    }

    private val data_url by lazy {
        intent?.getStringExtra(REF_URL)?:""
    }

    private val viewModel by lazy {
        ViewModelProviders.of(this).get(BasicLivenessViewModel::class.java)
    }

    private val PERMISSION_CODE = 1000;
    private val IMAGE_CAPTURE_CODE = 1001
    var image_uri: Uri? = null

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
        setContentView(R.layout.fragment_loan_require_liveness)
        AnalyticsManager.trackScreen(AnalyticsScreenName.ONLINE_LIVENESS)
        initInstance()
        initViewModel()

    }


    fun  initInstance() {
        map_online_require_upper_sheet.stepLiveness.background = ContextCompat.getDrawable(this, R.drawable.step_liveness_active)
        map_online_require_upper_sheet.step2.setTextColor(ContextCompat.getColor(this@BasicLivenessActivity, R.color.cherry_red))
        map_online_require_upper_sheet.txt_ref_value.text = data_extra

        img.visible()
        fab_capture.setOnClickListener {
            userGrantPermission(this@BasicLivenessActivity) {
                //               val intent = Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE)
//                startActivityForResult(intent, 10)
                openCamera()

            }
        }

        btn_next_confirm.setOnClickListener{
            try{
                toggleLoadingScreenDialog(true)
                var imgStr = Base64.encodeToString(compress(this!!.imageBitmap!!), Base64.DEFAULT)
                toggleLoadingScreenDialog(true)
                viewModel.SyncLiveness(imgStr , data_extra)
            }catch ( e : Exception){
                toggleLoadingScreenDialog(false)
                e.printStackTrace()
            }
        }

        btn_back.setOnClickListener{
            try{
                CarLoanFragment.startByInsight(
                        context = this@BasicLivenessActivity, authen = "Y"
                )
            }catch ( e : Exception){
                e.printStackTrace()
            }
        }
    }



    fun openCamera() {
        val values = ContentValues()
        values.put(MediaStore.Images.Media.TITLE, "idcard")
        values.put(MediaStore.Images.Media.DESCRIPTION, "From the Camera")
        image_uri = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
        //camera intent
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        intent.putExtra("android.intent.extras.CAMERA_FACING", 1);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, image_uri)
        startActivityForResult(cameraIntent, IMAGE_CAPTURE_CODE)
    }


    fun  initViewModel() {

        viewModel.whenSyncSuccessData.observe(this, Observer {
            it?.let {
                MenuStepController.open(this, it.ref_id, it.step, "")
            }
        })
    }

    private fun compress(bitmap: Bitmap, quality: Int = 100): ByteArray {
        val byteArrayOutputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, quality, byteArrayOutputStream)
        return byteArrayOutputStream.toByteArray()
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

//        imageBitmap = data!!.extras!!["data"] as Bitmap
//        camera_btn.setImageBitmap(imageBitmap)
//
        imageBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), image_uri)
        imageBitmap = Bitmap.createScaledBitmap(imageBitmap!!, 800, 600, true);
        camera_btn.setImageURI(image_uri)
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.N ) {
            camera_btn.rotation = 90f
        }
        img.gone()
        formValidate()
        AnalyticsManager.onlineLivenessAdd()
    }

    private fun formValidate() {
        if (imageBitmap == null){
            btn_next_confirm.isEnabled = false
            return
        }
        btn_next_confirm.isEnabled = true
        camera_btn.setImageURI(image_uri)
    }




    override fun onDialogConfirmClick() {
        setResult(Activity.RESULT_OK)
    }
    override fun onDialogCancelClick() {

    }

    companion object {

        const val REF_ID = "REF_ID"
        const val REF_URL = "REF_URL"

        fun start(activity: Activity?, data: String , url : String) {
            val intent = Intent(activity, BasicLivenessActivity::class.java)
            intent.putExtra(REF_ID, data)
            intent.putExtra(REF_URL, url)
            activity!!.startActivity(intent)
        }

    }
}
