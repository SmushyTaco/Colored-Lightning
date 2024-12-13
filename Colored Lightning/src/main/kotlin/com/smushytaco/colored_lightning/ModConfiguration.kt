package com.smushytaco.colored_lightning
import io.wispforest.owo.config.annotation.*
@Modmenu(modId = ColoredLightning.MOD_ID)
@Config(name = ColoredLightning.MOD_ID, wrapperName = "ModConfig")
@Suppress("UNUSED")
class ModConfiguration {
    @JvmField
    var enableColoredLightning = true
    @JvmField
    var changeColorForEachSegment = false
}