package tlt.th.co.toyotaleasing.modules.custloan.loanmodule.carloan.autoview

import android.annotation.TargetApi
import android.app.Dialog
import android.content.Context
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.util.AttributeSet
import android.view.*
import android.widget.FrameLayout
import kotlinx.android.synthetic.main.fragment_dialog_normal.view.*
import kotlinx.android.synthetic.main.item_file_gallery.view.*
import kotlinx.android.synthetic.main.view_recyclerview.view.*
import tlt.th.co.toyotaleasing.R
import tlt.th.co.toyotaleasing.common.extension.loadImageByBase64
import tlt.th.co.toyotaleasing.modules.custloan.loanmodule.carloan.common.ItemFilesData

class RealtimePDFAddGalleryWidget : FrameLayout {

    var maximumDoc = 3

//    lateinit var docBase64UriList: MutableList<String>
    lateinit var docBase64UriList: MutableList<ItemFilesData>


    private lateinit var onDocChangedListener: (base64List : List<ItemFilesData>) -> Unit
    private lateinit var onDocRemoveListener: ((String) -> Unit)
//    private lateinit var onDocLimitListener: ((Boolean) -> Unit)


    val galleryAdapter by lazy {
        GalleryAdapter()
    }

    @JvmOverloads
    constructor(
            context: Context,
            attrs: AttributeSet? = null,
            defStyleAttr: Int = 0)
            : super(context, attrs, defStyleAttr) {
        init(attrs)
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    constructor(
            context: Context,
            attrs: AttributeSet?,
            defStyleAttr: Int,
            defStyleRes: Int)
            : super(context, attrs, defStyleAttr, defStyleRes) {
        init(attrs)
    }

    private fun init(attrs: AttributeSet?) {
        inflate(context, R.layout.view_recyclerview, this)
        val a = context.obtainStyledAttributes(attrs,
                R.styleable.AddGalleryWidget)

        maximumDoc = a.getInt(
                R.styleable.AddGalleryWidget_maximumImage,
                0)
        docBase64UriList =  MutableList(maximumDoc){ItemFilesData("","") }  //MutableList(maximumDoc ){  }
        initRecyclerView()

        a.recycle()
    }


//    fun setOnAddDocListener(onAdd: (Int) -> Unit) {
//        onDocAddedListener = onAdd
//    }

    fun getDocList(): List<ItemFilesData> {
        return docBase64UriList.filter { it.fBase64 != "" }
    }

//    fun onDocChanged(listener: (base64List: List<String>) -> Unit) {
//        onDocChangedListener = listener
//    }

    fun triggerDocChanged() {
        if (!this::onDocChangedListener.isInitialized) {
            return
        }
        onDocChangedListener.invoke(getDocList())
    }


    fun triggerRemoveDocChanged(onDelete: (String) -> Unit) {
        onDocRemoveListener  = onDelete
    }

//    fun triggerOverLimitDocAdd(onStatus : (Boolean) -> Unit) {
//        onDocLimitListener  = onStatus
//    }

    private fun initRecyclerView() {
        custom_view_rv.layoutManager = GridLayoutManager(this.context, 3)
        custom_view_rv.adapter = galleryAdapter
    }

    inner class GalleryAdapter : RecyclerView.Adapter<GalleryViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GalleryViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.item_file_gallery, parent, false)
            return GalleryViewHolder(view)
        }

        override fun getItemCount(): Int {
            return maximumDoc
        }

        override fun onBindViewHolder(holder: GalleryViewHolder, position: Int) {
            if (docBase64UriList.get(position).fBase64 != "") {
                holder.loadDoc(docBase64UriList.get(position).fName , docBase64UriList.get(position).fBase64   )
                holder.itemView.txt_name.text = docBase64UriList.get(position).fName
                holder.itemView.gallery_close_iv.visibility = View.VISIBLE
            } else {
                holder.loadDefaultDoc()
                holder.itemView.gallery_close_iv.visibility = View.GONE
            }

            holder.itemView.gallery_close_iv.setOnClickListener {

                showAskForDelete(holder.adapterPosition)
//                removeDocGallery(holder.adapterPosition)
            }
        }


        fun addDocGallery( docName2 : String  , base64Doc: String , docName1: String ) {

                if(CheckFreeDoc()) {
                    // set doc name
//                    val position = if(docBase64UriList.filter { it.fBase64 != "" }.size != 0 )  { docBase64UriList.indexOfFirst { it.fBase64 == "" }  } else { 0 }
                    val position = docBase64UriList.indexOfFirst { it.fDocName == docName1 }
                    if(position == -1){
                        val pos = docBase64UriList.indexOfFirst { it.fDocName == "" }
                        docBase64UriList[pos].fName = docName2
                        docBase64UriList[pos].fBase64 = base64Doc
                        docBase64UriList[pos].fDocName = docName1
                    }else{
                        docBase64UriList[position].fName = docName2
                        docBase64UriList[position].fBase64 = base64Doc
                        docBase64UriList[position].fDocName = docName1
                    }
                    notifyItemChanged(position)
                    triggerDocChanged()
                }else{
//                    onDocLimitListener.invoke(true)
                }
        }

        private fun showAskForDelete( position : Int) {
            try {
                val dialog = LayoutInflater.from(context!!).inflate(R.layout.fragment_dialog_remove_file, null)
                val mBuilder = AlertDialog.Builder(context!!).setView(dialog)
                mBuilder.create().window!!.requestFeature(Window.FEATURE_NO_TITLE)
                mBuilder.create().window!!.setBackgroundDrawableResource(android.R.color.transparent)
                mBuilder.create().window!!.setLayout(resources.displayMetrics.widthPixels, resources.displayMetrics.heightPixels)
                mBuilder.create().window!!.setGravity(Gravity.CENTER);
                val alertDialog = mBuilder.show()
               dialog!!.title.setText(R.string.dialog_delete_file_pdf)
               dialog!!.btn_cancel.setText(R.string.dialog_button_cancel)
               dialog!!.btn_confirm.setText(R.string.dialog_button_ok)
               dialog!!.btn_cancel.setOnClickListener {
                    alertDialog.dismiss()
                }
               dialog!!.btn_confirm.setOnClickListener {
                    removeDocGallery(position)
                    alertDialog.dismiss()
                }
            } catch (e: Exception) {
                e.message
            }
        }

        private fun removeDocGallery(position: Int) {
            docBase64UriList[position].fBase64 = ""
            docBase64UriList[position].fName = ""
            notifyItemChanged(position)
            triggerDocChanged()
            onDocRemoveListener.invoke(docBase64UriList[position].fDocName)
        }

        private fun CheckFreeDoc() : Boolean{
            var emptyDoc = 0
            for (position in 0 until docBase64UriList.size) {
                if( docBase64UriList[position].fBase64 == "" ||  docBase64UriList[position].fBase64 == null ){
                    emptyDoc +=1
                }
            }
            if( emptyDoc > 0 )
                return true
            else
                return false
        }
    }

//    data class SyncDocModel(
//           var position: Int ,
//           var base64Doc: String
//    )

    inner class GalleryViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun loadDoc(name : String ,base64Doc: String  ) {
            try{
                if(base64Doc != "PDF") {
                    itemView.gallery_iv.loadImageByBase64(base64Doc)
                    itemView.txt_name.text = name
                }else {
                    itemView.gallery_iv.setImageResource(R.drawable.icon_pdf_uploaded)
                    itemView.txt_name.text = ""
                }

            }catch (e : Exception){

            }

        }

        fun loadDefaultDoc() {
            itemView.gallery_iv.setImageResource(R.drawable.ico_pdf_no_upload)
            itemView.txt_name.text = ""
        }
    }

}