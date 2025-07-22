package tlt.th.co.toyotaleasing.modules.tax.utils

import tlt.th.co.toyotaleasing.common.extension.toNumberFormatWithoutComma
import tlt.th.co.toyotaleasing.model.response.GetDataTaxResponse

class TaxDataHelper(private val data: GetDataTaxResponse) {

    fun isWarningDocument(): Boolean {
        val isPorlorborAlert = isDocumentAlert(data.flagPORLORBOR) || true
        val isTorloraorAlert = isDocumentAlert(data.flagTORLORAOR)
        val isGasAlert = isDocumentAlert(data.flagGAS)

        return if (isTorloraor() && isGas()) {
            isPorlorborAlert || isTorloraorAlert || isGasAlert
        } else if (!isTorloraor()) {
            isPorlorborAlert || isGasAlert
        } else if (!isGas()) {
            isPorlorborAlert || isTorloraorAlert
        } else {
            isPorlorborAlert
        }
    }

    fun isPorlorborPending() = data.flagPORLORBOR == "P"

    fun isPorlorborRequesting() = data.flagPORLORBOR == "R"

    private fun isDocumentAlert(data: String?) = data != "V" && data != "N"

    fun isTorloraor() = data.cVEHICLEAGE?.isNotEmpty() ?: false && data.cVEHICLEAGE?.toNumberFormatWithoutComma()?.toFloat()?.toInt() ?: 0 >= 7

    fun isGas() = data.cVEHICLEGAS?.isNotEmpty() ?: false && data.cVEHICLEGAS != "NORMAL"

//    fun isOverThreeTeae() = data.cVEHICLEGAS?.isNotEmpty()
}