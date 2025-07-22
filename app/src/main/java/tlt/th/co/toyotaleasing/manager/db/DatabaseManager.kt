package tlt.th.co.toyotaleasing.manager.db

import android.content.Context
import android.util.Log
import com.google.gson.Gson
import tlt.th.co.toyotaleasing.BuildConfig
import tlt.th.co.toyotaleasing.manager.ContextManager
import tlt.th.co.toyotaleasing.manager.FlowManager
import tlt.th.co.toyotaleasing.manager.JsonMapperManager
import tlt.th.co.toyotaleasing.manager.LocalizeManager
import tlt.th.co.toyotaleasing.model.entity.*
import tlt.th.co.toyotaleasing.model.entity.cache.Jsonable
import tlt.th.co.toyotaleasing.model.entity.masterdata.*
import tlt.th.co.toyotaleasing.model.response.ItemMyCarResponse
import java.util.*
import javax.crypto.BadPaddingException

class DatabaseManager private constructor() {

    private val LOGIN_ATTEMPT_PREF = "LOGIN_ATTEMPT"
    private val MASTER_DATA_VERSION = "MASTER_DATA_VERSION"
    private val APP_STATE = "APP_STATE"
    private val SOCIAL_LOGIN_STATE = "SOCIAL_LOGIN_STATE"
    private val NOTIFY_DEEPLINK_STATE = "NOTIFY_DEEPLINK_STATE"
    private val FLOW_STATE = "FLOW_STATE"
    private val PAYMENT_FLOW_STATE = "FLOW_STATE"
    private val SECRET_MODE = "SECRET_MODE"
    private val ACCEPT_TERMS_STAFF = "ACCEPT_TERMS_FOR_STAFF"

    private val DB_NAME = "TLT.realm"

    private fun initDatabase() {
       // realmConfiguration = getRealmConfig()!!
    }

    fun saveUserProfile(user: UserEntity) {
       // val realm = Realm.getInstance()
//        var entity = realm.where(UserEntity::class.java).findFirst()
//
//        realm.beginTransaction()
//
//        if (entity == null) {
//            entity = realm.createObject(UserEntity::class.java)
//        }
//
//        entity!!.transformToRealm(user)
//
//        realm.commitTransaction()
//        realm.close()
    }

    fun getUserProfile(): UserEntity {
//        val realm = Realm.getInstance(realmConfiguration)
//        var entity = realm.where(UserEntity::class.java).findFirst()
//
//        if (entity == null) {
//            realm.beginTransaction()
//            entity = realm.createObject(UserEntity::class.java)
//            realm.commitTransaction()
//        }
//
//        val result = entity!!.transform()
//
//        realm.close()
//
//        return result
        return UserEntity()
    }

    fun saveRegisterData(state: RegisterEntity) {
//        val realm = Realm.getInstance(realmConfiguration)
//        var realmEntity = realm.where(RegisterEntity::class.java).findFirst()
//
//        realm.beginTransaction()
//
//        if (realmEntity == null) {
//            realmEntity = realm.createObject(RegisterEntity::class.java)
//        }
//
//        realmEntity!!.transformToRealm(state)
//
//        realm.commitTransaction()
//        realm.close()
    }

    fun getRegisterData(): RegisterEntity {
//        val realm = Realm.getInstance(realmConfiguration)
//        var entity = realm.where(RegisterEntity::class.java).findFirst()
//
//        if (entity == null) {
//            realm.beginTransaction()
//            entity = realm.createObject(RegisterEntity::class.java)
//            realm.commitTransaction()
//        }
//
//        val result = entity!!.transform()
//
//        realm.close()
//
//        return result
        return RegisterEntity()
    }

    fun getProvinceList(): List<MailingA>? {
        return null
    }


    fun getAmphurProvinceList(postcode : String ): List<MailingA>? {
        return null
    }


    fun getSequenceList(): MutableList<SequenceTransactionEntity>? {
        return null
    }

    fun getAmphurListByProvinceCode(provinceCode: String): List<MailingA>? {
        return null
    }


    fun getTermAndConditionUrl(): String {
        return ""
    }

    fun getTermAndConditionCode(): String {
        return ""
    }

//    fun <T : RealmObject> save(realmClass: Class<T>, list: List<T>) {
//        val realm = Realm.getInstance(realmConfiguration)
//        realm.beginTransaction()
//        realm.copyToRealm(list)
//        realm.commitTransaction()
//        realm.close()
//    }
//
//    fun <T : RealmObject> deleteBy(realmClass: Class<T>, key: String = "", value: String = "") {
//        val realm = Realm.getInstance(realmConfiguration)
//        val query = if (key.isEmpty() || value.isEmpty()) {
//            realm.where(realmClass)
//        } else {
//            realm.where(realmClass).equalTo(key, value)
//        }
//
//        val items = query.findAll()
//
//        if (items.isEmpty()) {
//            realm.close()
//            return
//        }
//
//        realm.beginTransaction()
//        items.deleteAllFromRealm()
//        realm.commitTransaction()
//        realm.close()
//    }
//
//    fun <T : RealmObject> findAllBy(realmClass: Class<T>): List<T>? {
//        val realm = Realm.getInstance(realmConfiguration)
//        val entities = realm.where(realmClass).findAll()
//        val result = realm.copyFromRealm(entities)
//        realm.close()
//        return result
//    }
//
//    fun <T : RealmObject> findAllBy(realmClass: Class<T>, key: String = "", value: String = ""): List<T>? {
//        val realm = Realm.getInstance(realmConfiguration)
//        val query = if (key.isEmpty() || value.isEmpty()) {
//            realm.where(realmClass)
//        } else {
//            realm.where(realmClass).equalTo(key, value)
//        }
//
//        val entities = query.findAll()
//        val result = realm.copyFromRealm(entities)
//        realm.close()
//        return result
//    }
//
//    fun <T : Jsonable> saveCache(cacheRealmClass: Class<T>, model: Any) {
//        val realm = Realm.getInstance(realmConfiguration)
//        realm.beginTransaction()
//        realm.where(cacheRealmClass).findAll().deleteAllFromRealm()
//        realm.createObject(cacheRealmClass).apply {
//            setJsonString(JsonMapperManager.getInstance().gson.toJson(model))
//        }
//        realm.commitTransaction()
//        realm.close()
//    }
//
//    fun <T : Jsonable, E> findCacheBy(cacheRealmClass: Class<T>, model: Class<E>): E? {
//        val realm = Realm.getInstance(realmConfiguration)
//        val entities = realm.where(cacheRealmClass).findAll()
//        val result = realm.copyFromRealm(entities)
//        val cache = JsonMapperManager.getInstance()
//                .gson
//                .fromJson(result.firstOrNull()?.getJsonString(), model)
//        realm.close()
//        return cache
//    }

    fun saveMyCarList(items: List<ItemMyCarResponse>) {
    }

    fun getMyCarList(): List<ItemMyCarResponse> {
        return listOf()
    }

    fun deleteNotifyByKey(key: String) {

    }

    fun saveNotify(item: NotifyEntity) {

    }

    fun deleteSequenceIdList() {

    }

    fun deleteSequenceIdLast() {

    }

    fun deletePushPopupNotify() {

    }

    fun getNotifyList(): List<NotifyEntity> {
        return listOf()
    }

    fun getAnalytics(): List<AnalyticsEntity> {
        return listOf()
    }

    fun getAnalyticsString(): String {
        return ""
    }

    fun deleteAnalyticsList() {

    }

    fun saveAnalytics(item: AnalyticsEntity) {

    }

    fun updatePersonalAddress( realaddress: String  , postCode : String,  lat: String , long : String) {

    }

    fun updateVerifyCurrentAddress( realaddress: String ,postCode : String,  lat: String , long : String ) {

    }

    fun updateBasicInfoAddress( realaddress: String  , postCode : String,  lat: String , long : String) {

    }

    fun updateVerifyRegisAddress( realaddress: String , postCode : String,  lat: String , long : String ) {

    }

    fun updateVerifyMailingAddress( realaddress: String , postCode : String, lat: String , long : String ) {

    }

    fun updateVerifyShowroomLocation( showroomCode: String , showroom: String) {

    }

    fun saveSecretModeAttempt(attempt: Int = 1) {
        val context = ContextManager.getInstance().getApplicationContext()
        context.getSharedPreferences(context.packageName, Context.MODE_PRIVATE)
                .edit()
                .putInt(SECRET_MODE, attempt)
                .commit()
    }

    fun getSecretModeAttempt(): Int {
        val context = ContextManager.getInstance().getApplicationContext()

        return context.getSharedPreferences(context.packageName, Context.MODE_PRIVATE)
                .getInt(SECRET_MODE, 0)
    }

    fun getLoginAttempt(): Int {
        val context = ContextManager.getInstance().getApplicationContext()

        return context.getSharedPreferences(context.packageName, Context.MODE_PRIVATE)
                .getInt(LOGIN_ATTEMPT_PREF, 0)
    }

    fun updateStatusTermsAndConditionForStaff() {
        val context = ContextManager.getInstance().getApplicationContext()
        context.getSharedPreferences(context.packageName, Context.MODE_PRIVATE)
                .edit()
                .putBoolean(ACCEPT_TERMS_STAFF, true)
                .apply()
    }

    fun isAcceptTermsAlready(): Boolean {
        val context = ContextManager.getInstance().getApplicationContext()
        return context.getSharedPreferences(context.packageName, Context.MODE_PRIVATE)
                .getBoolean(ACCEPT_TERMS_STAFF, false)
    }

    fun saveLoginAttempt(attempt: Int = 1) {
        val context = ContextManager.getInstance().getApplicationContext()
        context.getSharedPreferences(context.packageName, Context.MODE_PRIVATE)
                .edit()
                .putInt(LOGIN_ATTEMPT_PREF, attempt)
                .commit()
    }

    fun getMasterDataVersion(): String {
        // biw
        val context = ContextManager.getInstance().getApplicationContext()

        return context.getSharedPreferences(context.packageName, Context.MODE_PRIVATE)
                .getString(MASTER_DATA_VERSION, "")!!
    }

    fun saveMasterDataVersion(version: String = "") {
        val context = ContextManager.getInstance().getApplicationContext()
        context.getSharedPreferences(context.packageName, Context.MODE_PRIVATE)
                .edit()
                .putString(MASTER_DATA_VERSION, version)
                .commit()
    }

    fun isNotifyDeeplinkState(): Boolean {
        val context = ContextManager.getInstance().getApplicationContext()

        return context.getSharedPreferences(context.packageName, Context.MODE_PRIVATE)
                .getBoolean(NOTIFY_DEEPLINK_STATE, false)
    }

    fun updateNotifyDeeplinkState(isNotifyDeeplinkState: Boolean = false) {
        val context = ContextManager.getInstance().getApplicationContext()

        context.getSharedPreferences(context.packageName, Context.MODE_PRIVATE)
                .edit()
                .putBoolean(NOTIFY_DEEPLINK_STATE, isNotifyDeeplinkState)
                .commit()
    }

    fun isSocialLoginState(): Boolean {
        val context = ContextManager.getInstance().getApplicationContext()

        return context.getSharedPreferences(context.packageName, Context.MODE_PRIVATE)
                .getBoolean(SOCIAL_LOGIN_STATE, false)
    }

    fun updateSocialLoginState(isSocialLoginState: Boolean = false) {
        val context = ContextManager.getInstance().getApplicationContext()

        context.getSharedPreferences(context.packageName, Context.MODE_PRIVATE)
                .edit()
                .putBoolean(SOCIAL_LOGIN_STATE, isSocialLoginState)
                .commit()
    }

    fun isAppStateForeground(): Boolean {
        val context = ContextManager.getInstance().getApplicationContext()

        return context.getSharedPreferences(context.packageName, Context.MODE_PRIVATE)
                .getBoolean(APP_STATE, false)
    }

    fun updateAppState(isAppStateForeground: Boolean = false) {
        val context = ContextManager.getInstance().getApplicationContext()

        context.getSharedPreferences(context.packageName, Context.MODE_PRIVATE)
                .edit()
                .putBoolean(APP_STATE, isAppStateForeground)
                .commit()
    }

    fun getFlowState(): String {
        val context = ContextManager.getInstance().getApplicationContext()

        return context.getSharedPreferences(context.packageName, Context.MODE_PRIVATE)
                .getString(FLOW_STATE, FlowManager.REGISTER_FLOW)!!
    }

    fun saveFlowState(state: String) {
        val context = ContextManager.getInstance().getApplicationContext()

        context.getSharedPreferences(context.packageName, Context.MODE_PRIVATE)
                .edit()
                .putString(FLOW_STATE, state)
                .commit()
    }

    fun getPaymentFlowState(): String {
        val context = ContextManager.getInstance().getApplicationContext()

        return context.getSharedPreferences(context.packageName, Context.MODE_PRIVATE)
                .getString(PAYMENT_FLOW_STATE, FlowManager.INSURANCE_PAYMENT_FLOW)!!
    }

    fun savePaymentFlowState(state: String) {
        val context = ContextManager.getInstance().getApplicationContext()

        context.getSharedPreferences(context.packageName, Context.MODE_PRIVATE)
                .edit()
                .putString(PAYMENT_FLOW_STATE, state)
                .commit()
    }



    fun saveMasterData(masterData: MasterData) {

    }

    fun clear() {

    }

//    private fun getRealmConfig(): RealmConfiguration? {
//        val appKey = try {
//            SecureManager.getInstance().getAppKey()
//        } catch (badPaddingException: BadPaddingException) {
//            clear()
//            SecureManager.getInstance().getAppKey()
//        }
//
//        val realmConfig = RealmConfiguration.Builder()
//                .name(DB_NAME)
//                .schemaVersion(1)
//
//        if (!BuildConfig.DEBUG) {
//            realmConfig.encryptionKey(appKey)
//        }
//        val charset = Charsets.UTF_8
////        println(appKey.toString(charset))
//        Log.e("appKey" , appKey.toString(charset))
//        return realmConfig.build()
//    }

//    private fun getRealmConfig(): RealmConfiguration? {
//        val appKey = try {
//            SecureManager.getInstance().getAppKey()
//        } catch (badPaddingException: BadPaddingException) {
//            clear()
//            SecureManager.getInstance().getAppKey()
//        }
//
//        val realmConfig = RealmConfiguration.Builder()
//                .name(DB_NAME)
//                .schemaVersion(9)
//                .migration(TLTMigration()) // Migration to run instead of throwing an exception
//
//
//        if (!BuildConfig.DEBUG) {
//            realmConfig.encryptionKey(appKey)
//        }
//        val charset = Charsets.UTF_8
//        println(appKey.toString(charset))
//        Log.e("appKey" , appKey.toString(charset))
//        return realmConfig.build()
//    }

    companion object {
        private val databaseManager = DatabaseManager()

        fun init(context: Context) {
            databaseManager.initDatabase()
        }

        fun getInstance() = databaseManager
    }
}