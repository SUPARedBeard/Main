package com.jpitts.backlogtracker.data

data class GameResponse(
    val results: List<Game>
)

data class Game(
    val id: Int,
    val name: String,
    val background_image: String?,
    val platforms: List<PlatformWrapper>?
)

data class PlatformWrapper(
    val platform: Platform
)

data class Platform(
    val id: Int,
    val name: String,
    val slug: String
)