package kr.astar.mcp_test

import io.modelcontextprotocol.kotlin.sdk.types.CallToolResult
import io.modelcontextprotocol.kotlin.sdk.types.TextContent
import kotlinx.serialization.json.buildJsonObject
import kr.astar.mcpmc.MCPMC
import kr.astar.mcpmc.schema.SchemaType
import kr.astar.mcpmc.utils.getParam
import kr.astar.mcpmc.utils.registeredToolGenerator
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.minimessage.MiniMessage
import org.bukkit.Bukkit
import org.bukkit.plugin.java.JavaPlugin

class Mcp_test : JavaPlugin() {

    override fun onEnable() {
        MCPMC.addTool(registeredToolGenerator(
            "announce", "broadcast a message to all player",
            mapOf("content" to SchemaType.STRING)
        ) { req->
            CallToolResult(listOf(TextContent("broadcasted to online players (${
                Bukkit.broadcast(MiniMessage.miniMessage().deserialize(
                    req.getParam("content") ?: ""
                ))
            })")))
        })
    }

    override fun onDisable() {
        // Plugin shutdown logic
    }
}
