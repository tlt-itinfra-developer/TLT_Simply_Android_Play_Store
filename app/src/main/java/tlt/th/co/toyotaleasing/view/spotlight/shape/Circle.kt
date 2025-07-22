package tlt.th.co.toyotaleasing.view.spotlight.shape

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.PointF

class Circle(private val radius: Float) : Shape {

    override val height: Int
        get() = radius.toInt() * 2

    override val width: Int
        get() = radius.toInt() * 2

    override fun draw(canvas: Canvas, point: PointF, value: Float, paint: Paint) {
        canvas.drawCircle(point.x, point.y, value * radius, paint)
    }
}