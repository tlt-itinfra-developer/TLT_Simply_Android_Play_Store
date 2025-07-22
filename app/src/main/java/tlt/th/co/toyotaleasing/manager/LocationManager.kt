package tlt.th.co.toyotaleasing.manager

import android.annotation.SuppressLint
import android.location.Location
import com.google.android.gms.location.LocationServices
import tlt.th.co.toyotaleasing.common.exception.PermissionException

import tlt.th.co.toyotaleasing.model.entity.location.LoanLocationDetailEntity
import tlt.th.co.toyotaleasing.model.entity.location.LoanLocationFilterEntity
import tlt.th.co.toyotaleasing.model.entity.location.LocationDetailEntity
import tlt.th.co.toyotaleasing.model.entity.location.LocationFilterEntity
import tlt.th.co.toyotaleasing.util.PermissionUtils
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

object LocationManager {

    private val databaseManager = null

    fun saveLocationFilter(
            dealerName: String = "",
            provinceIndex: Int = -1,
            amphurIndex: Int = -1,
            isOfficeChecked: Boolean = false,
            isShowRoomChecked: Boolean = false,
            isServiceCenterChecked: Boolean = false,
            isRepairServiceChecked: Boolean = false,
            isNearly: Boolean = false
    ) {
        val entity = LocationFilterEntity(
                dealerName,
                provinceIndex,
                amphurIndex,
                isOfficeChecked,
                isShowRoomChecked,
                isServiceCenterChecked,
                isRepairServiceChecked,
                isNearly
        )

        //databaseManager.save(LocationFilterEntity::class.java, listOf(entity))
    }

    fun getLocationFilter() = null

    fun clearLocationFilter() {
       // databaseManager.deleteBy(LocationFilterEntity::class.java)
    }

    fun saveLocationDetail(locationDetail: LocationDetailEntity) {
        //databaseManager.deleteBy(LocationDetailEntity::class.java)
       // databaseManager.save(LocationDetailEntity::class.java, listOf(locationDetail))
    }

    fun getLocationDetail() = null

    @SuppressLint("MissingPermission")
    suspend fun getLastKnowLocation() = suspendCoroutine<Location> { continuation ->
        val context = ContextManager.getInstance().getApplicationContext()

        if (!PermissionUtils.isGrantedLocation()) {
            val locationDefault = Location("GPS_Provider")
            locationDefault.latitude = 13.727685
            locationDefault.longitude = 100.538855
            continuation.resume(locationDefault)
            return@suspendCoroutine
        }

        LocationServices.getFusedLocationProviderClient(context)
                .lastLocation
                .addOnSuccessListener { location ->
                    if (location == null) {
                        val locationDefault = Location("GPS_Provider")
                        locationDefault.latitude = 13.727685
                        locationDefault.longitude = 100.538855
                        continuation.resume(locationDefault)
                    } else {
                        location.let {
                            continuation.resume(it)
                        }
                    }
                }
    }



    fun saveLoanLocationFilter(
            dealerName: String = "",
            showroomCode : String = "",
            dealerCode : String = "",
            provinceIndex: Int = -1,
            amphurIndex: Int = -1,
            isNearly: Boolean = false
    ) {
        val entity = LoanLocationFilterEntity(
                dealerName,
                showroomCode ,
                dealerCode,
                provinceIndex,
                amphurIndex,
                isNearly
        )
       // databaseManager.save(LoanLocationFilterEntity::class.java, listOf(entity))
    }

    fun getLoanLocationFilter() = null

    fun clearLoanLocationFilter() {
       // databaseManager.deleteBy(LoanLocationFilterEntity::class.java)
    }

    fun saveLoanLocationDetail(locationDetail: LoanLocationDetailEntity) {
       // databaseManager.deleteBy(LoanLocationDetailEntity::class.java)
       // databaseManager.save(LoanLocationDetailEntity::class.java, listOf(locationDetail))
    }

}