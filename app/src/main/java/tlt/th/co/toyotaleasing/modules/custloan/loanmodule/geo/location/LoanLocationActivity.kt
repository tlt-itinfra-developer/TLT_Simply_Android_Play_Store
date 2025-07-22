package tlt.th.co.toyotaleasing.modules.custloan.loanmodule.geo.location

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.provider.Settings
import com.google.android.material.bottomsheet.BottomSheetBehavior
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import android.view.View
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.maps.android.clustering.ClusterManager
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import kotlinx.android.synthetic.main.bottom_sheet_location_map.*
import kotlinx.android.synthetic.main.fragment_loan_location.*
import kotlinx.android.synthetic.main.layout_location_filter.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import tlt.th.co.toyotaleasing.R
import tlt.th.co.toyotaleasing.analytics.AnalyticsManager
import tlt.th.co.toyotaleasing.analytics.AnalyticsScreenName
import tlt.th.co.toyotaleasing.common.base.BaseActivity
import tlt.th.co.toyotaleasing.common.dialog.NormalDialogFragment
import tlt.th.co.toyotaleasing.common.extension.gone
import tlt.th.co.toyotaleasing.common.extension.setDrawableEnd
import tlt.th.co.toyotaleasing.common.extension.visible
import tlt.th.co.toyotaleasing.manager.LocationManager
import tlt.th.co.toyotaleasing.modules.custloan.loanmodule.cstatus.SummaryLoanActivity
import tlt.th.co.toyotaleasing.modules.custloan.loanmodule.cstatus.VerifyandConfirmActivity
import tlt.th.co.toyotaleasing.modules.custloan.loanmodule.geo.location.filter.LoanLocationFilterActivity
import tlt.th.co.toyotaleasing.modules.location.main.cluster.TLTClusterItem
import tlt.th.co.toyotaleasing.util.ExternalAppUtils
import tlt.th.co.toyotaleasing.view.ToolbarWidget

class LoanLocationActivity :  BaseActivity() , OnMapReadyCallback, NormalDialogFragment.Listener {

    private val viewModel by lazy {
        ViewModelProviders.of(this).get(LoanLocationViewModel::class.java)
    }

    private val data_extra by lazy {
        intent?.getStringExtra(REF_ID) ?: ""
    }

    private val data_url by lazy {
        intent?.getStringExtra(REF_URL) ?: ""
    }

    private val data_tag by lazy {
        intent?.getStringExtra(DATA_BY) ?: ""
    }

    private val offer_showcode by lazy {
        intent?.getStringExtra(OFFER_SHOWCODE) ?: ""
    }

    private val offer_dealcode by lazy {
        intent?.getStringExtra(OFFER_DEALCODE) ?: ""
    }



    private lateinit var googleMap: GoogleMap
//    private lateinit var clusterManager: ClusterManager<TLTClusterItem>

    private val markers = ArrayList<Marker>()
    private val defaultLatLng = LatLng(13.727685, 100.538855)
    private var currentType = LoanLocationViewModel.Type.SHOWROOM

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_loan_location)
        AnalyticsManager.trackScreen(AnalyticsScreenName.ONLINE_DEALER)
        initViewModel()
        initInstances()

//        viewModel.getData(LoanLocationViewModel.Type.SHOWROOM)
    }

//    override fun onSupportVisible() {
//        super.onSupportVisible()
//        checkLocationIsOn()
//        updateMap()
//    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == LoanLocationFilterActivity.REQUEST_CODE
                && resultCode == Activity.RESULT_OK) {
            viewModel.getData(LoanLocationViewModel.Type.SHOWROOM )
        }
    }

    private fun initViewModel() {

        viewModel.whenLoading.observe(this, Observer {
            toggleLoadingScreenDialog(it!!)
        })

        viewModel.whenDataLoaded.observe(this, Observer {
            setupDataIntoViews(it)
        })
    }

    private fun initInstances() {

        val mapFragment = getSupportFragmentManager().findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        toolbar.hideTextRightMenu()
//        filterHide(true)
        checkLocationIsOn()

        layout_office_location_filter.visibility = View.GONE
        toolbar.showTextRightMenu()
//        toolbar.setOnTitleClickListener {
//            when (layout_office_location_filter.visibility) {
//                View.VISIBLE -> filterHide(false)
//                else -> filterShow()
//            }
//        }



        toolbar.setOnRightMenuTitleClickListener {
            LoanLocationFilterActivity.startWithResult(this@LoanLocationActivity)
        }

        location_map_bottom_sheet_rv.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = LoanLocationAdapter(
                    items = arrayListOf(),
                    listener = onShowroomLocationListener
            )
            addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
        }

        location_map_all_tv.setOnClickListener {
            val bottomSheetBehavior = com.google.android.material.bottomsheet.BottomSheetBehavior.from(layout_map_bottom_sheet_view)

            if (bottomSheetBehavior?.state == com.google.android.material.bottomsheet.BottomSheetBehavior.STATE_COLLAPSED) {
                map_bottom_sheet_bg.visible()
                location_map_all_tv.setDrawableEnd(R.drawable.ic_arrow_down)
                location_map_all_tv.text = getString(R.string.location_hide)
                bottomSheetBehavior.state = com.google.android.material.bottomsheet.BottomSheetBehavior.STATE_EXPANDED
//                filterHide(true)
            } else {
                map_bottom_sheet_bg.gone()
                location_map_all_tv.setDrawableEnd(R.drawable.ic_arrow_up)
                location_map_all_tv.text = getString(R.string.location_show_all)
                bottomSheetBehavior?.state = com.google.android.material.bottomsheet.BottomSheetBehavior.STATE_COLLAPSED
            }
        }

//        btn_office_tlt.setOnClickListener {
//            officeLocationSelected()
//            viewModel.getData( LoanLocationViewModel.Type.OFFICE)
//        }

        btn_dealer_tlt.setOnClickListener {
//            dealerLocationSelected()
            viewModel.getData(LoanLocationViewModel.Type.NEARBY)
        }

        btn_search.setOnClickListener {
            LoanLocationFilterActivity.startWithResult(this@LoanLocationActivity)
        }
    }

//    private fun officeLocationSelected() {
//        toolbar.setTitle(getString(R.string.location_hq_filter))
//        btn_dealer_tlt.setTextColor(ContextCompat.getColor(this@LoanLocationActivity, R.color.black))
//        btn_office_tlt.setTextColor(ContextCompat.getColor(this@LoanLocationActivity, R.color.cherry_red))
//        toolbar.hideTextRightMenu()
//        filterHide(true)
//    }

//    private fun dealerLocationSelected() {
//        toolbar.setTitle(getString(R.string.location_loan_showroom_filter))
//        btn_dealer_tlt.setTextColor(ContextCompat.getColor(this@LoanLocationActivity, R.color.cherry_red))
//        btn_office_tlt.setTextColor(ContextCompat.getColor(this@LoanLocationActivity, R.color.black))
//        toolbar.showTextRightMenu()
//        filterHide(true)
//    }

    private fun setupDataIntoViews(model: LoanLocationViewModel.Model?) {
        if (model?.locationList?.isEmpty() == true) {
            group_normal.gone()
            group_not_found.visible()
            return
        }

        group_normal.visible()
        group_not_found.gone()

        val items = model?.locationList ?: listOf()
        val adapter = location_map_bottom_sheet_rv.adapter as LoanLocationAdapter

        moveCamera(LatLng(items[0].lat.toDouble(), items[0].lng.toDouble()))

        adapter.update(items)
        addMarker(items)

        val title =  getString(R.string.location_loan_showroom_title, items.size.toString())
        currentType =  LoanLocationViewModel.Type.SHOWROOM
        location_map_company_tv.text = title
    }

    @SuppressLint("MissingPermission")
    override fun onMapReady(googleMap: GoogleMap?) {
        this.googleMap = googleMap!!
        this.googleMap.setOnMarkerClickListener(onMarkerClickListener)

        updateMap()
    }

    private fun updateMap() {
        viewModel.whenDataLoaded.value?.let { return }
        checkPermission(success = {
            googleMap.isMyLocationEnabled = true
            googleMap.setOnMyLocationButtonClickListener {
                moveCameraToCurrentLocation()
                true
            }
            moveCameraToCurrentLocation()

            viewModel.getData(LoanLocationViewModel.Type.NEARBY  ,  offer_dealcode , offer_showcode , default = true)

        }, failure = {
            moveCamera(defaultLatLng)

            viewModel.getData(LoanLocationViewModel.Type.NEARBY )

        })
    }

     val onShowroomLocationListener = object : LoanLocationAdapter.Listener {
//        override fun onDetailClick(index: Int, item: LoanLocationViewModel.Location) {
//            viewModel.saveLocationDetail(item)
//            LoanLocationDetailActivity.start(this@LoanLocationActivity)
//        }
//
//        override fun onTelClick(index: Int, item:  LoanLocationViewModel.Location) {
//            CallCenterDialogFragment.show(fragmentManager = supportFragmentManager,
//                    displayPhoneNumber = item.telForCall,
//                    phoneNumber = item.tel,
//                    openBy = CallCenterDialogFragment.LOCATION)
//        }

         override fun onSelectClick(index: Int, item: LoanLocationViewModel.Location) {
              AnalyticsManager.onlineDealerAdd()
             if(!data_tag.equals("CHANGE")) {
                 viewModel.updateShowroomLocationDetail(item)
                 VerifyandConfirmActivity.start(this@LoanLocationActivity, data_extra, data_url)
             }else{
                 SummaryLoanActivity.openWithData(this@LoanLocationActivity, data_extra, item.code , item.title)
             }
         }

        override fun onDirectionClick(index: Int, item: LoanLocationViewModel.Location) {
            val lat = item.lat.toDouble()
            val lng = item.lng.toDouble()
            ExternalAppUtils.openGoogleDirection(this@LoanLocationActivity, lat, lng)
        }
    }

     val onMarkerClickListener = GoogleMap.OnMarkerClickListener {
        moveCamera(it.position)
        val index = it.tag as Int
        viewModel.onLocationClick(index)
        true
    }

     val onClusterItemClickListener = ClusterManager.OnClusterItemClickListener<TLTClusterItem> { item ->
        moveCamera(item!!.position)
        val index = item.getIndex()
        viewModel.onLocationClick(index)
        true
    }

     fun moveCameraToCurrentLocation() {
        GlobalScope.launch(Dispatchers.Main) {
            try {
                val location = LocationManager.getLastKnowLocation()
                val latLng = LatLng(location.latitude, location.longitude)
                moveCamera(latLng)
            } catch (error: Exception) {
                error.printStackTrace()
            }
        }
    }

     fun moveCamera(latLng: LatLng) {
        GlobalScope.launch(Dispatchers.Main) {
                googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 13f))
        }
    }

    private fun selectedMarker(position: Int): Bitmap {
        val normalWidth = 40
        val normalHeight = 63
        val selectedWidth = 80
        val selectedHeight = 126

        val bitmapDrawable = resources.getDrawable(R.drawable.ic_pincar) as BitmapDrawable
        val bitmap = bitmapDrawable.bitmap
        return if (position == 0) {
            Bitmap.createScaledBitmap(bitmap, selectedWidth, selectedHeight, false)
        } else {
            Bitmap.createScaledBitmap(bitmap, normalWidth, normalHeight, false)
        }
    }

    private fun addMarker(locationList: List<LoanLocationViewModel.Location>) {
        GlobalScope.launch(Dispatchers.Main) {
            clearMarkers()

            locationList.forEachIndexed { index, location ->
                val latLng = LatLng(location.lat.toDouble(), location.lng.toDouble())

                val marker = googleMap.addMarker(MarkerOptions()
                        .position(latLng)
                        .title(location.title)
                        .icon(BitmapDescriptorFactory.fromBitmap(selectedMarker(index))))

                marker.tag = index

                markers.add(marker)

//                val locationMarker = TLTClusterItem(
//                        lat = location.lat.toDouble(),
//                        lng = location.lng.toDouble(),
//                        title = location.title,
//                        snippet = location.title,
//                        index = index
//                )

//                clusterManager.addItem(locationMarker)
            }

//            clusterManager.cluster()
        }
    }

    private fun clearMarkers() {
        markers.forEach { it.remove() }
        markers.clear()

    }

    @SuppressLint("ServiceCast")
    private fun checkLocationIsOn() {
        val locationManager = this@LoanLocationActivity.getSystemService(Context.LOCATION_SERVICE) as android.location.LocationManager
        var gps_enabled = false
        var network_enabled = false

        try {
            gps_enabled = locationManager.isProviderEnabled(android.location.LocationManager.GPS_PROVIDER)
        } catch (exception: Exception) {

        }

        try {
            network_enabled = locationManager.isProviderEnabled(android.location.LocationManager.NETWORK_PROVIDER)
        } catch (exception: Exception) {

        }

        if (!gps_enabled && !network_enabled) {
            NormalDialogFragment.show(
                    fragmentManager = supportFragmentManager,
                    description = getString(R.string.location_loan_switch_on_location_title),
                    confirmButtonMessage = getString(R.string.dialog_button_ok),
                    cancelButtonMessage = getString(R.string.callcenter_dialog_btn_cancel)
            )
        }
    }

    override fun onDialogConfirmClick() {
        val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
        this@LoanLocationActivity.startActivity(intent)
    }

    override fun onDialogCancelClick() {
    }

    private fun checkPermission(success: () -> Unit, failure: () -> Unit) {
        Dexter.withActivity(this@LoanLocationActivity)
                .withPermissions(
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.ACCESS_FINE_LOCATION
                ).withListener(object : MultiplePermissionsListener {
                    override fun onPermissionsChecked(report: MultiplePermissionsReport) {
                        if (report.areAllPermissionsGranted()) {
                            success.invoke()
                            return
                        }

                        failure.invoke()
                    }

                    override fun onPermissionRationaleShouldBeShown(permissions: List<PermissionRequest>, token: PermissionToken) {
                        token.continuePermissionRequest()
                    }
                }).check()
    }

    private fun filterShow() {
        layout_office_location_filter.visible()


        toolbar.setTitle(
                toolbar.getTitle(),
                R.drawable.ic_caretup,
                ToolbarWidget.IconDrawableDirection.RIGHT
        )
    }

//    private fun filterHide(isCollapsed: Boolean) {
//        layout_office_location_filter.gone()
//
//        toolbar.setTitle(
//                toolbar.getTitle(),
//                R.drawable.ic_caretdown,
//                ToolbarWidget.IconDrawableDirection.RIGHT
//        )
//
//        isCollapsed.ifTrue {
//            val bottomSheetBehavior = BottomSheetBehavior.from(layout_map_bottom_sheet_view)
//
//            if (bottomSheetBehavior?.state == BottomSheetBehavior.STATE_EXPANDED) {
//                map_bottom_sheet_bg.gone()
//                location_map_all_tv.setDrawableEnd(R.drawable.ic_arrow_up)
//                location_map_all_tv.text = this@LoanLocationActivity.getString(R.string.location_loan_show_all)
//                bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
//            }
//        }
//    }

    companion object {

        const val REF_ID = "REF_ID"
        const val REF_URL = "REF_URL"
        const val DATA_BY = "DATA_BY"
        const val OFFER_DEALCODE = "OFFER_DEALCODE"
        const val OFFER_SHOWCODE = "OFFER_SHOWCODE"

        fun open(activity: Activity?, data: String , offerdealercode : String , offershowcode : String) {
            val intent = Intent(activity, LoanLocationActivity::class.java)
            intent.putExtra(REF_ID, data)
            intent.putExtra(OFFER_DEALCODE , offerdealercode )
            intent.putExtra(OFFER_SHOWCODE , offershowcode )
            activity!!.startActivity(intent)
        }

        fun openForChange(activity: Activity?, data: String , tag : String ) {
            val intent = Intent(activity, LoanLocationActivity::class.java)
            intent.putExtra(REF_ID, data)
            intent.putExtra(DATA_BY, tag)
            activity!!.startActivity(intent)
        }
    }


}