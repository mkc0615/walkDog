package walkdog.api.domain.dogs.model

enum class DogBreed(val code: String, val description: String) {
    BORDER_COLIE("BorderColie", "Border Colie"),
    COCKER_SPANIEL("CockerSpaniel", "Cocker Spaniel"),
    YORKSHIRE_TERRIER("YorkshireTerrier", "Yorkshire_Terrier");

    companion object {
        fun fromCode(code: String): DogBreed? {
            return entries.find { it.code.equals(code, ignoreCase = true) }
        }
    }
}
