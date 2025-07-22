package tlt.th.co.toyotaleasing.util

import android.content.ContentUris
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.provider.OpenableColumns
import tlt.th.co.toyotaleasing.util.PdfUtils.generateFileName
import tlt.th.co.toyotaleasing.util.PdfUtils.getDocumentCacheDir
import tlt.th.co.toyotaleasing.util.PdfUtils.saveFileFromUri
import java.io.File
import java.io.FileOutputStream
import java.io.IOException


object FilePath {

    /**
     * Method for return file path of Gallery image/ Document / Video / Audio
     *
     * @param context
     * @param uri
     * @return path of the selected image file from gallery
     */
    fun getPath(context: Context, uri: Uri): String? {
        val isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT

        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            when {
                isExternalStorageDocument(uri) -> {
                    val docId = DocumentsContract.getDocumentId(uri)
                    val split = docId.split(":")
                    val type = split[0]

                    if ("primary".equals(type, ignoreCase = true)) {
                        return "${Environment.getExternalStorageDirectory()}/${split.getOrNull(1)}"
                    }
                }

                isDownloadsDocument(uri) -> {
                    val docId = DocumentsContract.getDocumentId(uri)

                    if (docId.startsWith("raw:")) {
                        return docId.removePrefix("raw:")
                    }

                    val contentUriPrefixesToTry = arrayOf(
                        "content://downloads/public_downloads",
                        "content://downloads/my_downloads",
                        "content://downloads/all_downloads"
                    )

                    for (prefix in contentUriPrefixesToTry) {
                        try {
                            val contentUri = ContentUris.withAppendedId(Uri.parse(prefix), docId.toLong())
                            val path = getDataColumn(context, contentUri, null, null)
                            if (path != null) return path
                        } catch (_: Exception) {
                        }
                    }

                    return copyFileToCache(context, uri)
                }

                isMediaDocument(uri) -> {
                    val docId = DocumentsContract.getDocumentId(uri)
                    val split = docId.split(":")
                    val type = split[0]
                    val id = split.getOrNull(1)

                    val contentUri = when (type) {
                        "image" -> MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                        "video" -> MediaStore.Video.Media.EXTERNAL_CONTENT_URI
                        "audio" -> MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
                        "document" -> MediaStore.Files.getContentUri("external")
                        else -> null
                    }

                    if (contentUri != null && id != null) {
                        val selection = "_id=?"
                        val selectionArgs = arrayOf(id)
                        val path = getDataColumn(context, contentUri, selection, selectionArgs)
                        if (path != null) return path
                    }

                    return copyFileToCache(context, uri)
                }

                isGoogleDrive(uri) -> {
                    return copyFileToCache(context, uri)
                }

                else -> {
                    return copyFileToCache(context, uri)
                }
            }
        } else if ("content".equals(uri.scheme, ignoreCase = true)) {
            return if (isGooglePhotosUri(uri)) uri.lastPathSegment else getDataColumn(context, uri, null, null)
        } else if ("file".equals(uri.scheme, ignoreCase = true)) {
            return uri.path
        }

        return null
    }

    fun copyFileToCache(context: Context, uri: Uri): String? {
        val fileName = getFileName(context, uri) ?: return null
        val cacheDir = getDocumentCacheDir(context)
        val file = generateFileName(fileName, cacheDir) ?: return null
        saveFileFromUri(context, uri, file.absolutePath)
        return file.absolutePath
    }


    /**
     * Get the value of the data column for this Uri. This is useful for
     * MediaStore Uris, and other file-based ContentProviders.
     *
     * @param context
     * The context.
     * @param uri
     * The Uri to query.
     * @param selection
     * (Optional) Filter used in the query.
     * @param selectionArgs
     * (Optional) Selection arguments used in the query.
     * @return The value of the _data column, which is typically a file path.
     */
    fun getDataColumn(
        context: Context, uri: Uri?,
        selection: String?, selectionArgs: Array<String>?
    ): String? {

        var cursor: Cursor? = null
        val column = "_data"
        val projection = arrayOf(column)

        try {
            cursor = context.getContentResolver().query(
                uri!!, projection,
                selection, selectionArgs, null
            )
            if (cursor != null && cursor!!.moveToFirst()) {
                val index = cursor!!.getColumnIndexOrThrow(column)
                return cursor!!.getString(index)
            }
        } finally {
            if (cursor != null)
                cursor!!.close()
        }
        return null
    }

    /**
     * @param uri
     * The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    fun isExternalStorageDocument(uri: Uri): Boolean {
        return "com.android.externalstorage.documents" == uri
            .getAuthority()
    }

    /**
     * @param uri
     * The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    fun isDownloadsDocument(uri: Uri): Boolean {
        return "com.android.providers.downloads.documents" == uri
            .getAuthority()
    }

    /**
     * @param uri
     * The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    fun isMediaDocument(uri: Uri): Boolean {
        return "com.android.providers.media.documents" == uri
            .getAuthority()
    }

    /**
     * @param uri
     * The Uri to check.
     * @return Whether the Uri authority is Google Photos.
     */
    fun isGooglePhotosUri(uri: Uri): Boolean {
        return "com.google.android.apps.photos.content" == uri
            .getAuthority()
    }

    fun isGoogleDrive(uri: Uri): Boolean {
        return "com.google.android.apps.docs.storage" == uri.authority
    }

    @Throws(Exception::class)
    fun saveFileIntoExternalStorageByUri(context: Context, uri: Uri): File {
        val fileName = getFileName(context, uri)
        val file = makeEmptyFileIntoExternalStorageWithTitle(fileName)

        context.contentResolver.openInputStream(uri)?.use { inputStream ->
            FileOutputStream(file).use { outputStream ->
                val buffer = ByteArray(4096)
                var bytesRead: Int
                while (inputStream.read(buffer).also { bytesRead = it } != -1) {
                    outputStream.write(buffer, 0, bytesRead)
                }
                outputStream.flush()
            }
        } ?: throw IOException("Unable to open input stream for URI: $uri")

        return file
    }


    fun getFileName(context: Context, uri: Uri): String {
        var result: String? = null
        if (uri.scheme == "content") {
            val cursor = context.contentResolver.query(uri, null, null, null, null)
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME))
                }
            } finally {
                cursor!!.close()
            }
        }
        if (result == null) {
            result = uri.path
            val cut = result!!.lastIndexOf('/')
            if (cut != -1) {
                result = result.substring(cut + 1)
            }
        }
        return result
    }


    fun makeEmptyFileIntoExternalStorageWithTitle(title: String): File {
        val root = Environment.getExternalStorageDirectory().absolutePath
        return File(root, title)
    }
}