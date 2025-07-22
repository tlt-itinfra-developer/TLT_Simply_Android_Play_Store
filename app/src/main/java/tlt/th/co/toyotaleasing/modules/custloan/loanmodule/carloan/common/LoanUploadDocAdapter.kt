package tlt.th.co.toyotaleasing.modules.custloan.loanmodule.carloan.common

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.item_loan_upload_document.view.*
import tlt.th.co.toyotaleasing.R
import tlt.th.co.toyotaleasing.modules.custloan.loanmodule.carloan.LoanUploadDocumentViewModel

class LoanUploadDocAdapter(private val  items: List<LoanUploadDocumentViewModel.DocItem>,
                           private val listener: Listener) : RecyclerView.Adapter<LoanUploadDocAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_loan_upload_document, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind()
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bind() {
            val item = items[adapterPosition]

            itemView.txt_doc_name.text = item.dOCDESC
            if("BANK".equals(item.dOCTYPE)){
                 itemView.icon_type.setImageResource(R.drawable.icon_bank)
            }else   if("HOME".equals(item.dOCTYPE)){
                itemView.icon_type.setImageResource(R.drawable.icon_home_regis)
            }else   if("INCOME".equals(item.dOCTYPE)){
                itemView.icon_type.setImageResource(R.drawable.icon_income)
            }else {
                itemView.icon_type.setImageResource(R.drawable.icon_other)
            }

            itemView.txt_limit.text = item.summary

        }

        private val onDetailClick = View.OnClickListener {
            val item = items[adapterPosition]
            listener.onTopicClick(adapterPosition, item)
        }

        init {
            itemView.layout.setOnClickListener(onDetailClick)
        }
    }

    interface Listener {
        fun onTopicClick(index: Int, item: LoanUploadDocumentViewModel.DocItem)
    }
}