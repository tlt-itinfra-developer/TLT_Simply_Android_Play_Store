package tlt.th.co.toyotaleasing.model.entity.payment



open class PaymentEntity(
        var amount : String = "",
        var paymentMethodId: String = "",
        var paymentMethodName : String = "",
        var description : String = "" ,
        var paymentURL : String = "" ,
        var callbankDesc : String = ""
) 