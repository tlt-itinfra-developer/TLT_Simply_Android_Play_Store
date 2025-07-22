package tlt.th.co.toyotaleasing.util

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Environment
import androidx.core.content.FileProvider
import android.util.Base64
import tlt.th.co.toyotaleasing.BuildConfig
import android.provider.OpenableColumns
import androidx.annotation.NonNull
import androidx.annotation.Nullable
import android.util.Log
import tlt.th.co.toyotaleasing.util.FilePath.getPath
import tlt.th.co.toyotaleasing.modules.custloan.loanmodule.carloan.common.ItemFilesData
import java.io.*
import java.lang.Exception
import com.itextpdf.text.pdf.PdfReader


object PdfUtils {

    private val TAG = "PDF"
    val DOCUMENTS_DIR = "documents"
    private val DEBUG = false // Set to true to enable logging

    private val dir by lazy {
        File("${Environment.getExternalStorageDirectory().absolutePath}/test")
    }

    fun savePdf(filename: String = "", base64: String = "") {
        if (isFileExist(filename)) {
            return
        }

        dir.mkdirs()

        val outFile = getFileFromFilename(filename)
        val pdfAsBytes = Base64.decode(base64, Base64.DEFAULT)

        FileOutputStream(outFile, false).apply {
            write(pdfAsBytes)
            flush()
            close()
        }
    }

    fun openPdf(context: Context?, filename: String?) {
        val file = PdfUtils.getFileFromFilename(filename ?: "")
        val uri = FileProvider.getUriForFile(context!!, "${BuildConfig.APPLICATION_ID}.provider", file)

        val intent = Intent(Intent.ACTION_VIEW).apply {
            setDataAndType(uri, "application/pdf")
            flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
        }

        context.startActivity(intent)
    }

    fun isFileExist(filename: String = ""): Boolean {
        val file = getFileFromFilename(filename)
        return file.exists()
    }


    fun getPdfUri(context: Context?, filename: String?): File {
        val file = PdfUtils.getFileFromFilename(filename ?: "")
//        val uri = FileProvider.getUriForFile(context!!, "${BuildConfig.APPLICATION_ID}.provider", file)
        return file
    }
    private fun getFileFromFilename(filename: String) = File("$dir/$filename.pdf")

    fun ExportPdfbase64(   context: Context? ,  fileUrl: Uri) : ItemFilesData {
        try {
            val selectedFilePath = getPath(context!! , fileUrl ).toString() // FilePath.getPath(context!!, fileUrl)
            val fileS : File = File(selectedFilePath)
            val file_size = Integer.parseInt((fileS.length() / 2048000).toString())
            if(isFilelocked(selectedFilePath!!)){
                return  ItemFilesData( fBase64 = "" , fName = "" , isEncrypt = true , isLarge = false)
            }
            if(file_size > 1 ){
                return  ItemFilesData( fBase64 = "" , fName = "" , isEncrypt = false , isLarge = true)
            }
            val fName =  fileS.name.toString()
            val fileBase64 =  Base64.encodeToString(fileS.readBytes(), Base64.NO_WRAP)
            return  ItemFilesData( fBase64 = fileBase64 , fName = fName)
        } catch (e: IOException) {
            e.printStackTrace()
            return  ItemFilesData( fBase64 = "" , fName = "" , isEncrypt = true)
        }
    }

    fun isFilelocked(file: String): Boolean {
        try {
            val reader = PdfReader(file)
            if( reader.isEncrypted)
                return  true
        } catch (e: Exception) {
            return true
        }
        return false
    }


    fun getFileName(@NonNull context: Context, uri: Uri): String? {
        val mimeType = context.contentResolver.getType(uri)
        var filename: String? = null

        if (mimeType == null && context != null) {
            val path = getPath(context, uri)
            if (path == null) {
                filename = getName(uri.toString())
            } else {
                val file = File(path)
                filename = file.getName()
            }
        } else {
            val returnCursor = context.contentResolver.query(uri, null, null, null, null)
            if (returnCursor != null) {
                val nameIndex = returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                returnCursor.moveToFirst()
                filename = returnCursor.getString(nameIndex)
                returnCursor.close()
            }
        }

        return filename
    }

    fun getName(filename: String?): String? {
        if (filename == null) {
            return null
        }
        val index = filename.lastIndexOf('/')
        return filename.substring(index + 1)
    }


    fun getDocumentCacheDir(context: Context): File {
        val dir = File(context.cacheDir, DOCUMENTS_DIR)
        if (!dir.exists()) {
            dir.mkdirs()
        }
        logDir(context.cacheDir)
        logDir(dir)

        return dir
    }

    @Nullable
    fun generateFileName(@Nullable name: String?, directory: File): File? {
        var name: String? = name ?: return null

        var file = File(directory, name)

        if (file.exists()) {
            var fileName: String = name!!
            var extension = ""
            val dotIndex = name!!.lastIndexOf('.')
            if (dotIndex > 0) {
                fileName = name.substring(0, dotIndex)
                extension = name.substring(dotIndex)
            }

            var index = 0

            while (file.exists()) {
                index++
                name = "$fileName($index)$extension"
                file = File(directory, name)
            }
        }

        try {
            if (!file.createNewFile()) {
                return null
            }
        } catch (e: IOException) {
            Log.w(TAG, e)
            return null
        }

        logDir(directory)

        return file
    }

     fun logDir(dir: File) {
        if (!DEBUG) return
        Log.d(TAG, "Dir=$dir")
        val files = dir.listFiles()
        for (file in files) {
            Log.d(TAG, "File=" + file.path)
        }
    }

     fun saveFileFromUri(context: Context, uri: Uri, destinationPath: String) {
        var `is`: InputStream? = null
        var bos: BufferedOutputStream? = null
        try {
            `is` = context.contentResolver.openInputStream(uri)
            bos = BufferedOutputStream(FileOutputStream(destinationPath, false))
            val buf = ByteArray(1024)
            `is`!!.read(buf)
            do {
                bos!!.write(buf)
            } while (`is`!!.read(buf) !== -1)
        } catch (e: IOException) {
            e.printStackTrace()
        } finally {
            try {
                if (`is` != null) `is`!!.close()
                if (bos != null) bos!!.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }

        }
    }

}