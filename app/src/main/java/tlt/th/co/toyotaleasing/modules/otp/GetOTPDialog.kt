package tlt.th.co.toyotaleasing.modules.otp

import android.content.Context
import androidx.appcompat.app.AlertDialog
import tlt.th.co.toyotaleasing.R

class GetOTPDialog {

    companion object {
        fun show(context: Context) {
            AlertDialog.Builder(context)
                    .setMessage(R.string.otp_dialog_description)
                    .setPositiveButton(R.string.otp_dialog_btn_confirm) { dialog, _ ->
                        OTPActivity.open(context)
                       dialog!!.dismiss()
                    }
                    .setNegativeButton(R.string.otp_dialog_btn_cancel) { dialog, _ ->
                       dialog!!.dismiss()
                    }
                    .show()
        }
    }
}