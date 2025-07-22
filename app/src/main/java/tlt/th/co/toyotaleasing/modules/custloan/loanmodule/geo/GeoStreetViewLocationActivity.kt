package tlt.th.co.toyotaleasing.modules.custloan.loanmodule.geo

import android.content.Context
import android.content.Intent
import android.location.Geocoder
import android.os.Bundle
import androidx.fragment.app.FragmentActivity
import android.util.Log
import android.widget.TextView
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.*
import kotlinx.android.synthetic.main.fragment_geo_streetview.*
import kotlinx.android.synthetic.main.widget_toolbar.view.*
import java.text.DecimalFormat
import java.util.*
import tlt.th.co.toyotaleasing.R
import tlt.th.co.toyotaleasing.model.entity.LoanDataManager
import tlt.th.co.toyotaleasing.modules.custloan.loanmodule.carloan.LoanPersonalInfomationActivity
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.SupportStreetViewPanoramaFragment
import tlt.th.co.toyotaleasing.analytics.AnalyticsManager
import tlt.th.co.toyotaleasing.analytics.AnalyticsScreenName
import tlt.th.co.toyotaleasing.common.dialog.MsgdescNormalDialog
import tlt.th.co.toyotaleasing.modules.custloan.loanmodule.basicdetail.BasicInformationActivity
import tlt.th.co.toyotaleasing.modules.custloan.loanmodule.cstatus.VerifyandConfirmActivity


class GeoStreetViewLocationActivity : FragmentActivity(), StreetViewPanorama.OnStreetViewPanoramaChangeListener{

    lateinit var mStreetViewPanorama: StreetViewPanorama
    private var dLat: Double? = null
    private var dLng: Double? = null
    private var c_province: String? = null
    private lateinit var desAddr  : LatLng
    private lateinit var staAddr  : LatLng
    private lateinit var StrDesAddr  : String
    private lateinit var StrCurrentAddr  : String
    lateinit var btmtxtAddressName  : TextView
    lateinit var btmLat : TextView
    lateinit var btmLng: TextView
    private var d_Postcode : String = ""
    private  var lastAddress  : String ? = ""

    private val s_Location by lazy {
        intent.getStringExtra("DES_LOCATION") ?: ""
    }

    private val sLat by lazy {
        intent.getStringExtra("DES_LAT") ?: ""
    }
    private val sLng by lazy {
        intent.getStringExtra("DES_LNG") ?: ""
    }

    private val data_extra by lazy {
        intent.getStringExtra("DATA_EXTRA") ?: ""
    }

    private val sub_type by lazy {
        intent?.getStringExtra(DATA_TYPE) ?: ""
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_geo_streetview)
        AnalyticsManager.trackScreen(AnalyticsScreenName.ONLINE_STREET_VIEW)
        initInstances()
        initViewModel()
    }

    fun initInstances() {
        try{
            txt_ref_value.text = data_extra
            val streetViewFragment = supportFragmentManager.findFragmentById(R.id.streetviewpanorama) as SupportStreetViewPanoramaFragment

            btmtxtAddressName = (findViewById(R.id.txtAddressName) as TextView)
            btmLat = findViewById(R.id.txtPositiondetail1) as TextView
            btmLng = findViewById(R.id.txtPositiondetail2) as TextView


            streetViewFragment.getStreetViewPanoramaAsync { streetViewPanorama ->
                streetViewPanorama.setPosition(LatLng(sLat.toDouble(), sLng.toDouble()))
                mStreetViewPanorama = streetViewPanorama
                mStreetViewPanorama.getLocation()
                mStreetViewPanorama.setOnStreetViewPanoramaChangeListener(
                    this@GeoStreetViewLocationActivity
                )
            }

            toolbar.widget_toolbar_navigation.setOnClickListener{
                OnClickBacktoLastActivity()
            }

            StrCurrentAddr =  s_Location

            btnAccept.setOnClickListener {
                AnalyticsManager.onlineMapStreetViewADD()
                if(desAddr!!.latitude != 0.0 && desAddr!!.longitude != 0.0 ) {
                    if (sub_type == "personal") {
                        saveBack(isSaveInfo = false)
                        LoanPersonalInfomationActivity.OpenWithLocation(this@GeoStreetViewLocationActivity, data_extra, "")
                    }else if (sub_type == "basic") {
                        saveBack(isSaveInfo = false)
                        BasicInformationActivity.OpenWithLocation(this@GeoStreetViewLocationActivity, data_extra, "")
                    } else {
                        saveBack(isSaveInfo = false)
                        VerifyandConfirmActivity.OpenWithLocation(this@GeoStreetViewLocationActivity, data_extra, "")
                    }
                }else{
                    MsgdescNormalDialog.show(
                            fragmentManager = supportFragmentManager,
                            description = getString(R.string.dialog_please_select_location) ,
                            confirmButtonMessage = getString(R.string.dialog_button_ok)
                    )
                }
            }


        }catch (e : Exception){
            Log.e("geo street instance : " , e.stackTrace.toString())
        }
    }

    fun initViewModel(){
        try{

        }catch (e : Exception){
            Log.e("geo street view : " , e.stackTrace.toString())
        }
    }

    override fun onStreetViewPanoramaChange(location: StreetViewPanoramaLocation?) {
        try {
            if (location != null) {
                dLat = location.position.latitude
                dLng = location.position.longitude
                desAddr = LatLng(dLat!!,dLng!!)
                val geoCoder = Geocoder(this ,Locale("th", "TH"))
                val addresses = geoCoder.getFromLocation(dLat!!, dLng!!, 1)
                 StrDesAddr = addresses[0].getAddressLine(0) //0 to obtain first possible address
                val city = addresses[0].locality
                val state = addresses[0].adminArea
                val country = addresses[0].countryName
                d_Postcode = addresses[0].postalCode
                //create your custom title
                c_province = country
                val formatter = DecimalFormat("#,###.00")
                val lat = formatter.format(dLat!!)
                val lng = formatter.format(dLng!!)
                val strLatlng = String.format("Lat: %s°, Long: %s°", lat, lng)
                btmtxtAddressName.setText(StrDesAddr)
                btmLat.setText(String.format("%s°" ,  String.format("%.6f", dLat)))
                btmLng.setText(String.format("%s°" ,  String.format("%.6f", dLng)))

                if(StrDesAddr.contains("อำเภอ") || StrDesAddr.contains("เขต") || StrDesAddr.contains(" อ.")){
                    lastAddress =   if(StrDesAddr.contains("อำเภอ")){  StrDesAddr.split ("อำเภอ")[0]?:""  }
                    else if(StrDesAddr.contains(" อ.")) { StrDesAddr.split (" อ.")[0]?:"" }
                    else { StrDesAddr.split ("เขต")[0]?:"" }
                }else{
                    var addr =  ( addresses[0].featureName?:""  )  +  " " + (  addresses[0].subLocality?:"" )
                    lastAddress =   if(addr.contains("อำเภอ")){  addr.split ("อำเภอ")[0]?:""  }
                    else if(addr.contains(" อ.")) { addr.split (" อ.")[0]?:"" }
                    else { addr.split ("เขต")[0]?:"" }
                }
                // addresses[0].featureName?:"" + " " + addresses[0].thoroughfare?:"" + " "

            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }


    override fun onBackPressed(){
        OnClickBacktoLastActivity()
    }

    fun OnClickBacktoLastActivity(){
        try {
            GeoLocationActivity.StartWithType(this@GeoStreetViewLocationActivity , data_extra , sub_type )

        }catch (e : Exception){
            e.message
        }
    }

    private fun saveBack(isSaveInfo :  Boolean = false) {

       var realAddr =  lastAddress!!
        var lat =  desAddr!!.latitude.toString()
        var lng =  desAddr!!.longitude.toString()
        if (sub_type == "personal") {
            LoanDataManager.saveRealAddress(realAddr , d_Postcode, lat, lng)
        }else if(sub_type == "basic"){
            LoanDataManager.saveBasicRealAddress(realAddr, d_Postcode ,  lat, lng)
        }else {
            LoanDataManager.saveVerConfirmRealAddress(realAddr, d_Postcode , lat, lng , sub_type)
        }
    }


    companion object {

        const val DATA_TYPE = "DATA_TYPE"


        fun Open(  context: Context?  , curr_location : String  , lat : String ,lng : String   , data_extra : String ) {
            val intent = Intent(context, GeoStreetViewLocationActivity::class.java)
            intent.putExtra("DES_LOCATION" , curr_location)
            intent.putExtra("DES_LAT" , lat)
            intent.putExtra("DES_LNG" , lng)
            intent.putExtra("DATA_EXTRA" , data_extra)
            context?.startActivity(intent)
        }

        fun StartWithType(context: Context?  , curr_location : String  , lat : String ,lng : String   , data_extra : String , sub_type : String) {
            val intent = Intent(context, GeoStreetViewLocationActivity::class.java)
            intent.putExtra("DES_LOCATION" , curr_location)
            intent.putExtra("DES_LAT" , lat)
            intent.putExtra("DES_LNG" , lng)
            intent.putExtra("DATA_EXTRA" , data_extra)
            intent.putExtra("DATA_TYPE", sub_type)
            context?.startActivity(intent)
        }
    }
}