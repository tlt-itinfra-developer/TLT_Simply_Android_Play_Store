package tlt.th.co.toyotaleasing.common.lifecycleobserver

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import org.greenrobot.eventbus.Subscribe
import tlt.th.co.toyotaleasing.R
import tlt.th.co.toyotaleasing.common.base.BaseActivity
import tlt.th.co.toyotaleasing.common.eventbus.*
import tlt.th.co.toyotaleasing.manager.BusManager
import tlt.th.co.toyotaleasing.manager.ContextManager
import tlt.th.co.toyotaleasing.modules.pincode.AuthPincodeActivity

class DeviceLogonLifeCycleObserver(private val activity: AppCompatActivity) : LifecycleObserver {

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun onResume() {
        BusManager.subscribe(this)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun onStop() {
        BusManager.unsubscribe(this)
    }

    @Subscribe
    fun onDeviceLogonReceived(event: DeviceLogonEvent) {
        dismissLoadingScreenDialogFragment()
        if (Screens.nonCustomer.contains(activity::class.java.simpleName)) {
            return
        }

        AlertDialog.Builder(activity)
                .setCancelable(false)
                .setTitle(ContextManager.getInstance().getApplicationContext().resources.getString(R.string.alert_device_logon_title))
                .setMessage(ContextManager.getInstance().getApplicationContext().resources.getString(R.string.alert_device_logon_description))
                .setPositiveButton(ContextManager.getInstance().getApplicationContext().resources.getString(R.string.alert_device_logon_button)) { dialog, id ->
                    AuthPincodeActivity.startWithClearStack(activity)
                }
                .show()
    }

    @Subscribe
    fun onSocketTimeoutReceived(event: SocketTimeoutEvent) {
        dismissLoadingScreenDialogFragment()

        AlertDialog.Builder(activity)
                .setCancelable(true)
                .setTitle("Error")
                .setMessage("The request timed out.")
                .setPositiveButton("OK") { dialog, id ->
                   dialog!!.dismiss()
                }
                .show()
    }

    @Subscribe
    fun onServerUnavailableReceived(event: ServerUnAvailableEvent) {
        dismissLoadingScreenDialogFragment()

        AlertDialog.Builder(activity)
                .setCancelable(false)
                .setTitle(ContextManager.getInstance().getStringByRes(R.string.server_unavailable_title))
                .setMessage(ContextManager.getInstance().getStringByRes(R.string.server_unavailable_description))
                .setPositiveButton(ContextManager.getInstance().getStringByRes(R.string.dialog_button_ok)) { dialog, which ->
                   dialog!!.dismiss()
                }
                .show()
    }

    @Subscribe
    fun onDeviceInvalidReceived(event: DeviceInvalidEvent) {
        dismissLoadingScreenDialogFragment()
        if (Screens.nonCustomer.contains(activity::class.java.simpleName)) {
            return
        }
        AlertDialog.Builder(activity)
                .setCancelable(false)
                .setTitle(ContextManager.getInstance().getApplicationContext().resources.getString(R.string.alert_device_invalid_title))
                .setMessage(ContextManager.getInstance().getApplicationContext().resources.getString(R.string.alert_device_invalid_description))
                .setPositiveButton(ContextManager.getInstance().getApplicationContext().resources.getString(R.string.alert_device_invalid_button)) { dialog, id ->
                    AuthPincodeActivity.startWithClearStack(activity)
                }
                .show()
    }

    @Subscribe
    fun onDeviceErrorReceived(event: DeviceErrorEvent) {
        dismissLoadingScreenDialogFragment()
        if (Screens.nonCustomer.contains(activity::class.java.simpleName)) {
            return
        }

        AlertDialog.Builder(activity)
                .setCancelable(false)
                .setTitle(ContextManager.getInstance().getApplicationContext().resources.getString(R.string.alert_device_error_title))
                .setMessage(ContextManager.getInstance().getApplicationContext().resources.getString(R.string.alert_device_error_description))
                .setPositiveButton(ContextManager.getInstance().getApplicationContext().resources.getString(R.string.alert_device_error_button)) { dialog, id ->
                    AuthPincodeActivity.startWithClearStack(activity)
                }
                .show()
//        AuthPincodeActivity.startWithClearStack(activity)
    }

    private fun dismissLoadingScreenDialogFragment() {
        val fragmentTransaction = activity.supportFragmentManager.beginTransaction()
        val previous = activity.supportFragmentManager.findFragmentByTag(BaseActivity.LOADING_DIALOG_TAG_ACTIVITY)
        if (previous != null) {
            fragmentTransaction.remove(previous)
        }
        fragmentTransaction.commit()
    }
}