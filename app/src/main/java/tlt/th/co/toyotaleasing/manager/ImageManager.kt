//package tlt.th.co.toyotaleasing.manager
//
//import android.Manifest
//import android.app.Activity
//import android.content.Context
//import android.net.Uri
//import androidx.fragment.app.Fragment
//import androidx.core.content.ContextCompat
//import com.karumi.dexter.Dexter
//import com.karumi.dexter.MultiplePermissionsReport
//import com.karumi.dexter.PermissionToken
//import com.karumi.dexter.listener.PermissionRequest
//import com.karumi.dexter.listener.multi.MultiplePermissionsListener
//import tlt.th.co.toyotaleasing.R
//
//
//object ImageManager {
//
//    fun open(activity: Activity,
//             maxSelect : Int = 1,
//             selectImageUri : ArrayList<Uri> = arrayListOf()) {
//        userGrantPermission(activity) {
//        }
//    }
//
//    fun open(fragment: Fragment,
//             maxSelect : Int = 1,
//             selectImageUri : ArrayList<Uri> = arrayListOf()) {
//        userGrantPermission(fragment.activity) {
//        }
//    }
//
//
//    private fun userGrantPermission(activity: Activity?, callback: () -> Unit) {
//        Dexter.withActivity(activity)
//                .withPermissions(Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE)
//                .withListener(object : MultiplePermissionsListener {
//                    override fun onPermissionsChecked(report: MultiplePermissionsReport) {
//                        if (report.isAnyPermissionPermanentlyDenied) {
//                            return
//                        }
//
//                        callback.invoke()
//                    }
//
//                    override fun onPermissionRationaleShouldBeShown(permissions: List<PermissionRequest>, token: PermissionToken) {
//                        token.continuePermissionRequest()
//                    }
//                }).check()
//    }
//
//    private fun getColorPrimaryDark(context: Context?): Int {
//        return ContextCompat.getColor(context!!, R.color.colorPrimaryDark)
//    }
//
//    private fun getWhiteColor(context: Context?): Int {
//        return ContextCompat.getColor(context!!, android.R.color.white)
//    }
//}