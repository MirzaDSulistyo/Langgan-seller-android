package id.langgan.android.seller.utility

import java.util.*

class ColorGenerator private constructor(private val mColors: List<Int>) {
    private val mRandom: Random = Random(System.currentTimeMillis())

    val randomColor: Int
        get() = mColors[mRandom.nextInt(mColors.size)]

    fun getColor(key: Any): Int {
        return mColors[Math.abs(key.hashCode()) % mColors.size]
    }

    fun getColor(position: Int): Int {
        return mColors[position % mColors.size]
    }

    companion object {

        private var DEFAULT: ColorGenerator
        var STORE: ColorGenerator
        private var WARNING: ColorGenerator

        init {
            DEFAULT = create(
                Arrays.asList(
                -0xe9c9c,
                -0xa7aa7,
                -0x65bc2,
                -0x1b39d2,
                -0x98408c,
                -0xa65d42,
                -0xdf6c33,
                -0x529d59,
                -0x7fa87f
            ))
            STORE = create(Arrays.asList(
                -0xbb7501,
                -0xde690d,
                -0xe1771b,
                -0xe6892e,
                -0xea9a40,
                -0xbb7501,
                -0xde690d,
                -0xe1771b,
                -0xe6892e,
                -0xea9a40
            ))
            WARNING = create(Arrays.asList(
                -0x3fbfc4,
                -0x15bac2,
                -0x11999d,
                -0x3fbfc4,
                -0x15bac2,
                -0x11999d,
                -0x3fbfc4,
                -0x15bac2,
                -0x11999d,
                -0x3fbfc4
            ))
        }

        fun create(colorList: List<Int>): ColorGenerator {
            return ColorGenerator(colorList)
        }
    }
}