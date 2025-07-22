package tlt.th.co.toyotaleasing.manager.db

import tlt.th.co.toyotaleasing.model.entity.cache.*
import tlt.th.co.toyotaleasing.model.response.*

object CacheManager {
    fun cacheTIB(entity: GetTIBClubDetailResponse.TIBCLUBDETAIL) {
        //DatabaseManager.getInstance().saveCache(TIBDetailCacheEntity::class.java, entity)
    }

    fun getCacheTIB() = GetTIBClubDetailResponse.TIBCLUBDETAIL()

    fun cacheInsurance(entity: GetDataInsuranceResponse) {
        //DatabaseManager.getInstance().saveCache(InsuranceCacheEntity::class.java, entity)
    }

    fun getCacheInsurance() = GetDataInsuranceResponse()

    fun cacheCar(entity: ItemMyCarResponse) {
        //DatabaseManager.getInstance().saveCache(CarCacheEntity::class.java, entity)
    }

    fun getCacheCar() = ItemMyCarResponse()

    fun cacheInstallment(entity: ItemInstallmentResponse) {
        //DatabaseManager.getInstance().saveCache(InstallmentCacheEntity::class.java, entity)
    }

    fun getCacheInstallment() = ItemInstallmentResponse()

    fun cacheTax(entity: GetDataTaxResponse) {
        //DatabaseManager.getInstance().saveCache(TaxCacheEntity::class.java, entity)
    }

    fun getCacheTax() = GetDataTaxResponse()

    fun cacheProfile(entity: GetDataProfileResponse) {
        //DatabaseManager.getInstance().saveCache(ProfileCacheEntity::class.java, entity)
    }

    fun getCacheProfile() = GetDataProfileResponse()

    fun cacheEContractQRCode(entity: SyncExpenseResponse) {
        //DatabaseManager.getInstance().saveCache(QRCodeCacheEntity::class.java, entity)
    }

    fun getCacheEContractQRCode() = SyncExpenseResponse()


    fun cacheEContractPDF(entity: GetEContractResponse) {
        //DatabaseManager.getInstance().saveCache(EContractPDFCacheEntity::class.java, entity)
    }

}