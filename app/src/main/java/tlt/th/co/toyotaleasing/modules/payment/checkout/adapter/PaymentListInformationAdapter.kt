package tlt.th.co.toyotaleasing.modules.payment.checkout.adapter

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.item_payment_list_view.view.*
import tlt.th.co.toyotaleasing.R
import tlt.th.co.toyotaleasing.modules.payment.checkout.common.CheckoutViewModel

class PaymentListInformationAdapter(private val listInformation: MutableList<CheckoutViewModel.Model.ListInformationModel> = mutableListOf()) : RecyclerView.Adapter<PaymentListInformationAdapter.PaymentHeaderInformationViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PaymentHeaderInformationViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_payment_list_view, parent, false)
        return PaymentHeaderInformationViewHolder(view)
    }

    override fun getItemCount(): Int {
        return listInformation.size
    }

    override fun onBindViewHolder(holder: PaymentHeaderInformationViewHolder, position: Int) {
        holder.bind(listInformation[position])
    }

    fun updateItems(listInformation: MutableList<CheckoutViewModel.Model.ListInformationModel> = arrayListOf()) {
        this.listInformation.clear()
        this.listInformation.addAll(listInformation)
        notifyDataSetChanged()
    }

    inner class PaymentHeaderInformationViewHolder(var view: View) : RecyclerView.ViewHolder(view) {

        fun bind(listInformationModel: CheckoutViewModel.Model.ListInformationModel) {
            view.txt_payment_list_name_title.text = listInformationModel.title
            view.txt_payment_list_price_tv.text = listInformationModel.price
        }
    }
}