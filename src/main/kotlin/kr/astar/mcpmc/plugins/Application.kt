package kr.astar.mcpmc.plugins

import freemarker.cache.ClassTemplateLoader
import io.ktor.server.application.*
import io.ktor.server.freemarker.*
import io.ktor.server.http.content.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.modelcontextprotocol.kotlin.sdk.server.Server
import io.modelcontextprotocol.kotlin.sdk.server.ServerOptions
import io.modelcontextprotocol.kotlin.sdk.server.mcp
import io.modelcontextprotocol.kotlin.sdk.types.Implementation
import io.modelcontextprotocol.kotlin.sdk.types.ServerCapabilities
import kotlinx.serialization.json.buildJsonObject
import kr.astar.mcpmc.MCPMC

private val main = MCPMC.plugin
fun Application.module() {
    install(FreeMarker) {
        templateLoader = ClassTemplateLoader(this::class.java.classLoader, "templates")
    }

    mcp {
        Server(Implementation("MCPMC", main.pluginMeta.version),
            ServerOptions(ServerCapabilities(
                tools = ServerCapabilities.Tools(listChanged = true),
                resources = ServerCapabilities.Resources(listChanged = true, subscribe = true),
            )),
        ) {
            addTools(MCPMC.tools)
        }
    }

    routing {
        staticFiles("/static", main.dataFolder)

        get("/status") {
            call.respondText(buildJsonObject { "status" to "ok" }.toString())
        }

        get("/") {
            call.respondText(buildJsonObject {
                "code" to 200
                "data" to buildJsonObject {
                    "name" to main.pluginMeta.name
                    "version" to main.pluginMeta.version
                    "tools" to MCPMC.tools.map { it.tool.name }
                }
            }.toString())
        }
    }
}