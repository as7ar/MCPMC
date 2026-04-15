package kr.astar.mcpmc.ktor.modules

import io.ktor.server.application.*
import io.modelcontextprotocol.kotlin.sdk.server.Server
import io.modelcontextprotocol.kotlin.sdk.server.ServerOptions
import io.modelcontextprotocol.kotlin.sdk.server.mcpWebSocket
import io.modelcontextprotocol.kotlin.sdk.types.*
import kr.astar.mcpmc.MCPMC
import kr.astar.mcpmc.tools.MCPMCToolRegistry
import kr.astar.mcpmc.tools.registeredTools
import kr.astar.mcpmc.tools.toRegisteredTool

fun Application.configureWebSocket() {
    mcpWebSocket {
        Server(
            Implementation("MCPMC", MCPMC.plugin.pluginMeta.version),
            ServerOptions(
                ServerCapabilities(
                    tools = ServerCapabilities.Tools(listChanged = true),
                    logging = if (MCPMC.plugin.config.getBoolean("enable-log", false))
                        ServerCapabilities.Logging else null
                )
            )
        ) {
            addTools(registeredTools)
        }
    }
}