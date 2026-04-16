package kr.astar.mcpmc.application.modules

import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.routing.*
import io.modelcontextprotocol.kotlin.sdk.server.Server
import io.modelcontextprotocol.kotlin.sdk.server.ServerOptions
import io.modelcontextprotocol.kotlin.sdk.server.mcpStreamableHttp
import io.modelcontextprotocol.kotlin.sdk.server.mcpWebSocket
import io.modelcontextprotocol.kotlin.sdk.types.Implementation
import io.modelcontextprotocol.kotlin.sdk.types.ServerCapabilities
import kr.astar.mcpmc.MCPMC
import kr.astar.mcpmc.application.getAuthName

fun Application.mcpStreaming() {
    val authName = getAuthName()

    val serverInfo = Implementation("MCPMC", MCPMC.plugin.pluginMeta.version)
    val serverOptions = ServerOptions(ServerCapabilities(
        tools = ServerCapabilities.Tools(listChanged = true),
        logging = if (MCPMC.plugin.config.getBoolean("enable-log", false))
            ServerCapabilities.Logging else null
    ))

    mcpWebSocket {
        Server(serverInfo, serverOptions) { addTools(MCPMC.tools) }
    }

    routing { authenticate(authName) {
        mcpStreamableHttp {
            Server(serverInfo, serverOptions) { addTools(MCPMC.tools) }
        }
    } }
}