package tlt.th.co.toyotaleasing.modules.contactus

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_contactus_main.*
import tlt.th.co.toyotaleasing.R
import tlt.th.co.toyotaleasing.analytics.AnalyticsManager
import tlt.th.co.toyotaleasing.analytics.AnalyticsScreenName
import tlt.th.co.toyotaleasing.common.base.BaseFragment
import tlt.th.co.toyotaleasing.common.dialog.CallCenterDialogFragment
import tlt.th.co.toyotaleasing.common.dialog.NormalDialogFragment
import tlt.th.co.toyotaleasing.common.listener.OnHambergerClickListener
import tlt.th.co.toyotaleasing.modules.contactus.question_and_answer.QuestionAndAnswerActivity
import tlt.th.co.toyotaleasing.modules.insurance.hotline.HotLineActivity
import tlt.th.co.toyotaleasing.modules.insurance.tibclub.TIBClubActivity
import tlt.th.co.toyotaleasing.util.ExternalAppUtils

class ContactUsMainFragment : BaseFragment() {

    private val onHambergerClickListener by lazy {
        context as OnHambergerClickListener
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_contactus_main, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initInstances()
    }

    private fun initInstances() {
        toolbar.setOnHambergerMenuClickListener {
            onHambergerClickListener.onHambergerClick()
        }

        group_call_center.setOnClickListener {
            AnalyticsManager.contactMainCallCenter()
            onCallCenterClick()
        }
        group_qa.setOnClickListener {
            AnalyticsManager.contactMainContactQuestion()
            onQAClick()
        }
        group_line.setOnClickListener {
            AnalyticsManager.contactMainLine()
            onLineClick()
        }
        group_facebook.setOnClickListener {
            AnalyticsManager.contactMainFacebook()
            onFacebookClick()
        }

        btn_tib_detail.setOnClickListener {
            AnalyticsManager.contactMainInsurance()
            onTIBDetailClick()
        }
    }

    private fun onCallCenterClick() {
        CallCenterDialogFragment.show(fragmentManager = fragmentManager!!,
                openBy = CallCenterDialogFragment.CONTACT_US_MAIN)
    }

    private fun onQAClick() {
        QuestionAndAnswerActivity.start(context)
    }

    private fun onLineClick() {
        try {
            ExternalAppUtils.openByLink(activity!!, "")
        } catch (e: Exception) {
            ExternalAppUtils.openByLink(activity!!, "")
            e.printStackTrace()
        }
    }

    private fun onFacebookClick() {
        ExternalAppUtils.openFacebook(context, "")
    }

    private fun onTIBDetailClick() {
        CallCenterDialogFragment.show(
                fragmentManager = fragmentManager!!,
                displayPhoneNumber = "",
                phoneNumber = "",
                openBy = CallCenterDialogFragment.CONTACT_US_MAIN
        )
    }

    companion object {
        fun newInstance() = ContactUsMainFragment()
    }
}
