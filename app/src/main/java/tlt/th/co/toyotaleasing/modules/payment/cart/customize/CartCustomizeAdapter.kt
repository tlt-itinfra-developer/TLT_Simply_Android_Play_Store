package tlt.th.co.toyotaleasing.modules.payment.cart.customize

import androidx.appcompat.widget.AppCompatCheckBox
import androidx.recyclerview.widget.RecyclerView
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import kotlinx.android.synthetic.main.item_summary_edit_payment_normal.view.*
import kotlinx.android.synthetic.main.item_summary_edit_payment_select.view.*
import tlt.th.co.toyotaleasing.R
import tlt.th.co.toyotaleasing.common.extension.toNumberFormat
import tlt.th.co.toyotaleasing.common.extension.toNumberFormatWithoutComma
import tlt.th.co.toyotaleasing.common.extension.toStringWithoutComma
import tlt.th.co.toyotaleasing.modules.payment.cart.common.CartItem

class CartCustomizeAdapter(private val itemList: MutableList<CartItem> = mutableListOf(),
                           private val updateListner: (index: Int, item: CartItem) -> Unit,
                           private val removeListner: (index: Int, item: CartItem) -> Unit,
                           private val updateOptionItemListener: (Int, CartItem) -> Unit ,
                           private val PAYOFF_TYPE : Boolean = false ) : RecyclerView.Adapter<CartCustomizeAdapter.ViewHolder>() {

    private val NORMAL_TYPE = R.layout.item_summary_edit_payment_normal
    private val SELECT_TYPE = R.layout.item_summary_edit_payment_select

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return when (viewType) {
            NORMAL_TYPE -> {
                val view = LayoutInflater.from(parent.context).inflate(NORMAL_TYPE, parent, false)
                ViewHolder(view)
            }
            else -> {
                val view = LayoutInflater.from(parent.context).inflate(SELECT_TYPE, parent, false)
                ViewHolder(view)
            }
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        when (getItemViewType(position)) {
            NORMAL_TYPE -> {
                holder.bind()
            }
            else -> {
                holder.bindSelectType(itemList[position])
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (itemList[position].isRecentlyAdd) {
            SELECT_TYPE
        } else {
            NORMAL_TYPE
        }
    }

    override fun getItemCount(): Int = itemList.size

    fun updateItems(items: MutableList<CartItem>) {
        itemList.clear()
        itemList.addAll(items)
        notifyDataSetChanged()
    }

    fun getItems() = itemList

    inner class ViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {

        fun bind() {
            val item = itemList[adapterPosition]

            val defaultValue = item._price

            view.text_summary_payment_item_paid.isChecked = item.isChecked
            view.text_summary_payment_item_paid.text = item.title
            view.edittext_summary_payment_item_paid_money.setText(item.price)

            if(PAYOFF_TYPE){
                if( (item.cStatus_code  == "41")
                        && ( (item.code == "302" && item.type == "60" )
                                || (item.code == "502" && item.type == "10")
                                || (item.code == "013" && item.type == "10"  ) )) {
                    view.text_summary_payment_item_paid.isEnabled = false
                    view.edittext_summary_payment_item_paid_money.isEnabled  = false
                }else if( (item.cStatus_code  != "41")
                        && ( (item.code == "302" && item.type == "60" )
                                || (item.code == "013" && item.type == "10"  ) )) {
                    view.text_summary_payment_item_paid.isEnabled = false
                    view.edittext_summary_payment_item_paid_money.isEnabled  = false
                }else{
                    view.text_summary_payment_item_paid.isEnabled = true
                    view.edittext_summary_payment_item_paid_money.isEnabled  = true
                }
            }else{
                if((item.cStatus_code  == "41")
                        && ( (item.code == "302" && item.type == "60" )
                                || (item.code == "502" && item.type == "10")
                                || (item.code == "013" && item.type == "10"  ) )) {
                    view.text_summary_payment_item_paid.isEnabled = false
                    view.edittext_summary_payment_item_paid_money.isEnabled  = false
                }else{
                    view.text_summary_payment_item_paid.isEnabled = true
                    view.edittext_summary_payment_item_paid_money.isEnabled  = true
                }
            }


//            if(( PAYOFF_TYPE || item.cStatus_code  == "41")
//                    && ((item.code == "302" && item.type == "60" )
//                            || (item.code == "502" && item.type == "10")
//                            || (item.code == "013" && item.type == "10") ) ) {
//                view.text_summary_payment_item_paid.isEnabled = false
//                view.edittext_summary_payment_item_paid_money.isEnabled  = false
//            }else{
//                view.text_summary_payment_item_paid.isEnabled = true
//                view.edittext_summary_payment_item_paid_money.isEnabled  = true
//            }

            view.text_summary_payment_item_paid.setOnClickListener {
                val checkBox = it as AppCompatCheckBox
                item.isChecked = checkBox.isChecked
                updateListner.invoke(adapterPosition, item)
            }

            view.edittext_summary_payment_item_paid_money.removeTextChangedListener(onTextChanged)
            view.edittext_summary_payment_item_paid_money.addTextChangedListener(onTextChanged)
            view.edittext_summary_payment_item_paid_money.onFocusChangeListener = onFocusChanged
        }

        fun bindSelectType(item: CartItem) {
            view.edittext_summary_payment.setText(item.title)
            view.edittext_summary_payment_price.setText(item.price)

            val defaultValue = item._price

            view.btn_clear.setOnClickListener {
                removeListner.invoke(adapterPosition, item)
            }

            view.edittext_summary_payment.setOnClickListener {
                updateOptionItemListener.invoke(adapterPosition, item)
            }

            view.edittext_summary_payment_price.setText(item.price)

            view.edittext_summary_payment_price.removeTextChangedListener(onTextChanged)
            view.edittext_summary_payment_price.addTextChangedListener(onTextChanged)
            view.edittext_summary_payment_price.onFocusChangeListener = onFocusChanged


        }

        private val onTextChanged = object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (adapterPosition == -1) {
                    return
                }

                val item = itemList[adapterPosition]
//                if (s.toString().toStringWithoutComma().toDoubleOrNull() is Double) {
                if (item.code.toString() == "072" || item.code.toString() == "013") {
                    if (s.toString().toNumberFormatWithoutComma().toDouble() <= item.defaultValue.toDouble()) {
                        item._price = s.toString().toNumberFormatWithoutComma()
                    } else {
                        item._price = item.defaultValue
                    }
                } else {
                    item._price = s.toString().toNumberFormatWithoutComma()
                }

                if(item.price == "0.00"){
                    item._price = ""
                }
//                } else {
//                    val text = s.toString().substring(0, s!!.length - 1)
//                    item._price = text.toNumberFormatWithoutComma()
//                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }
        }

        private val onFocusChanged = View.OnFocusChangeListener { v, hasFocus ->
            if (adapterPosition == -1) {
                return@OnFocusChangeListener
            }

            val item = itemList[adapterPosition]

            val edittext = v as EditText
            var price = ""
            price = if (item.code.toString() == "072" || item.code.toString() == "013" ) {
                if (edittext.text.toString().toNumberFormatWithoutComma().toDouble() <= item.defaultValue.toDouble()) {
                    edittext.text.toString().toNumberFormatWithoutComma()
                } else {
                    item.defaultValue
                }
            } else {
                edittext.text.toString().toNumberFormatWithoutComma()
            }


            if (hasFocus) {
                edittext.setText(price)
            } else {
                edittext.setText(price.toNumberFormat())
            }

            if(price == "0.00"){
                edittext.setText("")
            }
        }
    }
}