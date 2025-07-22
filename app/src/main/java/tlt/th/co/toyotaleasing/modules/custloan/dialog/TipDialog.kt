package tlt.th.co.toyotaleasing.modules.custloan.dialog

import android.app.DialogFragment
import android.content.DialogInterface
import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.ImageView
import tlt.th.co.toyotaleasing.R


/**
 * Created by zhanqiang545 on 18/6/22.
 */

class TipDialog : DialogFragment(), DialogInterface.OnKeyListener, View.OnClickListener {

    private var ivX: ImageView? = null


    private var onCloseClickListener: OnCloseClickListener? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle): View? {
        dialog!!.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog!!.setCanceledOnTouchOutside(false)// Set the click screen Dialog does not disappear
        dialog!!.setOnKeyListener(this)
        val mDialog = inflater.inflate(R.layout.dialog_loan_tip, container)
        ivX = mDialog.findViewById(R.id.iv_x)
        initEvent()
        return mDialog
    }

    private fun initEvent() {
        ivX!!.setOnClickListener(this)
    }

    override fun onKey(dialog: DialogInterface, keyCode: Int, event: KeyEvent): Boolean {
        return if (keyCode == KeyEvent.KEYCODE_BACK) {
            true
        } else false
    }

    override fun onClick(v: View) {
        setOnCloseClickListener(onCloseClickListener)
        if (onCloseClickListener != null) {
            onCloseClickListener!!.onCloseClick()
            dismiss()
        }
    }

    interface OnCloseClickListener {
        fun onCloseClick()
    }

    fun setOnCloseClickListener(listener: OnCloseClickListener?) {
        onCloseClickListener = listener
    }

}
