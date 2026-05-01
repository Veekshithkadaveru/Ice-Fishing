package app.krafted.icefishing.data.model

data class SpeciesSeason(
    val peakMonths: List<String>,
    val behavior: String,
    val tips: List<String>
)

data class Species(
    val id: Int,
    val name: String,
    val symbol: String,
    val background: String,
    val accentColor: String,
    val heroFact: String,
    val habitat: List<String>,
    val baitAndTackle: List<String>,
    val season: SpeciesSeason
)
