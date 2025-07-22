package tlt.th.co.toyotaleasing.modules.tutorial

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.os.Handler
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import android.view.animation.DecelerateInterpolator
import com.aurelhubert.ahbottomnavigation.AHBottomNavigation
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem
import kotlinx.android.synthetic.main.activity_tutorial.*
import kotlinx.android.synthetic.main.widget_toolbar.view.*
import tlt.th.co.toyotaleasing.R
import tlt.th.co.toyotaleasing.analytics.AnalyticsManager
import tlt.th.co.toyotaleasing.analytics.AnalyticsScreenName
import tlt.th.co.toyotaleasing.common.base.BaseActivity
import tlt.th.co.toyotaleasing.view.spotlight.OnSpotlightStateChangedListener
import tlt.th.co.toyotaleasing.view.spotlight.Spotlight
import tlt.th.co.toyotaleasing.view.spotlight.shape.Circle
import tlt.th.co.toyotaleasing.view.spotlight.shape.RoundRectangle
import tlt.th.co.toyotaleasing.view.spotlight.target.HamburgerMenuTarget
import tlt.th.co.toyotaleasing.view.spotlight.target.PaymentTarget
import tlt.th.co.toyotaleasing.view.spotlight.target.SwipeTarget

class TutorialActivity : BaseActivity() {

    private val viewModel by lazy {
        ViewModelProviders.of(this).get(TutorialViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tutorial)

        Handler().postDelayed({
            startTutorial()
        }, 10)

        initBottomBar()

    }

    override fun onPause() {
        super.onPause()
        AnalyticsManager.trackScreen(AnalyticsScreenName.GUIDE)
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
        val typeface = ResourcesCompat.getFont(this, R.font.custom_regular)
        bottom_navigation.setTitleTypeface(typeface)

        // Add items
        bottom_navigation.addItem(installmentStatusTab)
        bottom_navigation.addItem(paymentHistoryTab)
        bottom_navigation.addItem(notificationTab)

        bottom_navigation.defaultBackgroundColor = ContextCompat.getColor(this, R.color.bottombar_default_background)

        // Change colors
        bottom_navigation.accentColor = ContextCompat.getColor(this, R.color.bottombar_tab_active)
        bottom_navigation.inactiveColor = ContextCompat.getColor(this, R.color.bottombar_tab_inactive)

        bottom_navigation.titleState = AHBottomNavigation.TitleState.ALWAYS_SHOW

        bottom_navigation.setNotification("3", 2)

        // Set current item programmatically
        bottom_navigation.currentItem = 0
    }

    private fun startTutorial() {
        //First Screen tutorial application
        val hamburgerMenuLocation = IntArray(2)
        toolbar.widget_toolbar_navigation.getLocationInWindow(hamburgerMenuLocation)
        val hamburgerPointX = hamburgerMenuLocation[0] + toolbar.widget_toolbar_navigation.width / 2f - 4
        val hamburgerPointY = hamburgerMenuLocation[1] + toolbar.widget_toolbar_navigation.height / 2f
        val spotlightHamburgerRadius = toolbar.widget_toolbar_navigation.width
        val hamburgerTarget = HamburgerMenuTarget.Builder(this@TutorialActivity).setPoint(hamburgerPointX, hamburgerPointY)
                .setShape(Circle(spotlightHamburgerRadius.toFloat() - 16))
                .setTitle(getString(R.string.tutorial_guide_title_1))
                .setDescription(getString(R.string.tutorial_guide_description_1))
                .build()

        //Second Screen tutorial application
        val selectCarLocation = IntArray(2)
        toolbar.widget_toolbar_txt_right.getLocationInWindow(selectCarLocation)
        val selectCarPointX = selectCarLocation[0] + (toolbar.widget_toolbar_menu_right.width / 2f)
        val selectCarPointY = selectCarLocation[1] + (toolbar.widget_toolbar_menu_right.height / 2f)

        val indicatorLocation = IntArray(2)
        indicator.getLocationInWindow(indicatorLocation)
        val indicatorPointX = indicatorLocation[0] + (indicator.width / 2f)
        val indicatorPointY = indicatorLocation[1] + (indicator.height / 2f)

        val selectCarTarget = SwipeTarget.Builder(this@TutorialActivity).setPoint(indicatorPointX, indicatorPointY)
                .setShape(RoundRectangle(selectCarPointX - 20,
                        selectCarPointY + toolbar.widget_toolbar_txt_right.height + 10,
                        selectCarPointY - 10,
                        selectCarPointX + toolbar.widget_toolbar_txt_right.width + 20,
                        indicatorPointX - indicator.width,
                        indicatorPointY + indicator.height,
                        indicatorPointY - indicator.height,
                        indicatorPointX + indicator.width))
                .setTitle(getString(R.string.tutorial_guide_title_2))
                .setDescription(getString(R.string.tutorial_guide_description_2))
                .build()

        //Third Screen tutorial application
        val paymentLocation = IntArray(2)
        btn_check_payment.getLocationInWindow(paymentLocation)
        val paymentPointX = paymentLocation[0] + (btn_check_payment.width / 2f)
        val paymentPointY = paymentLocation[1] + (btn_check_payment.height / 2f) + 8
        val paymentRadius = (btn_check_payment.width / 2) - 20

        val paymentTarget = PaymentTarget.Builder(this@TutorialActivity).setPoint(paymentPointX, paymentPointY)
                .setShape(Circle(paymentRadius.toFloat()))
                .setTitle(getString(R.string.tutorial_guide_title_3))
                .setDescription(getString(R.string.tutorial_guide_description_3))
                .build()


        Spotlight.with(this@TutorialActivity)
                .setOverlayColor(R.color.background)
                .setDuration(1L)
                .setAnimation(DecelerateInterpolator(2f))
                .setTargets(hamburgerTarget, selectCarTarget, paymentTarget)
                .setClosedOnTouchedOutside(true)
                .setOnSpotlightStateListener(object : OnSpotlightStateChangedListener {
                    override fun onPositionChanged(position: Int) {
                        when (position) {
                            3 -> AnalyticsManager.userGuideScreen1()
                            2 -> AnalyticsManager.userGuideScreen2()
                            1 -> AnalyticsManager.userGuideScreen3()
                        }
                    }

                    override fun onStarted() {

                    }

                    override fun onEnded() {
                        viewModel.disableShowTutorialForOpenNextTime()
                        finish()
                    }
                })
                .start()
    }
}
