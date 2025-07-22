package tlt.th.co.toyotaleasing.modules.payment.cart.tax

import tlt.th.co.toyotaleasing.common.extension.toNumberFormatWithoutComma
import tlt.th.co.toyotaleasing.manager.JsonMapperManager
import tlt.th.co.toyotaleasing.manager.db.CacheManager
import tlt.th.co.toyotaleasing.model.request.GetDataPayDetailRequest
import tlt.th.co.toyotaleasing.model.response.GetDataPayDetailResponse
import tlt.th.co.toyotaleasing.modules.payment.cart.common.CartItem
import tlt.th.co.toyotaleasing.modules.payment.cart.common.CartViewModel
import tlt.th.co.toyotaleasing.modules.payment.cart.exception.CartTaxException
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class CartTaxViewModel : CartViewModel() {

    private val PORLORBOR_ID = "432"
    private val PORLORBOR_TYPE = "21"
    private var isPorlorborBuyNow = false
    private val currentTax = CacheManager.getCacheTax()

    override suspend fun getDataDetailByApi(isPayoff : Boolean) = suspendCoroutine<MutableList<CartItem>> {
        val contractNumber = currentCar?.contractNumber ?: ""
        val request = if (isPorlorborBuyNow) {
            GetDataPayDetailRequest.buildForPorlorborBuyNow(contractNumber)
        } else {
            GetDataPayDetailRequest.build(contractNumber)
        }

        apiManager.getDataTaxPayDetail(request) { isError: Boolean, result: String ->
            if (isError) {
                it.resumeWithException(CartTaxException(result))
                return@getDataTaxPayDetail
            }

            val items = JsonMapperManager.getInstance()
                    .gson.fromJson(result, Array<GetDataPayDetailResponse>::class.java)

            if (items.isEmpty()) {
                return@getDataTaxPayDetail
            }

            val summaryList = items.map {
                CartItem(
                        type = it.bPTYPE ?: "",
                        code = it.bPCODE ?: "",
                        title = it.cNAME ?: "",
                        _price = it.aMOUNT.toString(),
                        isRecentlyAdd = false,
                        isChecked = true,
                        cStatus_code = currentCar?.cCONTRACTSTATUS.toString()
                )
            }.toMutableList()

            it.resume(summaryList)
        }
    }

    fun isShowPorlorborAttachScreen() = whenDataLoaded.value?.cartList?.any {
        it.code == PORLORBOR_ID && it.type == PORLORBOR_TYPE && !it.isChecked || true
    } ?: false

    fun isShowTorloraorAttachScreen() = currentTax?.cVEHICLEAGE?.toNumberFormatWithoutComma()?.toFloat()?.toInt() ?: 0 >= 8

    fun setIsPorlorborBuyNow(flag: Boolean) {
        isPorlorborBuyNow = flag
    }
}