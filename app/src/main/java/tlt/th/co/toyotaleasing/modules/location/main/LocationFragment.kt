package tlt.th.co.toyotaleasing.modules.location.main

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
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
import kotlinx.android.synthetic.main.fragment_location.*
import kotlinx.android.synthetic.main.layout_location_filter.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import tlt.th.co.toyotaleasing.R
import tlt.th.co.toyotaleasing.analytics.AnalyticsManager
import tlt.th.co.toyotaleasing.analytics.AnalyticsScreenName
import tlt.th.co.toyotaleasing.common.base.BaseFragment
import tlt.th.co.toyotaleasing.common.dialog.CallCenterDialogFragment
import tlt.th.co.toyotaleasing.common.dialog.NormalDialogFragment
import tlt.th.co.toyotaleasing.common.extension.gone
import tlt.th.co.toyotaleasing.common.extension.ifTrue
import tlt.th.co.toyotaleasing.common.extension.setDrawableEnd
import tlt.th.co.toyotaleasing.common.extension.visible
import tlt.th.co.toyotaleasing.common.listener.OnHambergerClickListener
import tlt.th.co.toyotaleasing.manager.LocationManager
import tlt.th.co.toyotaleasing.modules.home.customer.MainCustomerActivity
import tlt.th.co.toyotaleasing.modules.location.detail.LocationDetailActivity
import tlt.th.co.toyotaleasing.modules.location.filter.LocationFilterActivity
import tlt.th.co.toyotaleasing.modules.location.main.cluster.TLTClusterItem
import tlt.th.co.toyotaleasing.util.ExternalAppUtils
import tlt.th.co.toyotaleasing.view.ToolbarWidget

class LocationFragment : BaseFragment(), OnMapReadyCallback, NormalDialogFragment.Listener {

    private val viewModel by lazy {
        ViewModelProviders.of(this).get(LocationViewModel::class.java)
    }

    private val onHambergerClickListener by lazy {
        context as OnHambergerClickListener
    }

    private val isFromContractDetail by lazy {
        activity?.intent
                ?.getBundleExtra(MainCustomerActivity.DATA_POSITION_EXTRA)
                ?.getBoolean(IS_FROM_CONTRACT_DETAIL) ?: false
    }

    private lateinit var googleMap: GoogleMap
//    private lateinit var clusterManager: ClusterManager<TLTClusterItem>

    private val markers = ArrayList<Marker>()
    private val defaultLatLng = LatLng(13.727685, 100.538855)
    private var currentType = LocationViewModel.Type.OFFICE

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_location, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViewModel()
        initInstances()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == LocationFilterActivity.REQUEST_CODE
                && resultCode == Activity.RESULT_OK) {
            viewModel.getData(LocationViewModel.Type.SHOWROOM)
        }
    }

    private fun initViewModel() {
        viewModel.whenLoading.observe(this, Observer {

        })

        viewModel.whenDataLoaded.observe(this, Observer {
            setupDataIntoViews(it)
        })
    }

    private fun initInstances() {

        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        toolbar.hideTextRightMenu()
        filterHide(true)

        toolbar.setOnHambergerMenuClickListener {
            AnalyticsManager.mapOfficeMenuhome()
            onHambergerClickListener.onHambergerClick()
        }

        toolbar.setOnTitleClickListener {
            AnalyticsManager.mapOfficeSelectType()
            when (layout_office_location_filter.visibility) {
                View.VISIBLE -> filterHide(false)
                else -> filterShow()
            }
        }

        toolbar.setOnRightMenuTitleClickListener {
            AnalyticsManager.mapDealerSearch()
            LocationFilterActivity.startWithResult(this)
        }

        location_map_bottom_sheet_rv.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = LocationAdapter(
                    items = arrayListOf(),
                    listener = onOfficeLocationListener
            )
            addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
        }

        location_map_all_tv.setOnClickListener {
            val bottomSheetBehavior = com.google.android.material.bottomsheet.BottomSheetBehavior.from(layout_map_bottom_sheet_view)

            if (bottomSheetBehavior?.state == com.google.android.material.bottomsheet.BottomSheetBehavior.STATE_COLLAPSED) {
                AnalyticsManager.mapOfficeAllList()
                map_bottom_sheet_bg.visible()
                location_map_all_tv.setDrawableEnd(R.drawable.ic_arrow_down)
                location_map_all_tv.text = activity!!.getString(R.string.location_hide)
                bottomSheetBehavior.state = com.google.android.material.bottomsheet.BottomSheetBehavior.STATE_EXPANDED
                filterHide(true)
            } else {
                AnalyticsManager.mapOfficeHideList()
                map_bottom_sheet_bg.gone()
                location_map_all_tv.setDrawableEnd(R.drawable.ic_arrow_up)
                location_map_all_tv.text = activity!!.getString(R.string.location_show_all)
                bottomSheetBehavior?.state = com.google.android.material.bottomsheet.BottomSheetBehavior.STATE_COLLAPSED
            }
        }

        btn_office_tlt.setOnClickListener {
            officeLocationSelected()
            viewModel.getData(LocationViewModel.Type.OFFICE)
        }

        btn_dealer_tlt.setOnClickListener {
            AnalyticsManager.mapOfficeMapDealer()
            dealerLocationSelected()
            viewModel.getData(LocationViewModel.Type.NEARBY)
        }

        btn_search.setOnClickListener {
            AnalyticsManager.mapSearchBackToSearch()
            LocationFilterActivity.startWithResult(this)
        }
    }

    private fun officeLocationSelected() {
        toolbar.setTitle(getString(R.string.location_hq_filter))
        btn_dealer_tlt.setTextColor(ContextCompat.getColor(context!!, R.color.black))
        btn_office_tlt.setTextColor(ContextCompat.getColor(context!!, R.color.cherry_red))
        toolbar.hideTextRightMenu()
        filterHide(true)
    }

    private fun dealerLocationSelected() {
        toolbar.setTitle(getString(R.string.location_dealer_filter))
        btn_dealer_tlt.setTextColor(ContextCompat.getColor(context!!, R.color.cherry_red))
        btn_office_tlt.setTextColor(ContextCompat.getColor(context!!, R.color.black))
        toolbar.showTextRightMenu()
        filterHide(true)
    }

    private fun setupDataIntoViews(model: LocationViewModel.Model?) {
        if (model?.locationList?.isEmpty() == true) {
            AnalyticsManager.trackScreen(AnalyticsScreenName.MAP_DEALER_NO_SEARCH)
            group_normal.gone()
            group_not_found.visible()
            return
        }

        group_normal.visible()
        group_not_found.gone()

        val items = model?.locationList ?: listOf()
        val adapter = location_map_bottom_sheet_rv.adapter as LocationAdapter

        moveCamera(LatLng(items[0].lat.toDouble(), items[0].lng.toDouble()))

        adapter.update(items)
        addMarker(items)

        val title = when (model?.locationType) {
            LocationViewModel.Type.OFFICE -> {
                getString(R.string.location_office_title, items.size.toString())
            }
            else -> {
                getString(R.string.location_showroom_title, items.size.toString())
            }
        }

        currentType = when (model?.locationType) {
            LocationViewModel.Type.OFFICE -> LocationViewModel.Type.OFFICE
            else -> LocationViewModel.Type.SHOWROOM
        }

        location_map_company_tv.text = title
    }

    @SuppressLint("MissingPermission")
    override fun onMapReady(googleMap: GoogleMap?) {
        this.googleMap = googleMap!!
        this.googleMap.setOnMarkerClickListener(onMarkerClickListener)

//        clusterManager = ClusterManager(context, googleMap)
//        clusterManager.renderer = TLTClusterRenderer(context, googleMap, clusterManager)
//        clusterManager.setOnClusterItemClickListener(onClusterItemClickListener)

//        this.googleMap.setOnCameraIdleListener(clusterManager)
//        this.googleMap.setOnMarkerClickListener(clusterManager)
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

            if (isFromContractDetail) {
                dealerLocationSelected()
                viewModel.getDataFromContractDetail()
            } else {
                viewModel.getData(LocationViewModel.Type.OFFICE)
            }
        }, failure = {
            moveCamera(defaultLatLng)
            if (isFromContractDetail) {
                dealerLocationSelected()
                viewModel.getDataFromContractDetail()
            } else {
                viewModel.getData(LocationViewModel.Type.OFFICE)
            }
        })
    }

    private val onOfficeLocationListener = object : LocationAdapter.Listener {
        override fun onDetailClick(index: Int, item: LocationViewModel.Location) {
            AnalyticsManager.mapDealerDetail()
            viewModel.saveLocationDetail(item)
            LocationDetailActivity.start(context)
        }

        override fun onTelClick(index: Int, item: LocationViewModel.Location) {
            AnalyticsManager.mapDealerCall()
            CallCenterDialogFragment.show(fragmentManager = fragmentManager!!,
                    displayPhoneNumber = item.telForCall,
                    phoneNumber = item.tel,
                    openBy = CallCenterDialogFragment.LOCATION)
        }

        override fun onDirectionClick(index: Int, item: LocationViewModel.Location) {
            val lat = item.lat.toDouble()
            val lng = item.lng.toDouble()

            AnalyticsManager.mapOfficeRoute()
            ExternalAppUtils.openGoogleDirection(context, lat, lng)
        }
    }

    private val onMarkerClickListener = GoogleMap.OnMarkerClickListener {
        when (currentType) {
            LocationViewModel.Type.OFFICE -> AnalyticsManager.mapOfficeMapPin(it.title)
            else -> AnalyticsManager.mapDealerPin(it.title)
        }
        moveCamera(it.position)
        val index = it.tag as Int
        viewModel.onLocationClick(index)
        true
    }

    private val onClusterItemClickListener = ClusterManager.OnClusterItemClickListener<TLTClusterItem> { item ->
        moveCamera(item!!.position)
        val index = item.getIndex()
        viewModel.onLocationClick(index)
        true
    }

    private fun moveCameraToCurrentLocation() {
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

    private fun moveCamera(latLng: LatLng) {
        GlobalScope.launch(Dispatchers.Main) {
            if (currentType == LocationViewModel.Type.OFFICE) {
                googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 8f))
            } else {
                googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 13f))
            }
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

    private fun addMarker(locationList: List<LocationViewModel.Location>) {
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

//        googleMap.clear()
//        clusterManager.clearItems()
    }

    @SuppressLint("ServiceCast")
    private fun checkLocationIsOn() {
        val locationManager = activity!!.getSystemService(Context.LOCATION_SERVICE) as android.location.LocationManager
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
                    fragmentManager = fragmentManager!!,
                    fragment = this,
                    description = getString(R.string.location_switch_on_location_title),
                    confirmButtonMessage = getString(R.string.dialog_button_ok),
                    cancelButtonMessage = getString(R.string.callcenter_dialog_btn_cancel)
            )
        }
    }

    override fun onDialogConfirmClick() {
        AnalyticsManager.mapLocationSettingOk()
        val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
        activity!!.startActivity(intent)
    }

    override fun onDialogCancelClick() {
        AnalyticsManager.mapLocationSettingCancel()
    }

    private fun checkPermission(success: () -> Unit, failure: () -> Unit) {
        Dexter.withActivity(activity)
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

    private fun filterHide(isCollapsed: Boolean) {
        layout_office_location_filter.gone()

        toolbar.setTitle(
                toolbar.getTitle(),
                R.drawable.ic_caretdown,
                ToolbarWidget.IconDrawableDirection.RIGHT
        )

        isCollapsed.ifTrue {
            val bottomSheetBehavior = com.google.android.material.bottomsheet.BottomSheetBehavior.from(layout_map_bottom_sheet_view)

            if (bottomSheetBehavior?.state == com.google.android.material.bottomsheet.BottomSheetBehavior.STATE_EXPANDED) {
                map_bottom_sheet_bg.gone()
                location_map_all_tv.setDrawableEnd(R.drawable.ic_arrow_up)
                location_map_all_tv.text = activity!!.getString(R.string.location_show_all)
                bottomSheetBehavior.state = com.google.android.material.bottomsheet.BottomSheetBehavior.STATE_COLLAPSED
            }
        }
    }

    companion object {

        const val IS_FROM_CONTRACT_DETAIL = "is_from_contract_detail"

        fun newInstance() = LocationFragment()

        fun startByDeeplink(context: Context?,
                            fromContractDetail: Boolean) {
            MainCustomerActivity.startWithClearStack(
                    context = context,
                    position = MainCustomerActivity.OFFICE_LOCATION_MENU_POSITION,
                    data = Bundle().apply {
                        putBoolean(IS_FROM_CONTRACT_DETAIL, fromContractDetail)
                    }
            )
        }
    }
}