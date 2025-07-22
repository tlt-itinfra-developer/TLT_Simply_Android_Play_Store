package tlt.th.co.toyotaleasing.modules.noncustomer

import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.appcompat.app.AlertDialog
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import kotlinx.android.synthetic.main.fragment_dialog_announcement.view.*
import kotlinx.android.synthetic.main.fragment_register.*
import tlt.th.co.toyotaleasing.R
import tlt.th.co.toyotaleasing.analytics.AnalyticsManager
import tlt.th.co.toyotaleasing.analytics.AnalyticsScreenName
import tlt.th.co.toyotaleasing.common.base.BaseFragment
import tlt.th.co.toyotaleasing.common.extension.*
import tlt.th.co.toyotaleasing.common.listener.OnHambergerClickListener
import tlt.th.co.toyotaleasing.manager.ContextManager
import tlt.th.co.toyotaleasing.manager.LocalizeManager

import tlt.th.co.toyotaleasing.manager.social.SocialCallback
import tlt.th.co.toyotaleasing.manager.social.SocialManager
import tlt.th.co.toyotaleasing.manager.social.SocialProfile
import tlt.th.co.toyotaleasing.model.response.NotificationJsonResponse
import tlt.th.co.toyotaleasing.modules.home.customer.NoticeDialogFragment
import tlt.th.co.toyotaleasing.modules.home.noncustomer.MainNonCustomerActivity
import tlt.th.co.toyotaleasing.modules.termcondition.TermAndConditionActivity
import tlt.th.co.toyotaleasing.util.ExternalAppUtils

class RegisterFragment : BaseFragment(), NoticeDialogFragment.Listener {

    private val viewModel by lazy {
        ViewModelProviders.of(this).get(RegisterViewModel::class.java)
    }

    private val socialManager by lazy {
        SocialManager.getInstance(socialCallback)
    }

    private val onHambergerClickListener by lazy {
        context as OnHambergerClickListener
    }

    private val notifyItem by lazy {
        activity?.intent
                ?.getBundleExtra(MainNonCustomerActivity.DATA_POSITION_EXTRA)
                ?.getParcelable(DATA_BY_DEEPLINK_DIALOG) ?: NotificationJsonResponse()
    }

    private val isShowButtonDialog by lazy {
        activity?.intent
                ?.getBundleExtra(MainNonCustomerActivity.DATA_POSITION_EXTRA)
                ?.getBoolean(IS_SHOW_BUTTON_IN_DIALOG) ?: false
    }

    private val isShowDialog by lazy {
        activity?.intent
                ?.getBundleExtra(MainNonCustomerActivity.DATA_POSITION_EXTRA)
                ?.getBoolean(IS_SHOW_DIALOG_FROM_DEEPLINK) ?: false
    }

    private var isForceUpdate: Boolean = false
    private var forceUpdateMessage: String = ""

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_register, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (LocalizeManager.isFirstTime()) {
            val languageDialog = ChooseLanguageDialogFragment()
            languageDialog.show(fragmentManager!!, TAG)
        }

        initViewModel()
        initInstances()

        viewModel.getBannerListFromApi()
        viewModel.getDefaultEmail()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        socialManager.onActivityResult(requestCode, resultCode, data!!)
        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun initViewModel() {
        viewModel.whenLoading.observe(this, Observer {
            toggleLoadingScreenDialog(it!!, this)
        })

        viewModel.whenGetBannerList.observe(this, Observer {
            widget_banner.setBanners(it!!)
            if (!LocalizeManager.isFirstTime()) {

                viewModel.checkApplicationStatus()
            }
        })

        viewModel.whenGetDefaultEmail.observe(this, Observer {
            input_register_email.setText(it)
        })

        viewModel.whenRegisterByEmail.observe(this, Observer { isError ->
            if (isError!!) {
                Toast.makeText(context, "ีไม่สามารถส่งการยืนยันไปยัง Email นี้ได้", Toast.LENGTH_SHORT).show()
                return@Observer
            }

            TermAndConditionActivity.open(context!!)
        })

        viewModel.whenRegisterBySocial.observe(this, Observer { isError ->
            if (isError!!) {
                Toast.makeText(context, "ไม่สามารถสมัครด้วย Social ได้", Toast.LENGTH_SHORT).show()
                return@Observer
            }

            TermAndConditionActivity.open(context!!)
        })

        viewModel.whenEmailInvalid.observe(this, Observer {
            textinput_email.enableErrorMessage(getString(R.string.error_email_pattern))
        })

        viewModel.whenCheckApplicationSuccess.observe(this, Observer {
            showPopupCheckApplication(it!!)
        })

        viewModel.whenCheckApplicationFail.observe(this, Observer {
            showToast(it!!)
        })


        viewModel.whenCallAnnounceSuccess.observe(this, Observer {
            it?.let {
                if(it.size>0)
                    for (item  in it) {
                        var  url : Uri   = Uri.parse(item.urlLink)
                        showAnnouncement(url , item.linkRef)
                    }
            }

        })

    }

    private fun showAnnouncement(uri : Uri, linkRef : String) {
        try {
            val mDialogView = LayoutInflater.from(context!!).inflate(R.layout.fragment_dialog_announcement, null)
            val mBuilder = AlertDialog.Builder(context!!,R.style.dialogBlur).setView(mDialogView)
            val mAlertDialog = mBuilder.show()
            mDialogView.img_announcement.loadImageByUri(uri)
            mDialogView.popup_btn_close.setOnClickListener {
                mAlertDialog.dismiss()
            }
            mDialogView.img_announcement.setOnClickListener {
                if(linkRef != "") {
                    ExternalAppUtils.openByLink(context!!, linkRef)
                    mAlertDialog.dismiss()
                }
            }
        } catch (e: Exception) {
            e.message
        }
    }

    private fun initInstances() {
        textinput_email.setOnClickListener {
            AnalyticsManager.homeEmailClicked(input_register_email.text.toString().trim())
        }

        toolbar.setOnHambergerMenuClickListener {
            AnalyticsManager.homeMenuHamburgerClicked()
            onHambergerClickListener.onHambergerClick()
        }

        btn_register_email.setOnClickListener {
            AnalyticsManager.homeRegisterEmailClicked()
            viewModel.whenEmailPressed(input_register_email.text.toString().trim())
        }

        btn_register_facebook.setOnClickListener {
            AnalyticsManager.homeFacebookClicked()
            socialManager.facebookLogin(this)
        }
        btn_register_google.setOnClickListener {
            AnalyticsManager.homeGooglePlusClicked()
            socialManager.googleLogin(this)
        }
        btn_register_line.setOnClickListener {
            AnalyticsManager.homeLineClicked()
            socialManager.lineLogin(this)
        }

        textinput_email.enableClearErrorWhenTextChanged()

        isShowDialog.ifTrue {
            NoticeDialogFragment.show(
                    fragmentManager = fragmentManager!!,
                    fragment = this,
                    isShowButton = isShowButtonDialog,
                    item = notifyItem
            )
        }
    }

    private fun showPopupCheckApplication(it: RegisterViewModel.Model) {
        isForceUpdate = it.isShowForceUpdatePopup
        forceUpdateMessage = it.forceUpdateMessage

        val forceDialogBuilder = AlertDialog.Builder(activity!!)
        forceDialogBuilder.setCancelable(false)
        forceDialogBuilder.setMessage(it.forceUpdateMessage)
        forceDialogBuilder.setPositiveButton(ContextManager.getInstance().getStringByRes(R.string.dialog_button_ok)) { dialog, which ->
            dialog!!.dismiss()
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(ContextManager.getInstance().getStringByRes(R.string.google_play_store_link))))
        }
        val forceDialog = forceDialogBuilder.create()

        val noticeDialogBuilder = AlertDialog.Builder(activity!!)
        noticeDialogBuilder.setCancelable(false)
        noticeDialogBuilder.setMessage(it.NoticeMessage)
        noticeDialogBuilder.setPositiveButton(ContextManager.getInstance().getStringByRes(R.string.dialog_button_ok)) { dialog, which ->
            dialog!!.dismiss()
            viewModel.GetAnnouncement()
        }
        val noticeDialog = noticeDialogBuilder.create()

        if (it.isShowForceUpdatePopup && !forceDialog.isShowing) {
            forceDialog.show()
            val forceButton = forceDialog.getButton(AlertDialog.BUTTON_POSITIVE)
            forceButton.setTextColor(ContextCompat.getColor(ContextManager.getInstance().getApplicationContext(), R.color.cherry_red))
        } else if (it.isShowNoticePopup && !forceDialog.isShowing && !noticeDialog.isShowing) {
            noticeDialog.show()
            val noticeButton = noticeDialog.getButton(AlertDialog.BUTTON_POSITIVE)
            noticeButton.setTextColor(ContextCompat.getColor(ContextManager.getInstance().getApplicationContext(), R.color.cherry_red))
        }else{
            viewModel.GetAnnouncement()
        }
    }


    override fun onDetailButtonClicked() {

    }

    override fun onCloseButtonClicked() {

    }

    private val socialCallback = object : SocialCallback {
        override fun onSuccess(socialProfile: SocialProfile?) {
           // //DatabaseManager.getInstance().updateSocialLoginState(isSocialLoginState = false)
            viewModel.whenSocialPressed(socialProfile!!)
        }

        override fun onFailure(message: String) {
            Toast.makeText(context, "Error: $message", Toast.LENGTH_SHORT).show()
        }
    }

    companion object {
        const val TAG = "RegisterFragment"
        const val DATA_BY_DEEPLINK_DIALOG = "DATA_BY_DEEPLINK_DIALOG"
        const val IS_SHOW_BUTTON_IN_DIALOG = "IS_SHOW_BUTTON_IN_DIALOG"
        const val IS_SHOW_DIALOG_FROM_DEEPLINK = "IS_SHOW_DIALOG_FROM_DEEPLINK"
        fun newInstance() = RegisterFragment()

        fun startByDeeplinkDialog(context: Context?,
                                  item: NotificationJsonResponse,
                                  isShowButtonDialog: Boolean = false,
                                  isShowDialog: Boolean = false) {
            MainNonCustomerActivity.openWithClearStack(
                    context = context,
                    position = MainNonCustomerActivity.REGISTER_MENU_POSITION,
                    data = Bundle().apply {
                        putParcelable(DATA_BY_DEEPLINK_DIALOG, item)
                        putBoolean(IS_SHOW_BUTTON_IN_DIALOG, isShowButtonDialog)
                        putBoolean(IS_SHOW_DIALOG_FROM_DEEPLINK, isShowDialog)
                    }
            )
        }
    }
}
