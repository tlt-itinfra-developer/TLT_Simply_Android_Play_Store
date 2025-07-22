package tlt.th.co.toyotaleasing.modules.insurance.quotation

import androidx.lifecycle.MediatorLiveData
import android.content.Context
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.TextView
import kotlinx.android.synthetic.main.item_insurance.view.*
import tlt.th.co.toyotaleasing.R
import tlt.th.co.toyotaleasing.common.extension.customAdapter


class QuotationAdapter(private val itemList: ArrayList<QuotationModel>,
                       val liveData: MediatorLiveData<ArrayList<QuotationModel>>,
                       val itemCompany: List<String>,
                       val itemConverageType: List<String>,
                       val itemPayment: List<String>
) : RecyclerView.Adapter<QuotationAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_insurance, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount() = itemList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(itemList[position], position)
    }

    inner class ViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        fun bind(item: QuotationModel, position: Int) {

            val position_text = itemView.context.getText(R.string.iten_insurance)
            itemView.item_insurance_position.text = String.format("$position_text", position + 1)

            itemView.item_insurance_company.apply {
                customAdapter(itemView.context, itemCompany)
//                adapter = getArrayAdapter(itemView.context, itemCompany)
                onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                    override fun onNothingSelected(parent: AdapterView<*>?) {}
                    override fun onItemSelected(parent: AdapterView<*>?, view: View?, positionSelect: Int, id: Long) {
                        if (positionSelect > 0) {
                            itemList[position].company = positionSelect
                            liveData.value = itemList
                            val tv = view as TextView
                            tv.setTextColor(ContextCompat.getColor(itemView.context, R.color.brownish_grey))
                        }
                    }

                }
                itemView.item_insurance_company.setSelection(itemList[position].company)
            }

            itemView.item_insurance_converageType.apply {
                customAdapter(itemView.context, itemConverageType)
//                adapter = getArrayAdapter(itemView.context, itemConverageType)
                onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                    override fun onNothingSelected(parent: AdapterView<*>?) {}
                    override fun onItemSelected(parent: AdapterView<*>?, view: View?, positionSelect: Int, id: Long) {
                        if (positionSelect > 0) {
                            itemList[position].converageType = positionSelect
                            liveData.value = itemList
                            val tv = view as TextView
                            tv.setTextColor(ContextCompat.getColor(itemView.context, R.color.brownish_grey))
                        }
                    }

                }
                itemView.item_insurance_converageType.setSelection(itemList[position].converageType)
            }

            itemView.item_insurance_payment.apply {
                customAdapter(itemView.context, itemPayment)
//                adapter = getArrayAdapter(itemView.context, itemPayment)
                onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                    override fun onNothingSelected(parent: AdapterView<*>?) {}
                    override fun onItemSelected(parent: AdapterView<*>?, view: View?, positionSelect: Int, id: Long) {
                        if (positionSelect > 0) {
                            itemList[position].payment = positionSelect
                            liveData.value = itemList
                            val tv = view as TextView
                            tv.setTextColor(ContextCompat.getColor(itemView.context, R.color.brownish_grey))
                        }
                    }
                }
                itemView.item_insurance_payment.setSelection(itemList[position].payment)
            }

            if (position == 0) {
                itemView.item_insurance_del.visibility = View.INVISIBLE
            } else {
                itemView.item_insurance_del.visibility = View.VISIBLE
            }
            itemView.item_insurance_del.setOnClickListener {
                itemList.remove(item)
                liveData.value = itemList
                notifyItemRemoved(position)
                notifyItemRangeRemoved(position, itemList.size)
//                notifyDataSetChanged()
            }

        }
    }

//    fun getArrayAdapter(context: Context, arrayList: List<String>): ArrayAdapter<String> {
//        return object : ArrayAdapter<String>(
//                context, R.layout.item_spinner_two, arrayList) {
//            override fun isEnabled(position: Int): Boolean {
//                return position != 0
//            }
//
//            override fun getDropDownView(position: Int, convertView: View?,
//                                         parent: ViewGroup): View {
//                val view = super.getDropDownView(position, convertView, parent)
//                val tv = view as TextView
//                if (position == 0) {
//                    // Set the hint text color gray
//                    tv.setTextColor(ContextCompat.getColor(context, R.color.grey))
//                } else {
//                    tv.setTextColor(ContextCompat.getColor(context, R.color.brownish_grey))
//                }
//                return view
//            }
//        }
//    }

    fun getListItem(): ArrayList<QuotationModel> {
        return itemList
    }
}