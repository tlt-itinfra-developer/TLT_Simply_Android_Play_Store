package tlt.th.co.toyotaleasing.modules.payment.cart.installment

import tlt.th.co.toyotaleasing.manager.JsonMapperManager
import tlt.th.co.toyotaleasing.model.request.GetDataPayDetailRequest
import tlt.th.co.toyotaleasing.model.response.GetDataPayDetailResponse
import tlt.th.co.toyotaleasing.modules.payment.cart.common.CartItem
import tlt.th.co.toyotaleasing.modules.payment.cart.common.CartViewModel
import tlt.th.co.toyotaleasing.modules.payment.cart.exception.CartInstallmentException
import kotlin.coroutines.suspendCoroutine
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

class CartInstallmentViewModel : CartViewModel() {

    override suspend fun getDataDetailByApi(isPayoff : Boolean) = suspendCoroutine<MutableList<CartItem>> {
        val request = GetDataPayDetailRequest.build(currentCar?.contractNumber ?: "" , type_pay = isPayoff )

        apiManager.getDataInstallmentPayDetail(request) { isError: Boolean, result: String ->
            if (isError) {
                it.resumeWithException(CartInstallmentException(result))
                return@getDataInstallmentPayDetail
            }

            val items = JsonMapperManager.getInstance()
                    .gson.fromJson(result, Array<GetDataPayDetailResponse>::class.java)

            if (items.isEmpty()) {
                return@getDataInstallmentPayDetail
            }

            val summaryList = items.map {
                CartItem(
                        type = it.bPTYPE ?: "",
                        code = it.bPCODE ?: "",
                        title = it.cNAME ?: "",
                        _price = it.aMOUNT.toString(),
                        isRecentlyAdd = false,
                        isChecked = true,
                        defaultValue = it.aMOUNT.toString(),
                        cStatus_code = currentCar?.cCONTRACTSTATUS.toString()
                )
            }.toMutableList()

            it.resume(summaryList)
        }
    }
}