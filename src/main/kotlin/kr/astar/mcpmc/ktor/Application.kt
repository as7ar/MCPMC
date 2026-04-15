package kr.astar.mcpmc.ktor

import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.plugins.contentnegotiation.*
import io.modelcontextprotocol.kotlin.sdk.types.McpJson
import kr.astar.mcpmc.ktor.modules.configureAuth
import kr.astar.mcpmc.ktor.modules.configureRouting
import kr.astar.mcpmc.ktor.modules.configureWebSocket
import kr.astar.mcpmc.ktor.modules.registerTools

fun Application.module() {
    install(ContentNegotiation) {
        json(McpJson)
    }

    configureAuth()
    configureWebSocket()
    configureRouting()

    registerTools()
}