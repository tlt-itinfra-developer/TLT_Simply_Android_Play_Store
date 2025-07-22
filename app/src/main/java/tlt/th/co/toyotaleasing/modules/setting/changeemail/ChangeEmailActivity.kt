package tlt.th.co.toyotaleasing.modules.setting.changeemail

import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_change_email.*
import tlt.th.co.toyotaleasing.R
import tlt.th.co.toyotaleasing.analytics.AnalyticsManager
import tlt.th.co.toyotaleasing.analytics.AnalyticsScreenName
import tlt.th.co.toyotaleasing.common.base.BaseActivity
import tlt.th.co.toyotaleasing.common.extension.enableErrorMessage

class ChangeEmailActivity : BaseActivity() {

    private val viewModel by lazy {
        ViewModelProviders.of(this).get(ChangeEmailViewModel::class.java)
    }

    private val oldEmail by lazy {
        intent?.getStringExtra(EMAIL) ?: ""
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_change_email)

        initInstance()
        initViewModel()
        initTextWatcher()
        setOnClickListener()

    }

    override fun onPause() {
        super.onPause()
        AnalyticsManager.trackScreen(AnalyticsScreenName.SETTING_CHANGE_EMAIL)
    }

    private fun initInstance() {
        new_email_edit_text.requestFocus()
        old_email_edit_text.setText(oldEmail)
    }

    private fun initViewModel() {
        viewModel.whenEmailInvalidFormat.observe(this, Observer {
            new_email_text_input.enableErrorMessage(getString(R.string.error_email_pattern))
        })

        viewModel.whenSubmitFormFailure.observe(this, Observer {
            Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
        })

        viewModel.whenChangeEmailSuccess.observe(this, Observer {
            if (it!!) {
                ChangeEmailVerifySuccessActivity.start(this, new_email_edit_text.text.toString().trim())
            }
        })
    }

    private fun setOnClickListener() {
        button.setOnClickListener {
            AnalyticsManager.settingChangeEmailConfirm()
            viewModel.changeEmail(new_email_edit_text.text.toString())
        }
    }

    private fun initTextWatcher() {
        old_email_edit_text.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                button.isEnabled = old_email_edit_text.text!!.isNotEmpty() &&
                        new_email_edit_text.text!!.isNotEmpty()
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
        })

        new_email_edit_text.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                button.isEnabled = old_email_edit_text.text!!.isNotEmpty() &&
                        new_email_edit_text.text!!.isNotEmpty()
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
        })
    }

    companion object {
        const val EMAIL = "change_email_key_extra"

        fun start(context: Context?, email: String?) {
            val intent = Intent(context, ChangeEmailActivity::class.java)
            intent.putExtra(EMAIL, email)
            context?.startActivity(intent)
        }
    }
}
