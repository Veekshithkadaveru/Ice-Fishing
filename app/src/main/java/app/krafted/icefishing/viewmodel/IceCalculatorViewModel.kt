package app.krafted.icefishing.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

enum class IceType {
    CLEAR_BLUE,
    WHITE_OPAQUE,
    GREY
}

enum class WarningLevel {
    DANGER,
    CAUTION,
    SAFE,
    VERY_SAFE
}

data class IceSafetyResult(
    val warningLevel: WarningLevel,
    val label: String,
    val advice: String,
    val effectiveThicknessCm: Int
)

data class IceCalculatorUiState(
    val thicknessCm: Int = 15,
    val iceType: IceType = IceType.CLEAR_BLUE,
    val result: IceSafetyResult = calculateSafety(15, IceType.CLEAR_BLUE)
)

fun calculateSafety(thicknessCm: Int, iceType: IceType): IceSafetyResult {
    val effectiveCm = when (iceType) {
        IceType.CLEAR_BLUE -> thicknessCm
        IceType.WHITE_OPAQUE -> (thicknessCm * 0.5).toInt()
        IceType.GREY -> 0
    }

    return when {
        iceType == IceType.GREY -> IceSafetyResult(
            warningLevel = WarningLevel.DANGER,
            label = "DANGER",
            advice = "Grey ice is thawing. Exit immediately.",
            effectiveThicknessCm = effectiveCm
        )

        effectiveCm < 5 -> IceSafetyResult(
            warningLevel = WarningLevel.DANGER,
            label = "DANGER",
            advice = "Not safe for any activity.",
            effectiveThicknessCm = effectiveCm
        )

        effectiveCm < 8 -> IceSafetyResult(
            warningLevel = WarningLevel.CAUTION,
            label = "CAUTION",
            advice = "One person on foot only. Test every step.",
            effectiveThicknessCm = effectiveCm
        )

        effectiveCm < 12 -> IceSafetyResult(
            warningLevel = WarningLevel.CAUTION,
            label = "CAUTION",
            advice = "Single person with light gear.",
            effectiveThicknessCm = effectiveCm
        )

        effectiveCm < 20 -> IceSafetyResult(
            warningLevel = WarningLevel.SAFE,
            label = "SAFE",
            advice = "Group of anglers on foot.",
            effectiveThicknessCm = effectiveCm
        )

        effectiveCm < 30 -> IceSafetyResult(
            warningLevel = WarningLevel.SAFE,
            label = "SAFE",
            advice = "Group + snowmobile.",
            effectiveThicknessCm = effectiveCm
        )

        else -> IceSafetyResult(
            warningLevel = WarningLevel.VERY_SAFE,
            label = "VERY SAFE",
            advice = "Light vehicle (ATV/small truck).",
            effectiveThicknessCm = effectiveCm
        )
    }
}

class IceCalculatorViewModel : ViewModel() {
    private val _state = MutableStateFlow(IceCalculatorUiState())
    val state: StateFlow<IceCalculatorUiState> = _state.asStateFlow()

    fun setThickness(thicknessCm: Int) {
        val current = _state.value
        val nextThickness = thicknessCm.coerceIn(1, 60)
        _state.value = current.copy(
            thicknessCm = nextThickness,
            result = calculateSafety(nextThickness, current.iceType)
        )
    }

    fun setIceType(iceType: IceType) {
        val current = _state.value
        _state.value = current.copy(
            iceType = iceType,
            result = calculateSafety(current.thicknessCm, iceType)
        )
    }
}
