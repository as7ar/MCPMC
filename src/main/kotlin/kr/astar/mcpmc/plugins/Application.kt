package kr.astar.mcpmc.plugins

import io.ktor.server.application.*
import io.ktor.server.http.content.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.modelcontextprotocol.kotlin.sdk.server.Server
import io.modelcontextprotocol.kotlin.sdk.server.ServerOptions
import io.modelcontextprotocol.kotlin.sdk.server.mcpStreamableHttp
import io.modelcontextprotocol.kotlin.sdk.types.Implementation
import io.modelcontextprotocol.kotlin.sdk.types.ServerCapabilities
import kotlinx.serialization.json.buildJsonArray
import kotlinx.serialization.json.buildJsonObject
import kr.astar.mcpmc.MCPMC
import kr.astar.mcpmc.utils.Response
import kr.astar.mcpmc.utils.infoJson

private val main = MCPMC.plugin
fun Application.module() {
    /*install(FreeMarker) {
        templateLoader = ClassTemplateLoader(this::class.java.classLoader, "templates")
    }*/

    mcpStreamableHttp {
        Server(Implementation("MCPMC", main.pluginMeta.version),
            ServerOptions(ServerCapabilities(
                tools = ServerCapabilities.Tools(listChanged = true),
                resources = ServerCapabilities.Resources(listChanged = true, subscribe = true),
                logging = ServerCapabilities.Logging
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

        route("/tools") {
            get("/list") {
                call.respond(Response(
                    200,
                    buildJsonArray {
                        MCPMC.tools.forEach { add(it.infoJson()) }
                    }
                ))
            }

            get("/id/{id}") {
                val id = call.parameters["id"]
                    ?: return@get call.respondText(
                        "Tool ID required", status = io.ktor.http.HttpStatusCode.BadRequest
                    )

                val tool = MCPMC.tools.find { it.tool.name == id }
                    ?: return@get call.respondText(
                        "Tool not found", status = io.ktor.http.HttpStatusCode.NotFound
                    )

                call.respond(Response(200, tool.infoJson()))
            }
        }
    }
}