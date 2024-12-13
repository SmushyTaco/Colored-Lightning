package com.smushytaco.colored_lightning
import kotlinx.coroutines.*
import net.fabricmc.api.ClientModInitializer
import java.util.*
object ColoredLightning : ClientModInitializer {
    const val MOD_ID = "colored_lightning"
    val config = ModConfig.createAndLoad()
    override fun onInitializeClient() {}
}