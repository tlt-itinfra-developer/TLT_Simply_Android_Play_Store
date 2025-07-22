package tlt.th.co.toyotaleasing.modules.ownership

import android.Manifest
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener
import kotlinx.android.synthetic.main.activity_ecommerce.*
import kotlinx.android.synthetic.main.activity_ownershiptf_tracking.*
import kotlinx.android.synthetic.main.activity_ownershiptf_tracking.toolbar
import kotlinx.android.synthetic.main.fragment_dialog_ems_copied.view.*
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import tlt.th.co.toyotaleasing.R
import tlt.th.co.toyotaleasing.analytics.AnalyticsManager
import tlt.th.co.toyotaleasing.analytics.AnalyticsScreenName
import tlt.th.co.toyotaleasing.common.base.BaseFragment
import tlt.th.co.toyotaleasing.common.eventbus.ReloadTrackingOwnershipEvent
import tlt.th.co.toyotaleasing.common.extension.gone
import tlt.th.co.toyotaleasing.common.extension.visible
import tlt.th.co.toyotaleasing.common.listener.OnHambergerClickListener
import tlt.th.co.toyotaleasing.manager.BusManager

import tlt.th.co.toyotaleasing.manager.db.MasterDataManager
import tlt.th.co.toyotaleasing.modules.filtercar.FilterCarActivity
import tlt.th.co.toyotaleasing.modules.ownership.common.TOTrackingAdapter
import tlt.th.co.toyotaleasing.modules.tax.TaxWebActivity
import tlt.th.co.toyotaleasing.util.PdfUtils


class OwnershipTransferTrackingFragment : BaseFragment() , TOTrackingAdapter.Listener {

    private var recyclerView: RecyclerView? = null
    private var trackingArrayList: ArrayList<OwnershipTransferTrackingViewModel.StepInfoModel>? = null
    private var adapterTracking: TOTrackingAdapter? = null
     var EMSNO: String? = null
    var TITLE : String = ""
    var URL_LINK  = ""
    private val viewModel by lazy {
        ViewModelProviders.of(this).get(OwnershipTransferTrackingViewModel::class.java)
    }

    private val onHambergerClickListener by lazy {
        context as OnHambergerClickListener
    }


    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.activity_ownershiptf_tracking, container, false)
    }


    override fun onPause() {
        super.onPause()
        AnalyticsManager.trackScreen(AnalyticsScreenName.OWNERSHIP_TRANSFER_TRACKING)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViewModel()
        initInstance()

    }


    private fun initInstance() {

        recyclerView = view!!.findViewById(R.id.ownertf_recyclerView) as RecyclerView
//        viewModel.getOwnershipData()

        toolbar.setOnHambergerMenuClickListener {
            AnalyticsManager.taxMainMainMenu()
            onHambergerClickListener.onHambergerClick()
        }

        toolbar.setOnRightMenuTitleClickListener {
            FilterCarActivity.open(context!!)
        }

    }

    private fun initViewModel() {

        viewModel.whenLoading.observe(this, Observer {
            toggleLoadingScreenDialog(it!!, this)
        })

        viewModel.whenDataLoaded.observe(this, Observer {
            it?.let {
                setupDataIntoViews(it)
            }
        })

        viewModel.whenDataListLoaded.observe(this, Observer {
            it?.let {
                setupDataList(it)
            }
        })

        viewModel.whenDataLoadedFailure.observe(this, Observer {
            Toast.makeText(context, getString(R.string.txt_problem_open_pdf), Toast.LENGTH_SHORT).show()
        })

        viewModel.whenOpenPdf.observe(this, Observer {
            PdfUtils.openPdf(context, it)
        })


    }



    private fun setupDataIntoViews(it: OwnershipTransferTrackingViewModel.Model) {
        try{
            datetime.text  = it.date
            txt_reg_no.text  = it.regNo
            txt_address_to_delivery.text = (it.mAILINGADDRESS).replace("\\n", System.lineSeparator())
            if (it.eMSNO.isNullOrEmpty()) {
                txt_register_number.text = ""
                btn_copy.gone()
                txt_register_number.gone()
            } else {
                EMSNO  = it.eMSNO
                txt_register_number.text = getString(R.string.tax_document_register_number, it.eMSNO)
                btn_copy.visible()
                txt_register_number.visible()
            }
            if(it.uRLEMS != ""){
                txt_thaipost_track_website.visible()
                txt_thaipost_track_website.text = it.uRLEMS
            }else{
                txt_thaipost_track_website.gone()
                txt_thaipost_track_website.text = ""
            }

            var thaiPostUrl = it.thaipostTrackWebsite
            txt_thaipost_track_website.setOnClickListener {
                TaxWebActivity.startExternalWeb(context , thaiPostUrl  )
            }

            btn_copy.setOnClickListener {
                val ems = EMSNO
                val clipboard = activity!!.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager?
                val clip = ClipData.newPlainText("Copied", ems)
                if (clipboard != null && clip != null)
                 clipboard!!.setPrimaryClip(clip!!)

                showCopied()
            }

        }catch(e : Exception) {
            Log.e("setupDataIntoViews" ,e.message.toString() )
        }

    }

    private fun setupDataList(it: ArrayList<OwnershipTransferTrackingViewModel.StepInfoModel>) {
        try {
            trackingArrayList = it
            adapterTracking = TOTrackingAdapter(trackingArrayList!! ,  this@OwnershipTransferTrackingFragment )
            recyclerView?.apply {
                layoutManager = LinearLayoutManager(context)
                adapter = adapterTracking
            }


        }catch(e : Exception) {
            Log.e("setupDataList" ,e.message.toString() )
        }
    }

    private fun showCopied() {
        try {

            val mDialogView = LayoutInflater.from(context).inflate(R.layout.fragment_dialog_ems_copied, null)
            val mBuilder = AlertDialog.Builder(context!!, R.style.dialogBlur).setView(mDialogView)
            val mAlertDialog = mBuilder.show()
            if (mAlertDialog.getWindow() != null) {
                val window = mAlertDialog.getWindow()
                window!!.setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT)
                window!!.setGravity(Gravity.CENTER)
            }
            mDialogView.lay_cpoied.setOnClickListener {
                mAlertDialog.dismiss()
            }

        } catch (e: Exception) {
            e.message
        }
    }


    override fun onDownloadClick(  data: OwnershipTransferTrackingViewModel.StepInfoModel) {
        Dexter.withActivity(activity)
                .withPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .withListener(object : PermissionListener {
                    override fun onPermissionGranted(response: PermissionGrantedResponse) {
                        viewModel.showPDF(data.documentDownload , data.documentName)
                    }
                    override fun onPermissionDenied(response: PermissionDeniedResponse) {

                    }

                    override fun onPermissionRationaleShouldBeShown(permission: PermissionRequest, token: PermissionToken) {
                        token.continuePermissionRequest()
                    }
                }).check()
    }

    @Subscribe(threadMode = ThreadMode.MAIN)

    fun onReloadTrackingOwnership(event: ReloadTrackingOwnershipEvent) {
        viewModel.getOwnershipData()
    }

    override fun onStart() {
        super.onStart()
        BusManager.subscribe(this)
    }

    override fun onStop() {
        BusManager.unsubscribe(this)
        super.onStop()
    }


    companion object {
         fun newInstance() = OwnershipTransferTrackingFragment()
    }


}