package tlt.th.co.toyotaleasing.modules.custloan.utils

import android.content.Context
import android.graphics.Bitmap
import android.os.Environment

import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException
import java.io.RandomAccessFile
import java.text.SimpleDateFormat
import java.util.Date

object YUVUtil {
    private val `object` = Any()

    fun saveYUVBin(data: ByteArray, path: String, name: String?) {
        synchronized(`object`) {
            if (name == null) {
                return
            }
            try {
                val dir = File(path)
                if (!dir.exists() && dir.isDirectory)
                //Determine if the file directory exists
                {
                    dir.mkdirs()
                }
                val saveFile = File(path + File.separator + name)
                var raf: RandomAccessFile? = null
                //If it is an append, continue to write the file on the original basis.
                raf = RandomAccessFile(saveFile, "rw")
                raf.seek(saveFile.length())
                raf.write(data)
            } catch (e: FileNotFoundException) {
                e.printStackTrace()
            } catch (e: IOException) {
                e.printStackTrace()
            }

        }
    }

    /**
     * Data is rotated 90 degrees in the positive direction
     *
     * @param dst
     * @param src
     * @param imageWidth
     * @param imageHeight
     */
    fun YUV420spRotate90(dst: ByteArray, src: ByteArray, imageWidth: Int, imageHeight: Int) {
        // Rotate the Y luma
        var i = 0
        for (x in 0 until imageWidth) {
            for (y in imageHeight - 1 downTo 0) {
                dst[i] = src[y * imageWidth + x]
                i++
            }
        }
        // Rotate the U and V color components
        i = imageWidth * imageHeight * 3 / 2 - 1

        val wh = imageWidth * imageHeight
        var x = imageWidth - 1
        while (x > 0) {
            for (y in 0 until imageHeight / 2) {
                val yImageWidth = y * imageWidth
                dst[i] = src[wh + yImageWidth + x]
                i--
                dst[i] = src[wh + yImageWidth + (x - 1)]
                i--
            }
            x = x - 2
        }
    }

    /**
     * Image reverse inversion 90
     *
     * @param dst
     * @param src
     * @param srcWidth
     * @param height
     */
    fun YUV420spRotateNegative90(dst: ByteArray, src: ByteArray, srcWidth: Int, height: Int) {
        var nWidth = 0
        var nHeight = 0
        var wh = 0
        var uvHeight = 0
        if (srcWidth != nWidth || height != nHeight) {
            nWidth = srcWidth
            nHeight = height
            wh = srcWidth * height
            uvHeight = height shr 1//uvHeight = height / 2
        }

        //旋转Y
        var k = 0
        for (i in 0 until srcWidth) {
            var nPos = srcWidth - 1
            for (j in 0 until height) {
                dst[k] = src[nPos - i]
                k++
                nPos += srcWidth
            }
        }

        var i = 0
        while (i < srcWidth) {
            var nPos = wh + srcWidth - 1
            for (j in 0 until uvHeight) {
                dst[k] = src[nPos - i - 1]
                dst[k + 1] = src[nPos - i]
                k += 2
                nPos += srcWidth
            }
            i += 2
        }

    }

    /**
     * 图像反向反转180
     */
    fun YUV420Rotate180(dstyuv: ByteArray, srcdata: ByteArray, imageWidth: Int, imageHeight: Int) {

        var i = 0
        var j = 0
        var index = 0
        var tempindex = 0

        val ustart = imageWidth * imageHeight
        tempindex = ustart
        i = 0
        while (i < imageHeight) {

            tempindex -= imageWidth
            j = 0
            while (j < imageWidth) {

                dstyuv[index++] = srcdata[tempindex + j]
                j++
            }
            i++
        }

        val udiv = imageWidth * imageHeight / 4

        val uWidth = imageWidth / 2
        val uHeight = imageHeight / 2
        index = ustart
        tempindex = ustart + udiv
        i = 0
        while (i < uHeight) {

            tempindex -= uWidth
            j = 0
            while (j < uWidth) {

                dstyuv[index] = srcdata[tempindex + j]
                dstyuv[index + udiv] = srcdata[tempindex + j + udiv]
                index++
                j++
            }
            i++
        }
    }


    /**
     * 计算头像在图片中区域
     *
     * @param w
     * @param h
     * @param rect
     * @return
     */
    fun refineRect(w: Int, h: Int, rect: IntArray): IntArray {
        val ratio = 0.2f

        val new_rect = IntArray(4)

        new_rect[0] = (rect[0] - ratio * rect[2]).toInt()
        new_rect[1] = (rect[1] - 1.9 * ratio.toDouble() * rect[3].toDouble()).toInt()
        new_rect[2] = (rect[2] + 2f * ratio * rect[2].toFloat()).toInt()
        new_rect[3] = (rect[3] + 2.5 * ratio.toDouble() * rect[3].toDouble()).toInt()

        if (new_rect[0] < 0)
            new_rect[0] = 0
        if (new_rect[1] < 0)
            new_rect[1] = 0
        if (new_rect[2] + new_rect[0] >= w) {
            new_rect[2] = w - new_rect[0] - 1
        }

        if (new_rect[3] + new_rect[1] >= h) {
            new_rect[3] = h - new_rect[1] - 1
        }

        return new_rect
    }


    fun rawByteArray2RGBABitmap(data: ByteArray, width: Int, height: Int): Bitmap {
        val frameSize = width * height
        val rgba = IntArray(frameSize)

        for (i in 0 until height)
            for (j in 0 until width) {
                var y = 0xff and data[i * width + j].toInt()
                val u = 0xff and data[frameSize + (i shr 1) * width + (j and 1.inv()) + 0].toInt()
                val v = 0xff and data[frameSize + (i shr 1) * width + (j and 1.inv()) + 1].toInt()
                y = if (y < 16) 16 else y

                var r = Math.round(1.164f * (y - 16) + 1.596f * (v - 128))
                var g = Math.round(1.164f * (y - 16) - 0.813f * (v - 128) - 0.391f * (u - 128))
                var b = Math.round(1.164f * (y - 16) + 2.018f * (u - 128))

                r = if (r < 0) 0 else if (r > 255) 255 else r
                g = if (g < 0) 0 else if (g > 255) 255 else g
                b = if (b < 0) 0 else if (b > 255) 255 else b

                rgba[i * width + j] = -0x1000000 + (b shl 16) + (g shl 8) + r
            }

        val bmp = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        bmp.setPixels(rgba, 0, width, 0, 0, width, height)
        return bmp
    }

    fun saveBitmap2Files(context: Context, bitmap: Bitmap): String {
        // 刚刚拍照的文件名
        val fileName = ("IMG_"
                + SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
                .toString() + "_" + System.currentTimeMillis() + ".jpg")
        val sdRoot = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        val pictureFile = File(sdRoot, /*dir + */fileName)
        if (!pictureFile.exists()) {
            try {
                pictureFile.createNewFile()
                val out = FileOutputStream(pictureFile)
                bitmap.compress(Bitmap.CompressFormat.PNG, 90, out)
                out.flush()
                out.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }

        }


        return pictureFile.absolutePath
    }
}
