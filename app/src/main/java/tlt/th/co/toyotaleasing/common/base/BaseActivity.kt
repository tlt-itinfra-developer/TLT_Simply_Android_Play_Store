package tlt.th.co.toyotaleasing.common.base

import android.annotation.SuppressLint
import android.app.Activity
import android.app.ProgressDialog
import androidx.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.annotation.StringRes
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
//import me.yokeyword.fragmentation.SupportActivity
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import tlt.th.co.toyotaleasing.R
import tlt.th.co.toyotaleasing.analytics.AnalyticsManager
import tlt.th.co.toyotaleasing.common.eventbus.SendAnalyticsEvent
import tlt.th.co.toyotaleasing.common.extension.ifFalse
import tlt.th.co.toyotaleasing.common.extension.ifTrue
import tlt.th.co.toyotaleasing.common.lifecycleobserver.*
import tlt.th.co.toyotaleasing.manager.LocalizeManager
import java.util.*


@SuppressLint("Registered")
open class BaseActivity : AppCompatActivity() {

    private lateinit var baseLoadingScreenDialogFragment: BaseLoadingScreenDialogFragment

    private val viewModel by lazy {
        ViewModelProviders.of(this).get(BaseViewModel::class.java)
    }

    private var savedInstanceState: Bundle? = null
    private lateinit var progressDialog: ProgressDialog
    private val localizeLifeCycleObserver = LocalizeLifeCycleObserver(this)
    private val pushPopupLifeCycleObserver = PushPopupLifeCycleObserver(this)
    private val reAuthLifeCycleObserver = ReAuthLifeCycleObserver(this)
    private val deviceLogonLifeCycleObserver = DeviceLogonLifeCycleObserver(this)
    private val reOpenLifeCycleObserver = ResumingAppLifeCycleObserver(this)
    private val noInternetLifeCycleObserver = NoInternetLifeCycleobserver(this)

    override fun onResume() {
        super.onResume()
        AnalyticsManager.startTime = Calendar.getInstance().time
    }

    override fun onPause() {
        super.onPause()
        AnalyticsManager.endTime = Calendar.getInstance().time
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        this.savedInstanceState = savedInstanceState

        lifecycle.addObserver(localizeLifeCycleObserver)
        lifecycle.addObserver(pushPopupLifeCycleObserver)
        lifecycle.addObserver(deviceLogonLifeCycleObserver)
        lifecycle.addObserver(reAuthLifeCycleObserver)
        lifecycle.addObserver(reOpenLifeCycleObserver)
        lifecycle.addObserver(noInternetLifeCycleObserver)
    }

    override fun onDestroy() {
        lifecycle.removeObserver(localizeLifeCycleObserver)
        lifecycle.removeObserver(pushPopupLifeCycleObserver)
        lifecycle.removeObserver(deviceLogonLifeCycleObserver)
        lifecycle.removeObserver(reAuthLifeCycleObserver)
        lifecycle.removeObserver(reOpenLifeCycleObserver)
        lifecycle.removeObserver(noInternetLifeCycleObserver)

        super.onDestroy()
    }

    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(LocalizeManager.initDefaultLocalize(base))
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
    }

    fun isSavedInstanceStateNotNull() = savedInstanceState != null

    fun toggleLoadingScreenDialog(isEnable: Boolean? = false) {
        if (isEnable!!) {
            showLoadingScreenDialogFragment(BaseLoadingScreenDialogFragment.newInstance())
        } else {
            dismissLoadingScreenDialogFragment()
        }
    }

    fun toggleLoading(isEnable: Boolean? = false,
                      @StringRes description: Int = R.string.loading) {
        if (!this::progressDialog.isInitialized) {
            progressDialog = ProgressDialog(this)
            progressDialog.setMessage(getString(description))
            progressDialog.setCancelable(false)
        }

        isEnable?.ifTrue { progressDialog.show() }
        isEnable?.ifFalse { progressDialog.dismiss() }
    }

    private fun showLoadingScreenDialogFragment(dialogFragment: BaseLoadingScreenDialogFragment) {
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        val previous = supportFragmentManager.findFragmentByTag(Companion.LOADING_DIALOG_TAG_ACTIVITY)
        if (previous != null) {
            fragmentTransaction.remove(previous)
        }
        dialogFragment.show(fragmentTransaction, Companion.LOADING_DIALOG_TAG_ACTIVITY)
    }

    private fun dismissLoadingScreenDialogFragment() {
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        val previous = supportFragmentManager.findFragmentByTag(Companion.LOADING_DIALOG_TAG_ACTIVITY)
        if (previous != null) {
            fragmentTransaction.remove(previous)
        }
        fragmentTransaction.commit()
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun sendAnalyticsToServer(event: SendAnalyticsEvent) {
        viewModel.LogPrintViewModel()
    }

    fun hideKeyboard() {
        val imm = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0)
    }

    companion object {
        const val LOADING_DIALOG_TAG_ACTIVITY = "BaseLoadingScreenDialogFragmentActivity"
    }
}