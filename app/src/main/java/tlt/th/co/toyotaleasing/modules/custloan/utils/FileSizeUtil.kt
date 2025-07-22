package tlt.th.co.toyotaleasing.modules.custloan.utils

import android.util.Log

import java.io.File
import java.io.FileInputStream
import java.text.DecimalFormat

/**
 * Created by zhanqiang545 on 18/4/23.
 */

object FileSizeUtil {

    val SIZETYPE_B = 1//Get the double value of the file size unit in B

    val SIZETYPE_KB = 2//Get the double value of the file size unit in KB

    val SIZETYPE_MB = 3//Get the double value of the file size unit in MB

    val SIZETYPE_GB = 4//Get the double value of the file size unit in GB

    /**
     *
     * Get the size of the specified unit of the file specified file
     *
     * @param filePath file path
     *
     * @param sizeType Get size type 1 is B, 2 is KB, 3 is MB, 4 is GB
     *
     * @return double Value size
     */

    fun getFileOrFilesSize(filePath: String, sizeType: Int): Double {

        val file = File(filePath)

        var blockSize: Long = 0

        try {

            if (file.isDirectory) {

                blockSize = getFileSizes(file)

            } else {

                blockSize = getFileSize(file)

            }

        } catch (e: Exception) {

            e.printStackTrace()

            Log.e("Get file size", "Acquisition failed!")
        }

        return FormetFileSize(blockSize, sizeType)

    }

    /**
     *
     * Call this method to automatically calculate the size of the specified file or the specified folder
     *
     * @param filePath file path
     *
     * @return Calculated band B、KB、MB、GB string
     */

    fun getAutoFileOrFilesSize(filePath: String): String {

        val file = File(filePath)

        var blockSize: Long = 0

        try {

            if (file.isDirectory) {

                blockSize = getFileSizes(file)

            } else {

                blockSize = getFileSize(file)

            }

        } catch (e: Exception) {

            e.printStackTrace()

            Log.e("Get file size", "Acquisition failed!")

        }

        return FormetFileSize(blockSize)

    }

    /**
     *
     * Get the specified file size
     *
     * @param file
     *
     * @return
     *
     * @throws Exception
     */

    @Throws(Exception::class)
    private fun getFileSize(file: File): Long {

        var size: Long = 0

        if (file.exists()) {

            var fis: FileInputStream? = null

            fis = FileInputStream(file)

            size = fis.available().toLong()

        } else {

            file.createNewFile()

            Log.e("Get file size", "file does not exist!")

        }

        return size

    }


    /**
     *
     * Get the specified folder
     *
     * @param f
     *
     * @return
     *
     * @throws Exception
     */

    @Throws(Exception::class)
    private fun getFileSizes(f: File): Long {

        var size: Long = 0

        val flist = f.listFiles()

        for (i in flist.indices) {

            if (flist[i].isDirectory) {

                size = size + getFileSizes(flist[i])

            } else {

                size = size + getFileSize(flist[i])

            }

        }

        return size

    }

    /**
     *
     * Convert file size
     *
     * @param fileS
     *
     * @return
     */

    private fun FormetFileSize(fileS: Long): String {

        val df = DecimalFormat("#.00")

        var fileSizeString = ""

        val wrongSize = "0B"

        if (fileS == 0L) {

            return wrongSize

        }

        if (fileS < 1024) {

            fileSizeString = df.format(fileS.toDouble()) + "B"

        } else if (fileS < 1048576) {

            fileSizeString = df.format(fileS.toDouble() / 1024) + "KB"

        } else if (fileS < 1073741824) {

            fileSizeString = df.format(fileS.toDouble() / 1048576) + "MB"

        } else {

            fileSizeString = df.format(fileS.toDouble() / 1073741824) + "GB"

        }

        return fileSizeString

    }

    /**
     *
     * Convert file size, specify the type of conversion
     *
     * @param fileS
     *
     * @param sizeType
     *
     * @return
     */

    private fun FormetFileSize(fileS: Long, sizeType: Int): Double {

        val df = DecimalFormat("#.00")

        var fileSizeLong = 0.0

        when (sizeType) {

            SIZETYPE_B ->

                fileSizeLong = java.lang.Double.valueOf(df.format(fileS.toDouble()))

            SIZETYPE_KB ->

                fileSizeLong = java.lang.Double.valueOf(df.format(fileS.toDouble() / 1024))

            SIZETYPE_MB ->

                fileSizeLong = java.lang.Double.valueOf(df.format(fileS.toDouble() / 1048576))

            SIZETYPE_GB ->

                fileSizeLong = java.lang.Double.valueOf(df.format(fileS.toDouble() / 1073741824))

            else -> {
            }
        }

        return fileSizeLong

    }
}
