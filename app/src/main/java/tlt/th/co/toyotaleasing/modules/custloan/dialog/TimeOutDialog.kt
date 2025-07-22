package tlt.th.co.toyotaleasing.modules.custloan.dialog

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.FragmentManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import kotlinx.android.synthetic.main.dialog_loan_time_out.*
import tlt.th.co.toyotaleasing.R
import tlt.th.co.toyotaleasing.common.base.BaseDialogFragment

@SuppressLint("ValidFragment")
class TimeOutDialog() : BaseDialogFragment() {

    private var listenerActivity: TimeOutDialog.Listener? = null
    private val listenerFragment by lazy {
        targetFragment?.let { it as TimeOutDialog.Listener }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog!!.window!!.requestFeature(Window.FEATURE_NO_TITLE)
        dialog!!.window!!.setBackgroundDrawableResource(android.R.color.transparent)
        return dialog
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.dialog_loan_time_out, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (intData == 1) {
            tv_tip.text = "Illegal attack"//Non-living
        } else if (intData == 0) {
            tv_tip.text = "Operation timeout"//time out
        }

        initEvent()


    }

    private fun initEvent() {

        tv_re_start.setOnClickListener {
            fragmentManager?.let { dismiss() }
            listenerActivity?.onReDetectListenerm()
            listenerFragment?.onReDetectListenerm()
        }

        tv_exit.setOnClickListener {
            fragmentManager?.let { dismiss() }
            listenerActivity?.onExitClickListener()
            listenerFragment?.onExitClickListener()
        }
    }


    override fun onResume() {
        super.onResume()
        dialog!!.window!!.setLayout(resources.displayMetrics.widthPixels, resources.displayMetrics.heightPixels)
    }



    override fun onAttach(context: Context) {
        super.onAttach(context)

        try {
            listenerActivity = context as Listener
        } catch (e: ClassCastException) {
            e.printStackTrace()
        }
    }

    interface Listener {
        fun onReDetectListenerm()
        fun onExitClickListener()
    }

    companion object {
        val TAG = this::class.java.simpleName
        var intData : Int = 0

        fun newInstance() = TimeOutDialog()

        fun show(fragmentManager: FragmentManager, tag : Int) {
            newInstance().show(fragmentManager!!, TAG)
            intData = tag
        }
    }
}
