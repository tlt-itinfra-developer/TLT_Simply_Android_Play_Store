package tlt.th.co.toyotaleasing.modules.custloan.loanmodule.carloan

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.util.Base64
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import kotlinx.android.synthetic.main.fragment_dialog_announcement.view.*
import kotlinx.android.synthetic.main.fragment_dialog_normal.view.*
import kotlinx.android.synthetic.main.fragment_sync_document_type.*
import tlt.th.co.toyotaleasing.R
import tlt.th.co.toyotaleasing.common.base.BaseActivity
import tlt.th.co.toyotaleasing.common.dialog.MsgdescNormalDialog
import tlt.th.co.toyotaleasing.common.dialog.NormalDialogFragment
import tlt.th.co.toyotaleasing.modules.custloan.loanmodule.carloan.common.ItemFilesData
import tlt.th.co.toyotaleasing.modules.custloan.loanmodule.carloan.common.RemoveFileDialog
import tlt.th.co.toyotaleasing.util.PdfUtils
import java.io.ByteArrayOutputStream
import java.io.FileNotFoundException
import java.io.InputStream


class SyncDocumentActivity   : BaseActivity() , RemoveFileDialog.Listener , MsgdescNormalDialog.Listener  {

    private val data_url by lazy {
        intent?.getStringExtra(REF_URL) ?: ""
    }

    private val data_desc by lazy {
        intent?.getStringExtra(REF_DESC) ?: ""
    }

    private val isFromAddOther by lazy {
        intent.getBooleanExtra(OTHER, false)
    }

    var IMG_MAX : Int = 1
    var IMG_MIN : Int = 1
    var DOC_MAX : Int = 1
    var DOC_MIN : Int = 1
    var listSync : List<SyncDocumentViewModel.DocUploadItem>  = listOf()

    private var masterImage : List<SyncDocumentViewModel.DocItem> = listOf()
    private var pdfDocforDelete : String = ""

    private val type_doc by lazy {
        intent?.getStringExtra(TYPE_DOC) ?: ""
    }

    private val title_header by lazy {
        intent?.getStringExtra(TITLE_HEADER) ?: ""
    }

    private val data_extra by lazy {
        intent?.getStringExtra(REF_ID) ?: ""
    }

    private val viewModel by lazy {
        ViewModelProviders.of(this).get(SyncDocumentViewModel::class.java)
    }

    private fun userGrantPermission(activity: Activity?, callback: () -> Unit) {
        Dexter.withActivity(activity)
                .withPermissions(Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE)
                .withListener(object : MultiplePermissionsListener {
                    override fun onPermissionsChecked(report: MultiplePermissionsReport) {
                        if (report.isAnyPermissionPermanentlyDenied) {
                            return
                        }

                        callback.invoke()
                    }

                    override fun onPermissionRationaleShouldBeShown(permissions: List<PermissionRequest>, token: PermissionToken) {
                        token.continuePermissionRequest()
                    }
                }).check()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_sync_document_type)

        initViewModel()
        initInstance()
        viewModel.getMasterDocType()

    }


    fun initInstance() {

        title_add_image.text = title_header
        txt_ref_value.text = data_extra

        if("BANK".equals(type_doc)){
            ic_car_contract.setImageResource(R.drawable.icon_bank)
        }else   if("HOME".equals(type_doc)){
            ic_car_contract.setImageResource(R.drawable.icon_home_regis)
        }else   if("INCOME".equals(type_doc)){
            ic_car_contract.setImageResource(R.drawable.icon_income)
        }else {
            ic_car_contract.setImageResource(R.drawable.icon_other)
        }

        try{
            viewModel.getDataDocUpload(data_extra , type_doc)
        }catch ( e : Exception){
            e.message
        }


        attach_realtime_upload_widget.setOnAddImageListener {
            val photoPickerIntent = Intent(Intent.ACTION_PICK)
            photoPickerIntent.setType("image/*")
            startActivityForResult(photoPickerIntent, 999)


        }

        attach_realtime_upload_widget.onImageChanged {
            if (it.isEmpty()) {
                return@onImageChanged
            }
            attach_realtime_upload_widget.getImageList()
        }

        attach_realtime_upload_widget.triggerRemoveImageChanged{
            it?.let {
                // delete image
                // Sync image
                var item = listSync.get(it)
                try{
                    if(item.dOCIMAGE != null && item.dOCIMAGE !=""  ){
                        var syncDocModel  = listOf( SyncDocModel(
                                doctype =  type_doc
                                , refNo =  data_extra
                                , docname = item.dOCNAME
                                , imagebase64 = ""
                                , docname2 = ""
                                , extension = "JPG"
                        ))
                        item.dOCIMAGE = ""
                        viewModel.deleteDocUpload(syncDocModel)
                    }else{
                        return@let
                    }
                }catch ( e : Exception){
                    e.message
                }
            }
        }


        attach_realtime_upload_widget.triggerOverLimitImageAdd{
            showLimitImageDialog()
        }

        attach_realtime_upload_widget.triggerSyncImageChanged{
            it?.let {
                // Sync image
                var item = listSync.get(it.position)
                try{
                    if(item.dOCIMAGE != it.base64Image ){
                        var syncDocModel  = listOf( SyncDocModel(
                                  doctype =  type_doc
                                ,  refNo =  data_extra
                                , docname = item.dOCNAME
                                , imagebase64 = it.base64Image
                                , docname2 = ""
                                , extension = "JPG")
                        )
                        item.dOCIMAGE = it.base64Image
                        viewModel.uploadSyncImage(syncDocModel)
                    }else{
                        return@let
                    }
                }catch ( e : Exception){
                    e.message
                }
            }
        }

        btn_add_file.setOnClickListener {
            userGrantPermission(this@SyncDocumentActivity ) {
                if(DOC_MAX >  (attach_realtime_upload_widget_pdf.docBase64UriList.filter { it.fBase64 != ""}).size ) {
                    val intent = Intent()
                            .setType("application/pdf")
                            .setAction(Intent.ACTION_GET_CONTENT)
                            .addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                    startActivityForResult(Intent.createChooser(intent, "Select a file"), 111)
                }else{
                    showLimitImageDialog()
                }
            }

        }



        attach_realtime_upload_widget_pdf.triggerRemoveDocChanged{
            it?.let {
                // delete image
                // Sync image
                // Askl before remove
                pdfDocforDelete =  it
////                RemoveFileDialog.show(
////                        fragmentManager = supportFragmentManager,
////                        description = getString(R.string.dialog_delete_file_pdf)  ,
////                        confirmButtonMessage = getString(R.string.dialog_button_ok),
////                        cancelButtonMessage = getString(R.string.dialog_button_cancel)
////                )

                var syncDocModel = listOf(SyncDocModel(
                        doctype = type_doc
                        , refNo = data_extra
                        , docname = pdfDocforDelete // docname
                        , imagebase64 = ""
                        , docname2 = ""
                        , extension = "PDF")
                )
                pdfDocforDelete = ""
                viewModel.deleteDocUpload(syncDocModel)
            }
        }
    }

    //showAskBeforeDelete
    override fun onDialogConfirmClick() {
        if(pdfDocforDelete != "") {
            var syncDocModel = listOf(SyncDocModel(
                    doctype = type_doc
                    , refNo = data_extra
                    , docname = pdfDocforDelete // docname
                    , imagebase64 = ""
                    , docname2 = ""
                    , extension = "PDF")
            )
            pdfDocforDelete = ""
            viewModel.deleteDocUpload(syncDocModel)
        }
    }

    override fun onDialogCancelClick() {

    }

    private fun initViewModel() {
        try{
            viewModel.whenLoading.observe(this, Observer {
                toggleLoadingScreenDialog(it!!)
            })

            viewModel.whenDataLoadedDoc.observe(this, Observer {
                it?.let {
                    listSync = it.docUploadItem
                    IMG_MAX = it.m_max
                    IMG_MIN = it.m_min
                    attach_realtime_upload_widget.maximumImage = IMG_MAX
                    attach_realtime_upload_widget.imageBase64UriList =  MutableList(IMG_MAX) { "" }
                    if(listSync.size > 0 ){
                        listSync!!.forEach {
                          attach_realtime_upload_widget.galleryAdapter.addImageGallery(it.dOCIMAGE)
                        }
                    }
                }
            })

            viewModel.whenDataLoadedHistory.observe(this, Observer {
                it?.let {
                    if( it.get(0).images.size > 0 ) {
                        imageDocHistories.setData(it!!)
                        imageDocHistories.visibility = View.VISIBLE
                    } else{
                        imageDocHistories.visibility = View.GONE
                    }

                }
            })

            viewModel.whenDataLoadedPdf.observe(this, Observer {
                it?.let {
                    try{
                       var list = it.docUploadItem
                        DOC_MAX = it.m_max
                        DOC_MIN = it.m_min
                        attach_realtime_upload_widget_pdf.maximumDoc = DOC_MAX
                        attach_realtime_upload_widget_pdf.docBase64UriList =  MutableList(DOC_MAX){ ItemFilesData("","" , "") }
                        if(list.size > 0 ){
                            for (i in 0 until list.size) {
                                attach_realtime_upload_widget_pdf.galleryAdapter.addDocGallery(list[i].dOCFILENAME ,  list[i].dOCIMAGE  , list[i].dOCNAME)
                            }
                        }else{
                            return@let
                        }
                    }catch (e : Exception) {
                         e.message
                    }

                }
            })

            viewModel.whenDataLoadedPdfHistory.observe(this, Observer {
                it?.let {
                    if( it.get(0).images.size > 0 ) {
                        pdfDocHistories.setData(it!!)
                        pdfDocHistories.visibility = View.VISIBLE
                    } else{
                        pdfDocHistories.visibility = View.GONE
                    }

                }
            })


            viewModel.whenDataLoadedDocMaster.observe(this, Observer {
                it?.let {
                    masterImage = it
                }
            })

            viewModel.whenDataLoadedMessage.observe(this, Observer {
                it?.let {
                    MsgdescNormalDialog.show(
                            fragmentManager = supportFragmentManager,
                            description = it ,
                            confirmButtonMessage = getString(R.string.dialog_button_ok)
                    )
                }
            })

        }catch (e:Exception){
            e.message
        }
    }

    fun openFile(selectedFile : Uri) {
        try{
            if(DOC_MAX > attach_realtime_upload_widget_pdf.docBase64UriList.filter { it.fBase64!= "" }.size ) {
                var itemFile = PdfUtils.ExportPdfbase64(this@SyncDocumentActivity, selectedFile)
                var docName = attach_realtime_upload_widget_pdf.docBase64UriList.filter { it.fBase64 == "" }.first().fDocName

                if (!itemFile.isEncrypt && !itemFile.isLarge) {
                    var syncDocModel = listOf(
                            SyncDocModel(doctype = type_doc
                                    , refNo = data_extra
                                    , docname = docName
                                    , imagebase64 = itemFile.fBase64
                                    , docname2 = itemFile.fName
                                    , extension = "PDF"
                            ))
                    attach_realtime_upload_widget_pdf.galleryAdapter.addDocGallery(itemFile.fName, "PDF", docName)
                    viewModel.uploadSyncImage(syncDocModel)
                } else if (itemFile.isLarge){
                    showLargeFileDialog()
                }else if(itemFile.isEncrypt){
                    showPreventUploadEncryptFileDialog()
                }
            }else{
                showLimitImageDialog()
            }
        }catch ( e : Exception){
            e.stackTrace.toString()
            e.message
        }
    }


    private fun showLimitImageDialog() {
        NormalDialogFragment.show(
                fragmentManager = supportFragmentManager,
                description = getString(R.string.dialog_upload_image_limt),
                cancelButtonMessage = getString(R.string.dialog_upload_image_limt_button)
        )
    }

    private fun showPreventUploadEncryptFileDialog() {
        NormalDialogFragment.show(
                fragmentManager = supportFragmentManager,
                description = getString(R.string.dialog_upload_encrypt_pdf),
                cancelButtonMessage = getString(R.string.dialog_upload_encrypt_pdf_button)
        )
    }

    private fun showLargeFileDialog() {
        NormalDialogFragment.show(
                fragmentManager = supportFragmentManager,
                description = getString(R.string.dialog_upload_large_pdf),
                cancelButtonMessage = getString(R.string.btn_ok)
        )
    }



    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode != Activity.RESULT_OK) {
            return
        }

        when (requestCode) {
            111 -> {
                val selectedFile = data?.data //The uri with the location of the file
                if (selectedFile != null) {
                    val selectedFileURI = data.data
                    openFile(selectedFileURI!!)
                }
            }
            999 -> {
                try {
                    val imageUri = data?.data
                    val imageStream: InputStream? = contentResolver.openInputStream(imageUri!!)
                    val selectedImage = BitmapFactory.decodeStream(imageStream)

                    val outputStream = ByteArrayOutputStream()
                    selectedImage.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
                    val byteArray = outputStream.toByteArray()
                    val base64Image = Base64.encodeToString(byteArray, Base64.NO_WRAP)

                    attach_realtime_upload_widget.galleryAdapter.addImageGallery(base64Image)
                } catch (e: FileNotFoundException) {
                    e.printStackTrace()
                }
            }
        }
    }





    data class SyncDocModel(
          var  refNo: String ,
          var  doctype: String ,
          var  docname : String ,
          var  imagebase64 : String ,
          var  docname2 : String ,
          var  extension : String
    )

    companion object {

        const val REF_ID = "REF_ID"
        const val REF_URL = "REF_URL"
        const val TYPE_DOC = "TYPE_DOC"
        const val TITLE_HEADER = "TITLE_HEADER"
        const val REF_DESC = "REF_DESC"
        const val OTHER = "OTHER"

        fun start(activity: Activity?, ref_id : String ,  data: String,  title : String  ) {
            val intent = Intent(activity, SyncDocumentActivity::class.java)
            intent.putExtra(REF_ID, ref_id)
            intent.putExtra(TYPE_DOC, data)
            intent.putExtra(TITLE_HEADER, title)
            activity!!.startActivity(intent)
        }

//        fun start(activity: Context?, ref_id : String , data: String) {
//            val intent = Intent(activity, SyncDocumentActivity::class.java)
//            intent.putExtra(REF_ID, ref_id)
//            intent.putExtra(TYPE_DOC, data)
//            activity!!.startActivity(intent)
//        }

        fun openWithText(activity: Context?, ref_id: String ,  url : String  , data : String ,  title : String , txtDes : String , flagAdd : Boolean ) {
            val intent = Intent(activity, SyncDocumentActivity::class.java)
            intent.putExtra(REF_ID, ref_id)
            intent.putExtra(REF_URL, url)
            intent.putExtra(TYPE_DOC, data)
            intent.putExtra(TITLE_HEADER, title)
            intent.putExtra(OTHER, flagAdd)
            intent.putExtra(REF_DESC, txtDes)
            activity!!.startActivity(intent)
        }
    }

}
