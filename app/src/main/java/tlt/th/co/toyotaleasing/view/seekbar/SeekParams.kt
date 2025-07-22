package tlt.th.co.toyotaleasing.view.seekbar

class SeekParams(private val seekBar: IndicatorSeekBar) {

    var progress: Int = 0

    var progressFloat: Float = 0.toFloat()

    var fromUser: Boolean = false

    var thumbPosition: Int = 0

    var tickText: String? = null

}