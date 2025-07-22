package tlt.th.co.toyotaleasing.modules.editaddress.installment

import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import kotlinx.android.synthetic.main.fragment_dialog_edit_installment_email_success.*
import tlt.th.co.toyotaleasing.R
import tlt.th.co.toyotaleasing.common.base.BaseDialogFragment

class EditInstallmentEmailSuccessDialogFragment : BaseDialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
       dialog!!.window!!.requestFeature(Window.FEATURE_NO_TITLE)
       dialog!!.window!!.setBackgroundDrawableResource(android.R.color.transparent)
        return dialog
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_dialog_edit_installment_email_success, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        btn_confirm.setOnClickListener {
            fragmentManager?.let { dismiss() }
            (targetFragment as Listener).onEditInstallmentEmailSuccessConfirmClick()
        }
    }

    override fun onResume() {
        super.onResume()
       dialog!!.window!!.setLayout(resources.displayMetrics.widthPixels, resources.displayMetrics.heightPixels)
    }

    interface Listener {
        fun onEditInstallmentEmailSuccessConfirmClick()
    }

    companion object {
        val TAG = "EditInstallmentEmailSuccessDialogFragment"

        fun newInstance() = EditInstallmentEmailSuccessDialogFragment()

        fun show(fragmentManager: FragmentManager, parentFragment: Fragment) {
            newInstance().apply {
                setTargetFragment(parentFragment, 1)
                show(fragmentManager, TAG)
            }
        }
    }
}