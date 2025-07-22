//package tlt.th.co.toyotaleasing.modules.custloan.utils
//
//import android.content.Context
//import android.media.AudioAttributes
//import android.media.AudioManager
//import android.media.MediaMetadataRetriever
//import android.media.SoundPool
//import android.net.Uri
//import android.os.Build
//import java.util.HashMap
//import tlt.th.co.toyotaleasing.R
//
//
//
//object SoundPlayUtils {
//
//    val WATCH_CAMERA = 1
//    val BLINK_EYE = 2
//    val DETECTION_MUlTI_FACE = 3
//    val DETECTION_BLUR = 4
//    val DETECTION_BRIGHT = 5
//    val DETECTION_DARK = 6
//    val DETECTION_FAR = 7
//    val DETECTION_NEAR = 8
//    val DETECTION_ILLEGAL = 9
//    //    public static final int DETECTION_SUCCESS = 10;
//    val LEFT_HEAD = 10
//    val RIGHT_HEAD = 11
//    val LOWER_HEAD = 12
//    val UPPER_HEAD = 13
//    val OPEN_MOUTH = 14
//    val SHAKE_HEAD = 15
//    val TIME_OUT = 16
//    //    public static final int DONE = 17;
//
//
//    // SoundPool对象
//    var mSoundPlayer: SoundPool? = null
//    var soundPlayUtils: SoundPlayUtils? = null
//    // 上下文
//    internal var mContext: Context? = null
//    var lastSoundId: Int = 0
//    private val mPlayDurationMap = HashMap<Int, Long> ()
//    private var mCurrentMillis: Long = 0
//    private var mLastMills: Long = 0
//    private var soundDuration: Long = 0
//
//    /**
//     * initialization
//     *
//     * @param context
//     */
//    fun init(context: Context): SoundPlayUtils {
//        if (soundPlayUtils == null) {
//            soundPlayUtils = this
//        }
//
//        if (Build.VERSION.SDK_INT >= 21) {
//            val builder = SoundPool.Builder()
//            //The amount of incoming audio
//            builder.setMaxStreams(1)
//            //AudioAttributes is a class that encapsulates various properties of audio.
//            val attrBuilder = AudioAttributes.Builder()
//            //Set the appropriate properties for the audio stream
//            attrBuilder.setLegacyStreamType(AudioManager.STREAM_MUSIC)
//            builder.setAudioAttributes(attrBuilder.build())
//            mSoundPlayer = builder.build()
//        } else {
//            //The first parameter is the number of sounds that can be supported, the second is the sound type, and the third is the sound quality.
//            mSoundPlayer = SoundPool(1, AudioManager.STREAM_SYSTEM, 5)
//        }
//
//        // Initialization sound
//        mContext = context
//        mSoundPlayer?.load(mContext,R.raw.detection_forward, WATCH_CAMERA)
//        mSoundPlayer?.load(mContext, R.raw.blink_eye, BLINK_EYE)
//        mSoundPlayer?.load(mContext, R.raw.detection_multi_face, DETECTION_MUlTI_FACE)
//        mSoundPlayer?.load(mContext, R.raw.detection_blurness, DETECTION_BLUR)
//        mSoundPlayer?.load(mContext, R.raw.detection_bright, DETECTION_BRIGHT)
//        mSoundPlayer?.load(mContext, R.raw.detection_dark, DETECTION_DARK)
//        mSoundPlayer?.load(mContext, R.raw.detection_far, DETECTION_FAR)
//        mSoundPlayer?.load(mContext, R.raw.detection_near, DETECTION_NEAR)
//        mSoundPlayer?.load(mContext, R.raw.detection_unlive, DETECTION_ILLEGAL)
//        mSoundPlayer?.load(mContext, R.raw.left_head, LEFT_HEAD)
//        mSoundPlayer?.load(mContext, R.raw.right_head, RIGHT_HEAD)
//        mSoundPlayer?.load(mContext, R.raw.lower_head, LOWER_HEAD)
//        mSoundPlayer?.load(mContext, R.raw.upper_head, UPPER_HEAD)
//        mSoundPlayer?.load(mContext, R.raw.open_mouth, OPEN_MOUTH)
//        mSoundPlayer?.load(mContext, R.raw.shake_head, SHAKE_HEAD)
//        mSoundPlayer?.load(mContext, R.raw.time_out, TIME_OUT)
//
//        return this
//    }
//
//    /**
//     * Play sound
//     *
//     * @param soundID
//     */
//    fun play(soundID: Int) {
//        mCurrentMillis = System.currentTimeMillis()
//        if (mCurrentMillis - mLastMills > soundDuration) {
//            soundDuration = getSoundDuration(soundID)
//            mLastMills = System.currentTimeMillis()
//            if (lastSoundId != soundID) {
//                mSoundPlayer?.play(soundID, 1f, 1f, 0, 0, 1f)
//            }
//            lastSoundId = soundID
//        }
//    }
//
//
//    private fun getSoundDuration(soundId: Int): Long {
//        var rawId = 0
//        when (soundId) {
//            WATCH_CAMERA -> rawId = R.raw.detection_forward
//            BLINK_EYE -> rawId = R.raw.blink_eye
//            DETECTION_BLUR -> rawId = R.raw.detection_blurness
//            DETECTION_BRIGHT -> rawId = R.raw.detection_bright
//            DETECTION_DARK -> rawId = R.raw.detection_dark
//            DETECTION_FAR -> rawId = R.raw.detection_far
//            DETECTION_NEAR -> rawId = R.raw.detection_near
//            LOWER_HEAD -> rawId = R.raw.lower_head
//            UPPER_HEAD -> rawId = R.raw.upper_head
//            DETECTION_ILLEGAL -> rawId = R.raw.detection_unlive
//            TIME_OUT -> rawId = R.raw.time_out
//            else -> {
//            }
//        }
//        var duration = 600L
//        val durationStep = 0L
//        if (mPlayDurationMap.containsKey(Integer.valueOf(rawId))) {
//            duration = (mPlayDurationMap.get(Integer.valueOf(rawId)) as Long).toLong()
//        } else {
//            val time = System.currentTimeMillis()
//            val mmr = MediaMetadataRetriever()
//
//            try {
//                val uri = Uri.parse("android.resource://" + mContext!!.packageName + "/" + rawId)
//                mmr.setDataSource(mContext, uri)
//                val d = mmr.extractMetadata(9)
//                duration = java.lang.Long.valueOf(d).toLong() + durationStep
//                mPlayDurationMap.put(Integer.valueOf(rawId), java.lang.Long.valueOf(duration))
//            } catch (var11: IllegalArgumentException) {
//                var11.printStackTrace()
//            } catch (var12: IllegalStateException) {
//                var12.printStackTrace()
//            } catch (var13: Exception) {
//                var13.printStackTrace()
//            }
//
//        }
//
//        return duration
//    }
//
//}
