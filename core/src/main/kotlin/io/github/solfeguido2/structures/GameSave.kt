package io.github.solfeguido2.structures

import io.github.solfeguido2.settings.GameSettings
import kotlinx.serialization.Serializable
import java.time.ZonedDateTime

@Serializable
data class GameSave(
    var settings: GameSettings,
    var result: GameStats,
    @Serializable(with = ZonedDateTimeSerializer::class)
    var date: ZonedDateTime = ZonedDateTime.now()
)