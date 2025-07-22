package tlt.th.co.toyotaleasing.manager



object FlowManager {

    const val REGISTER_FLOW = "REGISTER_FLOW"
    const val FORGOTPIN_FLOW = "FORGOTPIN_FLOW"
    const val RESET_PINCODE_FLOW = "RESET_PINCODE_FLOW"
    const val INSURANCE_PAYMENT_FLOW = "INSURANCE_PAYMENT_FLOW"
    const val TAX_PAYMENT_FLOW = "TAX_PAYMENT_FLOW"
    const val INSTALLMENT_PAYMENT_FLOW = "INSTALLMENT_PAYMENT_FLOW"

    fun isRegisterFlow() = false

    fun isForgotPinFlow() = false

    fun isResetPinFlow() = false

    fun changeToRegisterFlow() {
        //DatabaseManager.getInstance().saveFlowState(REGISTER_FLOW)
    }

    fun changeToForgotPinFlow() {
        //DatabaseManager.getInstance().saveFlowState(FORGOTPIN_FLOW)
    }

    fun changeToResetPinFlow() {
        //DatabaseManager.getInstance().saveFlowState(RESET_PINCODE_FLOW)
    }
}