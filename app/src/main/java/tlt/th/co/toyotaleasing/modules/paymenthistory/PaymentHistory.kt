package tlt.th.co.toyotaleasing.modules.paymenthistory

data class PaymentHistory(
        var total: String = "",
        var paymentDate: String = "",
        var paymentBank: String = "",
        var paymentReceiptDate: String = "",
        var canDownloadReceipt: Boolean = false,
        var isExpanded: Boolean = false,
        var year: String = ""
)