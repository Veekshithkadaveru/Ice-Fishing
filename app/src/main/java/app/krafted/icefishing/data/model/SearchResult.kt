package app.krafted.icefishing.data.model

data class SearchResult(
    val type: ContentType,
    val title: String,
    val preview: String,
    val id: String // Can be Int ID as string, or actual String ID depending on the feature
)
