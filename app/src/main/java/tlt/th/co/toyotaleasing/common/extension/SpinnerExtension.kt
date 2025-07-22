package tlt.th.co.toyotaleasing.common.extension

import android.content.Context
import androidx.core.content.ContextCompat
import androidx.appcompat.widget.AppCompatSpinner
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import tlt.th.co.toyotaleasing.R

fun AppCompatSpinner.customAdapter(context: Context, arrayList: List<String>){
    this.adapter = object : ArrayAdapter<String>(
            context, R.layout.item_spinner_two, arrayList) {
        override fun isEnabled(position: Int): Boolean {
            return position != 0
        }

        override fun getDropDownView(position: Int, convertView: View?,
                                     parent: ViewGroup): View {
            val view = super.getDropDownView(position, convertView, parent)
            val tv = view as TextView
            if (position == 0) {
                // Set the hint text color gray
                tv.setTextColor(ContextCompat.getColor(context, R.color.grey))
            } else {
                tv.setTextColor(ContextCompat.getColor(context, R.color.brownish_grey))
            }
            return view
        }
    }
}