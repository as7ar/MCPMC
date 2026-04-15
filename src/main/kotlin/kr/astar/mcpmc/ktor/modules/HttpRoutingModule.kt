package kr.astar.mcpmc.ktor.modules

import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.http.*
import kotlinx.serialization.json.*
import kr.astar.mcpmc.MCPMC
import io.modelcontextprotocol.kotlin.sdk.server.Server
import io.modelcontextprotocol.kotlin.sdk.server.ServerOptions
import io.modelcontextprotocol.kotlin.sdk.server.mcpStreamableHttp
import io.modelcontextprotocol.kotlin.sdk.types.*
import kr.astar.mcpmc.tools.MCPMCToolRegistry
import kr.astar.mcpmc.tools.registeredTools
import kr.astar.mcpmc.tools.toRegisteredTool

fun Application.configureRouting() {
    val authName = getAuthName()

    routing {
        authenticate(authName) {
            mcpStreamableHttp {
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

        get("/status") {
            call.respond(buildJsonObject { put("status", JsonPrimitive("ok")) })
        }

        get("/callback") {
            val principal = call.principal<OAuthAccessTokenResponse.OAuth2>()
            if (principal != null) {
                call.respondText("Authentication successful! Token: ${principal.accessToken}")
            } else {
                call.respondText("Authentication failed", status = HttpStatusCode.Unauthorized)
            }
        }

        get("/") {
            call.respond(buildJsonObject {
                put("code", JsonPrimitive(200))
                put("data", buildJsonObject {
                    put("name", JsonPrimitive(MCPMC.plugin.pluginMeta.name))
                    put("version", JsonPrimitive(MCPMC.plugin.pluginMeta.version))
                    put("tools", buildJsonArray {
                        // MCPMC.tools.forEach { add(JsonPrimitive(it.tool.name)) }
                        MCPMCToolRegistry.getAll().map { it.toRegisteredTool() }.forEach { add(JsonPrimitive(it.tool.name)) }
                    })
                })
            })
        }
    }
}