package tlt.th.co.toyotaleasing.modules.installment

import android.annotation.SuppressLint
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.aurelhubert.ahbottomnavigation.AHBottomNavigation
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem
import kotlinx.android.synthetic.main.fragment_installment.*
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import tlt.th.co.toyotaleasing.R
import tlt.th.co.toyotaleasing.analytics.AnalyticsManager
import tlt.th.co.toyotaleasing.common.base.BaseFragment
import tlt.th.co.toyotaleasing.common.eventbus.NotificationInstallmentSelectCarEvent
import tlt.th.co.toyotaleasing.common.eventbus.NotificationInstallmentTypeEvent
import tlt.th.co.toyotaleasing.common.eventbus.UpdateBadgeNotificationEvent
import tlt.th.co.toyotaleasing.common.extension.ifTrue
import tlt.th.co.toyotaleasing.common.listener.OnHambergerClickListener
import tlt.th.co.toyotaleasing.manager.BusManager
import tlt.th.co.toyotaleasing.manager.ContextManager
import tlt.th.co.toyotaleasing.manager.db.UserManager
import tlt.th.co.toyotaleasing.model.response.NotificationJsonResponse
import tlt.th.co.toyotaleasing.modules.contract.ContractDetailActivity
import tlt.th.co.toyotaleasing.modules.customer.MyCarFragment
import tlt.th.co.toyotaleasing.modules.filtercar.FilterCarActivity
import tlt.th.co.toyotaleasing.modules.home.customer.MainCustomerActivity
import tlt.th.co.toyotaleasing.modules.home.customer.NoticeDialogFragment
import tlt.th.co.toyotaleasing.modules.notify.NotifyFragment
import tlt.th.co.toyotaleasing.modules.paymenthistory.PaymentHistoryFragment

class CarInstallmentFragment : BaseFragment(), NoticeDialogFragment.Listener {

    private val viewModel by lazy {
        ViewModelProviders.of(this).get(CarInstallmentViewModel::class.java)
    }

    private val onHambergerClickListener by lazy {
        context as OnHambergerClickListener
    }

    private val notifyItem by lazy {
        activity?.intent
                ?.getBundleExtra(MainCustomerActivity.DATA_POSITION_EXTRA)
                ?.getParcelable(DATA_BY_DEEPLINK_DIALOG) ?: NotificationJsonResponse()
    }

    private val isShowButtonDialog by lazy {
        activity?.intent
                ?.getBundleExtra(MainCustomerActivity.DATA_POSITION_EXTRA)
                ?.getBoolean(IS_SHOW_BUTTON_IN_DIALOG) ?: false
    }

    private val isShowDialog by lazy {
        activity?.intent
                ?.getBundleExtra(MainCustomerActivity.DATA_POSITION_EXTRA)
                ?.getBoolean(IS_SHOW_DIALOG_FROM_DEEPLINK) ?: false
    }

    private val isRefinanceShowDialog by lazy {
        activity?.intent
                ?.getBundleExtra(MainCustomerActivity.DATA_POSITION_EXTRA)
                ?.getBoolean(IS_SHOW_DIALOG_REFINANCE)
    }

    private val contractNumberFromRefinance by lazy {
        activity?.intent
                ?.getBundleExtra(MainCustomerActivity.DATA_POSITION_EXTRA)
                ?.getString(REFINANCE_CONTRACT_NUMBER)
    }

    private val defaultTabPosition by lazy {
        activity?.intent
                ?.getBundleExtra(MainCustomerActivity.DATA_POSITION_EXTRA)
                ?.getInt(TAB_POSITION_EXTRA, 0)
    }

    private var lastTabPosition = defaultTabPosition

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_installment, container, false)
    }

    override fun onStart() {
        super.onStart()
        BusManager.subscribe(this)
    }

    override fun onStop() {
        super.onStop()
        BusManager.unsubscribe(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViewModel()
        initInstances()

        viewModel.getCarList()
    }

    private fun initViewModel() {
        viewModel.whenLoading.observe(this, Observer {
            toggleLoadingScreenDialog(it!!, this)
        })

        viewModel.whenGetCarListLoaded.observe(this, Observer {
            initBottomBar()
        })

//        viewModel.whenCarListISEmpty.observe(this, Observer {
//            if (it!!) {
//                AlertDialog.Builder(activity)
//                        .setTitle(ContextManager.getInstance().getStringByRes(R.string.dialog_an_error_occurred))
//                        .setCancelable(false)
//                        .setPositiveButton(ContextManager.getInstance().getStringByRes(R.string.dialog_button_ok)) { _, _ ->
//                            viewModel.getCarList()
//                        }
//                        .show()
//            }
//        })

        viewModel.whenDataLoadedFailure.observe(this, Observer {
//            Toast.makeText(context!!, it, Toast.LENGTH_SHORT).show()
//            viewModel.getCarList()
        })

    }

    private fun initInstances() {
        toolbar.setOnHambergerMenuClickListener {
            AnalyticsManager.installmentMyCarMenuHomeClicked()
            onHambergerClickListener.onHambergerClick()
        }

        toolbar.setOnRightMenuTitleClickListener {
            AnalyticsManager.installmentMyCarSelectCarClicked()
            FilterCarActivity.open(context!!)
        }
    }

    private fun initBottomBar() {
        if (bottom_navigation.itemsCount > 0) {
            return
        }

        // Create items
        val installmentStatusTab = AHBottomNavigationItem(getString(R.string.installment_status_title), R.drawable.ic_tab_mycar)
        val paymentHistoryTab = AHBottomNavigationItem(getString(R.string.payment_history_title), R.drawable.ic_tab_history)
        val notificationTab = AHBottomNavigationItem(getString(R.string.notification_title), R.drawable.ic_tab_notification)

        // Change Font
        val typeface = ResourcesCompat.getFont(context!!, R.font.custom_regular)
        bottom_navigation.setTitleTypeface(typeface)

        // Add items
        bottom_navigation.addItem(installmentStatusTab)
        bottom_navigation.addItem(paymentHistoryTab)
        bottom_navigation.addItem(notificationTab)

        bottom_navigation.defaultBackgroundColor = ContextCompat.getColor(context!!, R.color.bottombar_default_background)

        // Change colors
        bottom_navigation.accentColor = ContextCompat.getColor(context!!, R.color.bottombar_tab_active)
        bottom_navigation.inactiveColor = ContextCompat.getColor(context!!, R.color.bottombar_tab_inactive)

        bottom_navigation.titleState = AHBottomNavigation.TitleState.ALWAYS_SHOW

        // Set listeners
        bottom_navigation.setOnTabSelectedListener { position, wasSelected ->

            if (lastTabPosition == 0) {
                if (position == 1) {
                    AnalyticsManager.installmentMyCarPaymentHistoryClicked()
                } else if (position == 2) {
                    AnalyticsManager.installmentMyCarNotificationClicked()
                }
            } else if (lastTabPosition == 1){
                if (position == 0) {
                    AnalyticsManager.historyMyCar()
                } else if (position == 2) {
                    AnalyticsManager.historyNotification()
                }
            } else {
                if (position == 0) {
                    AnalyticsManager.notificationMyCar()
                } else if (position == 1) {
                    AnalyticsManager.notificationPaymentHistory()
                }
            }

            lastTabPosition = position
            val currentTab = bottom_navigation.getItem(position)
            toolbar.setTitle(currentTab.getTitle(context))


            true
        }

        if (UserManager.getInstance().getUnreadNotification().toInt() > 0) {
            bottom_navigation.setNotification(UserManager.getInstance().getUnreadNotification(), 2)
        }

        // Set current item programmatically
        bottom_navigation.currentItem = defaultTabPosition ?: 0

        isShowDialog.ifTrue {
            NoticeDialogFragment.show(
                    fragmentManager = fragmentManager!!,
                    fragment = this,
                    item = notifyItem,
                    isShowButton = isShowButtonDialog)
        }

        contractNumberFromRefinance?.let {
            if (isRefinanceShowDialog == true) {
                NoticeDialogFragment.show(
                    fragmentManager = fragmentManager!!,
                        fragment = this,
                        item = notifyItem,
                        isShowButton = true
                )
            } else {
                ContractDetailActivity.start(
                        context = context,
                        contractNumber = it,
                        position = ContractDetailActivity.REFINANCE_POSITION
                )
            }
        }
    }

    override fun onDetailButtonClicked() {
        isRefinanceShowDialog?.ifTrue {
            ContractDetailActivity.start(
                    context = context,
                    contractNumber = contractNumberFromRefinance!!,
                    position = ContractDetailActivity.REFINANCE_POSITION
            )
        }
    }

    override fun onCloseButtonClicked() {

    }

    private fun addBadgeNotification(amount: String) {
        bottom_navigation.setNotification(amount, 2)
    }

    @SuppressLint("ResourceType")
    private fun hideBadgeNotification() {
        bottom_navigation.setNotification("", 2)
        bottom_navigation.setNotificationBackground(ContextCompat.getDrawable(activity!!, android.R.color.transparent))
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onBadgeNotificationReceived(event: UpdateBadgeNotificationEvent) {
        if (UserManager.getInstance().getUnreadNotification().toInt() > 0
                && bottom_navigation.currentItem != 2) {
            addBadgeNotification(event.message)
        } else {
            UserManager.getInstance().setZeroUnreadNotification()
            hideBadgeNotification()
        }
    }

    companion object {
        const val DATA_BY_DEEPLINK_DIALOG = "DATA_BY_DEEPLINK_DIALOG"
        const val IS_SHOW_BUTTON_IN_DIALOG = "IS_SHOW_BUTTON_IN_DIALOG"
        const val IS_SHOW_DIALOG_FROM_DEEPLINK = "IS_SHOW_DIALOG_FROM_DEEPLINK"
        const val IS_SHOW_DIALOG_REFINANCE = "IS_SHOW_DIALOG_REFINANCE"
        const val REFINANCE_CONTRACT_NUMBER = "REFINANCE_CONTRACT_NUMBER"
        private const val TAB_POSITION_EXTRA = "TAB_POSITION_EXTRA"

        const val INSTALLMENT_TAB_POSITION = 0
        const val PAYMENT_HISTORY_TAB_POSITION = 1
        const val NOTIFY_TAB_POSITION = 2

        fun newInstance() = CarInstallmentFragment()

        fun startByDeeplinkDialog(context: Context?,
                                  item: NotificationJsonResponse,
                                  isShowButtonDialog: Boolean = false,
                                  isShowDialog: Boolean = false) {
            MainCustomerActivity.startWithClearStack(
                    context = context,
                    position = MainCustomerActivity.INSTALLMENT_MENU_POSITION,
                    data = Bundle().apply {
                        putParcelable(DATA_BY_DEEPLINK_DIALOG, item)
                        putInt(TAB_POSITION_EXTRA, INSTALLMENT_TAB_POSITION)
                        putBoolean(IS_SHOW_BUTTON_IN_DIALOG, isShowButtonDialog)
                        putBoolean(IS_SHOW_DIALOG_FROM_DEEPLINK, isShowDialog)
                    }
            )
        }

        fun startByDeeplinkRefinanceDialog(context: Context?,
                                           item: NotificationJsonResponse,
                                           contractNo: String) {
            MainCustomerActivity.startWithClearStack(
                    context = context,
                    position = MainCustomerActivity.INSTALLMENT_MENU_POSITION,
                    data = Bundle().apply {
                        putParcelable(DATA_BY_DEEPLINK_DIALOG, item)
                        putBoolean(IS_SHOW_DIALOG_REFINANCE, true)
                        putString(REFINANCE_CONTRACT_NUMBER, contractNo)
                    }
            )
        }

        fun startByDeeplinkForeground(context: Context?,
                                      tabPosition: Int) {
            MainCustomerActivity.startWithClearStack(
                    context = context,
                    position = MainCustomerActivity.INSTALLMENT_MENU_POSITION,
                    data = Bundle().apply {
                        putInt(TAB_POSITION_EXTRA, tabPosition)
                    }
            )
        }

    }
}
