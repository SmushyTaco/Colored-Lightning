package com.smushytaco.colored_lightning
import io.wispforest.owo.config.annotation.Config
import io.wispforest.owo.config.annotation.Modmenu
@Modmenu(modId = ColoredLightning.MOD_ID)
@Config(name = ColoredLightning.MOD_ID, wrapperName = "ModConfig")
@Suppress("UNUSED")
class ModConfiguration {
    @JvmField
    var enableColoredLightning = true
    @JvmField
    var changeColorForEachSegment = false
}