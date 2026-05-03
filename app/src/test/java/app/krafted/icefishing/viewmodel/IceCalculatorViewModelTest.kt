package app.krafted.icefishing.viewmodel

import org.junit.Assert.assertEquals
import org.junit.Test

class IceCalculatorViewModelTest {
    @Test
    fun greyIceAlwaysReturnsDanger() {
        listOf(1, 30, 60).forEach { thickness ->
            val result = calculateSafety(thickness, IceType.GREY)

            assertEquals(WarningLevel.DANGER, result.warningLevel)
            assertEquals("DANGER", result.label)
            assertEquals("Grey ice is thawing. Exit immediately.", result.advice)
            assertEquals(0, result.effectiveThicknessCm)
        }
    }

    @Test
    fun clearBlueIceUsesGoldenThresholds() {
        val cases = listOf(
            ThresholdCase(4, WarningLevel.DANGER, "DANGER", "Not safe for any activity."),
            ThresholdCase(5, WarningLevel.CAUTION, "CAUTION", "One person on foot only. Test every step."),
            ThresholdCase(7, WarningLevel.CAUTION, "CAUTION", "One person on foot only. Test every step."),
            ThresholdCase(8, WarningLevel.CAUTION, "CAUTION", "Single person with light gear."),
            ThresholdCase(11, WarningLevel.CAUTION, "CAUTION", "Single person with light gear."),
            ThresholdCase(12, WarningLevel.SAFE, "SAFE", "Group of anglers on foot."),
            ThresholdCase(19, WarningLevel.SAFE, "SAFE", "Group of anglers on foot."),
            ThresholdCase(20, WarningLevel.SAFE, "SAFE", "Group + snowmobile."),
            ThresholdCase(29, WarningLevel.SAFE, "SAFE", "Group + snowmobile."),
            ThresholdCase(30, WarningLevel.VERY_SAFE, "VERY SAFE", "Light vehicle (ATV/small truck).")
        )

        cases.forEach { case ->
            val result = calculateSafety(case.thicknessCm, IceType.CLEAR_BLUE)

            assertEquals(case.warningLevel, result.warningLevel)
            assertEquals(case.label, result.label)
            assertEquals(case.advice, result.advice)
            assertEquals(case.thicknessCm, result.effectiveThicknessCm)
        }
    }

    @Test
    fun whiteOpaqueIceUsesFlooredHalfThickness() {
        val fifteenCm = calculateSafety(15, IceType.WHITE_OPAQUE)
        val twentyFourCm = calculateSafety(24, IceType.WHITE_OPAQUE)

        assertEquals(7, fifteenCm.effectiveThicknessCm)
        assertEquals(WarningLevel.CAUTION, fifteenCm.warningLevel)
        assertEquals("One person on foot only. Test every step.", fifteenCm.advice)

        assertEquals(12, twentyFourCm.effectiveThicknessCm)
        assertEquals(WarningLevel.SAFE, twentyFourCm.warningLevel)
        assertEquals("Group of anglers on foot.", twentyFourCm.advice)
    }

    private data class ThresholdCase(
        val thicknessCm: Int,
        val warningLevel: WarningLevel,
        val label: String,
        val advice: String
    )
}
