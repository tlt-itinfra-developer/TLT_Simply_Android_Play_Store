package tlt.th.co.toyotaleasing.modules.custloan.utils

import java.io.File
import java.io.RandomAccessFile


object WritelogUtil {

    // Write a string to a text file
    fun writeTxtToFile(strcontent: String, filePath: String, fileName: String) {
        //After the folder is generated, the file is generated again, otherwise it will be wrong.
        makeFilePath(filePath, fileName)

        val strFilePath = filePath + File.separator + fileName
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

    //Generate file
    fun makeFilePath(filePath: String, fileName: String): File? {
        var file: File? = null
        makeRootDirectory(filePath)
        try {
            file = File(filePath + File.separator + fileName)
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

}
