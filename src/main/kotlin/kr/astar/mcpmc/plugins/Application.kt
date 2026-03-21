package kr.astar.mcpmc.plugins

import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.http.content.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.modelcontextprotocol.kotlin.sdk.server.Server
import io.modelcontextprotocol.kotlin.sdk.server.ServerOptions
import io.modelcontextprotocol.kotlin.sdk.server.mcpStreamableHttp
import io.modelcontextprotocol.kotlin.sdk.types.*
import io.papermc.paper.entity.PlayerGiveResult
import kotlinx.serialization.json.*
import kr.astar.mcpmc.MCPMC
import kr.astar.mcpmc.schema.SchemaType
import kr.astar.mcpmc.utils.*
import net.kyori.adventure.text.minimessage.MiniMessage
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.inventory.ItemStack

private val main = MCPMC.plugin

fun Application.module() {

    install(ContentNegotiation) {
        json(McpJson)
    }

    MCPMC.addTools(listOf(
        registeredToolGenerator(
            name = "tool",
            description = "Get tool by Name",
            param = mapOf("name" to SchemaType.STRING)
        ) { req ->

            val tool = MCPMC.tools.find {
                it.tool.name == req.getParam("name")
            }

            if (tool == null) {
                return@registeredToolGenerator CallToolResult(
                    listOf(TextContent("tool not found"))
                )
            }

            CallToolResult(
                listOf(TextContent(tool.infoJson().also { println(it) }.toString()))
            )
        },
        registeredToolGenerator(
            name = "tools",
            description = "Get tools list",
            param = emptyMap()
        ) {
            MCPMC.tools.joinToString { it.tool.name }.toToolResult()
        },

        // TEST TOOLS
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
    ))

    mcpStreamableHttp {
        Server(
            Implementation("MCPMC", main.pluginMeta.version),
            ServerOptions(
                ServerCapabilities(
                    tools = ServerCapabilities.Tools(listChanged = true),
//                    resources = ServerCapabilities.Resources(listChanged = true, subscribe = true),
                    logging = if (MCPMC.plugin.config.getBoolean("enable-log", false)) ServerCapabilities.Logging else null
                )
            )
        ) {
            addTools(MCPMC.tools)
        }
    }

    routing {
//        staticFiles("/static", main.dataFolder)

        get("/status") {
            call.respond(
                buildJsonObject {
                    put("status", JsonPrimitive("ok"))
                }
            )
        }

        get("/") {
            call.respond(
                buildJsonObject {
                    put("code", JsonPrimitive(200))
                    put("data", buildJsonObject {
                        put("name", JsonPrimitive(main.pluginMeta.name))
                        put("version", JsonPrimitive(main.pluginMeta.version))
                        put(
                            "tools",
                            buildJsonArray {
                                MCPMC.tools.forEach {
                                    add(JsonPrimitive(it.tool.name))
                                }
                            }
                        )
                    })
                }
            )
        }
    }
}