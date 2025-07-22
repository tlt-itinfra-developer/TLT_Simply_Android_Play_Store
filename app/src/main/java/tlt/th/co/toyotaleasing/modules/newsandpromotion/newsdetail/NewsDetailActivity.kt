package tlt.th.co.toyotaleasing.modules.newsandpromotion.newsdetail

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.util.Linkify
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import kotlinx.android.synthetic.main.activity_news_detail.*
import tlt.th.co.toyotaleasing.R
import tlt.th.co.toyotaleasing.analytics.AnalyticsManager
import tlt.th.co.toyotaleasing.analytics.AnalyticsScreenName
import tlt.th.co.toyotaleasing.common.base.BaseActivity
import tlt.th.co.toyotaleasing.common.dialog.NormalDialogFragment
import tlt.th.co.toyotaleasing.common.extension.loadImageByUrl
import tlt.th.co.toyotaleasing.util.AppUtils


class NewsDetailActivity : BaseActivity() {

    private val viewModel by lazy {
        ViewModelProviders.of(this).get(NewsDetailViewModel::class.java)
    }

    private val idExtra by lazy {
        intent?.getStringExtra(ID_EXTRA) ?: ""
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_news_detail)

        initViewModel()

        viewModel.getData(idExtra)
    }

    override fun onPause() {
        super.onPause()
        AnalyticsManager.trackScreen(AnalyticsScreenName.NEWS_AND_PROMOTION_DETAIL)
    }

    private fun initViewModel() {
        viewModel.whenLoading.observe(this, Observer {
            toggleLoadingScreenDialog(it!!)
        })

        viewModel.whenDataLoaded.observe(this, Observer {
            setContentToView(it!!)
        })

        viewModel.whenUpdateBookmark.observe(this, Observer {
            if (!it!!) {
                status.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_bookmark_unactive))
                NormalDialogFragment.show(
                        fragmentManager = supportFragmentManager,
                        description = getString(R.string.news_and_promotion_bookmark_limit_dialog),
                        confirmButtonMessage = getString(R.string.dialog_button_ok)
                )
            }
        })
    }

    private fun setContentToView(data: NewsDetailViewModel.Model) {
        title_detail.text = data.title.replaceAfter("\n", "")
        detail.text = data.detail
        period.text = data.period
        image.loadImageByUrl(data.image)
        detail.setLinkTextColor(resources.getColor(R.color.blue))
        detail.setText(data.detail)
        Linkify.addLinks(detail, Linkify.WEB_URLS or Linkify.PHONE_NUMBERS)
        if (data.isBooked) {
            status.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_bookmark_active))
        } else {
            status.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_bookmark_unactive))
        }

        status.setOnClickListener {
            AnalyticsManager.newsDetailBookmark()
            viewModel.updateBookmark()
        }

        share.setOnClickListener {
            AnalyticsManager.newsDetailShare()
            AppUtils.shareTextContent(
                    context = this@NewsDetailActivity,
                    text = data.shareContent
            )
        }
    }

    companion object {
        const val ID_EXTRA = "ID_EXTRA"

        fun start(context: Context?, id: String) {
            val intent = Intent(context, NewsDetailActivity::class.java)
            intent.putExtra(ID_EXTRA, id)
            context?.startActivity(intent)
        }
    }
}
