package tlt.th.co.toyotaleasing.modules.payment.checkout.adapter

import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.item_payment_method.view.*
import tlt.th.co.toyotaleasing.R
import tlt.th.co.toyotaleasing.common.extension.loadImageByBase64
import tlt.th.co.toyotaleasing.modules.payment.checkout.common.CheckoutViewModel

class PaymentMethodAdapter(private val checkoutMethodList: MutableList<CheckoutViewModel.Model.PaymentMethodModel> = arrayListOf(),
                           private var onClick: (Int, CheckoutViewModel.Model.PaymentMethodModel) -> Unit) : RecyclerView.Adapter<PaymentMethodAdapter.PaymentMethodViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PaymentMethodViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_payment_method, parent, false)
        return PaymentMethodViewHolder(view)
    }

    override fun getItemCount(): Int {
        return checkoutMethodList.size
    }

    override fun onBindViewHolder(holder: PaymentMethodViewHolder, position: Int) {
        holder.bind()
    }

    inner class PaymentMethodViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        fun bind() {
            val paymentMethodModel = checkoutMethodList[adapterPosition]

            if (paymentMethodModel.isSelected) {
                view.layout_payment_bank.background = ContextCompat.getDrawable(view.context, R.drawable.shape_filter_car_selected)
            } else {
                view.layout_payment_bank.background = ContextCompat.getDrawable(view.context, R.drawable.shape_filter_car_unselect)
            }

            view.iv_payment_bank.loadImageByBase64(paymentMethodModel.imageUrl)
            view.txt_payment_bank.text = paymentMethodModel.paymentBankName

            view.setOnClickListener {
                if (paymentMethodModel.isClicked.toLowerCase() == "y") {
                    unselect()
                    select(paymentMethodModel)
                    onClick.invoke(adapterPosition, paymentMethodModel)
                }
            }
        }

        private fun unselect() {
            val index = checkoutMethodList.withIndex().indexOfFirst { it.value.isSelected }

            if (index == -1) {
                return
            }

            checkoutMethodList[index].isSelected = false
            notifyItemChanged(index)
        }

        private fun select(checkoutMethodModel: CheckoutViewModel.Model.PaymentMethodModel) {
            checkoutMethodModel.isSelected = true
            val index = checkoutMethodList.withIndex().indexOfFirst { it.value.isSelected }
            notifyItemChanged(index)
        }
    }
}