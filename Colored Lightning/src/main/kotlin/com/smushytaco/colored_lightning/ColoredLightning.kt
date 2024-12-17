package com.smushytaco.colored_lightning
import net.fabricmc.api.ClientModInitializer
object ColoredLightning : ClientModInitializer {
    const val MOD_ID = "colored_lightning"
    val config = ModConfig.createAndLoad()
    override fun onInitializeClient() {}
}