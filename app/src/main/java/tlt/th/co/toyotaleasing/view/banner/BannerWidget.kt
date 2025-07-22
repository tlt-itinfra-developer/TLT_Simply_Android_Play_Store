package tlt.th.co.toyotaleasing.view.banner

import android.annotation.TargetApi
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import kotlinx.android.synthetic.main.widget_banner.view.*
import tlt.th.co.toyotaleasing.R
import tlt.th.co.toyotaleasing.analytics.AnalyticsManager
import java.util.*

class BannerWidget : FrameLayout, BannerAdapter.Listener {

    @JvmOverloads
    constructor(
            context: Context,
            attrs: AttributeSet? = null,
            defStyleAttr: Int = 0)
            : super(context, attrs, defStyleAttr) {
        init(attrs)
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    constructor(
            context: Context,
            attrs: AttributeSet?,
            defStyleAttr: Int,
            defStyleRes: Int)
            : super(context, attrs, defStyleAttr, defStyleRes) {
        init(attrs)
    }

    private fun init(attrs: AttributeSet?) {
        LayoutInflater.from(context).inflate(R.layout.widget_banner, this, true)

        attrs?.let {

        }

        val snapHelper = LinearSnapHelper()
        snapHelper.attachToRecyclerView(recycler_view_banner)
        recycler_view_banner.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        recycler_view_banner.adapter = BannerAdapter(ArrayList(), this)

        recycler_view_banner.addOnPageChangedListener { p0, p1 -> AnalyticsManager.homeBannerSlide() }

        //indicator.attachToRecyclerView(recycler_view_banner)
    }


    override fun onBannerClicked(bannerName: String, bannerDescription: String) {
        AnalyticsManager.homeBannerClicked(bannerName)
        if (bannerDescription.isNotEmpty()) {
            openNewTabWindow(bannerDescription, context)
        }
    }

    fun setBanners(item: List<Banner>) {
        val adapter = recycler_view_banner.adapter as BannerAdapter
        adapter.updateItems(item)
    }

    private fun openNewTabWindow(urls: String, context: Context) {
        val uris = Uri.parse(urls)
        val intents = Intent(Intent.ACTION_VIEW, uris)
        context.startActivity(intents)
    }
}