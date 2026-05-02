package app.krafted.icefishing.ui.assets

import androidx.annotation.DrawableRes
import app.krafted.icefishing.R

object IceFishAssets {
    @DrawableRes
    fun resolve(key: String): Int = when (key) {
        "icefish_sym_1" -> R.drawable.icefish_sym_1
        "icefish_sym_2" -> R.drawable.icefish_sym_2
        "icefish_sym_3" -> R.drawable.icefish_sym_3
        "icefish_sym_4" -> R.drawable.icefish_sym_4
        "icefish_sym_5" -> R.drawable.icefish_sym_5
        "icefish_sym_6" -> R.drawable.icefish_sym_6
        "icefish_sym_7" -> R.drawable.icefish_sym_7
        "icefish_back_1" -> R.drawable.icefish_back_1
        "icefish_back_2" -> R.drawable.icefish_back_2
        "icefish_back_3" -> R.drawable.icefish_back_3
        "icefish_back_4" -> R.drawable.icefish_back_4
        "icefish_back_5" -> R.drawable.icefish_back_5
        else -> R.drawable.icefish_placeholder
    }
}
