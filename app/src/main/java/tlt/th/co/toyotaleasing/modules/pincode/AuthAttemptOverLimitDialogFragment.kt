package tlt.th.co.toyotaleasing.modules.pincode

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.FragmentManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import kotlinx.android.synthetic.main.fragment_dialog_refinance.*
import tlt.th.co.toyotaleasing.R
import tlt.th.co.toyotaleasing.common.base.BaseDialogFragment

class AuthAttemptOverLimitDialogFragment : BaseDialogFragment() {

    private lateinit var listener: Listener

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
       dialog!!.window!!.requestFeature(Window.FEATURE_NO_TITLE)
       dialog!!.window!!.setBackgroundDrawableResource(android.R.color.transparent)
       dialog!!.setCancelable(false)
        return dialog
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_dialog_auth_attempt_over_limit, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initInstances()
    }

    private fun initInstances() {
        btn_confirm.setOnClickListener {
            fragmentManager?.let {
                dismiss()
                listener.onAuthAttemptOverLimitConfirmClicked()
            }
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        listener = context as Listener
    }

    override fun onResume() {
        super.onResume()
       dialog!!.window!!.setLayout(resources.displayMetrics.widthPixels, resources.displayMetrics.heightPixels)
    }

    interface Listener {
        fun onAuthAttemptOverLimitConfirmClicked()
    }

    companion object {
        val TAG = this::class.java.simpleName

        fun newInstance() = AuthAttemptOverLimitDialogFragment()

        fun show(fragmentManager: FragmentManager) {
            newInstance().show(fragmentManager!!, TAG)
        }
    }
}