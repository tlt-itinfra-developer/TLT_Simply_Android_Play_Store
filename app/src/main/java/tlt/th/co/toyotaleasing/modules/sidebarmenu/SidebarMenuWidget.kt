package tlt.th.co.toyotaleasing.modules.sidebarmenu

import android.annotation.TargetApi
import android.content.Context
import android.os.Build
import androidx.core.content.ContextCompat
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import com.google.firebase.iid.FirebaseInstanceId
import kotlinx.android.synthetic.main.widget_sidebar_menu.view.*
import tlt.th.co.toyotaleasing.BuildConfig
import tlt.th.co.toyotaleasing.R
import tlt.th.co.toyotaleasing.common.extension.gone
import tlt.th.co.toyotaleasing.common.extension.loadImageByBase64
import tlt.th.co.toyotaleasing.common.extension.visible
import tlt.th.co.toyotaleasing.manager.LocalizeManager
import tlt.th.co.toyotaleasing.manager.db.CacheManager
import tlt.th.co.toyotaleasing.manager.db.MasterDataManager
import tlt.th.co.toyotaleasing.manager.db.UserManager

class SidebarMenuWidget : FrameLayout {

    private var onSidebarMenuInteractionListener: OnSidebarMenuInteractionListener? = null

    @JvmOverloads
    constructor(
            context: Context,
            attrs: AttributeSet? = null,
            defStyleAttr: Int = 0)
            : super(context, attrs, defStyleAttr) {
        init(attrs)
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    constructor(
            context: Context,
            attrs: AttributeSet?,
            defStyleAttr: Int,
            defStyleRes: Int)
            : super(context, attrs, defStyleAttr, defStyleRes) {
        init(attrs)
    }

    private fun init(attrs: AttributeSet?) {
        LayoutInflater.from(context).inflate(R.layout.widget_sidebar_menu, this, true)
    }

    fun initMenus(model: SidebarMenuViewModel.Model) {
        val sidebarList = ArrayList<SidebarMenu>()
        sidebarList.add(SidebarMenu(SidebarMenu.Type.EMPTY))
        sidebarList.add(SidebarMenu(getString(R.string.sidebar_menu_installment), R.drawable.ic_installment, SidebarMenu.Type.MENU) {
            onSidebarMenuInteractionListener?.onInstallmentClicked()
            onSidebarMenuInteractionListener?.onAfterInteraction()
        })

        if (UserManager.getInstance().isCustomer()) {
            val master = MasterDataManager.getInstance().getSideMenu()
            val profile = CacheManager.getCacheProfile()

        }


        sidebarList.add(SidebarMenu(getString(R.string.sidebar_menu_tax), R.drawable.ic_tax, SidebarMenu.Type.MENU) {
            onSidebarMenuInteractionListener?.onTaxClicked()
            onSidebarMenuInteractionListener?.onAfterInteraction()
        })


        if (UserManager.getInstance().isCustomer()) {
            val master = MasterDataManager.getInstance().getSideMenu()
            val profile = CacheManager.getCacheProfile()
            if (master != null  && profile!=null) {

            }
        }

        sidebarList.add(SidebarMenu(getString(R.string.sidebar_menu_insurance), R.drawable.ic_other_insurance, SidebarMenu.Type.MENU) {
            onSidebarMenuInteractionListener?.onInsuranceClicked()
            onSidebarMenuInteractionListener?.onAfterInteraction()
        })

//        Simply p2 Add other Insurance website
        sidebarList.add(SidebarMenu(getString(R.string.sidebar_menu_other_insurance), R.drawable.ic_insurance_1, SidebarMenu.Type.MENU) {
            onSidebarMenuInteractionListener?.onInsuranceOtherClicked()
            onSidebarMenuInteractionListener?.onAfterInteraction()
        })

        sidebarList.add(SidebarMenu(getString(R.string.sidebar_menu_car_loan), R.drawable.ic_car_menu, SidebarMenu.Type.MENU) {
            onSidebarMenuInteractionListener?.onCarLoanClicked()
            onSidebarMenuInteractionListener?.onAfterInteraction()
        })

        sidebarList.add(SidebarMenu(getString(R.string.sidebar_menu_qrcode), R.drawable.ic_qrcode, SidebarMenu.Type.MENU) {
            onSidebarMenuInteractionListener?.onQRCodeClicked()
            onSidebarMenuInteractionListener?.onAfterInteraction()
        })


        sidebarList.add(SidebarMenu(SidebarMenu.Type.DIVIDER))


        sidebarList.add(SidebarMenu(getString(R.string.sidebar_menu_cal_installment), R.drawable.ic_calculate_gray, SidebarMenu.Type.MENU) {
            onSidebarMenuInteractionListener?.onCalculateInstallmentClick()
            onSidebarMenuInteractionListener?.onAfterInteraction()
        })
        sidebarList.add(SidebarMenu(getString(R.string.sidebar_menu_news), R.drawable.ic_news, SidebarMenu.Type.MENU) {
            onSidebarMenuInteractionListener?.onNewsClicked()
            onSidebarMenuInteractionListener?.onAfterInteraction()
        })
        sidebarList.add(SidebarMenu(getString(R.string.sidebar_menu_location), R.drawable.ic_building_sidebar, SidebarMenu.Type.MENU) {
            onSidebarMenuInteractionListener?.onContactUsClicked()
            onSidebarMenuInteractionListener?.onAfterInteraction()
        })
        sidebarList.add(SidebarMenu(getString(R.string.sidebar_menu_office_location), R.drawable.ic_office_location, SidebarMenu.Type.MENU) {
            onSidebarMenuInteractionListener?.onOfficeLocationClicked()
            onSidebarMenuInteractionListener?.onAfterInteraction()
        })
        sidebarList.add(SidebarMenu(getString(R.string.sidebar_menu_chat), R.drawable.ic_chat, SidebarMenu.Type.MENU) {
            onSidebarMenuInteractionListener?.onLiveChatClicked()
            onSidebarMenuInteractionListener?.onAfterInteraction()
        })
        sidebarList.add(SidebarMenu(getString(R.string.sidebar_menu_document_download), R.drawable.ic_download, SidebarMenu.Type.MENU) {
            onSidebarMenuInteractionListener?.onDownloadClicked()
            onSidebarMenuInteractionListener?.onAfterInteraction()
        })
        sidebarList.add(SidebarMenu(getString(R.string.sidebar_menu_question), R.drawable.ic_question, SidebarMenu.Type.MENU) {
            onSidebarMenuInteractionListener?.onFAQClicked()
            onSidebarMenuInteractionListener?.onAfterInteraction()
        })
        sidebarList.add(SidebarMenu(getString(R.string.sidebar_menu_policy), R.drawable.ic_policy, SidebarMenu.Type.MENU) {
            onSidebarMenuInteractionListener?.onTermAndConditionClicked()
            onSidebarMenuInteractionListener?.onAfterInteraction()
        })

        sidebarList.add(SidebarMenu(getString(R.string.sidebar_menu_privacy_policy), R.drawable.ic_policy, SidebarMenu.Type.MENU) {
            onSidebarMenuInteractionListener?.onPrivacyPolicyClicked()
            onSidebarMenuInteractionListener?.onAfterInteraction()
        })

        sidebarList.add(SidebarMenu(getString(R.string.sidebar_menu_setting), R.drawable.ic_setting, getDrawableResLang(), SidebarMenu.Type.MENU) {
            onSidebarMenuInteractionListener?.onSettingClicked()
            onSidebarMenuInteractionListener?.onAfterInteraction()
        })



        if (!UserManager.getInstance().isCustomer()) {
            btn_notification.visible()
        } else {
            btn_notification.gone()
        }


        if (model.isCustomer) {
            sidebarList.add(SidebarMenu(SidebarMenu.Type.DIVIDER))
            sidebarList.add(SidebarMenu(getString(R.string.sidebar_menu_logout), R.drawable.ic_logout, SidebarMenu.Type.MENU) {
                onSidebarMenuInteractionListener?.onSignoutClicked()
                onSidebarMenuInteractionListener?.onAfterInteraction()
            })

            btn_register.text = model.fullname
            btn_register.isEnabled = false
        }

        if (UserManager.getInstance().isCustomer()) {
            if (model.profileImage.isEmpty()) {
                ic_profile.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_profile))
            } else {
                try {
                    ic_profile.loadImageByBase64(model.profileImage)
                }catch (e : Exception){
                    ic_profile.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_profile))
                    e.printStackTrace()
                }
            }
        } else {
            ic_profile.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_lock))
        }

//        sidebarList.add(SidebarMenu(SidebarMenu.Type.DIVIDER))
//        sidebarList.add(SidebarMenu(getString(R.string.sidebar_menu_debug), R.drawable.ic_setting, SidebarMenu.Type.MENU) {
//            onSidebarMenuInteractionListener?.onDebugClicked()
//            onSidebarMenuInteractionListener?.onAfterInteraction()
//        })

        sidebarList.add(SidebarMenu(SidebarMenu.Type.DIVIDER))
        sidebarList.add(SidebarMenu("v${BuildConfig.VERSION_NAME}", R.drawable.ic_baseline_info, SidebarMenu.Type.MENU) {
            onSidebarMenuInteractionListener?.onVersionClicked(secretMode())
            if (UserManager.getInstance().isSecretModeAttemptCompleted()) {
                onSidebarMenuInteractionListener?.onAfterInteraction()
            }
        })

        sidebarList.add(SidebarMenu(SidebarMenu.Type.EMPTY))

        val adapter = SidebarMenuAdapter(sidebarList)
        recycler_view_menu.adapter = adapter
    }

    private fun secretMode(): String {
        val userManager = UserManager.getInstance()

        userManager.increaseSecretModeAttempt()

        if (userManager.isSecretModeAttemptCompleted()) {
            userManager.resetSecretModeAttempt()
            return FirebaseInstanceId.getInstance().token ?: "Token is Empty"
        }

        if (userManager.getSecretAttempt() == 6) {
            return "1"
        }

        if (userManager.getSecretAttempt() == 5) {
            return "2"
        }

        return ""
    }

    fun setBadgeNotification(badge: Int) {
        btn_notification.setNotification(badge)
    }

    fun setOnNotificationClicked(listener: OnClickListener) {
        btn_notification.setOnClickListener(listener)
    }

    fun setOnRegisterClicked(listener: OnClickListener) {
        btn_register.setOnClickListener(listener)
    }

    fun setOnSidebarMenuCloseClicked(listener: OnClickListener) {
        btn_close_sidebar.setOnClickListener(listener)
    }

    fun setOnSidebarMenuInteractionListener(listener: OnSidebarMenuInteractionListener) {
        onSidebarMenuInteractionListener = listener
    }

    private fun getString(res: Int) = context.getString(res)

    private fun getDrawableResLang() = if (LocalizeManager.isThai()) {
        R.drawable.ic_th
    } else {
        R.drawable.ic_en
    }

    interface OnSidebarMenuInteractionListener {
        fun onInstallmentClicked()
        fun onTaxClicked()
        fun onInsuranceClicked()
        fun onInsuranceOtherClicked()
        fun onCarLoanClicked() // CarLoan Milk
        fun onMandatoriumClicked()
        fun onOwnershipTranferClicked()
        fun onECommerceClicked()
        fun onQRCodeClicked()
        fun onCalculateInstallmentClick()
        fun onNewsClicked()
        fun onContactUsClicked()
        fun onOfficeLocationClicked()
        fun onLiveChatClicked()
        fun onDownloadClicked()
        fun onFAQClicked()
        fun onTermAndConditionClicked()
        fun onPrivacyPolicyClicked()
        fun onSettingClicked()
        fun onSignoutClicked()
        fun onDebugClicked()
        fun onAfterInteraction()
        fun onVersionClicked(txt: String)
    }
}
