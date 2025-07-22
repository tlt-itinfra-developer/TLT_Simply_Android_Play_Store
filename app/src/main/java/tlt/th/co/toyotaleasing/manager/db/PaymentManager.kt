package tlt.th.co.toyotaleasing.manager.db

import tlt.th.co.toyotaleasing.model.entity.payment.DocumentEntity
import tlt.th.co.toyotaleasing.model.entity.payment.PaymentEntity
import tlt.th.co.toyotaleasing.model.entity.payment.SummaryEntity
import tlt.th.co.toyotaleasing.modules.payment.cart.common.CartItem

object PaymentManager {

    private const val PORLORBOR_DOC = "porlorbor"
    private const val TORLORAOR_DOC = "torloraor"
    private const val PORLORBOR_DOC_STATE = "porlorborstate"
    private const val TORLORAOR_DOC_STATE = "torloraorstate"

    private val databaseManager = null

    fun saveSummaryList(list: List<CartItem>) {
       // databaseManager.deleteBy(SummaryEntity::class.java)

        val items = list.map {
            SummaryEntity(
                    type = it.type,
                    code = it.code,
                    title = it.title,
                    price = it._price,
                    isChecked = it.isChecked,
                    isRecentlyAdd = it.isRecentlyAdd,
                    defaultValue = it.defaultValue,
                    cStatus_code = it.cStatus_code
            )
        }

       // databaseManager.save(SummaryEntity::class.java, items)
    }

//    fun getSummaryList() = databaseManager.findAllBy(SummaryEntity::class.java)
//            ?.map {
//                CartItem(
//                        type = it.type,
//                        code = it.code,
//                        title = it.title,
//                        _price = it.price,
//                        isChecked = it.isChecked,
//                        isRecentlyAdd = it.isRecentlyAdd,
//                        defaultValue = it.defaultValue ,
//                        cStatus_code = it.cStatus_code
//                )
//            }

    fun saveStatePorlorborDocs(base64List: List<String>) {
       // databaseManager.deleteBy(DocumentEntity::class.java, "type", PORLORBOR_DOC_STATE)

        val item = base64List.map {
            DocumentEntity(base64 = it, type = PORLORBOR_DOC_STATE)
        }

        //databaseManager.save(DocumentEntity::class.java, item)
    }

    fun saveStateTorloraorDocs(base64List: List<String>) {
       // databaseManager.deleteBy(DocumentEntity::class.java, "type", TORLORAOR_DOC_STATE)

        val item = base64List.map {
            DocumentEntity(base64 = it, type = TORLORAOR_DOC_STATE)
        }

      //  databaseManager.save(DocumentEntity::class.java, item)
    }

    fun savePorlorborDocs(base64List: List<String>) {
       // databaseManager.deleteBy(DocumentEntity::class.java, "type", PORLORBOR_DOC)

        val items = base64List.map {
            DocumentEntity(base64 = it, type = PORLORBOR_DOC)
        }

       // databaseManager.save(DocumentEntity::class.java, items)
    }

    fun saveTorloraorDocs(base64List: List<String>) {
       // databaseManager.deleteBy(DocumentEntity::class.java, "type", TORLORAOR_DOC)

        val items = base64List.map {
            DocumentEntity(base64 = it, type = TORLORAOR_DOC)
        }

       // databaseManager.save(DocumentEntity::class.java, items)
    }

    fun deletePorlorborDocsState() = null

    fun deleteTorloraorDocsState() = null

    fun getDocumentList() = null

    fun getPorlorborDocuments() = null

    fun getTorloraorDocuments() = null

    fun getPorlorborDocumentsState() = null

    fun getTorloraorDocumentsState() = null

    fun savePaymentMethod(paymentEntity: PaymentEntity) {
      //  databaseManager.deleteBy(PaymentEntity::class.java)

        val list = listOf(paymentEntity)
      //  databaseManager.save(PaymentEntity::class.java, list)
    }

    fun getPaymentMethod(): PaymentEntity? {
        return null
    }
}