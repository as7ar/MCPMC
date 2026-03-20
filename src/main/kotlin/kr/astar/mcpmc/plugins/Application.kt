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
import kotlinx.serialization.json.buildJsonArray
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import kr.astar.mcpmc.MCPMC
import javax.swing.UIManager.put

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

        route("/tools") {
            get("/list") {
                call.respond(buildJsonArray {
                    MCPMC.tools.forEach {
                        val tool = it.tool

                        add(buildJsonObject {
                            put("name", tool.name)
                            put("description", tool.description)

                            put("parameters", buildJsonObject {
                                tool.inputSchema.properties?.forEach { (key, value) ->
                                    put(key, buildJsonObject {
                                        put("type", value.jsonObject["type"])
                                    })
                                }
                            })
                        })
                    }
                })
            }

            get("/{id}") {
                val id = call.parameters["id"]
                    ?: return@get call.respondText(
                        "Tool ID required", status = io.ktor.http.HttpStatusCode.BadRequest
                    )

                val tool = MCPMC.tools.find { it.tool.name == id }
                    ?: return@get call.respondText(
                        "Tool not found", status = io.ktor.http.HttpStatusCode.NotFound
                    )

                call.respond(buildJsonObject {
                    put("name", tool.tool.name)
                    put("description", tool.tool.description)

                    put("parameters", buildJsonObject {
                        tool.tool.inputSchema.properties?.forEach { (key, value) ->
                            put(key, buildJsonObject {
                                put("type", value.jsonObject["type"])
                            })
                        }
                    })
                })
            }
        }
    }
}