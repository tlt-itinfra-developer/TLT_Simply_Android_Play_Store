package tlt.th.co.toyotaleasing.modules.setting.main

import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.github.ajalt.reprint.core.Reprint
//
import kotlinx.android.synthetic.main.fragment_setting.*
import tlt.th.co.toyotaleasing.R
import tlt.th.co.toyotaleasing.analytics.AnalyticsManager
import tlt.th.co.toyotaleasing.analytics.AnalyticsScreenName
import tlt.th.co.toyotaleasing.common.base.BaseFragment
import tlt.th.co.toyotaleasing.common.eventbus.ChangeImageProfileEvent
import tlt.th.co.toyotaleasing.common.extension.*
import tlt.th.co.toyotaleasing.common.listener.OnHambergerClickListener
import tlt.th.co.toyotaleasing.manager.BusManager
import tlt.th.co.toyotaleasing.manager.ContextManager
//import tlt.th.co.toyotaleasing.manager.ImageManager
import tlt.th.co.toyotaleasing.manager.LocalizeManager
import tlt.th.co.toyotaleasing.modules.fingerprint.FingerprintAuthDialogFragment
import tlt.th.co.toyotaleasing.modules.pincode.AuthPincodeActivity
import tlt.th.co.toyotaleasing.modules.pincode.SetupPincodeActivity
import tlt.th.co.toyotaleasing.modules.setting.changeemail.ChangeEmailActivity
import tlt.th.co.toyotaleasing.modules.setting.changelanguage.ChangeLanguageActivity
import tlt.th.co.toyotaleasing.modules.setting.changeprofilecar.ChangeCarProfileActivity
import tlt.th.co.toyotaleasing.modules.verifyemail.VerifyEmailActivity
import tlt.th.co.toyotaleasing.view.seekbar.IndicatorSeekBar
import tlt.th.co.toyotaleasing.view.seekbar.OnSeekChangeListener
import tlt.th.co.toyotaleasing.view.seekbar.SeekParams
import java.lang.Exception

class SettingFragment : BaseFragment() {

    private lateinit var email: String
    private var isChangedImage: Boolean = false
    private var setByApi: Boolean = false

    private val viewModel by lazy {
        ViewModelProviders.of(this).get(SettingViewModel::class.java)
    }

    private val onHambergerClickListener by lazy {
        context as OnHambergerClickListener
    }

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_setting, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initInstance()
        initSeekBarListener()
        initViewModel()
        setOnClickListener()
    }

//    override fun onSupportVisible() {
//        super.onSupportVisible()
//        viewModel.getData()
//    }
//
//    override fun onSupportInvisible() {
//        AnalyticsManager.trackScreen(AnalyticsScreenName.SETTING_MAIN)
//        super.onSupportInvisible()
//        isChangedImage = false
//    }

    private fun setOnClickListener() {
        profile_image_layout.setOnClickListener {
            AnalyticsManager.settingUserProfile()
            //ImageManager.open(this@SettingFragment)
        }

        language_cv.setOnClickListener {
            AnalyticsManager.settingChangeLanguage()
            ChangeLanguageActivity.start(context)
        }

        car_profile_layout.setOnClickListener {
            AnalyticsManager.settingCarProfile()
            ChangeCarProfileActivity.start(context)
        }

        layout_change_pincode.setOnClickListener {
            AnalyticsManager.settingChangePin()
            AuthPincodeActivity.startWithResult(
                    fragment = this@SettingFragment,
                    isForgotPinEnable = false
            )
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

//        if (resultCode == RESULT_OK
//                && requestCode == Define.ALBUM_REQUEST_CODE) {
//            val uri = data!!.getParcelableArrayListExtra<Uri>(Define.INTENT_PATH).firstOrNull()
//            profile_iv.loadImageByUri(uri)
//            BusManager.observe(ChangeImageProfileEvent(uri!!))
//            viewModel.updateProfileImage(uri)
//            isChangedImage = true
//            return
//        }
//
//        if (resultCode == RESULT_OK
//                && requestCode == AuthPincodeActivity.AUTH_CODE) {
//            val pincode = data?.getStringExtra(AuthPincodeActivity.AUTH_PINCODE_EXTRA)
//            viewModel.changeToResetPincodeFlow()
//            SetupPincodeActivity.start(
//                    context = context,
//                    oldPincode = pincode ?: ""
//            )
//        }
    }

    private fun initInstance() {
        val tf = ResourcesCompat.getFont(context!!, R.font.rsu_bold)
        val installmentarr = arrayOf("", resources.getString(R.string.seek_bar_five_day), "", resources.getString(R.string.seek_bar_seven_day), "", "", resources.getString(R.string.seek_bar_fifteen_day))
        val taxArr = arrayOf("", resources.getString(R.string.seek_bar_fifteen_day), "", resources.getString(R.string.seek_bar_thirty_day))
        val insuranceArr = arrayOf("", resources.getString(R.string.seek_bar_thirty_day), "", resources.getString(R.string.seek_bar_sixty_day))

        installment_seekbar.customTickTextsTypeface(tf!!)
        installment_seekbar.customTickTexts(installmentarr)

        tax_seekbar.customTickTextsTypeface(tf)
        tax_seekbar.customTickTexts(taxArr)

        insurance_seekbar.customTickTextsTypeface(tf)
        insurance_seekbar.customTickTexts(insuranceArr)

        toolbar.setOnHambergerMenuClickListener {
            AnalyticsManager.settingMenuHome()
            onHambergerClickListener.onHambergerClick()
        }

        news_promotion_switch.setOnCheckedChangeListener { _, _ ->
            AnalyticsManager.settingNewsAndPromotion()
            updateSetting()
        }
        tib_club_switch.setOnCheckedChangeListener { _, _ ->
            AnalyticsManager.settingTibClub()
            updateSetting()
        }

        id_touch_switch.setOnCheckedChangeListener { buttonView, isChecked ->
            (isChecked && !setByApi).ifTrue {
                FingerprintAuthDialogFragment.show(fragmentManager = fragmentManager!!, listener = object : FingerprintAuthDialogFragment.Listener {
                    override fun onFingerprintAuthSuccess() {
                        AnalyticsManager.settingEnabledTouchId()
                        updateSetting()
                    }

                    override fun onFingerprintAuthCancel() {
                        id_touch_switch.isChecked = false
                        updateSetting()
                    }
                })
            }

            isChecked.ifFalse {
                setByApi = false
            }

            updateSetting()
            setByApi = false
        }

        if (Reprint.isHardwarePresent() &&
                Reprint.hasFingerprintRegistered()) {
            id_touch_title.visibility = View.VISIBLE
            id_touch_switch.visibility = View.VISIBLE
        } else {
            id_touch_title.visibility = View.GONE
            id_touch_switch.visibility = View.GONE
        }
    }

    private fun initViewModel() {
        viewModel.whenLoading.observe(this, Observer {
            toggleLoadingScreenDialog(it!!, this)
        })

        viewModel.whenDataFailure.observe(this, Observer {
            if (!it.equals("invalid")) {
                showToast(it)
            }
        })

        viewModel.whenDataLoaded.observe(this, Observer {
            it?.let {
                setDataIntoViews(it)
                it.isStaffApp.ifTrue { supportForStaff(it) }
            }
        })
    }

    private fun setDataIntoViews(data: SettingViewModel.Model) {
        data.isEnabledIDTouch.ifTrue {
            setByApi = true
        }
        this.email = data.email
        full_name.text = data.name
        email_tv.text = resources.getString(R.string.setting_email_pattern, data.email)
        setProgressInstallmentSeekBar(data.notiInstallment)
        setProgressTaxSeekBar(data.notiTax)
        setProgressInsuranceSeekBar(data.notiInsurance)
        news_promotion_switch.isChecked = data.isEnabledNewsAndPromotion
        tib_club_switch.isChecked = data.isEnabledTIBClubs
        id_touch_switch.isChecked = data.isEnabledIDTouch

        when (data.status) {
            SettingViewModel.Status.CUSTOMER -> {
                group_customer.visible()
                camera_iv.visible()
                camera_iv_bg.visible()
                profile_image_layout.isClickable = true

                isChangedImage.ifFalse {
                    if (data.imageUrl.isEmpty()) {
                        profile_iv.setImageDrawable(ContextCompat.getDrawable(activity!!, R.drawable.ic_profile))
                    } else {
                        try {
                            profile_iv.loadImageByBase64(data.imageUrl)
                        }catch ( e : Exception ){
                            profile_iv.setImageDrawable(ContextCompat.getDrawable(activity!!, R.drawable.ic_profile))
                        }
                    }
                }

            }
            else -> {
                full_name.text = ContextManager.getInstance().getApplicationContext().resources.getString(R.string.question_and_answer_hint_name)
                group_customer.gone()
                camera_iv.gone()
                camera_iv_bg.gone()
                profile_iv.setImageDrawable(ContextCompat.getDrawable(activity!!, R.drawable.ic_profile))
                profile_image_layout.isClickable = false
            }
        }

        when (data.isNotVerifyEmail) {
            SettingViewModel.EmailStatus.PENDING -> {
                email_tv.setCompoundDrawablesWithIntrinsicBounds(null, null, ContextCompat.getDrawable(context!!, R.drawable.ic_exclamation_circle), null)
            }
            else -> {
                email_tv.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null)
            }
        }

        if (data.language == resources.getString(R.string.language_thai) ||
                LocalizeManager.isThai()) {
            language_selected.text = resources.getString(R.string.language_thai_display)
            language_selected.setCompoundDrawablesRelativeWithIntrinsicBounds(ContextCompat.getDrawable(context!!, R.drawable.language_thai), null, null, null)
        } else {
            language_selected.text = resources.getString(R.string.language_english_display)
            language_selected.setCompoundDrawablesRelativeWithIntrinsicBounds(ContextCompat.getDrawable(context!!, R.drawable.language_english), null, null, null)
        }

        change_email_layout.setOnClickListener {
            AnalyticsManager.settingChangeEmail()
            when (data.isNotVerifyEmail) {
                SettingViewModel.EmailStatus.PENDING -> {
                    VerifyEmailActivity.open(context = context, email = email)
                }
                else -> {
                    ChangeEmailActivity.start(context = context, email = email)
                }
            }

        }
    }

    private fun supportForStaff(it: SettingViewModel.Model) {

    }

    private fun updateSetting() {
        val installmentConfig = getProgressInstallmentSeekBar()
        val taxConfig = getProgressTaxSeekBar()
        val insuranceConfig = getProgressInsuranceSeekBar()
        val language = viewModel.whenDataLoaded.value?.language ?: ""
        val isNewsEnabled = news_promotion_switch.isChecked
        val isTIBEnabled = tib_club_switch.isChecked
        val isTouchIDEnabled = id_touch_switch.isChecked

        viewModel.updateProfile(
                language = language,
                installmentConfig = installmentConfig,
                taxConfig = taxConfig,
                insuranceConfig = insuranceConfig,
                isNewsEnabled = isNewsEnabled,
                isTIBEnabled = isTIBEnabled,
                isFingerprintEnable = isTouchIDEnabled
        )
    }

    private fun getProgressInstallmentSeekBar(): String {
        val progress = installment_seekbar.progress

        return when (progress) {
            1 -> resources.getString(R.string.seek_bar_five_day)
            3 -> resources.getString(R.string.seek_bar_seven_day)
            6 -> resources.getString(R.string.seek_bar_fifteen_day)
            else -> resources.getString(R.string.seek_bar_seven_day)
        }
    }

    private fun setProgressInstallmentSeekBar(progress: String) {
        when (progress) {
            resources.getString(R.string.seek_bar_five_day) -> installment_seekbar.setProgress(1.0f)
            resources.getString(R.string.seek_bar_seven_day) -> installment_seekbar.setProgress(3.0f)
            resources.getString(R.string.seek_bar_fifteen_day) -> installment_seekbar.setProgress(6.0f)
            else -> installment_seekbar.setProgress(3.0f)
        }
    }

    private fun getProgressTaxSeekBar(): String {
        val progress = tax_seekbar.progress

        return when (progress) {
            1 -> resources.getString(R.string.seek_bar_fifteen_day)
            3 -> resources.getString(R.string.seek_bar_thirty_day)
            else -> resources.getString(R.string.seek_bar_thirty_day)
        }
    }

    private fun setProgressTaxSeekBar(progress: String) {
        when (progress) {
            resources.getString(R.string.seek_bar_fifteen_day) -> tax_seekbar.setProgress(1.0f)
            resources.getString(R.string.seek_bar_thirty_day) -> tax_seekbar.setProgress(3.0f)
            else -> tax_seekbar.setProgress(3.0f)
        }
    }

    private fun getProgressInsuranceSeekBar(): String {
        val progress = insurance_seekbar.progress

        return when (progress) {
            1 -> resources.getString(R.string.seek_bar_thirty_day)
            3 -> resources.getString(R.string.seek_bar_sixty_day)
            else -> resources.getString(R.string.seek_bar_sixty_day)
        }
    }

    private fun setProgressInsuranceSeekBar(progress: String) {
        when (progress) {
            resources.getString(R.string.seek_bar_thirty_day) -> insurance_seekbar.setProgress(1.0f)
            resources.getString(R.string.seek_bar_sixty_day) -> insurance_seekbar.setProgress(3.0f)
            else -> insurance_seekbar.setProgress(3.0f)
        }
    }

    private fun initSeekBarListener() {
        installment_seekbar.setOnSeekChangeListener(object : OnSeekChangeListener {
            override fun onSeeking(seekParams: SeekParams) {
            }

            override fun onStartTrackingTouch(seekBar: IndicatorSeekBar) {
            }

            override fun onStopTrackingTouch(seekBar: IndicatorSeekBar) {
                AnalyticsManager.settingInstallmentDue()
                if (installment_seekbar.progress == 0) {
                    installment_seekbar.setProgress(1.0f)
                } else if (installment_seekbar.progress == 2 || installment_seekbar.progress == 4) {
                    installment_seekbar.setProgress(3.0f)
                } else if (installment_seekbar.progress == 5) {
                    installment_seekbar.setProgress(6.0f)
                }

                updateSetting()
            }
        })

        tax_seekbar.setOnSeekChangeListener(object : OnSeekChangeListener {
            override fun onSeeking(seekParams: SeekParams) {
            }

            override fun onStartTrackingTouch(seekBar: IndicatorSeekBar) {
            }

            override fun onStopTrackingTouch(seekBar: IndicatorSeekBar) {
                AnalyticsManager.settingTaxPaymentDue()
                if (tax_seekbar.progress == 0) {
                    tax_seekbar.setProgress(1.0f)
                } else if (tax_seekbar.progress == 2) {
                    tax_seekbar.setProgress(3.0f)
                }

                updateSetting()
            }
        })

        insurance_seekbar.setOnSeekChangeListener(object : OnSeekChangeListener {
            override fun onSeeking(seekParams: SeekParams) {
            }

            override fun onStartTrackingTouch(seekBar: IndicatorSeekBar) {
            }

            override fun onStopTrackingTouch(seekBar: IndicatorSeekBar) {
                AnalyticsManager.settingInsurancePaymentDue()
                if (insurance_seekbar.progress == 0) {
                    insurance_seekbar.setProgress(1.0f)
                } else if (insurance_seekbar.progress == 2) {
                    insurance_seekbar.setProgress(3.0f)
                }

                updateSetting()
            }
        })
    }

    companion object {
        fun newInstance() = SettingFragment()
    }
}
