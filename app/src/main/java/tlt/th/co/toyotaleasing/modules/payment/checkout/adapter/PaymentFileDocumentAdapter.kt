package tlt.th.co.toyotaleasing.modules.payment.checkout.adapter

import androidx.annotation.NonNull
import androidx.annotation.Nullable
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.item_payment_document_view.view.*
import tlt.th.co.toyotaleasing.R
import tlt.th.co.toyotaleasing.common.extension.loadImageByBase64

class PaymentFileDocumentAdapter(@Nullable private val fileList: List<String> = listOf(),
                                 @NonNull var onClick: (String, Int) -> Unit) : RecyclerView.Adapter<PaymentFileDocumentAdapter.PaymentFileDocumentViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PaymentFileDocumentViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_payment_document_view, parent, false)
        return PaymentFileDocumentViewHolder(view)
    }

    override fun getItemCount(): Int {
        return fileList.size
    }

    override fun onBindViewHolder(holder: PaymentFileDocumentViewHolder, position: Int) {
        holder.bind()

        holder.itemView.setOnClickListener {
            onClick.invoke(fileList[holder.adapterPosition], holder.adapterPosition)
        }
    }

    inner class PaymentFileDocumentViewHolder(val view: View) : RecyclerView.ViewHolder(view) {

        fun bind() {
            val item = fileList[adapterPosition]
//            view.iv_file_document.loadImageByUri(fileDocumentModel)
            view.iv_file_document.loadImageByBase64(item)
        }
    }
}