package tlt.th.co.toyotaleasing.view.seekbar

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.graphics.Typeface
import android.view.View
import androidx.annotation.ColorInt
import androidx.annotation.LayoutRes
import android.graphics.drawable.StateListDrawable
import androidx.annotation.ArrayRes

class Builder(val context: Context) {

    //seek bar
    var max = 100f
    var min = 0f
    var progress = 0f
    var progressValueFloat = false
    var seekSmoothly = false
    var r2l = false
    var userSeekable = true
    var onlyThumbDraggable = false
    var clearPadding = false
    //indicator
    var indicatorColor = Color.parseColor("#FF4081")
    var indicatorTextColor = Color.parseColor("#FFFFFF")
    var indicatorTextSize = 0
    lateinit var indicatorContentView: View
    lateinit var indicatorTopContentView: View
    //track
    var trackBackgroundSize = 0
    var trackBackgroundColor = Color.parseColor("#D7D7D7")
    var trackProgressSize = 0
    var trackProgressColor = Color.parseColor("#FF4081")
    var trackRoundedCorners = false
    //thumbText
    var thumbTextColor = Color.parseColor("#FF4081")
    var showThumbText = false
    //thumb
    var thumbSize = 0
    var thumbColor = Color.parseColor("#FF4081")
    lateinit var thumbColorStateList: ColorStateList
    lateinit var thumbDrawable: Drawable
    //tickTexts
    var showTickText: Boolean = false
    var tickTextsColor = Color.parseColor("#FF4081")
    var tickTextsSize = 0
    lateinit var tickTextsCustomArray: Array<String>
    var tickTextsTypeFace = Typeface.DEFAULT
    lateinit var tickTextsColorStateList: ColorStateList
    //tickMarks
    var tickCount = 0
    var tickMarksColor = Color.parseColor("#FF4081")
    var tickMarksSize = 0
    lateinit var tickMarksDrawable: Drawable
    var tickMarksEndsHide = false
    var tickMarksSweptHide = false
    lateinit var tickMarksColorStateList: ColorStateList

    init {
        this.indicatorTextSize = SizeUtils.sp2px(context, 14f)
        this.trackBackgroundSize = SizeUtils.dp2px(context, 2f)
        this.trackProgressSize = SizeUtils.dp2px(context, 2f)
        this.tickMarksSize = SizeUtils.dp2px(context, 10f)
        this.tickTextsSize = SizeUtils.sp2px(context, 13f)
        this.thumbSize = SizeUtils.dp2px(context, 14f)
    }

    fun build(): IndicatorSeekBar {
        return IndicatorSeekBar(this)
    }

    fun max(max: Float): Builder {
        this.max = max
        return this
    }

    fun min(min: Float): Builder {
        this.min = min
        return this
    }

    fun progress(progress: Float): Builder {
        this.progress = progress
        return this
    }

    fun progressValueFloat(isFloatProgress: Boolean): Builder {
        this.progressValueFloat = isFloatProgress
        return this
    }

    fun seekSmoothly(seekSmoothly: Boolean): Builder {
        this.seekSmoothly = seekSmoothly
        return this
    }

    fun r2l(r2l: Boolean): Builder {
        this.r2l = r2l
        return this
    }

    fun clearPadding(clearPadding: Boolean): Builder {
        this.clearPadding = clearPadding
        return this
    }

    fun userSeekable(userSeekable: Boolean): Builder {
        this.userSeekable = userSeekable
        return this
    }

    fun onlyThumbDraggable(onlyThumbDraggable: Boolean): Builder {
        this.onlyThumbDraggable = onlyThumbDraggable
        return this
    }

    fun indicatorColor(@ColorInt indicatorColor: Int): Builder {
        this.indicatorColor = indicatorColor
        return this
    }

    fun indicatorTextColor(@ColorInt indicatorTextColor: Int): Builder {
        this.indicatorTextColor = indicatorTextColor
        return this
    }

    fun indicatorTextSize(indicatorTextSize: Int): Builder {
        this.indicatorTextSize = SizeUtils.sp2px(context, indicatorTextSize.toFloat())
        return this
    }

    fun indicatorContentView(indicatorContentView: View): Builder {
        this.indicatorContentView = indicatorContentView
        return this
    }

    fun indicatorContentViewLayoutId(@LayoutRes layoutId: Int): Builder {
        this.indicatorContentView = View.inflate(context, layoutId, null)
        return this
    }

    fun indicatorTopContentView(topContentView: View): Builder {
        this.indicatorTopContentView = topContentView
        return this
    }

    fun indicatorTopContentViewLayoutId(@LayoutRes layoutId: Int): Builder {
        this.indicatorTopContentView = View.inflate(context, layoutId, null)
        return this
    }

    fun trackBackgroundSize(trackBackgroundSize: Int): Builder {
        this.trackBackgroundSize = SizeUtils.dp2px(context, trackBackgroundSize.toFloat())
        return this
    }

    fun trackBackgroundColor(@ColorInt trackBackgroundColor: Int): Builder {
        this.trackBackgroundColor = trackBackgroundColor
        return this
    }

    fun trackProgressSize(trackProgressSize: Int): Builder {
        this.trackProgressSize = SizeUtils.dp2px(context, trackProgressSize.toFloat())
        return this
    }

    fun trackProgressColor(@ColorInt trackProgressColor: Int): Builder {
        this.trackProgressColor = trackProgressColor
        return this
    }

    fun trackRoundedCorners(trackRoundedCorners: Boolean): Builder {
        this.trackRoundedCorners = trackRoundedCorners
        return this
    }

    fun thumbTextColor(@ColorInt thumbTextColor: Int): Builder {
        this.thumbTextColor = thumbTextColor
        return this
    }

    fun showThumbText(showThumbText: Boolean): Builder {
        this.showThumbText = showThumbText
        return this
    }

    fun thumbColor(@ColorInt thumbColor: Int): Builder {
        this.thumbColor = thumbColor
        return this
    }

    fun thumbColorStateList(thumbColorStateList: ColorStateList): Builder {
        this.thumbColorStateList = thumbColorStateList
        return this
    }

    fun thumbSize(thumbSize: Int): Builder {
        this.thumbSize = SizeUtils.dp2px(context, thumbSize.toFloat())
        return this
    }

    fun thumbDrawable(thumbDrawable: Drawable): Builder {
        this.thumbDrawable = thumbDrawable
        return this
    }

    fun thumbDrawable(thumbStateListDrawable: StateListDrawable): Builder {
        this.thumbDrawable = thumbStateListDrawable
        return this
    }

    fun showTickTexts(showTickText: Boolean): Builder {
        this.showTickText = showTickText
        return this
    }

    fun tickTextsColor(@ColorInt tickTextsColor: Int): Builder {
        this.tickTextsColor = tickTextsColor
        return this
    }

    fun tickTextsColorStateList(tickTextsColorStateList: ColorStateList): Builder {
        this.tickTextsColorStateList = tickTextsColorStateList
        return this
    }

    fun tickTextsSize(tickTextsSize: Int): Builder {
        this.tickTextsSize = SizeUtils.sp2px(context, tickTextsSize.toFloat())
        return this
    }

    fun tickTextsArray(tickTextsArray: Array<String>): Builder {
        this.tickTextsCustomArray = tickTextsArray
        return this
    }

    fun tickTextsArray(@ArrayRes tickTextsArray: Int): Builder {
        this.tickTextsCustomArray = context.resources.getStringArray(tickTextsArray)
        return this
    }

    fun tickTextsTypeFace(tickTextsTypeFace: Typeface): Builder {
        this.tickTextsTypeFace = tickTextsTypeFace
        return this
    }

    fun tickCount(tickCount: Int): Builder {
        this.tickCount = tickCount
        return this
    }

    fun tickMarksColor(@ColorInt tickMarksColor: Int): Builder {
        this.tickMarksColor = tickMarksColor
        return this
    }

    fun tickMarksColor(tickMarksColorStateList: ColorStateList): Builder {
        this.tickMarksColorStateList = tickMarksColorStateList
        return this
    }

    fun tickMarksSize(tickMarksSize: Int): Builder {
        this.tickMarksSize = SizeUtils.dp2px(context, tickMarksSize.toFloat())
        return this
    }

    fun tickMarksDrawable(tickMarksDrawable: Drawable): Builder {
        this.tickMarksDrawable = tickMarksDrawable
        return this
    }

    fun tickMarksDrawable(tickMarksStateListDrawable: StateListDrawable): Builder {
        this.tickMarksDrawable = tickMarksStateListDrawable
        return this
    }

    fun tickMarksEndsHide(tickMarksEndsHide: Boolean): Builder {
        this.tickMarksEndsHide = tickMarksEndsHide
        return this
    }

    fun tickMarksSweptHide(tickMarksSweptHide: Boolean): Builder {
        this.tickMarksSweptHide = tickMarksSweptHide
        return this
    }

}