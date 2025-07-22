package tlt.th.co.toyotaleasing.modules.custloan.utils

import android.graphics.Bitmap
import android.os.Environment

import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import java.io.RandomAccessFile

/**
 * Created by zhanqiang545 on 18/3/30.
 */

object FileUtil {

    fun createFile(pathName: String): File {
        val file = File(Environment.getExternalStorageDirectory().absolutePath
                + File.separator + pathName + File.separator)
        if (!file.exists()) {
            file.mkdirs()
        }
        return file
    }


    /**
     * Save bitmap to local
     *
     * @param mBitmap
     * @return
     */
    fun saveBitmap(mBitmap: Bitmap, savePath: String, fileName: String): String? {
        val filePic: File
        try {
            filePic = File(savePath + File.separator + fileName)
            if (!filePic.exists()) {
                filePic.parentFile.mkdirs()
                filePic.createNewFile()
            }
            val fos = FileOutputStream(filePic)
            mBitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos)
            fos.flush()
            fos.close()
        } catch (e: IOException) {
            e.printStackTrace()
            return null
        }

        return filePic.absolutePath
    }


    fun reNameFile(oldName: String, newName: String) {
        val oleFile = File(oldName) //The file or folder to be renamed
        val newFile = File(newName)  //Renamed file or folder
        oleFile.renameTo(newFile)  //Perform rename
    }


    // Write a string to a text file
    fun writeTxtToFile(strcontent: String, filePath: String, fileName: String) {
        //After the folder is generated, the file is generated again, otherwise it will be wrong.
        makeFilePath(filePath, fileName)

        val strFilePath = filePath + fileName
        // Written every time you write
        val strContent = strcontent + "\r\n"
        try {
            val file = File(strFilePath)
            if (!file.exists()) {
                file.parentFile.mkdirs()
                file.createNewFile()
            }
            val raf = RandomAccessFile(file, "rwd")
            raf.seek(file.length())
            raf.write(strContent.toByteArray())
            raf.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    // Generate file
    fun makeFilePath(filePath: String, fileName: String): File? {
        var file: File? = null
        makeRootDirectory(filePath)
        try {
            file = File(filePath + fileName)
            if (!file.exists()) {
                file.createNewFile()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return file
    }

    // Generate folder
    fun makeRootDirectory(filePath: String) {
        var file: File? = null
        try {
            file = File(filePath)
            if (!file.exists()) {
                file.mkdir()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }


    /**
     * Copy a single file
     *
     * @param oldPath String The original file path is as follows: c:/fqf.txt
     * @param newPath String Path after copying: f:/fqf.txt
     * @return boolean
     */
    fun copyFile(oldPath: String, newPath: String) {
        try {
            var bytesum = 0
            var byteread = 0
            val oldfile = File(oldPath)
            if (oldfile.exists()) { //When the file exists
                val inStream = FileInputStream(oldPath) //Read the original file
                val fs = FileOutputStream(newPath)
                val buffer = ByteArray(1444)
                val length: Int
                while (inStream.read(buffer)>0) {
                    bytesum += byteread //Number of bytes file size
                    println(bytesum)
                    fs.write(buffer, 0, byteread)
                }
                inStream.close()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        deleteFile(oldPath)
    }


    /**
     * Delete a single file
     *
     * @param fileName The file name of the file to be deleted
     * @return Return true if a single file is deleted successfully, otherwise return false
     */
    private fun deleteFile(fileName: String): Boolean {
        val file = File(fileName)
        // If the file corresponding to the file path exists and is a file, delete it directly.
        return if (file.exists() && file.isFile) {
            if (file.delete()) {
                true
            } else {
                false
            }
        } else {
            false
        }
    }


    /**
     * Get the specified file size
     *
     * @param file
     * @return
     * @throws Exception
     */
    fun getFileSize(file: File): Long {
        var size: Long = 0
        try {
            if (file.exists()) {
                var fis: FileInputStream? = null
                fis = FileInputStream(file)
                size = fis.available().toLong()
            } else {
                file.createNewFile()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return size
    }


}
