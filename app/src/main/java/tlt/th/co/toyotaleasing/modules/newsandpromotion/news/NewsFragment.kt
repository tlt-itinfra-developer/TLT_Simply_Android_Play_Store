package tlt.th.co.toyotaleasing.modules.newsandpromotion.news

import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_news.*
import tlt.th.co.toyotaleasing.R
import tlt.th.co.toyotaleasing.analytics.AnalyticsManager
import tlt.th.co.toyotaleasing.analytics.AnalyticsScreenName
import tlt.th.co.toyotaleasing.common.base.BaseFragment
import tlt.th.co.toyotaleasing.common.dialog.NormalDialogFragment
import tlt.th.co.toyotaleasing.modules.newsandpromotion.newsdetail.NewsDetailActivity

class NewsFragment : BaseFragment(), NewsAdapter.Listener {

    private val viewModel by lazy {
        ViewModelProviders.of(this).get(NewsViewModel::class.java)
    }

    private var currentPosition: Int = 0

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_news, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViewModel()
        initInstance()

        viewModel.getData()
    }

    private fun initInstance() {
        recycler_view.layoutManager = LinearLayoutManager(context)
        recycler_view.adapter = NewsAdapter(listener = this)

        swipe_refresh.setOnRefreshListener {
            swipe_refresh.isRefreshing = false
            viewModel.getData()
        }
    }

    private fun initViewModel() {
        viewModel.whenDataLoaded.observe(this, Observer {
            val adapter = recycler_view.adapter as NewsAdapter
            adapter.updateItems(it?.newsList ?: listOf())
        })

        viewModel.whenUpdateBookmark.observe(this, Observer {
            if (!it!!) {
                NormalDialogFragment.show(
                        fragmentManager = fragmentManager!!,
                        description = getString(R.string.news_and_promotion_bookmark_limit_dialog),
                        confirmButtonMessage = getString(R.string.dialog_button_ok)
                )
                val adapter = recycler_view.adapter as NewsAdapter
                adapter.bookmarkUnsuccessful(currentPosition)
            }
        })
    }

    override fun onNewsClick(position: Int, item: News) {
        AnalyticsManager.newsAndPromotionListNewsPromotionDetail()
        NewsDetailActivity.start(context, item.id)
    }

    override fun onBookmarkClick(position: Int, item: News) {
        AnalyticsManager.newsAndPromotionListBookmark()
        currentPosition = position
        viewModel.sendBookmark(item, position)
    }

    companion object {
        fun newInstance() = NewsFragment()
    }
}