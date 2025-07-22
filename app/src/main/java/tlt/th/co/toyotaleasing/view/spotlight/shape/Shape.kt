package tlt.th.co.toyotaleasing.view.spotlight.shape

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.PointF

interface Shape {

    /**
     * get the height of the Shape
     */
    val height: Int

    /**
     * get the with of the Shape
     */
    val width: Int

    /**
     * draw the Shape
     *
     * @param value the animated value from 0 to 1
     */
    fun draw(canvas: Canvas, point: PointF, value: Float, paint: Paint)
}