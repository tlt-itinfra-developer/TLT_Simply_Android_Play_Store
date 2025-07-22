package tlt.th.co.toyotaleasing.modules.custloan.utils

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageFormat
import android.graphics.Matrix
import android.graphics.Rect
import android.graphics.RectF
import android.graphics.YuvImage
import android.os.Build

import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileInputStream
import java.io.FileNotFoundException
import java.io.IOException

object BitmapUtil {

    val CV_ROTATE_90_CLOCKWISE = 0 //Rotate 90 degrees clockwise
    val CV_ROTATE_180 = 1 //Rotate 180 degrees clockwise
    val CV_ROTATE_90_COUNTERCLOCKWISE = 2 //Rotate 270 degrees clockwise
    val CV_ROTATE_360 = 3 //Rotate 270 degrees clockwise

    //Get the original bitmap image
    fun getBitmap(imageWidth: Int, imageHeight: Int, frame: ByteArray, ori: Int): Bitmap? {
        var bitmap: Bitmap? = null
        try {
            val image = YuvImage(frame, ImageFormat.NV21, imageWidth, imageHeight, null)
            val stream = ByteArrayOutputStream()
            image.compressToJpeg(Rect(0, 0, imageWidth, imageHeight), 100, stream)
            bitmap = BitmapFactory.decodeByteArray(stream.toByteArray(), 0, stream.size())
            stream.close()
        } catch (ex: Exception) {
            ex.printStackTrace()
        }

        val rotate: Float
        if (ori == CV_ROTATE_90_CLOCKWISE) {
            rotate = 90f
        } else if (ori == CV_ROTATE_90_COUNTERCLOCKWISE) {
            rotate = 270f
        } else if (ori == CV_ROTATE_180) {
            rotate = 180f
        } else {
            rotate = 360f
        }
        if (bitmap != null) {
            bitmap = rotateBitmap(bitmap, rotate)
        }
        return bitmap
    }

    //Get a face map
    fun getSmallBitmap(frame: ByteArray, x: Int, y: Int, w: Int, h: Int, width: Int, height: Int, ori: Int, bitmap: Bitmap): Bitmap? {
        var width = width
        var height = height
        // Successful picture
        try {

            if (ori == CV_ROTATE_90_CLOCKWISE || ori == CV_ROTATE_90_COUNTERCLOCKWISE) {
                width = height
                height = width
            }
            val rect: RectF
            if (x >= 0 && y >= 0 && w >= 0 && h >= 0) {
                rect = RectF(x.toFloat(), y.toFloat(), w.toFloat(), h.toFloat())
                if (frame.size != 0) {
                    val nw: Int
                    val nh: Int
                    var xn: Int
                    var yn: Int
                    var x0: Int
                    var y0: Int
                    val ratio = 1.45f
                    val x1 = rect.left.toInt()
                    val y1 = rect.top.toInt()
                    val w1 = rect.width().toInt() + x1
                    val h1 = rect.height().toInt() + y1

                    x0 = (x1 - w1.toDouble() * (ratio - 1).toDouble() * 0.5).toInt()
                    xn = (x0 + w1 * ratio).toInt()
                    if (x0 < 0) {
                        x0 = 0
                    }
                    if (xn > width - 1) {
                        xn = width - 1
                    }
                    nw = xn - x0 + 1

                    y0 = (y1 - h1 * (ratio - 1)).toInt()
                    yn = (y0 + h1 * (1 + (ratio - 1) * 1.5)).toInt()
                    if (y0 < 0) {
                        y0 = 0
                    }
                    if (yn > height - 1) {
                        yn = height - 1
                    }
                    nh = yn - y0 + 1
                    return Bitmap.createBitmap(bitmap, x0, y0, nw, nh)
                }
            }
        } catch (e: NullPointerException) {
            e.printStackTrace()
        }

        return null
    }


    //Rotate bitmap
    private fun rotateBitmap(origin: Bitmap?, alpha: Float): Bitmap? {
        if (origin == null) {
            return null
        }
        val width = origin.width
        val height = origin.height
        val matrix = Matrix()
        matrix.setRotate(alpha)
        // Rotate around the ground
        val newBM = Bitmap.createBitmap(origin, 0, 0, width, height, matrix, false)
        if (newBM == origin) {
            return newBM
        }
        origin.recycle()
        return newBM
    }

    fun bitmap2Bytes(bm: Bitmap): ByteArray {
        val baos = ByteArrayOutputStream()
        bm.compress(Bitmap.CompressFormat.PNG, 100, baos)
        return baos.toByteArray()
    }


    fun getBitmapSize(bitmap: Bitmap): Int {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {    //API 19
            return bitmap.allocationByteCount
        }
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR1) {//API 12
            bitmap.byteCount
        } else bitmap.rowBytes * bitmap.height
        // Use a byte x height in one line in the lower version
        //earlier version
    }

    fun file2byte(filePath: String): ByteArray? {
        var buffer: ByteArray? = null
        try {
            val file = File(filePath)
            val fis = FileInputStream(file)
            val bos = ByteArrayOutputStream()
            val b = ByteArray(1024)
            var n: Int
            while (fis.read(b)>0) {
                bos.write(b)
            }
            fis.close()
            bos.close()
            buffer = bos.toByteArray()

        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }

        return buffer
    }


}
