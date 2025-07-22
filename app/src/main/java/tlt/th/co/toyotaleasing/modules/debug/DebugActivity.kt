package tlt.th.co.toyotaleasing.modules.debug

import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_debug.*
import tlt.th.co.toyotaleasing.R
import tlt.th.co.toyotaleasing.common.base.BaseActivity
import tlt.th.co.toyotaleasing.modules.main.MainActivity

class DebugActivity : BaseActivity() {

    private val viewModel by lazy {
        ViewModelProviders.of(this).get(DebugViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_debug)

        initViewModel()

        viewModel.getData()
    }

    private fun initViewModel() {
        viewModel.whenDataLoaded.observe(this, Observer {
            setupDataIntoViews(it)
        })

        viewModel.whenProfileChanged.observe(this, Observer {
            MainActivity.openWithClearStack(this)
        })
    }

    private fun setupDataIntoViews(it: DebugViewModel.Model?) {
        recycler_view.apply {
            layoutManager = LinearLayoutManager(this@DebugActivity)
            adapter = DebugAdapter(it?.passportList ?: listOf(), onItemClickListener)
        }
    }

    private val onItemClickListener = object : DebugAdapter.OnInteractionListener {
        override fun onClick(position: Int) {
            viewModel.passportChange(position)
        }
    }

    companion object {
        fun start(context: Context) {
            val intent = Intent(context, DebugActivity::class.java)
            context.startActivity(intent)
        }
    }
}
