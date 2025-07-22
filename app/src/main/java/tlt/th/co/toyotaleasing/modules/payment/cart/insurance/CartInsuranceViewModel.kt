package tlt.th.co.toyotaleasing.modules.payment.cart.insurance

import tlt.th.co.toyotaleasing.manager.JsonMapperManager
import tlt.th.co.toyotaleasing.model.request.GetDataPayDetailRequest
import tlt.th.co.toyotaleasing.model.response.GetDataPayDetailResponse
import tlt.th.co.toyotaleasing.modules.payment.cart.common.CartItem
import tlt.th.co.toyotaleasing.modules.payment.cart.common.CartViewModel
import tlt.th.co.toyotaleasing.modules.payment.cart.exception.CartInsuranceException
import kotlin.coroutines.suspendCoroutine
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

class CartInsuranceViewModel : CartViewModel() {

    override suspend fun getDataDetailByApi(isPayoff : Boolean) = suspendCoroutine<MutableList<CartItem>> {
        val request = GetDataPayDetailRequest.build(currentCar?.contractNumber ?: "")

        apiManager.getDataInsurancePayDetail(request) { isError: Boolean, result: String ->
            if (isError) {
                it.resumeWithException(CartInsuranceException(result))
                return@getDataInsurancePayDetail
            }

            val items = JsonMapperManager.getInstance()
                    .gson.fromJson(result, Array<GetDataPayDetailResponse>::class.java)

            if (items.isEmpty()) {
                return@getDataInsurancePayDetail
            }

            val summaryList = items.map {
                CartItem(
                        type = it.bPTYPE ?: "",
                        code = it.bPCODE ?: "",
                        title = it.cNAME ?: "",
                        _price = it.aMOUNT.toString(),
                        isRecentlyAdd = false,
                        isChecked = true ,
                        cStatus_code = currentCar?.cCONTRACTSTATUS.toString()
                )
            }.toMutableList()

            it.resume(summaryList)
        }
    }
}