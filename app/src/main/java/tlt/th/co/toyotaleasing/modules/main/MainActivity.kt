package tlt.th.co.toyotaleasing.modules.main

import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import android.view.LayoutInflater
import android.view.View
import android.view.Window
import android.view.WindowManager
import com.scottyab.rootbeer.RootBeer
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_showmaster_dialog.view.*
import org.greenrobot.eventbus.Subscribe
import tlt.th.co.toyotaleasing.R
import tlt.th.co.toyotaleasing.common.base.BaseActivity
import tlt.th.co.toyotaleasing.common.eventbus.FCMTokenRefreshedEvent
import tlt.th.co.toyotaleasing.manager.BusManager
import tlt.th.co.toyotaleasing.manager.ContextManager
import tlt.th.co.toyotaleasing.manager.DeeplinkManager

import tlt.th.co.toyotaleasing.manager.db.UserManager
import tlt.th.co.toyotaleasing.modules.appintro.AppIntroActivity
import tlt.th.co.toyotaleasing.modules.appintro.IntroPAPDActivity
import tlt.th.co.toyotaleasing.modules.home.customer.MainCustomerActivity
import tlt.th.co.toyotaleasing.modules.home.noncustomer.MainNonCustomerActivity
import tlt.th.co.toyotaleasing.modules.pincode.AuthPincodeActivity
import tlt.th.co.toyotaleasing.modules.staff.termcon.StaffTermAndConditionActivity
import tlt.th.co.toyotaleasing.util.AppUtils

class MainActivity : BaseActivity() {

    private val viewModel by lazy {
        ViewModelProviders.of(this).get(MainViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        checkRooted()
    }

    private fun checkRooted() {
        val rootbeer = RootBeer(this)
        if (rootbeer.isRootedWithoutBusyBoxCheck) {
            AlertDialog.Builder(this)
                    .setCancelable(false)
                    .setTitle(ContextManager.getInstance().getApplicationContext().resources.getString(R.string.alert_dialog_root_title))
                    .setMessage(ContextManager.getInstance().getApplicationContext().resources.getString(R.string.alert_dialog_root_description))
                    .setPositiveButton(ContextManager.getInstance().getApplicationContext().resources.getString(R.string.dialog_button_ok)) { dialog, id ->
                       dialog!!.dismiss()
                        checkRooted()
                    }
                    .show()
        } else {
            hideNavigationBar()

            DeeplinkManager.getInstance()
                    .cacheDeeplinkDataIfComing(this)

            initViewModel()
            viewModel.getIntialByApi()
        }
    }

    private fun initViewModel() {



        viewModel.whenLoading.observe(this, Observer {

        })

        viewModel.whenMasterSizeDataLoad.observe(this, Observer {
            showSizeMasterData(it!!.dDescription)
        })


        viewModel.whenUserIsNotCustomer.observe(this, Observer { isShowAppIntro ->



//            if (isShowAppIntro!!) {
//
//                AppIntroActivity.open(this)
//            }


            if(UserManager.getInstance().isShowAppPAPD()!!) {
                IntroPAPDActivity.open(this)
            }else {
                if (isShowAppIntro!!)
                    AppIntroActivity.open(this)
            }

            finish()
        })


        viewModel.whenUserIsCustomer.observe(this, Observer {
           // MainCustomerActivity.start(this)
            AuthPincodeActivity.startWithResult(this)
            finish()
        })

        viewModel.whenNoInternetConnection.observe(this, Observer {
            if (it!!) {
                if (UserManager.getInstance().isCustomer()) {
                   // MainCustomerActivity.start(this)
                    AuthPincodeActivity.startWithResult(this)
                    finish()
                } else {
                    MainNonCustomerActivity.open(this)

//                    if (UserManager.getInstance().isShowAppIntro()) {
//                        AppIntroActivity.open(this)
//                    }


                    if(UserManager.getInstance().isShowAppPAPD())
                        IntroPAPDActivity.open(this)
                    else if (UserManager.getInstance().isShowAppIntro()) {
                        AppIntroActivity.open(this)
                    }

                    finish()
                }
            }
        })
    }



    private fun hideStatusBar() {
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
    }

    private fun hideNavigationBar() {
        val decorView = window.decorView
        val uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_FULLSCREEN
        decorView.systemUiVisibility = uiOptions
    }

    private fun showSizeMasterData(desc : String) {
        try {

            text_master_size.text = desc

        } catch (e: Exception) {
            e.message
        }
    }

    @Subscribe
    fun onFCMTokenRefreshed(event: FCMTokenRefreshedEvent) {
        viewModel.getIntialByApi()
    }

    override fun onStart() {
        super.onStart()
        BusManager.subscribe(this)
    }

    override fun onStop() {
        BusManager.unsubscribe(this)
        super.onStop()
    }

    companion object {

        fun openWithClearStack(context: Context) {
            val intent = Intent(context, MainActivity::class.java)
            intent!!.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            context.startActivity(intent)
        }
    }
}
