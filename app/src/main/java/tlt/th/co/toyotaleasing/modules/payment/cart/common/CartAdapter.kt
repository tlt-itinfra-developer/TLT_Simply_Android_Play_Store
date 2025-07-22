package tlt.th.co.toyotaleasing.modules.payment.cart.common

import androidx.appcompat.widget.AppCompatCheckBox
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.item_cart.view.*
import kotlinx.android.synthetic.main.item_summary_edit_payment_select.view.*
import tlt.th.co.toyotaleasing.R
import tlt.th.co.toyotaleasing.common.extension.setText

class CartAdapter(private val itemList: MutableList<CartItem>,
                  private val listener: (index: Int, item: CartItem) -> Unit ,
                  private val PAYOFF_TYPE : Boolean = false ) : RecyclerView.Adapter<CartAdapter.ViewHolder>() {

    private val PORLORBOR_ID = "432"

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_cart, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = itemList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind()
    }

    fun updateItems(items: List<CartItem>) {
        itemList.clear()
        itemList.addAll(items)
        notifyDataSetChanged()
    }

    inner class ViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {
        fun bind() {
            val item = itemList[adapterPosition]
            view.text_summary_payment_item_paid.isChecked = item.isChecked
            view.text_summary_payment_item_paid.text = item.title
            view.text_summary_payment_item_paid_money.setText("", item.price, view.context.getString(R.string.currency, ""), R.color.terracotta)


            if(PAYOFF_TYPE){
                if( (item.cStatus_code  == "41")
                        && ( (item.code == "302" && item.type == "60" )
                                || (item.code == "502" && item.type == "10")
                                || (item.code == "013" && item.type == "10"  ) )) {
                    view.text_summary_payment_item_paid.isEnabled = false
                    view.text_summary_payment_item_paid_money.isEnabled  = false
                }else if( (item.cStatus_code  != "41")
                        && ( (item.code == "302" && item.type == "60" )
                                || (item.code == "013" && item.type == "10"  ) )) {
                    view.text_summary_payment_item_paid.isEnabled = false
                    view.text_summary_payment_item_paid_money.isEnabled  = false
                }else{
                    view.text_summary_payment_item_paid.isEnabled = true
                    view.text_summary_payment_item_paid_money.isEnabled  = true
                }
            }else{
                if((item.cStatus_code  == "41")
                        && ( (item.code == "302" && item.type == "60" )
                                || (item.code == "502" && item.type == "10")
                                || (item.code == "013" && item.type == "10"  ) )) {
                    view.text_summary_payment_item_paid.isEnabled = false
                    view.text_summary_payment_item_paid_money.isEnabled  = false
                }else{
                    view.text_summary_payment_item_paid.isEnabled = true
                    view.text_summary_payment_item_paid_money.isEnabled  = true
                }
            }

//
//            if( (PAYOFF_TYPE || item.cStatus_code  == "41")
//                    && ( (item.code == "302" && item.type == "60" )
//                            || (item.code == "502" && item.type == "10")
//                            || (item.code == "013" && item.type == "10"  ) )) {
//                view.text_summary_payment_item_paid.isEnabled = false
//                view.text_summary_payment_item_paid_money.isEnabled  = false
//            }else{
//                view.text_summary_payment_item_paid.isEnabled = true
//                view.text_summary_payment_item_paid_money.isEnabled  = true
//            }


            view.text_summary_payment_item_paid.setOnClickListener {
                val checkBox = it as AppCompatCheckBox
                item.isChecked = checkBox.isChecked
                listener.invoke(adapterPosition, item)
            }

        }
    }
}