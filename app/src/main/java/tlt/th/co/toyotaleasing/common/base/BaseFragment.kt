package tlt.th.co.toyotaleasing.common.base

import android.app.ProgressDialog
import androidx.fragment.app.Fragment
import tlt.th.co.toyotaleasing.analytics.AnalyticsManager
import tlt.th.co.toyotaleasing.common.extension.ifFalse
import tlt.th.co.toyotaleasing.common.extension.ifTrue
import java.util.*

open class BaseFragment : Fragment() {

    private lateinit var baseLoadingScreenDialogFragment: BaseLoadingScreenDialogFragment

    var progressDialog: ProgressDialog? = null

    fun toggleLoadingScreenDialog(isEnable: Boolean? = false, fragment: Fragment?) {
        if (isEnable!!) {
            showLoadingScreenDialogFragment(
                    BaseLoadingScreenDialogFragment.newInstance(),
                    fragment
            )
        } else {
            dismissLoadingScreenDialogFragment()
        }
    }

    private fun showLoadingScreenDialogFragment(dialogFragment: BaseLoadingScreenDialogFragment,
                                                fragment: Fragment?) {
        val fragmentTransaction = fragmentManager!!.beginTransaction()
        val previous = fragmentManager!!.findFragmentByTag(LOADING_DIALOG_TAG_FRAGMENT)
        if (previous != null) {
            fragmentTransaction.remove(previous)
        }
        dialogFragment.show(fragmentTransaction, LOADING_DIALOG_TAG_FRAGMENT)
        dialogFragment.setTargetFragment(fragment, 1)
    }

    private fun dismissLoadingScreenDialogFragment() {
        val fragmentTransaction = fragmentManager!!.beginTransaction()
        val previous = fragmentManager!!.findFragmentByTag(LOADING_DIALOG_TAG_FRAGMENT)
        if (previous != null) {
            fragmentTransaction.remove(previous)
        }
        fragmentTransaction.commit()
    }

    fun toggleLoading(isEnable: Boolean? = false) {
        if (progressDialog == null) {
            progressDialog = ProgressDialog(context)
            progressDialog!!.setMessage("Loading")
            progressDialog!!.setCancelable(false)
        }

        isEnable?.ifTrue { progressDialog?.show() }
        isEnable?.ifFalse { progressDialog?.dismiss() }
    }

    companion object {
        const val LOADING_DIALOG_TAG_FRAGMENT = "BaseLoadingScreenDialogFragment"
    }
}