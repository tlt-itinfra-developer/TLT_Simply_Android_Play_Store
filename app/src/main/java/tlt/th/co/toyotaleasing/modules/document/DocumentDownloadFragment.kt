package tlt.th.co.toyotaleasing.modules.document

import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_document_download.*
import tlt.th.co.toyotaleasing.R
import tlt.th.co.toyotaleasing.analytics.AnalyticsManager
import tlt.th.co.toyotaleasing.analytics.AnalyticsScreenName
import tlt.th.co.toyotaleasing.common.base.BaseFragment
import tlt.th.co.toyotaleasing.common.extension.ifTrue
import tlt.th.co.toyotaleasing.common.listener.OnHambergerClickListener
import tlt.th.co.toyotaleasing.modules.web.WebActivity
import tlt.th.co.toyotaleasing.util.ExternalAppUtils
import tlt.th.co.toyotaleasing.view.DividerItemDecoration

class DocumentDownloadFragment : BaseFragment(), DocumentDownloadAdapter.Listener {

    private val viewModel by lazy {
        ViewModelProviders.of(this).get(DocumentDownloadViewModel::class.java)
    }

    private val onHambergerClickListener by lazy {
        context as OnHambergerClickListener
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_document_download, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViewModel()
        initInstances()

        viewModel.getData()
    }

    private fun initViewModel() {
        viewModel.whenDataLoaded.observe(this, Observer {
            setupDataIntoViews(it)
            it?.isStaffApp?.ifTrue { supportForStaff(it) }
        })
    }

    private fun setupDataIntoViews(it: DocumentDownloadViewModel.Model?) {
        toolbar.setOnHambergerMenuClickListener {
            AnalyticsManager.downloadDocumentMenuHome()
            onHambergerClickListener.onHambergerClick()
        }

        recycler_view.apply {
            layoutManager = LinearLayoutManager(context)
            addItemDecoration(DividerItemDecoration(ContextCompat.getDrawable(context, R.drawable.item_divider_recycler_view)))
            adapter = DocumentDownloadAdapter(it?.documents
                    ?: listOf(), this@DocumentDownloadFragment)
        }
    }

    private fun initInstances() {

    }

    private fun supportForStaff(it: DocumentDownloadViewModel.Model) {

    }

    override fun onDocumentClick(index: Int,
                                 item: DocumentDownloadViewModel.Document) {
        AnalyticsManager.downloadDocumentName()
        if(item.fileUrl.contains("url=")){
            WebActivity.start(
                    context,
                    getString(R.string.document_viewer_title),
                    item.fileUrl,
                    true
            )
        }else {
            ExternalAppUtils.openByLink(context, item.fileUrl)
        }
    }

    companion object {
        fun newInstance() = DocumentDownloadFragment()
    }
}
