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
        /*registeredToolGenerator(
            "announce", "broadcast a message to all player",
            mapOf("content" to SchemaType.STRING)
        ) { req->
            "broadcasted to online players (${
                Bukkit.broadcast(MiniMessage.miniMessage().deserialize(
                    req.getParam("content") ?: ""
                ))
            })".toToolResult()
        },

        registeredToolGenerator(
            "give_item", "give an item to player",
            mapOf(
                "player" to SchemaType.STRING,
                "itemid" to SchemaType.STRING,
                "amount" to SchemaType.INTEGER
            )
        ) { req->
            val player = Bukkit.getPlayer(
                req.getParam("player") ?: return@registeredToolGenerator "cant found player, is player that have the name online?".toToolResult()
            )
            val item = Material.getMaterial(req.getParam("itemid") ?: "")
                ?: return@registeredToolGenerator "item not found".toToolResult()
            val amount = req.getParam("amount")?.toIntOrNull() ?: 0

            var playerItemResult: PlayerGiveResult?= null

            Bukkit.getScheduler().runTask(MCPMC.plugin, Runnable {
               playerItemResult=  player?.give(ItemStack(item, amount))
            })

            "item given to player. result: $playerItemResult".toToolResult()
        }*/
    }

    override fun onDisable() {
        // Plugin shutdown logic
    }
}
