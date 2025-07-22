package tlt.th.co.toyotaleasing.modules.custloan.loanmodule.geo

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.FragmentActivity
import androidx.core.content.ContextCompat
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.widget.TextView
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import kotlinx.android.synthetic.main.fragment_geo_location.*
import kotlinx.android.synthetic.main.widget_toolbar.view.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.IOException
import java.util.*
import tlt.th.co.toyotaleasing.R
import tlt.th.co.toyotaleasing.analytics.AnalyticsManager
import tlt.th.co.toyotaleasing.analytics.AnalyticsScreenName
import tlt.th.co.toyotaleasing.common.dialog.MsgdescNormalDialog
import tlt.th.co.toyotaleasing.common.dialog.NormalDialogFragment
import tlt.th.co.toyotaleasing.manager.LocalizeManager
import tlt.th.co.toyotaleasing.manager.LocationManager
import tlt.th.co.toyotaleasing.model.entity.LoanDataManager
import tlt.th.co.toyotaleasing.modules.custloan.loanmodule.basicdetail.BasicInformationActivity
import tlt.th.co.toyotaleasing.modules.custloan.loanmodule.carloan.LoanPersonalInfomationActivity
import tlt.th.co.toyotaleasing.modules.custloan.loanmodule.cstatus.VerifyandConfirmActivity

class GeoLocationActivity : FragmentActivity() , OnMapReadyCallback, LocationListener, GoogleApiClient.ConnectionCallbacks, NormalDialogFragment.Listener ,
    GoogleApiClient.OnConnectionFailedListener{

    private var mMap: GoogleMap? = null
    private lateinit var mLastLocation: Location
    private var mCurrLocationMarker: Marker? = null
    private var mGoogleApiClient: GoogleApiClient? = null
    private lateinit var mLocationRequest: LocationRequest
    internal lateinit var MarkerPoints: ArrayList<LatLng>
    private  var desAddr  : LatLng ? = null
    private  var StrDesAddr  : String ? = ""
     lateinit var btmtxtAddressName  : TextView
    lateinit var btmLat : TextView
    lateinit var btmLng: TextView
    private val defaultLatLng = LatLng(13.727685, 100.538855)
    private var d_Postcode : String = ""
    private  var lastAddress  : String ? = ""
    private val data_extra by lazy {
        intent?.getStringExtra(DATA_EXTRA) ?: ""
    }

    private val sub_type by lazy {
        intent?.getStringExtra(DATA_TYPE) ?: ""
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_geo_location)
        AnalyticsManager.trackScreen(AnalyticsScreenName.ONLINE_MAP)
        initInstances()
        initViewModel()
    }

    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(LocalizeManager.initDefaultLocalize(base))
//        super.attachBaseContext(LocalizationUtil.applyLanguage(newBase, "id"))
    }

    fun initInstances() {
        txt_ref_value.text = data_extra
        val mapFragment = getSupportFragmentManager().findFragmentById(R.id.map) as SupportMapFragment

        mapFragment.getMapAsync(this)
        MarkerPoints = ArrayList<LatLng>()

        btmtxtAddressName = (findViewById(R.id.txtAddressName) as TextView)
        btmLat = findViewById(R.id.txtAddressPositiondetail1) as TextView
        btmLng = findViewById(R.id.txtAddressPositiondetail2) as TextView

        toolbar.widget_toolbar_navigation.setOnClickListener{
            OnClickBacktoLastActivity()
        }

        checkLocationIsOn()

        btnAccept.setOnClickListener {
            AnalyticsManager.onlineMapADD()
            if(desAddr!!.latitude != 0.0 && desAddr!!.longitude != 0.0 ) {
                if (sub_type == "personal") {
                    saveBack(isSaveInfo = false)
                    LoanPersonalInfomationActivity.OpenWithLocation(this@GeoLocationActivity, data_extra, "")
                } else if (sub_type == "basic"){
                    saveBack(isSaveInfo = false)
                    BasicInformationActivity.OpenWithLocation(this@GeoLocationActivity, data_extra, "")
                } else {
                    saveBack(isSaveInfo = false)
                    VerifyandConfirmActivity.start(this@GeoLocationActivity, data_extra, "")
                }
            }else{
                MsgdescNormalDialog.show(
                        fragmentManager = supportFragmentManager,
                        description = getString(R.string.dialog_please_select_location) ,
                        confirmButtonMessage = getString(R.string.dialog_button_ok)
                )
            }
        }
    }

    fun initViewModel(){

    }


    @SuppressLint("MissingPermission")
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        val thailand = LatLng(13.7563, 100.5018)
        mMap!!.moveCamera(CameraUpdateFactory.newLatLng(thailand))
        mMap!!.uiSettings.isMapToolbarEnabled
        mMap!!.uiSettings.isZoomGesturesEnabled
        mMap!!.uiSettings.isZoomControlsEnabled

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) === PackageManager.PERMISSION_GRANTED
            ) {
                buildGoogleApiClient()
                mMap!!.setMyLocationEnabled(true)
            }
        } else {
            buildGoogleApiClient()
            mMap!!.setMyLocationEnabled(true)
        }

        mMap!!.setOnMapClickListener(object : GoogleMap.OnMapClickListener {
            override fun onMapClick(p0: LatLng?) {
                try {
                    mMap!!.clear()
                    desAddr = p0!!
                    StrDesAddr = getGeoAddress(desAddr!!)
                    mMap!!.addMarker(
                        MarkerOptions()
                            .position(desAddr!!)
                            .title(StrDesAddr)
                    )
                    mMap!!.moveCamera(CameraUpdateFactory.newLatLng(desAddr))
                    mMap!!.animateCamera(CameraUpdateFactory.zoomTo(15F))
                    btmtxtAddressName.setText(StrDesAddr)
                    btmLat.setText(String.format("%s°", String.format("%.6f", desAddr!!.latitude)))
                    btmLng.setText(String.format("%s°", String.format("%.6f", desAddr!!.longitude)))
                } catch (e: Exception) {
                    e.message

                }
            }
        })

        search_edt.setOnKeyListener(View.OnKeyListener { v, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_UP) {
                try{
                    mMap!!.clear()
                    val  destination = search_edt.text.toString()
                    desAddr = geGeotLatLng(destination)
                    // Find real location name
                    StrDesAddr =  getGeoAddress(desAddr!!)
                    mMap!!.addMarker(MarkerOptions().position(desAddr!!).title(StrDesAddr))
                    mMap!!.moveCamera(CameraUpdateFactory.newLatLng(desAddr!!))
                    mMap!!.animateCamera(CameraUpdateFactory.zoomTo(15F))
                    btmtxtAddressName.setText(StrDesAddr)
                    btmLat.setText(String.format("%s°" , String.format("%.6f", desAddr!!.latitude )))
                    btmLng.setText(String.format("%s°" ,  String.format("%.6f", desAddr!!.longitude)))
                    return@OnKeyListener true
                }catch ( e : Exception){
                    e.message
                }
            }
            false
        })


        mMap!!.setInfoWindowAdapter(object : GoogleMap.InfoWindowAdapter {
            override fun getInfoWindow(marker: Marker): View {
                val v = layoutInflater.inflate(R.layout.custom_marker_option, null)
                val tv_title = v.findViewById(R.id.tv_title) as TextView
                val position = v.findViewById(R.id.position) as TextView
                val position2 = v.findViewById(R.id.position2) as TextView
                try{
                    tv_title.setText(marker.getTitle().toString())
                    position.setText(desAddr!!.latitude.toString())
                    position2.setText(desAddr!!.longitude.toString())
                }catch (e : Exception){
                    Log.e("MapInfo : " ,e.printStackTrace().toString() )
                }

                return v
            }

            override fun getInfoContents(marker: Marker): View? {
                return null
            }
        })

        btnStreetView.setOnClickListener{
            try {
                if(desAddr!!.latitude != 0.0 && desAddr!!.longitude != 0.0 ){

                    GeoStreetViewLocationActivity.StartWithType( this@GeoLocationActivity ,
                        StrDesAddr!! ,
                        desAddr!!.latitude.toString() ,
                        desAddr!!.longitude.toString(),
                        data_extra,
                         sub_type
                        )
                }else{
                    MsgdescNormalDialog.show(
                            fragmentManager = supportFragmentManager,
                            description =  getString(R.string.dialog_please_select_location) ,
                            confirmButtonMessage = getString(R.string.dialog_button_ok)
                    )
                }

            }catch (e: Exception){
                Log.e("geo-street" , e.printStackTrace().toString())
            }

        }

    }

    private fun getGeoAddress(latLng: LatLng): String {
        // 1
        val geocoder = Geocoder(this ,Locale("th", "TH"))
        val addresses: List<Address>?
        val address: Address?
        var addressText = ""

        try {
            // 2
            addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1)
            // 3
            if (null != addresses && !addresses.isEmpty()) {
                address = addresses[0]
                addressText = address.getAddressLine(0)
                val country =  address.countryName
                d_Postcode =  address.postalCode
                val state = address.adminArea
                val city =  address.locality
                val knownName = address.featureName
                address.subLocality
                address.thoroughfare
                address.subThoroughfare

                if(addressText.contains("อำเภอ") || addressText.contains("เขต") || addressText.contains(" อ.")){
                    lastAddress =   if(addressText.contains("อำเภอ")){  addressText.split ("อำเภอ")[0]?:""  }
                    else if(addressText.contains(" อ.")) { addressText.split (" อ.")[0]?:"" }
                    else { addressText.split ("เขต")[0]?:"" }
                }else{
                    var addr = (address.featureName?:"" ) + (address.subLocality?:"")
                    if((address.featureName?:"" ) ==  (address.subLocality?:"") )
                        addr = (address.featureName?:"" )
                    lastAddress =   if(addr.contains("อำเภอ")){  addr.split ("อำเภอ")[0]?:""  }
                    else if(addr.contains(" อ.")) { addr.split (" อ.")[0]?:"" }
                    else { addr.split ("เขต")[0]?:"" }
                }
                // address.featureName?:"" + " " + address.thoroughfare?:""

            }
        } catch (e: IOException) {
            Log.e("MapsActivity", e.localizedMessage)
        }

        return addressText
    }


    private fun geGeotLatLng(strAddress : String) : LatLng {
        // 1
        val geocoder = Geocoder(this ,Locale("th", "TH"))
        val geocodeMatches: List<Address>?
        var latlng : LatLng? = null

        try {
            geocodeMatches = geocoder.getFromLocationName(strAddress , 1)
            val latitude = geocodeMatches[0].latitude
            val longitude = geocodeMatches[0].longitude
            latlng = LatLng(latitude,longitude)
        }catch (e: IOException) {
            Log.e("MapsActivity", e.localizedMessage)
        }

        return latlng!!
    }


    @Synchronized
    protected fun buildGoogleApiClient() {
        mGoogleApiClient = GoogleApiClient.Builder(this)
            .addConnectionCallbacks(this)
            .addOnConnectionFailedListener(this)
            .addApi(LocationServices.API).build()
        mGoogleApiClient!!.connect()
    }

    override fun onConnected(p0: Bundle?) {

        mLocationRequest = LocationRequest()
        mLocationRequest.setInterval(1000)
        mLocationRequest.setFastestInterval(1000)
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY)
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) === PackageManager.PERMISSION_GRANTED
        ) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this)
        }

    }


    override fun onConnectionSuspended(i: Int) {
    }

    override fun onLocationChanged(location: Location) {
    try{
        mLastLocation = location
        if (mCurrLocationMarker != null) {
            mCurrLocationMarker!!.remove()
        }
        //Place current location marker
        val latLng = LatLng(location.getLatitude(), location.getLongitude())
        val markerOptions = MarkerOptions()
        markerOptions.position(latLng)

        desAddr =  LatLng(location.getLatitude() , location.getLongitude())

        StrDesAddr =  getGeoAddress(desAddr!!)
        mMap!!.addMarker(MarkerOptions()
            .position(desAddr!!)
            .title(StrDesAddr))
        //move map camera
        mMap!!.moveCamera(CameraUpdateFactory.newLatLng(desAddr!!))
        mMap!!.animateCamera(CameraUpdateFactory.zoomTo(15F))
        //stop location updates
        if (mGoogleApiClient != null) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this)
        }
        btmtxtAddressName.setText(StrDesAddr!!)
        btmLat.setText(String.format("%s°" ,  String.format("%.6f", desAddr!!.latitude )))
        btmLng.setText(String.format("%s°" ,  String.format("%.6f", desAddr!!.longitude)))
    }catch(e : Exception) {

    }

    }

    override fun onConnectionFailed(connectionResult: ConnectionResult) {
    }


    @SuppressLint("ServiceCast")
    private fun checkLocationIsOn() {
        val locationManager = this@GeoLocationActivity!!.getSystemService(Context.LOCATION_SERVICE) as android.location.LocationManager
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
                    description = getString(R.string.location_switch_on_location_title),
                    confirmButtonMessage = getString(R.string.dialog_button_ok),
                    cancelButtonMessage = getString(R.string.callcenter_dialog_btn_cancel)
            )
        }
    }

    override fun onDialogConfirmClick() {
        setResult(Activity.RESULT_OK)
        finish()
    }

    override fun onDialogCancelClick() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }


    private fun updateMap() {
        checkPermission(success = {
            mMap!!.isMyLocationEnabled = true
            mMap!!.setOnMyLocationButtonClickListener {
                moveCameraToCurrentLocation()
                true
            }

            moveCameraToCurrentLocation()

        }, failure = {
            moveCamera(defaultLatLng)

        })
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
            mMap!!.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 8f))
        }
    }


    private fun checkPermission(success: () -> Unit, failure: () -> Unit) {
        Dexter.withActivity(this@GeoLocationActivity)
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

    override fun onBackPressed(){
        OnClickBacktoLastActivity()
    }


    fun OnClickBacktoLastActivity(){
        try {
            if(sub_type == "personal")
                LoanPersonalInfomationActivity.OpenWithLocation(this@GeoLocationActivity , data_extra , "")
            else if(sub_type == "basic")
                BasicInformationActivity.OpenWithLocation(this@GeoLocationActivity , data_extra , "")
            else
                VerifyandConfirmActivity.start(this@GeoLocationActivity , data_extra , "")
        }catch (e : Exception){
            e.message
        }
    }

    private fun saveBack(isSaveInfo :  Boolean = false) {

        var realAddr = lastAddress!!
        var lat =  desAddr!!.latitude.toString()
        var lng =  desAddr!!.longitude.toString()
        if (sub_type == "personal") {
            LoanDataManager.saveRealAddress(realAddr, d_Postcode , lat, lng)
        }else if(sub_type == "basic"){
            LoanDataManager.saveBasicRealAddress(realAddr, d_Postcode , lat, lng)
        }else {
            LoanDataManager.saveVerConfirmRealAddress(realAddr, d_Postcode , lat, lng ,sub_type)
        }
    }



    companion object {

        const val DATA_EXTRA = "DATA_EXTRA"
        const val DATA_TYPE = "DATA_TYPE"

        fun Open(context: Context? , data_extra : String  ) {
            val intent = Intent(context, GeoLocationActivity::class.java)
            intent.putExtra("DATA_EXTRA", data_extra)
            context?.startActivity(intent)
        }


        fun StartWithType(context: Context? , data_extra : String , sub_type : String  ) {
            val intent = Intent(context, GeoLocationActivity::class.java)
            intent.putExtra("DATA_EXTRA", data_extra)
            intent.putExtra("DATA_TYPE", sub_type)
            context?.startActivity(intent)
        }
    }
}