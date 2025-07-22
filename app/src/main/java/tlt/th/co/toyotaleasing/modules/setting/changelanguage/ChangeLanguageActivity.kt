package tlt.th.co.toyotaleasing.modules.setting.changelanguage

import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_change_language.*
import kotlinx.android.synthetic.main.layout_lottie_loading_screen.*
import tlt.th.co.toyotaleasing.R
import tlt.th.co.toyotaleasing.common.base.BaseActivity
import tlt.th.co.toyotaleasing.common.extension.gone
import tlt.th.co.toyotaleasing.common.extension.ifFalse
import tlt.th.co.toyotaleasing.common.extension.ifTrue
import tlt.th.co.toyotaleasing.common.extension.visible
import tlt.th.co.toyotaleasing.manager.LocalizeManager
import tlt.th.co.toyotaleasing.modules.main.MainActivity

class ChangeLanguageActivity : BaseActivity() {

    private val viewModel by lazy {
        ViewModelProviders.of(this).get(ChangeLanguageViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_change_language)

        initInstance()
        initListener()
        initViewModel()
    }

    private fun initViewModel() {
        viewModel.whenLoading.observe(this, Observer {
            toggleLoadingScreenDialog(it!!)
        })
    }

    private fun initListener() {
        thai_cv.setOnClickListener {
            if (!LocalizeManager.isThai()) {
                viewModel.updateLanguage(LocalizeManager.THAI)
                LocalizeManager.changeToTH(this)
                MainActivity.openWithClearStack(this)
            }
        }

        english_cv.setOnClickListener {
            if (LocalizeManager.isThai()) {
                viewModel.updateLanguage(LocalizeManager.ENGLISH)
                LocalizeManager.changeToEN(this)
                MainActivity.openWithClearStack(this)
            }
        }
    }

    private fun initInstance() {
        if (LocalizeManager.isThai()) {
            thai_check_button.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_check_green))
            english_check_button.setImageDrawable(null)
        } else {
            thai_check_button.setImageDrawable(null)
            english_check_button.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_check_green))
        }
    }

    companion object {
        fun start(context: Context?) {
            val intent = Intent(context, ChangeLanguageActivity::class.java)
            context?.startActivity(intent)
        }
    }
}
