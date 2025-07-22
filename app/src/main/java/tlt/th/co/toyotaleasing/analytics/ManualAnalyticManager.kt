package tlt.th.co.toyotaleasing.analytics

import android.os.Build
import android.os.Bundle
import android.util.Base64
import com.google.firebase.analytics.FirebaseAnalytics
import tlt.th.co.toyotaleasing.manager.ContextManager
import tlt.th.co.toyotaleasing.manager.api.tlt.TLTApiManager
import tlt.th.co.toyotaleasing.manager.db.CacheManager

import tlt.th.co.toyotaleasing.model.entity.AnalyticsEntity
import java.io.ByteArrayOutputStream
import java.nio.charset.StandardCharsets.UTF_8
import java.util.*
import java.util.zip.GZIPInputStream
import java.util.zip.GZIPOutputStream

object ManualAnalyticManager {

    private val mFirebaseAnalytics: FirebaseAnalytics = FirebaseAnalytics.getInstance(ContextManager.getInstance().getApplicationContext())
    private const val EVENT_NAME = "TLTLog"

    fun saveAnalytics(bundle: Bundle) {
        mFirebaseAnalytics.logEvent(EVENT_NAME, bundle)
        val entity = AnalyticsEntity().apply {
            id = UUID.randomUUID().toString()
            cust_id = ""
            extContract = CacheManager.getCacheCar()?.contractNumber ?: ""
            os_version = "Android ${Build.VERSION.RELEASE}"
            os = "Android"
            screenName = (bundle.get(AnalyticsConstants.SCREEN) ?: "").toString()
            logType = (bundle.get(AnalyticsConstants.TYPE) ?: "").toString()
            startTimeStamp = bundle.get(AnalyticsConstants.START_TIME).toString()
            endTimeStamp = bundle.get(AnalyticsConstants.TIME).toString()
            durationTime = (bundle.get(AnalyticsConstants.DURATION_TIME) ?: "").toString()
            type = (bundle.get(AnalyticsConstants.TYPE) ?: "").toString()
            label = (bundle.get(AnalyticsConstants.LABEL) ?: "").toString()
            value = (bundle.get(AnalyticsConstants.VALUE) ?: "").toString()
            info = (bundle.get(AnalyticsConstants.INFO) ?: "").toString()
            action = (bundle.get(AnalyticsConstants.ACTION) ?: "").toString()
            language = (bundle.get(AnalyticsConstants.LANGUAGE) ?: "").toString()
            device_name = android.os.Build.MODEL
        }
        ////DatabaseManager.getInstance().saveAnalytics(entity)
    }

//    fun getAnalyticBase64() : String {
//        val zipped = gzip(DatabaseManager.getInstance().getAnalyticsString())
//        return Base64.encodeToString(zipped, Base64.DEFAULT)
//    }

    fun gzip(content: String): ByteArray {
        val bos = ByteArrayOutputStream()
        GZIPOutputStream(bos).bufferedWriter(UTF_8).use { it.write(content) }
        return bos.toByteArray()
    }

    fun ungzip(content: ByteArray): String =
            GZIPInputStream(content.inputStream()).bufferedReader(UTF_8).use { it.readText() }

}