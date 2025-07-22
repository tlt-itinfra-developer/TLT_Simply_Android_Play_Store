//package tlt.th.co.toyotaleasing.modules.carloan.mainloan
//
//import android.content.Context
//import android.content.Intent
//import android.os.Bundle
//import tlt.th.co.toyotaleasing.R
//import tlt.th.co.toyotaleasing.common.base.BaseActivity
//import tlt.th.co.toyotaleasing.common.base.BaseFragment
//
//class MainCustomerCarLoanActivity : BaseActivity() {
//
//    private val MainStep by lazy {
//        intent.getStringExtra(MAIN_LOAN_POSITION_EXTRA ) ?: "0"
//    }
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_loan_main_cust_car)
//
//        initInstances()
//        initViewModel()
//        initRouteMainMenu()
//
//    }
//
//    private fun initInstances() {
//
//    }
//
//    private fun initViewModel() {
//
//    }
//
//    private fun initRouteMainMenu() {
//        val fragmentList = ArrayList<BaseFragment>()
//        when(MainStep) {
//            "0" -> {
//                fragmentList.add(findFragment(AuthenLoanBasicDetailFragment::class.java)
//                        ?: AuthenLoanBasicDetailFragment.newInstance())
//            }
//            "1"-> {
//                fragmentList.add(findFragment(AuthenLoanBasicDetailFragment::class.java)
//                        ?: AuthenLoanBasicDetailFragment.newInstance())
//            }
//            "2"-> {
//                fragmentList.add(findFragment(AuthenLoanBasicDetailFragment::class.java)
//                        ?: AuthenLoanBasicDetailFragment.newInstance())
//            }
//            "3"-> {
//                fragmentList.add(findFragment(AuthenLoanBasicDetailFragment::class.java)
//                        ?: AuthenLoanBasicDetailFragment.newInstance())
//            }
//            "4"-> {
//                fragmentList.add(findFragment(AuthenLoanBasicDetailFragment::class.java)
//                        ?: AuthenLoanBasicDetailFragment.newInstance())
//            }
//
//        }
//
////        loadMultipleRootFragment(content_container.id, defaultTabPosition
////                ?: 0, fragmentList[0]
////                    , fragmentList[1]
////                    , fragmentList[2]
////                    ,  fragmentList[3]
////                    ,  fragmentList[4])
////
////        // Create items
////        val installmentStatusTab = AHBottomNavigationItem(getString(R.string.installment_status_title), R.drawable.ic_tab_mycar)
////        val paymentHistoryTab = AHBottomNavigationItem(getString(R.string.payment_history_title), R.drawable.ic_tab_history)
////        val notificationTab = AHBottomNavigationItem(getString(R.string.notification_title), R.drawable.ic_tab_notification)
////
////        // Change Font
////        val typeface = ResourcesCompat.getFont(context!!, R.font.custom_regular)
////        bottom_navigation.setTitleTypeface(typeface)
////
////        // Add items
////        bottom_navigation.addItem(installmentStatusTab)
////        bottom_navigation.addItem(paymentHistoryTab)
////        bottom_navigation.addItem(notificationTab)
////
////        bottom_navigation.defaultBackgroundColor = ContextCompat.getColor(context!!, R.color.bottombar_default_background)
////
////        // Change colors
////        bottom_navigation.accentColor = ContextCompat.getColor(context!!, R.color.bottombar_tab_active)
////        bottom_navigation.inactiveColor = ContextCompat.getColor(context!!, R.color.bottombar_tab_inactive)
////
////        bottom_navigation.titleState = AHBottomNavigation.TitleState.ALWAYS_SHOW
////
////        // Set listeners
////        bottom_navigation.setOnTabSelectedListener { position, wasSelected ->
////
////            if (lastTabPosition == 0) {
////                if (position == 1) {
////                    AnalyticsManager.installmentMyCarPaymentHistoryClicked()
////                } else if (position == 2) {
////                    AnalyticsManager.installmentMyCarNotificationClicked()
////                }
////            } else if (lastTabPosition == 1){
////                if (position == 0) {
////                    AnalyticsManager.historyMyCar()
////                } else if (position == 2) {
////                    AnalyticsManager.historyNotification()
////                }
////            } else {
////                if (position == 0) {
////                    AnalyticsManager.notificationMyCar()
////                } else if (position == 1) {
////                    AnalyticsManager.notificationPaymentHistory()
////                }
////            }
////
////            lastTabPosition = position
////            val currentTab = bottom_navigation.getItem(position)
////            toolbar.setTitle(currentTab.getTitle(context))
////            showHideFragment(fragmentList[position])
////
////
////            true
////        }
////
////        if (UserManager.getInstance().getUnreadNotification().toInt() > 0) {
////            bottom_navigation.setNotification(UserManager.getInstance().getUnreadNotification(), 2)
////        }
////
////        // Set current item programmatically
////        bottom_navigation.currentItem = defaultTabPosition ?: 0
////
////        isShowDialog.ifTrue {
////            NoticeDialogFragment.show(
////                    fragmentManager = fragmentManager,
////                    fragment = this,
////                    item = notifyItem,
////                    isShowButton = isShowButtonDialog)
////        }
////
////        contractNumberFromRefinance?.let {
////            if (isRefinanceShowDialog == true) {
////                NoticeDialogFragment.show(
////                        fragmentManager = fragmentManager,
////                        fragment = this,
////                        item = notifyItem,
////                        isShowButton = true
////                )
////            } else {
////                ContractDetailActivity.start(
////                        context = context,
////                        contractNumber = it,
////                        position = ContractDetailActivity.REFINANCE_POSITION
////                )
////            }
////        }
//    }
//    companion object {
//        const val DATA_LOAN_POSITION_EXTRA = "DATA_LOAN_POSITION_EXTRA"
//        private const val MAIN_LOAN_POSITION_EXTRA = "MAIN_LOAN_POSITION_EXTRA"
//
//        const val BASIC_DETAIL_MAIN_STEP = 0
//        const val CAR_LOAN_MAIN_STEP = 1
//        const val CUSTOMER_INFORMATION_MAIN_STEP = 2
//        const val CHECK_STATUS_MAIN_STEP = 3
//        const val DELIVERY_MAIN_STEP = 4
//
//        fun start(context: Context) {
//            val intent = Intent(context, MainCustomerCarLoanActivity::class.java).apply {
//                putExtra(MAIN_LOAN_POSITION_EXTRA, MAIN_LOAN_POSITION_EXTRA)
//                putExtra(DATA_LOAN_POSITION_EXTRA, DATA_LOAN_POSITION_EXTRA)
//            }
//            context.startActivity(intent)
//        }
//
//
//    }
//}
