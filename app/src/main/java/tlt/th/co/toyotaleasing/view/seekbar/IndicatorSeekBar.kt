package tlt.th.co.toyotaleasing.view.seekbar

import android.animation.ValueAnimator
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.*
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.graphics.drawable.StateListDrawable
import android.os.Build
import android.os.Bundle
import android.os.Parcelable
import androidx.annotation.ColorInt
import android.text.TextPaint
import android.util.AttributeSet
import android.util.TypedValue
import android.view.MotionEvent
import android.view.View
import tlt.th.co.toyotaleasing.R
import tlt.th.co.toyotaleasing.manager.LocalizeManager
import java.math.BigDecimal

class IndicatorSeekBar: View {

    private var mContext: Context? = null
    private var mStockPaint: Paint? = null//the paint for seek bar drawing
    private var mTextPaint: TextPaint? = null//the paint for mTickTextsArr drawing
    private var mUnitTextPaint: TextPaint? = null//the paint for unit of mTickTextsArr drawing
    private var onSeekChangeListener: OnSeekChangeListener? = null
    private var mRect: Rect? = null
    private var mCustomDrawableMaxHeight: Float = 0.toFloat()//the max height for custom drawable
    private var lastProgress: Float = 0.toFloat()
    private var mFaultTolerance = -1f//the tolerance for user seek bar touching
    private val mScreenWidth = -1f
    private var mClearPadding: Boolean = false
    private var mSeekParams: SeekParams? = null//save the params when seeking change.
    //seek bar
    private var mPaddingLeft: Int = 0
    private var mPaddingRight: Int = 0
    private var mMeasuredWidth: Int = 0
    private var mPaddingTop: Int = 0
    private var mSeekLength: Float = 0.toFloat()//the total length of seek bar
    private var mSeekBlockLength: Float = 0.toFloat()//the length for each section part to seek
    private var mIsTouching: Boolean = false//user is touching the seek bar
    private var mMax: Float = 0.toFloat()
    private var mMin: Float = 0.toFloat()
    private var mProgress: Float = 0.toFloat()
    private var mIsFloatProgress: Boolean = false// true for the progress value in float,otherwise in int.
    private val mScale = 1//the scale of the float progress.
    private var mUserSeekable: Boolean = false//true if the user can seek to change the progress,otherwise only can be changed by setProgress().
    private var mOnlyThumbDraggable: Boolean = false//only drag the seek bar's thumb can be change the progress
    private var mSeekSmoothly: Boolean = false//seek continuously
    private var mProgressArr: FloatArray? = null//save the progress which at tickMark position.
    private var mR2L: Boolean = false//right to left,compat local problem.
    //tick texts
    private var mShowTickText: Boolean = false//the palace where the tick text show .
    private val mShowBothTickTextsOnly: Boolean = false//show the tick texts on the both ends of seek bar before.
    private var mTickTextsHeight: Int = 0//the height of text
    private var mTickTextsArr: Array<String>? = null//save the tick texts which at tickMark position.
    private var mTickTextsWidth: FloatArray? = null//save the tick texts width bounds.
    private var mTextCenterX: FloatArray? = null//the text's drawing X anchor
    private var mTickTextY: Float = 0.toFloat()//the text's drawing Y anchor
    private var mTickTextsSize: Int = 0
    private var mTextsTypeface: Typeface? = null//the tick texts and thumb texts' typeface
    private var mSelectedTextsColor: Int = 0//the color for the tick texts those thumb swept.
    private var mUnselectedTextsColor: Int = 0//the color for the tick texts those thumb haven't reach.
    private var mHoveredTextColor: Int = 0//the color for the tick texts which below/above thumb.
    private var mTickTextsCustomArray: Array<CharSequence>? = null
    //indicator
    private var mIndicatorContentView: View? = null//the view to replace the raw indicator all view
    //tick marks
    private var mTickMarksX: FloatArray? = null//the tickMark's drawing X anchor
    var tickCount: Int = 0
        private set//the num of tickMarks
    private val mUnSelectedTickMarksColor: Int = 0//the color for the tickMarks those thumb haven't reach.
    private val mSelectedTickMarksColor: Int = 0//the color for the tickMarks those thumb swept.
    private var mTickRadius: Float = 0.toFloat()//the tick's radius
    private var mUnselectTickMarksBitmap: Bitmap? = null//the drawable bitmap for tick
    private var mSelectTickMarksBitmap: Bitmap? = null//the drawable bitmap for tick
    private var mTickMarksDrawable: Drawable? = null
    private val mShowTickMarksType: Int = 0
    private var mTickMarksEndsHide: Boolean = false//true if want to hide the tickMarks which in both side ends of seek bar
    private var mTickMarksSweptHide: Boolean = false//true if want to hide the tickMarks which on thumb left.
    private var mTickMarksSize: Int = 0//the width of tickMark
    //track
    private var mTrackRoundedCorners: Boolean = false
    private var mProgressTrack: RectF? = null//the background track on the thumb startWithResult
    private var mBackgroundTrack: RectF? = null//the background track on the thumb ends
    private var mBackgroundTrackSize: Int = 0
    private var mProgressTrackSize: Int = 0
    private var mBackgroundTrackColor: Int = 0
    private var mProgressTrackColor: Int = 0
    private val mSectionTrackColorArray: IntArray? = null//save the color for each section tracks.
    private val mCustomTrackSectionColorResult: Boolean = false//true to confirm to custom the section track color
    //thumb
    private var mThumbRadius: Float = 0.toFloat()//the thumb's radius
    private var mThumbTouchRadius: Float = 0.toFloat()//the thumb's radius when touching
    private var mThumbBitmap: Bitmap? = null//the drawable bitmap for thumb
    private var mThumbColor: Int = 0
    private var mThumbSize: Int = 0
    private var mThumbDrawable: Drawable? = null
    private var mPressedThumbBitmap: Bitmap? = null//the bitmap for pressing status
    private var mPressedThumbColor: Int = 0//the color for pressing status
    //thumb text
    private var mShowThumbText: Boolean = false//the place where the thumb text show .
    private var mThumbTextY: Float = 0.toFloat()//the thumb text's drawing Y anchor
    private var mThumbTextColor: Int = 0
    private var mHideThumb: Boolean = false

    private val mDay: FloatArray? = null

    private val thumbCenterX: Float
        get() = if (mR2L) {
            mBackgroundTrack!!.right
        } else mProgressTrack!!.right

    private val leftSideTickColor: Int
        get() {
            return if (mR2L) {
                mUnSelectedTickMarksColor
            } else mSelectedTickMarksColor
        }

    private val rightSideTickColor: Int
        get() {
            return if (mR2L) {
                mSelectedTickMarksColor
            } else mUnSelectedTickMarksColor
        }

    private val leftSideTickTextsColor: Int
        get() {
            return if (mR2L) {
                mUnselectedTextsColor
            } else mSelectedTextsColor
        }

    private val rightSideTickTextsColor: Int
        get() {
            return if (mR2L) {
                mSelectedTextsColor
            } else mUnselectedTextsColor
        }

    private val leftSideTrackSize: Int
        get() {
            return if (mR2L) {
                mBackgroundTrackSize
            } else mProgressTrackSize
        }

    private val rightSideTrackSize: Int
        get() {
            return if (mR2L) {
                mProgressTrackSize
            } else mBackgroundTrackSize
        }

    private//when tick count = 0 ; seek bar has not tick(continuous series), return 0;
    val thumbPosOnTick: Int
        get() {
            return if (tickCount != 0) {
                Math.round((thumbCenterX - mPaddingLeft) / mSeekBlockLength)
            } else 0
        }

    private val thumbPosOnTickFloat: Float
        get() {
            return if (tickCount != 0) {
                (thumbCenterX - mPaddingLeft) / mSeekBlockLength
            } else 0f
        }

    private val closestIndex: Int
        get() {
            var closestIndex = 0
            var amplitude = Math.abs(mMax - mMin)
            for (i in mProgressArr!!.indices) {
                val amplitudeTemp = Math.abs(mProgressArr!![i] - mProgress)
                if (amplitudeTemp <= amplitude) {
                    amplitude = amplitudeTemp
                    closestIndex = i
                }
            }
            return closestIndex
        }

    private val amplitude: Float
        get() = if ((mMax - mMin) > 0) (mMax - mMin) else 1F

    val progressFloat: Float
        @Synchronized get() {
            val bigDecimal = BigDecimal.valueOf(mProgress.toDouble())
            return bigDecimal.setScale(mScale, BigDecimal.ROUND_HALF_UP).toFloat()
        }

    val progress: Int
        get() = Math.round(mProgress)

    var max: Float
        get() = mMax
        @Synchronized set(max) {
            this.mMax = Math.max(mMin, max)
            initProgressRangeValue()
            refreshSeekBarLocation()
            invalidate()
        }


    var min: Float
        get() = mMin
        @Synchronized set(min) {
            this.mMin = Math.min(mMax, min)
            initProgressRangeValue()
            refreshSeekBarLocation()
            invalidate()
        }

    @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : super(context, attrs, defStyleAttr) {
        this.mContext = context
        initAttrs(mContext, attrs)
        initParams()
    }

    internal constructor(builder: Builder) : super(builder.context) {
        this.mContext = builder.context
        val defaultPadding = SizeUtils.dp2px(mContext!!, 16F)
        setPadding(defaultPadding, paddingTop, defaultPadding, paddingBottom)
        this.apply(builder)
        initParams()
    }

    private fun initAttrs(context: Context?, attrs: AttributeSet?) {
        val builder = Builder(context!!)
        if (attrs == null) {
            this.apply(builder)
            return
        }
        attrs.let {
            val ta = context.obtainStyledAttributes(attrs, R.styleable.IndicatorSeekBar)
            //seekBar
            mMax = ta.getFloat(R.styleable.IndicatorSeekBar_tlt_max, builder.max)
            mMin = ta.getFloat(R.styleable.IndicatorSeekBar_tlt_min, builder.min)
            mProgress = ta.getFloat(R.styleable.IndicatorSeekBar_tlt_progress, builder.progress)
            mIsFloatProgress = ta.getBoolean(R.styleable.IndicatorSeekBar_tlt_progress_value_float, builder.progressValueFloat)
            mUserSeekable = ta.getBoolean(R.styleable.IndicatorSeekBar_tlt_user_seekable, builder.userSeekable)
            mClearPadding = ta.getBoolean(R.styleable.IndicatorSeekBar_tlt_clear_default_padding, builder.clearPadding)
            mOnlyThumbDraggable = ta.getBoolean(R.styleable.IndicatorSeekBar_tlt_only_thumb_draggable, builder.onlyThumbDraggable)
            mSeekSmoothly = ta.getBoolean(R.styleable.IndicatorSeekBar_tlt_seek_smoothly, builder.seekSmoothly)
            mR2L = ta.getBoolean(R.styleable.IndicatorSeekBar_tlt_r2l, builder.r2l)
            //track
            mBackgroundTrackSize = ta.getDimensionPixelSize(R.styleable.IndicatorSeekBar_tlt_track_background_size, builder.trackBackgroundSize)
            mProgressTrackSize = ta.getDimensionPixelSize(R.styleable.IndicatorSeekBar_tlt_track_progress_size, builder.trackProgressSize)
            mBackgroundTrackColor = ta.getColor(R.styleable.IndicatorSeekBar_tlt_track_background_color, builder.trackBackgroundColor)
            mProgressTrackColor = ta.getColor(R.styleable.IndicatorSeekBar_tlt_track_progress_color, builder.trackProgressColor)
            mTrackRoundedCorners = ta.getBoolean(R.styleable.IndicatorSeekBar_tlt_track_rounded_corners, builder.trackRoundedCorners)
            //thumb
            mThumbSize = ta.getDimensionPixelSize(R.styleable.IndicatorSeekBar_tlt_thumb_size, builder.thumbSize)
            mThumbDrawable = ta.getDrawable(R.styleable.IndicatorSeekBar_tlt_thumb_drawable)
            initThumbColor(ta.getColorStateList(R.styleable.IndicatorSeekBar_tlt_thumb_color), builder.thumbColor)
            //thumb text
            mShowThumbText = ta.getBoolean(R.styleable.IndicatorSeekBar_tlt_show_thumb_text, builder.showThumbText)
            mThumbTextColor = ta.getColor(R.styleable.IndicatorSeekBar_tlt_thumb_text_color, builder.thumbTextColor)
            //tickMarks
            tickCount = ta.getInt(R.styleable.IndicatorSeekBar_tlt_ticks_count, builder.tickCount)
            mTickMarksSize = ta.getDimensionPixelSize(R.styleable.IndicatorSeekBar_tlt_tick_marks_size, builder.tickMarksSize)
            mTickMarksDrawable = ta.getDrawable(R.styleable.IndicatorSeekBar_tlt_tick_marks_drawable)
            mTickMarksSweptHide = ta.getBoolean(R.styleable.IndicatorSeekBar_tlt_tick_marks_swept_hide, builder.tickMarksSweptHide)
            mTickMarksEndsHide = ta.getBoolean(R.styleable.IndicatorSeekBar_tlt_tick_marks_ends_hide, builder.tickMarksEndsHide)
            //tickTexts
            mShowTickText = ta.getBoolean(R.styleable.IndicatorSeekBar_tlt_show_tick_texts, builder.showTickText)
            mTickTextsSize = ta.getDimensionPixelSize(R.styleable.IndicatorSeekBar_tlt_tick_texts_size, builder.tickTextsSize)
            initTickTextsColor(ta.getColorStateList(R.styleable.IndicatorSeekBar_tlt_tick_texts_color), builder.tickTextsColor)
            mTickTextsCustomArray = ta.getTextArray(R.styleable.IndicatorSeekBar_tlt_tick_texts_array)
            initTextsTypeface(ta.getInt(R.styleable.IndicatorSeekBar_tlt_tick_texts_typeface, -1), builder.tickTextsTypeFace)
            //indicator
            val indicatorContentViewId = ta.getResourceId(R.styleable.IndicatorSeekBar_tlt_indicator_content_layout, 0)
            if (indicatorContentViewId > 0) {
                mIndicatorContentView = View.inflate(mContext, indicatorContentViewId, null)
            }

            ta.recycle()
        }
    }

    private fun initParams() {
        if (tickCount < 0 || tickCount > 50) {
            throw IllegalArgumentException("the Argument: TICK COUNT must be limited between 0-50, Now is $tickCount")
        }
        initProgressRangeValue()
        if (mBackgroundTrackSize > mProgressTrackSize) {
            mBackgroundTrackSize = mProgressTrackSize
        }
        if (mThumbDrawable == null) {
            mThumbRadius = mThumbSize / 2.0f
            mThumbTouchRadius = mThumbRadius * 1.2f
        } else {
            mThumbRadius = Math.min(SizeUtils.dp2px(mContext!!, THUMB_MAX_WIDTH.toFloat()), mThumbSize) / 2.0f
            mThumbTouchRadius = mThumbRadius
        }
        if (mTickMarksDrawable == null) {
            mTickRadius = mTickMarksSize / 2.0f
        } else {
            mTickRadius = Math.min(SizeUtils.dp2px(mContext!!, THUMB_MAX_WIDTH.toFloat()), mTickMarksSize) / 2.0f
        }
        mCustomDrawableMaxHeight = Math.max(mThumbTouchRadius, mTickRadius) * 2.0f
        initStrokePaint()
        measureTickTextsBonds()
        measureTickUnitTextsBonds()
        lastProgress = mProgress

        if (tickCount != 0) {
            mTickMarksX = FloatArray(tickCount)
            if (mShowTickText) {
                mTextCenterX = FloatArray(tickCount)
                mTickTextsWidth = FloatArray(tickCount)
            }
            mProgressArr = FloatArray(tickCount)
            for (i in mProgressArr!!.indices) {
                mProgressArr!![i] = mMin + i * (mMax - mMin) / if (tickCount - 1 > 0) tickCount - 1 else 1
            }

        }

        mProgressTrack = RectF()
        mBackgroundTrack = RectF()
        initDefaultPadding()
    }

    private fun initDefaultPadding() {
        if (!mClearPadding) {
            val normalPadding = SizeUtils.dp2px(mContext!!, 16F)
            if (getPaddingLeft() === 0) {
                setPadding(normalPadding, getPaddingTop(), getPaddingRight(), getPaddingBottom())
            }
            if (getPaddingRight() === 0) {
                setPadding(getPaddingLeft(), getPaddingTop(), normalPadding, getPaddingBottom())
            }
        }
    }

    private fun initProgressRangeValue() {
        if (mMax < mMin) {
            throw IllegalArgumentException("the Argument: MAX's value must be larger than MIN's.")
        }
        if (mProgress < mMin) {
            mProgress = mMin
        }
        if (mProgress > mMax) {
            mProgress = mMax
        }
    }

    private fun initStrokePaint() {
        if (mStockPaint == null) {
            mStockPaint = Paint()
        }
        if (mTrackRoundedCorners) {
            mStockPaint!!.strokeCap = Paint.Cap.ROUND
        }
        mStockPaint!!.isAntiAlias = true
        if (mBackgroundTrackSize > mProgressTrackSize) {
            mProgressTrackSize = mBackgroundTrackSize
        }
    }

    private fun measureTickTextsBonds() {
        if (needDrawText()) {
            initTextPaint()
            mTextPaint!!.typeface = mTextsTypeface
            mTextPaint!!.getTextBounds("j", 0, 1, mRect)
            mTickTextsHeight = mRect!!.height() + SizeUtils.dp2px(mContext!!, 24F)//with the gap(24dp) between tickTexts and track.
        }
    }

    private fun measureTickUnitTextsBonds() {
        if (needDrawText()) {
            initUnitTextPaint()
            mUnitTextPaint!!.typeface = mTextsTypeface
            mUnitTextPaint!!.getTextBounds("j", 0, 1, mRect)
            mTickTextsHeight = mRect!!.height() + SizeUtils.dp2px(mContext!!, 24F)
        }
    }

    private fun needDrawText(): Boolean {
        return mShowThumbText || tickCount != 0 && mShowTickText
    }

    private fun initTextPaint() {
        if (mTextPaint == null) {
            mTextPaint = TextPaint()
            mTextPaint!!.isAntiAlias = true
            mTextPaint!!.textAlign = Paint.Align.CENTER
            mTextPaint!!.textSize = mTickTextsSize.toFloat()
        }
        if (mRect == null) {
            mRect = Rect()
        }
    }

    private fun initUnitTextPaint() {
        if (mUnitTextPaint == null) {
            mUnitTextPaint = TextPaint()
            mUnitTextPaint!!.isAntiAlias = true
            mUnitTextPaint!!.textAlign = Paint.Align.CENTER
            val sizeSp = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 14f, getResources().getDisplayMetrics()).toInt()
            mUnitTextPaint!!.textSize = sizeSp.toFloat()
        }
        if (mRect == null) {
            mRect = Rect()
        }
    }

    protected override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val height = Math.round(mCustomDrawableMaxHeight + getPaddingTop() + getPaddingBottom())
        setMeasuredDimension(resolveSize(SizeUtils.dp2px(mContext!!, 170F), widthMeasureSpec), height + mTickTextsHeight)
        initSeekBarInfo()
        refreshSeekBarLocation()
    }

    private fun initSeekBarInfo() {
        mMeasuredWidth = getMeasuredWidth()
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR1) {
            mPaddingLeft = getPaddingLeft()
            mPaddingRight = getPaddingRight()
        } else {
            mPaddingLeft = getPaddingStart()
            mPaddingRight = getPaddingEnd()
        }
        mPaddingTop = getPaddingTop()
        mSeekLength = (mMeasuredWidth - mPaddingLeft - mPaddingRight).toFloat()
        mSeekBlockLength = mSeekLength / if (tickCount - 1 > 0) tickCount - 1 else 1
    }

    private fun refreshSeekBarLocation() {
        initTrackLocation()
        //init TickTexts Y Location
        if (needDrawText()) {
            mTextPaint!!.getTextBounds("j", 0, 1, mRect)
            mTickTextY = mPaddingTop + mCustomDrawableMaxHeight + Math.round(mRect!!.height() - mTextPaint!!.descent()) + SizeUtils.dp2px(mContext!!, 3F)
            mThumbTextY = mTickTextY
        }
        //init tick's X and text's X location;
        if (mTickMarksX == null) {
            return
        }
        initTextsArray()
        //adjust thumb auto,so find out the closest progress in the mProgressArr array and replace it.
        //it is not necessary to adjust thumb while count is less than 3.
        if (tickCount > 2) {
            mProgress = mProgressArr!![closestIndex]
            lastProgress = mProgress
        }
        refreshThumbCenterXByProgress(mProgress)
    }

    private fun initTextsArray() {
        if (tickCount == 0) {
            return
        }
        for (i in mTickMarksX!!.indices) {
            if (mShowTickText) {
                if (mTickTextsArr == null) {
                    mTickTextsArr = Array(tickCount) { "" }
                }
                mTickTextsArr!![i] = getTickTextByPosition(i)
                var str: String
                str = if (LocalizeManager.isThai()) {
                    mTickTextsArr!![i] + "วัน"
                } else {
                    mTickTextsArr!![i] + "Day"
                }
                mTextPaint!!.getTextBounds(str, 0, str.length, mRect)
                mTickTextsWidth!![i] = mRect!!.width().toFloat()
                mTextCenterX!![i] = mPaddingLeft + mSeekBlockLength * i
            }
            mTickMarksX!![i] = mPaddingLeft + mSeekBlockLength * i
        }
    }

    private fun initTrackLocation() {
        if (mR2L) {
            mBackgroundTrack!!.left = mPaddingLeft.toFloat()
            mBackgroundTrack!!.top = mPaddingTop + mThumbTouchRadius
            //ThumbCenterX
            mBackgroundTrack!!.right = mPaddingLeft + mSeekLength * (1.0f - (mProgress - mMin) / amplitude)
            mBackgroundTrack!!.bottom = mBackgroundTrack!!.top
            //ThumbCenterX
            mProgressTrack!!.left = mBackgroundTrack!!.right
            mProgressTrack!!.top = mBackgroundTrack!!.top
            mProgressTrack!!.right = (mMeasuredWidth - mPaddingRight).toFloat()
            mProgressTrack!!.bottom = mBackgroundTrack!!.bottom
        } else {
            mProgressTrack!!.left = mPaddingLeft.toFloat()
            mProgressTrack!!.top = mPaddingTop + mThumbTouchRadius
            //ThumbCenterX
            mProgressTrack!!.right = (mProgress - mMin) * mSeekLength / amplitude + mPaddingLeft
            mProgressTrack!!.bottom = mProgressTrack!!.top
            //ThumbCenterX
            mBackgroundTrack!!.left = mProgressTrack!!.right
            mBackgroundTrack!!.top = mProgressTrack!!.bottom
            mBackgroundTrack!!.right = (mMeasuredWidth - mPaddingRight).toFloat()
            mBackgroundTrack!!.bottom = mProgressTrack!!.bottom
        }
    }

    private fun getTickTextByPosition(index: Int): String {
        if (mTickTextsCustomArray == null) {
            return getProgressString(mProgressArr!![index])
        }
        return if (index < mTickTextsCustomArray!!.size) {
            (mTickTextsCustomArray!![index]).toString()
        } else ""
    }

    /**
     * calculate the thumb's centerX by the changing progress.
     */
    private fun refreshThumbCenterXByProgress(progress: Float) {
        //ThumbCenterX
        if (mR2L) {
            mBackgroundTrack!!.right = mPaddingLeft + mSeekLength * (1.0f - (progress - mMin) / amplitude)//ThumbCenterX
            mProgressTrack!!.left = mBackgroundTrack!!.right
        } else {
            mProgressTrack!!.right = (progress - mMin) * mSeekLength / amplitude + mPaddingLeft
            mBackgroundTrack!!.left = mProgressTrack!!.right
        }
    }

    @Synchronized
    protected override fun onDraw(canvas: Canvas) {
        drawTrack(canvas)
        drawTickTexts(canvas)
        drawTickUnitTexts(canvas)
        drawThumb(canvas)
        drawThumbText(canvas)
    }

    private fun drawTrack(canvas: Canvas) {
        //draw progress track
        mStockPaint!!.color = mProgressTrackColor
        mStockPaint!!.strokeWidth = mProgressTrackSize.toFloat()
        canvas.drawLine(mProgressTrack!!.left, mProgressTrack!!.top, mProgressTrack!!.right, mProgressTrack!!.bottom, mStockPaint!!)
        //draw BG track
        mStockPaint!!.color = mBackgroundTrackColor
        mStockPaint!!.strokeWidth = mBackgroundTrackSize.toFloat()
        canvas.drawLine(mBackgroundTrack!!.left, mBackgroundTrack!!.top, mBackgroundTrack!!.right, mBackgroundTrack!!.bottom, mStockPaint!!)
    }

    private fun drawTickTexts(canvas: Canvas) {
        if (mTickTextsArr == null) {
            return
        }
        val thumbPosFloat = thumbPosOnTickFloat
        for (i in mTickTextsArr!!.indices) {
            if (mShowBothTickTextsOnly) {
                if (i != 0 && i != mTickTextsArr!!.size - 1) {
                    continue
                }
            }
            if (i == thumbPosOnTick && i.toFloat() == thumbPosFloat) {
                mTextPaint!!.color = mHoveredTextColor
            } else if (i < thumbPosFloat) {
                mTextPaint!!.color = leftSideTickTextsColor
            } else {
                mTextPaint!!.color = rightSideTickTextsColor
            }
            var index = i
            if (mR2L) {
                index = mTickTextsArr!!.size - i - 1
            }
            if (i == 0) {
                canvas.drawText(mTickTextsArr!![index], mTextCenterX!![i] + mTickTextsWidth!![index] / 2.0f, mTickTextY + 24, mTextPaint!!)
            } else if (i == mTickTextsArr!!.size - 1) {
                canvas.drawText(mTickTextsArr!![index], mTextCenterX!![i] - mTickTextsWidth!![index] / 2.0f, mTickTextY + 24, mTextPaint!!)
            } else {
                canvas.drawText(mTickTextsArr!![index], mTextCenterX!![i], mTickTextY + 24, mTextPaint!!)
            }
        }
    }

    private fun drawTickUnitTexts(canvas: Canvas) {
        val thumbPosFloat = thumbPosOnTickFloat
        for (i in mTickTextsArr!!.indices) {
            if (mShowBothTickTextsOnly) {
                if (i != 0 && i != mTickTextsArr!!.size - 1) {
                    continue
                }
            }
            if (i == thumbPosOnTick && i.toFloat() == thumbPosFloat) {
                mUnitTextPaint!!.color = mHoveredTextColor
            } else if (i < thumbPosFloat) {
                mUnitTextPaint!!.color = leftSideTickTextsColor
            } else {
                mUnitTextPaint!!.color = rightSideTickTextsColor
            }
            var index = i
            if (mR2L) {
                index = mTickTextsArr!!.size - i - 1
            }
            if (i == 0) {
                if (mTickTextsArr!![index] != "") {
                    if (LocalizeManager.isThai()) {
                        canvas.drawText("วัน", mTextCenterX!![i] + mTickTextsWidth!![index] / 2.0f + mTickTextsWidth!![index], mTickTextY + 24, mUnitTextPaint!!)
                    } else {
                        canvas.drawText("Day", mTextCenterX!![i] + mTickTextsWidth!![index] / 2.0f + mTickTextsWidth!![index], mTickTextY + 24, mUnitTextPaint!!)
                    }
                }
            } else if (i == mTickTextsArr!!.size - 1) {
                if (mTickTextsArr!![index] != "") {
                    if (LocalizeManager.isThai()) {
                        canvas.drawText("วัน", mTextCenterX!![i], mTickTextY + 24, mUnitTextPaint!!)
                    } else {
                        canvas.drawText("Day", mTextCenterX!![i], mTickTextY + 24, mUnitTextPaint!!)
                    }
                }
            } else {
                if (mTickTextsArr!![index] != "") {
                    if (LocalizeManager.isThai()) {
                        canvas.drawText("วัน", mTextCenterX!![i] + mTickTextsWidth!![index] / 2.0f, mTickTextY + 24, mUnitTextPaint!!)
                    } else {
                        canvas.drawText("Day", mTextCenterX!![i] + mTickTextsWidth!![index] / 2.0f, mTickTextY + 24, mUnitTextPaint!!)
                    }
                }
            }
        }

    }

    private fun drawThumb(canvas: Canvas) {
        if (mHideThumb) {
            return
        }
        val thumbCenterX = thumbCenterX
        if (mThumbDrawable != null) {//check user has set thumb drawable or not.ThumbDrawable first, thumb color for later.
            if (mThumbBitmap == null || mPressedThumbBitmap == null) {
                initThumbBitmap()
            }
            if (mThumbBitmap == null || mPressedThumbBitmap == null) {
                //please check your selector drawable's format and correct.
                throw IllegalArgumentException("the format of the selector thumb drawable is wrong!")
            }
            mStockPaint!!.setAlpha(255)
            if (mIsTouching) {
                canvas.drawBitmap(mPressedThumbBitmap!!, thumbCenterX - mPressedThumbBitmap!!.width / 2.0f, mProgressTrack!!.top - mPressedThumbBitmap!!.height / 2.0f, mStockPaint!!)
            } else {
                canvas.drawBitmap(mThumbBitmap!!, thumbCenterX - mThumbBitmap!!.width / 2.0f, mProgressTrack!!.top - mThumbBitmap!!.height / 2.0f, mStockPaint!!)
            }
        } else {
            if (mIsTouching) {
                mStockPaint!!.setColor(mPressedThumbColor)
            } else {
                mStockPaint!!.setColor(mThumbColor)
            }
            canvas.drawCircle(thumbCenterX, mProgressTrack!!.top, if (mIsTouching) mThumbTouchRadius else mThumbRadius, mStockPaint!!)
        }
    }

    private fun drawThumbText(canvas: Canvas) {
        if (!mShowThumbText || mShowTickText && tickCount > 2) {
            return
        }
        mTextPaint!!.color = mThumbTextColor
        canvas.drawText(getProgressString(mProgress), thumbCenterX, mThumbTextY, mTextPaint!!)
    }

    private fun getHeightByRatio(drawable: Drawable, width: Int): Int {
        val intrinsicWidth = drawable.intrinsicWidth
        val intrinsicHeight = drawable.intrinsicHeight
        return Math.round(1.0f * width.toFloat() * intrinsicHeight.toFloat() / intrinsicWidth)
    }

    private fun getDrawBitmap(drawable: Drawable?, isThumb: Boolean): Bitmap? {
        if (drawable == null) {
            return null
        }
        var width: Int
        var height: Int
        val maxRange = SizeUtils.dp2px(mContext!!, THUMB_MAX_WIDTH.toFloat())
        val intrinsicWidth = drawable.intrinsicWidth
        if (intrinsicWidth > maxRange) {
            if (isThumb) {
                width = mThumbSize
            } else {
                width = mTickMarksSize
            }
            height = getHeightByRatio(drawable, width)

            if (width > maxRange) {
                width = maxRange
                height = getHeightByRatio(drawable, width)
            }
        } else {
            width = drawable.intrinsicWidth
            height = drawable.intrinsicHeight
        }

        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight())
        drawable.draw(canvas)
        return bitmap
    }

    private fun initThumbColor(colorStateList: ColorStateList?, defaultColor: Int) {
        //if you didn't set the thumb color, set a default color.
        if (colorStateList == null) {
            mThumbColor = defaultColor
            mPressedThumbColor = mThumbColor
            return
        }
        var states: Array<IntArray>? = null
        var colors: IntArray? = null
        val aClass = colorStateList.javaClass
        try {
            val f = aClass.declaredFields
            for (field in f) {
                field.isAccessible = true
                if ("mStateSpecs" == field.name) {
                    states = field.get(colorStateList) as Array<IntArray>
                }
                if ("mColors" == field.name) {
                    colors = field.get(colorStateList) as IntArray
                }
            }
            if (states == null || colors == null) {
                return
            }
        } catch (e: Exception) {
            throw RuntimeException("Something wrong happened when parseing thumb selector color.")
        }

        if (states.size == 1) {
            mThumbColor = colors[0]
            mPressedThumbColor = mThumbColor
        } else if (states.size == 2) {
            for (i in states.indices) {
                val attr = states[i]
                if (attr.size == 0) {//didn't have state,so just get color.
                    mPressedThumbColor = colors[i]
                    continue
                }
                when (attr[0]) {
                    android.R.attr.state_pressed -> mThumbColor = colors[i]
                    else ->
                        //the color selector file was set by a wrong format , please see above to correct.
                        throw IllegalArgumentException("the selector color file you set for the argument: isb_thumb_color is in wrong format.")
                }
            }
        } else {
            //the color selector file was set by a wrong format , please see above to correct.
            throw IllegalArgumentException("the selector color file you set for the argument: isb_thumb_color is in wrong format.")
        }

    }

    private fun initTickTextsColor(colorStateList: ColorStateList?, defaultColor: Int) {
        //if you didn't set the tick's texts color, will be set a selector color file default.
        if (colorStateList == null) {
            mUnselectedTextsColor = defaultColor
            mSelectedTextsColor = mUnselectedTextsColor
            mHoveredTextColor = mUnselectedTextsColor
            return
        }
        var states: Array<IntArray>? = null
        var colors: IntArray? = null
        val aClass = colorStateList.javaClass
        try {
            val f = aClass.declaredFields
            for (field in f) {
                field.isAccessible = true
                if ("mStateSpecs" == field.name) {
                    states = field.get(colorStateList) as Array<IntArray>
                }
                if ("mColors" == field.name) {
                    colors = field.get(colorStateList) as IntArray
                }
            }
            if (states == null || colors == null) {
                return
            }
        } catch (e: Exception) {
            throw RuntimeException("Something wrong happened when parseing thumb selector color.")
        }

        if (states.size == 1) {
            mUnselectedTextsColor = colors[0]
            mSelectedTextsColor = mUnselectedTextsColor
            mHoveredTextColor = mUnselectedTextsColor
        } else if (states.size == 3) {
            for (i in states.indices) {
                val attr = states[i]
                if (attr.isEmpty()) {//didn't have state,so just get color.
                    mUnselectedTextsColor = colors[i]
                    continue
                }
                when (attr[0]) {
                    android.R.attr.state_selected -> mSelectedTextsColor = colors[i]
                    android.R.attr.state_hovered -> mHoveredTextColor = colors[i]
                    else ->
                        //the color selector file was set by a wrong format , please see above to correct.
                        throw IllegalArgumentException("the selector color file you set for the argument: isb_tick_texts_color is in wrong format.")
                }
            }
        } else {
            //the color selector file was set by a wrong format , please see above to correct.
            throw IllegalArgumentException("the selector color file you set for the argument: isb_tick_texts_color is in wrong format.")
        }
    }

    private fun initTextsTypeface(typeface: Int, defaultTypeface: Typeface?) {
        when (typeface) {
            0 -> mTextsTypeface = Typeface.DEFAULT
            1 -> mTextsTypeface = Typeface.MONOSPACE
            2 -> mTextsTypeface = Typeface.SANS_SERIF
            3 -> mTextsTypeface = Typeface.SERIF
            else -> {
                if (defaultTypeface == null) {
                    mTextsTypeface = Typeface.DEFAULT
                }
                mTextsTypeface = defaultTypeface
            }
        }
    }

    private fun initThumbBitmap() {
        if (mThumbDrawable == null) {
            return
        }
        when (mThumbDrawable) {
            is BitmapDrawable -> {
                mThumbBitmap = getDrawBitmap(mThumbDrawable, true)
                mPressedThumbBitmap = mThumbBitmap
            }
            is StateListDrawable -> try {
                val listDrawable = mThumbDrawable as StateListDrawable?
                val aClass = listDrawable!!.javaClass
                val stateCount = aClass.getMethod("getStateCount").invoke(listDrawable) as Int
                if (stateCount == 2) {
                    val getStateSet = aClass.getMethod("getStateSet", Int::class.javaPrimitiveType)
                    val getStateDrawable = aClass.getMethod("getStateDrawable", Int::class.javaPrimitiveType)
                    for (i in 0 until stateCount) {
                        val stateSet = getStateSet.invoke(listDrawable, i) as IntArray
                        if (stateSet.isNotEmpty()) {
                            if (stateSet[0] == android.R.attr.state_pressed) {
                                val stateDrawable = getStateDrawable.invoke(listDrawable, i) as Drawable
                                mPressedThumbBitmap = getDrawBitmap(stateDrawable, true)
                            } else {
                                //please check your selector drawable's format, please see above to correct.
                                throw IllegalArgumentException("the state of the selector thumb drawable is wrong!")
                            }
                        } else {
                            val stateDrawable = getStateDrawable.invoke(listDrawable, i) as Drawable
                            mThumbBitmap = getDrawBitmap(stateDrawable, true)
                        }
                    }
                } else {
                    //please check your selector drawable's format, please see above to correct.
                    throw IllegalArgumentException("the format of the selector thumb drawable is wrong!")
                }
            } catch (e: Exception) {
                throw RuntimeException("Something wrong happened when parsing thumb selector drawable." + e.message)
            }
            else -> //please check your selector drawable's format, please see above to correct.
                throw IllegalArgumentException("Nonsupport this drawable's type for custom thumb drawable!")
        }
    }

    private fun initTickMarksBitmap() {
        when (mTickMarksDrawable) {
            is BitmapDrawable -> {
                mUnselectTickMarksBitmap = getDrawBitmap(mTickMarksDrawable, false)
                mSelectTickMarksBitmap = mUnselectTickMarksBitmap
            }
            is StateListDrawable -> {
                val listDrawable = mTickMarksDrawable as StateListDrawable?
                try {
                    val aClass = listDrawable!!.javaClass
                    val getStateCount = aClass.getMethod("getStateCount")
                    val stateCount = getStateCount.invoke(listDrawable) as Int
                    if (stateCount == 2) {
                        val getStateSet = aClass.getMethod("getStateSet", Int::class.javaPrimitiveType)
                        val getStateDrawable = aClass.getMethod("getStateDrawable", Int::class.javaPrimitiveType)
                        for (i in 0 until stateCount) {
                            val stateSet = getStateSet.invoke(listDrawable, i) as IntArray
                            if (stateSet.isNotEmpty()) {
                                if (stateSet[0] == android.R.attr.state_selected) {
                                    val stateDrawable = getStateDrawable.invoke(listDrawable, i) as Drawable
                                    mSelectTickMarksBitmap = getDrawBitmap(stateDrawable, false)
                                } else {
                                    //please check your selector drawable's format, please see above to correct.
                                    throw IllegalArgumentException("the state of the selector TickMarks drawable is wrong!")
                                }
                            } else {
                                val stateDrawable = getStateDrawable.invoke(listDrawable, i) as Drawable
                                mUnselectTickMarksBitmap = getDrawBitmap(stateDrawable, false)
                            }
                        }
                    } else {
                        //please check your selector drawable's format, please see above to correct.
                        throw IllegalArgumentException("the format of the selector TickMarks drawable is wrong!")
                    }
                } catch (e: Exception) {
                    throw RuntimeException("Something wrong happened when parsing TickMarks selector drawable." + e.message)
                }

            }
            else -> //please check your selector drawable's format, please see above to correct.
                throw IllegalArgumentException("Nonsupport this drawable's type for custom TickMarks drawable!")
        }
    }

    override fun setEnabled(enabled: Boolean) {
        if (enabled == isEnabled) {
            return
        }
        super.setEnabled(enabled)
        alpha = if (isEnabled) {
            1.0f
        } else {
            0.3f
        }
    }

    protected override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        post(Runnable { requestLayout() })
    }

    override fun dispatchTouchEvent(event: MotionEvent): Boolean {
        val parent = parent ?: return super.dispatchTouchEvent(event)
        when (event.action) {
            MotionEvent.ACTION_DOWN -> parent.requestDisallowInterceptTouchEvent(true)
            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> parent.requestDisallowInterceptTouchEvent(false)
        }
        return super.dispatchTouchEvent(event)
    }

    override fun performClick(): Boolean {
        return super.performClick()
    }

    protected override fun onSaveInstanceState(): Parcelable {
        val bundle = Bundle()
        bundle.putParcelable("isb_instance_state", super.onSaveInstanceState())
        bundle.putFloat("isb_progress", mProgress)
        return bundle
    }

    protected override fun onRestoreInstanceState(state: Parcelable) {
        if (state is Bundle) {
            setProgress(state.getFloat("isb_progress"))
            super.onRestoreInstanceState(state.getParcelable<Parcelable>("isb_instance_state"))
            return
        }
        super.onRestoreInstanceState(state)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (!mUserSeekable || !isEnabled) {
            return false
        }
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                performClick()
                val mX = event.x
                if (isTouchSeekBar(mX, event.y)) {
                    if (mOnlyThumbDraggable && !isTouchThumb(mX)) {
                        return false
                    }
                    mIsTouching = true
                    if (onSeekChangeListener != null) {
                        onSeekChangeListener!!.onStartTrackingTouch(this)
                    }
                    refreshSeekBar(event)
                    return true
                }
            }
            MotionEvent.ACTION_MOVE -> refreshSeekBar(event)
            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                mIsTouching = false
                if (onSeekChangeListener != null) {
                    onSeekChangeListener!!.onStopTrackingTouch(this)
                }
                if (!autoAdjustThumb()) {
                    invalidate()
                }
            }
        }
        return super.onTouchEvent(event)
    }

    private fun refreshSeekBar(event: MotionEvent) {
        refreshThumbCenterXByProgress(calculateProgress(calculateTouchX(adjustTouchX(event))))
        setSeekListener(true)
        invalidate()
    }

    private fun progressChange(): Boolean {
        return if (mIsFloatProgress) {
            lastProgress != mProgress
        } else {
            Math.round(lastProgress) != Math.round(mProgress)
        }
    }

    private fun adjustTouchX(event: MotionEvent): Float {
        val mTouchXCache: Float
        if (event.x < mPaddingLeft) {
            mTouchXCache = mPaddingLeft.toFloat()
        } else if (event.x > mMeasuredWidth - mPaddingRight) {
            mTouchXCache = (mMeasuredWidth - mPaddingRight).toFloat()
        } else {
            mTouchXCache = event.x
        }
        return mTouchXCache
    }

    private fun calculateProgress(touchX: Float): Float {
        lastProgress = mProgress
        mProgress = mMin + amplitude * (touchX - mPaddingLeft) / mSeekLength
        return mProgress
    }

    private fun calculateTouchX(touchX: Float): Float {
        var touchXTemp = touchX
        //make sure the seek bar to seek smoothly always
        // while the tick's count is less than 3(tick's count is 1 or 2.).
        if (tickCount > 2 && !mSeekSmoothly) {
            val touchBlockSize = Math.round((touchX - mPaddingLeft) / mSeekBlockLength)
            touchXTemp = mSeekBlockLength * touchBlockSize + mPaddingLeft
        }
        return if (mR2L) {
            mSeekLength - touchXTemp + 2 * mPaddingLeft
        } else touchXTemp
    }

    private fun isTouchSeekBar(mX: Float, mY: Float): Boolean {
        if (mFaultTolerance == -1f) {
            mFaultTolerance = SizeUtils.dp2px(mContext!!, 5F).toFloat()
        }
        val inWidthRange = mX >= mPaddingLeft - 2 * mFaultTolerance && mX <= mMeasuredWidth - mPaddingRight + 2 * mFaultTolerance
        val inHeightRange = mY >= mProgressTrack!!.top - mThumbTouchRadius - mFaultTolerance && mY <= mProgressTrack!!.top + mThumbTouchRadius + mFaultTolerance
        return inWidthRange && inHeightRange
    }

    private fun isTouchThumb(mX: Float): Boolean {
        val rawTouchX: Float
        refreshThumbCenterXByProgress(mProgress)
        if (mR2L) {
            rawTouchX = mBackgroundTrack!!.right
        } else {
            rawTouchX = mProgressTrack!!.right
        }
        return rawTouchX - mThumbSize / 2f <= mX && mX <= rawTouchX + mThumbSize / 2f
    }

    private fun autoAdjustThumb(): Boolean {
        if (tickCount < 3 || !mSeekSmoothly) {//it is not necessary to adjust while count less than 3 .
            return false
        }
        val closestIndex = closestIndex
        val touchUpProgress = mProgress
        val animator = ValueAnimator.ofFloat(0F, Math.abs(touchUpProgress - mProgressArr!![closestIndex]))
        animator.start()
        animator.addUpdateListener { animation ->
            lastProgress = mProgress
            if (touchUpProgress - mProgressArr!![closestIndex] > 0) {
                mProgress = touchUpProgress - animation.animatedValue as Float
            } else {
                mProgress = touchUpProgress + animation.animatedValue as Float
            }
            refreshThumbCenterXByProgress(mProgress)
            //the auto adjust was happened after user touched up, so from user is false.
            setSeekListener(false)
            invalidate()
        }
        return true
    }

    /**
     * transfer the progress value to string type
     */
    private fun getProgressString(progress: Float): String {
        val progressString: String
        if (mIsFloatProgress) {
            progressString = (BigDecimal.valueOf(progress.toDouble()).setScale(mScale, BigDecimal.ROUND_HALF_UP).toFloat()).toString()
        } else {
            progressString = Math.round(progress).toString()
        }
        return progressString
    }

    private fun getIndexFromProgress(progress: Float): Int {
        var closestIndex = 0
        var amplitude = Math.abs(mMax - mMin)
        for (i in mProgressArr!!.indices) {
            if (mTickTextsArr!![i] != "") {
                val amplitudeTemp = Math.abs(mProgressArr!![i] - progress)
                if (amplitudeTemp <= amplitude) {
                    amplitude = amplitudeTemp
                    closestIndex = i
                }
            }
        }
        return closestIndex
    }

    private fun setSeekListener(formUser: Boolean) {
        if (onSeekChangeListener == null) {
            return
        }
        if (progressChange()) {
            onSeekChangeListener!!.onSeeking(collectParams(formUser))
        }
    }

    private fun collectParams(formUser: Boolean): SeekParams {
        if (mSeekParams == null) {
            mSeekParams = SeekParams(this)
        }
        mSeekParams!!.progress = progress
        mSeekParams!!.progressFloat = progressFloat
        mSeekParams!!.fromUser = formUser
        //for discrete series seek bar
        if (tickCount > 2) {
            val rawThumbPos = thumbPosOnTick
            if (mShowTickText && mTickTextsArr != null) {
                mSeekParams!!.tickText = mTickTextsArr!![rawThumbPos]
            }
            if (mR2L) {
                mSeekParams!!.thumbPosition = tickCount - rawThumbPos - 1
            } else {
                mSeekParams!!.thumbPosition = rawThumbPos
            }
        }
        return mSeekParams!!
    }

    private fun apply(builder: Builder) {
        //seek bar
        this.mMax = builder.max
        this.mMin = builder.min
        this.mProgress = builder.progress
        this.mIsFloatProgress = builder.progressValueFloat
        this.tickCount = builder.tickCount
        this.mSeekSmoothly = builder.seekSmoothly
        this.mR2L = builder.r2l
        this.mUserSeekable = builder.userSeekable
        this.mClearPadding = builder.clearPadding
        this.mOnlyThumbDraggable = builder.onlyThumbDraggable
        //indicator
        this.mIndicatorContentView = builder.indicatorContentView
        //track
        this.mBackgroundTrackSize = builder.trackBackgroundSize
        this.mBackgroundTrackColor = builder.trackBackgroundColor
        this.mProgressTrackSize = builder.trackProgressSize
        this.mProgressTrackColor = builder.trackProgressColor
        this.mTrackRoundedCorners = builder.trackRoundedCorners
        //thumb
        this.mThumbSize = builder.thumbSize
        this.mThumbDrawable = builder.thumbDrawable
        this.mThumbTextColor = builder.thumbTextColor
        initThumbColor(builder.thumbColorStateList, builder.thumbColor)
        this.mShowThumbText = builder.showThumbText
        //tickMarks
        this.mTickMarksSize = builder.tickMarksSize
        this.mTickMarksDrawable = builder.tickMarksDrawable
        this.mTickMarksEndsHide = builder.tickMarksEndsHide
        this.mTickMarksSweptHide = builder.tickMarksSweptHide
        //tickTexts
        this.mShowTickText = builder.showTickText
        this.mTickTextsSize = builder.tickTextsSize
        this.mTickTextsCustomArray = builder.tickTextsCustomArray.map {
            it as CharSequence
        }.toTypedArray()
        this.mTextsTypeface = builder.tickTextsTypeFace
        initTickTextsColor(builder.tickTextsColorStateList, builder.tickTextsColor)
    }

    @Synchronized
    fun setProgress(progress: Float) {
        lastProgress = mProgress
        mProgress = if (progress < mMin) mMin else if (progress > mMax) mMax else progress
        //        adjust to the closest tick's progress
        if (tickCount > 2) {
            mProgress = mProgressArr!![closestIndex]
        }

        setSeekListener(false)
        refreshThumbCenterXByProgress(mProgress)
        postInvalidate()
    }

    /**
     * compat app local change
     *
     * @param isR2L True if see form right to left on the screen.
     */
    fun setR2L(isR2L: Boolean) {
        this.mR2L = isR2L
        requestLayout()
        invalidate()
    }

    fun setThumbDrawable(drawable: Drawable?) {
        if (drawable == null) {
            this.mThumbDrawable = null
            this.mThumbBitmap = null
            this.mPressedThumbBitmap = null
        } else {
            this.mThumbDrawable = drawable
            this.mThumbRadius = Math.min(SizeUtils.dp2px(mContext!!, THUMB_MAX_WIDTH.toFloat()), mThumbSize) / 2.0f
            this.mThumbTouchRadius = mThumbRadius
            this.mCustomDrawableMaxHeight = Math.max(mThumbTouchRadius, mTickRadius) * 2.0f
            initThumbBitmap()
        }
        requestLayout()
        invalidate()
    }

    /**
     * call this will do not draw thumb, true if hide.
     */
    fun hideThumb(hide: Boolean) {
        mHideThumb = hide
        invalidate()
    }

    /**
     * call this will do not draw the text which below thumb. true if hide.
     */
    fun hideThumbText(hide: Boolean) {
        mShowThumbText = !hide
        invalidate()
    }

    /**
     * set the seek bar's thumb's color.
     *
     * @param thumbColor colorInt
     */
    fun thumbColor(@ColorInt thumbColor: Int) {
        this.mThumbColor = thumbColor
        this.mPressedThumbColor = thumbColor
        invalidate()
    }

    /**
     * set the color for text below/above seek bar's tickText.
     *
     * @param tickTextsColor ColorInt
     */
    fun tickTextsColor(@ColorInt tickTextsColor: Int) {
        mUnselectedTextsColor = tickTextsColor
        mSelectedTextsColor = tickTextsColor
        mHoveredTextColor = tickTextsColor
        invalidate()
    }

    fun customTickTexts(tickTextsArr: Array<String>) {
        this.mTickTextsCustomArray = tickTextsArr.map {
            it as CharSequence
        }.toTypedArray()
        if (mTickTextsArr != null) {
            for (i in mTickTextsArr!!.indices) {
                var tickText: String
                if (i < tickTextsArr.size) {
                    tickText = tickTextsArr[i]
                } else {
                    tickText = ""
                }
                var index = i
                if (mR2L) {
                    index = tickCount - 1 - i
                }
                mTickTextsArr!![index] = tickText
                if (mTextPaint != null && mRect != null) {
                    tickText = if (LocalizeManager.isThai()) {
                        "$tickText วัน"
                    } else {
                        "$tickText Day"
                    }
                    mTextPaint!!.getTextBounds(tickText, 0, tickText.length, mRect)
                    mTickTextsWidth!![index] = mRect!!.width().toFloat()
                }
            }
            invalidate()
        }
    }

    fun customTickTextsTypeface(typeface: Typeface) {
        this.mTextsTypeface = typeface
        measureTickTextsBonds()
        measureTickUnitTextsBonds()
        requestLayout()
        invalidate()
    }

    fun setOnSeekChangeListener(listener: OnSeekChangeListener) {
        this.onSeekChangeListener = listener
    }

    companion object {

        private val THUMB_MAX_WIDTH = 30
        private val FORMAT_PROGRESS = "\${PROGRESS}"
        private val FORMAT_TICK_TEXT = "\${TICK_TEXT}"

        /*------------------API START-------------------*/

        fun with(context: Context): Builder {
            return Builder(context)
        }
    }


    /*------------------API END-------------------*/

}