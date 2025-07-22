package tlt.th.co.toyotaleasing.view.spotlight.shape

import android.annotation.TargetApi
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.PointF
import android.os.Build

class RoundRectangle(private val selectCarLeft: Float,
                     private val selectCarBottom: Float,
                     private val selectCarTop: Float,
                     private val selectCarRight: Float,
                     private val indicatorLeft: Float,
                     private val indicatorBottom: Float,
                     private val indicatorTop: Float,
                     private val indicatorRight: Float) : Shape {

    override val width: Int
        get() = selectCarRight.toInt() - selectCarLeft.toInt()

    override val height: Int
        get() = selectCarBottom.toInt() - selectCarTop.toInt()

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    override fun draw(canvas: Canvas, point: PointF, value: Float, paint: Paint) {
        canvas.drawRoundRect(selectCarLeft, selectCarTop, selectCarRight, selectCarBottom, 5f, 5f, paint)
        canvas.drawRoundRect(indicatorLeft, indicatorTop, indicatorRight, indicatorBottom, 5f, 5f, paint)
    }
}