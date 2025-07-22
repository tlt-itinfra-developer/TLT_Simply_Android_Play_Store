package tlt.th.co.toyotaleasing.modules.faq.detail

import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_faq_detail.*
import tlt.th.co.toyotaleasing.R
import tlt.th.co.toyotaleasing.analytics.AnalyticsManager
import tlt.th.co.toyotaleasing.common.base.BaseActivity
import tlt.th.co.toyotaleasing.common.extension.ifTrue
import tlt.th.co.toyotaleasing.view.DividerItemDecoration

class FAQDetailActivity : BaseActivity(), FAQDetailAdapter.Listener {

    private val viewModel by lazy {
        ViewModelProviders.of(this).get(FAQDetailViewModel::class.java)
    }

    private val topicId by lazy {
        intent?.getStringExtra(TOPIC_ID_EXTRA) ?: ""
    }

    private val topicName by lazy {
        intent?.getStringExtra(TOPIC_NAME_EXTRA) ?: ""
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_faq_detail)

        initViewModel()
        initInstances()

        viewModel.getData(topicId)
    }

    private fun initViewModel() {
        txt_topic.text = topicName
        viewModel.whenDataLoaded.observe(this, Observer {
            it?.let {
                setupDataIntoViews(it)
                it.isStaffApp.ifTrue { supportForStaff(it) }
            }
        })
    }

    override fun onPause() {
        super.onPause()

    }

    private fun initInstances() {

    }

    private fun setupDataIntoViews(it: FAQDetailViewModel.Model?) {
        recycler_view.apply {
            layoutManager = LinearLayoutManager(context)
            addItemDecoration(DividerItemDecoration(ContextCompat.getDrawable(context, R.drawable.item_divider_recycler_view)))
            adapter = FAQDetailAdapter(it?.items
                    ?: listOf(), this@FAQDetailActivity)
        }
    }

    private fun supportForStaff(it: FAQDetailViewModel.Model) {

    }

    override fun onTopicClick(index: Int, item: FAQDetailViewModel.Item) {
        AnalyticsManager.faqDetailQuestion()
    }

    companion object {
        const val TOPIC_ID_EXTRA = "TOPIC_ID_EXTRA"
        const val TOPIC_NAME_EXTRA = "TOPIC_NAME_EXTRA"

        fun start(context: Context?, topicId: String) {
            val intent = Intent(context, FAQDetailActivity::class.java)
            intent.putExtra(TOPIC_ID_EXTRA, topicId)
            context?.startActivity(intent)
        }

        fun start(context: Context?, topicId: String, topicName: String) {
            val intent = Intent(context, FAQDetailActivity::class.java)
            intent.putExtra(TOPIC_ID_EXTRA, topicId)
            intent.putExtra(TOPIC_NAME_EXTRA, topicName)
            context?.startActivity(intent)
        }
    }
}
