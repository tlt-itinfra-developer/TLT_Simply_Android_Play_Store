package tlt.th.co.toyotaleasing.modules.contactus.question_and_answer

import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import kotlinx.android.synthetic.main.activity_question_and_answer.*
import tlt.th.co.toyotaleasing.R
import tlt.th.co.toyotaleasing.analytics.AnalyticsManager
import tlt.th.co.toyotaleasing.analytics.AnalyticsScreenName
import tlt.th.co.toyotaleasing.common.base.BaseActivity
import tlt.th.co.toyotaleasing.common.dialog.NormalDialogFragment
import tlt.th.co.toyotaleasing.common.extension.*
import tlt.th.co.toyotaleasing.manager.db.UserManager

class QuestionAndAnswerActivity : BaseActivity(), NormalDialogFragment.Listener {

    private val viewModel by lazy {
        ViewModelProviders.of(this).get(QuestionAndAnswerViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_question_and_answer)

        initViewModel()
        initInstances()

        viewModel.getData()
    }

    override fun onPause() {
        super.onPause()
        AnalyticsManager.trackScreen(AnalyticsScreenName.CONTACT_QUESTION)
    }

    private fun initViewModel() {
        viewModel.whenLoading.observe(this, Observer {
            toggleLoadingScreenDialog(it!!)
        })

        viewModel.whenDataLoaded.observe(this, Observer {
            it?.let {
                setupDataIntoViews(it)
                it.isStaffApp.ifTrue { supportForStaff(it) }
            }
        })

        viewModel.whenSendToApiFailure.observe(this, Observer {
            showToast("Send Contact Fail.")
        })

        viewModel.whenSendToApiSuccess.observe(this, Observer {
            showSuccessDialog()
        })
    }

    private fun initInstances() {
        spinner_topic.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                watcherEnableConfirm()
            }
        }

        btn_confirm.setOnClickListener {
            if (edittext_phonenumber.length() < 10){
                textinput_phonenumber.enableErrorMessage(getString(R.string.error_not_empty)) // test
            }else {
                AnalyticsManager.contactQuestionSubmit()
                viewModel.send(
                        name = edittext_name.text.toString(),
                        email = edittext_email.text.toString(),
                        phone = edittext_phonenumber.text.toString(),
                        carLicense = edittext_car_license.text.toString(),
                        topicIndex = spinner_topic.getSelectedPositionWithoutHint(),
                        detail = edittext_detail.text.toString()
                )
            }
        }

        edittext_name.onWatcherAfterTextChanged { watcherEnableConfirm() }
        edittext_phonenumber.onWatcherAfterTextChanged { watcherEnableConfirm() }
    }

    private fun supportForStaff(it: QuestionAndAnswerViewModel.Model) {

    }

    override fun onDialogConfirmClick() {
        finish()
    }

    override fun onDialogCancelClick() {

    }

    private fun showSuccessDialog() {
        NormalDialogFragment.show(
                fragmentManager = supportFragmentManager,
                description = getString(R.string.question_and_answer_sent_success),
                confirmButtonMessage = getString(R.string.refinance_diaglog_btn_confirm)
        )
    }

    private fun setupDataIntoViews(it: QuestionAndAnswerViewModel.Model?) {
        edittext_name.setText(it?.name)
        edittext_email.setText(it?.email)
        edittext_phonenumber.setText(it?.phonenumber)
        edittext_car_license.setText(it?.carLicense)
        spinner_topic.setItems(it?.topics ?: listOf())

        when (it?.status) {
            QuestionAndAnswerViewModel.Status.CUSTOMER -> group_customer.gone()
            else -> group_customer.visible()
        }
    }

    private fun watcherEnableConfirm() {
        if (UserManager.getInstance().isCustomer()) {
            if (spinner_topic.isSelectHint()) {
                btn_confirm.isEnabled = false
                return
            }
        } else {
            if (edittext_name.text.toString().trim().isEmpty()
                    || edittext_phonenumber.text.toString().trim().isEmpty()
                    || spinner_topic.isSelectHint()) {
                btn_confirm.isEnabled = false
                return
            }
        }

        btn_confirm.isEnabled = true
    }

    companion object {
        fun start(context: Context?) {
            val intent = Intent(context, QuestionAndAnswerActivity::class.java)
            context?.startActivity(intent)
        }
    }
}
