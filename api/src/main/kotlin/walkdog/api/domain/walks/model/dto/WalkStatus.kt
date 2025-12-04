package walkdog.api.domain.walks.model.dto

enum class WalkStatus(code: String, description: String) {
    STARTED("STARTED", "start walk"),
    PAUSED("PAUSED", "pause walk"),
    FINISHED("FINISHED", "finish walk");
}